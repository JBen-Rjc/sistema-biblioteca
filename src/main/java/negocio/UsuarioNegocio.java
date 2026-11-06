package negocio;

import dao.UsuarioDAO;
import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.List;
import modelo.SesionActual;
import modelo.Usuario;

public class UsuarioNegocio {

    private final UsuarioDAO usuarioDAO;

    public UsuarioNegocio() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String username, String clave) throws NegocioException, DAOException {
        // Validaciones en capa de negocio
        if (username == null || username.trim().isEmpty()) {
            throw new NegocioException("Debe ingresar su nombre de usuario.");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new NegocioException("Debe ingresar su contraseña.");
        }

        // Llamada a la capa DAO
        Usuario usuario = usuarioDAO.autenticar(username.trim(), clave.trim());

        if (usuario == null) {
            throw new NegocioException("Usuario o contraseña incorrectos.");
        }

        if (!usuario.isEstado()) {
            throw new NegocioException("Su cuenta se encuentra inactiva. Contacte al administrador.");
        }

        // Guardar usuario en memoria (SesionActual)
        SesionActual.setUsuarioLogueado(usuario);

        return usuario;
    }

    private void validarUsuario(Usuario u, boolean claveObligatoria) throws NegocioException {
        if (u.getDni() == null || u.getDni().trim().length() != 8) {
            throw new NegocioException("El DNI debe tener exactamente 8 dígitos.");
        }
        if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar un nombre de usuario.");
        }
        boolean sinClave = u.getClave() == null || u.getClave().trim().isEmpty();
        if (claveObligatoria && sinClave) {
            throw new NegocioException("Debe ingresar una contraseña.");
        }
        if (!sinClave && u.getClave().trim().length() < 6) {
            throw new NegocioException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (u.getNombres() == null || u.getNombres().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar los nombres.");
        }
        if (u.getApellidos() == null || u.getApellidos().trim().isEmpty()) {
            throw new NegocioException("Debe ingresar los apellidos.");
        }
        if (u.getRol() == null || u.getRol().getIdRol() == 0) {
            throw new NegocioException("Debe seleccionar un rol válido.");
        }
    }

    public List<Usuario> listarTodos() throws DAOException {
        return usuarioDAO.listarTodos();
    }

    public void registrar(Usuario u) throws NegocioException, DAOException {
        validarUsuario(u, true);
        usuarioDAO.registrar(u);
    }

    public void actualizar(Usuario u) throws NegocioException, DAOException {
        if (u.getIdUsuario() <= 0) {
            throw new NegocioException("Debe seleccionar un usuario para actualizar.");
        }
        validarUsuario(u, false); // vacía = conservar la contraseña actual
        usuarioDAO.modificar(u);
    }
}