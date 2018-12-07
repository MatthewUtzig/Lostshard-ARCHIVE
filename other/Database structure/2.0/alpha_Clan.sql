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
-- Table structure for table `Clan`
--

DROP TABLE IF EXISTS `Clan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clan` (
  `id` int(11) NOT NULL,
  `clan_name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clan`
--

LOCK TABLES `Clan` WRITE;
/*!40000 ALTER TABLE `Clan` DISABLE KEYS */;
INSERT INTO `Clan` VALUES (1,'Dignitaries','521d627d-9016-49b3-baa3-d07e73becaed'),(2,'blimplipnigger','e23a39c4-93a1-483d-b6e3-a1d37aa4721c'),(3,'The Brotherhood','d0358a79-e1ea-4505-8a93-fdd9349be31b'),(4,'Mystic','23606409-a47c-47dc-9949-5be341789238'),(5,'Aeres','a1607811-deb7-4b66-a88d-266dd3d1ff67'),(6,'Eureka','6338fc76-5726-4673-97c9-87014970bb88'),(7,'The Lostshardians','ec93f186-0386-49bd-85f4-449d0be72ccd'),(8,'Shady People','2e033034-a8f8-406b-9dbf-c778a00020de'),(9,'Gentlemen\'s Pub','b48ba110-7bfc-4138-8f76-27facf69f236'),(10,'Zietius is hacking','82be65e6-e2bf-4698-bfc5-447a09e804bb'),(11,'Rebels','87115edf-7704-4bdf-90f1-5b1a8f3d781a'),(12,'null','63ff8bd5-9b48-4856-bb2f-ce547adb6ea4'),(13,'Illuminati','db9ebf2d-db0c-4fe2-ab71-60a23d1ea2a9'),(14,'ISIS','81a75c9c-a4a6-4919-838b-c9d523836096'),(15,'Team Duwa','0d206224-30e2-4cdb-9e10-b079e524703e'),(16,'smithyhasabigdick','0d206224-30e2-4cdb-9e10-b079e524703e'),(17,'X-Men','1c418320-7480-4029-b534-83f6924e0467'),(18,'TheRushers','7509d86d-5b9f-4dbc-a304-cfb57b6ddb4d'),(19,'cunt','0d206224-30e2-4cdb-9e10-b079e524703e'),(20,'SandNiggers','bdf941b2-1ab0-4583-a8cc-de5d5dd657ae'),(21,'Autism','0d206224-30e2-4cdb-9e10-b079e524703e'),(22,'Aeres*','ec93f186-0386-49bd-85f4-449d0be72ccd'),(23,'The LostShardians','ec93f186-0386-49bd-85f4-449d0be72ccd');
/*!40000 ALTER TABLE `Clan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:13:14
