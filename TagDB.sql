/*
SQLyog Community v13.1.0 (64 bit)
MySQL - 5.7.16-log : Database - sangpsnew
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sangpsnew` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `sangpsnew`;

/*Table structure for table `aspnetroles` */

DROP TABLE IF EXISTS `aspnetroles`;

CREATE TABLE `aspnetroles` (
  `Id` varchar(128) NOT NULL,
  `Name` varchar(256) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `aspnetuserclaims` */

DROP TABLE IF EXISTS `aspnetuserclaims`;

CREATE TABLE `aspnetuserclaims` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserId` varchar(128) NOT NULL,
  `ClaimType` longtext,
  `ClaimValue` longtext,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id` (`Id`),
  KEY `UserId` (`UserId`),
  CONSTRAINT `ApplicationUser_Claims` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `aspnetuserlogins` */

DROP TABLE IF EXISTS `aspnetuserlogins`;

CREATE TABLE `aspnetuserlogins` (
  `LoginProvider` varchar(128) NOT NULL,
  `ProviderKey` varchar(128) NOT NULL,
  `UserId` varchar(128) NOT NULL,
  PRIMARY KEY (`LoginProvider`,`ProviderKey`,`UserId`),
  KEY `ApplicationUser_Logins` (`UserId`),
  CONSTRAINT `ApplicationUser_Logins` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `aspnetuserroles` */

DROP TABLE IF EXISTS `aspnetuserroles`;

CREATE TABLE `aspnetuserroles` (
  `UserId` varchar(128) NOT NULL,
  `RoleId` varchar(128) NOT NULL,
  PRIMARY KEY (`UserId`,`RoleId`),
  KEY `IdentityRole_Users` (`RoleId`),
  CONSTRAINT `ApplicationUser_Roles` FOREIGN KEY (`UserId`) REFERENCES `aspnetusers` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `IdentityRole_Users` FOREIGN KEY (`RoleId`) REFERENCES `aspnetroles` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `aspnetusers` */

DROP TABLE IF EXISTS `aspnetusers`;

CREATE TABLE `aspnetusers` (
  `Id` varchar(128) NOT NULL,
  `Email` varchar(256) DEFAULT NULL,
  `EmailConfirmed` tinyint(1) NOT NULL,
  `PasswordHash` longtext,
  `SecurityStamp` longtext,
  `PhoneNumber` longtext,
  `PhoneNumberConfirmed` tinyint(1) NOT NULL,
  `TwoFactorEnabled` tinyint(1) NOT NULL,
  `LockoutEndDateUtc` datetime DEFAULT NULL,
  `LockoutEnabled` tinyint(1) NOT NULL,
  `AccessFailedCount` int(11) NOT NULL,
  `UserName` varchar(256) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `attributes` */

DROP TABLE IF EXISTS `attributes`;

CREATE TABLE `attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(4000) NOT NULL,
  `type` varchar(128) NOT NULL,
  `attribute` varchar(128) NOT NULL,
  `expression` varchar(4000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `basepointgeofence` */

DROP TABLE IF EXISTS `basepointgeofence`;

CREATE TABLE `basepointgeofence` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `DeviceId` int(11) DEFAULT NULL,
  `BLatitude` double DEFAULT NULL,
  `BLongitude` double DEFAULT NULL,
  `CircularDistance` double DEFAULT NULL,
  `cBoundStatus` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

/*Table structure for table `bikeaccalarmflag` */

DROP TABLE IF EXISTS `bikeaccalarmflag`;

CREATE TABLE `bikeaccalarmflag` (
  `DeviceId` int(11) DEFAULT NULL,
  `nAcc` int(11) DEFAULT NULL,
  `bikeonoffsts` int(11) DEFAULT NULL,
  `trackerresponse` int(11) DEFAULT NULL,
  `recievedsts` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `calendars` */

DROP TABLE IF EXISTS `calendars`;

CREATE TABLE `calendars` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `data` blob NOT NULL,
  `attributes` varchar(4000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `commands` */

DROP TABLE IF EXISTS `commands`;

CREATE TABLE `commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(4000) NOT NULL,
  `type` varchar(128) NOT NULL,
  `textchannel` bit(1) NOT NULL DEFAULT b'0',
  `attributes` varchar(4000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `customerdevices` */

DROP TABLE IF EXISTS `customerdevices`;

CREATE TABLE `customerdevices` (
  `CustomerId` int(11) DEFAULT NULL,
  `DeviceId` int(50) DEFAULT NULL,
  `dtActivationDate` datetime DEFAULT NULL,
  `dtExpireDate` datetime DEFAULT NULL,
  `nActivationStatus` int(11) DEFAULT NULL,
  `nExpiryStatus` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `customermaster` */

DROP TABLE IF EXISTS `customermaster`;

CREATE TABLE `customermaster` (
  `CustomerId` int(11) NOT NULL AUTO_INCREMENT,
  `cCustomerName` varchar(100) DEFAULT NULL,
  `cAddress` varchar(200) DEFAULT NULL,
  `nContactNumber` bigint(10) DEFAULT NULL,
  `cEmailId` varchar(50) DEFAULT NULL,
  `cIndividual` varchar(4) DEFAULT NULL,
  `cUserId` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`CustomerId`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;

/*Table structure for table `databasechangelog` */

DROP TABLE IF EXISTS `databasechangelog`;

CREATE TABLE `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `databasechangeloglock` */

DROP TABLE IF EXISTS `databasechangeloglock`;

CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dealerdevice` */

DROP TABLE IF EXISTS `dealerdevice`;

CREATE TABLE `dealerdevice` (
  `CustomerId` int(11) DEFAULT NULL,
  `DealerId` int(11) DEFAULT NULL,
  `DeviceId` int(11) DEFAULT NULL,
  `dtActivationDate` datetime DEFAULT NULL,
  `dtExpireDate` datetime DEFAULT NULL,
  `nActivationStatus` int(11) DEFAULT NULL,
  `nExpiryStatus` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dealermaster` */

DROP TABLE IF EXISTS `dealermaster`;

CREATE TABLE `dealermaster` (
  `DealerId` int(11) NOT NULL AUTO_INCREMENT,
  `cDealerName` varchar(50) DEFAULT NULL,
  `cAddress` varchar(100) DEFAULT NULL,
  `nContactNumber` bigint(20) DEFAULT NULL,
  `cEmailId` varchar(20) DEFAULT NULL,
  `cContactPerson` varchar(20) DEFAULT NULL,
  `cUserId` varchar(200) DEFAULT NULL,
  UNIQUE KEY `Id` (`DealerId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Table structure for table `device_attribute` */

DROP TABLE IF EXISTS `device_attribute`;

CREATE TABLE `device_attribute` (
  `deviceid` int(11) NOT NULL,
  `attributeid` int(11) NOT NULL,
  KEY `fk_device_attribute_deviceid` (`deviceid`),
  KEY `fk_device_attribute_attributeid` (`attributeid`),
  CONSTRAINT `fk_device_attribute_attributeid` FOREIGN KEY (`attributeid`) REFERENCES `attributes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_attribute_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `device_command` */

DROP TABLE IF EXISTS `device_command`;

CREATE TABLE `device_command` (
  `deviceid` int(11) NOT NULL,
  `commandid` int(11) NOT NULL,
  KEY `fk_device_command_deviceid` (`deviceid`),
  KEY `fk_device_command_commandid` (`commandid`),
  CONSTRAINT `fk_device_command_commandid` FOREIGN KEY (`commandid`) REFERENCES `commands` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_command_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `device_driver` */

DROP TABLE IF EXISTS `device_driver`;

CREATE TABLE `device_driver` (
  `deviceid` int(11) NOT NULL,
  `driverid` int(11) NOT NULL,
  KEY `fk_device_driver_deviceid` (`deviceid`),
  KEY `fk_device_driver_driverid` (`driverid`),
  CONSTRAINT `fk_device_driver_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_driver_driverid` FOREIGN KEY (`driverid`) REFERENCES `drivers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `device_geofence` */

DROP TABLE IF EXISTS `device_geofence`;

CREATE TABLE `device_geofence` (
  `deviceid` int(11) NOT NULL,
  `geofenceid` int(11) NOT NULL,
  KEY `fk_device_geofence_deviceid` (`deviceid`),
  KEY `fk_device_geofence_geofenceid` (`geofenceid`),
  CONSTRAINT `fk_device_geofence_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_geofence_geofenceid` FOREIGN KEY (`geofenceid`) REFERENCES `geofences` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `device_notification` */

DROP TABLE IF EXISTS `device_notification`;

CREATE TABLE `device_notification` (
  `deviceid` int(11) NOT NULL,
  `notificationid` int(11) NOT NULL,
  KEY `fk_device_notification_deviceid` (`deviceid`),
  KEY `fk_device_notification_notificationid` (`notificationid`),
  CONSTRAINT `fk_device_notification_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_notification_notificationid` FOREIGN KEY (`notificationid`) REFERENCES `notifications` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `devicealarm` */

DROP TABLE IF EXISTS `devicealarm`;

CREATE TABLE `devicealarm` (
  `DeviceId` int(11) DEFAULT NULL,
  `dtDate` datetime DEFAULT NULL,
  `dtTime` datetime DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cAlarmType` varchar(50) DEFAULT NULL,
  `nTabStatus` int(11) DEFAULT NULL,
  `nTabnotification` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `devicemaster` */

DROP TABLE IF EXISTS `devicemaster`;

CREATE TABLE `devicemaster` (
  `DeviceId` int(11) NOT NULL AUTO_INCREMENT,
  `cModelCode` varchar(20) DEFAULT NULL,
  `cIMEI` varchar(50) NOT NULL,
  `dtDate` datetime DEFAULT NULL,
  `cStatus` varchar(1) DEFAULT NULL,
  `SimNumber` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`DeviceId`,`cIMEI`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1;

/*Table structure for table `deviceposition` */

DROP TABLE IF EXISTS `deviceposition`;

CREATE TABLE `deviceposition` (
  `imei` bigint(20) NOT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `dname` varchar(80) DEFAULT NULL,
  `subname` varchar(80) DEFAULT NULL,
  `destatus` varchar(20) DEFAULT NULL,
  `acc` varchar(10) DEFAULT NULL,
  `dcharge` varchar(10) DEFAULT NULL,
  `teralarm` varchar(50) DEFAULT NULL,
  `gtrack` varchar(20) DEFAULT NULL,
  `oilstatus` varchar(10) DEFAULT NULL,
  `voltage` varchar(30) DEFAULT NULL,
  `dsignal` int(11) DEFAULT NULL,
  `alarm` varchar(30) DEFAULT NULL,
  `allang` varchar(20) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLstatus` int(11) DEFAULT NULL,
  `vstatus` int(11) DEFAULT NULL,
  `maxspeed` int(11) DEFAULT NULL,
  `DeviceId` int(11) NOT NULL,
  `toffcount` int(11) DEFAULT NULL,
  `nGsmStatus` int(11) DEFAULT NULL,
  KEY `NewIndex1` (`dname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `devices` */

DROP TABLE IF EXISTS `devices`;

CREATE TABLE `devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `uniqueid` varchar(128) NOT NULL,
  `lastupdate` timestamp NULL DEFAULT NULL,
  `positionid` int(11) DEFAULT NULL,
  `groupid` int(11) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `phone` varchar(128) DEFAULT NULL,
  `model` varchar(128) DEFAULT NULL,
  `contact` varchar(512) DEFAULT NULL,
  `category` varchar(128) DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_uniqueid` (`uniqueid`),
  KEY `fk_devices_groupid` (`groupid`),
  CONSTRAINT `fk_devices_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=latin1;

/*Table structure for table `deviceterminalstatus` */

DROP TABLE IF EXISTS `deviceterminalstatus`;

CREATE TABLE `deviceterminalstatus` (
  `imei` double DEFAULT NULL,
  `destatus` varchar(60) DEFAULT NULL,
  `acc` varchar(30) DEFAULT NULL,
  `dcharge` varchar(30) DEFAULT NULL,
  `teralarm` varchar(240) DEFAULT NULL,
  `gtrack` varchar(30) DEFAULT NULL,
  `oilstatus` varchar(150) DEFAULT NULL,
  `voltage` varchar(150) DEFAULT NULL,
  `dsignal` double DEFAULT NULL,
  `alarm` varchar(120) DEFAULT NULL,
  `allang` varchar(90) DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `cdateimei` (`imei`,`cdate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `drivers` */

DROP TABLE IF EXISTS `drivers`;

CREATE TABLE `drivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `uniqueid` varchar(128) NOT NULL,
  `attributes` varchar(4000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_driver_uniqueid` (`uniqueid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `events` */

DROP TABLE IF EXISTS `events`;

CREATE TABLE `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(128) NOT NULL,
  `servertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deviceid` int(11) DEFAULT NULL,
  `positionid` int(11) DEFAULT NULL,
  `geofenceid` int(11) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_event_deviceid` (`deviceid`),
  CONSTRAINT `fk_event_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=302002 DEFAULT CHARSET=latin1;

/*Table structure for table `geofences` */

DROP TABLE IF EXISTS `geofences`;

CREATE TABLE `geofences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `area` varchar(4096) NOT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `calendarid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_geofence_calendar_calendarid` (`calendarid`),
  CONSTRAINT `fk_geofence_calendar_calendarid` FOREIGN KEY (`calendarid`) REFERENCES `calendars` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `geofencingdetails` */

DROP TABLE IF EXISTS `geofencingdetails`;

CREATE TABLE `geofencingdetails` (
  `DeviceId` int(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `dtDate` datetime DEFAULT NULL,
  `Geostatus` int(11) DEFAULT NULL,
  `nTabStatus` int(11) DEFAULT NULL,
  `nTabnotification` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `group_attribute` */

DROP TABLE IF EXISTS `group_attribute`;

CREATE TABLE `group_attribute` (
  `groupid` int(11) NOT NULL,
  `attributeid` int(11) NOT NULL,
  KEY `fk_group_attribute_groupid` (`groupid`),
  KEY `fk_group_attribute_attributeid` (`attributeid`),
  CONSTRAINT `fk_group_attribute_attributeid` FOREIGN KEY (`attributeid`) REFERENCES `attributes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_group_attribute_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `group_command` */

DROP TABLE IF EXISTS `group_command`;

CREATE TABLE `group_command` (
  `groupid` int(11) NOT NULL,
  `commandid` int(11) NOT NULL,
  KEY `fk_group_command_groupid` (`groupid`),
  KEY `fk_group_command_commandid` (`commandid`),
  CONSTRAINT `fk_group_command_commandid` FOREIGN KEY (`commandid`) REFERENCES `commands` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_group_command_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `group_driver` */

DROP TABLE IF EXISTS `group_driver`;

CREATE TABLE `group_driver` (
  `groupid` int(11) NOT NULL,
  `driverid` int(11) NOT NULL,
  KEY `fk_group_driver_groupid` (`groupid`),
  KEY `fk_group_driver_driverid` (`driverid`),
  CONSTRAINT `fk_group_driver_driverid` FOREIGN KEY (`driverid`) REFERENCES `drivers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_group_driver_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `group_geofence` */

DROP TABLE IF EXISTS `group_geofence`;

CREATE TABLE `group_geofence` (
  `groupid` int(11) NOT NULL,
  `geofenceid` int(11) NOT NULL,
  KEY `fk_group_geofence_groupid` (`groupid`),
  KEY `fk_group_geofence_geofenceid` (`geofenceid`),
  CONSTRAINT `fk_group_geofence_geofenceid` FOREIGN KEY (`geofenceid`) REFERENCES `geofences` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_group_geofence_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `group_notification` */

DROP TABLE IF EXISTS `group_notification`;

CREATE TABLE `group_notification` (
  `groupid` int(11) NOT NULL,
  `notificationid` int(11) NOT NULL,
  KEY `fk_group_notification_groupid` (`groupid`),
  KEY `fk_group_notification_notificationid` (`notificationid`),
  CONSTRAINT `fk_group_notification_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_group_notification_notificationid` FOREIGN KEY (`notificationid`) REFERENCES `notifications` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `groupmaster` */

DROP TABLE IF EXISTS `groupmaster`;

CREATE TABLE `groupmaster` (
  `GroupId` int(11) NOT NULL AUTO_INCREMENT,
  `cGroupCode` varchar(20) DEFAULT NULL,
  `cGroupDescription` varchar(50) DEFAULT NULL,
  `cUserId` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`GroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

/*Table structure for table `groups` */

DROP TABLE IF EXISTS `groups`;

CREATE TABLE `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `groupid` int(11) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_groups_groupid` (`groupid`),
  CONSTRAINT `fk_groups_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488000035369` */

DROP TABLE IF EXISTS `imei_355488000035369`;

CREATE TABLE `imei_355488000035369` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488000035377` */

DROP TABLE IF EXISTS `imei_355488000035377`;

CREATE TABLE `imei_355488000035377` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020105445` */

DROP TABLE IF EXISTS `imei_355488020105445`;

CREATE TABLE `imei_355488020105445` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020105459` */

DROP TABLE IF EXISTS `imei_355488020105459`;

CREATE TABLE `imei_355488020105459` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020105471` */

DROP TABLE IF EXISTS `imei_355488020105471`;

CREATE TABLE `imei_355488020105471` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020105471_testtraccar` */

DROP TABLE IF EXISTS `imei_355488020105471_testtraccar`;

CREATE TABLE `imei_355488020105471_testtraccar` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020827711` */

DROP TABLE IF EXISTS `imei_355488020827711`;

CREATE TABLE `imei_355488020827711` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020827845` */

DROP TABLE IF EXISTS `imei_355488020827845`;

CREATE TABLE `imei_355488020827845` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488020827902` */

DROP TABLE IF EXISTS `imei_355488020827902`;

CREATE TABLE `imei_355488020827902` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_355488488020105471` */

DROP TABLE IF EXISTS `imei_355488488020105471`;

CREATE TABLE `imei_355488488020105471` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_358511022242659` */

DROP TABLE IF EXISTS `imei_358511022242659`;

CREATE TABLE `imei_358511022242659` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_358511022242691` */

DROP TABLE IF EXISTS `imei_358511022242691`;

CREATE TABLE `imei_358511022242691` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_358511022242696` */

DROP TABLE IF EXISTS `imei_358511022242696`;

CREATE TABLE `imei_358511022242696` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_358511022242915` */

DROP TABLE IF EXISTS `imei_358511022242915`;

CREATE TABLE `imei_358511022242915` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_555555555555555` */

DROP TABLE IF EXISTS `imei_555555555555555`;

CREATE TABLE `imei_555555555555555` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_777777777777777` */

DROP TABLE IF EXISTS `imei_777777777777777`;

CREATE TABLE `imei_777777777777777` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035679547` */

DROP TABLE IF EXISTS `imei_864502035679547`;

CREATE TABLE `imei_864502035679547` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035679554` */

DROP TABLE IF EXISTS `imei_864502035679554`;

CREATE TABLE `imei_864502035679554` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035759315` */

DROP TABLE IF EXISTS `imei_864502035759315`;

CREATE TABLE `imei_864502035759315` (
  `DeviceId` int(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035760768` */

DROP TABLE IF EXISTS `imei_864502035760768`;

CREATE TABLE `imei_864502035760768` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035760990` */

DROP TABLE IF EXISTS `imei_864502035760990`;

CREATE TABLE `imei_864502035760990` (
  `DeviceId` int(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035822584` */

DROP TABLE IF EXISTS `imei_864502035822584`;

CREATE TABLE `imei_864502035822584` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035977297` */

DROP TABLE IF EXISTS `imei_864502035977297`;

CREATE TABLE `imei_864502035977297` (
  `DeviceId` int(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035977347` */

DROP TABLE IF EXISTS `imei_864502035977347`;

CREATE TABLE `imei_864502035977347` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502035978170` */

DROP TABLE IF EXISTS `imei_864502035978170`;

CREATE TABLE `imei_864502035978170` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502036023018` */

DROP TABLE IF EXISTS `imei_864502036023018`;

CREATE TABLE `imei_864502036023018` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_864502036080125` */

DROP TABLE IF EXISTS `imei_864502036080125`;

CREATE TABLE `imei_864502036080125` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034455840` */

DROP TABLE IF EXISTS `imei_867322034455840`;

CREATE TABLE `imei_867322034455840` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034455865` */

DROP TABLE IF EXISTS `imei_867322034455865`;

CREATE TABLE `imei_867322034455865` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034508713` */

DROP TABLE IF EXISTS `imei_867322034508713`;

CREATE TABLE `imei_867322034508713` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034508895` */

DROP TABLE IF EXISTS `imei_867322034508895`;

CREATE TABLE `imei_867322034508895` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034509125` */

DROP TABLE IF EXISTS `imei_867322034509125`;

CREATE TABLE `imei_867322034509125` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034538421` */

DROP TABLE IF EXISTS `imei_867322034538421`;

CREATE TABLE `imei_867322034538421` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034576033` */

DROP TABLE IF EXISTS `imei_867322034576033`;

CREATE TABLE `imei_867322034576033` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034614537` */

DROP TABLE IF EXISTS `imei_867322034614537`;

CREATE TABLE `imei_867322034614537` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034677443` */

DROP TABLE IF EXISTS `imei_867322034677443`;

CREATE TABLE `imei_867322034677443` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_867322034677476` */

DROP TABLE IF EXISTS `imei_867322034677476`;

CREATE TABLE `imei_867322034677476` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996034965749` */

DROP TABLE IF EXISTS `imei_868996034965749`;

CREATE TABLE `imei_868996034965749` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035357284` */

DROP TABLE IF EXISTS `imei_868996035357284`;

CREATE TABLE `imei_868996035357284` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035384387` */

DROP TABLE IF EXISTS `imei_868996035384387`;

CREATE TABLE `imei_868996035384387` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035384445` */

DROP TABLE IF EXISTS `imei_868996035384445`;

CREATE TABLE `imei_868996035384445` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035384593` */

DROP TABLE IF EXISTS `imei_868996035384593`;

CREATE TABLE `imei_868996035384593` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035385384` */

DROP TABLE IF EXISTS `imei_868996035385384`;

CREATE TABLE `imei_868996035385384` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035385533` */

DROP TABLE IF EXISTS `imei_868996035385533`;

CREATE TABLE `imei_868996035385533` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035385632` */

DROP TABLE IF EXISTS `imei_868996035385632`;

CREATE TABLE `imei_868996035385632` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035386630` */

DROP TABLE IF EXISTS `imei_868996035386630`;

CREATE TABLE `imei_868996035386630` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035387463` */

DROP TABLE IF EXISTS `imei_868996035387463`;

CREATE TABLE `imei_868996035387463` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035387836` */

DROP TABLE IF EXISTS `imei_868996035387836`;

CREATE TABLE `imei_868996035387836` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035395987` */

DROP TABLE IF EXISTS `imei_868996035395987`;

CREATE TABLE `imei_868996035395987` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035397223` */

DROP TABLE IF EXISTS `imei_868996035397223`;

CREATE TABLE `imei_868996035397223` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT '0',
  `nGeostat` int(11) DEFAULT '0',
  `cGeolocation` varchar(3000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035397223_testtraccar` */

DROP TABLE IF EXISTS `imei_868996035397223_testtraccar`;

CREATE TABLE `imei_868996035397223_testtraccar` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035397322` */

DROP TABLE IF EXISTS `imei_868996035397322`;

CREATE TABLE `imei_868996035397322` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035397397` */

DROP TABLE IF EXISTS `imei_868996035397397`;

CREATE TABLE `imei_868996035397397` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035397397_testtraccar` */

DROP TABLE IF EXISTS `imei_868996035397397_testtraccar`;

CREATE TABLE `imei_868996035397397_testtraccar` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_868996035397728` */

DROP TABLE IF EXISTS `imei_868996035397728`;

CREATE TABLE `imei_868996035397728` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL,
  `nOverSpeedStat` int(11) DEFAULT NULL,
  `nGeostat` int(11) DEFAULT NULL,
  `cGeolocation` varchar(3000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `imei_989898989898` */

DROP TABLE IF EXISTS `imei_989898989898`;

CREATE TABLE `imei_989898989898` (
  `DeviceId` bigint(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLStatus` int(11) DEFAULT NULL,
  `satposition` int(11) DEFAULT NULL,
  `satlength` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `mobileuserregister` */

DROP TABLE IF EXISTS `mobileuserregister`;

CREATE TABLE `mobileuserregister` (
  `CustomerId` int(11) NOT NULL AUTO_INCREMENT,
  `cUsername` varchar(60) DEFAULT NULL,
  `cPassword` varchar(60) DEFAULT NULL,
  `cConfirmpassword` varchar(60) DEFAULT NULL,
  `Mobileno` bigint(11) DEFAULT NULL,
  `cMobilebrand` varchar(60) DEFAULT NULL,
  `cMacaddress` varchar(60) DEFAULT NULL,
  `regstat` int(11) DEFAULT NULL,
  `login_stat` int(11) DEFAULT NULL,
  `adminset_ok` int(11) DEFAULT NULL,
  UNIQUE KEY `cMobileCustomerId` (`CustomerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `mobilevehicledetails` */

DROP TABLE IF EXISTS `mobilevehicledetails`;

CREATE TABLE `mobilevehicledetails` (
  `CustomerId` int(11) DEFAULT NULL,
  `DeviceId` int(11) DEFAULT NULL,
  `imei` varchar(60) DEFAULT NULL,
  `cVehicleno` varchar(60) DEFAULT NULL,
  `cVehicleModel` varchar(60) DEFAULT NULL,
  `adminno` bigint(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `modelmaster` */

DROP TABLE IF EXISTS `modelmaster`;

CREATE TABLE `modelmaster` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cModelCode` varchar(20) NOT NULL,
  `cModelName` varchar(100) DEFAULT NULL,
  `cDefaultModel` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`cModelCode`),
  UNIQUE KEY `Id` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

/*Table structure for table `notifications` */

DROP TABLE IF EXISTS `notifications`;

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(128) NOT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `web` bit(1) DEFAULT b'0',
  `mail` bit(1) DEFAULT b'0',
  `sms` bit(1) DEFAULT b'0',
  `always` bit(1) NOT NULL DEFAULT b'0',
  `calendarid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notification_calendar_calendarid` (`calendarid`),
  CONSTRAINT `fk_notification_calendar_calendarid` FOREIGN KEY (`calendarid`) REFERENCES `calendars` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `overspeedalarmdetails` */

DROP TABLE IF EXISTS `overspeedalarmdetails`;

CREATE TABLE `overspeedalarmdetails` (
  `DeviceId` int(11) DEFAULT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `dtDate` datetime DEFAULT NULL,
  `dtTime` datetime DEFAULT NULL,
  `nSpeed` int(11) DEFAULT NULL,
  `nStatus` int(11) DEFAULT NULL,
  `nTabStatus` int(11) DEFAULT NULL,
  `nTabnotification` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `positions` */

DROP TABLE IF EXISTS `positions`;

CREATE TABLE `positions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `protocol` varchar(128) DEFAULT NULL,
  `deviceid` int(11) NOT NULL,
  `servertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `devicetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fixtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `valid` bit(1) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `altitude` float NOT NULL,
  `speed` float NOT NULL,
  `course` float NOT NULL,
  `address` varchar(512) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `accuracy` double NOT NULL DEFAULT '0',
  `network` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `position_deviceid_fixtime` (`deviceid`,`fixtime`),
  CONSTRAINT `fk_position_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5595781 DEFAULT CHARSET=latin1;

/*Table structure for table `positions_backup13072018` */

DROP TABLE IF EXISTS `positions_backup13072018`;

CREATE TABLE `positions_backup13072018` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `protocol` varchar(128) DEFAULT NULL,
  `deviceid` int(11) NOT NULL,
  `servertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `devicetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fixtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `valid` bit(1) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `altitude` float NOT NULL,
  `speed` float NOT NULL,
  `course` float NOT NULL,
  `address` varchar(512) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `accuracy` double NOT NULL DEFAULT '0',
  `network` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `position_deviceid_fixtime` (`deviceid`,`fixtime`)
) ENGINE=InnoDB AUTO_INCREMENT=50740 DEFAULT CHARSET=latin1;

/*Table structure for table `positions_copy090319` */

DROP TABLE IF EXISTS `positions_copy090319`;

CREATE TABLE `positions_copy090319` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `protocol` varchar(128) DEFAULT NULL,
  `deviceid` int(11) NOT NULL,
  `servertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `devicetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fixtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `valid` bit(1) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `altitude` float NOT NULL,
  `speed` float NOT NULL,
  `course` float NOT NULL,
  `address` varchar(512) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `accuracy` double NOT NULL DEFAULT '0',
  `network` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `position_deviceid_fixtime` (`deviceid`,`fixtime`)
) ENGINE=InnoDB AUTO_INCREMENT=4747865 DEFAULT CHARSET=latin1;

/*Table structure for table `positions_copy_19112018` */

DROP TABLE IF EXISTS `positions_copy_19112018`;

CREATE TABLE `positions_copy_19112018` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `protocol` varchar(128) DEFAULT NULL,
  `deviceid` int(11) NOT NULL,
  `servertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `devicetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fixtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `valid` bit(1) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `altitude` float NOT NULL,
  `speed` float NOT NULL,
  `course` float NOT NULL,
  `address` varchar(512) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `accuracy` double NOT NULL DEFAULT '0',
  `network` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `position_deviceid_fixtime` (`deviceid`,`fixtime`)
) ENGINE=InnoDB AUTO_INCREMENT=241196 DEFAULT CHARSET=latin1;

/*Table structure for table `query` */

DROP TABLE IF EXISTS `query`;

CREATE TABLE `query` (
  `SQL` varchar(5000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `servers` */

DROP TABLE IF EXISTS `servers`;

CREATE TABLE `servers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registration` bit(1) NOT NULL DEFAULT b'1',
  `latitude` double NOT NULL DEFAULT '0',
  `longitude` double NOT NULL DEFAULT '0',
  `zoom` int(11) NOT NULL DEFAULT '0',
  `map` varchar(128) DEFAULT NULL,
  `bingkey` varchar(128) DEFAULT NULL,
  `mapurl` varchar(512) DEFAULT NULL,
  `readonly` bit(1) NOT NULL DEFAULT b'0',
  `twelvehourformat` bit(1) NOT NULL DEFAULT b'0',
  `attributes` varchar(4000) DEFAULT NULL,
  `forcesettings` bit(1) NOT NULL DEFAULT b'0',
  `coordinateformat` varchar(128) DEFAULT NULL,
  `devicereadonly` bit(1) DEFAULT b'0',
  `limitcommands` bit(1) DEFAULT b'0',
  `poilayer` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `simtrack` */

DROP TABLE IF EXISTS `simtrack`;

CREATE TABLE `simtrack` (
  `cCustomerName` varchar(50) DEFAULT NULL,
  `cAddress` varchar(100) DEFAULT NULL,
  `SimNumber` bigint(20) DEFAULT NULL,
  `dtIssueDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `statistics` */

DROP TABLE IF EXISTS `statistics`;

CREATE TABLE `statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `capturetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `activeusers` int(11) NOT NULL DEFAULT '0',
  `activedevices` int(11) NOT NULL DEFAULT '0',
  `requests` int(11) NOT NULL DEFAULT '0',
  `messagesreceived` int(11) NOT NULL DEFAULT '0',
  `messagesstored` int(11) NOT NULL DEFAULT '0',
  `attributes` varchar(4000) NOT NULL,
  `mailsent` int(11) NOT NULL DEFAULT '0',
  `smssent` int(11) NOT NULL DEFAULT '0',
  `geocoderrequests` int(11) NOT NULL DEFAULT '0',
  `geolocationrequests` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=410 DEFAULT CHARSET=latin1;

/*Table structure for table `subgroupmaster` */

DROP TABLE IF EXISTS `subgroupmaster`;

CREATE TABLE `subgroupmaster` (
  `SubGroupId` int(11) NOT NULL AUTO_INCREMENT,
  `cSubGroupCode` varchar(20) NOT NULL,
  `cSubGroupName` varchar(50) DEFAULT NULL,
  `cGroupCode` varchar(20) DEFAULT NULL,
  `cUserId` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`cSubGroupCode`),
  UNIQUE KEY `SubGroupId` (`SubGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

/*Table structure for table `tabalarmsetting` */

DROP TABLE IF EXISTS `tabalarmsetting`;

CREATE TABLE `tabalarmsetting` (
  `cUserId` varchar(128) DEFAULT NULL,
  `nSos` int(11) DEFAULT NULL,
  `nOverSpeed` int(11) DEFAULT NULL,
  `nAcc` int(11) DEFAULT NULL,
  `nLowBattery` int(11) DEFAULT NULL,
  `nPowerCutoff` int(11) DEFAULT NULL,
  `nGeofence` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tempconsolidatedata` */

DROP TABLE IF EXISTS `tempconsolidatedata`;

CREATE TABLE `tempconsolidatedata` (
  `dtDate` datetime DEFAULT NULL,
  `cGroupCode` varchar(50) DEFAULT NULL,
  `ovid` int(11) DEFAULT NULL,
  `cOverSpeedLocation` varchar(1000) DEFAULT NULL,
  `dtOverSpeddtime` datetime DEFAULT NULL,
  `accid` int(11) DEFAULT NULL,
  `cAccLocation` varchar(1000) DEFAULT NULL,
  `dtAcctime` datetime DEFAULT NULL,
  `sosid` int(11) DEFAULT NULL,
  `cSOSLocation` varchar(1000) DEFAULT NULL,
  `dtSOStime` datetime DEFAULT NULL,
  `powerid` int(11) DEFAULT NULL,
  `cPowerCutOffLocation` varchar(1000) DEFAULT NULL,
  `dtPowerCuttime` datetime DEFAULT NULL,
  `lowerid` int(11) DEFAULT NULL,
  `cLowBatteryLocation` varchar(1000) DEFAULT NULL,
  `dtLowbatterytime` datetime DEFAULT NULL,
  `geoid` int(11) DEFAULT NULL,
  `cGeofenceLocation` varchar(1000) DEFAULT NULL,
  `dtGeotime` datetime DEFAULT NULL,
  `cIMEI` varchar(50) DEFAULT NULL,
  `cUserId` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tempdeviceposition` */

DROP TABLE IF EXISTS `tempdeviceposition`;

CREATE TABLE `tempdeviceposition` (
  `imei` bigint(20) NOT NULL,
  `Lattitude` double DEFAULT NULL,
  `Longitude` double DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `mileage` double DEFAULT NULL,
  `RFID` varchar(100) DEFAULT NULL,
  `fence` int(11) DEFAULT NULL,
  `dname` varchar(80) DEFAULT NULL,
  `subname` varchar(80) DEFAULT NULL,
  `destatus` varchar(20) DEFAULT NULL,
  `acc` varchar(10) DEFAULT NULL,
  `dcharge` varchar(10) DEFAULT NULL,
  `teralarm` varchar(50) DEFAULT NULL,
  `gtrack` varchar(20) DEFAULT NULL,
  `oilstatus` varchar(10) DEFAULT NULL,
  `voltage` varchar(30) DEFAULT NULL,
  `dsignal` int(11) DEFAULT NULL,
  `alarm` varchar(30) DEFAULT NULL,
  `allang` varchar(20) DEFAULT NULL,
  `ddegree` int(11) DEFAULT NULL,
  `dewlat` int(11) DEFAULT NULL,
  `dsnlat` int(11) DEFAULT NULL,
  `glocat` int(11) DEFAULT NULL,
  `greal` int(11) DEFAULT NULL,
  `GLstatus` int(11) DEFAULT NULL,
  `vstatus` int(11) DEFAULT NULL,
  `DeviceId` int(11) DEFAULT NULL,
  `toffcount` int(11) DEFAULT NULL,
  `nGsmStatus` int(11) DEFAULT NULL,
  KEY `NewIndex1` (`dname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tempdistancetravelled` */

DROP TABLE IF EXISTS `tempdistancetravelled`;

CREATE TABLE `tempdistancetravelled` (
  `cDeviceName` varchar(50) DEFAULT NULL,
  `cGroupDescription` varchar(20) DEFAULT NULL,
  `cdate` datetime DEFAULT NULL,
  `D1` varchar(50) DEFAULT NULL,
  `D2` varchar(50) DEFAULT NULL,
  `D3` varchar(50) DEFAULT NULL,
  `D4` varchar(50) DEFAULT NULL,
  `D5` varchar(50) DEFAULT NULL,
  `D6` varchar(50) DEFAULT NULL,
  `D7` varchar(50) DEFAULT NULL,
  `D8` varchar(50) DEFAULT NULL,
  `D9` varchar(50) DEFAULT NULL,
  `D10` varchar(50) DEFAULT NULL,
  `D11` varchar(50) DEFAULT NULL,
  `D12` varchar(50) DEFAULT NULL,
  `D13` varchar(50) DEFAULT NULL,
  `D14` varchar(50) DEFAULT NULL,
  `D15` varchar(50) DEFAULT NULL,
  `D16` varchar(50) DEFAULT NULL,
  `D17` varchar(50) DEFAULT NULL,
  `D18` varchar(50) DEFAULT NULL,
  `D19` varchar(50) DEFAULT NULL,
  `D20` varchar(50) DEFAULT NULL,
  `D21` varchar(50) DEFAULT NULL,
  `D22` varchar(50) DEFAULT NULL,
  `D23` varchar(50) DEFAULT NULL,
  `D24` varchar(50) DEFAULT NULL,
  `D25` varchar(50) DEFAULT NULL,
  `D26` varchar(50) DEFAULT NULL,
  `D27` varchar(50) DEFAULT NULL,
  `D28` varchar(50) DEFAULT NULL,
  `D29` varchar(50) DEFAULT NULL,
  `D30` varchar(50) DEFAULT NULL,
  `D31` varchar(50) DEFAULT NULL,
  `totaldistance` double DEFAULT NULL,
  `cUserId` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `temppositions` */

DROP TABLE IF EXISTS `temppositions`;

CREATE TABLE `temppositions` (
  `id` int(11) NOT NULL,
  `protocol` varchar(120) DEFAULT NULL,
  `deviceid` int(11) NOT NULL,
  `servertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `devicetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fixtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `valid` bit(1) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `altitude` float NOT NULL,
  `speed` float NOT NULL,
  `course` float NOT NULL,
  `address` varchar(512) DEFAULT NULL,
  `attributes` varchar(4000) DEFAULT NULL,
  `accuracy` double NOT NULL DEFAULT '0',
  `network` varchar(4000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tempuseddevices` */

DROP TABLE IF EXISTS `tempuseddevices`;

CREATE TABLE `tempuseddevices` (
  `DeviceId` int(11) DEFAULT NULL,
  `CustomerId` int(11) DEFAULT NULL,
  `cCustomerName` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `testinsert` */

DROP TABLE IF EXISTS `testinsert`;

CREATE TABLE `testinsert` (
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `tripmaster` */

DROP TABLE IF EXISTS `tripmaster`;

CREATE TABLE `tripmaster` (
  `VehicleId` int(11) DEFAULT NULL,
  `cStart` varchar(200) DEFAULT NULL,
  `cEnd` varchar(200) DEFAULT NULL,
  `dtDate` datetime DEFAULT NULL,
  `cBosRef` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_attribute` */

DROP TABLE IF EXISTS `user_attribute`;

CREATE TABLE `user_attribute` (
  `userid` int(11) NOT NULL,
  `attributeid` int(11) NOT NULL,
  KEY `fk_user_attribute_userid` (`userid`),
  KEY `fk_user_attribute_attributeid` (`attributeid`),
  CONSTRAINT `fk_user_attribute_attributeid` FOREIGN KEY (`attributeid`) REFERENCES `attributes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_attribute_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_calendar` */

DROP TABLE IF EXISTS `user_calendar`;

CREATE TABLE `user_calendar` (
  `userid` int(11) NOT NULL,
  `calendarid` int(11) NOT NULL,
  KEY `fk_user_calendar_userid` (`userid`),
  KEY `fk_user_calendar_calendarid` (`calendarid`),
  CONSTRAINT `fk_user_calendar_calendarid` FOREIGN KEY (`calendarid`) REFERENCES `calendars` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_calendar_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_command` */

DROP TABLE IF EXISTS `user_command`;

CREATE TABLE `user_command` (
  `userid` int(11) NOT NULL,
  `commandid` int(11) NOT NULL,
  KEY `fk_user_command_userid` (`userid`),
  KEY `fk_user_command_commandid` (`commandid`),
  CONSTRAINT `fk_user_command_commandid` FOREIGN KEY (`commandid`) REFERENCES `commands` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_command_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_device` */

DROP TABLE IF EXISTS `user_device`;

CREATE TABLE `user_device` (
  `userid` int(11) NOT NULL,
  `deviceid` int(11) NOT NULL,
  KEY `fk_user_device_deviceid` (`deviceid`),
  KEY `user_device_user_id` (`userid`),
  CONSTRAINT `fk_user_device_deviceid` FOREIGN KEY (`deviceid`) REFERENCES `devices` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_device_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_driver` */

DROP TABLE IF EXISTS `user_driver`;

CREATE TABLE `user_driver` (
  `userid` int(11) NOT NULL,
  `driverid` int(11) NOT NULL,
  KEY `fk_user_driver_userid` (`userid`),
  KEY `fk_user_driver_driverid` (`driverid`),
  CONSTRAINT `fk_user_driver_driverid` FOREIGN KEY (`driverid`) REFERENCES `drivers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_driver_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_geofence` */

DROP TABLE IF EXISTS `user_geofence`;

CREATE TABLE `user_geofence` (
  `userid` int(11) NOT NULL,
  `geofenceid` int(11) NOT NULL,
  KEY `fk_user_geofence_userid` (`userid`),
  KEY `fk_user_geofence_geofenceid` (`geofenceid`),
  CONSTRAINT `fk_user_geofence_geofenceid` FOREIGN KEY (`geofenceid`) REFERENCES `geofences` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_geofence_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_group` */

DROP TABLE IF EXISTS `user_group`;

CREATE TABLE `user_group` (
  `userid` int(11) NOT NULL,
  `groupid` int(11) NOT NULL,
  KEY `fk_user_group_userid` (`userid`),
  KEY `fk_user_group_groupid` (`groupid`),
  CONSTRAINT `fk_user_group_groupid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_group_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_notification` */

DROP TABLE IF EXISTS `user_notification`;

CREATE TABLE `user_notification` (
  `userid` int(11) NOT NULL,
  `notificationid` int(11) NOT NULL,
  KEY `fk_user_notification_userid` (`userid`),
  KEY `fk_user_notification_notificationid` (`notificationid`),
  CONSTRAINT `fk_user_notification_notificationid` FOREIGN KEY (`notificationid`) REFERENCES `notifications` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_notification_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `user_user` */

DROP TABLE IF EXISTS `user_user`;

CREATE TABLE `user_user` (
  `userid` int(11) NOT NULL,
  `manageduserid` int(11) NOT NULL,
  KEY `fk_user_user_userid` (`userid`),
  KEY `fk_user_user_manageduserid` (`manageduserid`),
  CONSTRAINT `fk_user_user_manageduserid` FOREIGN KEY (`manageduserid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_user_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `hashedpassword` varchar(128) DEFAULT NULL,
  `salt` varchar(128) DEFAULT NULL,
  `readonly` bit(1) NOT NULL DEFAULT b'0',
  `admin` bit(1) NOT NULL DEFAULT b'0',
  `map` varchar(128) DEFAULT NULL,
  `latitude` double NOT NULL DEFAULT '0',
  `longitude` double NOT NULL DEFAULT '0',
  `zoom` int(11) NOT NULL DEFAULT '0',
  `twelvehourformat` bit(1) NOT NULL DEFAULT b'0',
  `attributes` varchar(4000) DEFAULT NULL,
  `coordinateformat` varchar(128) DEFAULT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `expirationtime` timestamp NULL DEFAULT NULL,
  `devicelimit` int(11) DEFAULT '-1',
  `token` varchar(128) DEFAULT NULL,
  `userlimit` int(11) DEFAULT '0',
  `devicereadonly` bit(1) DEFAULT b'0',
  `phone` varchar(128) DEFAULT NULL,
  `limitcommands` bit(1) DEFAULT b'0',
  `login` varchar(128) DEFAULT NULL,
  `poilayer` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Table structure for table `vehicledetails` */

DROP TABLE IF EXISTS `vehicledetails`;

CREATE TABLE `vehicledetails` (
  `VehicleId` int(11) NOT NULL AUTO_INCREMENT,
  `cGroupCode` varchar(20) DEFAULT NULL,
  `DeviceId` int(11) DEFAULT NULL,
  `SimNumber` bigint(20) DEFAULT NULL,
  `cDeviceName` varchar(50) DEFAULT NULL,
  `cRegNumber` varchar(50) DEFAULT NULL,
  `cVehicleType` varchar(11) DEFAULT NULL,
  `nMaxSpeed` int(11) DEFAULT NULL,
  `nOverSpeedStatus` int(11) DEFAULT NULL,
  `nAccStatus` int(11) DEFAULT NULL,
  `nSOSStatus` int(11) DEFAULT NULL,
  `nLowBatteryStatus` int(11) DEFAULT NULL,
  `nPowerCutoffStatus` int(11) DEFAULT NULL,
  `nGeofenceStatus` int(11) DEFAULT NULL,
  `nDelay` int(11) DEFAULT NULL,
  `nTripStatus` int(11) DEFAULT NULL,
  PRIMARY KEY (`VehicleId`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;

/*Table structure for table `vehicletypemaster` */

DROP TABLE IF EXISTS `vehicletypemaster`;

CREATE TABLE `vehicletypemaster` (
  `VehicleId` int(11) NOT NULL AUTO_INCREMENT,
  `cVehicleType` varchar(20) DEFAULT NULL,
  `cSVG` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`VehicleId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/* Trigger structure for table `devicemaster` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `AddtoDevicePosition` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `AddtoDevicePosition` AFTER INSERT ON `devicemaster` FOR EACH ROW BEGIN
	SET @deviceid := (SELECT DeviceId   FROM  devicemaster  WHERE  cimei= NEW.cimei );	
	INSERT INTO deviceposition(imei,deviceid,lattitude,longitude,speed,vStatus,toffcount,ddegree) VALUES(NEW.cimei,@deviceid,0,0,0,0,1,0);
    END */$$


DELIMITER ;

/* Trigger structure for table `devicemaster` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `DropDeviceTable` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'%' */ /*!50003 TRIGGER `DropDeviceTable` AFTER DELETE ON `devicemaster` FOR EACH ROW BEGIN
	
	SET @deviceid := (SELECT cimei   FROM  devicemaster  WHERE  cimei= old.cimei );
	set @DeviceTablename= 'IMEI_' & @DeviceTablename;
	
    END */$$


DELIMITER ;

/* Procedure structure for procedure `InsertGPSData` */

/*!50003 DROP PROCEDURE IF EXISTS  `InsertGPSData` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertGPSData`(IN _deviceid INT)
BEGIN
		SET @gpsTableName := "testinsert";
		SET @s = CONCAT('INSERT INTO ', @gpsTableName, ' values (' , _deviceid , ')');
		PREPARE stmt1 FROM @s; 
		EXECUTE stmt1; 
		DEALLOCATE PREPARE stmt1; 		
    END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_InsertGPSData` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_InsertGPSData` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_InsertGPSData`(IN _deviceid INT)
BEGIN
		SET @gpsTableName := "QUERY";
		
		SET @sAN = CONCAT('INSERT INTO ', @gpsTableName , ' values(', _deviceid ,')' );
		CALL sp_InsertGPSDataiNSERT(_deviceid);
    END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_InsertGPSDataiNSERT` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_InsertGPSDataiNSERT` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_InsertGPSDataiNSERT`(IN _deviceid INT)
BEGIN
		SET @gpsTableName := "QUERY";
		
		SET @_QUERY = CONCAT('INSERT INTO ', @gpsTableName , ' values(', _deviceid ,')' );
		
		 PREPARE stmt2 FROM  @_QUERY;
		 EXECUTE stmt2;
		 DEALLOCATE PREPARE stmt2;
    END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
