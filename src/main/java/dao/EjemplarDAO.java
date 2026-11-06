package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Ejemplar;
import modelo.Libro;

public class EjemplarDAO {

    public void registrar(Ejemplar ej) throws DAOException {
        String sql = "INSERT INTO ejemplar (codigo_patrimonial, estado_conservacion, ubicacion, disponible, id_libro) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ej.getCodigoPatrimonial());
            ps.setString(2, ej.getEstadoConservacion());
            ps.setString(3, ej.getUbicacion());
            ps.setBoolean(4, ej.isDisponible());
            ps.setInt(5, ej.getLibro().getIdLibro());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DAOException("El código patrimonial '" + ej.getCodigoPatrimonial() + "' ya existe.", e);
            }
            throw new DAOException("Error al registrar ejemplar: " + e.getMessage(), e);
        }
    }

    public List<Ejemplar> listarPorLibro(int idLibro) throws DAOException {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.id_ejemplar, e.codigo_patrimonial, e.estado_conservacion, e.ubicacion, e.disponible, "
                + "l.id_libro, l.titulo "
                + "FROM ejemplar e "
                + "INNER JOIN libro l ON e.id_libro = l.id_libro "
                + "WHERE e.id_libro = ? "
                + "ORDER BY e.codigo_patrimonial ASC";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idLibro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setIdLibro(rs.getInt("id_libro"));
                    libro.setTitulo(rs.getString("titulo"));

                    Ejemplar ej = new Ejemplar();
                    ej.setIdEjemplar(rs.getInt("id_ejemplar"));
                    ej.setCodigoPatrimonial(rs.getString("codigo_patrimonial"));
                    ej.setEstadoConservacion(rs.getString("estado_conservacion"));
                    ej.setUbicacion(rs.getString("ubicacion"));
                    ej.setDisponible(rs.getBoolean("disponible"));
                    ej.setLibro(libro);

                    lista.add(ej);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar ejemplares del libro: " + e.getMessage(), e);
        }
        return lista;
    }

    public Ejemplar buscarPorCodigoPatrimonial(String codigo) throws DAOException {
        String sql = "SELECT e.id_ejemplar, e.codigo_patrimonial, e.estado_conservacion, e.ubicacion, e.disponible, "
                + "l.id_libro, l.titulo "
                + "FROM ejemplar e "
                + "INNER JOIN libro l ON e.id_libro = l.id_libro "
                + "WHERE e.codigo_patrimonial = ?";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Libro l = new Libro();
                    l.setIdLibro(rs.getInt("id_libro"));
                    l.setTitulo(rs.getString("titulo"));

                    Ejemplar ej = new Ejemplar();
                    ej.setIdEjemplar(rs.getInt("id_ejemplar"));
                    ej.setCodigoPatrimonial(rs.getString("codigo_patrimonial"));
                    ej.setEstadoConservacion(rs.getString("estado_conservacion"));
                    ej.setUbicacion(rs.getString("ubicacion"));
                    ej.setDisponible(rs.getBoolean("disponible"));
                    ej.setLibro(l);

                    return ej;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar ejemplar por código: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Ejemplar> listarDisponibles(String criterio) throws DAOException {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.id_ejemplar, e.codigo_patrimonial, e.estado_conservacion, e.ubicacion, e.disponible, "
                + "l.id_libro, l.titulo "
                + "FROM ejemplar e "
                + "INNER JOIN libro l ON e.id_libro = l.id_libro "
                + "WHERE e.disponible = TRUE "
                + "AND (LOWER(e.codigo_patrimonial) LIKE ? OR LOWER(l.titulo) LIKE ?) "
                + "ORDER BY l.titulo ASC";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + (criterio != null ? criterio.trim().toLowerCase() : "") + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro l = new Libro();
                    l.setIdLibro(rs.getInt("id_libro"));
                    l.setTitulo(rs.getString("titulo"));

                    Ejemplar ej = new Ejemplar();
                    ej.setIdEjemplar(rs.getInt("id_ejemplar"));
                    ej.setCodigoPatrimonial(rs.getString("codigo_patrimonial"));
                    ej.setEstadoConservacion(rs.getString("estado_conservacion"));
                    ej.setUbicacion(rs.getString("ubicacion"));
                    ej.setDisponible(rs.getBoolean("disponible"));
                    ej.setLibro(l);

                    lista.add(ej);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar ejemplares disponibles: " + e.getMessage(), e);
        }
        return lista;
    }
}
