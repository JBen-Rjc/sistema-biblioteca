package negocio;

import dao.LectorDAO;
import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.List;
import modelo.Lector;

public class LectorNegocio {

    private final LectorDAO lectorDAO;

    public LectorNegocio() {
        this.lectorDAO = new LectorDAO();
    }

    private void validarLector(Lector l) throws NegocioException {
        if (l.getDni() == null || l.getDni().trim().length() != 8) {
            throw new NegocioException("El DNI debe tener exactamente 8 dígitos.");
        }
        if (l.getNombres() == null || l.getNombres().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar los nombres del lector.");
        }
        if (l.getApellidos() == null || l.getApellidos().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar los apellidos del lector.");
        }
        if (l.getTipoLector() == null || l.getTipoLector().trim().isEmpty()) {
            throw new NegocioException("Debe seleccionar un tipo de lector.");
        }
    }

    public void registrar(Lector l) throws NegocioException, DAOException {
        validarLector(l);
        lectorDAO.registrar(l);
    }

    public void actualizar(Lector l) throws NegocioException, DAOException {
        if (l.getIdLector() <= 0) {
            throw new NegocioException("No se ha seleccionado un lector válido para actualizar.");
        }
        validarLector(l);
        lectorDAO.modificar(l);
    }

    public void guardar(Lector l) throws NegocioException, DAOException {
        if (l.getIdLector() == 0) {
            registrar(l);
        } else {
            actualizar(l);
        }
    }

    public List<Lector> buscar(String dni, String apellidos) throws DAOException {
        return lectorDAO.buscarParametrizado(dni, apellidos);
    }

    // MÉTODO AGREGADO PARA EL MÓDULO DE PRÉSTAMOS
    public Lector buscarPorDni(String dni) throws NegocioException, DAOException {
        if (dni == null || dni.trim().length() != 8) {
            throw new NegocioException("El DNI a buscar debe tener 8 dígitos.");
        }
        return lectorDAO.buscarPorDni(dni.trim());
    }
    
}