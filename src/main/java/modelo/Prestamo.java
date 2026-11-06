package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Prestamo {
    private int idPrestamo;
    private Date fechaPrestamo;
    private Date fechaDevolucionEsperada;
    private Date fechaDevolucionReal;
    private String estado;
    private Lector lector;     // Lector que solicita
    private Usuario usuario;   // Bibliotecario que atiende
    private List<DetallePrestamo> detalles; // Contiene los libros prestados

    public Prestamo() {
        this.detalles = new ArrayList<>();
    }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public Date getFechaDevolucionEsperada() { return fechaDevolucionEsperada; }
    public void setFechaDevolucionEsperada(Date fechaDevolucionEsperada) { this.fechaDevolucionEsperada = fechaDevolucionEsperada; }

    public Date getFechaDevolucionReal() { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(Date fechaDevolucionReal) { this.fechaDevolucionReal = fechaDevolucionReal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Lector getLector() { return lector; }
    public void setLector(Lector lector) { this.lector = lector; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetallePrestamo> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePrestamo> detalles) { this.detalles = detalles; }

    // Método de apoyo sobrecargado con observaciones
    public void agregarDetalle(Ejemplar ejemplar, String observacionEntrega) {
        DetallePrestamo detalle = new DetallePrestamo();
        detalle.setEjemplar(ejemplar);
        detalle.setObservacionEntrega(observacionEntrega);
        detalle.setDevuelto(false);
        this.detalles.add(detalle);
    }
}