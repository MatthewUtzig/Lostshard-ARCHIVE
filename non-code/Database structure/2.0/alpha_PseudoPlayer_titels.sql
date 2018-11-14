-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: jenkins.lostshard.com    Database: alpha
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
-- Table structure for table `PseudoPlayer_titels`
--

DROP TABLE IF EXISTS `PseudoPlayer_titels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PseudoPlayer_titels` (
  `PseudoPlayer_id` int(11) NOT NULL,
  `titels` varchar(255) DEFAULT NULL,
  KEY `FKcq36c5sg2mhw93pngrfpsyxtw` (`PseudoPlayer_id`),
  CONSTRAINT `FKcq36c5sg2mhw93pngrfpsyxtw` FOREIGN KEY (`PseudoPlayer_id`) REFERENCES `PseudoPlayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PseudoPlayer_titels`
--

LOCK TABLES `PseudoPlayer_titels` WRITE;
/*!40000 ALTER TABLE `PseudoPlayer_titels` DISABLE KEYS */;
INSERT INTO `PseudoPlayer_titels` VALUES (7,'j'),(7,'Best on Server'),(7,'Dragon Slayer'),(5,'Bastard'),(5,'hspriggs'),(4,'Bunt\'s Lover'),(48,'Piece of shit'),(48,'I like dick.'),(48,'Fist my ass'),(13,'34'),(13,'The King'),(13,'The King'),(16,'A'),(65,'Big Meme'),(2,'Admin'),(2,'null'),(2,'&5'),(2,'&5test'),(2,'----->'),(2,'-------------->'),(2,'???????????????'),(89,'Bastard'),(52,'DunkMaster'),(85,'Scoota'),(122,'Marvelous'),(21,'Alpha Finalist'),(20,'Alpha Champ');
/*!40000 ALTER TABLE `PseudoPlayer_titels` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:12:10
