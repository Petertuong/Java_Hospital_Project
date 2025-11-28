package  dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import  model.Facility.*;
import  model.Patients.Patient;
import  model.Staff.*;
import  model.Treatment.*;

public class MapperUtil {
    public static Patient mapPatient(ResultSet rs) throws SQLException {
        String ssn = rs.getString("ssn");
        String name = rs.getString("fullname");
        String address = rs.getString("address");
        String phoneno = rs.getString("phoneno");
        String _g = rs.getString("gender");
        char gender = (_g == null || _g.isEmpty()) ? 'U' : _g.charAt(0);
        String emergencyContact = rs.getString("emergency_contact");
        Date dob = rs.getDate("dob");
        String statusStr = rs.getString("status");
        Status status;
        if (statusStr == null) {
            status = Status.Null;
        } else {
            try {
                status = Status.valueOf(statusStr);
            } catch (IllegalArgumentException ex) {
                status = Arrays.stream(Status.values())
                        .filter(s -> s.toString().equalsIgnoreCase(statusStr))
                        .findFirst()
                        .orElse(Status.Null);
            }
        }

        Patient p = new Patient(name, phoneno, gender, ssn, dob, address, emergencyContact, status);

        return p;
    }

    public static Doctor mapDoctor(ResultSet rs) throws SQLException{
        int doctor_id = rs.getInt("doctor_id");
        String fullname = rs.getString("fullname");
        String _dg = rs.getString("gender");
        char gender = (_dg == null || _dg.isEmpty()) ? 'U' : _dg.charAt(0);
        String phoneno = rs.getString("phoneno");
        String qual = rs.getString("qualification");
        String spec = rs.getString("specialization");

        Doctor d = new Doctor(fullname, phoneno, gender, doctor_id, spec, qual);

        return d;
    }

    public static Nurse mapNurse(ResultSet rs) throws SQLException{
        int nurse_id = rs.getInt("nurse_id");
        String fullname = rs.getString("fullname");
        String _ng = rs.getString("gender");
        char gender = (_ng == null || _ng.isEmpty()) ? 'U' : _ng.charAt(0);
        String phoneno = rs.getString("phoneno");
        String spec = rs.getString("specialization");
        int p_incharge = rs.getInt("patient_in_charge");

        Nurse n = new Nurse(fullname, phoneno, gender, nurse_id, spec, p_incharge);

        return n;
    }

    public static Room mapRoom (ResultSet rs) throws SQLException{
        int roomno = rs.getInt("roomno");
        int BedsAvailable = rs.getInt("bedsavailable");

        Room r = new Room(roomno, BedsAvailable);

        return r;
    }
    //Khanh
    public static Bed mapBed(ResultSet rs) throws  SQLException{

            //room
            Room r = MapperUtil.mapRoom(rs);
            //bed
            int bedno = rs.getInt("bedno");
            boolean isOccupied = rs.getBoolean("is_occupied");
            
            //patient - check if exists
            Patient p = null;
            if (rs.getString("ssn") != null) {
                p = MapperUtil.mapPatient(rs);
            }
            
            //nurse - check if exists  
            Nurse n = null;
            if (rs.getObject("nurse_id") != null) {
                n = MapperUtil.mapNurse(rs);
            }

            Bed b = new Bed(r, bedno, isOccupied, p, n);

            return b;
    }
    
    public static Medicine mapMedicine(ResultSet rs) throws SQLException{
        String drugname = rs.getString("drugname");
        int drugid = rs.getInt("drug_id");
        int quantity = rs.getInt("quantity");

        Medicine m = new Medicine(drugid, drugname, quantity);

        return m;
    }

    public static Prescription mapPrescription(ResultSet rs) throws SQLException{
        String desc = rs.getString("description");
        int treatment_id = rs.getInt("treatment_id");
        int dpd = rs.getInt("dosage_per_day");
        int nod = rs.getInt("number_of_day");
        

        Patient p = MapperUtil.mapPatient(rs);
        Doctor d = MapperUtil.mapDoctor(rs);
        Medicine m = MapperUtil.mapMedicine(rs);

        Prescription pres = new Prescription(m, dpd, nod, treatment_id, p, d, desc);

        return pres;
    }

    public static Diagnosis mapDiagnosis(ResultSet rs) throws SQLException{
        int diag_id = rs.getInt("diag_id");
        String result = rs.getString("result");

        Patient p = MapperUtil.mapPatient(rs);

        Diagnosis d = new Diagnosis(diag_id, result, p);

        return d;

    }

}
