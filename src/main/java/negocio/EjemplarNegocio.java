package negocio;

import dao.EjemplarDAO;
import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.List;
import modelo.Ejemplar;

public class EjemplarNegocio {

    private final EjemplarDAO ejemplarDAO;

    public EjemplarNegocio() {
        this.ejemplarDAO = new EjemplarDAO();
    }

    public void registrarEjemplar(Ejemplar ej) throws NegocioException, DAOException {
        if (ej.getLibro() == null || ej.getLibro().getIdLibro() == 0) {
            throw new NegocioException("Debe seleccionar un libro para asignar el ejemplar.");
        }
        if (ej.getCodigoPatrimonial() == null || ej.getCodigoPatrimonial().trim().isEmpty()) {
            throw new NegocioException("El código patrimonial es obligatorio.");
        }
        if (ej.getUbicacion() == null || ej.getUbicacion().trim().isEmpty()) {
            throw new NegocioException("Debe especificar la ubicación del ejemplar (estante/repisa).");
        }

        ejemplarDAO.registrar(ej);
    }

    public List<Ejemplar> listarPorLibro(int idLibro) throws DAOException {
        return ejemplarDAO.listarPorLibro(idLibro);
    }
    public List<Ejemplar> listarDisponibles(String criterio) throws DAOException {
    return ejemplarDAO.listarDisponibles(criterio);
}
}