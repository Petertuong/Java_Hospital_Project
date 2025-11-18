package dao.FacilityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Facility.Bed;
import util.DBConnect;


public class BedDAO implements DAOInterface<Bed, Integer> {

    public static BedDAO getInstance() {
        return new BedDAO();
    }
    
    @Override
    public Bed create(Bed t) {
        String sql = "INSERT INTO bed (roomno, bedno) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx=1;
            ps.setString(idx++, t.getRoom().getRoomNo());
            ps.setInt(idx, t.getBedNo());

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
    public Integer update(Bed t) {
       String sql = "UPDATE bed " +
                    "SET is_occupied = ?, nurse_id = ?, ssn = ?" + 
                    "WHERE bedno = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if(t.isOccupied()){
                ps.setBoolean(idx++, true);
                ps.setInt(idx++, t.getNurse().getSID());
                ps.setString(idx++, t.getPatient().getSSN());
            }else{
                ps.setBoolean(idx++, false);
                ps.setNull(idx++, java.sql.Types.INTEGER);
                ps.setNull(idx, java.sql.Types.VARCHAR);
            }


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
       String sql = "DELETE from bed " +
                    "WHERE bedno = ?";
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
    public ArrayList<Bed> selectAll() {
        ArrayList<Bed> beds = new ArrayList<>();
        String sql = "SELECT * from bed b" +
                    "LEFT OUTER JOIN patient p ON (b.ssn = p.ssn)" +
                    "LEFT OUTER JOIN nurse n ON (b.nurse_id = n.nurse_id)" +
                    "JOIN room r ON (b.roomno = r.roomno)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {
                
                Bed b = MapperUtil.mapBed(rs);

                beds.add(b);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return beds;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Bed selectById(Integer k) {

        Bed b = new Bed();
        String sql = "SELECT * from bed b" +
                    "LEFT OUTER JOIN patient p ON (b.ssn = p.ssn)" +
                    "LEFT OUTER JOIN nurse n ON (b.nurse_id = n.nurse_id)" +
                    "JOIN room r ON (b.roomno = r.roomno)" +
                    "WHERE bedno = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                b = MapperUtil.mapBed(rs);
            }
            
            System.out.println("Retrieved bed with bedno = " + k + " successfully!");

            return b;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Bed> selectByCondition(String condition) {
        ArrayList<Bed> beds = new ArrayList<>();
        String sql = "SELECT * from bed b" +
                    "LEFT OUTER JOIN patient p ON (b.ssn = p.ssn)" +
                    "LEFT OUTER JOIN nurse n ON (b.nurse_id = n.nurse_id)" +
                    "JOIN room r ON (b.roomno = r.roomno) " + 
                    "WHERE " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Bed b = MapperUtil.mapBed(rs);

                beds.add(b);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return beds;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
