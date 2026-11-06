package dao;

import configuracion.Conexion;
import excepciones.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Libro;
import modelo.TipoMaterial;

public class LibroDAO {

    public List<Libro> listarTodos() throws DAOException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT l.id_libro, l.isbn, l.titulo, l.autor, l.editorial, l.anio_publicacion, "
                + "c.id_categoria, c.nombre AS nombre_categoria, "
                + "tm.id_tipo_material, tm.nombre AS nombre_tipo_material "
                + "FROM libro l "
                + "INNER JOIN categoria c ON l.id_categoria = c.id_categoria "
                + "INNER JOIN tipo_material tm ON l.id_tipo_material = tm.id_tipo_material "
                + "ORDER BY l.titulo ASC";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre_categoria"));

                TipoMaterial tipoMaterial = new TipoMaterial();
                tipoMaterial.setIdTipoMaterial(rs.getInt("id_tipo_material"));
                tipoMaterial.setNombre(rs.getString("nombre_tipo_material"));

                Libro libro = new Libro();
                libro.setIdLibro(rs.getInt("id_libro"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                libro.setCategoria(categoria);
                libro.setTipoMaterial(tipoMaterial);

                lista.add(libro);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar los libros: " + e.getMessage(), e);
        }
        return lista;
    }

    public void registrar(Libro l) throws DAOException {
        String sql = "INSERT INTO libro (isbn, titulo, autor, editorial, anio_publicacion, id_categoria, id_tipo_material) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getIsbn());
            ps.setString(2, l.getTitulo());
            ps.setString(3, l.getAutor());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getAnioPublicacion());
            ps.setInt(6, l.getCategoria().getIdCategoria());
            ps.setInt(7, l.getTipoMaterial().getIdTipoMaterial());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DAOException("El ISBN '" + l.getIsbn() + "' ya se encuentra registrado.", e);
            }
            throw new DAOException("Error al registrar libro: " + e.getMessage(), e);
        }
    }

    public void modificar(Libro l) throws DAOException {
        String sql = "UPDATE libro SET isbn=?, titulo=?, autor=?, editorial=?, anio_publicacion=?, id_categoria=?, id_tipo_material=? "
                + "WHERE id_libro=?";

        try (Connection con = Conexion.getInstancia().getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getIsbn());
            ps.setString(2, l.getTitulo());
            ps.setString(3, l.getAutor());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getAnioPublicacion());
            ps.setInt(6, l.getCategoria().getIdCategoria());
            ps.setInt(7, l.getTipoMaterial().getIdTipoMaterial());
            ps.setInt(8, l.getIdLibro());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DAOException("El ISBN '" + l.getIsbn() + "' ya pertenece a otro libro.", e);
            }
            throw new DAOException("Error al actualizar libro: " + e.getMessage(), e);
        }
    }
}
