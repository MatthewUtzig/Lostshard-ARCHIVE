-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: jenkins.lostshard.com    Database: Brandværdi
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
-- Table structure for table `ØvreOgNedreBrændVærdi`
--

DROP TABLE IF EXISTS `ØvreOgNedreBrændVærdi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ØvreOgNedreBrændVærdi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Materiale` varchar(45) NOT NULL,
  `ØvreBrandVærdi` int(11) NOT NULL,
  `NedreBrandVærdi` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Materiale_UNIQUE` (`Materiale`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ØvreOgNedreBrændVærdi`
--

LOCK TABLES `ØvreOgNedreBrændVærdi` WRITE;
/*!40000 ALTER TABLE `ØvreOgNedreBrændVærdi` DISABLE KEYS */;
INSERT INTO `ØvreOgNedreBrændVærdi` VALUES (1,'Hydrogen',141800,119960),(2,'Methane',55500,50000),(3,'Ethane',51900,47800),(4,'Propane',50350,46350),(5,'Butane',49500,45750),(6,'Pentane',48600,45350),(7,'Paraffin wax',46000,41500),(8,'Kerosene',46200,43000),(9,'Wood Fuel',24,43400),(10,'Diesel',44800,NULL),(11,'Coal (Anthracite)',32500,NULL),(12,'Coal (Lignite)',15000,NULL);
/*!40000 ALTER TABLE `ØvreOgNedreBrændVærdi` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:11:06
