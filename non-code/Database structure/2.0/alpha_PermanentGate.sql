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
-- Table structure for table `PermanentGate`
--

DROP TABLE IF EXISTS `PermanentGate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PermanentGate` (
  `id` int(11) NOT NULL,
  `creatorUUID` varchar(255) DEFAULT NULL,
  `direction` bit(1) NOT NULL,
  `from_pitch` float DEFAULT NULL,
  `from_world` varchar(255) DEFAULT NULL,
  `from_x` double DEFAULT NULL,
  `from_y` double DEFAULT NULL,
  `from_yaw` float DEFAULT NULL,
  `from_z` double DEFAULT NULL,
  `to_pitch` float DEFAULT NULL,
  `to_world` varchar(255) DEFAULT NULL,
  `to_x` double DEFAULT NULL,
  `to_y` double DEFAULT NULL,
  `to_yaw` float DEFAULT NULL,
  `to_z` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PermanentGate`
--

LOCK TABLES `PermanentGate` WRITE;
/*!40000 ALTER TABLE `PermanentGate` DISABLE KEYS */;
INSERT INTO `PermanentGate` VALUES (2,'85b99109-67f8-4397-b25b-55b1167b6441','\0',0,'world',-393,73,0,93,0,'world_nether',-50,44,0,-83),(3,'85b99109-67f8-4397-b25b-55b1167b6441','\0',0,'world',-100,87,0,246,0,'world_the_end',35,61,0,16),(8,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world',-34,70,0,226,0,'world',-427,113,0,-122),(10,'521d627d-9016-49b3-baa3-d07e73becaed','\0',0,'world',195,55,0,419,0,'world',1700,81,0,901),(13,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world',-430,113,0,-124,0,'world',67,166,0,288),(14,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world',-429,113,0,-120,0,'world',-777,56,0,-2333),(16,'386f791e-9ddb-4f19-a089-989315c83e62','\0',0,'world',36,62,0,214,0,'world',714,15,0,0),(18,'d0358a79-e1ea-4505-8a93-fdd9349be31b','',0,'world',-871,44,0,2109,0,'world',-875,40,0,2097),(25,'6338fc76-5726-4673-97c9-87014970bb88','',0,'world',48,69,0,280,0,'world',-1209,71,0,207),(27,'6338fc76-5726-4673-97c9-87014970bb88','',0,'world',-365,83,0,107,0,'world',-1211,71,0,207),(28,'fa3e0ffe-aaad-4ca9-a80f-0d707ae63a72','\0',0,'world',203,81,0,114,0,'world',169,61,0,220),(30,'fa3e0ffe-aaad-4ca9-a80f-0d707ae63a72','\0',0,'world',193,81,0,119,0,'world',240,60,0,52),(32,'fa3e0ffe-aaad-4ca9-a80f-0d707ae63a72','\0',0,'world',191,81,0,119,0,'world',-830,62,0,280),(36,'d0358a79-e1ea-4505-8a93-fdd9349be31b','',0,'world',-856,44,0,2120,0,'world',-889,41,0,2111),(38,'7fb0117e-a5a2-4ce0-a714-2f26313e0637','',0,'world',-34,68,0,149,0,'world',-3,19,0,283),(41,'d0358a79-e1ea-4505-8a93-fdd9349be31b','\0',0,'world',-875,44,0,2106,0,'world',-884,40,0,2086),(44,'d0358a79-e1ea-4505-8a93-fdd9349be31b','\0',0,'world',-881,44,0,2099,0,'world',-880,40,0,2109),(45,'d0358a79-e1ea-4505-8a93-fdd9349be31b','\0',0,'world',-875,44,0,2104,0,'world',-875,48,0,2093),(46,'d0358a79-e1ea-4505-8a93-fdd9349be31b','\0',0,'world',-870,44,0,2108,0,'world',-859,40,0,2109),(48,'3db2478d-7395-4df0-87c2-04e62dc1dd10','\0',0,'world_nether',-83,54,0,-140,0,'world_nether',-118,70,0,-164),(49,'d0358a79-e1ea-4505-8a93-fdd9349be31b','',0,'world',-889,41,0,2125,0,'world',-888,48,0,2085),(51,'d0358a79-e1ea-4505-8a93-fdd9349be31b','',0,'world',-874,44,0,2109,0,'world',-886,44,0,2079),(52,'d0358a79-e1ea-4505-8a93-fdd9349be31b','\0',0,'world',-870,44,0,2104,0,'world',-873,48,0,2093),(55,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0','',0,'world',-1734,57,0,293,0,'world',-1840,25,0,50),(59,'3db2478d-7395-4df0-87c2-04e62dc1dd10','\0',0,'world',-446,118,0,-201,0,'world_nether',-98,67,0,-149),(61,'073c2d1f-cfb0-4bef-a2b5-eefad9e0d704','',0,'world',-75,71,0,174,0,'world',2318,65,0,-1462),(68,'c98cda82-2b52-42fb-8c38-707fef11088b','\0',0,'world',369,74,0,271,0,'world',377,134,0,281),(71,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world',-328,125,0,115,0,'world',-608,64,0,-463),(73,'c44259d1-9bc5-4e67-a45c-904da138696d','',0,'world',-1932,62,0,298,0,'world',-1965,66,0,302),(91,'d8b91fea-1a0d-44be-9de3-c08fbd337804','',0,'world',-1239,68,0,348,0,'world',-1262,95,0,326),(93,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world_nether',-50,44,0,-81,0,'world',83,71,0,291),(95,'34955663-8b3c-4568-b72b-4314ef2b1abb','',0,'world',133,64,0,390,0,'world',134,61,0,388),(97,'ca088110-b9b6-4da7-989f-682cfd172caf','',0,'world',-1723,37,0,1773,0,'world',-628,21,0,341),(99,'acb79d26-cb77-43ed-aee9-c0d754ee73b3','\0',0,'world',-1049,207,0,695,0,'world',-1069,202,0,713),(100,'acb79d26-cb77-43ed-aee9-c0d754ee73b3','',0,'world',-1058,202,0,703,0,'world',-1005,202,0,746),(102,'1e513af9-352d-4fad-825f-dbde6a49a6fd','\0',0,'world',-1725,37,0,1773,0,'world',-22,63,0,274),(104,'521d627d-9016-49b3-baa3-d07e73becaed','',0,'world',192,55,0,416,0,'world',133,60,0,396),(107,'0d206224-30e2-4cdb-9e10-b079e524703e','\0',0,'world',9,52,0,494,0,'world',-78,63,0,274),(109,'521d627d-9016-49b3-baa3-d07e73becaed','',0,'world',188,55,0,423,0,'world',-90,12,0,140),(110,'0d206224-30e2-4cdb-9e10-b079e524703e','\0',0,'world',13,52,0,494,0,'world_the_end',93,58,0,28),(112,'6338fc76-5726-4673-97c9-87014970bb88','',0,'world',-368,38,0,94,0,'world',-361,58,0,92),(113,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world_nether',410,135,0,-288,0,'world_nether',422,135,0,-288),(115,'6338fc76-5726-4673-97c9-87014970bb88','\0',0,'world',55,71,0,300,0,'world',1869,81,0,2021),(116,'acb79d26-cb77-43ed-aee9-c0d754ee73b3','\0',0,'world',66,71,0,354,0,'world',-1060,202,0,703),(122,'0acb9e31-ad47-49e7-855c-de37cedb1e5b','\0',0,'world',-1480,8,0,120,0,'world',342,68,0,367),(124,'b090946b-578b-4e5e-b7cd-1236ccb8cb6c','\0',0,'world',-678,72,0,133,0,'world',-684,91,0,143),(125,'34955663-8b3c-4568-b72b-4314ef2b1abb','\0',0,'world',2321,59,0,-2141,0,'world',134,60,0,398),(126,'521d627d-9016-49b3-baa3-d07e73becaed','',0,'world',189,55,0,416,0,'world',2321,59,0,-2135),(127,'3de3039a-a109-4fde-829d-3d7555ecd33f','',0,'world',-1459,14,0,134,0,'world',16,52,0,494),(128,'34955663-8b3c-4568-b72b-4314ef2b1abb','\0',0,'world',2321,59,0,-2129,0,'world',1021,252,0,-38),(129,'34955663-8b3c-4568-b72b-4314ef2b1abb','\0',0,'world',2321,59,0,-2131,0,'world',-17,251,0,1023);
/*!40000 ALTER TABLE `PermanentGate` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:13:42
