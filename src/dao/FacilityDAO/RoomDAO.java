package dao.FacilityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.DAOInterface;
import dao.MapperUtil;
import model.Facility.Room;
import util.DBConnect;

public class RoomDAO implements DAOInterface<Room, String> {

    public static RoomDAO getInstance() {
        return new RoomDAO();
    }
    
    @Override
    public Room create(Room t) {
        String sql = "INSERT INTO room (roomno, bedsavailable) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx=1;
            ps.setString(idx++, t.getRoomNo());
            ps.setInt(idx, t.getBedsAvailable());

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
    public Room update(Room t) {
       String sql = "UPDATE room " +
                    "SET bedsavailable = ?" + 
                    "WHERE roomno= ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setInt(idx, t.getBedsAvailable());

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
    public Room delete(Room t) {
       String sql = "DELETE from room " +
                    "WHERE roomno = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getRoomNo());

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
    public ArrayList<Room> selectAll() {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = "SELECT * from room ";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Room r = MapperUtil.mapRoom(rs);

                rooms.add(r);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return rooms;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Room selectById(String k) {

        Room r = new Room();
        String sql = "SELECT * from room WHERE roomno = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
 
                r = MapperUtil.mapRoom(rs);

            }
            
            System.out.println("Retrieved room with roomno = " + k + " successfully!");

            return r;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Room> selectByCondition(String condition) {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = "SELECT * from doctor WHERE " + condition;
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            
            int count = 0;

            while (rs.next()) {

                Room r = MapperUtil.mapRoom(rs);

                rooms.add(r);

                ++count;
            }

            System.out.println(count + " row(s) retrieved!");

            return rooms;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
