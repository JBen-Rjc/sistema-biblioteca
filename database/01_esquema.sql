SET client_encoding = 'UTF8';

-- =============================================================
--  Sistema de Biblioteca - Esquema de base de datos (PostgreSQL)
--  Uso:
--    CREATE DATABASE bd_biblioteca;
--    \c bd_biblioteca
--    \i database/01_esquema.sql
--    \i database/02_datos_iniciales.sql
-- =============================================================

-- ---------- Catálogos ----------

CREATE TABLE rol (
    id_rol       SERIAL PRIMARY KEY,
    nombre       VARCHAR(30)  NOT NULL UNIQUE,
    descripcion  VARCHAR(150)
);

CREATE TABLE categoria (
    id_categoria SERIAL PRIMARY KEY,
    nombre       VARCHAR(50)  NOT NULL UNIQUE,
    descripcion  VARCHAR(150)
);

CREATE TABLE tipo_material (
    id_tipo_material SERIAL PRIMARY KEY,
    nombre           VARCHAR(50)  NOT NULL UNIQUE,
    descripcion      VARCHAR(150)
);

-- Parámetros del negocio (días de préstamo, multa diaria, etc.)
CREATE TABLE configuracion (
    id_configuracion SERIAL PRIMARY KEY,
    clave            VARCHAR(50)  NOT NULL UNIQUE,
    valor            VARCHAR(50)  NOT NULL,
    descripcion      VARCHAR(150)
);

-- ---------- Personas ----------

-- Personal que usa el sistema. "clave" guarda un hash BCrypt (60 caracteres).
CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    username   VARCHAR(30)  NOT NULL UNIQUE,
    clave      VARCHAR(100) NOT NULL,
    nombres    VARCHAR(50)  NOT NULL,
    apellidos  VARCHAR(50)  NOT NULL,
    dni        CHAR(8)      NOT NULL UNIQUE,
    correo     VARCHAR(100),
    estado     BOOLEAN      NOT NULL DEFAULT TRUE,
    id_rol     INTEGER      NOT NULL REFERENCES rol(id_rol)
);

-- Personas que solicitan préstamos.
CREATE TABLE lector (
    id_lector   SERIAL PRIMARY KEY,
    dni         CHAR(8)      NOT NULL UNIQUE,
    nombres     VARCHAR(50)  NOT NULL,
    apellidos   VARCHAR(50)  NOT NULL,
    telefono    VARCHAR(15),
    correo      VARCHAR(100),
    direccion   VARCHAR(150),
    tipo_lector VARCHAR(20)  NOT NULL CHECK (tipo_lector IN ('Estudiante', 'Docente', 'Externo')),
    estado      BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ---------- Acervo ----------

CREATE TABLE libro (
    id_libro         SERIAL PRIMARY KEY,
    isbn             VARCHAR(20)  UNIQUE,
    titulo           VARCHAR(150) NOT NULL,
    autor            VARCHAR(100) NOT NULL,
    editorial        VARCHAR(80),
    anio_publicacion INTEGER,
    id_categoria     INTEGER      NOT NULL REFERENCES categoria(id_categoria),
    id_tipo_material INTEGER      NOT NULL REFERENCES tipo_material(id_tipo_material)
);

-- Copia física de un libro.
CREATE TABLE ejemplar (
    id_ejemplar         SERIAL PRIMARY KEY,
    codigo_patrimonial  VARCHAR(30)  NOT NULL UNIQUE,
    estado_conservacion VARCHAR(30)  NOT NULL DEFAULT 'Bueno'
                        CHECK (estado_conservacion IN ('Bueno', 'Regular', 'Dañado', 'Perdido')),
    ubicacion           VARCHAR(100),
    disponible          BOOLEAN      NOT NULL DEFAULT TRUE,
    id_libro            INTEGER      NOT NULL REFERENCES libro(id_libro) ON DELETE CASCADE
);

-- ---------- Préstamos ----------

CREATE TABLE prestamo (
    id_prestamo               SERIAL PRIMARY KEY,
    fecha_prestamo            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion_esperada DATE         NOT NULL,
    fecha_devolucion_real     DATE,
    monto_multa               NUMERIC(8,2) DEFAULT 0.00,
    estado                    VARCHAR(20)  NOT NULL DEFAULT 'Pendiente'
                              CHECK (estado IN ('Pendiente', 'Devuelto', 'Con Retraso')),
    id_lector                 INTEGER      NOT NULL REFERENCES lector(id_lector),
    id_usuario                INTEGER      NOT NULL REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_prestamo (
    id_detalle             SERIAL PRIMARY KEY,
    id_prestamo            INTEGER      NOT NULL REFERENCES prestamo(id_prestamo) ON DELETE CASCADE,
    id_ejemplar            INTEGER      NOT NULL REFERENCES ejemplar(id_ejemplar),
    observacion_entrega    VARCHAR(150),
    observacion_devolucion VARCHAR(150),
    devuelto               BOOLEAN      NOT NULL DEFAULT FALSE
);

-- ---------- Índices ----------

CREATE INDEX idx_libro_titulo     ON libro (titulo);
CREATE INDEX idx_lector_apellidos ON lector (apellidos);
CREATE INDEX idx_prestamo_estado  ON prestamo (estado);
