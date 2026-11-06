package negocio;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReporteService {

    // Estilos inline estandarizados para tablas HTML
    private static final String ESTILO_TABLA = "width:100%; border-collapse:collapse; margin-top:15px;";
    private static final String ESTILO_TH = "background-color:#2980B9; color:#FFFFFF; font-weight:bold; padding:8px; text-align:left; border:1px solid #2980B9;";
    private static final String ESTILO_TD = "border:1px solid #BDC3C7; padding:6px; font-size:12px;";

    // REPORTE 1: Historial de Préstamos por Fechas
    public static String generarHtmlPrestamosPorFechas(String fechaInicio, String fechaFin) throws DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='text-align:center; color:#2C3E50;'>HISTORIAL DE PRÉSTAMOS POR FECHAS</h2>");
        sb.append("<p style='text-align:center; color:#7F8C8D;'>Rango: ").append(fechaInicio).append(" al ").append(fechaFin).append("</p>");
        sb.append("<table style='").append(ESTILO_TABLA).append("'><thead><tr>")
          .append("<th style='").append(ESTILO_TH).append("'>ID</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Préstamo</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Límite</th>")
          .append("<th style='").append(ESTILO_TH).append("'>DNI</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Lector</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Estado</th>")
          .append("</tr></thead><tbody>");

        String sql = "SELECT p.id_prestamo, p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado, "
                   + "l.dni, (l.nombres || ' ' || l.apellidos) AS lector "
                   + "FROM prestamo p INNER JOIN lector l ON p.id_lector = l.id_lector "
                   + "WHERE DATE(p.fecha_prestamo) BETWEEN ?::date AND ?::date "
                   + "ORDER BY p.fecha_prestamo DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("<tr>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getInt("id_prestamo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_prestamo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_devolucion_esperada")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("dni")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("lector")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("estado")).append("</td>")
                      .append("</tr>");
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error al consultar préstamos por fechas: " + e.getMessage(), e);
        }

        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }

    // REPORTE 2: Devoluciones y Multas Liquidadas
    public static String generarHtmlDevolucionesMultas(String fechaInicio, String fechaFin) throws DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='text-align:center; color:#2C3E50;'>REGISTRO DE DEVOLUCIONES Y MULTAS LIQUIDADAS</h2>");
        sb.append("<p style='text-align:center; color:#7F8C8D;'>Rango: ").append(fechaInicio).append(" al ").append(fechaFin).append("</p>");
        sb.append("<table style='").append(ESTILO_TABLA).append("'><thead><tr>")
          .append("<th style='").append(ESTILO_TH).append("'>ID</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Préstamo</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Dev. Real</th>")
          .append("<th style='").append(ESTILO_TH).append("'>DNI</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Lector</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Multa (S/)</th>")
          .append("</tr></thead><tbody>");

        String sql = "SELECT p.id_prestamo, p.fecha_prestamo, p.fecha_devolucion_real, p.monto_multa, "
                   + "l.dni, (l.nombres || ' ' || l.apellidos) AS lector "
                   + "FROM prestamo p INNER JOIN lector l ON p.id_lector = l.id_lector "
                   + "WHERE p.fecha_devolucion_real IS NOT NULL "
                   + "AND DATE(p.fecha_devolucion_real) BETWEEN ?::date AND ?::date "
                   + "ORDER BY p.fecha_devolucion_real DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("<tr>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getInt("id_prestamo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_prestamo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_devolucion_real")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("dni")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("lector")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>S/ ").append(String.format("%.2f", rs.getDouble("monto_multa"))).append("</td>")
                      .append("</tr>");
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error al consultar devoluciones y multas: " + e.getMessage(), e);
        }

        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }

    // REPORTE 3: Lectores Morosos
    public static String generarHtmlLectoresMorosos() throws DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='text-align:center; color:#2C3E50;'>REPORTE DE LECTORES MOROSOS Y PRÉSTAMOS VENCIDOS</h2>");
        sb.append("<p style='text-align:center; color:#7F8C8D;'>Sistema de Gestión de Biblioteca USAT</p>");
        sb.append("<table style='").append(ESTILO_TABLA).append("'><thead><tr>")
          .append("<th style='").append(ESTILO_TH).append("'>ID</th>")
          .append("<th style='").append(ESTILO_TH).append("'>DNI</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Lector</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Préstamo</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Límite</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Estado</th>")
          .append("</tr></thead><tbody>");

        String sql = "SELECT p.id_prestamo, l.dni, (l.nombres || ' ' || l.apellidos) AS lector, "
                   + "p.fecha_prestamo, p.fecha_devolucion_esperada, p.estado "
                   + "FROM prestamo p INNER JOIN lector l ON p.id_lector = l.id_lector "
                   + "WHERE p.estado = 'Con Retraso' OR (p.estado = 'Pendiente' AND p.fecha_devolucion_esperada < CURRENT_DATE) "
                   + "ORDER BY p.fecha_devolucion_esperada ASC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append("<tr>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getInt("id_prestamo")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("dni")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("lector")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_prestamo")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_devolucion_esperada")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append(" color:red; font-weight:bold;'>").append(rs.getString("estado")).append("</td>")
                  .append("</tr>");
            }
        } catch (Exception e) {
            throw new DAOException("Error al consultar datos de morosos: " + e.getMessage(), e);
        }

        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }

    // REPORTE 4: Historial por Lector
    public static String generarHtmlHistorialLector(String dni) throws DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='text-align:center; color:#2C3E50;'>HISTORIAL INDIVIDUAL DE LECTOR</h2>");
        sb.append("<p style='text-align:center; color:#7F8C8D;'>Búsqueda realizada para DNI: ").append(dni).append("</p>");
        sb.append("<table style='").append(ESTILO_TABLA).append("'><thead><tr>")
          .append("<th style='").append(ESTILO_TH).append("'>ID</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Libro</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Cód. Ejemplar</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Préstamo</th>")
          .append("<th style='").append(ESTILO_TH).append("'>F. Dev. Real</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Estado</th>")
          .append("</tr></thead><tbody>");

        String sql = "SELECT p.id_prestamo, lib.titulo, ej.codigo_patrimonial, p.fecha_prestamo, "
                   + "p.fecha_devolucion_real, p.estado "
                   + "FROM prestamo p INNER JOIN lector l ON p.id_lector = l.id_lector "
                   + "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo "
                   + "INNER JOIN ejemplar ej ON dp.id_ejemplar = ej.id_ejemplar "
                   + "INNER JOIN libro lib ON ej.id_libro = lib.id_libro "
                   + "WHERE l.dni = ? ORDER BY p.fecha_prestamo DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("<tr>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getInt("id_prestamo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("titulo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("codigo_patrimonial")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_prestamo")).append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getDate("fecha_devolucion_real") != null ? rs.getDate("fecha_devolucion_real") : "Pendiente").append("</td>")
                      .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("estado")).append("</td>")
                      .append("</tr>");
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error al consultar historial del lector: " + e.getMessage(), e);
        }

        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }

    // REPORTE 5: Ranking de Libros Más Prestados
    public static String generarHtmlLibrosMasPrestados() throws DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='text-align:center; color:#2C3E50;'>RANKING DE LIBROS MÁS SOLICITADOS</h2>");
        sb.append("<p style='text-align:center; color:#7F8C8D;'>Top de alta demanda en biblioteca</p>");
        sb.append("<table style='").append(ESTILO_TABLA).append("'><thead><tr>")
          .append("<th style='").append(ESTILO_TH).append("'>ID</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Título del Libro</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Autor</th>")
          .append("<th style='").append(ESTILO_TH).append("'>ISBN</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Total Préstamos</th>")
          .append("</tr></thead><tbody>");

        String sql = "SELECT lib.id_libro, lib.titulo, lib.autor, lib.isbn, COUNT(dp.id_detalle) AS total_prestamos "
                   + "FROM detalle_prestamo dp INNER JOIN ejemplar ej ON dp.id_ejemplar = ej.id_ejemplar "
                   + "INNER JOIN libro lib ON ej.id_libro = lib.id_libro "
                   + "GROUP BY lib.id_libro, lib.titulo, lib.autor, lib.isbn "
                   + "ORDER BY total_prestamos DESC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append("<tr>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getInt("id_libro")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("titulo")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("autor")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("isbn")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'><b>").append(rs.getInt("total_prestamos")).append("</b></td>")
                  .append("</tr>");
            }
        } catch (Exception e) {
            throw new DAOException("Error al consultar libros más prestados: " + e.getMessage(), e);
        }

        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }

    // REPORTE 6: Inventario General de Ejemplares
    public static String generarHtmlInventarioEjemplares() throws DAOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='text-align:center; color:#2C3E50;'>INVENTARIO GENERAL DE EJEMPLARES</h2>");
        sb.append("<p style='text-align:center; color:#7F8C8D;'>Control físico de stock en estantería</p>");
        sb.append("<table style='").append(ESTILO_TABLA).append("'><thead><tr>")
          .append("<th style='").append(ESTILO_TH).append("'>Cód. Patrimonial</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Título</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Ubicación</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Conservación</th>")
          .append("<th style='").append(ESTILO_TH).append("'>Disponibilidad</th>")
          .append("</tr></thead><tbody>");

        String sql = "SELECT ej.codigo_patrimonial, lib.titulo, ej.ubicacion, ej.estado_conservacion, "
                   + "CASE WHEN ej.disponible THEN 'Disponible' ELSE 'Prestado' END AS estado_disponibilidad "
                   + "FROM ejemplar ej INNER JOIN libro lib ON ej.id_libro = lib.id_libro "
                   + "ORDER BY lib.titulo ASC, ej.codigo_patrimonial ASC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sb.append("<tr>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("codigo_patrimonial")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("titulo")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("ubicacion")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("estado_conservacion")).append("</td>")
                  .append("<td style='").append(ESTILO_TD).append("'>").append(rs.getString("estado_disponibilidad")).append("</td>")
                  .append("</tr>");
            }
        } catch (Exception e) {
            throw new DAOException("Error al consultar inventario de ejemplares: " + e.getMessage(), e);
        }

        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }
}