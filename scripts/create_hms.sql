-- Create database and application user for Java_Hospital_Project
CREATE DATABASE IF NOT EXISTS hms CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER IF NOT EXISTS 'hmsuser'@'localhost' IDENTIFIED BY 'hms_password';
GRANT ALL PRIVILEGES ON hms.* TO 'hmsuser'@'localhost';
FLUSH PRIVILEGES;
