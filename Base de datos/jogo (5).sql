-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-04-2023 a las 20:19:33
-- Versión del servidor: 10.4.25-MariaDB
-- Versión de PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `jogo`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistir_evento`
--

CREATE TABLE `asistir_evento` (
  `nombre` varchar(100) NOT NULL,
  `id` int(10) UNSIGNED NOT NULL,
  `fecha_insercion` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evento`
--

CREATE TABLE `evento` (
  `id` int(10) UNSIGNED NOT NULL,
  `ciudad` varchar(100) NOT NULL,
  `calle` varchar(100) NOT NULL,
  `localidad` varchar(100) NOT NULL,
  `hora` time NOT NULL,
  `dia` date NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(1000) NOT NULL,
  `plazas` bigint(20) UNSIGNED NOT NULL,
  `nombre_usuario` varchar(100) DEFAULT NULL,
  `QR` longblob DEFAULT NULL,
  `latitud` double DEFAULT NULL,
  `longitud` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seguidores`
--

CREATE TABLE `seguidores` (
  `nombre` varchar(100) NOT NULL,
  `nombre_seguidor` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `nombre` varchar(100) NOT NULL,
  `contraseña` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `foto` longblob DEFAULT NULL,
  `descripcion` varchar(33) DEFAULT NULL,
  `validado` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asistir_evento`
--
ALTER TABLE `asistir_evento`
  ADD PRIMARY KEY (`nombre`,`id`),
  ADD KEY `fk_asistir_evento2` (`id`);

--
-- Indices de la tabla `evento`
--
ALTER TABLE `evento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_nombre_usuario` (`nombre_usuario`);

--
-- Indices de la tabla `seguidores`
--
ALTER TABLE `seguidores`
  ADD PRIMARY KEY (`nombre`,`nombre_seguidor`),
  ADD KEY `fk_id_usuario_seguidores` (`nombre_seguidor`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`nombre`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `evento`
--
ALTER TABLE `evento`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asistir_evento`
--
ALTER TABLE `asistir_evento`
  ADD CONSTRAINT `fk_asistir_evento` FOREIGN KEY (`nombre`) REFERENCES `usuario` (`nombre`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_asistir_evento2` FOREIGN KEY (`id`) REFERENCES `evento` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `evento`
--
ALTER TABLE `evento`
  ADD CONSTRAINT `fk_nombre_usuario` FOREIGN KEY (`nombre_usuario`) REFERENCES `usuario` (`nombre`) ON DELETE CASCADE;

--
-- Filtros para la tabla `seguidores`
--
ALTER TABLE `seguidores`
  ADD CONSTRAINT `fk_id_usuarioS` FOREIGN KEY (`nombre`) REFERENCES `usuario` (`nombre`),
  ADD CONSTRAINT `fk_id_usuario_seguidores` FOREIGN KEY (`nombre_seguidor`) REFERENCES `usuario` (`nombre`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
