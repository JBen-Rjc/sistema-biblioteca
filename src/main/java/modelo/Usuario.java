package modelo;

public class Usuario {
    private int idUsuario;
    private String username;
    private String clave;
    private String nombres;
    private String apellidos;
    private String dni;
    private String correo;
    private boolean estado;
    private Rol rol; // Objeto de relación para la tabla rol

    public Usuario() {}

    public Usuario(int idUsuario, String username, String clave, String nombres, String apellidos, String dni, String correo, boolean estado, Rol rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.clave = clave;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.correo = correo;
        this.estado = estado;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}