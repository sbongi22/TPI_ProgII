/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import models.Mascota;
import models.Microchip;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/*
Clase DAO (Data Access Object) que maneja las operaciones de persistencia para la entidad Mascota.
Implementa las operaciones CRUD y consultas específicas relacionadas con mascotas.
Se encarga de mapear entre objetos Java y registros de la base de datos.
 */

public class MascotaDao implements GenericDao<Mascota> {
    
    // Crea una nueva mascota en la base de datos. Incluye la asignación de microchip si está presente en el objeto
    @Override
    public Mascota crear(Mascota mascota, Connection conn) throws SQLException {
        String sql = "INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio, microchip_id, eliminado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mascota.getNombre());
            stmt.setString(2, mascota.getEspecie());
            stmt.setString(3, mascota.getRaza());
            stmt.setDate(4, mascota.getFechaNacimiento() != null ? 
                Date.valueOf(mascota.getFechaNacimiento()) : null);
            stmt.setString(5, mascota.getDuenio());
            stmt.setObject(6, mascota.getMicrochip() != null ? mascota.getMicrochip().getId() : null, Types.BIGINT);
            stmt.setBoolean(7, false);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating mascota failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mascota.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating mascota failed, no ID obtained.");
                }
            }
        }
        return mascota;
    }
    
    // Recupera una mascota por su ID, incluyendo la información del microchip asociado si existe
    // Realiza un JOIN con la tabla microchip para cargar toda la información relacionada
    @Override
    public Mascota leer(Long id, Connection conn) throws SQLException {
        String sql = "SELECT m.*, mc.* FROM mascota m LEFT JOIN microchip mc ON m.microchip_id = mc.id AND mc.eliminado = false WHERE m.id = ? AND m.eliminado = false";
        Mascota mascota = null;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mascota = resultSetToMascota(rs);
                }
            }
        }
        return mascota;
    }
    
    // Recupera todas las mascotas activas (no eliminadas) del sistema
    // Incluye la información de microchip para cada mascota mediante LEFT JOIN
    @Override
    public List<Mascota> leerTodos(Connection conn) throws SQLException {
        String sql = "SELECT m.*, mc.* FROM mascota m LEFT JOIN microchip mc ON m.microchip_id = mc.id AND mc.eliminado = false WHERE m.eliminado = false";
        List<Mascota> mascotas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mascotas.add(resultSetToMascota(rs));
            }
        }
        return mascotas;
    }
    
    // Actualiza los datos de una mascota existente en la base de datos
    // Permite modificar todos los campos incluyendo la asignación de microchip
    @Override
    public Mascota actualizar(Mascota mascota, Connection conn) throws SQLException {
        String sql = "UPDATE mascota SET nombre = ?, especie = ?, raza = ?, fecha_nacimiento = ?, duenio = ?, microchip_id = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mascota.getNombre());
            stmt.setString(2, mascota.getEspecie());
            stmt.setString(3, mascota.getRaza());
            stmt.setDate(4, mascota.getFechaNacimiento() != null ? 
                Date.valueOf(mascota.getFechaNacimiento()) : null);
            stmt.setString(5, mascota.getDuenio());
            stmt.setObject(6, mascota.getMicrochip() != null ? mascota.getMicrochip().getId() : null, Types.BIGINT);
            stmt.setLong(7, mascota.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating mascota failed, no rows affected.");
            }
        }
        return mascota;
    }
    
    // Realiza una eliminación lógica de una mascota marcándola como eliminada. No borra físicamente el registro de la base de datos
    @Override
    public boolean eliminar(Long id, Connection conn) throws SQLException {
        String sql = "UPDATE mascota SET eliminado = true WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Busca mascotas por el nombre del dueño utilizando búsqueda parcial (LIKE)
    public List<Mascota> buscarPorDuenio(String duenio, Connection conn) throws SQLException {
        String sql = "SELECT m.*, mc.* FROM mascota m LEFT JOIN microchip mc ON m.microchip_id = mc.id AND mc.eliminado = false WHERE m.duenio LIKE ? AND m.eliminado = false";
        List<Mascota> mascotas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + duenio + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(resultSetToMascota(rs));
                }
            }
        }
        return mascotas;
    }
    
    // Busca mascotas por especie exacta 
    public List<Mascota> buscarPorEspecie(String especie, Connection conn) throws SQLException {
        String sql = "SELECT m.*, mc.* FROM mascota m LEFT JOIN microchip mc ON m.microchip_id = mc.id AND mc.eliminado = false WHERE m.especie = ? AND m.eliminado = false";
        List<Mascota> mascotas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, especie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(resultSetToMascota(rs));
                }
            }
        }
        return mascotas;
    }
    
    // Convierte un ResultSet en un objeto Mascota, incluyendo la carga del microchip asociado
    private Mascota resultSetToMascota(ResultSet rs) throws SQLException {
        Mascota mascota = new Mascota();
        mascota.setId(rs.getLong("m.id"));
        mascota.setNombre(rs.getString("m.nombre"));
        mascota.setEspecie(rs.getString("m.especie"));
        mascota.setRaza(rs.getString("m.raza"));
        mascota.setFechaNacimiento(rs.getDate("m.fecha_nacimiento") != null ? 
            rs.getDate("m.fecha_nacimiento").toLocalDate() : null);
        mascota.setDuenio(rs.getString("m.duenio"));
        mascota.setEliminado(rs.getBoolean("m.eliminado"));
        
        // Cargar microchip si existe
        if (rs.getObject("mc.id") != null) {
            Microchip microchip = new Microchip();
            microchip.setId(rs.getLong("mc.id"));
            microchip.setCodigo(rs.getString("mc.codigo"));
            microchip.setFechaImplantacion(rs.getDate("mc.fecha_implantacion") != null ? 
                rs.getDate("mc.fecha_implantacion").toLocalDate() : null);
            microchip.setVeterinaria(rs.getString("mc.veterinaria"));
            microchip.setObservaciones(rs.getString("mc.observaciones"));
            microchip.setEliminado(rs.getBoolean("mc.eliminado"));
            mascota.setMicrochip(microchip);
        }
        
        return mascota;
    }
}