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
-- Table structure for table `PseudoPlayer_ignored`
--

DROP TABLE IF EXISTS `PseudoPlayer_ignored`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PseudoPlayer_ignored` (
  `PseudoPlayer_id` int(11) NOT NULL,
  `ignored` varchar(255) DEFAULT NULL,
  KEY `FKa7ymfn98wvmr23yy9dlbdjy1s` (`PseudoPlayer_id`),
  CONSTRAINT `FKa7ymfn98wvmr23yy9dlbdjy1s` FOREIGN KEY (`PseudoPlayer_id`) REFERENCES `PseudoPlayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PseudoPlayer_ignored`
--

LOCK TABLES `PseudoPlayer_ignored` WRITE;
/*!40000 ALTER TABLE `PseudoPlayer_ignored` DISABLE KEYS */;
INSERT INTO `PseudoPlayer_ignored` VALUES (25,'43f8b162-0569-441d-a5a3-5d9a890476ee'),(85,'acb79d26-cb77-43ed-aee9-c0d754ee73b3'),(85,'acb79d26-cb77-43ed-aee9-c0d754ee73b3'),(85,'acb79d26-cb77-43ed-aee9-c0d754ee73b3'),(89,'48c98fea-3faa-48ab-85a4-bcf7e641521c'),(89,'48c98fea-3faa-48ab-85a4-bcf7e641521c'),(40,'ca088110-b9b6-4da7-989f-682cfd172caf'),(40,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(40,'ca088110-b9b6-4da7-989f-682cfd172caf'),(40,'ca088110-b9b6-4da7-989f-682cfd172caf'),(40,'ca088110-b9b6-4da7-989f-682cfd172caf'),(40,'ca088110-b9b6-4da7-989f-682cfd172caf'),(84,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(101,'ec93f186-0386-49bd-85f4-449d0be72ccd'),(101,'ec93f186-0386-49bd-85f4-449d0be72ccd'),(121,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(121,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(121,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(121,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(121,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(121,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(121,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(121,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(121,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(121,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(121,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(121,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1');
/*!40000 ALTER TABLE `PseudoPlayer_ignored` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:12:29
