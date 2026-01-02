/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.MascotaDao;
import dao.MicrochipDao;
import models.Mascota;
import models.Microchip;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/*
 Servicio que maneja la lógica de negocio para las operaciones con mascotas.
 Implementa validaciones, transacciones y reglas de negocio específicas.
 Se comunica con los DAOs correspondientes para las operaciones de persistencia.
*/

public class MascotaService implements GenericService<Mascota> {
    
    private final MascotaDao mascotaDao = new MascotaDao();
    private final MicrochipDao microchipDao = new MicrochipDao();
    
    @Override
    public Mascota insertar(Mascota mascota) throws Exception {
        validarMascota(mascota);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Si tiene microchip, verificar que existe y no está asignado a otra mascota
                if (mascota.getMicrochip() != null && mascota.getMicrochip().getId() != null) {
                    validarMicrochipParaAsignacion(mascota.getMicrochip().getId(), conn);
                }
                
                Mascota resultado = mascotaDao.crear(mascota, conn);
                conn.commit();
                return resultado;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    @Override
    public Mascota actualizar(Mascota mascota) throws Exception {
        validarMascota(mascota);
        
        if (mascota.getId() == null) {
            throw new IllegalArgumentException("ID de la mascota es requerido para actualizar");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar que la mascota existe
                Mascota existente = mascotaDao.leer(mascota.getId(), conn);
                if (existente == null) {
                    throw new IllegalArgumentException("Mascota no encontrada con ID: " + mascota.getId());
                }
                
                // Si está cambiando el microchip, validar la nueva asignación
                if (mascota.getMicrochip() != null && mascota.getMicrochip().getId() != null) {
                    Long nuevoMicrochipId = mascota.getMicrochip().getId();
                    if (existente.getMicrochip() == null || !nuevoMicrochipId.equals(existente.getMicrochip().getId())) {
                        validarMicrochipParaAsignacion(nuevoMicrochipId, conn);
                    }
                }
                
                Mascota resultado = mascotaDao.actualizar(mascota, conn);
                conn.commit();
                return resultado;
                
            } catch (IllegalArgumentException | SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    @Override
    public boolean eliminar(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("ID de la mascota es requerido");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar que la mascota existe
                Mascota existente = mascotaDao.leer(id, conn);
                if (existente == null) {
                    throw new IllegalArgumentException("Mascota no encontrada con ID: " + id);
                }
                
                boolean resultado = mascotaDao.eliminar(id, conn);
                conn.commit();
                return resultado;
                
            } catch (IllegalArgumentException | SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    @Override
    public Mascota getById(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("ID de la mascota es requerido");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            return mascotaDao.leer(id, conn);
        }
    }
    
    @Override
    public List<Mascota> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return mascotaDao.leerTodos(conn);
        }
    }
    
    public List<Mascota> buscarPorDuenio(String duenio) throws Exception {
        if (duenio == null || duenio.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre del dueño es requerido para la búsqueda");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            return mascotaDao.buscarPorDuenio(duenio, conn);
        }
    }
    
    public List<Mascota> buscarPorEspecie(String especie) throws Exception {
        if (especie == null || especie.trim().isEmpty()) {
            throw new IllegalArgumentException("Especie es requerida para la búsqueda");
        }
        
        if (!especieValida(especie)) {
            throw new IllegalArgumentException("Especie no válida. Valores permitidos: PERRO, GATO, AVE, PEZ, REPTIL, OTRO");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            return mascotaDao.buscarPorEspecie(especie, conn);
        }
    }
    
    public Mascota asignarMicrochip(Long mascotaId, Long microchipId) throws Exception {
        if (mascotaId == null || microchipId == null) {
            throw new IllegalArgumentException("ID de mascota y microchip son requeridos");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar que la mascota existe
                Mascota mascota = mascotaDao.leer(mascotaId, conn);
                if (mascota == null) {
                    throw new IllegalArgumentException("Mascota no encontrada con ID: " + mascotaId);
                }
                
                // Verificar que el microchip existe y está disponible
                validarMicrochipParaAsignacion(microchipId, conn);
                
                // Asignar microchip
                mascota.setMicrochip(new Microchip());
                mascota.getMicrochip().setId(microchipId);
                
                Mascota resultado = mascotaDao.actualizar(mascota, conn);
                conn.commit();
                return resultado;
                
            } catch (IllegalArgumentException | SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    public Mascota quitarMicrochip(Long mascotaId) throws Exception {
        if (mascotaId == null) {
            throw new IllegalArgumentException("ID de la mascota es requerido");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar que la mascota existe
                Mascota mascota = mascotaDao.leer(mascotaId, conn);
                if (mascota == null) {
                    throw new IllegalArgumentException("Mascota no encontrada con ID: " + mascotaId);
                }
                
                // Quitar microchip
                mascota.setMicrochip(null);
                Mascota resultado = mascotaDao.actualizar(mascota, conn);
                conn.commit();
                return resultado;
                
            } catch (IllegalArgumentException | SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    private void validarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("Mascota no puede ser nula");
        }
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de la mascota es requerido");
        }
        if (mascota.getNombre().length() > 60) {
            throw new IllegalArgumentException("Nombre no puede tener más de 60 caracteres");
        }
        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new IllegalArgumentException("Especie de la mascota es requerida");
        }
        if (!especieValida(mascota.getEspecie())) {
            throw new IllegalArgumentException("Especie no válida. Valores permitidos: PERRO, GATO, AVE, PEZ, REPTIL, OTRO");
        }
        if (mascota.getDuenio() == null || mascota.getDuenio().trim().isEmpty()) {
            throw new IllegalArgumentException("Dueño de la mascota es requerido");
        }
        if (mascota.getDuenio().length() > 120) {
            throw new IllegalArgumentException("Dueño no puede tener más de 120 caracteres");
        }
        if (mascota.getRaza() != null && mascota.getRaza().length() > 60) {
            throw new IllegalArgumentException("Raza no puede tener más de 60 caracteres");
        }
    }
    
    private boolean especieValida(String especie) {
        return especie != null && 
               especie.matches("(?i)PERRO|GATO|AVE|PEZ|REPTIL|OTRO");
    }
    
    private void validarMicrochipParaAsignacion(Long microchipId, Connection conn) throws SQLException {
        // Verificar que el microchip existe
        Microchip microchip = microchipDao.leer(microchipId, conn);
        if (microchip == null) {
            throw new IllegalArgumentException("Microchip no encontrado con ID: " + microchipId);
        }
        
        // Verificar que el microchip no esté asignado a otra mascota
        // (esto se garantiza con la FK UNIQUE, pero validamos para mejor mensaje de error)
        String sql = "SELECT COUNT(*) FROM mascota WHERE microchip_id = ? AND eliminado = false";
        try (var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, microchipId);
            try (var rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new IllegalArgumentException("El microchip ya está asignado a otra mascota");
                }
            }
        }
    }
}