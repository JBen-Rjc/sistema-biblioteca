package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Lector;

public class LectorDAO {

    public void registrar(Lector l) throws DAOException {
        String sql = "INSERT INTO lector (dni, nombres, apellidos, telefono, correo, direccion, tipo_lector, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getDni());
            ps.setString(2, l.getNombres());
            ps.setString(3, l.getApellidos());
            ps.setString(4, l.getTelefono());
            ps.setString(5, l.getCorreo());
            ps.setString(6, l.getDireccion());
            ps.setString(7, l.getTipoLector());
            ps.setBoolean(8, l.isEstado());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DAOException("El DNI '" + l.getDni() + "' ya se encuentra registrado.", e);
            }
            throw new DAOException("Error al registrar lector: " + e.getMessage(), e);
        }
    }

    public void modificar(Lector l) throws DAOException {
        String sql = "UPDATE lector SET nombres=?, apellidos=?, telefono=?, correo=?, direccion=?, tipo_lector=?, estado=? "
                + "WHERE id_lector=?";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getNombres());
            ps.setString(2, l.getApellidos());
            ps.setString(3, l.getTelefono());
            ps.setString(4, l.getCorreo());
            ps.setString(5, l.getDireccion());
            ps.setString(6, l.getTipoLector());
            ps.setBoolean(7, l.isEstado());
            ps.setInt(8, l.getIdLector());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al modificar lector: " + e.getMessage(), e);
        }
    }

    // Búsqueda Parametrizada Dinámica (Sesión 12)
    public List<Lector> buscarParametrizado(String dni, String apellidos) throws DAOException {
        List<Lector> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM lector WHERE 1=1");

        if (dni != null && !dni.trim().isEmpty()) {
            sql.append(" AND dni LIKE ?");
        }
        if (apellidos != null && !apellidos.trim().isEmpty()) {
            sql.append(" AND apellidos ILIKE ?");
        }
        sql.append(" ORDER BY apellidos ASC");

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (dni != null && !dni.trim().isEmpty()) {
                ps.setString(index++, dni.trim() + "%");
            }
            if (apellidos != null && !apellidos.trim().isEmpty()) {
                ps.setString(index++, "%" + apellidos.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lector l = new Lector();
                    l.setIdLector(rs.getInt("id_lector"));
                    l.setDni(rs.getString("dni"));
                    l.setNombres(rs.getString("nombres"));
                    l.setApellidos(rs.getString("apellidos"));
                    l.setTelefono(rs.getString("telefono"));
                    l.setCorreo(rs.getString("correo"));
                    l.setDireccion(rs.getString("direccion"));
                    l.setTipoLector(rs.getString("tipo_lector"));
                    l.setEstado(rs.getBoolean("estado"));
                    lista.add(l);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar lectores: " + e.getMessage(), e);
        }
        return lista;
    }

    public Lector buscarPorDni(String dni) throws DAOException {
        String sql = "SELECT id_lector, dni, nombres, apellidos, telefono, correo, direccion, tipo_lector, estado "
                + "FROM lector WHERE dni = ?";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lector l = new Lector();
                    l.setIdLector(rs.getInt("id_lector"));
                    l.setDni(rs.getString("dni"));
                    l.setNombres(rs.getString("nombres"));
                    l.setApellidos(rs.getString("apellidos"));
                    l.setTelefono(rs.getString("telefono"));
                    l.setCorreo(rs.getString("correo"));
                    l.setDireccion(rs.getString("direccion"));
                    l.setTipoLector(rs.getString("tipo_lector"));
                    l.setEstado(rs.getBoolean("estado"));
                    return l;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar lector por DNI: " + e.getMessage(), e);
        }
        return null;
    }
}
