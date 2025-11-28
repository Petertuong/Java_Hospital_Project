package util;

import java.sql.*;

public class DatabaseViewer {
    
    public static void main(String[] args) {
        System.out.println("========== HOSPITAL DATABASE VIEWER ==========\n");
        
        viewPatients();
        viewNurses();
        viewDoctors();
        viewBeds();
        viewMedicines();
        viewDiagnosis();
        viewPrescriptions();
        
        // Consistency check
        checkAdmissionConsistency();
    }
    
    public static void viewPatients() {
        System.out.println("PATIENTS TABLE:");
        System.out.println("SSN\t\tName\t\t\tStatus\t\tGender");
        System.out.println("----------------------------------------------------------------");
        
        String sql = "SELECT ssn, fullname, status, gender FROM Patient ORDER BY ssn";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("%s\t%s\t\t%s\t\t%s\n",
                    rs.getString("ssn"),
                    rs.getString("fullname"),
                    rs.getString("status") != null ? rs.getString("status") : "NULL",
                    rs.getString("gender")
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing patients: " + e.getMessage());
        }
    }
    
    public static void viewNurses() {
        System.out.println("NURSES TABLE:");
        System.out.println("Nurse_ID\tName\t\t\tPatients_in_charge");
        System.out.println("----------------------------------------");
        
        String sql = "SELECT nurse_id, fullname, patient_in_charge FROM Nurse ORDER BY nurse_id";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("%d\t\t%s\t\t%d\n",
                    rs.getInt("nurse_id"),
                    rs.getString("fullname"),
                    rs.getObject("patient_in_charge") != null ? rs.getInt("patient_in_charge") : 0
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing nurses: " + e.getMessage());
        }
    }
    
    public static void viewBeds() {
        System.out.println("BEDS TABLE:");
        System.out.println("BedNo\tRoom\tOccupied\tNurse_ID\tPatient_SSN");
        System.out.println("------------------------------------------------");
        
        String sql = "SELECT bedno, roomno, is_occupied, nurse_id, ssn FROM Bed ORDER BY bedno";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("%d\t%d\t%s\t\t%s\t\t%s\n",
                    rs.getInt("bedno"),
                    rs.getInt("roomno"),
                    rs.getBoolean("is_occupied") ? "YES" : "NO",
                    rs.getObject("nurse_id") != null ? rs.getString("nurse_id") : "NULL",
                    rs.getObject("ssn") != null ? rs.getString("ssn") : "NULL"
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing beds: " + e.getMessage());
        }
    }
    
    public static void viewDiagnosis() {
        System.out.println("DIAGNOSIS TABLE:");
        System.out.println("Diag_ID\tPatient_SSN\tResult");
        System.out.println("--------------------------------------------------------");
        
        String sql = "SELECT diag_id, ssn, result FROM Diagnosis ORDER BY diag_id";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String result = rs.getBytes("result") != null ? new String(rs.getBytes("result")) : "NULL";
                System.out.printf("%d\t%s\t\t%s\n",
                    rs.getInt("diag_id"),
                    rs.getString("ssn") != null ? rs.getString("ssn") : "NULL",
                    result
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing diagnosis: " + e.getMessage());
        }
    }
    
    public static void viewPrescriptions() {
        System.out.println("PRESCRIPTIONS TABLE:");
        System.out.println("Treatment_ID\tSSN\t\tDrug_ID\tDoctor_ID\tDosage/Day\tDays\tDescription");
        System.out.println("--------------------------------------------------------------------------------");
        
        String sql = "SELECT treatment_id, ssn, drug_id, doctor_id, dosage_per_day, number_of_day, description FROM Prescription ORDER BY treatment_id";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("%d\t\t%s\t%d\t%d\t\t%d\t\t%d\t%s\n",
                    rs.getInt("treatment_id"),
                    rs.getString("ssn") != null ? rs.getString("ssn") : "NULL",
                    rs.getObject("drug_id") != null ? rs.getInt("drug_id") : 0,
                    rs.getObject("doctor_id") != null ? rs.getInt("doctor_id") : 0,
                    rs.getObject("dosage_per_day") != null ? rs.getInt("dosage_per_day") : 0,
                    rs.getObject("number_of_day") != null ? rs.getInt("number_of_day") : 0,
                    rs.getString("description") != null ? rs.getString("description") : "NULL"
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing prescriptions: " + e.getMessage());
        }
    }
    
