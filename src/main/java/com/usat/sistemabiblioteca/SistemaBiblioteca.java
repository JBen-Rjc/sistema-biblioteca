package com.usat.sistemabiblioteca;

import configuracion.Conexion;
import java.sql.Connection;

public class SistemaBiblioteca {

    public static void main(String[] args) {
        // 1. Verificar la conexión a la base de datos (sólo informa en consola)
        try {
            Connection cn = Conexion.getInstancia().getConexion();
            if (cn != null && !cn.isClosed()) {
                System.out.println("=========================================");
                System.out.println("¡CONEXIÓN EXITOSA A BD_BIBLIOTECA!");
                System.out.println("=========================================");
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. Aplicar el tema visual y abrir la ventana principal
        estilo.Tema.instalar();
        java.awt.EventQueue.invokeLater(() -> new presentacion.frmPrincipal().setVisible(true));
    }
}
