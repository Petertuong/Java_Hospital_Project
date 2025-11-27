DROP TABLE IF EXISTS Prescription;
DROP TABLE IF EXISTS Diagnosis;
DROP TABLE IF EXISTS Bed;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Medicine;
DROP TABLE IF EXISTS Doctor;
DROP TABLE IF EXISTS Nurse;
DROP TABLE IF EXISTS Patient;

CREATE TABLE Patient (
    ssn VARCHAR(12) PRIMARY KEY,
    fullname VARCHAR(50) UNIQUE,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M','F')),
    dob DATE,
    phoneno CHAR(9) UNIQUE,
    address VARCHAR(100),
    emergency_contact CHAR(9) UNIQUE,
    status VARCHAR(50),
    CONSTRAINT chk_contact_not_same CHECK (phoneno <> emergency_contact)
) ENGINE=InnoDB;

CREATE TABLE Nurse (
    nurse_id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(50) NOT NULL UNIQUE,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M','F')),
    specialization VARCHAR(50),
    patient_in_charge INT,
    phoneno CHAR(9) UNIQUE
) ENGINE=InnoDB;

CREATE TABLE Doctor (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(50) NOT NULL UNIQUE,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M','F')),
    specialization VARCHAR(50),
    qualification VARCHAR(50),
    phoneno CHAR(9) UNIQUE
) ENGINE=InnoDB;

CREATE TABLE Medicine (
    drug_id INT AUTO_INCREMENT PRIMARY KEY,
    drugname VARCHAR(50) NOT NULL UNIQUE,
    quantity INT
) ENGINE=InnoDB;

CREATE TABLE Room (
    roomno INT PRIMARY KEY,
    bedsavailable INT DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE Bed (
    bedno INT PRIMARY KEY,
    roomno INT NOT NULL,
    is_occupied BOOLEAN DEFAULT FALSE,
    ssn VARCHAR(12),
    nurse_id INT,
    CONSTRAINT fk_bed_patient FOREIGN KEY (ssn)
        REFERENCES Patient(ssn)
        ON DELETE SET NULL,
    CONSTRAINT fk_bed_nurse FOREIGN KEY (nurse_id)
        REFERENCES Nurse(nurse_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_bed_room FOREIGN KEY (roomno)
        REFERENCES Room(roomno)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Diagnosis (
    diag_id INT AUTO_INCREMENT PRIMARY KEY,
    ssn VARCHAR(12),
    result BLOB,
    CONSTRAINT fk_diag_patient FOREIGN KEY (ssn)
        REFERENCES Patient(ssn)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Prescription (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    drug_id INT,
    doctor_id INT,
    ssn VARCHAR(12),
    dosage_per_day INT,
    number_of_day INT,
    description VARCHAR(5000),
    CONSTRAINT fk_presc_patient FOREIGN KEY (ssn)
        REFERENCES Patient(ssn)
        ON DELETE CASCADE,
    CONSTRAINT fk_presc_medicine FOREIGN KEY (drug_id)
        REFERENCES Medicine(drug_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_presc_doctor FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id)
        ON DELETE SET NULL
) ENGINE=InnoDB;
