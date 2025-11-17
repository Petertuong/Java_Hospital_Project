package dao.FacilityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Facility.Medicine;
import util.DBConnect;

public class MedicineDAO implements DAOInterface<Medicine, Integer> {

    public static MedicineDAO getInstance() {
        return new MedicineDAO();
    }
    
    @Override
    public Medicine create(Medicine t) {
        String sql = "INSERT INTO medicine (drugname, quantity) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int idx=1;
            ps.setString(idx++, t.getDrugName());
            ps.setInt(idx, t.getQuantity());

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
    public Medicine update(Medicine t) {
       String sql = "UPDATE medicine " +
                    "SET drugname = ?, quantity = ?" + 
                    "WHERE drug_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, t.getDrugName());
            ps.setInt(idx++, t.getQuantity());
            ps.setInt(idx, t.getDrugID());

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
    public Medicine delete(Medicine t) {
       String sql = "DELETE from medicine " +
                    "WHERE drug_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getDrugID());

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
    public ArrayList<Medicine> selectAll() {
        ArrayList<Medicine> meds = new ArrayList<>();
        String sql = "SELECT * from room ";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                
                Medicine m = MapperUtil.mapMedicine(rs);

                meds.add(m);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return meds;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Medicine selectById(Integer k) {

        Medicine m = new Medicine();
        String sql = "SELECT * from medicine WHERE drug_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
 
                m = MapperUtil.mapMedicine(rs);

            }
            
            System.out.println("Retrieved medicine with drug_id = " + k + " successfully!");

            return m;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Medicine> selectByCondition(String condition) {
        ArrayList<Medicine> meds = new ArrayList<>();
        String sql = "SELECT * from medicine " + 
                    "WHERE " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Medicine m = MapperUtil.mapMedicine(rs);

                meds.add(m);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return meds;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
