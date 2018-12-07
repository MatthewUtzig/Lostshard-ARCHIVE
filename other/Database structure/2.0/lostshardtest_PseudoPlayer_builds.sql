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
-- Table structure for table `PseudoPlayer_builds`
--

DROP TABLE IF EXISTS `PseudoPlayer_builds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PseudoPlayer_builds` (
  `PseudoPlayer_id` int(11) NOT NULL,
  `archery_locked` bit(1) DEFAULT NULL,
  `archery_level` int(11) DEFAULT NULL,
  `blackSmithy_locked` bit(1) DEFAULT NULL,
  `blackSmithy_level` int(11) DEFAULT NULL,
  `blades_locked` bit(1) DEFAULT NULL,
  `blades_level` int(11) DEFAULT NULL,
  `brawling_locked` bit(1) DEFAULT NULL,
  `brawling_level` int(11) DEFAULT NULL,
  `fishing_locked` bit(1) DEFAULT NULL,
  `fishing_level` int(11) DEFAULT NULL,
  `lumberjacking_locked` bit(1) DEFAULT NULL,
  `lumberjacking_level` int(11) DEFAULT NULL,
  `magery_locked` bit(1) DEFAULT NULL,
  `magery_level` int(11) DEFAULT NULL,
  `mining_locked` bit(1) DEFAULT NULL,
  `mining_level` int(11) DEFAULT NULL,
  `survivalism_locked` bit(1) DEFAULT NULL,
  `survivalism_level` int(11) DEFAULT NULL,
  `doogs` int(11) NOT NULL,
  `mount` bit(1) NOT NULL,
  `taming_locked` bit(1) DEFAULT NULL,
  `taming_level` int(11) DEFAULT NULL,
  KEY `FKbsmvegqcjuq9nh8tgy3ij2d7h` (`PseudoPlayer_id`),
  CONSTRAINT `FKbsmvegqcjuq9nh8tgy3ij2d7h` FOREIGN KEY (`PseudoPlayer_id`) REFERENCES `PseudoPlayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PseudoPlayer_builds`
--

LOCK TABLES `PseudoPlayer_builds` WRITE;
/*!40000 ALTER TABLE `PseudoPlayer_builds` DISABLE KEYS */;
INSERT INTO `PseudoPlayer_builds` VALUES (2,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,0,'','\0',0),(2,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,0,'','\0',0),(3,'\0',0,'\0',0,'\0',82,'\0',1,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,0,'','\0',0),(1,'\0',1000,'\0',1000,'\0',1000,'\0',0,'\0',0,'\0',0,'\0',1000,'\0',0,'\0',1000,0,'','\0',0),(4,'\0',0,'\0',0,'\0',0,'\0',3,'\0',997,'\0',0,'\0',0,'\0',1000,'\0',1000,0,'','\0',1000),(5,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,'\0',0,0,'','\0',0);
/*!40000 ALTER TABLE `PseudoPlayer_builds` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:11:57
