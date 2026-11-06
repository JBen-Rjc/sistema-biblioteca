package estilo;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

/**
 * Apariencia visual del sistema (sólo presentación, no toca la lógica).
 *
 * Paleta: papel gris frío de fondo, hojas blancas para cada sección y un
 * único acento verde "lomo de libro". Títulos en Literata (tipografía
 * diseñada para lectura de libros) y la interfaz en Inter.
 *
 * Uso: llamar a {@link #instalar()} al iniciar la aplicación y a
 * {@link #decorar(Container)} al final del constructor de cada formulario.
 */
public final class Tema {

    // ---- Paleta -----------------------------------------------------------
    public static final Color PAPEL = new Color(0xF3F4F2);       // fondo general
    public static final Color HOJA = Color.WHITE;                 // secciones
    public static final Color TINTA = new Color(0x1D2928);        // texto principal
    public static final Color TINTA_SUAVE = new Color(0x5E6A69);  // etiquetas, texto secundario
    public static final Color LINEA = new Color(0xE0E4E2);        // bordes finos
    public static final Color LOMO = new Color(0x1F5C57);         // acento
    public static final Color LOMO_CLARO = new Color(0xE3EEEC);   // selección
    public static final Color LOMO_OSCURO = new Color(0x132B29);  // barra lateral
    public static final Color ALERTA = new Color(0xA8382F);       // acciones destructivas

    private static Font literata;
    private static boolean instalado;

    private Tema() {
    }

    // ---- Instalación global -------------------------------------------------
    public static void instalar() {
        if (instalado) {
            return;
        }
        instalado = true;

        FlatInterFont.install();
        FlatLaf.setPreferredFontFamily(FlatInterFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatInterFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatInterFont.FAMILY_SEMIBOLD);
        FlatLaf.setGlobalExtraDefaults(Map.of(
                "@accentColor", hex(LOMO),
                "@background", hex(PAPEL),
                "@foreground", hex(TINTA)));
        FlatLightLaf.setup();

        UIManager.put("defaultFont", new FontUIResource(FlatInterFont.FAMILY, Font.PLAIN, 13));

        // Formas
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.borderColor", LINEA.darker());
        UIManager.put("Component.focusedBorderColor", LOMO);
        UIManager.put("Component.focusColor", new Color(0x401F5C57, true));
        UIManager.put("Button.margin", new Insets(6, 16, 6, 16));
        UIManager.put("TextField.margin", new Insets(4, 8, 4, 8));
        UIManager.put("PasswordField.margin", new Insets(4, 8, 4, 8));

        // Campos sobre fondo blanco
        for (String k : new String[]{"TextField", "PasswordField", "FormattedTextField", "TextArea", "ComboBox"}) {
            UIManager.put(k + ".background", HOJA);
        }
        UIManager.put("ComboBox.buttonBackground", HOJA);
        UIManager.put("ComboBox.buttonEditableBackground", HOJA);
        UIManager.put("Label.foreground", TINTA);

        // Tablas
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
        UIManager.put("Table.gridColor", LINEA);
        UIManager.put("Table.background", HOJA);
        UIManager.put("Table.alternateRowColor", null);
        UIManager.put("Table.selectionBackground", LOMO_CLARO);
        UIManager.put("Table.selectionForeground", TINTA);
        UIManager.put("Table.selectionInactiveBackground", LOMO_CLARO);
        UIManager.put("Table.selectionInactiveForeground", TINTA);
        UIManager.put("Table.cellMargins", new Insets(2, 10, 2, 10));
        UIManager.put("TableHeader.height", 34);
        UIManager.put("TableHeader.background", HOJA);
        UIManager.put("TableHeader.foreground", TINTA_SUAVE);
        UIManager.put("TableHeader.separatorColor", HOJA);
        UIManager.put("TableHeader.bottomSeparatorColor", LINEA);
        UIManager.put("TableHeader.cellMargins", new Insets(2, 10, 2, 10));
        UIManager.put("ScrollPane.background", HOJA);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.track", HOJA);

        UIManager.put("Desktop.background", PAPEL);
        UIManager.put("OptionPane.background", HOJA);
        UIManager.put("Panel.background", PAPEL);
    }

