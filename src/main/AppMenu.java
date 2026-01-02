/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import models.Mascota;
import models.Microchip;
import service.MascotaService;
import service.MicrochipService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AppMenu {
    private final Scanner scanner;
    private final MascotaService mascotaService;
    private final MicrochipService microchipService;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.mascotaService = new MascotaService();
        this.microchipService = new MicrochipService();
    }

    public void iniciar() {
        System.out.println("=== SISTEMA MASCOTA-MICROCHIP ===");

        while (true) {
            mostrarMenuPrincipal();
            String opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1" -> crearMascota();
                    case "2" -> listarMascotas();
                    case "3" -> buscarMascotaPorId();
                    case "4" -> actualizarMascota();
                    case "5" -> eliminarMascota();
                    case "6" -> buscarMascotasPorDuenio();
                    case "7" -> gestionarMicrochips();
                    case "0" -> {
                        System.out.println("¡Hasta luego!");
                        return;
                    }
                    default -> System.out.println("Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println("Presione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Crear Mascota");
        System.out.println("2. Listar Mascotas");
        System.out.println("3. Buscar Mascota por ID");
        System.out.println("4. Actualizar Mascota");
        System.out.println("5. Eliminar Mascota (lógico)");
        System.out.println("6. Buscar Mascotas por Dueño");
        System.out.println("7. Gestionar Microchips");
        System.out.println("0. Salir");
        System.out.print("Seleccione: ");
    }

    private void crearMascota() {
        System.out.println("\n--- CREAR MASCOTA ---");
        try {
            Mascota mascota = new Mascota();
            System.out.print("Nombre: ");
            mascota.setNombre(scanner.nextLine());
            
            System.out.print("Especie (PERRO/GATO/AVE/PEZ/REPTIL/OTRO): ");
            mascota.setEspecie(scanner.nextLine().toUpperCase());
            
            System.out.print("Raza: ");
            mascota.setRaza(scanner.nextLine());
            
            System.out.print("Fecha Nacimiento (yyyy-mm-dd): ");
            String fecha = scanner.nextLine();
            if (!fecha.isEmpty()) {
                mascota.setFechaNacimiento(LocalDate.parse(fecha));
            }
            
            System.out.print("Dueño: ");
            mascota.setDuenio(scanner.nextLine());

            Mascota resultado = mascotaService.insertar(mascota);
            System.out.println("Mascota creada: " + resultado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarMascotas() {
        try {
            System.out.println("\n--- LISTA MASCOTAS ---");
            List<Mascota> mascotas = mascotaService.getAll();
            if (mascotas.isEmpty()) {
                System.out.println("No hay mascotas");
            } else {
                mascotas.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarMascotaPorId() {
        try {
            System.out.print("ID Mascota: ");
            Long id = Long.valueOf(scanner.nextLine());
            Mascota mascota = mascotaService.getById(id);
            if (mascota != null) {
                System.out.println("Encontrada: " + mascota);
            } else {
                System.out.println("No encontrada");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarMascota() {
        try {
            System.out.print("ID Mascota a actualizar: ");
            Long id = Long.valueOf(scanner.nextLine());
            
            Mascota existente = mascotaService.getById(id);
            if (existente == null) {
                System.out.println("Mascota no encontrada");
                return;
            }

            System.out.println("Actual: " + existente);
            Mascota actualizada = new Mascota();
            actualizada.setId(id);

            System.out.print("Nuevo nombre: ");
            actualizada.setNombre(scanner.nextLine());

            System.out.print("Nueva especie: ");
            actualizada.setEspecie(scanner.nextLine().toUpperCase());

            System.out.print("Nueva raza: ");
            actualizada.setRaza(scanner.nextLine());

            System.out.print("Nuevo dueño: ");
            actualizada.setDuenio(scanner.nextLine());

            // Mantener microchip actual
            actualizada.setMicrochip(existente.getMicrochip());

            Mascota resultado = mascotaService.actualizar(actualizada);
            System.out.println("Actualizada: " + resultado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarMascota() {
        try {
            System.out.print("ID Mascota a eliminar: ");
            Long id = Long.valueOf(scanner.nextLine());
            
            System.out.print("¿Seguro? (s/n): ");
            String confirma = scanner.nextLine();
            if (confirma.equalsIgnoreCase("s")) {
                boolean resultado = mascotaService.eliminar(id);
                System.out.println(resultado ? "Eliminada" : "Error al eliminar");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarMascotasPorDuenio() {
        try {
            System.out.print("Nombre dueño: ");
            String duenio = scanner.nextLine();
            List<Mascota> mascotas = mascotaService.buscarPorDuenio(duenio);
            
            if (mascotas.isEmpty()) {
                System.out.println("No se encontraron mascotas");
            } else {
                mascotas.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void gestionarMicrochips() {
  while (true) {
        System.out.println("\n--- GESTIÓN MICROCHIPS ---");
        System.out.println("1. Crear Microchip");
        System.out.println("2. Listar Microchips");
        System.out.println("3. Asignar a Mascota");
        System.out.println("4. Buscar por ID");
        System.out.println("5. Eliminar Microchip (lógico)");
        System.out.println("0. Volver");
        System.out.print("Seleccione: ");

        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1" -> crearMicrochip();
            case "2" -> listarMicrochips();
            case "3" -> asignarMicrochip();
            case "4" -> buscarMicrochipPorId();
            case "5" -> eliminarMicrochip();
            case "0" -> { return; }
            default -> System.out.println("Opción no válida");
            }
        }
    }

    private void crearMicrochip() {
        try {
            Microchip microchip = new Microchip();
            System.out.print("Código: ");
            microchip.setCodigo(scanner.nextLine());
            
            System.out.print("Fecha Implantación (yyyy-mm-dd): ");
            String fecha = scanner.nextLine();
            if (!fecha.isEmpty()) {
                microchip.setFechaImplantacion(LocalDate.parse(fecha));
            }
            
            System.out.print("Veterinaria: ");
            microchip.setVeterinaria(scanner.nextLine());
            
            System.out.print("Observaciones: ");
            microchip.setObservaciones(scanner.nextLine());

            Microchip resultado = microchipService.insertar(microchip);
            System.out.println("Microchip creado: " + resultado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarMicrochips() {
        try {
            List<Microchip> microchips = microchipService.getAll();
            if (microchips.isEmpty()) {
                System.out.println("No hay microchips");
            } else {
                microchips.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void asignarMicrochip() {
        try {
            System.out.print("ID Mascota: ");
            Long mascotaId = Long.valueOf(scanner.nextLine());
            System.out.print("ID Microchip: ");
            Long microchipId = Long.valueOf(scanner.nextLine());

            Mascota resultado = mascotaService.asignarMicrochip(mascotaId, microchipId);
            System.out.println("Asignado: " + resultado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarMicrochipPorId() {
        try {
            System.out.print("ID Microchip: ");
            Long id = Long.valueOf(scanner.nextLine());
            Microchip microchip = microchipService.getById(id);
            if (microchip != null) {
                System.out.println("Encontrado: " + microchip);
            } else {
                System.out.println("No encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void eliminarMicrochip() {
    try {
        System.out.print("ID Microchip a eliminar: ");
        Long id = Long.valueOf(scanner.nextLine());
        
        System.out.print("¿Seguro? (s/n): ");
        String confirma = scanner.nextLine();
        if (confirma.equalsIgnoreCase("s")) {
            boolean resultado = microchipService.eliminar(id);
            System.out.println(resultado ? "Microchip eliminado" : "Error al eliminar");
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
  }
}

