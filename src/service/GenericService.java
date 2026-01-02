/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.List;

/*
 Interfaz que define las operaciones CRUD básicas para los servicios del sistema.
 Proporciona una capa de abstracción sobre las operaciones de base de datos.

 @param <T> Tipo de entidad que manejará el servicio
 */

public interface GenericService<T> {
    T insertar(T entity) throws Exception;
    T actualizar(T entity) throws Exception;
    boolean eliminar(Long id) throws Exception;
    T getById(Long id) throws Exception;
    List<T> getAll() throws Exception;
}