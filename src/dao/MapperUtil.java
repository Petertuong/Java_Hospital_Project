package dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Facility.*;
import model.Patients.Patient;
import model.Staff.*;
import model.Treatment.*;

public class MapperUtil {
    public static Patient mapPatient(ResultSet rs) throws SQLException {
        String ssn = rs.getString("ssn");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String phoneno = rs.getString("phoneno");
        char gender = rs.getString("gender").charAt(0);
        String emergencyContact = rs.getString("emergency_contact");
        Date dob = rs.getDate("dob");
        Status status = Status.valueOf(rs.getString("status"));


        Patient p = new Patient(name, phoneno, gender, ssn, dob, address, emergencyContact, status);

        return p;
    }

    public static Doctor mapDoctor(ResultSet rs) throws SQLException{
        int doctor_id = rs.getInt("doctor_id");
        String fullname = rs.getString("fullname");
        char gender = rs.getString("gender").charAt(0);
        String phoneno = rs.getString("phoneno");
        String qual = rs.getString("qualification");
        String spec = rs.getString("specialization");

        Doctor d = new Doctor(fullname, phoneno, gender, doctor_id, spec, qual);

        return d;
    }

    public static Nurse mapNurse(ResultSet rs) throws SQLException{
        int nurse_id = rs.getInt("nurse_id");
        String fullname = rs.getString("fullname");
        char gender = rs.getString("gender").charAt(0);
        String phoneno = rs.getString("phoneno");
        String spec = rs.getString("specialization");
        int p_incharge = rs.getInt("patient_in_charge");

        Nurse n = new Nurse(fullname, phoneno, gender, nurse_id, spec, p_incharge);

        return n;
    }

    public static Room mapRoom (ResultSet rs) throws SQLException{
        String roomno = rs.getString("roomno");
        int BedsAvailable = rs.getInt("bedsavailable");

        Room r = new Room(roomno, BedsAvailable);

        return r;
    }

    public static Bed mapBed(ResultSet rs) throws  SQLException{

            //room
            Room r = MapperUtil.mapRoom(rs);
            //bed
            String bedno = rs.getString("bedno");
            boolean isOccupied = rs.getBoolean("is_occupied");
            //patient
            Patient p = MapperUtil.mapPatient(rs);
            //nurse
            Nurse n = MapperUtil.mapNurse(rs);

            Bed b = new Bed(r, bedno, isOccupied, p, n);

            return b;
    }

    public static Medicine mapMedicine(ResultSet rs) throws SQLException{
        String drugname = rs.getString("DrugName");
        int drugid = rs.getInt("Drug_ID");
        int quantity = rs.getInt("Quantity");

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
