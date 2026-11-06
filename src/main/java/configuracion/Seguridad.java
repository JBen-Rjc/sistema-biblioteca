package configuracion;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidades de hash de contraseñas con BCrypt.
 * Las contraseñas nunca se guardan en texto plano en la base de datos.
 */
public final class Seguridad {
    private static final int COSTO = 12;

    private Seguridad() {}

    public static String hashear(String clavePlana) {
        return BCrypt.hashpw(clavePlana, BCrypt.gensalt(COSTO));
    }

    /** true si el valor guardado ya es un hash BCrypt. */
    public static boolean esHash(String valorGuardado) {
        return valorGuardado != null && valorGuardado.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }

    public static boolean verificar(String clavePlana, String hashGuardado) {
        try {
            return BCrypt.checkpw(clavePlana, hashGuardado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
