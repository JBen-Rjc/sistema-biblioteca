"""
Generador de datos de demostración para el Sistema de Biblioteca.

Produce dos scripts SQL coherentes con el esquema (01_esquema.sql) y con las
reglas de negocio de la aplicación:

  02_datos_iniciales.sql  -> catálogos, configuración y usuarios del sistema
  03_datos_demo.sql       -> libros, ejemplares, lectores y un año de préstamos

Reglas que respeta la simulación:
  - Un ejemplar solo se presta si está libre (no está en un préstamo sin devolver).
  - Los ejemplares "Perdido" nunca se prestan y quedan como no disponibles.
  - Los lectores inactivos no registran préstamos (igual que en la app).
  - Un lector con un préstamo vencido sin devolver no recibe préstamos nuevos.
  - Fecha límite = fecha de préstamo + días de préstamo (por defecto DIAS_PRESTAMO).
  - Multa = días de retraso x MULTA_DIARIA_SOLES; estado "Con Retraso" si hay multa,
    "Devuelto" si no, y "Pendiente" si aún no se devuelve.
  - Al devolver, se devuelven todos los ejemplares del préstamo (como en la app).
  - ejemplar.disponible refleja el estado final real.

Todos los lectores, DNIs, teléfonos y correos son ficticios.

Uso:
    python generar_datos_demo.py            (escribe los .sql en la carpeta database/)
"""

import random
from datetime import date, datetime, timedelta
from pathlib import Path

SEMILLA = 2026
HOY = date(2026, 11, 6)           # fecha de referencia de la simulación
INICIO = date(2025, 11, 3)        # un año de historial
DIAS_PRESTAMO = 5
MULTA_DIARIA = 10.00

SALIDA = Path(__file__).resolve().parent.parent
rnd = random.Random(SEMILLA)

# Hash BCrypt (costo 12). admin -> admin123 ; bibliotecarios -> biblio123
HASH_ADMIN = "$2a$12$4soM5Hi5pL/wGnRvA6gSMu2iTbEgfKpvtBVS7aOUHFCxQScgln9ta"
HASH_BIBLIO = "$2a$12$Vx9/K5891aINvGDaUwobQeOvjPHpl7ejXu5H7.Jj.62Y/vh4fb2Iu"

# ------------------------------------------------------------------ catálogos

ROLES = [
    (1, "Administrador", "Control total del sistema"),
    (2, "Bibliotecario", "Gestión de préstamos y catálogo"),
]

CATEGORIAS = [
    (1, "Ingeniería de Software", "Metodologías, arquitectura y diseño de software"),
    (2, "Base de Datos", "Modelamiento, SQL y gestión de datos"),
    (3, "Redes y Sistemas", "Redes de computadoras y sistemas operativos"),
    (4, "Matemáticas y Estadística", "Cálculo, álgebra, probabilidad y estadística"),
    (5, "Ciencia Ficción", "Novelas de ciencia ficción y distopías"),
    (6, "Literatura Peruana", "Narrativa y poesía de autores peruanos"),
    (7, "Literatura Latinoamericana", "Narrativa latinoamericana contemporánea"),
    (8, "Literatura Universal", "Clásicos de la literatura universal"),
    (9, "Historia", "Historia del Perú y universal"),
    (10, "Administración y Negocios", "Gestión, emprendimiento y economía"),
    (11, "Psicología", "Psicología general y del desarrollo"),
]

TIPOS = [
    (1, "Libro Impreso", "Texto bibliográfico impreso"),
    (2, "Tesis", "Proyecto de titulación universitaria"),
    (3, "Revista", "Publicaciones periódicas y afines"),
]

CONFIG = [
    (1, "DIAS_PRESTAMO", str(DIAS_PRESTAMO), "Días permitidos para préstamo de ejemplares"),
    (2, "MULTA_DIARIA_SOLES", f"{MULTA_DIARIA:.2f}", "Costo de multa por cada día de retraso"),
]

USUARIOS = [
    # id, username, hash, nombres, apellidos, dni, correo, estado, rol
    (1, "admin", HASH_ADMIN, "Admin", "Demo", "00000001", "admin@ejemplo.com", True, 1),
    (2, "mlopez", HASH_BIBLIO, "María", "López Chávez", "00000002", "mlopez@ejemplo.com", True, 2),
    (3, "jcastro", HASH_BIBLIO, "Jorge", "Castro Ramírez", "00000003", "jcastro@ejemplo.com", True, 2),
    (4, "rvilchez", HASH_BIBLIO, "Rosa", "Vílchez Sánchez", "00000004", "rvilchez@ejemplo.com", True, 2),
    (5, "pdiaz", HASH_BIBLIO, "Pedro", "Díaz Montenegro", "00000005", "pdiaz@ejemplo.com", False, 2),
]
# Peso de cada usuario al registrar préstamos (pdiaz inactivo: solo los primeros meses)
PESO_USUARIO = {1: 1, 2: 5, 3: 4, 4: 4, 5: 3}
PDIAZ_HASTA = date(2026, 3, 31)

