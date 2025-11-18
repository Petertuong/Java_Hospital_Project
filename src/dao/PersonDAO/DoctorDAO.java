package dao.PersonDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Staff.Doctor;
import util.DBConnect;

public class DoctorDAO implements DAOInterface<Doctor, Integer> {


    @Override
    public Doctor create(Doctor t) {
        String sql = "INSERT INTO doctor (Fullname, Gender, phoneno, qualification, specialization) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int idx=1;
            ps.setString(idx++, t.getFullname());
            ps.setString(idx++, String.valueOf(t.getGender()));
            ps.setString(idx++, t.getPhoneNo());
            ps.setString(idx++, t.getQualification());
            ps.setString(idx, t.getSpecialization());

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
    public Integer update(Doctor t) {
       String sql = "UPDATE doctor " +
                    "SET Fullname = ?, PhoneNo = ?, Gender = ?, Qualification = ?, Specialization = ? " +
                    "WHERE doctor_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, t.getFullname());
            ps.setString(idx++, t.getPhoneNo());
            ps.setString(idx++, String.valueOf(t.getGender()));
            ps.setString(idx++, t.getQualification());
            ps.setString(idx++, t.getSpecialization());
            ps.setInt(idx, t.getSID());


            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) updated successfully!");
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer delete(Integer k) {
       String sql = "DELETE from doctor " +
                    "WHERE doctor_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, k);

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) deleted successfully!");
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public ArrayList<Doctor> selectAll() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * from doctor ";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Doctor d = MapperUtil.mapDoctor(rs);

                doctors.add(d);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return doctors;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Doctor selectById(Integer k) {

        Doctor d = new Doctor();
        String sql = "SELECT * from doctor WHERE doctor_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                d = MapperUtil.mapDoctor(rs);

            }
            
            System.out.println("Retrieved doctor with doctor_id = " + k + " successfully!");

            return d;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Doctor> selectByCondition(String condition) {
        ArrayList<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * from doctor " + 
                    "WHERE " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                
                Doctor d = MapperUtil.mapDoctor(rs);

                doctors.add(d);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return doctors;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
