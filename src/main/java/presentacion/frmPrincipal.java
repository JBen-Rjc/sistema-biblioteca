/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

/**
 *
 * @author laboratorio_computo
 */
public class frmPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmPrincipal.class.getName());

    public void establecerUsuarioSesion(modelo.Usuario usuario) {
        if (usuario != null) {
            // 1. Desbloquear los menús principales del JMenuBar
            bloquearInterfaz(false);

            // 2. Mostrar datos en la barra de estado
            lblEstadoUsuario.setText("Usuario: " + usuario.getNombres() + " " + usuario.getApellidos()
                    + " | Rol: " + usuario.getRol().getNombre());

            // 3. Evaluar permisos específicos según el Rol
            boolean esAdmin = "Administrador".equalsIgnoreCase(usuario.getRol().getNombre());

            // Si no es admin, se bloquean estas opciones específicas
            if (mniUsuarios != null) {
                mniUsuarios.setEnabled(esAdmin);
            }
            if (mniConfiguracion != null) {
                mniConfiguracion.setEnabled(esAdmin);
            }
            if (mniReportes != null) {
                mniReportes.setEnabled(esAdmin);
            }
        }
    }

    public void bloquearInterfaz(boolean bloqueado) {
        // Si bloqueado == true, deshabilita los menús; si es false, los activa
        boolean estado = !bloqueado;

        // Deshabilitar/Habilitar los menús principales del JMenuBar
        if (menuMantenimientos != null) {
            menuMantenimientos.setEnabled(estado);
        }
        if (menuProcesos != null) {
            menuProcesos.setEnabled(estado);
        }
        if (menuReportes != null) {
            menuReportes.setEnabled(estado);
        }
    }

    public void abrirFormularioInterno(javax.swing.JInternalFrame frame) {
        // 1. Si la ventana ya está abierta dentro del DesktopPane, la traemos al frente
        for (javax.swing.JInternalFrame f : desktopPane.getAllFrames()) {
            if (f.getClass().equals(frame.getClass())) {
                f.toFront();
                try {
                    f.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                    System.err.println("Error al seleccionar la ventana: " + e.getMessage());
                }
                return;
            }
        }

        // 2. Si no está abierta, se agrega al DesktopPane
        desktopPane.add(frame);

        // 3. Centrado dinámico según las dimensiones actuales del escritorio
        int x = (desktopPane.getWidth() - frame.getWidth()) / 2;
        int y = (desktopPane.getHeight() - frame.getHeight()) / 2;

        // Evitar coordenadas negativas si el frame es más grande que el desktop
        x = Math.max(0, x);
        y = Math.max(0, y);

        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    // ===== Diseño visual (no altera la lógica: reutiliza los mismos menús) =====
    private estilo.BarraLateral barraLateral;
    private final javax.swing.JLabel lblVacio = new javax.swing.JLabel();

    private void aplicarDiseno() {
        if (!(javax.swing.UIManager.getLookAndFeel() instanceof com.formdev.flatlaf.FlatLaf)) {
            estilo.Tema.instalar();
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        }
        setTitle("Biblioteca");
        setMinimumSize(new java.awt.Dimension(1100, 680));

        // Menú lateral que refleja los ítems del JMenuBar (mismos permisos y acciones)
        barraLateral = new estilo.BarraLateral("Biblioteca");
        barraLateral.agregarGrupo("Catálogo");
        barraLateral.agregarEnlace("Libros", mniLibros, menuMantenimientos);
        barraLateral.agregarEnlace("Ejemplares", mniEjemplares, menuMantenimientos);
        barraLateral.agregarGrupo("Circulación");
        barraLateral.agregarEnlace("Préstamos", mniPrestamos, menuProcesos);
        barraLateral.agregarEnlace("Devoluciones", mniDevoluciones, menuProcesos);
        barraLateral.agregarGrupo("Personas");
        barraLateral.agregarEnlace("Lectores", mniLectores, menuMantenimientos);
        barraLateral.agregarEnlace("Usuarios", mniUsuarios, menuMantenimientos);
        barraLateral.agregarGrupo("Administración");
        barraLateral.agregarEnlace("Reportes", mniReportes, menuReportes);
        barraLateral.agregarEnlace("Configuración", mniConfiguracion, menuProcesos);
        barraLateral.agregarAccion("Cerrar sesión", mniCerrarSesion);
        barraLateral.agregarAccion("Salir", jMenuItem2);
        setJMenuBar(null);

        // Barra de estado: fina y clara
        desktopPane.remove(jPanel1);
        desktopPane.setLayout(null);
        desktopPane.setBackground(estilo.Tema.PAPEL);
        jPanel1.setBackground(java.awt.Color.WHITE);
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, estilo.Tema.LINEA));
        lblEstadoUsuario.setFont(estilo.Tema.texto(java.awt.Font.PLAIN, 12));
        lblEstadoUsuario.setForeground(estilo.Tema.TINTA_SUAVE);

        // Mensaje cuando no hay ninguna ventana abierta
        lblVacio.setText("Elige una sección en el menú de la izquierda para empezar.");
        lblVacio.setFont(estilo.Tema.texto(java.awt.Font.PLAIN, 14));
        lblVacio.setForeground(estilo.Tema.TINTA_SUAVE);
        lblVacio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        desktopPane.add(lblVacio, javax.swing.JLayeredPane.FRAME_CONTENT_LAYER);
        desktopPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                lblVacio.setBounds(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
                for (javax.swing.JInternalFrame f : desktopPane.getAllFrames()) {
                    if (f instanceof frmLoginInternal) {
                        f.setLocation(Math.max(0, (desktopPane.getWidth() - f.getWidth()) / 2),
                                Math.max(0, (desktopPane.getHeight() - f.getHeight()) / 2));
                    }
                }
            }
        });

        // Las ventanas de trabajo ocupan todo el escritorio; el login queda centrado
        desktopPane.addContainerListener(new java.awt.event.ContainerAdapter() {
            @Override
            public void componentAdded(java.awt.event.ContainerEvent e) {
                if (e.getChild() instanceof javax.swing.JInternalFrame f) {
                    lblVacio.setVisible(false);
                    if (!(f instanceof frmLoginInternal)) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            try {
                                f.setMaximum(true);
                            } catch (java.beans.PropertyVetoException ex) {
                                // si no se puede maximizar se queda centrada
                            }
                        });
                    } else {
                        barraLateral.limpiarActivo();
                    }
                }
            }

            @Override
            public void componentRemoved(java.awt.event.ContainerEvent e) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    boolean hayVentanas = desktopPane.getAllFrames().length > 0;
                    lblVacio.setVisible(!hayVentanas && menuMantenimientos.isEnabled());
                    if (!hayVentanas) {
                        barraLateral.limpiarActivo();
                    }
                });
            }
        });

        javax.swing.JPanel centro = new javax.swing.JPanel(new java.awt.BorderLayout());
        centro.add(desktopPane, java.awt.BorderLayout.CENTER);
        centro.add(jPanel1, java.awt.BorderLayout.SOUTH);
        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(barraLateral, java.awt.BorderLayout.WEST);
        getContentPane().add(centro, java.awt.BorderLayout.CENTER);
    }

    public frmPrincipal() {
        initComponents();
        aplicarDiseno();
        this.setExtendedState(MAXIMIZED_BOTH);
        bloquearInterfaz(true);
        lblEstadoUsuario.setText("Estado: Sesión no iniciada. Ingrese sus credenciales.");
        javax.swing.SwingUtilities.invokeLater(() -> {
            abrirFormularioInterno(new frmLoginInternal());
        });
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        lblEstadoUsuario = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mniCerrarSesion = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuMantenimientos = new javax.swing.JMenu();
        mniLectores = new javax.swing.JMenuItem();
        mniEjemplares = new javax.swing.JMenuItem();
        mniLibros = new javax.swing.JMenuItem();
        mniUsuarios = new javax.swing.JMenuItem();
        menuProcesos = new javax.swing.JMenu();
        mniPrestamos = new javax.swing.JMenuItem();
        mniDevoluciones = new javax.swing.JMenuItem();
        mniConfiguracion = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        mniReportes = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktopPane.setBackground(new java.awt.Color(51, 51, 51));

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        lblEstadoUsuario.setBackground(new java.awt.Color(255, 255, 255));
        lblEstadoUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEstadoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblEstadoUsuario.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblEstadoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(243, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEstadoUsuario)
                .addContainerGap())
        );

        desktopPane.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, desktopPaneLayout.createSequentialGroup()
                .addGap(0, 535, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("Archivo");

        mniCerrarSesion.setText("Cerrar sesión");
        mniCerrarSesion.addActionListener(this::mniCerrarSesionActionPerformed);
        jMenu1.add(mniCerrarSesion);

        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(this::jMenuItem2ActionPerformed);
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        menuMantenimientos.setText("Mantenimiento");

        mniLectores.setText("Lectores");
        mniLectores.addActionListener(this::mniLectoresActionPerformed);
        menuMantenimientos.add(mniLectores);

        mniEjemplares.setText("Ejemplares");
        mniEjemplares.addActionListener(this::mniEjemplaresActionPerformed);
        menuMantenimientos.add(mniEjemplares);

        mniLibros.setText("Libros");
        mniLibros.addActionListener(this::mniLibrosActionPerformed);
        menuMantenimientos.add(mniLibros);

        mniUsuarios.setText("Usuarios");
        mniUsuarios.addActionListener(this::mniUsuariosActionPerformed);
        menuMantenimientos.add(mniUsuarios);

        jMenuBar1.add(menuMantenimientos);

        menuProcesos.setText("Procesos");

        mniPrestamos.setText("Registrar préstamo");
        mniPrestamos.addActionListener(this::mniPrestamosActionPerformed);
        menuProcesos.add(mniPrestamos);

        mniDevoluciones.setText("Devoluciones");
        mniDevoluciones.addActionListener(this::mniDevolucionesActionPerformed);
        menuProcesos.add(mniDevoluciones);

        mniConfiguracion.setText("Configuración");
        mniConfiguracion.addActionListener(this::mniConfiguracionActionPerformed);
        menuProcesos.add(mniConfiguracion);

        jMenuBar1.add(menuProcesos);

        menuReportes.setText("Reportes");

        mniReportes.setText("Reportes");
        mniReportes.addActionListener(this::mniReportesActionPerformed);
        menuReportes.add(mniReportes);

        jMenuBar1.add(menuReportes);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mniCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniCerrarSesionActionPerformed
        // 1. Limpiar sesión en memoria
        modelo.SesionActual.limpiarSesion();

        // 2. Cerrar todas las ventanas internas abiertas en el escritorio
        for (javax.swing.JInternalFrame frame : desktopPane.getAllFrames()) {
            frame.dispose();
        }

        // 3. Volver a bloquear la interfaz
        bloquearInterfaz(true);
        lblEstadoUsuario.setText("Estado: Sesión no iniciada.");

        // 4. Volver a mostrar el formulario de Login
        abrirFormularioInterno(new frmLoginInternal());
    }//GEN-LAST:event_mniCerrarSesionActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mniLectoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniLectoresActionPerformed
        abrirFormularioInterno(new frmLectorInternal());
    }//GEN-LAST:event_mniLectoresActionPerformed

    private void mniEjemplaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniEjemplaresActionPerformed
        abrirFormularioInterno(new frmEjemplarInternal());
    }//GEN-LAST:event_mniEjemplaresActionPerformed

    private void mniLibrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniLibrosActionPerformed
        abrirFormularioInterno(new frmLibroInternal());
    }//GEN-LAST:event_mniLibrosActionPerformed

    private void mniUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniUsuariosActionPerformed
        abrirFormularioInterno(new frmUsuarioInternal());
    }//GEN-LAST:event_mniUsuariosActionPerformed

    private void mniPrestamosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniPrestamosActionPerformed
        abrirFormularioInterno(new frmPrestamoInternal());
    }//GEN-LAST:event_mniPrestamosActionPerformed

    private void mniDevolucionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniDevolucionesActionPerformed
        abrirFormularioInterno(new frmDevolucionInternal());
    }//GEN-LAST:event_mniDevolucionesActionPerformed

    private void mniConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniConfiguracionActionPerformed
        abrirFormularioInterno(new frmConfiguracionInternal());
    }//GEN-LAST:event_mniConfiguracionActionPerformed

    private void mniReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniReportesActionPerformed
        abrirFormularioInterno(new frmReportesInternal());
    }//GEN-LAST:event_mniReportesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        // Tema visual minimalista (FlatLaf + paleta propia), ver estilo.Tema
        estilo.Tema.instalar();
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEstadoUsuario;
    private javax.swing.JMenu menuMantenimientos;
    private javax.swing.JMenu menuProcesos;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenuItem mniCerrarSesion;
    private javax.swing.JMenuItem mniConfiguracion;
    private javax.swing.JMenuItem mniDevoluciones;
    private javax.swing.JMenuItem mniEjemplares;
    private javax.swing.JMenuItem mniLectores;
    private javax.swing.JMenuItem mniLibros;
    private javax.swing.JMenuItem mniPrestamos;
    private javax.swing.JMenuItem mniReportes;
    private javax.swing.JMenuItem mniUsuarios;
    // End of variables declaration//GEN-END:variables
}
