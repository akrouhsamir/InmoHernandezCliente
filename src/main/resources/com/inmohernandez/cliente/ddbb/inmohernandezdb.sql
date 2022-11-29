-- Adminer 4.8.1 MySQL 5.5.5-10.9.3-MariaDB-1:10.9.3+maria~ubu2204 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

CREATE DATABASE inmohernandezdb;

USE inmohernandezdb;

DROP TABLE IF EXISTS `alquileres`;
CREATE TABLE `alquileres` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `mensualidad` float NOT NULL,
  `id_inmueble` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_inmueble` (`id_inmueble`),
  CONSTRAINT `alquileres_ibfk_1` FOREIGN KEY (`id_inmueble`) REFERENCES `inmuebles` (`id_inmueble`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `alquileres` (`id`, `cliente`, `fecha_inicio`, `fecha_fin`, `mensualidad`, `id_inmueble`) VALUES
(1,	'Samir Akrouh',	'2021-01-01',	'2022-01-01',	550.5,	1),
(3,	'Luciano Agustin',	'2022-11-18',	'2022-11-18',	1200.45,	1),
(4,	'Marcos Arroyo Rivas',	'2022-07-04',	'2022-11-19',	190.4,	1),
(6,	'erfe',	'2022-11-01',	'2022-11-18',	0,	3),
(7,	'erfe',	'2022-11-01',	'2022-11-18',	0,	3),
(10,	'Rachel',	'2022-08-29',	'2024-11-16',	345,	34),
(11,	'Cristiano Ronaldo',	'2022-11-09',	'2022-11-27',	567,	1),
(12,	'Samir Akrouh',	'2020-07-08',	'2021-10-06',	560.45,	36),
(13,	'Fran Delegdo',	'2020-06-08',	'2020-06-08',	789,	36),
(15,	'Mianne Lepourt',	'2022-06-20',	'2022-11-23',	450.43,	1),
(17,	'Fernando Iglesisas',	'2022-11-02',	'2022-11-30',	2443,	6);

DROP TABLE IF EXISTS `inmuebles`;
CREATE TABLE `inmuebles` (
  `id_inmueble` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `precio` float NOT NULL,
  `descripcion` varchar(3000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `metros_construidos` int(11) NOT NULL,
  `metros_utiles` int(11) DEFAULT NULL,
  `ubicacion` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `zona` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_publicacion` date NOT NULL,
  `habitaciones` int(11) NOT NULL,
  `bannos` int(11) NOT NULL,
  PRIMARY KEY (`id_inmueble`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `inmuebles` (`id_inmueble`, `titulo`, `precio`, `descripcion`, `metros_construidos`, `metros_utiles`, `ubicacion`, `zona`, `fecha_publicacion`, `habitaciones`, `bannos`) VALUES
(1,	'Dúplex El Ejido Sur',	125000,	'Se vende vivienda unifamiliar. ',	200,	125,	'Calle Lobero nº10 3ºb',	'Ejido Sur',	'2022-10-04',	1,	1),
(2,	'Piso Bulevar',	89000,	'ref. ',	90,	85,	'Calle Danise nº14 1ºb',	'Ejido Centro',	'2022-10-04',	3,	2),
(3,	'Piso Ejido Centro',	39000,	'null',	92,	85,	'Calle Danise nº15 1ºb',	'Ejido Centro',	'2022-10-04',	1,	1),
(4,	'Piso pablo',	50000,	'',	100,	80,	'',	'Almerimar',	'2022-11-08',	3,	2),
(5,	'thegerg',	20000,	'',	120,	120,	'',	'',	'2022-11-14',	2,	2),
(6,	'Piso Samir',	20000,	'',	120,	120,	'',	'',	'2022-11-16',	1,	1),
(7,	'Piso en Torre Laguna',	76000,	'Piso con buenas vistas',	90,	82,	'calle trocolo nº 10',	'Ejido Centro',	'2022-11-04',	4,	2),
(8,	'Chabola de Marcos',	11900,	'',	300,	260,	'cerca del toro',	'Pampanico',	'2022-11-03',	1,	1),
(9,	'Casa de dos pisos',	88000,	'sr',	200,	180,	'',	'Balerma',	'2022-11-09',	4,	3),
(10,	'Casa de selena',	200500,	'ewf',	230,	200,	'323 juen',	'Guardias Viejas',	'2022-11-23',	5,	4),
(11,	'Casa Marcos Playa',	30000,	'muy boita',	120,	119,	'peza',	'Almerimar',	'2022-11-29',	4,	3),
(12,	'Casa vieja',	42342,	'',	342,	423,	'4242',	'',	'2022-11-15',	1,	1),
(13,	'tgvet',	1,	'4',	4,	4,	'e',	'Balerma',	'2022-11-01',	2,	2),
(14,	'La casa de papel',	24000,	'vfeev',	213,	122,	'ed',	'Ejido Centro',	'2022-11-16',	1,	3),
(15,	'Casa de Nico',	34090,	'Buen piso',	100,	90,	'calle liberol n4 04700',	'Ejido Centro',	'2022-09-06',	3,	2),
(16,	'erfrtgr',	343,	'',	34,	43,	'efvrtvgr',	'Balerma',	'2022-11-15',	2,	1),
(17,	'La casa de gerardo',	1444,	'',	12,	12,	'',	'Las Norias',	'2022-11-15',	3,	2),
(19,	'hola',	2,	'',	2,	2,	'',	'Ejido Sur',	'2022-11-21',	2,	1),
(21,	'Casas de mosquete',	1,	'1',	1,	1,	'1',	'Balerma',	'2022-11-01',	1,	1),
(22,	'Atico Juanjo',	89000,	'ggbfgyhyhehy',	434,	344,	'calle sed',	'',	'2022-11-12',	4,	3),
(23,	'Atico de Danir',	58834.9,	'',	43,	43,	'43',	'',	'2022-11-14',	3,	2),
(24,	'Atico de isidoro',	79955,	'gbsbhhryntdyhte tryhryt',	54,	4,	'4',	'',	'2022-11-01',	7,	1),
(32,	'Mansion Juan',	1250000,	'Muy espaciosa con jardin y cancha para jugar al futbol.',	350,	327,	'calle pedro ponze',	'Ejido Sur',	'2022-11-08',	1,	1),
(33,	'Casa de skrillex',	21000,	'gbbfgn',	78,	67,	'4fdfd',	'Guardias Viejas',	'2022-11-30',	3,	2),
(34,	'Casa de Denis',	74343,	'iyufdkuyfufuiyfuyfdukyfkuydf',	443,	237,	'vdfbd',	'Matagorda',	'2022-02-14',	1,	1),
(36,	'Casa de Dionisio',	230100,	'Vistas al mar',	299,	200,	'calle kilowert n19',	'Balerma',	'2022-05-03',	1,	1),
(37,	'Mansion de David ',	590000,	'Gaupoa',	400,	360,	'calle calle calle',	'Ejido Sur',	'2017-12-22',	5,	3),
(38,	'Casa de Noha',	3900500,	'Casa de la princ',	500,	480,	'calle apuesta n60',	'Almerimar',	'2022-02-14',	7,	4),
(39,	'Casa de Mosquete',	57685,	'ergerbtbrtbetbrtbnrthyjsrtjsrtjdtrtyjryjtey ytjsr th',	232,	200,	'geetrthrt',	'Matagorda',	'2022-01-27',	4,	3);

-- 2022-11-29 11:10:31