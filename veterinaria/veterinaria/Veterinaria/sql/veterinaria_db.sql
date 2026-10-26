CREATE DATABASE IF NOT EXISTS veterinaria_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE veterinaria_db;

CREATE TABLE IF NOT EXISTS duenos (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(80) NOT NULL,
 apellido VARCHAR(80) NOT NULL,
 documento VARCHAR(20) NULL UNIQUE,
 telefono VARCHAR(30) NULL,
 email VARCHAR(120) NULL UNIQUE,
 direccion VARCHAR(180) NULL,
 fecha_alta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS mascotas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 dueno_id INT NOT NULL,
 nombre VARCHAR(80) NOT NULL,
 especie VARCHAR(40) NOT NULL,
 raza VARCHAR(80) NULL,
 sexo ENUM('Macho','Hembra','No especificado') NOT NULL DEFAULT 'No especificado',
 fecha_nacimiento DATE NULL,
 color VARCHAR(60) NULL,
 peso_kg DECIMAL(6,2) NULL,
 observaciones TEXT NULL,
 fecha_alta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_mascotas_dueno FOREIGN KEY (dueno_id) REFERENCES duenos(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS veterinarios (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(80) NOT NULL,
 apellido VARCHAR(80) NOT NULL,
 documento VARCHAR(20) NULL UNIQUE,
 telefono VARCHAR(30) NULL,
 email VARCHAR(120) NULL UNIQUE,
 especialidad VARCHAR(100) NULL,
 activo TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS citas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 mascota_id INT NOT NULL,
 veterinario_id INT NOT NULL,
 fecha_hora DATETIME NOT NULL,
 motivo VARCHAR(180) NOT NULL,
 estado ENUM('Pendiente','Confirmada','Realizada','Cancelada') NOT NULL DEFAULT 'Pendiente',
 observaciones TEXT NULL,
 CONSTRAINT fk_citas_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT fk_citas_veterinario FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS consultas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 cita_id INT NULL,
 mascota_id INT NOT NULL,
 veterinario_id INT NOT NULL,
 fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 diagnostico TEXT NOT NULL,
 tratamiento TEXT NULL,
 peso_kg DECIMAL(6,2) NULL,
 costo DECIMAL(10,2) NOT NULL DEFAULT 0,
 observaciones TEXT NULL,
 CONSTRAINT fk_consultas_cita FOREIGN KEY (cita_id) REFERENCES citas(id) ON UPDATE CASCADE ON DELETE SET NULL,
 CONSTRAINT fk_consultas_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT fk_consultas_veterinario FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vacunas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL UNIQUE,
 descripcion VARCHAR(250) NULL,
 intervalo_dias INT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS mascota_vacunas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 mascota_id INT NOT NULL,
 vacuna_id INT NOT NULL,
 veterinario_id INT NULL,
 fecha_aplicacion DATE NOT NULL,
 proxima_dosis DATE NULL,
 lote VARCHAR(80) NULL,
 observaciones VARCHAR(250) NULL,
 CONSTRAINT fk_mv_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT fk_mv_vacuna FOREIGN KEY (vacuna_id) REFERENCES vacunas(id) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT fk_mv_veterinario FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

INSERT IGNORE INTO vacunas(nombre, descripcion, intervalo_dias) VALUES
('Antirrábica','Vacuna contra la rabia',365),
('Polivalente canina','Vacuna múltiple para perros',365),
('Triple felina','Vacuna múltiple para gatos',365);
