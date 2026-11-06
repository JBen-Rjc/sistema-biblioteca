package presentacion;

import configuracion.ReporteHtmlUtil;
import excepciones.DAOException;
import negocio.ReporteService;
import javax.swing.JOptionPane;

public class frmReportesInternal extends javax.swing.JInternalFrame {

    public frmReportesInternal() {
        initComponents();
        estilo.Tema.decorar(this); // aspecto visual
        cargarComboReportes();
        evaluarParametrosSegunReporte();
    }

    private void cargarComboReportes() {
        cboReportes.removeAllItems(); // Limpia cualquier valor previo
        cboReportes.addItem("1. Historial de Préstamos por Rango de Fechas");
        cboReportes.addItem("2. Registro de Devoluciones y Multas Liquidadas");
        cboReportes.addItem("3. Lectores Morosos y Préstamos Vencidos");
        cboReportes.addItem("4. Historial / Ficha de Préstamos por Lector");
        cboReportes.addItem("5. Ranking de Libros Más Prestados");
        cboReportes.addItem("6. Inventario de Ejemplares y Estado de Conservación");
    }

    private void evaluarParametrosSegunReporte() {
        int index = cboReportes.getSelectedIndex();
        // Reportes que usan rango de fechas: 0 (Préstamos por fechas), 1 (Devoluciones y multas)
        boolean usaFechas = (index == 0 || index == 1);
        // Reporte que usa DNI: 3 (Historial por Lector)
        boolean usaDni = (index == 3);

        lblFechaInicio.setEnabled(usaFechas);
        txtFechaInicio.setEnabled(usaFechas);
        lblFechaFin.setEnabled(usaFechas);
        txtFechaFin.setEnabled(usaFechas);

        lblDni.setEnabled(usaDni);
        txtDni.setEnabled(usaDni);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboReportes = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        lblFechaInicio = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        lblFechaFin = new javax.swing.JLabel();
        txtFechaFin = new javax.swing.JTextField();
        lblDni = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        btnGenerarReporte = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Reportes");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles"));

        jLabel1.setText("Reporte");

        cboReportes.addActionListener(this::cboReportesActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cboReportes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        lblFechaInicio.setText("Desde (AAAA-MM-DD)");

        lblFechaFin.setText("Hasta (AAAA-MM-DD)");

        lblDni.setText("DNI del lector");

        btnGenerarReporte.setBackground(new java.awt.Color(0, 102, 102));
        btnGenerarReporte.setText("Generar reporte");
        btnGenerarReporte.addActionListener(this::btnGenerarReporteActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenerarReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblFechaInicio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblDni)
                                .addGap(18, 18, 18)
                                .addComponent(txtDni)))
                        .addGap(24, 24, 24)
                        .addComponent(lblFechaFin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFechaInicio)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaFin)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDni)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGenerarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReportesActionPerformed
        evaluarParametrosSegunReporte();
    }//GEN-LAST:event_cboReportesActionPerformed

    private void btnGenerarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteActionPerformed
        int index = cboReportes.getSelectedIndex();
        try {
            String html = "";
            String titulo = "";

            switch (index) {
                case 0: // Historial por Rango de Fechas
                    if (txtFechaInicio.getText().trim().isEmpty() || txtFechaFin.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ingrese la fecha de inicio y fin (YYYY-MM-DD).", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    titulo = "Historial de Préstamos por Fechas";
                    html = ReporteService.generarHtmlPrestamosPorFechas(txtFechaInicio.getText().trim(), txtFechaFin.getText().trim());
                    break;

                case 1: // Devoluciones y Multas Liquidadas
                    if (txtFechaInicio.getText().trim().isEmpty() || txtFechaFin.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ingrese la fecha de inicio y fin (YYYY-MM-DD).", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    titulo = "Devoluciones y Multas Liquidadas";
                    html = ReporteService.generarHtmlDevolucionesMultas(txtFechaInicio.getText().trim(), txtFechaFin.getText().trim());
                    break;

                case 2: // Morosos
                    titulo = "Reporte de Lectores Morosos y Préstamos Vencidos";
                    html = ReporteService.generarHtmlLectoresMorosos();
                    break;

                case 3: // Historial por Lector
                    if (txtDni.getText().trim().length() != 8) {
                        JOptionPane.showMessageDialog(this, "Ingrese un DNI válido de 8 dígitos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    titulo = "Historial del Lector - DNI: " + txtDni.getText().trim();
                    html = ReporteService.generarHtmlHistorialLector(txtDni.getText().trim());
                    break;

                case 4: // Libros Más Prestados
                    titulo = "Ranking de Libros Más Prestados";
                    html = ReporteService.generarHtmlLibrosMasPrestados();
                    break;

                case 5: // Inventario de Ejemplares
                    titulo = "Inventario General de Ejemplares";
                    html = ReporteService.generarHtmlInventarioEjemplares();
                    break;
            }

            if (!html.isEmpty()) {
                ReporteHtmlUtil.mostrarReporte(titulo, html);
            }

        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en Base de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar el reporte: " + ex.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGenerarReporteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerarReporte;
    private javax.swing.JComboBox<String> cboReportes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblFechaFin;
    private javax.swing.JLabel lblFechaInicio;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    // End of variables declaration//GEN-END:variables
}
