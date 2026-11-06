package modelo;

public class TipoMaterial {
    private int idTipoMaterial;
    private String nombre;
    private String descripcion;

    public TipoMaterial() {}

    public TipoMaterial(int idTipoMaterial, String nombre, String descripcion) {
        this.idTipoMaterial = idTipoMaterial;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdTipoMaterial() { return idTipoMaterial; }
    public void setIdTipoMaterial(int idTipoMaterial) { this.idTipoMaterial = idTipoMaterial; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() { return nombre; }
}