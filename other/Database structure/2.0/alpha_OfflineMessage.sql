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
-- Table structure for table `OfflineMessage`
--

DROP TABLE IF EXISTS `OfflineMessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OfflineMessage` (
  `id` int(11) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `player` varchar(255) DEFAULT NULL,
  `seen` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OfflineMessage`
--

LOCK TABLES `OfflineMessage` WRITE;
/*!40000 ALTER TABLE `OfflineMessage` DISABLE KEYS */;
INSERT INTO `OfflineMessage` VALUES (6,'Order of Balance have failed to pay tax and have shrunk.','23606409-a47c-47dc-9949-5be341789238','\0'),(10,'Paradise  have failed to pay tax and have shrunk.','010a8c8e-651e-4a70-b931-5a411af66fbf','\0'),(12,'Order of Balance have failed to pay tax and have shrunk.','23606409-a47c-47dc-9949-5be341789238','\0'),(15,'Paradise  have failed to pay tax and have shrunk.','010a8c8e-651e-4a70-b931-5a411af66fbf','\0'),(19,'WolfPack  have failed to pay tax and have shrunk.','6cb5c2a5-5dbd-45e8-a6d4-752b17b6549e','\0'),(20,'Order of Balance have failed to pay tax and have shrunk.','23606409-a47c-47dc-9949-5be341789238','\0'),(21,'. have failed to pay tax and have shrunk.','f681cb86-ddc5-4797-9f79-457e7326e9c6','\0'),(22,'Melonig  have failed to pay tax and have shrunk.','ca088110-b9b6-4da7-989f-682cfd172caf','\0'),(23,'spidernigger  have failed to pay tax and have shrunk.','1f14e4d2-914a-463a-ba1e-d3bbcd89e23d','\0'),(24,'Nesme  have failed to pay tax and have shrunk.','bb1493bb-70c2-496c-b1b4-bb6146f53919','\0'),(26,'Paradise  have failed to pay tax and have shrunk.','010a8c8e-651e-4a70-b931-5a411af66fbf','\0'),(27,'TheDansishSlayers  have failed to pay tax and have shrunk.','41d76dcb-d8f4-40df-bed0-f454d1cfaded','\0'),(28,'Dep\'s outpost  have failed to pay tax and have shrunk.','7509d86d-5b9f-4dbc-a304-cfb57b6ddb4d','\0'),(30,'Mushrom  have failed to pay tax and have shrunk.','1f14e4d2-914a-463a-ba1e-d3bbcd89e23d','\0'),(31,'Rushers Town  have failed to pay tax and have shrunk.','7509d86d-5b9f-4dbc-a304-cfb57b6ddb4d','\0'),(32,'WolfPack  have failed to pay tax and have shrunk.','6cb5c2a5-5dbd-45e8-a6d4-752b17b6549e','\0'),(33,'.  have failed to pay tax and have shrunk.','f681cb86-ddc5-4797-9f79-457e7326e9c6','\0');
/*!40000 ALTER TABLE `OfflineMessage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:12:13
