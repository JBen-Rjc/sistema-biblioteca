SET client_encoding = 'UTF8';


-- =============================================================
--  Datos iniciales: catálogos, configuración y usuarios del sistema
--  Generado por database/generador/generar_datos_demo.py
--  Usuarios de prueba:  admin / admin123   |   mlopez, jcastro, rvilchez / biblio123
-- =============================================================

INSERT INTO rol (id_rol, nombre, descripcion) VALUES
    (1, 'Administrador', 'Control total del sistema'),
    (2, 'Bibliotecario', 'Gestión de préstamos y catálogo');

INSERT INTO configuracion (id_configuracion, clave, valor, descripcion) VALUES
    (1, 'DIAS_PRESTAMO', '5', 'Días permitidos para préstamo de ejemplares'),
    (2, 'MULTA_DIARIA_SOLES', '10.00', 'Costo de multa por cada día de retraso');

INSERT INTO categoria (id_categoria, nombre, descripcion) VALUES
    (1, 'Ingeniería de Software', 'Metodologías, arquitectura y diseño de software'),
    (2, 'Base de Datos', 'Modelamiento, SQL y gestión de datos'),
    (3, 'Redes y Sistemas', 'Redes de computadoras y sistemas operativos'),
    (4, 'Matemáticas y Estadística', 'Cálculo, álgebra, probabilidad y estadística'),
    (5, 'Ciencia Ficción', 'Novelas de ciencia ficción y distopías'),
    (6, 'Literatura Peruana', 'Narrativa y poesía de autores peruanos'),
    (7, 'Literatura Latinoamericana', 'Narrativa latinoamericana contemporánea'),
    (8, 'Literatura Universal', 'Clásicos de la literatura universal'),
    (9, 'Historia', 'Historia del Perú y universal'),
    (10, 'Administración y Negocios', 'Gestión, emprendimiento y economía'),
    (11, 'Psicología', 'Psicología general y del desarrollo');

INSERT INTO tipo_material (id_tipo_material, nombre, descripcion) VALUES
    (1, 'Libro Impreso', 'Texto bibliográfico impreso'),
    (2, 'Tesis', 'Proyecto de titulación universitaria'),
    (3, 'Revista', 'Publicaciones periódicas y afines');

INSERT INTO usuario (id_usuario, username, clave, nombres, apellidos, dni, correo, estado, id_rol) VALUES
    (1, 'admin', '$2a$12$4soM5Hi5pL/wGnRvA6gSMu2iTbEgfKpvtBVS7aOUHFCxQScgln9ta', 'Admin', 'Demo', '00000001', 'admin@ejemplo.com', TRUE, 1),
    (2, 'mlopez', '$2a$12$Vx9/K5891aINvGDaUwobQeOvjPHpl7ejXu5H7.Jj.62Y/vh4fb2Iu', 'María', 'López Chávez', '00000002', 'mlopez@ejemplo.com', TRUE, 2),
    (3, 'jcastro', '$2a$12$Vx9/K5891aINvGDaUwobQeOvjPHpl7ejXu5H7.Jj.62Y/vh4fb2Iu', 'Jorge', 'Castro Ramírez', '00000003', 'jcastro@ejemplo.com', TRUE, 2),
    (4, 'rvilchez', '$2a$12$Vx9/K5891aINvGDaUwobQeOvjPHpl7ejXu5H7.Jj.62Y/vh4fb2Iu', 'Rosa', 'Vílchez Sánchez', '00000004', 'rvilchez@ejemplo.com', TRUE, 2),
    (5, 'pdiaz', '$2a$12$Vx9/K5891aINvGDaUwobQeOvjPHpl7ejXu5H7.Jj.62Y/vh4fb2Iu', 'Pedro', 'Díaz Montenegro', '00000005', 'pdiaz@ejemplo.com', FALSE, 2);

SELECT setval(pg_get_serial_sequence('rol', 'id_rol'), (SELECT COALESCE(MAX(id_rol), 1) FROM rol));
SELECT setval(pg_get_serial_sequence('configuracion', 'id_configuracion'), (SELECT COALESCE(MAX(id_configuracion), 1) FROM configuracion));
SELECT setval(pg_get_serial_sequence('categoria', 'id_categoria'), (SELECT COALESCE(MAX(id_categoria), 1) FROM categoria));
SELECT setval(pg_get_serial_sequence('tipo_material', 'id_tipo_material'), (SELECT COALESCE(MAX(id_tipo_material), 1) FROM tipo_material));
SELECT setval(pg_get_serial_sequence('usuario', 'id_usuario'), (SELECT COALESCE(MAX(id_usuario), 1) FROM usuario));
