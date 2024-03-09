CREATE DATABASE  IF NOT EXISTS `dbenrollment` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `dbenrollment`;
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: dbenrollment
-- ------------------------------------------------------
-- Server version	8.0.36-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course_offering`
--

DROP TABLE IF EXISTS `course_offering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_offering` (
  `coursecode` varchar(7) NOT NULL,
  `section` varchar(3) NOT NULL,
  `termcode` varchar(6) NOT NULL,
  `offering_slots` int DEFAULT NULL,
  `taken_slots` int DEFAULT NULL,
  `schedule` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`coursecode`,`section`,`termcode`),
  KEY `FKA_002_idx` (`termcode`),
  CONSTRAINT `FKA_001` FOREIGN KEY (`coursecode`) REFERENCES `courses` (`coursecode`),
  CONSTRAINT `FKA_002` FOREIGN KEY (`termcode`) REFERENCES `ref_terms` (`termcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_offering`
--

LOCK TABLES `course_offering` WRITE;
/*!40000 ALTER TABLE `course_offering` DISABLE KEYS */;
INSERT INTO `course_offering` VALUES ('CCPROG1','S11','T32223',5,4,'MH 0730-0900'),('CCPROG1','S12','T32223',5,5,'TF 0730-0900'),('CCPROG2','S11','T12324',5,4,'MH 0730-0900'),('CCPROG2','S12','T12324',5,5,'TF 0730-0900'),('CCPROG3','S18','T22324',10,0,'MH 0730-0900'),('CCPROG3','S19','T22324',10,0,'TF 0730-0900');
/*!40000 ALTER TABLE `course_offering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `coursecode` varchar(7) NOT NULL,
  `coursetitle` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`coursecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES ('CCINFOM','Introduction to Databases'),('CCPROG1','Computer Programming 1'),('CCPROG2','Computer Programming 2'),('CCPROG3','Computer Programming 3');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollment` (
  `studentid` int NOT NULL,
  `coursecode` varchar(7) NOT NULL,
  `section` varchar(3) NOT NULL,
  `termcode` varchar(6) NOT NULL,
  `grade` decimal(4,2) DEFAULT NULL,
  PRIMARY KEY (`studentid`,`coursecode`,`section`,`termcode`),
  KEY `FKB_002_idx` (`coursecode`,`section`,`termcode`),
  KEY `FKB_003_idx` (`grade`),
  CONSTRAINT `FKB_001` FOREIGN KEY (`studentid`) REFERENCES `students` (`studentid`),
  CONSTRAINT `FKB_002` FOREIGN KEY (`coursecode`, `section`, `termcode`) REFERENCES `course_offering` (`coursecode`, `section`, `termcode`),
  CONSTRAINT `FKB_003` FOREIGN KEY (`grade`) REFERENCES `ref_grades` (`grade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` VALUES (121001,'CCPROG3','S18','T22324',NULL),(121001,'CCPROG1','S11','T32223',1.00),(121005,'CCPROG2','S12','T12324',1.00),(121007,'CCPROG1','S12','T32223',1.00),(121008,'CCPROG1','S12','T32223',1.00),(121002,'CCPROG1','S11','T32223',1.50),(121002,'CCPROG2','S12','T12324',2.00),(121004,'CCPROG1','S11','T32223',2.00),(121001,'CCPROG2','S11','T12324',2.50),(121002,'CCPROG2','S11','T12324',2.50),(121005,'CCPROG1','S12','T32223',2.50),(121006,'CCPROG1','S12','T32223',2.50),(121003,'CCPROG1','S11','T32223',3.00),(121003,'CCPROG2','S12','T12324',3.00),(121008,'CCPROG2','S11','T12324',3.00),(121010,'CCPROG2','S11','T12324',3.50),(121004,'CCPROG2','S12','T12324',4.00),(121009,'CCPROG2','S11','T12324',4.00);
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ref_grades`
--

DROP TABLE IF EXISTS `ref_grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ref_grades` (
  `grade` decimal(4,2) NOT NULL,
  `interpretation` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`grade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ref_grades`
--

LOCK TABLES `ref_grades` WRITE;
/*!40000 ALTER TABLE `ref_grades` DISABLE KEYS */;
INSERT INTO `ref_grades` VALUES (0.00,'Fail'),(1.00,'Acceptable'),(1.50,'Low Satisfactory'),(2.00,'Satisfactory'),(2.50,'Above Satisfactory'),(3.00,'As Expected'),(3.50,'Beyond Expectation'),(4.00,'Excellent');
/*!40000 ALTER TABLE `ref_grades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ref_terms`
--

DROP TABLE IF EXISTS `ref_terms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ref_terms` (
  `termcode` varchar(6) NOT NULL,
  `term` int DEFAULT NULL,
  `start_year` int DEFAULT NULL,
  `end_year` int DEFAULT NULL,
  `current` tinyint DEFAULT NULL,
  PRIMARY KEY (`termcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ref_terms`
--

LOCK TABLES `ref_terms` WRITE;
/*!40000 ALTER TABLE `ref_terms` DISABLE KEYS */;
INSERT INTO `ref_terms` VALUES ('T12223',1,2022,2023,0),('T12324',1,2023,2024,0),('T22223',2,2022,2023,0),('T22324',2,2023,2024,1),('T32223',3,2022,2023,0);
/*!40000 ALTER TABLE `ref_terms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `studentid` int NOT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `firstname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`studentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (121001,'Reyes','Samuel'),(121002,'Fabian','Daniella'),(121003,'David','Grace'),(121004,'Joaquin','Ralph'),(121005,'Tan','Hailey'),(121006,'Quirino','Sarah'),(121007,'Lee','Ignacio'),(121008,'Ang','France'),(121009,'Santos','Bobby'),(121010,'Valenzuela','Carlos');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-09 17:24:06
