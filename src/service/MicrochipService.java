/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.MicrochipDao;
import models.Microchip;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/*
Servicio que maneja la lógica de negocio para las operaciones con microchips.
Implementa validaciones, transacciones y garantiza la unicidad de los códigos de microchip.
Se comunica con el MicrochipDao para las operaciones de persistencia.
*/

public class MicrochipService implements GenericService<Microchip> {
    
    private final MicrochipDao microchipDao = new MicrochipDao();
    
    @Override
    public Microchip insertar(Microchip microchip) throws Exception {
        validarMicrochip(microchip);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar si ya existe un microchip con el mismo código
                Microchip existente = microchipDao.leerPorCodigo(microchip.getCodigo(), conn);
                if (existente != null) {
                    throw new IllegalArgumentException("Ya existe un microchip con el código: " + microchip.getCodigo());
                }
                
                Microchip resultado = microchipDao.crear(microchip, conn);
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
    public Microchip actualizar(Microchip microchip) throws Exception {
        validarMicrochip(microchip);
        
        if (microchip.getId() == null) {
            throw new IllegalArgumentException("ID del microchip es requerido para actualizar");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar que el microchip existe
                Microchip existente = microchipDao.leer(microchip.getId(), conn);
                if (existente == null) {
                    throw new IllegalArgumentException("Microchip no encontrado con ID: " + microchip.getId());
                }
                
                // Verificar unicidad del código (excluyendo el actual)
                Microchip conMismoCodigo = microchipDao.leerPorCodigo(microchip.getCodigo(), conn);
                if (conMismoCodigo != null && !conMismoCodigo.getId().equals(microchip.getId())) {
                    throw new IllegalArgumentException("Ya existe otro microchip con el código: " + microchip.getCodigo());
                }
                
                Microchip resultado = microchipDao.actualizar(microchip, conn);
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
            throw new IllegalArgumentException("ID del microchip es requerido");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Verificar que el microchip existe
                Microchip existente = microchipDao.leer(id, conn);
                if (existente == null) {
                    throw new IllegalArgumentException("Microchip no encontrado con ID: " + id);
                }
                
                boolean resultado = microchipDao.eliminar(id, conn);
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
    public Microchip getById(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("ID del microchip es requerido");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            return microchipDao.leer(id, conn);
        }
    }
    
    @Override
    public List<Microchip> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return microchipDao.leerTodos(conn);
        }
    }
    
    public Microchip getByCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código del microchip es requerido");
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            return microchipDao.leerPorCodigo(codigo, conn);
        }
    }
    
    private void validarMicrochip(Microchip microchip) {
        if (microchip == null) {
            throw new IllegalArgumentException("Microchip no puede ser nulo");
        }
        if (microchip.getCodigo() == null || microchip.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("Código del microchip es requerido");
        }
        if (microchip.getCodigo().length() > 25) {
            throw new IllegalArgumentException("Código no puede tener más de 25 caracteres");
        }
        if (microchip.getVeterinaria() != null && microchip.getVeterinaria().length() > 120) {
            throw new IllegalArgumentException("Veterinaria no puede tener más de 120 caracteres");
        }
        if (microchip.getObservaciones() != null && microchip.getObservaciones().length() > 255) {
            throw new IllegalArgumentException("Observaciones no pueden tener más de 255 caracteres");
        }
    }
}