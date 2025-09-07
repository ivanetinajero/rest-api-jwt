drop table if exists UsuariosPerfiles;
drop table if exists Usuarios;
drop table if exists Perfiles;
drop table if exists Productos;
drop table if exists Sucursales;

-- Tabla de Perfiles
CREATE TABLE Perfiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE `Sucursales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `clave` varchar(200) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(100) NOT NULL,
  `estatus` enum('Activa','Inactiva') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clave_UNIQUE` (`clave`)
);

-- Tabla de Usuarios
CREATE TABLE `Usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `nombreCompleto` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `estatus` int NOT NULL,
  `password` varchar(100) NOT NULL,
  `idSucursal` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `fk_Usuarios_1_idx` (`idSucursal`),
  CONSTRAINT `fk_Usuarios_Sucursales` FOREIGN KEY (`idSucursal`) REFERENCES `Sucursales` (`id`)
);

-- Tabla intermedia UsuariosPerfiles
CREATE TABLE UsuariosPerfiles (
    idUsuario BIGINT NOT NULL,
    idPerfil INT NOT NULL,
    PRIMARY KEY (idUsuario, idPerfil),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(id),
    FOREIGN KEY (idPerfil) REFERENCES Perfiles(id)
);

-- Tabla Productos (CRUD)
CREATE TABLE Productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DOUBLE,
    cantidad INT,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Perfiles (perfil) VALUES ('ADMIN'), ('SUPERVISOR'), ('USUARIO');

-- Insert de productos
INSERT INTO Productos (nombre, descripcion, precio, cantidad, fechaCreacion) VALUES
('Laptop', 'Laptop profesional', 18500.00, 10, NOW()),
('Mouse', 'Mouse inalámbrico', 350.00, 50, NOW()),
('Teclado', 'Teclado mecánico', 1200.00, 30, NOW()),
('Monitor', 'Monitor 24 pulgadas', 3200.00, 15, NOW()),
('Impresora', 'Impresora multifuncional', 2500.00, 8, NOW()),
('Tablet', 'Tablet Android', 4200.00, 12, NOW()),
('Smartphone', 'Teléfono inteligente', 7800.00, 20, NOW()),
('Auriculares', 'Auriculares Bluetooth', 650.00, 40, NOW()),
('Cámara', 'Cámara digital', 5400.00, 7, NOW()),
('Disco Duro', 'Disco duro externo 1TB', 1100.00, 25, NOW()),
('Memoria USB', 'Memoria USB 64GB', 180.00, 60, NOW()),
('Router', 'Router WiFi', 900.00, 18, NOW()),
('Proyector', 'Proyector portátil', 3500.00, 5, NOW()),
('Silla Gamer', 'Silla ergonómica', 2900.00, 6, NOW()),
('Microfono', 'Micrófono profesional', 800.00, 14, NOW()),
('Webcam', 'Webcam HD', 450.00, 22, NOW()),
('Fuente Poder', 'Fuente de poder 600W', 950.00, 11, NOW()),
('Tarjeta Video', 'Tarjeta gráfica 8GB', 7200.00, 4, NOW()),
('SSD', 'Disco SSD 512GB', 1300.00, 16, NOW()),
('Altavoces', 'Altavoces estéreo', 650.00, 13, NOW());

INSERT INTO Sucursales (nombre, clave, direccion, telefono, estatus) VALUES
('Sucursal Centro', 'CENTRO01', 'Av. Juárez 123, Centro', '555-1234', 'Activa'),
('Sucursal Norte', 'NORTE01', 'Calle Hidalgo 456, Norte', '555-5678', 'Activa'),
('Sucursal Sur', 'SUR01', 'Blvd. Morelos 789, Sur', '555-9012', 'Activa'),
('Sucursal Poniente', 'PONIENTE01', 'Av. Reforma 321, Poniente', '555-3456', 'Inactiva'),
('Sucursal Oriente', 'ORIENTE01', 'Calle Zaragoza 654, Oriente', '555-7890', 'Activa');

INSERT INTO `Usuarios` (`id`,`username`,`nombreCompleto`,`email`,`estatus`,`password`,`idSucursal`) VALUES (1,'itinajero','Iván Eliseo Tinajero Díaz','ivanetinajero@gmail.com',1,'$2y$10$k/i/HSE5ViCT9CMnewDaaO3r0O6uVM/vF8vpGneYv/bfythIA/zXO',1);
INSERT INTO `Usuarios` (`id`,`username`,`nombreCompleto`,`email`,`estatus`,`password`,`idSucursal`) VALUES (2,'maria','María Campos Lima','maria@ejemplo.com',1,'$2y$10$k/i/HSE5ViCT9CMnewDaaO3r0O6uVM/vF8vpGneYv/bfythIA/zXO',3);

INSERT INTO `UsuariosPerfiles` (`idUsuario`, `idPerfil`) VALUES ('1', '1');
INSERT INTO `UsuariosPerfiles` (`idUsuario`, `idPerfil`) VALUES ('2', '2');