# ------------------------------------------------------------------ acervo

# (titulo, autor, editorial, año, categoria, popularidad 1-5)
LIBROS = [
    # Ingeniería de Software
    ("Código limpio", "Robert C. Martin", "Anaya Multimedia", 2012, 1, 5),
    ("Arquitectura limpia", "Robert C. Martin", "Anaya Multimedia", 2018, 1, 4),
    ("Patrones de diseño", "Erich Gamma et al.", "Addison-Wesley", 2003, 1, 4),
    ("Refactoring", "Martin Fowler", "Addison-Wesley", 2018, 1, 3),
    ("El programador pragmático", "Andrew Hunt y David Thomas", "Anaya Multimedia", 2019, 1, 4),
    ("Ingeniería del software: un enfoque práctico", "Roger S. Pressman", "McGraw-Hill", 2010, 1, 5),
    ("Ingeniería de software", "Ian Sommerville", "Pearson", 2011, 1, 5),
    ("Domain-Driven Design", "Eric Evans", "Addison-Wesley", 2003, 1, 2),
    ("Scrum: el arte de hacer el doble de trabajo en la mitad de tiempo", "Jeff Sutherland", "Ariel", 2015, 1, 3),
    ("UML gota a gota", "Martin Fowler", "Pearson", 1999, 1, 3),
    ("Java: cómo programar", "Paul Deitel y Harvey Deitel", "Pearson", 2016, 1, 5),
    ("Head First Java", "Kathy Sierra y Bert Bates", "O'Reilly", 2022, 1, 3),
    ("Effective Java", "Joshua Bloch", "Addison-Wesley", 2018, 1, 3),
    ("Introducción a los algoritmos", "Thomas H. Cormen et al.", "MIT Press", 2009, 1, 3),
    ("Estructuras de datos en Java", "Mark Allen Weiss", "Pearson", 2013, 1, 4),
    # Base de Datos
    ("Fundamentos de bases de datos", "Abraham Silberschatz et al.", "McGraw-Hill", 2014, 2, 5),
    ("Sistemas de bases de datos", "Ramez Elmasri y Shamkant Navathe", "Pearson", 2007, 2, 4),
    ("Introducción a los sistemas de bases de datos", "C. J. Date", "Pearson", 2001, 2, 3),
    ("SQL para principiantes", "Jorge Sánchez Asenjo", "Ra-Ma", 2019, 2, 4),
    ("PostgreSQL: Up and Running", "Regina Obe y Leo Hsu", "O'Reilly", 2017, 2, 3),
    ("Designing Data-Intensive Applications", "Martin Kleppmann", "O'Reilly", 2017, 2, 2),
    ("Data Warehouse Toolkit", "Ralph Kimball y Margy Ross", "Wiley", 2013, 2, 2),
    # Redes y Sistemas
    ("Redes de computadoras", "Andrew S. Tanenbaum", "Pearson", 2012, 3, 5),
    ("Sistemas operativos modernos", "Andrew S. Tanenbaum", "Pearson", 2009, 3, 4),
    ("Redes de computadoras: un enfoque descendente", "James F. Kurose y Keith W. Ross", "Pearson", 2017, 3, 4),
    ("Fundamentos de sistemas operativos", "Abraham Silberschatz et al.", "McGraw-Hill", 2006, 3, 3),
    ("Linux: guía práctica", "Sebastián Sánchez Prieto", "Ra-Ma", 2015, 3, 2),
    ("Seguridad en redes", "William Stallings", "Pearson", 2004, 3, 2),
    # Matemáticas y Estadística
    ("Cálculo de una variable", "James Stewart", "Cengage", 2012, 4, 5),
    ("Álgebra lineal y sus aplicaciones", "David C. Lay", "Pearson", 2012, 4, 4),
    ("Matemáticas discretas", "Richard Johnsonbaugh", "Pearson", 2005, 4, 4),
    ("Probabilidad y estadística para ingeniería y ciencias", "Ronald E. Walpole et al.", "Pearson", 2012, 4, 4),
    ("Estadística para administración y economía", "David R. Anderson et al.", "Cengage", 2016, 4, 3),
    ("Cálculo diferencial e integral", "Frank Ayres", "McGraw-Hill", 2010, 4, 3),
    ("Investigación de operaciones", "Hamdy A. Taha", "Pearson", 2012, 4, 3),
    # Ciencia Ficción
    ("1984", "George Orwell", "Debolsillo", 2013, 5, 5),
    ("Un mundo feliz", "Aldous Huxley", "Debolsillo", 2014, 5, 4),
    ("Fahrenheit 451", "Ray Bradbury", "Minotauro", 2012, 5, 4),
    ("Crónicas marcianas", "Ray Bradbury", "Minotauro", 2010, 5, 3),
    ("Fundación", "Isaac Asimov", "Debolsillo", 2015, 5, 4),
    ("Yo, robot", "Isaac Asimov", "Edhasa", 2009, 5, 3),
    ("Dune", "Frank Herbert", "Debolsillo", 2019, 5, 4),
    ("¿Sueñan los androides con ovejas eléctricas?", "Philip K. Dick", "Minotauro", 2012, 5, 3),
    ("Neuromante", "William Gibson", "Minotauro", 2016, 5, 2),
    ("El juego de Ender", "Orson Scott Card", "Ediciones B", 2013, 5, 3),
    ("La mano izquierda de la oscuridad", "Ursula K. Le Guin", "Minotauro", 2014, 5, 2),
    ("El problema de los tres cuerpos", "Liu Cixin", "Nova", 2016, 5, 3),
    ("Solaris", "Stanisław Lem", "Impedimenta", 2011, 5, 2),
    # Literatura Peruana
    ("La ciudad y los perros", "Mario Vargas Llosa", "Alfaguara", 2012, 6, 5),
    ("Conversación en La Catedral", "Mario Vargas Llosa", "Alfaguara", 2010, 6, 4),
    ("La casa verde", "Mario Vargas Llosa", "Alfaguara", 2011, 6, 3),
    ("La fiesta del Chivo", "Mario Vargas Llosa", "Alfaguara", 2000, 6, 4),
    ("Los ríos profundos", "José María Arguedas", "Losada", 2005, 6, 5),
    ("Todas las sangres", "José María Arguedas", "Losada", 2004, 6, 3),
    ("El zorro de arriba y el zorro de abajo", "José María Arguedas", "Losada", 2006, 6, 2),
    ("El mundo es ancho y ajeno", "Ciro Alegría", "Peisa", 2011, 6, 4),
    ("Los perros hambrientos", "Ciro Alegría", "Peisa", 2009, 6, 3),
    ("Un mundo para Julius", "Alfredo Bryce Echenique", "Anagrama", 2005, 6, 4),
    ("Tradiciones peruanas", "Ricardo Palma", "Peisa", 2008, 6, 4),
    ("Los heraldos negros", "César Vallejo", "Peisa", 2012, 6, 4),
    ("Trilce", "César Vallejo", "Peisa", 2012, 6, 3),
    ("Aves sin nido", "Clorinda Matto de Turner", "Peisa", 2010, 6, 3),
    ("La palabra del mudo", "Julio Ramón Ribeyro", "Seix Barral", 2009, 6, 4),
    ("Crónica de San Gabriel", "Julio Ramón Ribeyro", "Seix Barral", 2010, 6, 2),
    ("Abril rojo", "Santiago Roncagliolo", "Alfaguara", 2006, 6, 3),
    ("La hora azul", "Alonso Cueto", "Anagrama", 2005, 6, 3),
    # Literatura Latinoamericana
    ("Cien años de soledad", "Gabriel García Márquez", "Debolsillo", 2014, 7, 5),
    ("El amor en los tiempos del cólera", "Gabriel García Márquez", "Debolsillo", 2014, 7, 4),
    ("Crónica de una muerte anunciada", "Gabriel García Márquez", "Debolsillo", 2013, 7, 5),
    ("Pedro Páramo", "Juan Rulfo", "Cátedra", 2010, 7, 4),
    ("Rayuela", "Julio Cortázar", "Alfaguara", 2013, 7, 3),
    ("Ficciones", "Jorge Luis Borges", "Debolsillo", 2011, 7, 4),
    ("El Aleph", "Jorge Luis Borges", "Debolsillo", 2011, 7, 3),
    ("La casa de los espíritus", "Isabel Allende", "Plaza & Janés", 2012, 7, 4),
    ("El túnel", "Ernesto Sabato", "Seix Barral", 2011, 7, 4),
    ("Como agua para chocolate", "Laura Esquivel", "Debolsillo", 2010, 7, 3),
    ("El laberinto de la soledad", "Octavio Paz", "Fondo de Cultura Económica", 2004, 7, 2),
    ("Los detectives salvajes", "Roberto Bolaño", "Anagrama", 2010, 7, 2),
    ("Veinte poemas de amor y una canción desesperada", "Pablo Neruda", "Seix Barral", 2012, 7, 3),
    # Literatura Universal
    ("Don Quijote de la Mancha", "Miguel de Cervantes", "Real Academia Española", 2015, 8, 4),
    ("Crimen y castigo", "Fiódor Dostoyevski", "Alianza", 2011, 8, 4),
    ("Cien sonetos", "William Shakespeare", "Cátedra", 2009, 8, 1),
    ("Hamlet", "William Shakespeare", "Cátedra", 2010, 8, 3),
    ("Orgullo y prejuicio", "Jane Austen", "Alba", 2012, 8, 3),
    ("La metamorfosis", "Franz Kafka", "Alianza", 2010, 8, 5),
    ("El extranjero", "Albert Camus", "Alianza", 2012, 8, 3),
    ("El principito", "Antoine de Saint-Exupéry", "Salamandra", 2013, 8, 5),
    ("Madame Bovary", "Gustave Flaubert", "Alianza", 2011, 8, 2),
    ("Frankenstein", "Mary Shelley", "Alianza", 2013, 8, 3),
    ("El retrato de Dorian Gray", "Oscar Wilde", "Alianza", 2012, 8, 3),
    ("Rebelión en la granja", "George Orwell", "Debolsillo", 2013, 8, 4),
    # Historia
    ("Historia de la República del Perú", "Jorge Basadre", "El Comercio", 2005, 9, 3),
    ("Comentarios reales de los incas", "Inca Garcilaso de la Vega", "Fondo de Cultura Económica", 2005, 9, 4),
    ("Historia del Tahuantinsuyu", "María Rostworowski", "IEP", 1999, 9, 4),
    ("Sapiens: de animales a dioses", "Yuval Noah Harari", "Debate", 2015, 9, 5),
    ("Armas, gérmenes y acero", "Jared Diamond", "Debate", 2006, 9, 3),
    ("La era de la revolución, 1789-1848", "Eric Hobsbawm", "Crítica", 2009, 9, 2),
    ("Los señores de Sipán", "Walter Alva", "Fondo Editorial USMP", 2014, 9, 3),
    ("Historia mínima del Perú", "Carlos Contreras y Marcos Cueto", "El Colegio de México", 2014, 9, 3),
    # Administración y Negocios
    ("Administración", "Stephen P. Robbins y Mary Coulter", "Pearson", 2014, 10, 5),
    ("Fundamentos de marketing", "Philip Kotler y Gary Armstrong", "Pearson", 2013, 10, 4),
    ("El método Lean Startup", "Eric Ries", "Deusto", 2012, 10, 4),
    ("Generación de modelos de negocio", "Alexander Osterwalder e Yves Pigneur", "Deusto", 2011, 10, 4),
    ("Los 7 hábitos de la gente altamente efectiva", "Stephen R. Covey", "Paidós", 2011, 10, 3),
    ("Padre rico, padre pobre", "Robert T. Kiyosaki", "Aguilar", 2012, 10, 3),
    ("Contabilidad financiera", "Gerardo Guajardo", "McGraw-Hill", 2014, 10, 3),
    ("Economía", "Paul A. Samuelson y William D. Nordhaus", "McGraw-Hill", 2010, 10, 3),
    # Psicología
    ("Pensar rápido, pensar despacio", "Daniel Kahneman", "Debate", 2012, 11, 4),
    ("El hombre en busca de sentido", "Viktor E. Frankl", "Herder", 2015, 11, 5),
    ("Inteligencia emocional", "Daniel Goleman", "Kairós", 2010, 11, 4),
    ("Psicología del desarrollo", "Diane E. Papalia et al.", "McGraw-Hill", 2012, 11, 3),
    ("Introducción a la psicología", "Charles G. Morris y Albert A. Maisto", "Pearson", 2011, 11, 3),
]

