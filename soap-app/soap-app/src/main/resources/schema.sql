-- Primero eliminamos todas las restricciones de clave foránea que puedan existir
ALTER TABLE movimientos DROP FOREIGN KEY IF EXISTS FK1l8x7bomg6fla82i9m0ves41r;
ALTER TABLE movimientos DROP FOREIGN KEY IF EXISTS FKoxlnbqi5x711wlbq3v89127kd;

-- Modificamos la columna numero_cuenta
ALTER TABLE cuentas MODIFY COLUMN numero_cuenta VARCHAR(12) NOT NULL;

-- Recreamos la restricción de clave foránea
ALTER TABLE movimientos 
ADD CONSTRAINT FK_movimientos_cuentas 
FOREIGN KEY (numero_cuenta) 
REFERENCES cuentas(numero_cuenta);

CREATE TABLE IF NOT EXISTS movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cuenta_origen VARCHAR(12) NOT NULL,
    cuenta_destino VARCHAR(12) NOT NULL,
    fecha_movimiento DATE NOT NULL,
    hora_movimiento TIME NOT NULL,
    tipo_movimiento CHAR(1) NOT NULL,
    monto DECIMAL(16,2) NOT NULL,
    numero_referencia VARCHAR(16) NOT NULL,
    FOREIGN KEY (cuenta_origen) REFERENCES cuentas(numero_cuenta),
    FOREIGN KEY (cuenta_destino) REFERENCES cuentas(numero_cuenta)
); 