-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Aug 12, 2016 at 09:05 AM
-- Server version: 10.1.8-MariaDB
-- PHP Version: 5.6.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `facepal-database`
--

-- --------------------------------------------------------

--
-- Table structure for table `enhanced`
--

CREATE TABLE `enhanced` (
  `e_id` bigint(20) NOT NULL,
  `info_id` bigint(20) NOT NULL,
  `p_id` bigint(20) NOT NULL,
  `e_path` varchar(270) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `info`
--

CREATE TABLE `info` (
  `info_id` bigint(20) NOT NULL,
  `log_id` bigint(20) NOT NULL,
  `Admin_name` varchar(50) NOT NULL,
  `Email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `log_id` bigint(20) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `original`
--

CREATE TABLE `original` (
  `o_id` bigint(20) NOT NULL,
  `info_id` bigint(20) NOT NULL,
  `p_id` bigint(20) NOT NULL,
  `o_path` varchar(270) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

CREATE TABLE `person` (
  `p_id` bigint(20) NOT NULL,
  `info_id` bigint(20) NOT NULL,
  `person_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `enhanced`
--
ALTER TABLE `enhanced`
  ADD PRIMARY KEY (`e_id`),
  ADD KEY `info_id` (`info_id`),
  ADD KEY `p_id` (`p_id`);

--
-- Indexes for table `info`
--
ALTER TABLE `info`
  ADD PRIMARY KEY (`info_id`),
  ADD UNIQUE KEY `Email` (`Email`),
  ADD KEY `log_id` (`log_id`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`log_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `original`
--
ALTER TABLE `original`
  ADD PRIMARY KEY (`o_id`),
  ADD KEY `info_id` (`info_id`),
  ADD KEY `p_id` (`p_id`);

--
-- Indexes for table `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`p_id`),
  ADD KEY `info_id` (`info_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `enhanced`
--
ALTER TABLE `enhanced`
  MODIFY `e_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `info`
--
ALTER TABLE `info`
  MODIFY `info_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `log_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `original`
--
ALTER TABLE `original`
  MODIFY `o_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `person`
--
ALTER TABLE `person`
  MODIFY `p_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `enhanced`
--
ALTER TABLE `enhanced`
  ADD CONSTRAINT `enhanced_ibfk_1` FOREIGN KEY (`info_id`) REFERENCES `info` (`info_id`),
  ADD CONSTRAINT `enhanced_ibfk_2` FOREIGN KEY (`p_id`) REFERENCES `person` (`p_id`);

--
-- Constraints for table `info`
--
ALTER TABLE `info`
  ADD CONSTRAINT `info_ibfk_1` FOREIGN KEY (`log_id`) REFERENCES `login` (`log_id`);

--
-- Constraints for table `original`
--
ALTER TABLE `original`
  ADD CONSTRAINT `original_ibfk_1` FOREIGN KEY (`info_id`) REFERENCES `info` (`info_id`),
  ADD CONSTRAINT `original_ibfk_2` FOREIGN KEY (`p_id`) REFERENCES `person` (`p_id`);

--
-- Constraints for table `person`
--
ALTER TABLE `person`
  ADD CONSTRAINT `person_ibfk_1` FOREIGN KEY (`info_id`) REFERENCES `info` (`info_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
