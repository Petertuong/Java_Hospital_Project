package dao.PersonDAO;

import java.sql.*;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Patients.Patient;
import util.DBConnect;


public class PatientDAO implements DAOInterface<Patient, String> {


    @Override
    public Patient create(Patient t) {
        String sql = "INSERT INTO Patient (Fullname, PhoneNo, Gender, dob, address, emergency_contact, SSN, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getFullname());
            ps.setString(2, t.getPhoneNo());
            ps.setString(3, String.valueOf(t.getGender()));
            ps.setDate(4, t.getDOB());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getEmergencyContact());
            ps.setString(7, t.getSSN());
            ps.setString(8, t.getStatus());

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " rows inserted successfully!");
            return t;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Patient update(Patient t) {
       String sql = "UPDATE Patient " +
                    "Status = ? " +
                    "WHERE SSN = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getStatus());
            ps.setString(2, t.getSSN());

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) updated successfully!");
            return t;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Patient delete(String k) {
       String sql = "DELETE from Patient " +
                    "WHERE SSN = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, k);

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " rows deleted successfully!");
            return t;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Patient> selectAll() {
        ArrayList<Patient> patients = new ArrayList<>();
        String sql = "SELECT * from Patient ";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Patient p = MapperUtil.mapPatient(rs);

                patients.add(p);

                ++count;
            }

            System.out.println(count + " rows retrieved!");

            return patients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Patient selectById(String k) {

        Patient p = new Patient();
        String sql = "SELECT * from Patient WHERE SSN = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                p = MapperUtil.mapPatient(rs);
            }
            
            System.out.println("Retrieved patient with SSN = " + k + " successfully!");

            return p;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Patient> selectByCondition(String condition) {
        ArrayList<Patient> patients = new ArrayList<>();
        String sql = "SELECT * from Patient WHERE " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                
                Patient p = MapperUtil.mapPatient(rs);

                patients.add(p);

                ++count;
            }

            System.out.println(count + " rows retrieved!");

            return patients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
