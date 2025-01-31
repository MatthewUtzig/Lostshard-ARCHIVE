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
-- Table structure for table `Plot_friends`
--

DROP TABLE IF EXISTS `Plot_friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Plot_friends` (
  `Plot_id` int(11) NOT NULL,
  `friends` varchar(255) DEFAULT NULL,
  KEY `FKijifdyujlys8gkm4lu8xvtypv` (`Plot_id`),
  CONSTRAINT `FKijifdyujlys8gkm4lu8xvtypv` FOREIGN KEY (`Plot_id`) REFERENCES `Plot` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Plot_friends`
--

LOCK TABLES `Plot_friends` WRITE;
/*!40000 ALTER TABLE `Plot_friends` DISABLE KEYS */;
INSERT INTO `Plot_friends` VALUES (13,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(13,'29248316-6ebb-4776-859a-5c9142f1b693'),(13,'f681cb86-ddc5-4797-9f79-457e7326e9c6'),(13,'34955663-8b3c-4568-b72b-4314ef2b1abb'),(13,'12643edf-c163-41a5-b8cb-f97aa7322292'),(23,'34955663-8b3c-4568-b72b-4314ef2b1abb'),(23,'12643edf-c163-41a5-b8cb-f97aa7322292'),(23,'f681cb86-ddc5-4797-9f79-457e7326e9c6'),(55,'ab52bc00-2134-4263-9b6a-ec68a5004f3a'),(60,'34955663-8b3c-4568-b72b-4314ef2b1abb'),(24,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(24,'6338fc76-5726-4673-97c9-87014970bb88'),(24,'073c2d1f-cfb0-4bef-a2b5-eefad9e0d704'),(24,'81a75c9c-a4a6-4919-838b-c9d523836096'),(32,'e9c2d630-4635-49da-aef8-529b086b6f0a'),(32,'bf627ed5-9f74-483b-9154-0d79eec789c3'),(32,'6192a860-6e4b-4868-a449-5890f0958f47'),(32,'b4a82f69-aff9-4e6b-bba3-3bd541a4aa01'),(32,'c3918242-adb9-47d8-a508-d620a454cb69'),(32,'0d206224-30e2-4cdb-9e10-b079e524703e'),(40,'2c2f1e70-292e-4642-b03c-9133e21c13fc'),(57,'5fe875bd-7af7-4c19-b4bf-4bf023c69400'),(72,'fa3e0ffe-aaad-4ca9-a80f-0d707ae63a72'),(72,'d0358a79-e1ea-4505-8a93-fdd9349be31b'),(31,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(52,'097e6275-56e5-4fbc-aa5e-551637c2bda3'),(52,'0848cc3c-b3f3-4fbf-b4c6-d8e25a6c6e4e'),(84,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(67,'34955663-8b3c-4568-b72b-4314ef2b1abb'),(14,'6338fc76-5726-4673-97c9-87014970bb88'),(17,'6ee8361a-b40a-447f-b90a-81bd41295c99'),(17,'9a07961a-57dc-408d-be00-49139ad14fad'),(17,'c44259d1-9bc5-4e67-a45c-904da138696d'),(17,'099765e7-09c4-4344-b26e-47afb0060726'),(88,'96ee6c9c-ac23-44a6-9575-32496d699e1e'),(94,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(94,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(94,'1426d019-4609-4c60-be34-43ca43299144'),(4,'29248316-6ebb-4776-859a-5c9142f1b693'),(4,'12643edf-c163-41a5-b8cb-f97aa7322292'),(16,'1c02a7cd-c196-4a45-9e87-0bb41ecceb36'),(16,'f9e3dc1f-8248-4056-a3f5-d935d0536373'),(16,'ca088110-b9b6-4da7-989f-682cfd172caf'),(19,'f9e3dc1f-8248-4056-a3f5-d935d0536373'),(19,'2c2f1e70-292e-4642-b03c-9133e21c13fc'),(19,'099765e7-09c4-4344-b26e-47afb0060726'),(19,'1e513af9-352d-4fad-825f-dbde6a49a6fd'),(33,'a1607811-deb7-4b66-a88d-266dd3d1ff67'),(33,'de986d9c-6ae2-4cac-8444-33fa32733c59'),(34,'22c890f1-5d0e-44c9-89a5-1c4068949fb0'),(34,'b090946b-578b-4e5e-b7cd-1236ccb8cb6c'),(38,'099765e7-09c4-4344-b26e-47afb0060726'),(38,'87fbc806-ef38-4f3f-9ee6-36ebc7b64b48'),(38,'1c418320-7480-4029-b534-83f6924e0467'),(41,'099765e7-09c4-4344-b26e-47afb0060726'),(41,'b57ef14f-cc50-4846-8d39-7bbf7aafa250'),(41,'16b43956-fd3a-44f0-aebc-3d9ef741774d'),(44,'7dd02835-5b03-4035-b5a2-f309b1d8aacf'),(44,'e9c2d630-4635-49da-aef8-529b086b6f0a'),(44,'71717c57-53e1-4401-9a63-0a93cc3adaff'),(48,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(48,'2e033034-a8f8-406b-9dbf-c778a00020de'),(48,'1c418320-7480-4029-b534-83f6924e0467'),(48,'537f4ab0-f6e5-4cd6-8ad5-c9ed3efa1a1b'),(48,'3de3039a-a109-4fde-829d-3d7555ecd33f'),(48,'836c3c9e-97ba-4be3-8e52-717163c497db'),(49,'1426d019-4609-4c60-be34-43ca43299144'),(49,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(49,'3de3039a-a109-4fde-829d-3d7555ecd33f'),(50,'6be436ee-56b4-4023-a409-ec5dd626d24d'),(50,'09250fea-2937-4e83-8aae-7c30059b5a38'),(58,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(58,'f681cb86-ddc5-4797-9f79-457e7326e9c6'),(68,'dd2b8cd4-8145-4049-beac-6d6c8cc84ef6'),(96,'6ee8361a-b40a-447f-b90a-81bd41295c99'),(100,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(100,'82894576-3c31-4539-9a01-e9653d360f9c'),(100,'5a0371b1-9a29-4c98-bb7f-9e506f24a429'),(110,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(110,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(110,'1426d019-4609-4c60-be34-43ca43299144'),(110,'1c418320-7480-4029-b534-83f6924e0467'),(118,'1f14e4d2-914a-463a-ba1e-d3bbcd89e23d'),(118,'de986d9c-6ae2-4cac-8444-33fa32733c59'),(118,'a1607811-deb7-4b66-a88d-266dd3d1ff67'),(118,'b57ef14f-cc50-4846-8d39-7bbf7aafa250'),(118,'16b43956-fd3a-44f0-aebc-3d9ef741774d'),(121,'2c2f1e70-292e-4642-b03c-9133e21c13fc'),(123,'2c2f1e70-292e-4642-b03c-9133e21c13fc'),(123,'158e676e-7012-4be1-a648-795d5b5b0fc3'),(123,'ef29f6ec-db37-4fe1-8c1e-047bb9710516'),(123,'d574ada2-d21b-484b-a066-a0a235808d87'),(124,'3de3039a-a109-4fde-829d-3d7555ecd33f');
/*!40000 ALTER TABLE `Plot_friends` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:12:07
