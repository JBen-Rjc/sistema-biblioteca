package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.TipoMaterial;

public class TipoMaterialDAO {

    public List<TipoMaterial> listarTodos() throws DAOException {
        List<TipoMaterial> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_material, nombre, descripcion FROM tipo_material ORDER BY nombre ASC";

        try (Connection con = Conexion.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoMaterial tm = new TipoMaterial();
                tm.setIdTipoMaterial(rs.getInt("id_tipo_material"));
                tm.setNombre(rs.getString("nombre"));
                tm.setDescripcion(rs.getString("descripcion"));
                lista.add(tm);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar tipos de material: " + e.getMessage(), e);
        }
        return lista;
    }
}