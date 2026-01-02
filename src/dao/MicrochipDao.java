/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import models.Microchip;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Clase DAO (Data Access Object) que maneja las operaciones de persistencia para la entidad Microchip.
Implementa las operaciones CRUD y consultas específicas relacionadas con microchips.
Garantiza la integridad de los datos y el manejo adecuado de conexiones a la base de datos.
 */

public class MicrochipDao implements GenericDao<Microchip> {
    
    // Crea un nuevo microchip en la base de datos
    @Override
    public Microchip crear(Microchip microchip, Connection conn) throws SQLException {
        String sql = "INSERT INTO microchip (codigo, fecha_implantacion, veterinaria, observaciones, eliminado) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, microchip.getCodigo());
            stmt.setDate(2, microchip.getFechaImplantacion() != null ? 
                Date.valueOf(microchip.getFechaImplantacion()) : null);
            stmt.setString(3, microchip.getVeterinaria());
            stmt.setString(4, microchip.getObservaciones());
            stmt.setBoolean(5, false);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating microchip failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    microchip.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating microchip failed, no ID obtained.");
                }
            }
        }
        return microchip;
    }
    
    // Recupera un microchip por su ID
    @Override
    public Microchip leer(Long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM microchip WHERE id = ? AND eliminado = false";
        Microchip microchip = null;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    microchip = resultSetToMicrochip(rs);
                }
            }
        }
        return microchip;
    }
    // Recupera todos los microchips activos del sistema
    @Override
    public List<Microchip> leerTodos(Connection conn) throws SQLException {
        String sql = "SELECT * FROM microchip WHERE eliminado = false";
        List<Microchip> microchips = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                microchips.add(resultSetToMicrochip(rs));
            }
        }
        return microchips;
    }
    
    // Actualiza los datos de un microchip existente
    @Override
    public Microchip actualizar(Microchip microchip, Connection conn) throws SQLException {
        String sql = "UPDATE microchip SET codigo = ?, fecha_implantacion = ?, veterinaria = ?, observaciones = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, microchip.getCodigo());
            stmt.setDate(2, microchip.getFechaImplantacion() != null ? 
                Date.valueOf(microchip.getFechaImplantacion()) : null);
            stmt.setString(3, microchip.getVeterinaria());
            stmt.setString(4, microchip.getObservaciones());
            stmt.setLong(5, microchip.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating microchip failed, no rows affected.");
            }
        }
        return microchip;
    }
    
    // Realiza una eliminación lógica de un microchip marcándolo como eliminado
    @Override
    public boolean eliminar(Long id, Connection conn) throws SQLException {
        String sql = "UPDATE microchip SET eliminado = true WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Busca un microchip por su código único
    public Microchip leerPorCodigo(String codigo, Connection conn) throws SQLException {
        String sql = "SELECT * FROM microchip WHERE codigo = ? AND eliminado = false";
        Microchip microchip = null;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    microchip = resultSetToMicrochip(rs);
                }
            }
        }
        return microchip;
    }
    
    // Convierte un ResultSet en un objeto Microchip
    private Microchip resultSetToMicrochip(ResultSet rs) throws SQLException {
        Microchip microchip = new Microchip();
        microchip.setId(rs.getLong("id"));
        microchip.setCodigo(rs.getString("codigo"));
        microchip.setFechaImplantacion(rs.getDate("fecha_implantacion") != null ? 
            rs.getDate("fecha_implantacion").toLocalDate() : null);
        microchip.setVeterinaria(rs.getString("veterinaria"));
        microchip.setObservaciones(rs.getString("observaciones"));
        microchip.setEliminado(rs.getBoolean("eliminado"));
        return microchip;
    }
}