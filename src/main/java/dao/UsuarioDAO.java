package dao;

import configuracion.Conexion;
import configuracion.Seguridad;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Rol;
import modelo.Usuario;

public class UsuarioDAO {

    /**
     * Valida usuario y contraseña. La contraseña se compara contra un hash BCrypt.
     * Si el usuario aún tiene la clave en texto plano (datos antiguos), se valida
     * igual y se reemplaza por su hash en ese momento (migración transparente).
     */
    public Usuario autenticar(String username, String clave) throws DAOException {
        String sql = "SELECT u.id_usuario, u.username, u.clave, u.nombres, u.apellidos, u.dni, u.correo, u.estado, "
                   + "r.id_rol, r.nombre AS nombre_rol, r.descripcion AS desc_rol "
                   + "FROM usuario u "
                   + "INNER JOIN rol r ON u.id_rol = r.id_rol "
                   + "WHERE u.username = ?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null; // Usuario no existe
                }

                String guardada = rs.getString("clave");
                boolean valida;
                if (Seguridad.esHash(guardada)) {
                    valida = Seguridad.verificar(clave, guardada);
                } else {
                    valida = guardada != null && guardada.equals(clave);
                    if (valida) {
                        actualizarHash(con, rs.getInt("id_usuario"), clave);
                    }
                }
                if (!valida) {
                    return null; // Contraseña incorrecta
                }

                Rol rol = new Rol(
                    rs.getInt("id_rol"),
                    rs.getString("nombre_rol"),
                    rs.getString("desc_rol")
                );

                return new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    null, // la clave no se mantiene en memoria
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("correo"),
                    rs.getBoolean("estado"),
                    rol
                );
            }

        } catch (SQLException e) {
            throw new DAOException("Error en la base de datos al validar credenciales: " + e.getMessage(), e);
        }
    }

    private void actualizarHash(Connection con, int idUsuario, String clavePlana) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE usuario SET clave = ? WHERE id_usuario = ?")) {
            ps.setString(1, Seguridad.hashear(clavePlana));
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    public List<Usuario> listarTodos() throws DAOException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.username, u.nombres, u.apellidos, u.dni, u.correo, u.estado, "
                   + "r.id_rol, r.nombre AS nombre_rol, r.descripcion AS desc_rol "
                   + "FROM usuario u "
                   + "INNER JOIN rol r ON u.id_rol = r.id_rol "
                   + "ORDER BY u.apellidos, u.nombres ASC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol(
                    rs.getInt("id_rol"),
                    rs.getString("nombre_rol"),
                    rs.getString("desc_rol")
                );

                Usuario u = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    null, // no se expone el hash
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("dni"),
                    rs.getString("correo"),
                    rs.getBoolean("estado"),
                    rol
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    public void registrar(Usuario u) throws DAOException {
        String sql = "INSERT INTO usuario (username, clave, nombres, apellidos, dni, correo, estado, id_rol) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, Seguridad.hashear(u.getClave()));
            ps.setString(3, u.getNombres());
            ps.setString(4, u.getApellidos());
            ps.setString(5, u.getDni());
            ps.setString(6, u.getCorreo());
            ps.setBoolean(7, u.isEstado());
            ps.setInt(8, u.getRol().getIdRol());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DAOException("El Username o DNI ya se encuentra registrado.", e);
            }
            throw new DAOException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos del usuario. Si la clave viene vacía se conserva la actual;
     * si viene una nueva, se guarda su hash.
     */
    public void modificar(Usuario u) throws DAOException {
        boolean cambiaClave = u.getClave() != null && !u.getClave().isBlank();
        String sql = cambiaClave
                ? "UPDATE usuario SET username=?, nombres=?, apellidos=?, dni=?, correo=?, estado=?, id_rol=?, clave=? WHERE id_usuario=?"
                : "UPDATE usuario SET username=?, nombres=?, apellidos=?, dni=?, correo=?, estado=?, id_rol=? WHERE id_usuario=?";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, u.getUsername());
            ps.setString(i++, u.getNombres());
            ps.setString(i++, u.getApellidos());
            ps.setString(i++, u.getDni());
            ps.setString(i++, u.getCorreo());
            ps.setBoolean(i++, u.isEstado());
            ps.setInt(i++, u.getRol().getIdRol());
            if (cambiaClave) {
                ps.setString(i++, Seguridad.hashear(u.getClave()));
            }
            ps.setInt(i, u.getIdUsuario());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DAOException("El Username o DNI ingresado pertenece a otro usuario.", e);
            }
            throw new DAOException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }
}