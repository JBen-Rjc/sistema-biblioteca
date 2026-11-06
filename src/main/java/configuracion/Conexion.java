package configuracion;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Conexión única (singleton) a PostgreSQL.
 *
 * Los datos de acceso NO se guardan en el código. Se leen, en este orden:
 *   1. Variables de entorno: BD_URL, BD_USUARIO, BD_CLAVE
 *   2. Archivo bd.properties en la carpeta desde donde se ejecuta la app
 *      (copiar bd.properties.example y completar).
 */
public class Conexion {
    private static final String ARCHIVO_CONFIG = "bd.properties";
    private static final String URL_POR_DEFECTO = "jdbc:postgresql://localhost:5432/bd_biblioteca";
    private static final String USUARIO_POR_DEFECTO = "postgres";

    private static Conexion instancia;
    private Connection conexion;

    private final String url;
    private final String usuario;
    private final String clave;

    private Conexion() {
        Properties archivo = cargarArchivo();
        this.url = valor("BD_URL", archivo, "bd.url", URL_POR_DEFECTO);
        this.usuario = valor("BD_USUARIO", archivo, "bd.usuario", USUARIO_POR_DEFECTO);
        this.clave = valor("BD_CLAVE", archivo, "bd.clave", "");
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(url, usuario, clave);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: Driver de PostgreSQL no encontrado.", e);
        }
        return conexion;
    }

    public void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private static Properties cargarArchivo() {
        Properties props = new Properties();
        Path ruta = Path.of(ARCHIVO_CONFIG);
        if (Files.isRegularFile(ruta)) {
            try (Reader in = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("No se pudo leer " + ARCHIVO_CONFIG + ": " + e.getMessage());
            }
        }
        return props;
    }

    private static String valor(String variableEntorno, Properties archivo, String claveArchivo, String porDefecto) {
        String env = System.getenv(variableEntorno);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        String prop = archivo.getProperty(claveArchivo);
        if (prop != null && !prop.isBlank()) {
            return prop.trim();
        }
        return porDefecto;
    }
}
