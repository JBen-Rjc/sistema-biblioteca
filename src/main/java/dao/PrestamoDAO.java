package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.DetallePrestamo;
import modelo.Ejemplar;
import modelo.Lector;
import modelo.Libro;
import modelo.Prestamo;

public class PrestamoDAO {

    public boolean registrarPrestamoTransaccional(Prestamo p) throws DAOException {
        Connection con = null;
        PreparedStatement psPrestamo = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psActualizarEjemplar = null;
        ResultSet rs = null;

        String sqlPrestamo = "INSERT INTO prestamo (fecha_devolucion_esperada, estado, id_lector, id_usuario) "
                           + "VALUES (?, ?, ?, ?) RETURNING id_prestamo";

        String sqlDetalle = "INSERT INTO detalle_prestamo (id_prestamo, id_ejemplar, observacion_entrega, devuelto) "
                          + "VALUES (?, ?, ?, FALSE)";

        String sqlActualizarEjemplar = "UPDATE ejemplar SET disponible = FALSE WHERE id_ejemplar = ? AND disponible = TRUE";

        try {
            con = Conexion.getInstancia().getConexion();
            con.setAutoCommit(false); // Iniciar Transacción

            // 1. Insertar Cabecera de Préstamo
            psPrestamo = con.prepareStatement(sqlPrestamo);
            
            // Conversión de java.util.Date a java.sql.Date
            psPrestamo.setDate(1, new java.sql.Date(p.getFechaDevolucionEsperada().getTime()));
            
            psPrestamo.setString(2, "Pendiente");
            psPrestamo.setInt(3, p.getLector().getIdLector());
            psPrestamo.setInt(4, p.getUsuario().getIdUsuario());

            rs = psPrestamo.executeQuery();
            int idPrestamoGenerado = 0;
            if (rs.next()) {
                idPrestamoGenerado = rs.getInt(1);
            } else {
                throw new SQLException("No se pudo generar el ID del préstamo.");
            }

            // PreparedStatements para el detalle y la actualización de ejemplares
            psDetalle = con.prepareStatement(sqlDetalle);
            psActualizarEjemplar = con.prepareStatement(sqlActualizarEjemplar);

            // 2. Iterar cada ítem del detalle
            for (DetallePrestamo det : p.getDetalles()) {
                // Actualizar estado del ejemplar a no disponible
                psActualizarEjemplar.setInt(1, det.getEjemplar().getIdEjemplar());
                int filasAfectadas = psActualizarEjemplar.executeUpdate();

                if (filasAfectadas == 0) {
                    throw new SQLException("El ejemplar con código '" + det.getEjemplar().getCodigoPatrimonial() 
                                         + "' ya no se encuentra disponible.");
                }

                // Insertar detalle
                psDetalle.setInt(1, idPrestamoGenerado);
                psDetalle.setInt(2, det.getEjemplar().getIdEjemplar());
                psDetalle.setString(3, det.getObservacionEntrega() != null ? det.getObservacionEntrega() : "En buen estado");
                psDetalle.executeUpdate();
            }

            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Deshacer cambios si ocurre un error
                } catch (SQLException ex) {
                    System.err.println("Error en Rollback: " + ex.getMessage());
                }
            }
            throw new DAOException("Error transaccional al registrar préstamo: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (psPrestamo != null) psPrestamo.close();
                if (psDetalle != null) psDetalle.close();
                if (psActualizarEjemplar != null) psActualizarEjemplar.close();
                if (con != null) con.setAutoCommit(true); // Restaurar auto-commit
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    public List<Prestamo> buscarPrestamosPendientesPorDni(String dni) throws DAOException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, p.fecha_prestamo, p.fecha_devolucion_esperada, p.monto_multa, p.estado, "
                   + "l.id_lector, l.dni, l.nombres, l.apellidos "
                   + "FROM prestamo p "
                   + "INNER JOIN lector l ON p.id_lector = l.id_lector "
                   + "WHERE l.dni = ? AND p.estado IN ('Pendiente', 'Con Retraso') "
                   + "ORDER BY p.fecha_prestamo DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lector lec = new Lector();
                    lec.setIdLector(rs.getInt("id_lector"));
                    lec.setDni(rs.getString("dni"));
                    lec.setNombres(rs.getString("nombres"));
                    lec.setApellidos(rs.getString("apellidos"));

                    Prestamo p = new Prestamo();
                    p.setIdPrestamo(rs.getInt("id_prestamo"));
                    p.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
                    p.setFechaDevolucionEsperada(rs.getDate("fecha_devolucion_esperada"));
                    p.setEstado(rs.getString("estado"));
                    p.setLector(lec);

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar préstamos pendientes: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<DetallePrestamo> listarDetallesPorPrestamo(int idPrestamo) throws DAOException {
        List<DetallePrestamo> lista = new ArrayList<>();
        String sql = "SELECT d.id_detalle, d.observacion_entrega, d.devuelto, "
                   + "e.id_ejemplar, e.codigo_patrimonial, e.ubicacion, e.estado_conservacion, "
                   + "l.titulo "
                   + "FROM detalle_prestamo d "
                   + "INNER JOIN ejemplar e ON d.id_ejemplar = e.id_ejemplar "
                   + "INNER JOIN libro l ON e.id_libro = l.id_libro "
                   + "WHERE d.id_prestamo = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro lib = new Libro();
                    lib.setTitulo(rs.getString("titulo"));

                    Ejemplar ej = new Ejemplar();
                    ej.setIdEjemplar(rs.getInt("id_ejemplar"));
                    ej.setCodigoPatrimonial(rs.getString("codigo_patrimonial"));
                    ej.setUbicacion(rs.getString("ubicacion"));
                    ej.setEstadoConservacion(rs.getString("estado_conservacion"));
                    ej.setLibro(lib);

                    DetallePrestamo det = new DetallePrestamo();
                    det.setIdDetalle(rs.getInt("id_detalle"));
                    det.setObservacionEntrega(rs.getString("observacion_entrega"));
                    det.setDevuelto(rs.getBoolean("devuelto"));
                    det.setEjemplar(ej);

                    lista.add(det);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar detalles del préstamo: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean registrarDevolucionTransaccional(int idPrestamo, double montoMulta, String nuevoEstado) throws DAOException {
        Connection con = null;
        PreparedStatement psPrestamo = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psEjemplar = null;

        String sqlPrestamo = "UPDATE prestamo SET fecha_devolucion_real = CURRENT_DATE, monto_multa = ?, estado = ? "
                           + "WHERE id_prestamo = ?";
        String sqlDetalle = "UPDATE detalle_prestamo SET devuelto = TRUE WHERE id_prestamo = ?";
        String sqlEjemplar = "UPDATE ejemplar SET disponible = TRUE WHERE id_ejemplar IN ("
                           + "SELECT id_ejemplar FROM detalle_prestamo WHERE id_prestamo = ?)";

        try {
            con = Conexion.getInstancia().getConexion();
            con.setAutoCommit(false); // Iniciar Transacción

            // 1. Actualizar Prestamo
            psPrestamo = con.prepareStatement(sqlPrestamo);
            psPrestamo.setDouble(1, montoMulta);
            psPrestamo.setString(2, nuevoEstado);
            psPrestamo.setInt(3, idPrestamo);
            psPrestamo.executeUpdate();

            // 2. Marcar detalles como devueltos
            psDetalle = con.prepareStatement(sqlDetalle);
            psDetalle.setInt(1, idPrestamo);
            psDetalle.executeUpdate();

            // 3. Liberar ejemplares
            psEjemplar = con.prepareStatement(sqlEjemplar);
            psEjemplar.setInt(1, idPrestamo);
            psEjemplar.executeUpdate();

            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {}
            }
            throw new DAOException("Error transaccional al procesar la devolución: " + e.getMessage(), e);
        } finally {
            try {
                if (psPrestamo != null) psPrestamo.close();
                if (psDetalle != null) psDetalle.close();
                if (psEjemplar != null) psEjemplar.close();
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {}
        }
    }
}