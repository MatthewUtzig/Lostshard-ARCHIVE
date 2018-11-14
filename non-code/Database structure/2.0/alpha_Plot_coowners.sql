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
-- Table structure for table `Plot_coowners`
--

DROP TABLE IF EXISTS `Plot_coowners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Plot_coowners` (
  `Plot_id` int(11) NOT NULL,
  `coowners` varchar(255) DEFAULT NULL,
  KEY `FKtjrbcxrx0e5yocwme7ba6qw5n` (`Plot_id`),
  CONSTRAINT `FKtjrbcxrx0e5yocwme7ba6qw5n` FOREIGN KEY (`Plot_id`) REFERENCES `Plot` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Plot_coowners`
--

LOCK TABLES `Plot_coowners` WRITE;
/*!40000 ALTER TABLE `Plot_coowners` DISABLE KEYS */;
INSERT INTO `Plot_coowners` VALUES (5,'d8b91fea-1a0d-44be-9de3-c08fbd337804'),(5,'85b99109-67f8-4397-b25b-55b1167b6441'),(3,'29671b38-c060-495e-9641-9071eb81e00b'),(14,'d8b91fea-1a0d-44be-9de3-c08fbd337804'),(15,'6338fc76-5726-4673-97c9-87014970bb88'),(32,'d0358a79-e1ea-4505-8a93-fdd9349be31b'),(32,'7dd02835-5b03-4035-b5a2-f309b1d8aacf'),(32,'0848cc3c-b3f3-4fbf-b4c6-d8e25a6c6e4e'),(32,'03a43adb-a582-4df2-8ab0-cbfe71e9a4a6'),(35,'7fb0117e-a5a2-4ce0-a714-2f26313e0637'),(35,'386f791e-9ddb-4f19-a089-989315c83e62'),(1,'d8b91fea-1a0d-44be-9de3-c08fbd337804'),(1,'521d627d-9016-49b3-baa3-d07e73becaed'),(1,'6338fc76-5726-4673-97c9-87014970bb88'),(2,'521d627d-9016-49b3-baa3-d07e73becaed'),(2,'6338fc76-5726-4673-97c9-87014970bb88'),(13,'691145b3-a10c-465d-975a-8fddd2362e65'),(13,'f72e1ed1-6b3a-40c0-aa21-beddd3881e81'),(21,'fa3e0ffe-aaad-4ca9-a80f-0d707ae63a72'),(21,'054e3e22-6775-4b6c-a3fc-defe4bcfcbae'),(23,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(23,'691145b3-a10c-465d-975a-8fddd2362e65'),(23,'f72e1ed1-6b3a-40c0-aa21-beddd3881e81'),(39,'5dadb5df-211d-437c-aee0-63efd9f0ff0c'),(39,'189ed923-78d8-4bd0-a4cd-b7a95f0cd2d6'),(39,'a2cbb0b3-d8e1-4ab1-81ce-f5a739ec156a'),(39,'609d12d3-6728-435d-9c61-e7c8451bbe52'),(39,'8a6d5662-0770-4d9e-a9de-2d0ee3ec6f0f'),(39,'19ca7cd2-d838-4048-98e0-8ae39312df08'),(24,'23606409-a47c-47dc-9949-5be341789238'),(24,'85b99109-67f8-4397-b25b-55b1167b6441'),(44,'bf627ed5-9f74-483b-9154-0d79eec789c3'),(44,'03a43adb-a582-4df2-8ab0-cbfe71e9a4a6'),(44,'c3918242-adb9-47d8-a508-d620a454cb69'),(44,'0848cc3c-b3f3-4fbf-b4c6-d8e25a6c6e4e'),(44,'097e6275-56e5-4fbc-aa5e-551637c2bda3'),(60,'521d627d-9016-49b3-baa3-d07e73becaed'),(70,'521d627d-9016-49b3-baa3-d07e73becaed'),(71,'81a75c9c-a4a6-4919-838b-c9d523836096'),(72,'073c2d1f-cfb0-4bef-a2b5-eefad9e0d704'),(48,'1426d019-4609-4c60-be34-43ca43299144'),(48,'09250fea-2937-4e83-8aae-7c30059b5a38'),(52,'7dd02835-5b03-4035-b5a2-f309b1d8aacf'),(52,'13f92d1c-487a-4bff-8801-ac108466a10f'),(52,'b4a82f69-aff9-4e6b-bba3-3bd541a4aa01'),(52,'ee992a91-f6f2-401c-96d4-f77fa50393ce'),(68,'3de3039a-a109-4fde-829d-3d7555ecd33f'),(68,'2a86b344-e122-4981-a75b-4c6e1710829c'),(78,'521d627d-9016-49b3-baa3-d07e73becaed'),(84,'537f4ab0-f6e5-4cd6-8ad5-c9ed3efa1a1b'),(84,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(41,'a1607811-deb7-4b66-a88d-266dd3d1ff67'),(41,'de986d9c-6ae2-4cac-8444-33fa32733c59'),(41,'4353931e-dc4c-4be8-9469-4346a84cc982'),(41,'1f14e4d2-914a-463a-ba1e-d3bbcd89e23d'),(80,'6338fc76-5726-4673-97c9-87014970bb88'),(80,'a1607811-deb7-4b66-a88d-266dd3d1ff67'),(80,'b57ef14f-cc50-4846-8d39-7bbf7aafa250'),(94,'1c418320-7480-4029-b534-83f6924e0467'),(96,'7710d3b3-b4e6-4bac-8cf6-2baa9f8ddbb1'),(96,'e23a39c4-93a1-483d-b6e3-a1d37aa4721c'),(96,'d3241a40-e2ab-4e13-84a6-9cdb1054e88d'),(17,'d3241a40-e2ab-4e13-84a6-9cdb1054e88d'),(17,'acb79d26-cb77-43ed-aee9-c0d754ee73b3'),(17,'498dd6f3-89f6-41d3-83f8-a5c7c8027233'),(17,'7710d3b3-b4e6-4bac-8cf6-2baa9f8ddbb1'),(45,'7fb0117e-a5a2-4ce0-a714-2f26313e0637'),(45,'bdf941b2-1ab0-4583-a8cc-de5d5dd657ae'),(76,'7fb0117e-a5a2-4ce0-a714-2f26313e0637'),(76,'ca088110-b9b6-4da7-989f-682cfd172caf'),(88,'ec93f186-0386-49bd-85f4-449d0be72ccd'),(88,'c4025cab-7198-479d-95e1-d32d12ca8611'),(97,'36eb8226-4e22-4a66-a16f-c4503b443c5a'),(97,'746fe4b3-0561-4e12-8a5b-76c273fc0fa6'),(4,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(4,'691145b3-a10c-465d-975a-8fddd2362e65'),(4,'f72e1ed1-6b3a-40c0-aa21-beddd3881e81'),(4,'34955663-8b3c-4568-b72b-4314ef2b1abb'),(4,'f681cb86-ddc5-4797-9f79-457e7326e9c6'),(16,'1e513af9-352d-4fad-825f-dbde6a49a6fd'),(16,'7fb0117e-a5a2-4ce0-a714-2f26313e0637'),(16,'386f791e-9ddb-4f19-a089-989315c83e62'),(19,'ca088110-b9b6-4da7-989f-682cfd172caf'),(19,'386f791e-9ddb-4f19-a089-989315c83e62'),(19,'bdf941b2-1ab0-4583-a8cc-de5d5dd657ae'),(30,'76c07ac5-f299-4b50-9505-be9f775f5fcb'),(33,'4353931e-dc4c-4be8-9469-4346a84cc982'),(33,'16b43956-fd3a-44f0-aebc-3d9ef741774d'),(34,'ab52bc00-2134-4263-9b6a-ec68a5004f3a'),(34,'96ee6c9c-ac23-44a6-9575-32496d699e1e'),(34,'c4025cab-7198-479d-95e1-d32d12ca8611'),(34,'43f8b162-0569-441d-a5a3-5d9a890476ee'),(38,'1f14e4d2-914a-463a-ba1e-d3bbcd89e23d'),(38,'48c98fea-3faa-48ab-85a4-bcf7e641521c'),(38,'de986d9c-6ae2-4cac-8444-33fa32733c59'),(38,'4353931e-dc4c-4be8-9469-4346a84cc982'),(38,'b57ef14f-cc50-4846-8d39-7bbf7aafa250'),(38,'16b43956-fd3a-44f0-aebc-3d9ef741774d'),(40,'099765e7-09c4-4344-b26e-47afb0060726'),(49,'075ec923-add3-49eb-8eb9-f2b49753c56e'),(50,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(50,'2e033034-a8f8-406b-9dbf-c778a00020de'),(50,'836c3c9e-97ba-4be3-8e52-717163c497db'),(51,'017cf580-7fc7-40a0-b0b4-1aabc4acd04a'),(58,'521d627d-9016-49b3-baa3-d07e73becaed'),(67,'6338fc76-5726-4673-97c9-87014970bb88'),(67,'43d6c57e-14a3-46de-941b-d1de92d2d414'),(83,'1e513af9-352d-4fad-825f-dbde6a49a6fd'),(83,'7fb0117e-a5a2-4ce0-a714-2f26313e0637'),(83,'ca088110-b9b6-4da7-989f-682cfd172caf'),(83,'386f791e-9ddb-4f19-a089-989315c83e62'),(93,'30153391-27d7-4c21-8d95-497eedc48af8'),(93,'41d76dcb-d8f4-40df-bed0-f454d1cfaded'),(93,'c30d6890-4540-40bf-830a-07304d1ff9af'),(93,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(93,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(93,'1b4a1f5b-0afb-4fea-98e9-053c533e901e'),(100,'dd2b8cd4-8145-4049-beac-6d6c8cc84ef6'),(100,'3de3039a-a109-4fde-829d-3d7555ecd33f'),(100,'2a86b344-e122-4981-a75b-4c6e1710829c'),(102,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(102,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(102,'30153391-27d7-4c21-8d95-497eedc48af8'),(102,'41d76dcb-d8f4-40df-bed0-f454d1cfaded'),(110,'0d206224-30e2-4cdb-9e10-b079e524703e'),(110,'2a86b344-e122-4981-a75b-4c6e1710829c'),(114,'a1607811-deb7-4b66-a88d-266dd3d1ff67'),(115,'ba945787-3810-4ba0-b67b-fc3c3c7a5507'),(115,'247612b8-9c1a-435f-b4fd-86ce2fc169ab'),(116,'017cf580-7fc7-40a0-b0b4-1aabc4acd04a'),(116,'521d627d-9016-49b3-baa3-d07e73becaed'),(118,'1c418320-7480-4029-b534-83f6924e0467'),(119,'30153391-27d7-4c21-8d95-497eedc48af8'),(119,'900f0d16-dcbd-4f77-b755-adacaa85ef85'),(119,'fd0c6071-c2a5-43ae-9b22-1b034ff202f1'),(119,'1b4a1f5b-0afb-4fea-98e9-053c533e901e'),(120,'e23a39c4-93a1-483d-b6e3-a1d37aa4721c'),(121,'0d206224-30e2-4cdb-9e10-b079e524703e'),(121,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(121,'2a86b344-e122-4981-a75b-4c6e1710829c'),(122,'ca088110-b9b6-4da7-989f-682cfd172caf'),(123,'b577a4a3-181c-4aa9-b7de-e64d9a63ab27'),(123,'8f18c785-3c74-402e-93d4-d655694f874f'),(123,'8516f9f5-ccb1-4e92-9def-0307117a8ee7'),(124,'017cf580-7fc7-40a0-b0b4-1aabc4acd04a');
/*!40000 ALTER TABLE `Plot_coowners` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:13:00
