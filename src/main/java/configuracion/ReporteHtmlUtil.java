package configuracion;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ReporteHtmlUtil {

    public static void mostrarReporte(String titulo, String htmlContent) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText(htmlContent);
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);

        JDialog dialog = new JDialog();
        dialog.setTitle(titulo);
        dialog.setSize(950, 680);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        javax.swing.JPanel panelBotones = new javax.swing.JPanel();
        javax.swing.JButton btnPdf = new javax.swing.JButton("Exportar a PDF");
        javax.swing.JButton btnExcel = new javax.swing.JButton("Exportar a Excel");

        btnPdf.addActionListener(e -> exportarAPdf(htmlContent));
        btnExcel.addActionListener(e -> exportarAExcel(htmlContent));

        panelBotones.add(btnPdf);
        panelBotones.add(btnExcel);

        dialog.getContentPane().setLayout(new java.awt.BorderLayout());
        dialog.getContentPane().add(panelBotones, java.awt.BorderLayout.NORTH);
        dialog.getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    public static void exportarAExcel(String htmlContent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte en Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Excel (*.xls)", "xls"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".xls")) {
                archivo = new File(archivo.getAbsolutePath() + ".xls");
            }
            // Forzar UTF-8 con Byte Order Mark (\uFEFF) para que Excel reconozca tildes y eñes
            try (FileOutputStream fos = new FileOutputStream(archivo);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                osw.write("\uFEFF"); 
                osw.write(htmlContent);
                osw.flush();
                JOptionPane.showMessageDialog(null, "Reporte exportado exitosamente a Excel.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al exportar a Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void exportarAPdf(String htmlContent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte en PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos PDF (*.pdf)", "pdf"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                archivo = new File(archivo.getAbsolutePath() + ".pdf");
            }
            try {
                // Configurar documento en orientación Horizontal (A4) con márgenes adecuados
                Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
                PdfWriter.getInstance(document, new FileOutputStream(archivo));
                document.open();

                HTMLWorker htmlWorker = new HTMLWorker(document);
                htmlWorker.parse(new StringReader(htmlContent));
                
                document.close();
                JOptionPane.showMessageDialog(null, "Reporte exportado exitosamente a PDF.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al exportar a PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}