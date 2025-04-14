CREATE DATABASE IF NOT EXISTS db_rest;
USE db_rest;

CREATE TABLE `usuarios` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(100) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(50) DEFAULT 'USER'
);

CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `identificacion` varchar(255) DEFAULT NULL,
  `tipo_identificacion` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `identificacion` (`identificacion`)
);

CREATE TABLE `cuentas` (
  `nro_cta` varchar(20) NOT NULL,
  `id_cliente` int NOT NULL,
  `fecha_apertura` date NOT NULL,
  `hora_apertura` time NOT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `saldo` decimal(15,2) DEFAULT NULL,
  `numero_cuenta` varchar(12) NOT NULL,
  `cliente_id` bigint DEFAULT NULL,
  PRIMARY KEY (`nro_cta`),
  KEY `id_cliente` (`id_cliente`),
  CONSTRAINT `cuentas_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`)
);

CREATE TABLE `movimientos` (
  `numero_referencia` varchar(20) NOT NULL,
  `cuenta_origen` varchar(20) NOT NULL,
  `cuenta_destino` varchar(20) NOT NULL,
  `fecha_movimiento` date NOT NULL,
  `hora_movimiento` time NOT NULL,
  `tipo_movimiento` varchar(255) DEFAULT NULL,
  `monto` decimal(38,2) DEFAULT NULL,
  `nro_cta` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`numero_referencia`),
  KEY `cuenta_origen` (`cuenta_origen`),
  KEY `cuenta_destino` (`cuenta_destino`),
  KEY `FKbfbygc1iphnv01hp5siwvltn6` (`nro_cta`),
  CONSTRAINT `FKbfbygc1iphnv01hp5siwvltn6` FOREIGN KEY (`nro_cta`) REFERENCES `cuentas` (`nro_cta`),
  CONSTRAINT `movimientos_ibfk_1` FOREIGN KEY (`cuenta_origen`) REFERENCES `cuentas` (`nro_cta`),
  CONSTRAINT `movimientos_ibfk_2` FOREIGN KEY (`cuenta_destino`) REFERENCES `cuentas` (`nro_cta`)
);

INSERT INTO usuarios (username, password, role)
VALUES ('admin', '$2a$10$yFV6KbOvz6H8Pq0FqRvXJ.5DKaZxfZ41BoRjwHtYIUkeM/lkOnK3C', 'ADMIN');

INSERT INTO clientes (id_cliente, nombre, identificacion, tipo_identificacion, fecha_nacimiento) VALUES 
(100, 'Lucía Arevalo', '0101199112456', 'DNI', '1991-01-01'),
(101, 'Pedro Mateo Quintero', '0202198723091', 'DNI', '1987-02-02'),
(102, 'Paola Figueroa', '0303199033172', 'DNI', '1990-03-03'),
(103, 'Ricardo Laureano', '0404198523110', 'DNI', '1985-04-04'),
(104, 'Samuel Casares', '0505198823893', 'DNI', '1988-05-05'),
(105, 'Julio Feliciano', '0606198911234', 'DNI', '1989-06-06'),
(106, 'Amalia León', '0707199022222', 'DNI', '1990-07-07'),
(107, 'Francisco Cotto', '0808198919876', 'DNI', '1989-08-08'),
(108, 'Gilberto Camarillo', '0909198523456', 'DNI', '1985-09-09'),
(109, 'Rodrigo Lovato', '1010198811223', 'DNI', '1988-10-10'),
(110, 'Helena Cardona', '1111199215581', 'DNI', '1992-11-11'),
(111, 'Luisa Botello', '1212198920349', 'DNI', '1989-12-12'),
(112, 'Rubén Saavedra', '1301199518890', 'DNI', '1995-01-13'),
(113, 'Alfredo Alejandro', '1402199317455', 'DNI', '1993-02-14'),
(114, 'Rebeca Hernández', '1503199012867', 'DNI', '1990-03-15'),
(115, 'Cornelio Almonte', '1604199428791', 'DNI', '1994-04-16'),
(116, 'María Benítez', '1705199123340', 'DNI', '1991-05-17'),
(117, 'Lorena Anguiano', '1806199323984', 'DNI', '1993-06-18'),
(118, 'Mateo Montañez', '1907199128765', 'DNI', '1991-07-19'),
(119, 'Ruby Orta', '2008199516742', 'DNI', '1995-08-20'),
(120, 'Gerónimo Carrasco', '2109199023581', 'DNI', '1990-09-21'),
(121, 'Mariana Caballero', '2210198919850', 'DNI', '1989-10-22'),
(122, 'Dalia Pedroza', '2311199319901', 'DNI', '1993-11-23'),
(123, 'Esteban Romero', '2412199212349', 'DNI', '1992-12-24'),
(124, 'Renato Espino', '2501198524790', 'DNI', '1985-01-25');