TESIS = [
    # (titulo, autor, año, categoria)
    ("Sistema web para la gestión de citas en un centro de salud de Chiclayo", "Carla Fernández Ríos", 2023, 1),
    ("Aplicación móvil para el seguimiento de pedidos en una mype de Lambayeque", "Diego Salazar Muñoz", 2024, 1),
    ("Implementación de un data mart para el área de ventas de una empresa comercial", "Lucía Paredes Vega", 2022, 2),
    ("Modelo de base de datos para la historia clínica electrónica", "Andrés Quispe Mendoza", 2023, 2),
    ("Diseño de una red LAN segura para una institución educativa", "Kevin Huamán Torres", 2022, 3),
    ("Evaluación del rendimiento de servidores virtualizados con Proxmox", "Fiorella Campos Ruiz", 2024, 3),
    ("Modelo de pronóstico de demanda con series de tiempo para una distribuidora", "Renzo Alvarado Díaz", 2023, 4),
    ("Sistema de recomendación de libros basado en filtrado colaborativo", "Valeria Guevara Silva", 2025, 1),
    ("Automatización del proceso de matrícula con arquitectura de microservicios", "Jhon Castañeda Flores", 2025, 1),
    ("Plan de negocio para una cafetería temática en Chiclayo", "Sofía Medina Rojas", 2024, 10),
    ("Estrés académico y rendimiento en estudiantes universitarios", "Gabriela Ñique Bances", 2023, 11),
    ("Análisis de la deserción estudiantil mediante minería de datos", "Luis Vásquez Carrasco", 2024, 2),
]

