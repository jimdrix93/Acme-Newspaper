start transaction;

drop database if exists `acme-newspaper`;
create database `acme-newspaper`;

use `acme-newspaper`;

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';
create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
  on `acme-newspaper`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
    create temporary tables, lock tables, create view, create routine, 
    alter routine, execute, trigger, show view
  on `acme-newspaper`.* to 'acme-manager'@'%';
  
  
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: acme-newspaper
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Table structure for table `actor`
--

DROP TABLE IF EXISTS `actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cgls5lrufx91ufsyh467spwa3` (`userAccount_id`),
  CONSTRAINT `FK_cgls5lrufx91ufsyh467spwa3` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor`
--

LOCK TABLES `actor` WRITE;
/*!40000 ALTER TABLE `actor` DISABLE KEYS */;
/*!40000 ALTER TABLE `actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actor_folder`
--

DROP TABLE IF EXISTS `actor_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor_folder` (
  `Actor_id` int(11) NOT NULL,
  `folders_id` int(11) NOT NULL,
  UNIQUE KEY `UK_dp573h40udupcm5m1kgn2bc2r` (`folders_id`),
  CONSTRAINT `FK_dp573h40udupcm5m1kgn2bc2r` FOREIGN KEY (`folders_id`) REFERENCES `folder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor_folder`
--

LOCK TABLES `actor_folder` WRITE;
/*!40000 ALTER TABLE `actor_folder` DISABLE KEYS */;
INSERT INTO `actor_folder` VALUES (452,488),(452,498),(452,508),(452,518),(452,528);
/*!40000 ALTER TABLE `actor_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_idt4b4u259p6vs4pyr9lax4eg` (`userAccount_id`),
  CONSTRAINT `FK_idt4b4u259p6vs4pyr9lax4eg` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (452,0,'Sin calle','admin@acme.com','Admin','123123123','Surname',442);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `advertisement`
--

DROP TABLE IF EXISTS `advertisement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `advertisement` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `bannerURL` varchar(255) DEFAULT NULL,
  `targetURL` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `agent_id` int(11) NOT NULL,
  `creditCard_id` int(11) NOT NULL,
  `newspaper_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_pdtmvobr4ousebqlhebxwn7q0` (`title`),
  KEY `FK_7n9ussuxsi3k6rm34vajrccvn` (`agent_id`),
  KEY `FK_t1egal141du0gw5ya0o6j11ya` (`creditCard_id`),
  KEY `FK_2a9jqcvexg35eohaebb71i4xu` (`newspaper_id`),
  CONSTRAINT `FK_2a9jqcvexg35eohaebb71i4xu` FOREIGN KEY (`newspaper_id`) REFERENCES `newspaper` (`id`),
  CONSTRAINT `FK_7n9ussuxsi3k6rm34vajrccvn` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`),
  CONSTRAINT `FK_t1egal141du0gw5ya0o6j11ya` FOREIGN KEY (`creditCard_id`) REFERENCES `creditcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `advertisement`
--

