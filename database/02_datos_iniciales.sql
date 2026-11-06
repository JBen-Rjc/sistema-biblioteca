SET client_encoding = 'UTF8';

-- =============================================================
--  Datos iniciales y de demostración
--  Usuario de prueba:  admin / admin123   (cámbialo después de entrar)
-- =============================================================

INSERT INTO rol (nombre, descripcion) VALUES
    ('Administrador', 'Control total del sistema'),
    ('Bibliotecario', 'Gestión de préstamos y catálogo');

INSERT INTO configuracion (clave, valor, descripcion) VALUES
    ('DIAS_PRESTAMO',      '5',     'Días permitidos para préstamo de ejemplares'),
    ('MULTA_DIARIA_SOLES', '10.00', 'Costo de multa por cada día de retraso');

INSERT INTO categoria (nombre, descripcion) VALUES
    ('Ingeniería de Software',  'Metodologías y diseño de software'),
    ('Base de Datos',           'Modelamiento y gestión SQL'),
    ('Ciencia Ficción',         'Novelas de ciencia ficción y distopías'),
    ('Literatura y Narrativa',  'Obras literarias, novelas y narrativa general');

INSERT INTO tipo_material (nombre, descripcion) VALUES
    ('Libro Impreso', 'Texto bibliográfico impreso'),
    ('Tesis',         'Proyecto de titulación universitaria'),
    ('Revista',       'Publicaciones periódicas y afines');

-- Contraseña: admin123 (hash BCrypt, costo 12)
INSERT INTO usuario (username, clave, nombres, apellidos, dni, correo, estado, id_rol) VALUES
    ('admin', '$2a$12$4soM5Hi5pL/wGnRvA6gSMu2iTbEgfKpvtBVS7aOUHFCxQScgln9ta',
     'Admin', 'Demo', '00000001', 'admin@ejemplo.com', TRUE, 1);

-- Acervo de ejemplo
INSERT INTO libro (isbn, titulo, autor, editorial, anio_publicacion, id_categoria, id_tipo_material) VALUES
    ('9780132350884', 'Clean Code',                         'Robert C. Martin',  'Prentice Hall', 2008, 1, 1),
    ('9780201633610', 'Design Patterns',                    'Erich Gamma et al.', 'Addison-Wesley', 1994, 1, 1),
    ('9780073523323', 'Database System Concepts',           'Abraham Silberschatz', 'McGraw-Hill', 2010, 2, 1),
    ('9780451524935', '1984',                               'George Orwell',     'Signet Classics', 1949, 3, 1),
    ('9780307474728', 'Cien años de soledad',               'Gabriel García Márquez', 'Vintage Español', 1967, 4, 1);

INSERT INTO ejemplar (codigo_patrimonial, estado_conservacion, ubicacion, disponible, id_libro) VALUES
    ('EJ-0001', 'Bueno',   'Estante A-1', TRUE, 1),
    ('EJ-0002', 'Regular', 'Estante A-1', TRUE, 1),
    ('EJ-0003', 'Bueno',   'Estante A-2', TRUE, 2),
    ('EJ-0004', 'Bueno',   'Estante B-1', TRUE, 3),
    ('EJ-0005', 'Bueno',   'Estante C-1', TRUE, 4),
    ('EJ-0006', 'Bueno',   'Estante C-2', TRUE, 5);

INSERT INTO lector (dni, nombres, apellidos, telefono, correo, direccion, tipo_lector, estado) VALUES
    ('10000001', 'Ana',   'Pérez Ruiz',  '900000001', 'ana@ejemplo.com',   'Av. Ejemplo 123', 'Estudiante', TRUE),
    ('10000002', 'Luis',  'Gómez Díaz',  '900000002', 'luis@ejemplo.com',  'Calle Demo 456',  'Docente',    TRUE),
    ('10000003', 'María', 'Torres León', '900000003', 'maria@ejemplo.com', 'Jr. Prueba 789',  'Externo',    TRUE);
