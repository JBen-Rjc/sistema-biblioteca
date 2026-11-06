package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Configuracion;

public class ConfiguracionDAO {

    public List<Configuracion> listarTodas() throws DAOException {
        List<Configuracion> lista = new ArrayList<>();
        String sql = "SELECT id_configuracion, clave, valor, descripcion FROM configuracion ORDER BY clave ASC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Configuracion c = new Configuracion();
                c.setIdConfiguracion(rs.getInt("id_configuracion"));
                c.setClave(rs.getString("clave"));
                c.setValor(rs.getString("valor"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al consultar configuraciones: " + e.getMessage(), e);
        }
        return lista;
    }

    public String obtenerValorPorClave(String clave) throws DAOException {
        String sql = "SELECT valor FROM configuracion WHERE clave = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("valor");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener parámetro de configuración: " + e.getMessage(), e);
        }
        return null;
    }

    public void actualizarValor(String clave, String nuevoValor) throws DAOException {
        String sql = "UPDATE configuracion SET valor = ? WHERE clave = ?";
        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoValor);
            ps.setString(2, clave);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar parámetro de configuración: " + e.getMessage(), e);
        }
    }
}