    public static void viewSummary() {
        System.out.println("DATABASE SUMMARY:");
        System.out.println("====================");
        
        String[] tables = {"Patient", "Nurse", "Doctor", "Bed", "Room", "Medicine", "Diagnosis", "Prescription"};
        
        for (String table : tables) {
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + table);
                 ResultSet rs = ps.executeQuery()) {
                
                if (rs.next()) {
                    System.out.printf("%s: %d records\n", table, rs.getInt(1));
                }
                
            } catch (SQLException e) {
                System.err.printf("Error counting %s: %s\n", table, e.getMessage());
            }
        }
        System.out.println();
    }
    
    public static void viewDoctors() {
        System.out.println("DOCTORS TABLE:");
        System.out.println("Doctor_ID\tName\t\t\tGender\tSpecialization");
        System.out.println("--------------------------------------------------------");
        
        String sql = "SELECT doctor_id, fullname, gender, specialization FROM Doctor ORDER BY doctor_id";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("%d\t\t%s\t\t%s\t%s\n",
                    rs.getInt("doctor_id"),
                    rs.getString("fullname"),
                    rs.getString("gender"),
                    rs.getString("specialization") != null ? rs.getString("specialization") : "NULL"
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing doctors: " + e.getMessage());
        }
    }
    
    public static void viewMedicines() {
        System.out.println("MEDICINES TABLE:");
        System.out.println("Drug_ID\tDrug_Name\t\tQuantity");
        System.out.println("----------------------------------------");
        
        String sql = "SELECT drug_id, drugname, quantity FROM Medicine ORDER BY drug_id";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                System.out.printf("%d\t%s\t\t\t%d\n",
                    rs.getInt("drug_id"),
                    rs.getString("drugname"),
                    rs.getObject("quantity") != null ? rs.getInt("quantity") : 0
                );
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing medicines: " + e.getMessage());
        }
    }
    
    public static void checkAdmissionConsistency() {
        System.out.println("ADMISSION CONSISTENCY CHECK:");
        System.out.println("===============================");
        
        // Check if admitted patients have beds assigned
        String sql1 = """
            SELECT p.ssn, p.fullname, p.status, b.bedno, b.nurse_id 
            FROM Patient p 
            LEFT JOIN bed b ON p.ssn = b.ssn 
            WHERE p.status = 'Admitted'
            """;
            
        System.out.println("\nAdmitted Patients and their Beds:");
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql1);
             ResultSet rs = ps.executeQuery()) {
            
            boolean hasAdmitted = false;
            while (rs.next()) {
                hasAdmitted = true;
                System.out.printf("Patient: %s (%s) - Status: %s - Bed: %s - Nurse: %s%n",
                    rs.getString("fullname"),
                    rs.getString("ssn"),
                    rs.getString("status"),
                    rs.getObject("bedno") != null ? rs.getInt("bedno") : "NO BED",
                    rs.getObject("nurse_id") != null ? rs.getInt("nurse_id") : "NO NURSE"
                );
            }
            if (!hasAdmitted) {
                System.out.println("No admitted patients found.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking consistency: " + e.getMessage());
        }
        
        // Check nurse workload
        String sql2 = """
            SELECT n.nurse_id, n.fullname, n.patient_in_charge, 
                   COUNT(b.ssn) as actual_patients
            FROM Nurse n 
            LEFT JOIN bed b ON n.nurse_id = b.nurse_id AND b.is_occupied = true
            GROUP BY n.nurse_id, n.fullname, n.patient_in_charge
            """;
            
        System.out.println("\nNurse Workload Verification:");
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int recorded = rs.getObject("patient_in_charge") != null ? rs.getInt("patient_in_charge") : 0;
                int actual = rs.getInt("actual_patients");
                String status = (recorded == actual) ? "OK" : "MISMATCH";
                
                System.out.printf("Nurse %s (ID: %d): Recorded=%d, Actual=%d %s%n",
                    rs.getString("fullname"),
                    rs.getInt("nurse_id"),
                    recorded,
                    actual,
                    status
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking nurse workload: " + e.getMessage());
        }
        System.out.println();
    }
}