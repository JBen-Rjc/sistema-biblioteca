package presentacion;

import excepciones.DAOException;
import excepciones.NegocioException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DetallePrestamo;
import modelo.Ejemplar;
import modelo.Lector;
import modelo.Prestamo;
import modelo.SesionActual;
import modelo.Usuario;
import negocio.LectorNegocio;
import negocio.PrestamoNegocio;

public class frmPrestamoInternal extends javax.swing.JInternalFrame {

    private Lector lectorSeleccionado = null;
    private List<DetallePrestamo> listaDetalle = new ArrayList<>();
    private DefaultTableModel modeloTabla;

    public frmPrestamoInternal() {
        initComponents();
        estilo.Tema.decorar(this); // aspecto visual
        configurarTabla();
        txtNombreLector.setEditable(false);

        // Cargar días de préstamo desde la BD
        try {
            negocio.ConfiguracionNegocio configNegocio = new negocio.ConfiguracionNegocio();
            int diasBD = configNegocio.obtenerDiasPrestamo();
            txtDiasPrestamo.setText(String.valueOf(diasBD));
        } catch (excepciones.DAOException e) {
            txtDiasPrestamo.setText("3"); // Valor por defecto si ocurre un error
        }
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new String[]{"ID Ejemplar", "Código", "Título del Libro", "Ubicación", "Estado Conservación", "Observación"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalle.setModel(modeloTabla);
    }

    private void actualizarTablaUI() {
        modeloTabla.setRowCount(0);
        for (DetallePrestamo det : listaDetalle) {
            Ejemplar ej = det.getEjemplar();
            modeloTabla.addRow(new Object[]{
                ej.getIdEjemplar(),
                ej.getCodigoPatrimonial(),
                ej.getLibro() != null ? ej.getLibro().getTitulo() : "N/A",
                ej.getUbicacion(),
                ej.getEstadoConservacion(),
                det.getObservacionEntrega()
            });
        }
    }

    private void limpiarFormularioCompleto() {
        lectorSeleccionado = null;
        txtDniLector.setText("");
        txtNombreLector.setText("");
        txtCodigoPatrimonial.setText("");
        txtObservacion.setText("");
        listaDetalle.clear();
        actualizarTablaUI();
        txtDniLector.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDniLector = new javax.swing.JTextField();
        btnBuscarLector = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNombreLector = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCodigoPatrimonial = new javax.swing.JTextField();
        btnAgregarEjemplar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtObservacion = new javax.swing.JTextField();
        btnBuscarEjemplar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtDiasPrestamo = new javax.swing.JTextField();
        btnQuitarItem = new javax.swing.JButton();
        btnRegistrarPrestamo = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Préstamos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Lector"));

        jLabel1.setText("DNI del lector");

        btnBuscarLector.setBackground(new java.awt.Color(229, 169, 60));
        btnBuscarLector.setText("Buscar");
        btnBuscarLector.addActionListener(this::btnBuscarLectorActionPerformed);

        jLabel2.setText("Lector");

        txtNombreLector.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtDniLector, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnBuscarLector, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNombreLector, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDniLector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarLector))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreLector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ejemplares a prestar"));

        jLabel3.setText("Código patrimonial");

        btnAgregarEjemplar.setBackground(new java.awt.Color(112, 193, 179));
        btnAgregarEjemplar.setText("Agregar");
        btnAgregarEjemplar.addActionListener(this::btnAgregarEjemplarActionPerformed);

        jLabel4.setText("Observación");

