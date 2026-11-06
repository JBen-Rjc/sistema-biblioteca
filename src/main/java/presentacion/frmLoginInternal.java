/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package presentacion;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class frmLoginInternal extends javax.swing.JInternalFrame {

    FondoPanel fondo = new FondoPanel();

    public frmLoginInternal() {

        initComponents();
        aplicarDiseno();
    }

    /** Tarjeta de acceso minimalista. Reubica los mismos controles; la lógica no cambia. */
    private void aplicarDiseno() {
        estilo.Tema.decorar(this);

        javax.swing.JPanel tarjeta = new javax.swing.JPanel(new java.awt.GridBagLayout());
        tarjeta.setBackground(java.awt.Color.WHITE);
        tarjeta.setBorder(javax.swing.BorderFactory.createEmptyBorder(36, 40, 32, 40));

        javax.swing.JLabel marca = new javax.swing.JLabel("Biblioteca");
        marca.setFont(estilo.Tema.titulo(34));
        marca.setForeground(estilo.Tema.TINTA);
        javax.swing.JLabel bajada = new javax.swing.JLabel("Ingresa con tu usuario para continuar.");
        bajada.setFont(estilo.Tema.texto(java.awt.Font.PLAIN, 13));
        bajada.setForeground(estilo.Tema.TINTA_SUAVE);

        jLabel1.setText("Usuario");
        jLabel2.setText("Contraseña");
        estilo.Tema.aplicarBoton(btnIngresar, estilo.Tema.TipoBoton.PRINCIPAL);
        estilo.Tema.aplicarBoton(btnCancelar, estilo.Tema.TipoBoton.TEXTO);
        txtUsuario.putClientProperty("JTextField.placeholderText", "Nombre de usuario");
        java.awt.Dimension campo = new java.awt.Dimension(300, 38);
        txtUsuario.setPreferredSize(campo);
        txtClave.setPreferredSize(campo);
        btnIngresar.setPreferredSize(new java.awt.Dimension(300, 40));

        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridx = 0;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;
        c.anchor = java.awt.GridBagConstraints.WEST;
        c.insets = new java.awt.Insets(0, 0, 4, 0);
        tarjeta.add(marca, c);
        c.insets = new java.awt.Insets(0, 0, 28, 0);
        tarjeta.add(bajada, c);
        c.insets = new java.awt.Insets(0, 0, 6, 0);
        tarjeta.add(jLabel1, c);
        c.insets = new java.awt.Insets(0, 0, 16, 0);
        tarjeta.add(txtUsuario, c);
        c.insets = new java.awt.Insets(0, 0, 6, 0);
        tarjeta.add(jLabel2, c);
        c.insets = new java.awt.Insets(0, 0, 24, 0);
        tarjeta.add(txtClave, c);
        c.insets = new java.awt.Insets(0, 0, 8, 0);
        tarjeta.add(btnIngresar, c);
        c.fill = java.awt.GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        c.insets = new java.awt.Insets(0, 0, 0, 0);
        tarjeta.add(btnCancelar, c);

        setContentPane(tarjeta);
        setBorder(new estilo.Tema.BordeHoja(null, 0));
        getRootPane().setDefaultButton(btnIngresar); // Enter = Ingresar
        pack();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtClave = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel1 = new FondoPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Inicio de Sesión");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Contraseña");

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtClave.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnIngresar.setBackground(new java.awt.Color(0, 102, 102));
        btnIngresar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresar.setText("Ingresar");
        btnIngresar.addActionListener(this::btnIngresarActionPerformed);

        btnCancelar.setBackground(new java.awt.Color(153, 0, 51));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Salir");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 208, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 217, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUsuario)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnIngresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                        .addGap(124, 124, 124))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        String username = txtUsuario.getText();
        String clave = new String(txtClave.getPassword());

        try {
            negocio.UsuarioNegocio negocio = new negocio.UsuarioNegocio();
            modelo.Usuario u = negocio.iniciarSesion(username, clave);

            javax.swing.JOptionPane.showMessageDialog(this,
                    "¡Bienvenido al Sistema, " + u.getNombres() + " " + u.getApellidos() + "!",
                    "Acceso Concedido",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            frmPrincipal principal = (frmPrincipal) javax.swing.SwingUtilities.getWindowAncestor(this);
            if (principal != null) {
                principal.establecerUsuarioSesion(u);
            }

            this.dispose();

        } catch (excepciones.NegocioException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso de Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (excepciones.DAOException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Base de Datos", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
    class FondoPanel extends JPanel {
    
    private Image imagen;

    public FondoPanel() {
        try {
            java.net.URL url = getClass().getResource("/imagenes/logoinicio.png");
            if (url != null) {
                imagen = new ImageIcon(url).getImage();
            } else {
                System.err.println("No se encontró la imagen en /imagenes/logoinicio.png");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
}
