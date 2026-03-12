-- =========================================================
-- ParchApp - Script de creación de base de datos
-- Archivo: schema.sql
--
-- TABLAS:
--   1. usuario    → personas registradas en la plataforma
--   2. agencia    → empresas que publican planes turísticos
--   3. plan       → planes turísticos publicados por agencias
--   4. reserva    → reservas hechas por usuarios a planes
--
-- CÓMO EJECUTARLO:
--   MySQL Workbench: File > Open SQL Script > ejecutar
--   Terminal:        mysql -u root -p < schema.sql
-- =========================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS parchapp_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE parchapp_db;

-- =========================================================
-- TABLA: usuario
-- Almacena los datos de los usuarios registrados.
-- Campos basados en el diagrama ER: ID, NOMBRE, APELLIDO,
-- CORREO, EDAD, PAIS, CIUDAD, IDIOMA, TIPO_ID
-- =========================================================
CREATE TABLE IF NOT EXISTS usuario (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    tipo_id         VARCHAR(20)     NOT NULL COMMENT 'Tipo de documento: CC, CE, PASAPORTE',
    numero_id       VARCHAR(30)     NOT NULL COMMENT 'Número del documento de identidad',
    nombre          VARCHAR(100)    NOT NULL,
    apellido        VARCHAR(100)    NOT NULL,
    correo          VARCHAR(150)    NOT NULL UNIQUE,
    contrasena      VARCHAR(255)    NOT NULL COMMENT 'Contraseña encriptada (bcrypt)',
    edad            INT             NULL,
    pais            VARCHAR(100)    NULL DEFAULT 'Colombia',
    ciudad          VARCHAR(100)    NULL,
    idioma          VARCHAR(50)     NULL DEFAULT 'Español',
    fecha_registro  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_usuario_numero_id (numero_id),
    UNIQUE KEY uq_usuario_correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Usuarios registrados en ParchApp';


-- =========================================================
-- TABLA: agencia
-- Almacena los datos de las agencias que publican planes.
-- Campos basados en el diagrama ER: NOMBRE, CORREO,
-- DIRECCIÓN, TELEFONO
-- =========================================================
CREATE TABLE IF NOT EXISTS agencia (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(150)    NOT NULL,
    correo          VARCHAR(150)    NOT NULL UNIQUE,
    telefono        VARCHAR(20)     NULL,
    direccion       VARCHAR(255)    NULL,
    fecha_registro  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uq_agencia_correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Agencias de turismo registradas';


-- =========================================================
-- TABLA: plan
-- Planes turísticos publicados por una agencia.
-- Campos basados en el diagrama ER: ID_PLAN, VALOR,
-- DISPONIBILIDAD + datos del formulario publicar_planes.html
-- =========================================================
CREATE TABLE IF NOT EXISTS plan (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    agencia_id      BIGINT          NOT NULL COMMENT 'Agencia que publica el plan',
    nombre          VARCHAR(200)    NOT NULL COMMENT 'Nombre del plan turístico',
    descripcion     TEXT            NULL,
    destino         VARCHAR(150)    NULL COMMENT 'Lugar al que va el tour',
    valor           DECIMAL(12,2)   NOT NULL COMMENT 'Precio en COP',
    disponibilidad  INT             NOT NULL DEFAULT 0 COMMENT 'Cupos disponibles',
    imagen_url      VARCHAR(500)    NULL,
    activo          BOOLEAN         NOT NULL DEFAULT TRUE,
    fecha_creacion  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_plan_agencia FOREIGN KEY (agencia_id)
        REFERENCES agencia(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Planes turísticos disponibles';


-- =========================================================
-- TABLA: reserva
-- Reservas realizadas por un usuario sobre un plan.
-- Campos basados en el diagrama ER: CODIGO_RESERVA,
-- FECHA_IN, FECHA_FIN, FECHA_CAN, ID_PLAN, AGENCIA
-- =========================================================
CREATE TABLE IF NOT EXISTS reserva (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    codigo_reserva      VARCHAR(50)     NOT NULL UNIQUE COMMENT 'Código único generado al crear la reserva',
    usuario_id          BIGINT          NOT NULL COMMENT 'Usuario que realiza la reserva',
    plan_id             BIGINT          NOT NULL COMMENT 'Plan reservado',
    agencia_id          BIGINT          NOT NULL COMMENT 'Agencia del plan',
    fecha_inicio        DATE            NOT NULL COMMENT 'Fecha de inicio del tour',
    fecha_fin           DATE            NULL    COMMENT 'Fecha de fin del tour',
    fecha_cancelacion   DATETIME        NULL    COMMENT 'Fecha en que se canceló (null = activa)',
    cantidad_personas   INT             NOT NULL DEFAULT 1,
    total_pagar         DECIMAL(12,2)   NOT NULL COMMENT 'Total a pagar en COP',
    estado              VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE'
                        COMMENT 'Estados: PENDIENTE, CONFIRMADA, CANCELADA',
    fecha_creacion      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_reserva_plan FOREIGN KEY (plan_id)
        REFERENCES plan(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_reserva_agencia FOREIGN KEY (agencia_id)
        REFERENCES agencia(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Reservas de planes turísticos';


-- =========================================================
-- DATOS DE PRUEBA (opcionales, comentar en producción)
-- =========================================================

-- Agencias de prueba
INSERT INTO agencia (nombre, correo, telefono, direccion) VALUES
    ('Tours Antioquia SAS', 'contacto@toursantioquia.com', '3001234567', 'Cra 50 # 40-20, Medellín'),
    ('Aventura Paisa Ltda',  'info@aventurapaisa.com',     '3107654321', 'Cl 10 # 65-30, Medellín');

-- Usuario de prueba (contraseña en texto plano solo para pruebas, en producción usar BCrypt)
INSERT INTO usuario (tipo_id, numero_id, nombre, apellido, correo, contrasena, edad, ciudad, pais) VALUES
    ('CC', '1234567890', 'Juan', 'Pérez', 'juan@example.com', 'cambiar_en_produccion', 25, 'Medellín', 'Colombia');

-- Plan de prueba
INSERT INTO plan (agencia_id, nombre, descripcion, destino, valor, disponibilidad) VALUES
    (1, 'Tour Guatapé Completo',  'Visita al Peñol, paseo en lancha y recorrido por el municipio.', 'Guatapé',           120000.00, 10),
    (1, 'Tour Jardín Mágico',     'Visita al pueblo más bonito de Antioquia con senderismo.',        'Jardín',            150000.00, 5),
    (2, 'Santa Fe Colonial',      'Recorrido histórico por Santa Fe de Antioquia.',                  'Santa Fe Antioquia', 100000.00, 8);

-- Reserva de prueba
INSERT INTO reserva (codigo_reserva, usuario_id, plan_id, agencia_id, fecha_inicio, cantidad_personas, total_pagar, estado) VALUES
    ('PARCH-20240001', 1, 1, 1, '2025-04-15', 2, 240000.00, 'CONFIRMADA');
