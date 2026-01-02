USE mascota_microchip;

-- INSERCIONES CORRECTAS
-- 1. Insertar MICROCHIP
INSERT INTO microchip (id, codigo, fecha_implantacion, veterinaria) 
VALUES 
(1, 'CHIP-123456789', '2023-01-10', 'Veterinaria Canina'),
(2, 'CHIP-113456789', '2025-02-09', 'Veterinaria General'),
(3, 'CHIP-113336789', '2025-01-20', 'Veterinaria San Juan');

-- 2. Insertar MASCOTA con referencia al microchip
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, duenio, microchip_id) 
VALUES 
('Huesos', 'PERRO', 'Galgo', '2020-05-15', 'Homero Simpson', 1),
('Snowball', 'GATO', 'Negro', '2022-09-12','Lisa Simpson',2),
('Pipo', 'AVE', 'Cotorra', '2025-01-05','Juana Martinez',3);

-- INSERCIONES ERRÓNEAS
-- 1. Error de UNIQUE en código de microchip
INSERT INTO microchip (codigo, fecha_implantacion) 
VALUES ('CHIP-123456789', '2024-05-23');
-- Error Code: 1062. Duplicate entry 'CHIP-123456789' for key 'codigo'

-- 2. Error de NOT NULL en campo obligatorio
INSERT INTO mascota (nombre, especie, duenio) 
VALUES (NULL, 'GATO', 'Lisa Simpson');
-- Error Code: 1048. Column 'nombre' cannot be null