REVISTAS = [
    # (titulo, editor, año, categoria, issn)
    ("Communications of the ACM, vol. 66, n.º 4", "ACM", 2023, 1, "0001-0782"),
    ("Communications of the ACM, vol. 67, n.º 2", "ACM", 2024, 1, "0001-0782"),
    ("IEEE Software, vol. 41, n.º 1", "IEEE", 2024, 1, "0740-7459"),
    ("IEEE Software, vol. 41, n.º 3", "IEEE", 2024, 1, "0740-7459"),
    ("National Geographic en Español, marzo 2025", "National Geographic", 2025, 9, "1138-1434"),
    ("National Geographic en Español, agosto 2025", "National Geographic", 2025, 9, "1138-1434"),
    ("Harvard Business Review en Español, n.º 345", "Harvard Business Publishing", 2025, 10, "0717-9952"),
    ("Muy Interesante, n.º 520", "Zinet Media", 2024, 11, "1130-0574"),
]

# ------------------------------------------------------------------ personas

NOMBRES_M = ["Juan", "Luis", "Carlos", "José", "Jorge", "Miguel", "Diego", "Renzo", "Kevin", "Bruno",
             "Gonzalo", "Andrés", "Fernando", "Ricardo", "Alonso", "Sebastián", "Mateo", "Adrián",
             "Víctor", "Manuel", "César", "Óscar", "Raúl", "Hugo", "Marco", "Gustavo", "Álvaro", "Piero"]
