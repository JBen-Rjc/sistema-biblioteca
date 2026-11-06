package modelo;

public class DetallePrestamo {
    private int idDetalle;
    private int idPrestamo;
    private Ejemplar ejemplar; // Apunta a Ejemplar en lugar de Libro
    private String observacionEntrega;
    private String observacionDevolucion;
    private boolean devuelto;

    public DetallePrestamo() {}

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public Ejemplar getEjemplar() { return ejemplar; }
    public void setEjemplar(Ejemplar ejemplar) { this.ejemplar = ejemplar; }

    public String getObservacionEntrega() { return observacionEntrega; }
    public void setObservacionEntrega(String observacionEntrega) { this.observacionEntrega = observacionEntrega; }

    public String getObservacionDevolucion() { return observacionDevolucion; }
    public void setObservacionDevolucion(String observacionDevolucion) { this.observacionDevolucion = observacionDevolucion; }

    public boolean isDevuelto() { return devuelto; }
    public void setDevuelto(boolean devuelto) { this.devuelto = devuelto; }
}