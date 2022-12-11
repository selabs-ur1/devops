-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le :  mar. 03 nov. 2020 à 18:32
-- Version du serveur :  8.0.19
-- Version de PHP :  7.3.11-0ubuntu0.19.10.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `tlc`
--
CREATE DATABASE IF NOT EXISTS `tlc` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `tlc`;

-- --------------------------------------------------------

--
-- Structure de la table `Choice`
--

CREATE TABLE `Choice` (
  `id` bigint NOT NULL,
  `endDate` datetime(6) DEFAULT NULL,
  `startDate` datetime(6) DEFAULT NULL,
  `pollID` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `choice_user`
--

CREATE TABLE `choice_user` (
  `choice_id` bigint NOT NULL,
  `user_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `Comment`
--

CREATE TABLE `Comment` (
  `id` bigint NOT NULL,
  `auteur` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `pollID` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(15),
(15),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1),
(1);
 

-- --------------------------------------------------------

--
-- Structure de la table `MealPreference`
--

CREATE TABLE `MealPreference` (
  `id` bigint NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `pollID` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `Poll`
--

CREATE TABLE `Poll` (
  `id` bigint NOT NULL,
  `clos` bit(1) NOT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `has_meal` bit(1) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `padURL` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `slugAdmin` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `tlkURL` varchar(255) DEFAULT NULL,
  `updatedAt` datetime(6) DEFAULT NULL,
  `selectedChoice_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `User`
--

CREATE TABLE `User` (
  `id` bigint NOT NULL,
  `icsurl` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `Choice`
--
ALTER TABLE `Choice`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9pb9a172pl46be48ythes94cq` (`pollID`);

--
-- Index pour la table `choice_user`
--
ALTER TABLE `choice_user`
  ADD KEY `FK2m8oie88bmgxt3sm87i1mn1ao` (`user_id`),
  ADD KEY `FK9s1mrftmuef6lcexnlh89qgdn` (`choice_id`);

--
-- Index pour la table `Comment`
--
ALTER TABLE `Comment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKat24ob33uby14ubbye7ntggxs` (`pollID`);

--
-- Index pour la table `MealPreference`
--
ALTER TABLE `MealPreference`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6i9viog154nbsy8q81tsn73l5` (`user_id`),
  ADD KEY `FKowvfxet4tq8yhwj0j07lkskli` (`pollID`);

--
-- Index pour la table `Poll`
--
ALTER TABLE `Poll`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKo3gvyilei6ae6n4o2k2xgbfar` (`selectedChoice_id`);

--
-- Index pour la table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `Choice`
--
ALTER TABLE `Choice`
  ADD CONSTRAINT `FK9pb9a172pl46be48ythes94cq` FOREIGN KEY (`pollID`) REFERENCES `Poll` (`id`);

--
-- Contraintes pour la table `choice_user`
--
ALTER TABLE `choice_user`
  ADD CONSTRAINT `FK2m8oie88bmgxt3sm87i1mn1ao` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `FK9s1mrftmuef6lcexnlh89qgdn` FOREIGN KEY (`choice_id`) REFERENCES `Choice` (`id`);

--
-- Contraintes pour la table `Comment`
--
ALTER TABLE `Comment`
  ADD CONSTRAINT `FKat24ob33uby14ubbye7ntggxs` FOREIGN KEY (`pollID`) REFERENCES `Poll` (`id`);

--
-- Contraintes pour la table `MealPreference`
--
ALTER TABLE `MealPreference`
  ADD CONSTRAINT `FK6i9viog154nbsy8q81tsn73l5` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `FKowvfxet4tq8yhwj0j07lkskli` FOREIGN KEY (`pollID`) REFERENCES `Poll` (`id`);

--
-- Contraintes pour la table `Poll`
--
ALTER TABLE `Poll`
  ADD CONSTRAINT `FKo3gvyilei6ae6n4o2k2xgbfar` FOREIGN KEY (`selectedChoice_id`) REFERENCES `Choice` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
