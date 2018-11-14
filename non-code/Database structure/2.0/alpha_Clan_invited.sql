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
-- Table structure for table `Clan_invited`
--

DROP TABLE IF EXISTS `Clan_invited`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clan_invited` (
  `Clan_id` int(11) NOT NULL,
  `invited` varchar(255) DEFAULT NULL,
  KEY `FKmygasqweku7pvjvneuvj66a24` (`Clan_id`),
  CONSTRAINT `FKmygasqweku7pvjvneuvj66a24` FOREIGN KEY (`Clan_id`) REFERENCES `Clan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clan_invited`
--

LOCK TABLES `Clan_invited` WRITE;
/*!40000 ALTER TABLE `Clan_invited` DISABLE KEYS */;
INSERT INTO `Clan_invited` VALUES (9,'a2cbb0b3-d8e1-4ab1-81ce-f5a739ec156a'),(4,'de986d9c-6ae2-4cac-8444-33fa32733c59'),(14,'017cf580-7fc7-40a0-b0b4-1aabc4acd04a'),(1,'85b99109-67f8-4397-b25b-55b1167b6441'),(2,'6ee8361a-b40a-447f-b90a-81bd41295c99'),(5,'a3c74aef-425e-449c-8375-2de96b649426');
/*!40000 ALTER TABLE `Clan_invited` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:12:26