    // ---- Tipografía -------------------------------------------------------
    /** Fuente de títulos (Literata). */
    public static Font titulo(float tamano) {
        if (literata == null) {
            try (var in = Tema.class.getResourceAsStream("/fuentes/Literata.ttf")) {
                literata = Font.createFont(Font.TRUETYPE_FONT, in);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(literata);
            } catch (Exception e) {
                literata = new Font(Font.SERIF, Font.PLAIN, 12);
            }
        }
        return literata.deriveFont(Font.PLAIN, tamano);
    }

    public static Font texto(int estilo, float tamano) {
        String familia = estilo == Font.BOLD ? FlatInterFont.FAMILY_SEMIBOLD : FlatInterFont.FAMILY;
        return new Font(familia, Font.PLAIN, Math.round(tamano)).deriveFont(tamano);
    }

    // ---- Decoración de formularios -----------------------------------------
    /** Aplica el estilo a una ventana o panel y a todos sus componentes. */
    public static void decorar(Container raiz) {
        if (raiz instanceof JInternalFrame f) {
            decorarVentanaInterna(f);
        } else if (raiz instanceof RootPaneContainer rpc) {
            rpc.getContentPane().setBackground(PAPEL);
            if (rpc.getContentPane() instanceof JComponent jc) {
                jc.setBorder(new EmptyBorder(12, 12, 12, 12));
            }
        }
        recorrer(raiz);
        if (raiz instanceof Window w && !w.isVisible()) {
            w.pack(); // recalcula el tamaño con los nuevos márgenes
        }
    }

    private static void recorrer(Component c) {
        if (c instanceof JComponent jc && jc.getClientProperty("tema.fijo") != null) {
            return; // elementos ya diseñados por el tema
        }
        if (c instanceof JLabel l) {
            l.setFont(texto(Font.PLAIN, 13));
            l.setForeground(TINTA_SUAVE);
        } else if (c instanceof JButton b) {
            estiloBoton(b);
        } else if (c instanceof JTextComponent t) {
            t.setFont(texto(Font.PLAIN, 13));
        } else if (c instanceof JComboBox<?> cb) {
            cb.setFont(texto(Font.PLAIN, 13));
        } else if (c instanceof JTable t) {
            estiloTabla(t);
        } else if (c instanceof JScrollPane sp && sp.getClientProperty("tema.marco") == null) {
            // Dentro de una sección ya hay hoja: la tabla se apoya directamente en ella.
            boolean enHoja = sp.getParent() instanceof JComponent p && p.getBorder() instanceof BordeHoja;
            sp.setBorder(enHoja ? BorderFactory.createEmptyBorder() : new BordeHoja(null, 3));
            sp.getViewport().setBackground(HOJA);
            sp.setOpaque(false);
            sp.getViewport().setOpaque(true);
        }
        if (c instanceof JPanel p && p.getBorder() instanceof TitledBorder tb) {
            p.setOpaque(false);
            p.setBorder(new BordeHoja(tb.getTitle(), 16));
        }
        if (c instanceof Container k && !(c instanceof JTable) && !(c instanceof JComboBox)) {
            for (Component h : k.getComponents()) {
                recorrer(h);
            }
        }
    }

    private static void decorarVentanaInterna(JInternalFrame f) {
        // Se reemplaza la barra de título por un encabezado tipográfico.
        if (f.getUI() instanceof BasicInternalFrameUI ui) {
            ui.setNorthPane(null);
        }
        f.setBorder(new BordeHoja(null, 0));
        f.setFrameIcon(null);

        Container contenido = f.getContentPane();
        contenido.setBackground(PAPEL);

        JLabel titulo = new JLabel(f.getTitle());
        titulo.setFont(titulo(26));
        titulo.setForeground(TINTA);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(18, 24, 10, 16));
        cabecera.putClientProperty("tema.fijo", Boolean.TRUE);
        cabecera.add(titulo, BorderLayout.WEST);
        if (f.isClosable()) {
            JButton cerrar = new JButton("Cerrar");
            aplicarBoton(cerrar, TipoBoton.TEXTO);
            cerrar.setFocusable(false);
            cerrar.addActionListener(e -> f.doDefaultCloseAction());
            cabecera.add(cerrar, BorderLayout.EAST);
        }