NOMBRES_F = ["María", "Ana", "Lucía", "Valeria", "Camila", "Daniela", "Fiorella", "Rosa", "Patricia",
             "Sandra", "Gabriela", "Andrea", "Carmen", "Sofía", "Alejandra", "Milagros", "Stefany",
             "Brenda", "Diana", "Karina", "Jimena", "Paola", "Claudia", "Mariela", "Ximena", "Nicole"]
SEGUNDOS = ["Alejandro", "Antonio", "Enrique", "Eduardo", "Javier", "Isabel", "Elena", "Beatriz",
            "Fernanda", "Alberto", "Teresa", "Rafael", "Victoria", "Ignacio", "Paula"]
APELLIDOS = ["Acosta", "Alvarado", "Bances", "Barturén", "Benites", "Cabrera", "Calderón", "Campos",
             "Carrasco", "Castañeda", "Castillo", "Chávez", "Cieza", "Coronado", "Cruz",
             "Delgado", "Díaz", "Effio", "Espinoza", "Farro", "Fernández", "Flores", "Fuentes", "García",
             "Gonzales", "Guevara", "Gutiérrez", "Huamán", "Jiménez", "León", "López", "Llontop",
             "Mendoza", "Medina", "Montenegro", "Morales", "Muñoz", "Ñiquen", "Paredes", "Peralta",
             "Pérez", "Quispe", "Ramírez", "Ramos", "Reyes", "Ríos", "Rojas", "Ruiz", "Salazar",
             "Sánchez", "Santisteban", "Silva", "Suárez", "Tantaleán", "Torres", "Vásquez", "Vega",
             "Vílchez", "Villegas", "Zapata"]
CALLES = ["Av. José Balta", "Av. Luis Gonzales", "Av. Sáenz Peña", "Av. Bolognesi", "Av. Grau",
          "Calle Elías Aguirre", "Calle San José", "Calle Izaga", "Av. Salaverry", "Av. Chinchaysuyo",
          "Calle Vicente de la Vega", "Av. Pedro Ruiz", "Calle Manuel María Izaga", "Av. Augusto B. Leguía"]
DISTRITOS = ["Chiclayo", "Chiclayo", "Chiclayo", "José Leonardo Ortiz", "La Victoria", "Pimentel", "Lambayeque"]

SIN_TILDE = str.maketrans("áéíóúÁÉÍÓÚñÑü", "aeiouAEIOUnNu")


def sin_tildes(s):
    return s.translate(SIN_TILDE)


# ------------------------------------------------------------------ utilidades

def sql(v):
    if v is None:
        return "NULL"
    if isinstance(v, bool):
        return "TRUE" if v else "FALSE"
    if isinstance(v, (int, float)):
        return str(v)
    if isinstance(v, datetime):
        return "'" + v.strftime("%Y-%m-%d %H:%M:%S") + "'"
    if isinstance(v, date):
        return "'" + v.isoformat() + "'"
    return "'" + str(v).replace("'", "''") + "'"


