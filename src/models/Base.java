/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/*
Clase abstracta base que proporciona campos comunes para todas las entidades del sistema.
Define los atributos básicos de identificación y estado de eliminación lógica.
 */

public abstract class Base {
    private Long id;
    private Boolean eliminado;
    
    // Constructores
    public Base() {
        this.eliminado = false;
    }
    
    public Base(Long id, Boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
}
