/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.util.List;

/*
Interfaz que define las operaciones CRUD básicas para el acceso a datos.
Proporciona una capa de abstracción para la persistencia de entidades en la base de datos.
Todas las operaciones reciben una conexión para permitir el manejo de transacciones.

@param <T> Tipo de entidad que manejará el DAO
 */

public interface GenericDao<T> {
    T crear(T entity, Connection conn) throws Exception;
    T leer(Long id, Connection conn) throws Exception;
    List<T> leerTodos(Connection conn) throws Exception;
    T actualizar(T entity, Connection conn) throws Exception;
    boolean eliminar(Long id, Connection conn) throws Exception;
}