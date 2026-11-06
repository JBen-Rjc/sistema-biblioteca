package negocio;

import dao.LibroDAO;
import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.List;
import modelo.Libro;

public class LibroNegocio {

    private final LibroDAO libroDAO;

    public LibroNegocio() {
        this.libroDAO = new LibroDAO();
    }

    public List<Libro> listarTodos() throws DAOException {
        return libroDAO.listarTodos();
    }

    public void registrarLibro(Libro l) throws NegocioException, DAOException {
        if (l.getTitulo() == null || l.getTitulo().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el título del libro.");
        }
        if (l.getIsbn() == null || l.getIsbn().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el código ISBN.");
        }
        if (l.getAutor() == null || l.getAutor().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el autor del libro.");
        }
        if (l.getCategoria() == null || l.getCategoria().getIdCategoria() == 0) {
            throw new NegocioException("Debe seleccionar una categoría.");
        }
        if (l.getTipoMaterial() == null || l.getTipoMaterial().getIdTipoMaterial() == 0) {
            throw new NegocioException("Debe seleccionar el tipo de material.");
        }

        libroDAO.registrar(l);
    }

    public void actualizarLibro(Libro l) throws NegocioException, DAOException {
        if (l.getIdLibro() <= 0) {
            throw new NegocioException("Debe seleccionar un libro válido para actualizar.");
        }
        if (l.getTitulo() == null || l.getTitulo().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el título del libro.");
        }
        if (l.getIsbn() == null || l.getIsbn().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el código ISBN.");
        }
        if (l.getAutor() == null || l.getAutor().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el autor del libro.");
        }
        if (l.getCategoria() == null || l.getCategoria().getIdCategoria() == 0) {
            throw new NegocioException("Debe seleccionar una categoría.");
        }
        if (l.getTipoMaterial() == null || l.getTipoMaterial().getIdTipoMaterial() == 0) {
            throw new NegocioException("Debe seleccionar el tipo de material.");
        }

        libroDAO.modificar(l);
    }
}
