SET @@autocommit = 1;
START TRANSACTION;
CREATE DATABASE IF NOT EXISTS Jogo;
USE Jogo;
CREATE TABLE usuario(
	nombre		VARCHAR(100) NOT NULL UNIQUE,
    contraseña	VARCHAR(100) NOT NULL,
    email		VARCHAR(100) UNIQUE,
    foto		LONGBLOB,
    descripcion VARCHAR(33),
    validado BOOLEAN,
    CONSTRAINT pk_usuario PRIMARY KEY usuario(nombre) 
);

CREATE TABLE evento(
	id		INT UNSIGNED AUTO_INCREMENT,
    ciudad	VARCHAR(100) NOT NULL,
    calle	VARCHAR(100) NOT NULL,
    localidad VARCHAR(100) NOT NULL,
    hora	TIME NOT NULL,
    dia		DATE NOT NULL,
    nombre	VARCHAR(100) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    plazas  BIGINT UNSIGNED NOT NULL,
    CONSTRAINT pk_evento PRIMARY KEY evento(id) 
);

CREATE TABLE usuario_evento(
	nombre	VARCHAR(100),
    id		INT UNSIGNED,
    CONSTRAINT pk_usuario_evento PRIMARY KEY usuario_evento(nombre,id),
    CONSTRAINT fk_usuario_evento FOREIGN KEY usuario_evento(nombre) REFERENCES usuario(nombre) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_evento2 FOREIGN KEY usuario_evento(id) REFERENCES evento(id) ON DELETE CASCADE
);

CREATE TABLE asistir_evento(
	nombre	VARCHAR(100),
    id		INT UNSIGNED,
	CONSTRAINT pk_asistir_evento PRIMARY KEY asistir_evento(nombre,id),
	CONSTRAINT fk_asistir_evento FOREIGN KEY asistir_evento(nombre) REFERENCES usuario(nombre) ON DELETE CASCADE,
    CONSTRAINT fk_asistir_evento2 FOREIGN KEY asistir_evento(id) REFERENCES evento(id) ON DELETE CASCADE
);

COMMIT;

DELIMITER //
	CREATE TRIGGER creacionU AFTER INSERT ON usuario  FOR EACH ROW
    BEGIN
		UPDATE usuario
			SET foto = LOAD_FILE('C:\Users\pavog\Documents\Jofo\Recursos\usuario.pg')
            WHERE nombre = new.nombre;
    END //
DELIMITER ;

