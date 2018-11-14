-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: jenkins.lostshard.com    Database: lostshardtest
-- ------------------------------------------------------
-- Server version	5.1.73

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Plot`
--

DROP TABLE IF EXISTS `Plot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Plot` (
  `id` int(11) NOT NULL,
  `allowExplosions` bit(1) NOT NULL,
  `allowMagic` bit(1) NOT NULL,
  `allowPvp` bit(1) NOT NULL,
  `capturepoint` bit(1) NOT NULL,
  `friendBuild` bit(1) NOT NULL,
  `money` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `privatePlot` bit(1) NOT NULL,
  `protected` bit(1) NOT NULL,
  `salePrice` int(11) NOT NULL,
  `pitch` float NOT NULL,
  `world` varchar(255) DEFAULT NULL,
  `x` double NOT NULL,
  `y` double NOT NULL,
  `yaw` float NOT NULL,
  `z` double NOT NULL,
  `size` int(11) NOT NULL,
  `titleEntrence` bit(1) NOT NULL,
  `lastCaptureDate` bigint(20) NOT NULL,
  `owningClan_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ia5a5n2pbbo9uko0ymg2w6iug` (`name`),
  KEY `FKlcilfy5dg1l711qyt8mjrlrkc` (`owningClan_id`),
  CONSTRAINT `FKlcilfy5dg1l711qyt8mjrlrkc` FOREIGN KEY (`owningClan_id`) REFERENCES `Clan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Plot`
--

LOCK TABLES `Plot` WRITE;
/*!40000 ALTER TABLE `Plot` DISABLE KEYS */;
INSERT INTO `Plot` VALUES (1,'\0','','','','\0',999995480,'Buccaneer\'s Cove','85b99109-67f8-4397-b25b-55b1167b6441','\0','',0,0,'world',-109,58,0,212,85,'',0,NULL);
/*!40000 ALTER TABLE `Plot` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:11:11
