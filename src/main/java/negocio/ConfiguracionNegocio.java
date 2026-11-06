package negocio;

import dao.ConfiguracionDAO;
import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.List;
import modelo.Configuracion;

public class ConfiguracionNegocio {

    private final ConfiguracionDAO configuracionDAO;

    public ConfiguracionNegocio() {
        this.configuracionDAO = new ConfiguracionDAO();
    }

    public List<Configuracion> listarTodas() throws DAOException {
        return configuracionDAO.listarTodas();
    }

    public int obtenerDiasPrestamo() throws DAOException {
        try {
            String val = configuracionDAO.obtenerValorPorClave("DIAS_PRESTAMO");
            return val != null ? Integer.parseInt(val) : 3;
        } catch (NumberFormatException e) {
            return 3;
        }
    }

    public double obtenerMultaDiaria() throws DAOException {
        try {
            String val = configuracionDAO.obtenerValorPorClave("MULTA_DIARIA_SOLES");
            return val != null ? Double.parseDouble(val) : 5.00;
        } catch (NumberFormatException e) {
            return 5.00;
        }
    }

    public void guardarConfiguracion(String clave, String valor) throws NegocioException, DAOException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new NegocioException("Debe ingresar un valor válido.");
        }

        if ("DIAS_PRESTAMO".equalsIgnoreCase(clave)) {
            try {
                int d = Integer.parseInt(valor.trim());
                if (d <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                throw new NegocioException("Los días de préstamo deben ser un número entero mayor a 0.");
            }
        } else if ("MULTA_DIARIA_SOLES".equalsIgnoreCase(clave)) {
            try {
                double m = Double.parseDouble(valor.trim());
                if (m < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                throw new NegocioException("La multa diaria debe ser un monto numérico positivo.");
            }
        }

        configuracionDAO.actualizarValor(clave, valor.trim());
    }
}