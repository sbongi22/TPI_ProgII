/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;

/*
Clase que representa un microchip en el sistema.
Extiende de la clase Base e incluye atributos específicos como código único,
fecha de implantación, veterinaria donde se implantó y observaciones.
*/

public class Microchip extends Base {
    private String codigo;
    private LocalDate fechaImplantacion;
    private String veterinaria;
    private String observaciones;
    
    // Constructores
    public Microchip() {
        super(); // Llama al constructor por defecto de Base
    }
    
    public Microchip(Long id, Boolean eliminado, String codigo, LocalDate fechaImplantacion, 
                    String veterinaria, String observaciones) {
        super(id, eliminado); // Llama al constructor completo de Base
        this.codigo = codigo;
        this.fechaImplantacion = fechaImplantacion;
        this.veterinaria = veterinaria;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters (Solo los campos específicos de Microchip)
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public LocalDate getFechaImplantacion() { return fechaImplantacion; }
    public void setFechaImplantacion(LocalDate fechaImplantacion) { this.fechaImplantacion = fechaImplantacion; }
    
    public String getVeterinaria() { return veterinaria; }
    public void setVeterinaria(String veterinaria) { this.veterinaria = veterinaria; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    @Override
    public String toString() {
        return "Microchip{" +
                "id=" + getId() +  // Usa getId() de Base
                ", codigo='" + codigo + '\'' +
                ", fechaImplantacion=" + fechaImplantacion +
                ", veterinaria='" + veterinaria + '\'' +
                ", eliminado=" + getEliminado() +  // Usa getEliminado() de Base
                '}';
    }
}