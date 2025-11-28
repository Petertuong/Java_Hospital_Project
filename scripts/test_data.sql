-- Sample data for testing Hospital Management System
-- Clear existing data first (in correct order due to foreign keys)
DELETE FROM Prescription;
DELETE FROM Diagnosis;
DELETE FROM Bed;
DELETE FROM Room;
DELETE FROM Medicine;
DELETE FROM Doctor;
DELETE FROM Nurse;
DELETE FROM Patient;

-- Insert sample patients
INSERT INTO Patient (ssn, fullname, gender, dob, phoneno, address, emergency_contact, status) VALUES
('123456789001', 'Nguyễn Văn An', 'M', '1990-01-15', '091234567', '123 Đường Lê Lợi, Q1, HCM', '098765432', 'Waiting'),
('123456789002', 'Trần Thị Bình', 'F', '1985-05-20', '092345678', '456 Đường Nguyễn Huệ, Q1, HCM', '087654321', 'Waiting'),
('123456789003', 'Lê Văn Cường', 'M', '1978-12-10', '093456789', '789 Đường Hai Bà Trưng, Q3, HCM', '076543210', 'Waiting'),
('123456789004', 'Phạm Thị Dung', 'F', '1992-08-03', '094567890', '321 Đường Võ Văn Tần, Q1, HCM', '065432109', 'Discharged'),
('123456789005', 'Hoàng Văn Em', 'M', '1988-03-25', '095678901', '654 Đường Pasteur, Q3, HCM', '054321098', 'Waiting');

-- Insert sample nurses
INSERT INTO Nurse (fullname, gender, specialization, patient_in_charge, phoneno) VALUES
('Y tá Nguyễn Thị Hoa', 'F', 'Nội Khoa', 0, '081111111'),
('Y tá Trần Văn Khoa', 'M', 'Ngoại Khoa', 0, '082222222'),
('Y tá Lê Thị Lan', 'F', 'Nhi Khoa', 0, '083333333'),
('Y tá Phạm Văn Minh', 'M', 'Cấp Cứu', 0, '084444444');

-- Insert sample doctors
INSERT INTO Doctor (fullname, gender, specialization, qualification, phoneno) VALUES
('BS. Nguyễn Văn Nam', 'M', 'Tim Mạch', 'Thạc sĩ Y khoa', '071111111'),
('BS. Trần Thị Oanh', 'F', 'Nhi Khoa', 'Tiến sĩ Y khoa', '072222222'),
('BS. Lê Văn Phúc', 'M', 'Ngoại Khoa', 'Thạc sĩ Y khoa', '073333333'),
('BS. Phạm Thị Quỳnh', 'F', 'Da Liễu', 'Chuyên khoa I', '074444444');

-- Insert sample medicines
INSERT INTO Medicine (drugname, quantity) VALUES
('Paracetamol 500mg', 1000),
('Amoxicillin 250mg', 500),
('Ibuprofen 400mg', 750),
('Aspirin 100mg', 800),
('Vitamin C 1000mg', 1200);

-- Insert sample rooms
INSERT INTO Room (roomno, bedsavailable) VALUES
(101, 2),
(102, 2), 
(103, 1),
(201, 2),
(202, 1);

-- Insert sample beds (all available for admission testing)
INSERT INTO Bed (bedno, roomno, is_occupied, ssn, nurse_id) VALUES
(1001, 101, FALSE, NULL, NULL),
(1002, 101, FALSE, NULL, NULL),
(1003, 102, FALSE, NULL, NULL),
(1004, 102, FALSE, NULL, NULL),
(1005, 103, FALSE, NULL, NULL),
(2001, 201, FALSE, NULL, NULL),
(2002, 201, FALSE, NULL, NULL),
(2003, 202, FALSE, NULL, NULL);

-- Insert some sample diagnoses
INSERT INTO Diagnosis (ssn, result) VALUES
('123456789002', 'Cảm cúm thông thường, cần nghỉ ngơi và uống nhiều nước'),
('123456789004', 'Đau dạ dày nhẹ, đã khỏi và xuất viện');

-- Insert some sample prescriptions  
INSERT INTO Prescription (drug_id, doctor_id, ssn, dosage_per_day, number_of_day, description) VALUES
((SELECT drug_id FROM Medicine WHERE drugname = 'Paracetamol 500mg'), 
 (SELECT doctor_id FROM Doctor WHERE fullname = 'BS. Nguyễn Văn Nam'), 
 '123456789002', 2, 5, 'Uống 1 viên/lần, ngày 2 lần sau ăn'),
((SELECT drug_id FROM Medicine WHERE drugname = 'Amoxicillin 250mg'), 
 (SELECT doctor_id FROM Doctor WHERE fullname = 'BS. Trần Thị Oanh'), 
 '123456789003', 3, 7, 'Uống 1 viên/lần, ngày 3 lần trước ăn'),
((SELECT drug_id FROM Medicine WHERE drugname = 'Ibuprofen 400mg'), 
 (SELECT doctor_id FROM Doctor WHERE fullname = 'BS. Nguyễn Văn Nam'), 
 '123456789005', 2, 3, 'Uống khi đau, tối đa 2 lần/ngày');