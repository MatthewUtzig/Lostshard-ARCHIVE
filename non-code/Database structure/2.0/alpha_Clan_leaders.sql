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
-- Table structure for table `Clan_leaders`
--

DROP TABLE IF EXISTS `Clan_leaders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clan_leaders` (
  `Clan_id` int(11) NOT NULL,
  `leaders` varchar(255) DEFAULT NULL,
  KEY `FKhupsi02ielg0sfrl4j3hpkyxt` (`Clan_id`),
  CONSTRAINT `FKhupsi02ielg0sfrl4j3hpkyxt` FOREIGN KEY (`Clan_id`) REFERENCES `Clan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clan_leaders`
--

LOCK TABLES `Clan_leaders` WRITE;
/*!40000 ALTER TABLE `Clan_leaders` DISABLE KEYS */;
INSERT INTO `Clan_leaders` VALUES (3,'7dd02835-5b03-4035-b5a2-f309b1d8aacf'),(6,'d8b91fea-1a0d-44be-9de3-c08fbd337804'),(14,'073c2d1f-cfb0-4bef-a2b5-eefad9e0d704'),(14,'d0358a79-e1ea-4505-8a93-fdd9349be31b'),(17,'537f4ab0-f6e5-4cd6-8ad5-c9ed3efa1a1b'),(1,'29248316-6ebb-4776-859a-5c9142f1b693'),(1,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(5,'de986d9c-6ae2-4cac-8444-33fa32733c59'),(5,'48c98fea-3faa-48ab-85a4-bcf7e641521c'),(5,'1f14e4d2-914a-463a-ba1e-d3bbcd89e23d'),(7,'ab52bc00-2134-4263-9b6a-ec68a5004f3a'),(7,'c4025cab-7198-479d-95e1-d32d12ca8611'),(20,'7fb0117e-a5a2-4ce0-a714-2f26313e0637'),(20,'1e513af9-352d-4fad-825f-dbde6a49a6fd'),(21,'5a0371b1-9a29-4c98-bb7f-9e506f24a429');
/*!40000 ALTER TABLE `Clan_leaders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:13:16
