use almacen;

-- 1.Crear bd
CREATE SCHEMA `almacen` ;

-- 2. Crear entidad Roles
CREATE TABLE roles (
	idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombreRol VARCHAR(250) NOT NULL UNIQUE
);

-- 3. Crear entidad usuarios
CREATE TABLE usuarios (
    idUsuario INT(6) AUTO_INCREMENT PRIMARY KEY,
    idRol INT(2) NOT NULL,
    nombre VARCHAR(100),
    correo VARCHAR(50),
    contrasena VARCHAR(25),
    estatus INT(1),
    
	CONSTRAINT fk_usuario_rol FOREIGN KEY (idRol) REFERENCES roles(idRol)
);

-- 4. Crear la entidad productos
CREATE TABLE productos (
	idProducto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cantidad INT DEFAULT 0,
    estatus BOOLEAN DEFAULT TRUE
    
);

-- 5. Crear entidad historico
CREATE TABLE historico (
	idMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    idProducto INT NOT NULL,
    idUsuario INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA'),
    cantidad INT,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_historico_producto FOREIGN KEY (idProducto) REFERENCES productos(idProducto),

    CONSTRAINT fk_historico_usuario FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
    
);

-- 6 Crear entidad de permisos
CREATE TABLE permisos (
	idPermiso INT AUTO_INCREMENT PRIMARY KEY,
    nombrePermiso VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- 7. Crear entidad intermedia m-m
CREATE TABLE roles_permisos (
	idRol INT,
    idPermiso INT,
    PRIMARY KEY (idRol, idPermiso),
    FOREIGN KEY (idRol) REFERENCES roles(idRol) ON DELETE CASCADE,
    FOREIGN KEY (idPermiso) REFERENCES permisos(idPermiso) ON DELETE CASCADE
);

-- 6 Datos iniciales
INSERT INTO roles (nombreRol) VALUES ('ADMINISTRADOR'), ('ALMACENISTA');

INSERT INTO productos (nombre, cantidad, estatus) VALUES ('Laptop Dell', 10, TRUE), ('Mouse Logitech', 50, TRUE);

INSERT INTO usuarios (idRol, nombre, correo, contrasena, estatus) VALUES(1, 'Administrador', 'admin@empresa.com', '123456', TRUE);
INSERT INTO usuarios (idRol, nombre, correo, contrasena, estatus) VALUES(2, 'Almacenista', 'almacenista@empresa.com', '123456', TRUE);

INSERT INTO permisos (nombrePermiso, descripcion) VALUES 
('VER_MODULO_INVENTARIO', 'Permite visualizar el módulo de inventario'),
('AGREGAR_NUEVOS_PRODUCTO', 'Permite registrar nuevos productos'),
('AUMENTAR_INVENTARIO', 'Permite añadir existencias a un producto'),
('BAJA_REACTIVAR_PRODUCTO', 'Permite activar o desactivar productos'),
('VER_SALIDAS', 'Permite ver el módulo de salida de productos'),
('SACAR_INVENTARIO_ALAMACEN', 'Permite retirar stock del almacén'),
('VER_HISTORICO', 'Permite consultar el historial de movimientos');

-- Asignar permisos al Administrador (idRol = 1)
INSERT INTO roles_permisos (idRol, idPermiso) VALUES 
(1, 1), -- VER_INVENTARIO
(1, 2), -- AGREGAR_PRODUCTO
(1, 3), -- AUMENTAR_INVENTARIO
(1, 4), -- BAJA_REACTIVAR_PRODUCTO
(1, 7); -- VER_HISTORICO

-- Asignar permisos al Almacenista (idRol = 2)
INSERT INTO roles_permisos (idRol, idPermiso) VALUES 
(2, 1), -- VER_INVENTARIO
(2, 5), -- VER_SALIDAS
(2, 6); -- SACAR_INVENTARIO