def insert(tabla, columnas, filas, por_bloque=200):
    salida = []
    for i in range(0, len(filas), por_bloque):
        bloque = filas[i:i + por_bloque]
        salida.append(f"INSERT INTO {tabla} ({', '.join(columnas)}) VALUES")
        salida.append(",\n".join("    (" + ", ".join(sql(v) for v in f) + ")" for f in bloque) + ";")
        salida.append("")
    return "\n".join(salida)


def setval(tabla, columna):
    return (f"SELECT setval(pg_get_serial_sequence('{tabla}', '{columna}'), "
            f"(SELECT COALESCE(MAX({columna}), 1) FROM {tabla}));")


def isbn13(prefijo_editor):
    """ISBN-13 ficticio con dígito de control válido (prefijo 978-612 = Perú)."""
    cuerpo = "978612" + prefijo_editor + "".join(str(rnd.randint(0, 9)) for _ in range(12 - 6 - len(prefijo_editor)))
    suma = sum(int(d) * (1 if i % 2 == 0 else 3) for i, d in enumerate(cuerpo))
    return cuerpo + str((10 - suma % 10) % 10)


# ------------------------------------------------------------------ generación

def generar_acervo():
    libros, ejemplares = [], []
    isbns = set()
    pop = {}
    id_libro = 0

    def nuevo_isbn():
        while True:
            v = isbn13(str(rnd.randint(10, 99)))
            if v not in isbns:
                isbns.add(v)
                return v

    for titulo, autor, editorial, anio, cat, p in LIBROS:
        id_libro += 1
        libros.append((id_libro, nuevo_isbn(), titulo, autor, editorial, anio, cat, 1))
        pop[id_libro] = p
    for i, (titulo, autor, anio, cat) in enumerate(TESIS, 1):
        id_libro += 1
        codigo = f"TES-{anio}-{i:03d}"
        isbns.add(codigo)
        libros.append((id_libro, codigo, titulo, autor, "Repositorio institucional", anio, cat, 2))
        pop[id_libro] = 1
    for i, (titulo, editor, anio, cat, issn) in enumerate(REVISTAS, 1):
        id_libro += 1
        codigo = f"ISSN {issn}-{i:02d}"
        isbns.add(codigo)
        libros.append((id_libro, codigo, titulo, editor, editor, anio, cat, 3))
        pop[id_libro] = 1

    # Ubicación por categoría: sala y estante
    salas = {1: "A", 2: "A", 3: "A", 4: "B", 5: "C", 6: "C", 7: "C", 8: "C", 9: "D", 10: "D", 11: "D"}
    id_ej = 0
    perdidos = 0
    for (lid, _, _, _, _, _, cat, tipo) in libros:
        if tipo == 2:
            copias = 1
        elif tipo == 3:
            copias = rnd.choice([1, 1, 2])
        else:
            copias = {5: rnd.randint(4, 6), 4: rnd.randint(3, 4), 3: rnd.randint(2, 3),
                      2: rnd.randint(1, 2), 1: 1}[pop[lid]]
        estante = rnd.randint(1, 12)
        for _ in range(copias):
            id_ej += 1
            r = rnd.random()
            if r < 0.70:
                estado = "Bueno"
            elif r < 0.92:
                estado = "Regular"
            elif r < 0.975 or perdidos >= 6:
                estado = "Dañado"
            else:
                estado = "Perdido"
                perdidos += 1
            ubic = ("Sala de Tesis - Archivo" if tipo == 2 else
                    "Hemeroteca - Repisa " + str(rnd.randint(1, 4)) if tipo == 3 else
                    f"Sala {salas[cat]} - Estante {estante}")
            ejemplares.append([id_ej, f"BIB-{id_ej:05d}", estado, ubic, True, lid])
    return libros, ejemplares, pop


def generar_lectores(n=180):
    lectores, dnis, correos = [], set(), set()
    for i in range(1, n + 1):
        mujer = rnd.random() < 0.52
        nombre = rnd.choice(NOMBRES_F if mujer else NOMBRES_M)
        if rnd.random() < 0.35:
            nombre += " " + rnd.choice(SEGUNDOS)
        ap1, ap2 = rnd.sample(APELLIDOS, 2)
        tipo = rnd.choices(["Estudiante", "Docente", "Externo"], [70, 15, 15])[0]
        while True:
            dni = str(rnd.randint(40000000, 79999999))
            if dni not in dnis:
                dnis.add(dni)
                break
        base = sin_tildes(f"{nombre.split()[0]}.{ap1}").lower().replace("ñ", "n")
        dominio = "ejemplo.edu.pe" if tipo in ("Estudiante", "Docente") else "ejemplo.com"
        correo = f"{base}@{dominio}"
        k = 2
        while correo in correos:
            correo = f"{base}{k}@{dominio}"
            k += 1
        correos.add(correo)
        telefono = "9" + "".join(str(rnd.randint(0, 9)) for _ in range(8)) if rnd.random() < 0.92 else None
        direccion = f"{rnd.choice(CALLES)} {rnd.randint(100, 1999)}, {rnd.choice(DISTRITOS)}" if rnd.random() < 0.85 else None
        activo = rnd.random() > 0.05
        lectores.append((i, dni, nombre, f"{ap1} {ap2}", telefono, correo, direccion, tipo, activo))
    return lectores


