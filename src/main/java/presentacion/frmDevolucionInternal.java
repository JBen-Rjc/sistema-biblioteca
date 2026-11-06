package presentacion;

import excepciones.DAOException;
import excepciones.NegocioException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DetallePrestamo;
import modelo.Prestamo;
import negocio.PrestamoNegocio;

public class frmDevolucionInternal extends javax.swing.JInternalFrame {

    private List<Prestamo> listaPrestamos = new ArrayList<>();
    private Prestamo prestamoSeleccionado = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public frmDevolucionInternal() {
        initComponents();
        estilo.Tema.decorar(this); // aspecto visual
        configurarTablas();
        configurarEventoSeleccion();
    }

    private void configurarTablas() {
        tblPrestamos.setModel(new DefaultTableModel(
                new String[]{"ID Préstamo", "Fecha Préstamo", "Fecha Límite Dev.", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tblDetalles.setModel(new DefaultTableModel(
                new String[]{"Código", "Título del Libro", "Ubicación", "Estado Conservación", "Observación"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void configurarEventoSeleccion() {
        tblPrestamos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblPrestamos.getSelectedRow();
                if (fila >= 0 && fila < listaPrestamos.size()) {
                    prestamoSeleccionado = listaPrestamos.get(fila);
                    cargarDetallesEjemplares(prestamoSeleccionado.getIdPrestamo());

                    // Calcular multa tentativa
                    PrestamoNegocio negocio = new PrestamoNegocio();
                    try {
                        double multa = negocio.calcularMulta(prestamoSeleccionado.getFechaDevolucionEsperada(),
                                negocio.obtenerTarifaMultaDiaria());
                        lblMulta.setText(String.format("S/ %.2f", multa));
                    } catch (DAOException ex) {
                        lblMulta.setText("S/ --");
                    }
                }
            }
        });
    }

    private void cargarDetallesEjemplares(int idPrestamo) {
        DefaultTableModel modeloDetalle = (DefaultTableModel) tblDetalles.getModel();
        modeloDetalle.setRowCount(0);

        try {
            PrestamoNegocio negocio = new PrestamoNegocio();
            List<DetallePrestamo> detalles = negocio.obtenerDetallesPrestamo(idPrestamo);

            for (DetallePrestamo d : detalles) {
                modeloDetalle.addRow(new Object[]{
                    d.getEjemplar().getCodigoPatrimonial(),
                    d.getEjemplar().getLibro().getTitulo(),
                    d.getEjemplar().getUbicacion(),
                    d.getEjemplar().getEstadoConservacion(),
                    d.getObservacionEntrega()
                });
            }
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        prestamoSeleccionado = null;
        ((DefaultTableModel) tblPrestamos.getModel()).setRowCount(0);
        ((DefaultTableModel) tblDetalles.getModel()).setRowCount(0);
        lblMulta.setText("S/ 0.00");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDniLector = new javax.swing.JTextField();
        btnBuscarPrestamos = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDetalles = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnProcesarDevolucion = new javax.swing.JButton();
        lblMulta = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Devoluciones");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar"));

        jLabel1.setText("DNI del lector");

        btnBuscarPrestamos.setBackground(new java.awt.Color(229, 169, 60));
        btnBuscarPrestamos.setText("Buscar préstamos");
        btnBuscarPrestamos.addActionListener(this::btnBuscarPrestamosActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDniLector, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBuscarPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarPrestamos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtDniLector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Préstamos pendientes"));

        tblPrestamos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPrestamos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ejemplares prestados"));

        tblDetalles.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblDetalles);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Multa"));

        btnProcesarDevolucion.setBackground(new java.awt.Color(0, 102, 102));
        btnProcesarDevolucion.setText("Registrar devolución");
        btnProcesarDevolucion.addActionListener(this::btnProcesarDevolucionActionPerformed);

        lblMulta.setText("S/ 0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblMulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProcesarDevolucion, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(lblMulta)
                .addGap(18, 18, 18)
                .addComponent(btnProcesarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarPrestamosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPrestamosActionPerformed
        String dni = txtDniLector.getText().trim();
        limpiarFormulario();
        txtDniLector.setText(dni);

        try {
            PrestamoNegocio negocio = new PrestamoNegocio();
            listaPrestamos = negocio.buscarPrestamosPendientes(dni);

            DefaultTableModel modelo = (DefaultTableModel) tblPrestamos.getModel();
            if (listaPrestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El lector no registra préstamos pendientes.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Prestamo p : listaPrestamos) {
                modelo.addRow(new Object[]{
                    p.getIdPrestamo(),
                    sdf.format(p.getFechaPrestamo()),
                    sdf.format(p.getFechaDevolucionEsperada()),
                    p.getEstado()
                });
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarPrestamosActionPerformed

    private void btnProcesarDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarDevolucionActionPerformed
        if (prestamoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un préstamo de la tabla superior.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opc = JOptionPane.showConfirmDialog(this,
                "¿Desea confirmar la devolución de los ejemplares de este préstamo?",
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (opc == JOptionPane.YES_OPTION) {
            try {
                PrestamoNegocio negocio = new PrestamoNegocio();
                negocio.registrarDevolucion(prestamoSeleccionado.getIdPrestamo(), prestamoSeleccionado.getFechaDevolucionEsperada());

                JOptionPane.showMessageDialog(this, "Devolución registrada con éxito. Los ejemplares vuelven a estar disponibles.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                txtDniLector.setText("");

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnProcesarDevolucionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPrestamos;
    private javax.swing.JButton btnProcesarDevolucion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblMulta;
    private javax.swing.JTable tblDetalles;
    private javax.swing.JTable tblPrestamos;
    private javax.swing.JTextField txtDniLector;
    // End of variables declaration//GEN-END:variables
}
