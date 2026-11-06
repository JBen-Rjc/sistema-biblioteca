package estilo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Menú lateral. Cada enlace "refleja" un JMenuItem existente: al pulsarlo
 * ejecuta la misma acción y se habilita/deshabilita igual que él, así los
 * permisos por rol del formulario principal siguen funcionando sin cambios.
 */
public class BarraLateral extends JPanel {

    private static final Color TEXTO = new Color(0xC9D6D4);
    private static final Color TEXTO_TENUE = new Color(0x7F9693);
    private static final Color HOVER = new Color(0x1B3A37);
    private static final Color ACTIVO = new Color(0x22504B);

    private final JPanel lista = new JPanel();
    private final JPanel pie = new JPanel();
    private final List<Enlace> enlaces = new ArrayList<>();
    private Enlace activo;

    public BarraLateral(String marca) {
        super(new BorderLayout());
        setBackground(Tema.LOMO_OSCURO);
        setPreferredSize(new Dimension(232, 10));

        JLabel lblMarca = new JLabel(marca);
        lblMarca.setFont(Tema.titulo(24));
        lblMarca.setForeground(Color.WHITE);
        lblMarca.setBorder(new EmptyBorder(26, 24, 22, 16));
        add(lblMarca, BorderLayout.NORTH);

        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        add(lista, BorderLayout.CENTER);

        pie.setOpaque(false);
        pie.setLayout(new BoxLayout(pie, BoxLayout.Y_AXIS));
        pie.setBorder(new EmptyBorder(8, 0, 16, 0));
        add(pie, BorderLayout.SOUTH);
    }

    public void agregarGrupo(String nombre) {
        JLabel l = new JLabel(nombre);
        l.setFont(Tema.texto(Font.PLAIN, 12));
        l.setForeground(TEXTO_TENUE);
        l.setBorder(new EmptyBorder(lista.getComponentCount() == 0 ? 4 : 20, 24, 6, 16));
        l.setAlignmentX(LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, l.getPreferredSize().height));
        lista.add(l);
    }

    /** Enlace de navegación (queda marcado mientras su ventana está abierta). */
    public void agregarEnlace(String texto, JMenuItem destino, JMenu menuPadre) {
        lista.add(new Enlace(texto, destino, menuPadre, true));
    }

    /** Acción al pie de la barra (cerrar sesión, salir). */
    public void agregarAccion(String texto, JMenuItem destino) {
        pie.add(new Enlace(texto, destino, null, false));
    }

    /** Quita la marca del enlace activo (p. ej. cuando no queda ninguna ventana abierta). */
    public void limpiarActivo() {
        marcar(null);
    }

    private void marcar(Enlace e) {
        activo = e;
        for (Enlace x : enlaces) {
            x.repaint();
        }
    }

    private class Enlace extends JComponent {

        private final String texto;
        private final JMenuItem destino;
        private final JMenu menuPadre;
        private final boolean navegable;
        private boolean encima;

        Enlace(String texto, JMenuItem destino, JMenu menuPadre, boolean navegable) {
            this.texto = texto;
            this.destino = destino;
            this.menuPadre = menuPadre;
            this.navegable = navegable;
            enlaces.add(this);
            setAlignmentX(LEFT_ALIGNMENT);
            setFont(Tema.texto(Font.PLAIN, 14));
            setPreferredSize(new Dimension(232, 38));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            setToolTipText(null);

            destino.addPropertyChangeListener("enabled", ev -> sincronizar());
            if (menuPadre != null) {
                menuPadre.addPropertyChangeListener("enabled", ev -> sincronizar());
            }
            sincronizar();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    encima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    encima = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!isEnabled()) {
                        return;
                    }
                    if (navegable) {
                        marcar(Enlace.this);
                    }
                    destino.doClick(0);
                }
            });
        }

        private void sincronizar() {
            boolean ok = destino.isEnabled() && (menuPadre == null || menuPadre.isEnabled());
            setEnabled(ok);
            setCursor(ok ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            boolean esActivo = activo == this;
            if (isEnabled() && (esActivo || encima)) {
                g2.setColor(esActivo ? ACTIVO : HOVER);
                g2.fillRoundRect(12, 3, getWidth() - 24, getHeight() - 6, 8, 8);
            }
            if (esActivo) {
                g2.setColor(new Color(0x8FC7BF));
                g2.fillRoundRect(12, 11, 3, getHeight() - 22, 3, 3);
            }
            g2.setFont(esActivo ? Tema.texto(Font.BOLD, 14) : getFont());
            g2.setColor(!isEnabled() ? new Color(0x4D6461) : esActivo ? Color.WHITE : TEXTO);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, 26, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }
}