INSERT INTO cuentas (nro_cta, id_cliente, fecha_apertura, hora_apertura, estado, saldo, numero_cuenta) VALUES
('300000000001', 100, '2023-01-10', '08:00:00', 'ACTIVO', 1500.00, '300000000001'),
('300000000002', 101, '2023-01-11', '08:15:00', 'ACTIVO', 0.00, '300000000002'),
('300000000003', 102, '2023-01-12', '08:30:00', 'INACTIVA', 3200.50, '300000000003'),
('300000000004', 103, '2023-01-13', '08:45:00', 'ACTIVO', 1200.00, '300000000004'),
('300000000005', 104, '2023-01-14', '09:00:00', 'INACTIVA', 0.00, '300000000005'),
('300000000006', 105, '2023-01-15', '09:15:00', 'ACTIVO', 870.00, '300000000006'),
('300000000007', 106, '2023-01-16', '09:30:00', 'ACTIVO', 5000.00, '300000000007'),
('300000000008', 107, '2023-01-17', '09:45:00', 'INACTIVA', 0.00, '300000000008'),
('300000000009', 108, '2023-01-18', '10:00:00', 'ACTIVO', 2100.75, '300000000009'),
('300000000010', 109, '2023-01-19', '10:15:00', 'ACTIVO', 0.00, '300000000010'),
('300000000011', 110, '2023-01-20', '10:30:00', 'INACTIVA', 990.00, '300000000011'),
('300000000012', 111, '2023-01-21', '10:45:00', 'ACTIVO', 3000.00, '300000000012'),
('300000000013', 112, '2023-01-22', '11:00:00', 'ACTIVO', 0.00, '300000000013'),
('300000000014', 113, '2023-01-23', '11:15:00', 'INACTIVA', 450.00, '300000000014'),
('300000000015', 114, '2023-01-24', '11:30:00', 'ACTIVO', 210.00, '300000000015'),
('300000000016', 115, '2023-01-25', '11:45:00', 'ACTIVO', 7800.00, '300000000016'),
('300000000017', 116, '2023-01-26', '12:00:00', 'INACTIVA', 0.00, '300000000017'),
('300000000018', 117, '2023-01-27', '12:15:00', 'ACTIVO', 500.00, '300000000018'),
('300000000019', 118, '2023-01-28', '12:30:00', 'ACTIVO', 1000.00, '300000000019'),
('300000000020', 119, '2023-01-29', '12:45:00', 'INACTIVA', 100.00, '300000000020'),
('300000000021', 120, '2023-01-30', '13:00:00', 'ACTIVO', 0.00, '300000000021'),
('300000000022', 121, '2023-01-31', '13:15:00', 'INACTIVA', 4100.00, '300000000022'),
('300000000023', 122, '2023-02-01', '13:30:00', 'ACTIVO', 725.00, '300000000023'),
('300000000024', 123, '2023-02-02', '13:45:00', 'ACTIVO', 0.00, '300000000024'),
('300000000025', 124, '2023-02-03', '14:00:00', 'INACTIVA', 200.00, '300000000025')
('300000000026', 101, '2023-03-01', '08:10:00', 'ACTIVO', 3000.00, '300000000026'),
('300000000027', 105, '2023-03-02', '09:20:00', 'INACTIVA', 0.00, '300000000027'),
('300000000028', 110, '2023-03-03', '10:30:00', 'ACTIVO', 550.00, '300000000028'),
('300000000029', 114, '2023-03-04', '11:40:00', 'ACTIVO', 1800.00, '300000000029'),
('300000000030', 114, '2023-03-05', '12:50:00', 'INACTIVA', 125.00, '300000000030'),
('300000000031', 117, '2023-03-06', '13:00:00', 'ACTIVO', 920.00, '300000000031'),
('300000000032', 117, '2023-03-07', '13:15:00', 'INACTIVA', 0.00, '300000000032'),
('300000000033', 118, '2023-03-08', '14:20:00', 'ACTIVO', 675.00, '300000000033'),
('300000000034', 120, '2023-03-09', '15:25:00', 'ACTIVO', 4000.00, '300000000034'),
('300000000035', 122, '2023-03-10', '16:30:00', 'INACTIVA', 300.00, '300000000035'),
('300000000036', 122, '2023-03-11', '16:45:00', 'ACTIVO', 0.00, '300000000036'),
('300000000037', 124, '2023-03-12', '17:00:00', 'ACTIVO', 5200.00, '300000000037');

