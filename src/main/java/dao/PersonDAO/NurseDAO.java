package  dao.PersonDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import  dao.DAOInterface;
import  dao.MapperUtil;
import  model.Staff.Nurse;
import  util.DBConnect;

public class NurseDAO implements DAOInterface<Nurse, Integer> {

    @Override
    public Nurse create(Nurse t) {
        String sql = "INSERT INTO nurse (Fullname, Gender, phoneno, specialization) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int idx=1;
            ps.setString(idx++, t.getFullname());
            ps.setString(idx++, String.valueOf(t.getGender()));
            ps.setString(idx++, t.getPhoneNo());
            ps.setString(idx, t.getSpecialization());

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) inserted successfully!");

            DBConnect.closeConnection(conn);
            return t;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer update(Nurse t) {
       String sql = "UPDATE nurse " +
                    "SET Patient_in_charge = ?" +
                    "WHERE nurse_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setInt(idx++, t.getPatient_in_charge());
            ps.setInt(idx, t.getSID());


            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " row(s) updated successfully!");

            DBConnect.closeConnection(conn);
            return t.getPatient_in_charge();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public ArrayList<Nurse> selectAll() {
        ArrayList<Nurse> nurses = new ArrayList<>();
        String sql = "SELECT * from nurse ";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Nurse n = MapperUtil.mapNurse(rs);

                nurses.add(n);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            DBConnect.closeConnection(conn);
            return nurses;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Nurse selectById(Integer k) {

        Nurse n = new Nurse();

        String sql = "SELECT * from nurse WHERE nurse_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                n = MapperUtil.mapNurse(rs);
            }

            if(rs.wasNull()){
                return null;
            }
            
            System.out.println("Retrieved nurse with SID = " + k + " successfully!");

            DBConnect.closeConnection(conn);
            return n;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Nurse> selectByCondition(String condition) {
        ArrayList<Nurse> nurses = new ArrayList<>();
        String sql = "SELECT * from nurse " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                Nurse n = MapperUtil.mapNurse(rs);

                nurses.add(n);

                ++count;
            }

            System.out.println(count + " rows(s) retrieved!");

            DBConnect.closeConnection(conn);
            return nurses;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer delete(Integer k) {
        String sql = "DELETE from Nurse " +
                    "WHERE nurse_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, k);

            int rows = ps.executeUpdate();

            if (rows == 0) return null;

            System.out.println(rows + " rows deleted successfully!");

            DBConnect.closeConnection(conn);
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
