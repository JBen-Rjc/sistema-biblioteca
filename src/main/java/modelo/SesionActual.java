package modelo;

public class SesionActual {
    
    private static Usuario usuarioLogueado;

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    // Método para resetear/limpiar la sesión al cerrar sesión
    public static void limpiarSesion() {
        usuarioLogueado = null;
    }
}