LOCK TABLES `advertisement` WRITE;
/*!40000 ALTER TABLE `advertisement` DISABLE KEYS */;
/*!40000 ALTER TABLE `advertisement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5cg6nedtnilfs6spfq209syse` (`userAccount_id`),
  CONSTRAINT `FK_5cg6nedtnilfs6spfq209syse` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent`
--

LOCK TABLES `agent` WRITE;
/*!40000 ALTER TABLE `agent` DISABLE KEYS */;
/*!40000 ALTER TABLE `agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` longtext,
  `finalMode` bit(1) NOT NULL,
  `publicationMoment` datetime DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `newspaper_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_s0bbgdvg3xnsm1bwqhqk3bwpd` (`title`,`summary`),
  KEY `FK_pftm848lf5hu8sd0vghfs605l` (`newspaper_id`),
  CONSTRAINT `FK_pftm848lf5hu8sd0vghfs605l` FOREIGN KEY (`newspaper_id`) REFERENCES `newspaper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_followup`
--

DROP TABLE IF EXISTS `article_followup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_followup` (
  `Article_id` int(11) NOT NULL,
  `followups_id` int(11) NOT NULL,
  UNIQUE KEY `UK_fo8lp2o0ges3haa0y5uy6rxik` (`followups_id`),
  KEY `FK_6r0w9kdctegoycdcjg8dsm28f` (`Article_id`),
  CONSTRAINT `FK_6r0w9kdctegoycdcjg8dsm28f` FOREIGN KEY (`Article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FK_fo8lp2o0ges3haa0y5uy6rxik` FOREIGN KEY (`followups_id`) REFERENCES `followup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_followup`
--

LOCK TABLES `article_followup` WRITE;
/*!40000 ALTER TABLE `article_followup` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_followup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_pictures`
--

DROP TABLE IF EXISTS `article_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_pictures` (
  `Article_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_slh5rr6y2n4ml5s20v5nlr52g` (`Article_id`),
  CONSTRAINT `FK_slh5rr6y2n4ml5s20v5nlr52g` FOREIGN KEY (`Article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_pictures`
--

LOCK TABLES `article_pictures` WRITE;
/*!40000 ALTER TABLE `article_pictures` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chirp`
--

DROP TABLE IF EXISTS `chirp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chirp` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `moment` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chirp`
--

LOCK TABLES `chirp` WRITE;
/*!40000 ALTER TABLE `chirp` DISABLE KEYS */;
/*!40000 ALTER TABLE `chirp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditcard`
--

DROP TABLE IF EXISTS `creditcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditcard` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `cvv` int(11) DEFAULT NULL,
  `expirationMonth` int(11) DEFAULT NULL,
  `expirationYear` int(11) DEFAULT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_7b2yxtxdjjo00k6xpakqkdyn0` (`number`,`cvv`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcard`
--

LOCK TABLES `creditcard` WRITE;
/*!40000 ALTER TABLE `creditcard` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pwmktpkay2yx7v00mrwmuscl8` (`userAccount_id`),
  CONSTRAINT `FK_pwmktpkay2yx7v00mrwmuscl8` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `systemFolder` bit(1) NOT NULL,
  `parentFolder_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_l1kp977466ddsv762wign7kdh` (`name`),
  KEY `FK_qshunmxjrgbdahcn1eu357sxt` (`parentFolder_id`),
  CONSTRAINT `FK_qshunmxjrgbdahcn1eu357sxt` FOREIGN KEY (`parentFolder_id`) REFERENCES `folder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
INSERT INTO `folder` VALUES (488,0,'inbox','',NULL),(498,0,'outbox','',NULL),(508,0,'notificationbox','',NULL),(518,0,'trashbox','',NULL),(528,0,'spambox','',NULL);
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder_mymessage`
--

DROP TABLE IF EXISTS `folder_mymessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder_mymessage` (
  `Folder_id` int(11) NOT NULL,
  `mymessages_id` int(11) NOT NULL,
  KEY `FK_hffeps88yjyqb5cbb62jx9xad` (`mymessages_id`),
  KEY `FK_pwfcqwrpnbb9naaakv66u04c` (`Folder_id`),
  CONSTRAINT `FK_pwfcqwrpnbb9naaakv66u04c` FOREIGN KEY (`Folder_id`) REFERENCES `folder` (`id`),
  CONSTRAINT `FK_hffeps88yjyqb5cbb62jx9xad` FOREIGN KEY (`mymessages_id`) REFERENCES `mymessage` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder_mymessage`
--

LOCK TABLES `folder_mymessage` WRITE;
/*!40000 ALTER TABLE `folder_mymessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `folder_mymessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followup`
--

DROP TABLE IF EXISTS `followup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `followup` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `publicationMoment` datetime DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followup`
--

LOCK TABLES `followup` WRITE;
/*!40000 ALTER TABLE `followup` DISABLE KEYS */;
/*!40000 ALTER TABLE `followup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followup_pictures`
--

DROP TABLE IF EXISTS `followup_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `followup_pictures` (
  `Followup_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_bikvs1wwtsvtql1bt1ep88bo4` (`Followup_id`),
  CONSTRAINT `FK_bikvs1wwtsvtql1bt1ep88bo4` FOREIGN KEY (`Followup_id`) REFERENCES `followup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followup_pictures`
--

LOCK TABLES `followup_pictures` WRITE;
/*!40000 ALTER TABLE `followup_pictures` DISABLE KEYS */;
/*!40000 ALTER TABLE `followup_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mymessage`
--

DROP TABLE IF EXISTS `mymessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mymessage` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `broadcast` bit(1) NOT NULL,
  `moment` datetime DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `recipient_id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mymessage`
--

LOCK TABLES `mymessage` WRITE;
/*!40000 ALTER TABLE `mymessage` DISABLE KEYS */;
/*!40000 ALTER TABLE `mymessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newspaper`
--

DROP TABLE IF EXISTS `newspaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newspaper` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `isPrivate` bit(1) NOT NULL,
  `isPublicated` bit(1) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `publicationDate` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_a2rc9sav5fw5lyvjcersbl81m` (`title`,`description`,`publicationDate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newspaper`
--

LOCK TABLES `newspaper` WRITE;
/*!40000 ALTER TABLE `newspaper` DISABLE KEYS */;
/*!40000 ALTER TABLE `newspaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creditCard_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `newspaper_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cak1wsbmm9pxfybdcvdnao3ga` (`creditCard_id`),
  KEY `FK_2i5w4btb7x3r6d2jsd213aqct` (`customer_id`),
  KEY `FK_b3d3q413vlktogdjnnus3ep9e` (`newspaper_id`),
  CONSTRAINT `FK_b3d3q413vlktogdjnnus3ep9e` FOREIGN KEY (`newspaper_id`) REFERENCES `newspaper` (`id`),
  CONSTRAINT `FK_2i5w4btb7x3r6d2jsd213aqct` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_cak1wsbmm9pxfybdcvdnao3ga` FOREIGN KEY (`creditCard_id`) REFERENCES `creditcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tabooword`
--

DROP TABLE IF EXISTS `tabooword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tabooword` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_dhh5tlth4rbpl63wbqhk691kb` (`text`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tabooword`
--

LOCK TABLES `tabooword` WRITE;
/*!40000 ALTER TABLE `tabooword` DISABLE KEYS */;
INSERT INTO `tabooword` VALUES (457,0,'sex'),(458,0,'sexo'),(459,0,'viagra'),(460,0,'cialis');
/*!40000 ALTER TABLE `tabooword` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_o6s94d43co03sx067ili5760c` (`userAccount_id`),
  CONSTRAINT `FK_o6s94d43co03sx067ili5760c` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_article`
--

DROP TABLE IF EXISTS `user_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_article` (
  `User_id` int(11) NOT NULL,
  `articles_id` int(11) NOT NULL,
  UNIQUE KEY `UK_siug4boflrp0p0dncq76a13wu` (`articles_id`),
  KEY `FK_bkiti988ki4sxkoqc8ro1wnsa` (`User_id`),
  CONSTRAINT `FK_bkiti988ki4sxkoqc8ro1wnsa` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_siug4boflrp0p0dncq76a13wu` FOREIGN KEY (`articles_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_article`
--

LOCK TABLES `user_article` WRITE;
/*!40000 ALTER TABLE `user_article` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_chirp`
--

DROP TABLE IF EXISTS `user_chirp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_chirp` (
  `User_id` int(11) NOT NULL,
  `chirps_id` int(11) NOT NULL,
  UNIQUE KEY `UK_ls9bn8hbpkktyfahbm4wujrps` (`chirps_id`),
  KEY `FK_8lvf5igmdhhmxc70p7ujd36ym` (`User_id`),
  CONSTRAINT `FK_8lvf5igmdhhmxc70p7ujd36ym` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_ls9bn8hbpkktyfahbm4wujrps` FOREIGN KEY (`chirps_id`) REFERENCES `chirp` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_chirp`
--

LOCK TABLES `user_chirp` WRITE;
/*!40000 ALTER TABLE `user_chirp` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_chirp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_newspaper`
--

DROP TABLE IF EXISTS `user_newspaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_newspaper` (
  `User_id` int(11) NOT NULL,
  `newspapers_id` int(11) NOT NULL,
  UNIQUE KEY `UK_oqhrpkgl440xehybmm713ru36` (`newspapers_id`),
  KEY `FK_24xv3fsqc505dhy1bv4ldnor9` (`User_id`),
  CONSTRAINT `FK_24xv3fsqc505dhy1bv4ldnor9` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_oqhrpkgl440xehybmm713ru36` FOREIGN KEY (`newspapers_id`) REFERENCES `newspaper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_newspaper`
--

LOCK TABLES `user_newspaper` WRITE;
/*!40000 ALTER TABLE `user_newspaper` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_newspaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_user`
--

DROP TABLE IF EXISTS `user_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_user` (
  `User_id` int(11) NOT NULL,
  `followeds_id` int(11) NOT NULL,
  KEY `FK_rvg566sropcusl3dx5xtex7wq` (`followeds_id`),
  KEY `FK_nlnx78x3m38aq2r86t1d5eio1` (`User_id`),
  CONSTRAINT `FK_nlnx78x3m38aq2r86t1d5eio1` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_rvg566sropcusl3dx5xtex7wq` FOREIGN KEY (`followeds_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_user`
--

LOCK TABLES `user_user` WRITE;
/*!40000 ALTER TABLE `user_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (442,0,'21232f297a57a5a743894a0e4a801fc3','admin');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount_authorities`
--

DROP TABLE IF EXISTS `useraccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount_authorities`
--

LOCK TABLES `useraccount_authorities` WRITE;
/*!40000 ALTER TABLE `useraccount_authorities` DISABLE KEYS */;
INSERT INTO `useraccount_authorities` VALUES (442,'ADMIN');
/*!40000 ALTER TABLE `useraccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `volume`
--

DROP TABLE IF EXISTS `volume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `volume` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1g852qpv1irbrshl0rmqgfm3a` (`user_id`),
  CONSTRAINT `FK_1g852qpv1irbrshl0rmqgfm3a` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `volume`
--

LOCK TABLES `volume` WRITE;
/*!40000 ALTER TABLE `volume` DISABLE KEYS */;
/*!40000 ALTER TABLE `volume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `volume_newspaper`
--

DROP TABLE IF EXISTS `volume_newspaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `volume_newspaper` (
  `Volume_id` int(11) NOT NULL,
  `newspapers_id` int(11) NOT NULL,
  KEY `FK_55de0xvt5cb2u4p2xkeofporj` (`newspapers_id`),
  KEY `FK_piiv98lkttksatc6qx4cncuas` (`Volume_id`),
  CONSTRAINT `FK_piiv98lkttksatc6qx4cncuas` FOREIGN KEY (`Volume_id`) REFERENCES `volume` (`id`),
  CONSTRAINT `FK_55de0xvt5cb2u4p2xkeofporj` FOREIGN KEY (`newspapers_id`) REFERENCES `newspaper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `volume_newspaper`
--

LOCK TABLES `volume_newspaper` WRITE;
/*!40000 ALTER TABLE `volume_newspaper` DISABLE KEYS */;
/*!40000 ALTER TABLE `volume_newspaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vsubscription`
--

DROP TABLE IF EXISTS `vsubscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vsubscription` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `creditCard_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `volume_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s2f3x6rqal3kbp9twfmm0hyq4` (`creditCard_id`),
  KEY `FK_nfsspgoloak2ly8tarl9rku8g` (`customer_id`),
  KEY `FK_j1cxqinvxmtoyc016wlda96b3` (`volume_id`),
  CONSTRAINT `FK_j1cxqinvxmtoyc016wlda96b3` FOREIGN KEY (`volume_id`) REFERENCES `volume` (`id`),
  CONSTRAINT `FK_nfsspgoloak2ly8tarl9rku8g` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_s2f3x6rqal3kbp9twfmm0hyq4` FOREIGN KEY (`creditCard_id`) REFERENCES `creditcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vsubscription`
--

LOCK TABLES `vsubscription` WRITE;
/*!40000 ALTER TABLE `vsubscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `vsubscription` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-09 20:05:55


commit;

