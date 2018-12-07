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
-- Table structure for table `Plot_npcs`
--

DROP TABLE IF EXISTS `Plot_npcs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Plot_npcs` (
  `Plot_id` int(11) NOT NULL,
  `pitch` float NOT NULL,
  `world` varchar(255) DEFAULT NULL,
  `x` double NOT NULL,
  `y` double NOT NULL,
  `yaw` float NOT NULL,
  `z` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  KEY `FKerw51ulre9849eiblnm63jpjq` (`Plot_id`),
  KEY `FK8u6cj1cbl4f1ulx66tpo9mum3` (`store_id`),
  CONSTRAINT `FK8u6cj1cbl4f1ulx66tpo9mum3` FOREIGN KEY (`store_id`) REFERENCES `Store` (`id`),
  CONSTRAINT `FKerw51ulre9849eiblnm63jpjq` FOREIGN KEY (`Plot_id`) REFERENCES `Plot` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Plot_npcs`
--

LOCK TABLES `Plot_npcs` WRITE;
/*!40000 ALTER TABLE `Plot_npcs` DISABLE KEYS */;
INSERT INTO `Plot_npcs` VALUES (1,-1.35001,'world',-98.413863409863,59.0625,180.141,224.487450157223,'Roalf','BANKER',NULL,NULL),(1,-1.35001,'world',-101.717127667252,59.0625,180.141,224.300000011921,'Lucid','BANKER',NULL,NULL);
/*!40000 ALTER TABLE `Plot_npcs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:11:46
