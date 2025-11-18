package dao.TreatmentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Treatment.Prescription;
import util.DBConnect;

public class PrescriptionDAO implements DAOInterface<Prescription, Integer> {


    @Override
    public Prescription create(Prescription t) {
        String sql = "INSERT INTO prescription (description, doctor_id, dosage_per_day, drug_id, number_of_day, ssn) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int idx=1;

            ps.setString(idx++, t.getDescription());
            ps.setInt(idx++, t.getDoctor().getSID());
            ps.setInt(idx++, t.getDosagePerDay());
            ps.setInt(idx++, t.getMedicine().getDrugID());
            ps.setInt(idx++, t.getNumberOfDay());
            ps.setString(idx, t.getPatient().getSSN());

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) inserted successfully!");
            return t;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer update(Prescription t) {
       String sql = "UPDATE prescription " +
                    "SET description = ?, dosage_per_day = ?, drug_id = ?, number_of_day = ?" + 
                    "WHERE treatment_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, t.getDescription());
            ps.setInt(idx++, t.getDosagePerDay());
            ps.setInt(idx++, t.getMedicine().getDrugID());
            ps.setInt(idx++, t.getNumberOfDay());
            ps.setInt(idx, t.getTreatmentID());


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
       String sql = "DELETE from prescription " +
                    "WHERE treatment_id = ?";
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
    public ArrayList<Prescription> selectAll() {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * from prescription pr " +
                    "JOIN doctor d ON (d.doctor_id = pr.doctor_id)" +
                    "JOIN patient p ON (p.ssn = pr.ssn)" +
                    "JOIN medicine m ON (m.drug_id = pr.drug_id)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                
                Prescription pr = MapperUtil.mapPrescription(rs);

                prescriptions.add(pr);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return prescriptions;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Prescription selectById(Integer k) {

        Prescription pr = new Prescription();
        String sql = "SELECT * from prescription pr " +
                    "JOIN doctor d ON (d.doctor_id = pr.doctor_id)" +
                    "JOIN patient p ON (p.ssn = pr.ssn)" +
                    "JOIN medicine m ON (m.drug_id = pr.drug_id)" +
                    "WHERE treatment_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
 
                pr = MapperUtil.mapPrescription(rs);

            }
            
            System.out.println("Retrieved prescription with treatment_id = " + k + " successfully!");

            return pr;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Prescription> selectByCondition(String condition) {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * from prescription pr " +
                    "JOIN doctor d ON (d.doctor_id = pr.doctor_id)" +
                    "JOIN patient p ON (p.ssn = pr.ssn)" +
                    "JOIN medicine m ON (m.drug_id = pr.drug_id) " + 
                    "WHERE " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Prescription pr = MapperUtil.mapPrescription(rs);

                prescriptions.add(pr);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return prescriptions;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
