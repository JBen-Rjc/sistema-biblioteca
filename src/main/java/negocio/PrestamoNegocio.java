package negocio;

import dao.EjemplarDAO;
import dao.PrestamoDAO;
import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.Date;
import java.util.List;
import modelo.DetallePrestamo;
import modelo.Ejemplar;
import modelo.Prestamo;

public class PrestamoNegocio {

    private final PrestamoDAO prestamoDAO;
    private final EjemplarDAO ejemplarDAO;

    public PrestamoNegocio() {
        this.prestamoDAO = new PrestamoDAO();
        this.ejemplarDAO = new EjemplarDAO();
    }

    public Ejemplar buscarEjemplarParaPrestamo(String codigo) throws NegocioException, DAOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new NegocioException("Debe ingresar el código patrimonial del ejemplar.");
        }

        Ejemplar ej = ejemplarDAO.buscarPorCodigoPatrimonial(codigo.trim());

        if (ej == null) {
            throw new NegocioException("No se encontró ningún ejemplar con el código '" + codigo + "'.");
        }

        if (!ej.isDisponible()) {
            throw new NegocioException("El ejemplar '" + ej.getCodigoPatrimonial() + "' (" + ej.getLibro().getTitulo() + ") no se encuentra disponible (ya está prestado).");
        }

        return ej;
    }

    public void registrarPrestamo(Prestamo p) throws NegocioException, DAOException {
        if (p.getLector() == null || p.getLector().getIdLector() <= 0) {
            throw new NegocioException("Debe seleccionar un lector válido.");
        }

        if (!p.getLector().isEstado()) {
            throw new NegocioException("El lector seleccionado se encuentra inactivo y no puede solicitar préstamos.");
        }

        if (p.getUsuario() == null || p.getUsuario().getIdUsuario() <= 0) {
            throw new NegocioException("No hay un usuario autenticado para registrar la transacción.");
        }

        if (p.getDetalles() == null || p.getDetalles().isEmpty()) {
            throw new NegocioException("Debe agregar al menos un ejemplar al carrito del préstamo.");
        }

        if (p.getFechaDevolucionEsperada() == null) {
            throw new NegocioException("Debe definir la fecha límite de devolución.");
        }

        prestamoDAO.registrarPrestamoTransaccional(p);
    }

    // MÉTODOS PARA EL MÓDULO DE DEVOLUCIONES
    public List<Prestamo> buscarPrestamosPendientes(String dni) throws NegocioException, DAOException {
        if (dni == null || dni.trim().length() != 8) {
            throw new NegocioException("Ingrese un DNI válido de 8 dígitos para consultar los préstamos.");
        }
        return prestamoDAO.buscarPrestamosPendientesPorDni(dni.trim());
    }

    public List<DetallePrestamo> obtenerDetallesPrestamo(int idPrestamo) throws DAOException {
        return prestamoDAO.listarDetallesPorPrestamo(idPrestamo);
    }

    public double calcularMulta(Date fechaDevEsperada, double tarifaDiariaSoles) {
        Date hoy = new Date();
        if (hoy.after(fechaDevEsperada)) {
            long diferenciaMilis = hoy.getTime() - fechaDevEsperada.getTime();
            long diasRetraso = diferenciaMilis / (1000 * 60 * 60 * 24);
            if (diasRetraso > 0) {
                return diasRetraso * tarifaDiariaSoles;
            }
        }
        return 0.0;
    }

    public void registrarDevolucion(int idPrestamo, Date fechaDevEsperada) throws NegocioException, DAOException {
        if (idPrestamo <= 0) {
            throw new NegocioException("Debe seleccionar un préstamo activo para procesar la devolución.");
        }

        double multa = calcularMulta(fechaDevEsperada, 5.00); // S/ 5.00 tarifa diaria
        String estadoFinal = (multa > 0) ? "Con Retraso" : "Devuelto";

        prestamoDAO.registrarDevolucionTransaccional(idPrestamo, multa, estadoFinal);
    }
}