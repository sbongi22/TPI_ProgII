/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;

/*
Clase que representa una mascota en el sistema.
Extiende de la clase Base e incluye atributos específicos como:
nombre, especie, raza, fecha de nacimiento, dueño y microchip asociado.
 */

public class Mascota extends Base {
    private String nombre;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private String duenio;
    private Microchip microchip;
    
    // Constructores
    public Mascota() {
        super(); // Llama al constructor por defecto de Base
    }
    
    public Mascota(Long id, Boolean eliminado, String nombre, String especie, String raza, 
                  LocalDate fechaNacimiento, String duenio, Microchip microchip) {
        super(id, eliminado); // Llama al constructor completo de Base
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.duenio = duenio;
        this.microchip = microchip;
    }
    
    // Getters y Setters (SOLO los campos específicos de Mascota)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getDuenio() { return duenio; }
    public void setDuenio(String duenio) { this.duenio = duenio; }
    
    public Microchip getMicrochip() { return microchip; }
    public void setMicrochip(Microchip microchip) { this.microchip = microchip; }
    
    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + getId() +  // Usa getId() de Base
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", raza='" + raza + '\'' +
                ", dueño='" + duenio + '\'' +
                ", microchip=" + (microchip != null ? microchip.getCodigo() : "Sin microchip") +
                ", eliminado=" + getEliminado() +  // Usa getEliminado() de Base
                '}';
    }
}