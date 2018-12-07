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
-- Temporary view structure for view `MaxPlayers`
--

DROP TABLE IF EXISTS `MaxPlayers`;
/*!50001 DROP VIEW IF EXISTS `MaxPlayers`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `MaxPlayers` AS SELECT 
 1 AS `start time`,
 1 AS `end time`,
 1 AS `players online`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `playtime`
--

DROP TABLE IF EXISTS `playtime`;
/*!50001 DROP VIEW IF EXISTS `playtime`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `playtime` AS SELECT 
 1 AS `username`,
 1 AS `total time`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `currentUsernames`
--

DROP TABLE IF EXISTS `currentUsernames`;
/*!50001 DROP VIEW IF EXISTS `currentUsernames`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `currentUsernames` AS SELECT 
 1 AS `id`,
 1 AS `creation`,
 1 AS `username`,
 1 AS `uuid`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `listConnections`
--

DROP TABLE IF EXISTS `listConnections`;
/*!50001 DROP VIEW IF EXISTS `listConnections`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `listConnections` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `creation`,
 1 AS `left_date`,
 1 AS `ip`,
 1 AS `time diff`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `MaxPlayers`
--

/*!50001 DROP VIEW IF EXISTS `MaxPlayers`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`Defman`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `MaxPlayers` AS select distinct max(`c`.`creation`) AS `start time`,min(`c`.`left_date`) AS `end time`,count(`c`.`id`) AS `players online` from (`ConnectionRecord` `c` join `ConnectionRecord` `overlapping_c` on((`c`.`creation` between `overlapping_c`.`creation` and `overlapping_c`.`left_date`))) group by `c`.`id` order by (count(`c`.`id`) - 1) desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `playtime`
--

/*!50001 DROP VIEW IF EXISTS `playtime`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`Defman`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `playtime` AS select `currentUsernames`.`username` AS `username`,sum((unix_timestamp(`ConnectionRecord`.`left_date`) - unix_timestamp(`ConnectionRecord`.`creation`))) AS `total time` from (`ConnectionRecord` join `currentUsernames` on((`currentUsernames`.`uuid` = `ConnectionRecord`.`player`))) group by `ConnectionRecord`.`player` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `currentUsernames`
--

/*!50001 DROP VIEW IF EXISTS `currentUsernames`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`Defman`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `currentUsernames` AS select `UsernameUUIDRecord`.`id` AS `id`,`UsernameUUIDRecord`.`creation` AS `creation`,`UsernameUUIDRecord`.`username` AS `username`,`UsernameUUIDRecord`.`uuid` AS `uuid` from `UsernameUUIDRecord` where `UsernameUUIDRecord`.`id` in (select max(`UsernameUUIDRecord`.`id`) from `UsernameUUIDRecord` group by `UsernameUUIDRecord`.`uuid`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `listConnections`
--

/*!50001 DROP VIEW IF EXISTS `listConnections`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`Defman`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `listConnections` AS select `ConnectionRecord`.`id` AS `id`,`currentUsernames`.`username` AS `username`,`ConnectionRecord`.`creation` AS `creation`,`ConnectionRecord`.`left_date` AS `left_date`,`ConnectionRecord`.`ip` AS `ip`,(unix_timestamp(`ConnectionRecord`.`left_date`) - unix_timestamp(`ConnectionRecord`.`creation`)) AS `time diff` from (`ConnectionRecord` join `currentUsernames` on((`currentUsernames`.`uuid` = `ConnectionRecord`.`player`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-17 17:13:53
