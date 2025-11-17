package dao.TreatmentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Treatment.Diagnosis;
import util.DBConnect;

public class DiagnosisDAO implements DAOInterface<Diagnosis, Integer>{

    public static DiagnosisDAO getInstance() {
        return new DiagnosisDAO();
    }
    
    @Override
    public Diagnosis create(Diagnosis t) {
        String sql = "INSERT INTO diagnosis (ssn, result) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int idx=1;
            ps.setString(idx++, t.getPatient().getSSN());
            ps.setString(idx, t.getResult());

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
    public Diagnosis update(Diagnosis t) {
       String sql = "UPDATE Diagnosis " +
                    "SET ssn = ?, result = ?" + 
                    "WHERE diag_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, t.getPatient().getSSN());
            ps.setString(idx++, t.getResult());
            ps.setInt(idx, t.getDiag_id());

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
    public Diagnosis delete(Diagnosis t) {
       String sql = "DELETE from diagnosis " +
                    "WHERE diag_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getDiag_id());

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) deleted successfully!");
            return t;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Diagnosis> selectAll() {
        ArrayList<Diagnosis> diags = new ArrayList<>();
        String sql = "SELECT * from diagnosis, patient "+
                    "WHERE diagnosis.ssn = patient.ssn";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                
                Diagnosis d = MapperUtil.mapDiagnosis(rs);

                diags.add(d);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return diags;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Diagnosis selectById(Integer k) {

        Diagnosis d = new Diagnosis();
         String sql = "SELECT * from diagnosis, patient "+
                    "WHERE diagnosis.ssn = patient.ssn AND diagnosis.diag_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
 
                d = MapperUtil.mapDiagnosis(rs);

            }
            
            System.out.println("Retrieved diagnosis with diag_id = " + k + " successfully!");

            return d;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Diagnosis> selectByCondition(String condition) {
        ArrayList<Diagnosis> diags = new ArrayList<>();
        String sql = "SELECT * from diagnosis, patient "+
                    "WHERE diagnosis.ssn = patient.ssn AND " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Diagnosis d = MapperUtil.mapDiagnosis(rs);

                diags.add(d);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return diags;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 
}