def es_dia_habil(d):
    return d.weekday() < 6  # lunes a sábado


def afluencia(d):
    """Préstamos esperados por día según el calendario académico."""
    if d.month in (1, 2):
        base = 1.2            # vacaciones
    elif d.month in (3, 8):
        base = 2.5            # inicio de ciclo
    elif d.month in (6, 7, 11, 12):
        base = 4.0            # exámenes y trabajos finales
    else:
        base = 3.2
    return base * (0.6 if d.weekday() == 5 else 1.0)


def poisson(lmbda):
    # algoritmo de Knuth
    import math
    L, k, p = math.exp(-lmbda), 0, 1.0
    while True:
        k += 1
        p *= rnd.random()
        if p <= L:
            return k - 1


def simular_prestamos(ejemplares, pop, lectores):
    libro_de = {e[0]: e[5] for e in ejemplares}
    prestables = [e[0] for e in ejemplares if e[2] != "Perdido"]
    ocupado_hasta = {}                 # id_ejemplar -> fecha en que vuelve (None = no vuelve aún)
    activos = [l for l in lectores if l[8]]
    peso_lector = {l[0]: rnd.choice([1, 1, 1, 2, 2, 3, 5]) for l in activos}
    deuda = {}                         # id_lector -> fecha límite del préstamo sin devolver más antiguo
    pendientes_lector = {}             # id_lector -> lista de (fecha_esperada, fecha_real)

    prestamos, detalles = [], []
    id_p = id_d = 0
    d = INICIO
    while d < HOY:
        if es_dia_habil(d):
            for _ in range(poisson(afluencia(d))):
                # lector sin préstamos vencidos y sin devolver a esta fecha
                candidatos = []
                for l in activos:
                    vencido = any(esp < d and (real is None or real > d)
                                  for esp, real in pendientes_lector.get(l[0], []))
                    if not vencido:
                        candidatos.append(l)
                if not candidatos:
                    continue
                lector = rnd.choices(candidatos, [peso_lector[l[0]] for l in candidatos])[0]

                libres = [e for e in prestables
                          if ocupado_hasta.get(e, date.min) is not None and ocupado_hasta.get(e, date.min) < d]
                if not libres:
                    continue
                cantidad = rnd.choices([1, 2, 3], [65, 27, 8])[0]
                elegidos = []
                for _ in range(cantidad):
                    opciones = [e for e in libres if e not in elegidos and libro_de[e] not in {libro_de[x] for x in elegidos}]
                    if not opciones:
                        break
                    elegidos.append(rnd.choices(opciones, [pop[libro_de[e]] ** 2 for e in opciones])[0])

                id_p += 1
                hora = datetime(d.year, d.month, d.day, rnd.randint(8, 19), rnd.randint(0, 59), rnd.randint(0, 59))
                dias = rnd.choices([DIAS_PRESTAMO, 7, 10, 3], [80, 10, 6, 4])[0]
                if lector[7] == "Docente" and rnd.random() < 0.5:
                    dias = 10
                esperada = d + timedelta(days=dias)

                r = rnd.random()
                if r < 0.68:
                    real = d + timedelta(days=rnd.randint(1, dias))           # a tiempo
                elif r < 0.93:
                    real = esperada + timedelta(days=rnd.randint(1, 6))       # retraso leve
                else:
                    real = esperada + timedelta(days=rnd.randint(7, 30))      # retraso grave
                if r > 0.985 and esperada < HOY - timedelta(days=20):
                    real = None                                                # nunca devuelto
                if real is not None and real >= HOY:
                    real = None                                                # aún no se devuelve

                if real is None:
                    estado, multa, fecha_real = "Pendiente", 0.00, None
                else:
                    retraso = max(0, (real - esperada).days)
                    multa = round(retraso * MULTA_DIARIA, 2)
                    estado = "Con Retraso" if multa > 0 else "Devuelto"
                    fecha_real = real

                usuarios_ok = [u for u in PESO_USUARIO if u != 5 or d <= PDIAZ_HASTA]
                usuario = rnd.choices(usuarios_ok, [PESO_USUARIO[u] for u in usuarios_ok])[0]

                prestamos.append((id_p, hora, esperada, fecha_real, multa, estado, lector[0], usuario))
                pendientes_lector.setdefault(lector[0], []).append((esperada, real))
                for e in elegidos:
                    id_d += 1
                    obs = rnd.choices([None, "Buen estado", "Con subrayado leve", "Tapa desgastada", "Sin observaciones"],
                                      [50, 25, 8, 7, 10])[0]
                    detalles.append((id_d, id_p, e, obs, None, real is not None))
                    ocupado_hasta[e] = real
        d += timedelta(days=1)

    # disponibilidad final
    for e in ejemplares:
        if e[2] == "Perdido":
            e[4] = False
        elif e[0] in ocupado_hasta and ocupado_hasta[e[0]] is None:
            e[4] = False
    return prestamos, detalles