        btnBuscarEjemplar.setBackground(new java.awt.Color(229, 169, 60));
        btnBuscarEjemplar.setText("Buscar");
        btnBuscarEjemplar.addActionListener(this::btnBuscarEjemplarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCodigoPatrimonial, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAgregarEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtObservacion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCodigoPatrimonial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarEjemplar)
                    .addComponent(btnBuscarEjemplar))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ejemplares del préstamo"));

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblDetalle);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Condiciones del préstamo"));

        jLabel5.setText("Días de préstamo");

        btnQuitarItem.setBackground(new java.awt.Color(153, 0, 51));
        btnQuitarItem.setText("Quitar ítem");
        btnQuitarItem.addActionListener(this::btnQuitarItemActionPerformed);

        btnRegistrarPrestamo.setBackground(new java.awt.Color(0, 102, 102));
        btnRegistrarPrestamo.setText("Registrar préstamo");
        btnRegistrarPrestamo.addActionListener(this::btnRegistrarPrestamoActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDiasPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnQuitarItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRegistrarPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDiasPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnQuitarItem, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarLectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLectorActionPerformed
        String dni = txtDniLector.getText().trim();
        if (dni.length() != 8) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI de 8 dígitos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LectorNegocio negocio = new LectorNegocio();
            lectorSeleccionado = negocio.buscarPorDni(dni);

            if (lectorSeleccionado == null) {
                txtNombreLector.setText("");
                JOptionPane.showMessageDialog(this, "Lector no encontrado en el sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else if (!lectorSeleccionado.isEstado()) {
                txtNombreLector.setText("");
                lectorSeleccionado = null;
                JOptionPane.showMessageDialog(this, "El lector seleccionado está Inactivo y no puede pedir préstamos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                txtNombreLector.setText(lectorSeleccionado.getNombres() + " " + lectorSeleccionado.getApellidos());
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarLectorActionPerformed

    private void btnAgregarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEjemplarActionPerformed
        String codigo = txtCodigoPatrimonial.getText().trim();
        String obs = txtObservacion.getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código patrimonial del ejemplar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si el ejemplar ya está en el carrito
        for (DetallePrestamo det : listaDetalle) {
            if (det.getEjemplar().getCodigoPatrimonial().equalsIgnoreCase(codigo)) {
                JOptionPane.showMessageDialog(this, "El ejemplar con código '" + codigo + "' ya fue agregado a la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            PrestamoNegocio negocio = new PrestamoNegocio();
            Ejemplar ej = negocio.buscarEjemplarParaPrestamo(codigo);

            DetallePrestamo det = new DetallePrestamo();
            det.setEjemplar(ej);
            det.setObservacionEntrega(obs.isEmpty() ? "En buen estado" : obs);
            det.setDevuelto(false);

            listaDetalle.add(det);
            actualizarTablaUI();

            txtCodigoPatrimonial.setText("");
            txtObservacion.setText("");
            txtCodigoPatrimonial.requestFocus();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarEjemplarActionPerformed

    private void btnQuitarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarItemActionPerformed
        int fila = tblDetalle.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para quitarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        listaDetalle.remove(fila);
        actualizarTablaUI();
    }//GEN-LAST:event_btnQuitarItemActionPerformed

    private void btnRegistrarPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPrestamoActionPerformed
        if (lectorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe buscar y seleccionar un lector activo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (listaDetalle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un ejemplar al préstamo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int dias = 3;
        try {
            dias = Integer.parseInt(txtDiasPrestamo.getText().trim());
            if (dias <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los días de préstamo deben ser un número entero positivo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioSesion = SesionActual.getUsuarioLogueado();
        if (usuarioSesion == null) {
            JOptionPane.showMessageDialog(this, "No hay una sesión de usuario activa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular fecha dev. esperada
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dias);
        Date fechaDevEsperada = cal.getTime();

        Prestamo p = new Prestamo();
        p.setLector(lectorSeleccionado);
        p.setUsuario(usuarioSesion);
        p.setFechaDevolucionEsperada(fechaDevEsperada);
        p.setDetalles(listaDetalle);

        try {
            PrestamoNegocio negocio = new PrestamoNegocio();
            negocio.registrarPrestamo(p);

            JOptionPane.showMessageDialog(this, "¡Préstamo registrado exitosamente en el sistema!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormularioCompleto();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarPrestamoActionPerformed

    private void btnBuscarEjemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEjemplarActionPerformed
        // Se abre el diálogo modal de búsqueda
        jdBuscarEjemplar dialog = new jdBuscarEjemplar(null, true);
        dialog.setVisible(true);

        // Obtener el código seleccionado al cerrar el diálogo
        String codigo = dialog.getCodigoSeleccionado();
        if (codigo != null && !codigo.isEmpty()) {
            txtCodigoPatrimonial.setText(codigo);
        }
    }//GEN-LAST:event_btnBuscarEjemplarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEjemplar;
    private javax.swing.JButton btnBuscarEjemplar;
    private javax.swing.JButton btnBuscarLector;
    private javax.swing.JButton btnQuitarItem;
    private javax.swing.JButton btnRegistrarPrestamo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtCodigoPatrimonial;
    private javax.swing.JTextField txtDiasPrestamo;
    private javax.swing.JTextField txtDniLector;
    private javax.swing.JTextField txtNombreLector;
    private javax.swing.JTextField txtObservacion;
    // End of variables declaration//GEN-END:variables
}
