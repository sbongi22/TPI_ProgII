CREATE DATABASE if not exists mascota_microchip;
USE mascota_microchip;

CREATE TABLE if not exists microchip (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(25) NOT NULL UNIQUE,
    fecha_implantacion DATE,
    veterinaria VARCHAR(120),
    observaciones VARCHAR(255),
    eliminado BOOLEAN DEFAULT FALSE    
);
CREATE TABLE if not exists mascota (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    especie VARCHAR(30) NOT NULL,
    raza VARCHAR(60),
    fecha_nacimiento DATE,
    duenio VARCHAR(120) NOT NULL,
    eliminado BOOLEAN DEFAULT FALSE,
    CONSTRAINT chk_especie CHECK (especie IN ('PERRO', 'GATO', 'AVE', 'PEZ', 'REPTIL', 'OTRO')),
    microchip_id BIGINT UNIQUE,
    FOREIGN KEY (microchip_id) REFERENCES microchip(id) ON DELETE SET NULL
);
CREATE INDEX idx_mascota_especie ON mascota(especie);
CREATE INDEX idx_mascota_duenio ON mascota(duenio);
CREATE INDEX idx_microchip_fecha ON microchip(fecha_implantacion);

/*drop database mascota_microchip;*/