        JPanel marco = new JPanel(new BorderLayout());
        marco.setBackground(PAPEL);
        marco.add(cabecera, BorderLayout.NORTH);
        if (contenido instanceof JComponent jc) {
            jc.setBorder(new EmptyBorder(0, 14, 14, 14));
        }
        // Si la pantalla es pequeña, el formulario se desplaza en lugar de recortarse.
        // El formulario conserva el tamaño con el que se diseñó, alineado arriba a la izquierda.
        JPanel soporte = new JPanel(new GridBagLayout());
        soporte.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 1;
        gc.weighty = 1;
        soporte.add(contenido, gc);
        JScrollPane desplazable = new JScrollPane(soporte);
        desplazable.setBorder(BorderFactory.createEmptyBorder());
        desplazable.setOpaque(false);
        desplazable.getViewport().setOpaque(false);
        desplazable.getVerticalScrollBar().setUnitIncrement(16);
        desplazable.getHorizontalScrollBar().setUnitIncrement(16);
        desplazable.putClientProperty("tema.marco", Boolean.TRUE);
        marco.add(desplazable, BorderLayout.CENTER);
        f.setContentPane(marco);
        f.putClientProperty("tema.decorado", Boolean.TRUE);
    }

    // ---- Botones ----------------------------------------------------------
    public enum TipoBoton { PRINCIPAL, SECUNDARIO, PELIGRO, TEXTO }

    private static void estiloBoton(JButton b) {
        String t = sinTildes(b.getText());
        TipoBoton tipo = TipoBoton.SECUNDARIO;
        if (t.startsWith("guardar") || t.startsWith("registrar") || t.startsWith("ingresar")
                || t.startsWith("generar") || t.startsWith("seleccionar") || t.startsWith("procesar")) {
            tipo = TipoBoton.PRINCIPAL;
        } else if (t.startsWith("cancelar") || t.startsWith("quitar") || t.startsWith("eliminar")
                || t.startsWith("salir")) {
            tipo = TipoBoton.PELIGRO;
        }
        aplicarBoton(b, tipo);
    }

    public static void aplicarBoton(JButton b, TipoBoton tipo) {
        b.setUI(new BotonUI(tipo));
        b.setFont(texto(Font.BOLD, 13));
        b.setRolloverEnabled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Botón dibujado a mano: alto fijo de 38 px centrado en el espacio que le
     * da el diseñador, así un botón estirado en el formulario no se ve gigante.
     */
    static class BotonUI extends javax.swing.plaf.basic.BasicButtonUI {

        private static final int ALTO = 38;
        private final TipoBoton tipo;

        BotonUI(TipoBoton tipo) {
            this.tipo = tipo;
        }

        @Override
        public void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setFocusPainted(false);
            b.setBorder(new EmptyBorder(0, 16, 0, 16));
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            AbstractButton b = (AbstractButton) c;
            FontMetrics fm = c.getFontMetrics(b.getFont());
            return new Dimension(fm.stringWidth(b.getText()) + 36, ALTO);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = c.getWidth();
            int h = Math.min(c.getHeight(), ALTO);
            int y = (c.getHeight() - h) / 2;
            boolean activo = b.isEnabled();
            boolean encima = activo && m.isRollover();
            boolean presionado = activo && m.isArmed() && m.isPressed();

            Color fondo;
            Color borde;
            Color texto;
            switch (tipo) {
                case PRINCIPAL -> {
                    fondo = !activo ? new Color(0xC6D3D1) : presionado ? new Color(0x143F3C) : encima ? new Color(0x194D49) : LOMO;
                    borde = null;
                    texto = Color.WHITE;
                }
                case PELIGRO -> {
                    fondo = presionado ? new Color(0xF6E6E4) : encima ? new Color(0xFBF3F2) : HOJA;
                    borde = encima ? ALERTA : new Color(0xE6CFCC);
                    texto = activo ? ALERTA : new Color(0xC9B5B2);
                }
                case TEXTO -> {
                    fondo = encima ? new Color(0xE9ECEA) : null;
                    borde = null;
                    texto = activo ? TINTA_SUAVE : LINEA;
                }
                default -> {
                    fondo = !activo ? PAPEL : presionado ? new Color(0xEAF1EF) : encima ? new Color(0xF4F8F7) : HOJA;
                    borde = encima ? LOMO : new Color(0xD3D9D7);
                    texto = activo ? TINTA : new Color(0xA7B0AE);
                }
            }
            if (fondo != null) {
                g2.setColor(fondo);
                g2.fillRoundRect(0, y, w, h, 8, 8);
            }
            if (borde != null) {
                g2.setColor(borde);
                g2.drawRoundRect(0, y, w - 1, h - 1, 8, 8);
            }
            if (b.isFocusOwner() && b.isFocusable() && activo) {
                g2.setColor(new Color(0x661F5C57, true));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, y + 1, w - 3, h - 3, 8, 8);
            }
            g2.setFont(b.getFont());
            g2.setColor(texto);
            FontMetrics fm = g2.getFontMetrics();
            String s = b.getText();
            int tx = (w - fm.stringWidth(s)) / 2;
            int ty = y + (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(s, Math.max(8, tx), ty);
            g2.dispose();
        }
    }

    // ---- Tablas -----------------------------------------------------------
    private static void estiloTabla(JTable t) {
        t.setFont(texto(Font.PLAIN, 13));
        t.setRowHeight(30);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(LINEA);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setFillsViewportHeight(true);
        t.setBackground(HOJA);
        t.setForeground(TINTA);
        JTableHeader h = t.getTableHeader();
        if (h != null) {
            h.setFont(texto(Font.BOLD, 12));
            h.setForeground(TINTA_SUAVE);
            h.setBackground(HOJA);
            h.setReorderingAllowed(false);
        }
        // Columnas legibles: si no caben, se activa el desplazamiento horizontal.
        anchosColumnas(t);
        t.addPropertyChangeListener("columnModel", e -> anchosColumnas(t));
        t.getModel().addTableModelListener(e -> {
            if (e.getFirstRow() == javax.swing.event.TableModelEvent.HEADER_ROW) {
                SwingUtilities.invokeLater(() -> anchosColumnas(t));
            }
        });
        t.addPropertyChangeListener("model", e -> SwingUtilities.invokeLater(() -> anchosColumnas(t)));
        if (t.getParent() instanceof JViewport vp) {
            vp.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    modoColumnas(t);
                }
            });
        }
    }

    /** Ancho cómodo por columna ("ID" y similares, más angostas). */
    private static void anchosColumnas(JTable t) {
        for (int i = 0; i < t.getColumnCount(); i++) {
            var col = t.getColumnModel().getColumn(i);
            int ancho = String.valueOf(col.getHeaderValue()).length() <= 2 ? 64 : 130;
            col.setPreferredWidth(ancho);
        }
        modoColumnas(t);
    }

    /** Si las columnas no caben en el ancho visible se activa el desplazamiento horizontal. */
    private static void modoColumnas(JTable t) {
        if (!(t.getParent() instanceof JViewport vp) || vp.getWidth() == 0) {
            return;
        }
        int minimo = 0;
        for (int i = 0; i < t.getColumnCount(); i++) {
            minimo += t.getColumnModel().getColumn(i).getPreferredWidth();
        }
        int modo = vp.getWidth() >= minimo ? JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS : JTable.AUTO_RESIZE_OFF;
        if (t.getAutoResizeMode() != modo) {
            t.setAutoResizeMode(modo);
        }
    }

    // ---- Utilidades --------------------------------------------------------
    private static String sinTildes(String s) {
        if (s == null) {
            return "";
        }
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT).trim();
    }

    private static String hex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    /**
     * Hoja blanca con esquinas suaves y línea fina; si tiene título lo dibuja
     * arriba a la izquierda. Reemplaza los TitledBorder del diseñador.
     */
    public static class BordeHoja extends AbstractBorder {

        private final String titulo;
        private final int relleno;
        private static final int ARCO = 12;

        public BordeHoja(String titulo, int relleno) {
            this.titulo = titulo == null || titulo.isBlank() ? null : titulo;
            this.relleno = relleno;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            RoundRectangle2D r = new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, w - 1, h - 1, ARCO, ARCO);
            if (!c.isOpaque()) {
                g2.setColor(HOJA);
                g2.fill(r);
            }
            g2.setColor(LINEA);
            g2.draw(r);
            if (titulo != null) {
                g2.setFont(texto(Font.BOLD, 13));
                g2.setColor(TINTA);
                g2.drawString(titulo, x + relleno, y + 14 + g2.getFontMetrics().getAscent());
            }
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c, Insets i) {
            int arriba = titulo != null ? 42 : Math.max(1, relleno);
            int lado = Math.max(1, relleno);
            i.set(arriba, lado, lado, lado);
            return i;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    /** Espacio vacío; atajo para los formularios. */
    public static Border espacio(int arriba, int izq, int abajo, int der) {
        return new EmptyBorder(arriba, izq, abajo, der);
    }
}