def main():
    libros, ejemplares, pop = generar_acervo()
    lectores = generar_lectores()
    prestamos, detalles = simular_prestamos(ejemplares, pop, lectores)

    cabecera = "SET client_encoding = 'UTF8';\n\n"

    # ---- 02: catálogos y usuarios
    p2 = [cabecera,
          "-- =============================================================",
          "--  Datos iniciales: catálogos, configuración y usuarios del sistema",
          "--  Generado por database/generador/generar_datos_demo.py",
          "--  Usuarios de prueba:  admin / admin123   |   mlopez, jcastro, rvilchez / biblio123",
          "-- =============================================================", "",
          insert("rol", ["id_rol", "nombre", "descripcion"], ROLES),
          insert("configuracion", ["id_configuracion", "clave", "valor", "descripcion"], CONFIG),
          insert("categoria", ["id_categoria", "nombre", "descripcion"], CATEGORIAS),
          insert("tipo_material", ["id_tipo_material", "nombre", "descripcion"], TIPOS),
          insert("usuario", ["id_usuario", "username", "clave", "nombres", "apellidos", "dni", "correo", "estado", "id_rol"], USUARIOS),
          setval("rol", "id_rol"), setval("configuracion", "id_configuracion"),
          setval("categoria", "id_categoria"), setval("tipo_material", "id_tipo_material"),
          setval("usuario", "id_usuario"), ""]
    (SALIDA / "02_datos_iniciales.sql").write_text("\n".join(p2), encoding="utf-8")

    # ---- 03: acervo, lectores y préstamos
    n_pend = sum(1 for p in prestamos if p[5] == "Pendiente")
    n_ret = sum(1 for p in prestamos if p[5] == "Con Retraso")
    multas = sum(p[4] for p in prestamos)
    resumen = [
        f"--  Libros/tesis/revistas: {len(libros)}   Ejemplares: {len(ejemplares)}   Lectores: {len(lectores)}",
        f"--  Préstamos: {len(prestamos)} ({len(detalles)} ejemplares prestados) del {INICIO} al {HOY - timedelta(days=1)}",
        f"--  Pendientes: {n_pend}   Devueltos con retraso: {n_ret}   Multas totales: S/ {multas:,.2f}",
    ]
    p3 = [cabecera,
          "-- =============================================================",
          "--  Datos de demostración (ficticios) - requiere 02_datos_iniciales.sql",
          "--  Generado por database/generador/generar_datos_demo.py (semilla fija)",
          *resumen,
          "-- =============================================================", "",
          "BEGIN;", "",
          insert("libro", ["id_libro", "isbn", "titulo", "autor", "editorial", "anio_publicacion", "id_categoria", "id_tipo_material"], libros),
          insert("ejemplar", ["id_ejemplar", "codigo_patrimonial", "estado_conservacion", "ubicacion", "disponible", "id_libro"], [tuple(e) for e in ejemplares]),
          insert("lector", ["id_lector", "dni", "nombres", "apellidos", "telefono", "correo", "direccion", "tipo_lector", "estado"], lectores),
          insert("prestamo", ["id_prestamo", "fecha_prestamo", "fecha_devolucion_esperada", "fecha_devolucion_real", "monto_multa", "estado", "id_lector", "id_usuario"], prestamos),
          insert("detalle_prestamo", ["id_detalle", "id_prestamo", "id_ejemplar", "observacion_entrega", "observacion_devolucion", "devuelto"], detalles),
          setval("libro", "id_libro"), setval("ejemplar", "id_ejemplar"), setval("lector", "id_lector"),
          setval("prestamo", "id_prestamo"), setval("detalle_prestamo", "id_detalle"), "",
          "COMMIT;", ""]
    (SALIDA / "03_datos_demo.sql").write_text("\n".join(p3), encoding="utf-8")
    print("\n".join(r[4:] for r in resumen))


if __name__ == "__main__":
    main()
