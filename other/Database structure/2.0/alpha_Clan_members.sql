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
-- Table structure for table `Clan_members`
--

DROP TABLE IF EXISTS `Clan_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clan_members` (
  `Clan_id` int(11) NOT NULL,
  `members` varchar(255) DEFAULT NULL,
  KEY `FKsa1gx3t8pl5ob6byv2rsylxnd` (`Clan_id`),
  CONSTRAINT `FKsa1gx3t8pl5ob6byv2rsylxnd` FOREIGN KEY (`Clan_id`) REFERENCES `Clan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clan_members`
--

LOCK TABLES `Clan_members` WRITE;
/*!40000 ALTER TABLE `Clan_members` DISABLE KEYS */;
INSERT INTO `Clan_members` VALUES (7,'22c890f1-5d0e-44c9-89a5-1c4068949fb0'),(9,'609d12d3-6728-435d-9c61-e7c8451bbe52'),(9,'5dadb5df-211d-437c-aee0-63efd9f0ff0c'),(9,'189ed923-78d8-4bd0-a4cd-b7a95f0cd2d6'),(9,'8a6d5662-0770-4d9e-a9de-2d0ee3ec6f0f'),(9,'19ca7cd2-d838-4048-98e0-8ae39312df08'),(12,'498dd6f3-89f6-41d3-83f8-a5c7c8027233'),(16,'2a86b344-e122-4981-a75b-4c6e1710829c'),(8,'6be436ee-56b4-4023-a409-ec5dd626d24d'),(8,'09250fea-2937-4e83-8aae-7c30059b5a38'),(17,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(17,'eb0d9b81-0c57-4db9-ac3b-a38679e64ea0'),(17,'1426d019-4609-4c60-be34-43ca43299144'),(19,'3de3039a-a109-4fde-829d-3d7555ecd33f'),(19,'2a86b344-e122-4981-a75b-4c6e1710829c'),(1,'34955663-8b3c-4568-b72b-4314ef2b1abb'),(1,'12643edf-c163-41a5-b8cb-f97aa7322292'),(1,'c8f3ddb0-bc08-40c9-a430-b9c56c0eccc0'),(1,'d5def77c-eef7-4760-b66c-fa0d1c5798be'),(1,'ee65ec87-0209-49e1-923d-b169d73bc2ff'),(1,'691145b3-a10c-465d-975a-8fddd2362e65'),(1,'f72e1ed1-6b3a-40c0-aa21-beddd3881e81'),(1,'6338fc76-5726-4673-97c9-87014970bb88'),(1,'f681cb86-ddc5-4797-9f79-457e7326e9c6'),(1,'36eb8226-4e22-4a66-a16f-c4503b443c5a'),(1,'017cf580-7fc7-40a0-b0b4-1aabc4acd04a'),(2,'099765e7-09c4-4344-b26e-47afb0060726'),(2,'d3241a40-e2ab-4e13-84a6-9cdb1054e88d'),(2,'7710d3b3-b4e6-4bac-8cf6-2baa9f8ddbb1'),(2,'acb79d26-cb77-43ed-aee9-c0d754ee73b3'),(3,'097e6275-56e5-4fbc-aa5e-551637c2bda3'),(3,'0848cc3c-b3f3-4fbf-b4c6-d8e25a6c6e4e'),(3,'6192a860-6e4b-4868-a449-5890f0958f47'),(3,'b4a82f69-aff9-4e6b-bba3-3bd541a4aa01'),(3,'e9c2d630-4635-49da-aef8-529b086b6f0a'),(3,'bf627ed5-9f74-483b-9154-0d79eec789c3'),(3,'c3918242-adb9-47d8-a508-d620a454cb69'),(4,'054e3e22-6775-4b6c-a3fc-defe4bcfcbae'),(4,'ab52bc00-2134-4263-9b6a-ec68a5004f3a'),(4,'c4025cab-7198-479d-95e1-d32d12ca8611'),(4,'3db2478d-7395-4df0-87c2-04e62dc1dd10'),(5,'4353931e-dc4c-4be8-9469-4346a84cc982'),(5,'b57ef14f-cc50-4846-8d39-7bbf7aafa250'),(5,'16b43956-fd3a-44f0-aebc-3d9ef741774d'),(5,'87fbc806-ef38-4f3f-9ee6-36ebc7b64b48'),(5,'43f8b162-0569-441d-a5a3-5d9a890476ee'),(5,'1426d019-4609-4c60-be34-43ca43299144'),(5,'1c418320-7480-4029-b534-83f6924e0467'),(14,'fa3e0ffe-aaad-4ca9-a80f-0d707ae63a72'),(18,'30153391-27d7-4c21-8d95-497eedc48af8'),(20,'ca088110-b9b6-4da7-989f-682cfd172caf'),(20,'386f791e-9ddb-4f19-a089-989315c83e62'),(21,'2a86b344-e122-4981-a75b-4c6e1710829c'),(21,'76c07ac5-f299-4b50-9505-be9f775f5fcb'),(21,'3de3039a-a109-4fde-829d-3d7555ecd33f'),(21,'0acb9e31-ad47-49e7-855c-de37cedb1e5b'),(21,'03a43adb-a582-4df2-8ab0-cbfe71e9a4a6');
/*!40000 ALTER TABLE `Clan_members` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:13:36
