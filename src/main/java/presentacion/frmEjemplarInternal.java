package presentacion;

import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

public class frmEjemplarInternal extends javax.swing.JInternalFrame {

    private List<Libro> listaLibrosMemoria = new ArrayList<>();
    
    public frmEjemplarInternal() {
        initComponents();
        estilo.Tema.decorar(this); // aspecto visual
        configurarComboEstados();
        cargarComboLibros();
        cargarTablaEjemplares();
    }
    
    private void configurarComboEstados() {
        cboEstadoConservacion.removeAllItems();
        cboEstadoConservacion.addItem("Bueno");
        cboEstadoConservacion.addItem("Regular");
        cboEstadoConservacion.addItem("Dañado");
        cboEstadoConservacion.addItem("Perdido");
    }

    private void cargarComboLibros() {
        try {
            negocio.LibroNegocio negocioLibro = new negocio.LibroNegocio();
            listaLibrosMemoria = negocioLibro.listarTodos();

            cboLibro.removeAllItems();
            for (Libro l : listaLibrosMemoria) {
                // Se agrega el título para evitar conflictos de genéricos en NetBeans
                cboLibro.addItem(l.getTitulo());
            }
        } catch (excepciones.DAOException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Libro obtenerLibroSeleccionado() {
        int index = cboLibro.getSelectedIndex();
        if (index >= 0 && index < listaLibrosMemoria.size()) {
            return listaLibrosMemoria.get(index);
        }
        return null;
    }

    private void cargarTablaEjemplares() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                new String[]{"ID", "Código Patrimonial", "Ubicación", "Estado Conservación", "Disponible"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            Libro libroSeleccionado = obtenerLibroSeleccionado();

            if (libroSeleccionado != null && libroSeleccionado.getIdLibro() > 0) {
                negocio.EjemplarNegocio negocio = new negocio.EjemplarNegocio();
                java.util.List<modelo.Ejemplar> lista = negocio.listarPorLibro(libroSeleccionado.getIdLibro());

                for (modelo.Ejemplar ej : lista) {
                    modelo.addRow(new Object[]{
                        ej.getIdEjemplar(),
                        ej.getCodigoPatrimonial(),
                        ej.getUbicacion(),
                        ej.getEstadoConservacion(),
                        ej.isDisponible() ? "Disponible" : "Prestado"
                    });
                }
            }
            tblEjemplares.setModel(modelo);

        } catch (excepciones.DAOException e) {
            javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Base de Datos", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtCodigoPatrimonial = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        txtUbicacion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cboEstadoConservacion = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboLibro = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEjemplares = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ejemplares");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del ejemplar"));

        btnNuevo.setBackground(new java.awt.Color(0, 102, 153));
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        jLabel3.setText("Código patrimonial");

        jLabel4.setText("Ubicación");

        cboEstadoConservacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Conservación");

        cboLibro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLibro.addActionListener(this::cboLibroActionPerformed);

        jLabel1.setText("Libro");

        btnGuardar.setBackground(new java.awt.Color(0, 102, 102));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCodigoPatrimonial)
                    .addComponent(txtUbicacion)
                    .addComponent(cboEstadoConservacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoPatrimonial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboEstadoConservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ejemplares registrados"));

        tblEjemplares.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblEjemplares);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            Libro libroSeleccionado = obtenerLibroSeleccionado();

            modelo.Ejemplar ej = new modelo.Ejemplar();
            ej.setCodigoPatrimonial(txtCodigoPatrimonial.getText().trim());
            ej.setUbicacion(txtUbicacion.getText().trim());
            ej.setEstadoConservacion(cboEstadoConservacion.getSelectedItem().toString());
            ej.setDisponible(true);
            ej.setLibro(libroSeleccionado);

            negocio.EjemplarNegocio negocio = new negocio.EjemplarNegocio();
            negocio.registrarEjemplar(ej);

            javax.swing.JOptionPane.showMessageDialog(this, "Ejemplar registrado correctamente.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            txtCodigoPatrimonial.setText("");
            txtUbicacion.setText("");
            cargarTablaEjemplares();

        } catch (excepciones.NegocioException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (excepciones.DAOException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Base de Datos", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cboLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLibroActionPerformed
        cargarTablaEjemplares();
    }//GEN-LAST:event_cboLibroActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        txtCodigoPatrimonial.setText("");
        txtUbicacion.setText("");
        if (cboEstadoConservacion.getItemCount() > 0) {
            cboEstadoConservacion.setSelectedIndex(0);
        }
        txtCodigoPatrimonial.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cboEstadoConservacion;
    private javax.swing.JComboBox<String> cboLibro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEjemplares;
    private javax.swing.JTextField txtCodigoPatrimonial;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables
}
