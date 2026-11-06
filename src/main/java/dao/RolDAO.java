package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Rol;

public class RolDAO {

    public List<Rol> listarTodos() throws DAOException {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre, descripcion FROM rol ORDER BY id_rol ASC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol r = new Rol(
                    rs.getInt("id_rol"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                );
                lista.add(r);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar roles: " + e.getMessage(), e);
        }
        return lista;
    }
}