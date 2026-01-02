/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package main;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            
            // Test de conexión
            try (var conn = config.DatabaseConnection.getConnection()) {
                System.out.println("Conexión establecida con éxito a: " + conn.getMetaData().getURL());
            }
            
            // Usar menú
            AppMenu menu = new AppMenu();
            menu.iniciar();
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}