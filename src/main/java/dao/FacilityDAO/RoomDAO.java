package  dao.FacilityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import  dao.DAOInterface;
import  dao.MapperUtil;
import  model.Facility.Room;
import  model.Facility.BedStats;
import  util.DBConnect;

public class RoomDAO implements DAOInterface<Room, Integer>, IRoomDAO {

    @Override
    public Room create(Room t) {
        String sql = "INSERT INTO room (roomno, bedsavailable) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx=1;
            ps.setInt(idx++, t.getRoomNo());
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
    public Integer update(Room t) {
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


            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer delete(Integer roomno) {
       String sql = "DELETE from room " +
                    "WHERE roomno = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomno);

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
    public Room selectById(Integer k) {

        Room r = new Room();
        String sql = "SELECT * from room WHERE roomno = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, k);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
 
                r = MapperUtil.mapRoom(rs);

            }
            
            if(rs.wasNull()){
                return null;
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
        String sql = "SELECT * from room WHERE " + condition;
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
    
    // ===== SYNC-SPECIFIC DAO METHODS =====
    
    /**
     * Update room's bed availability based on actual bed count
     */
    public boolean updateRoomBedAvailability(int roomNo) {
        try (Connection conn = DBConnect.getConnection()) {
            // Count total beds and occupied beds for this room
            String countSQL = """
                SELECT 
                    COUNT(*) as total_beds,
                    SUM(CASE WHEN is_occupied = 1 THEN 1 ELSE 0 END) as occupied_beds
                FROM Bed 
                WHERE roomno = ?
            """;
            
            PreparedStatement psCount = conn.prepareStatement(countSQL);
            psCount.setInt(1, roomNo);
            ResultSet rsCount = psCount.executeQuery();
            
            if (rsCount.next()) {
                int totalBeds = rsCount.getInt("total_beds");
                int occupiedBeds = rsCount.getInt("occupied_beds");
                int availableBeds = totalBeds - occupiedBeds;
                
                // Update room's beds available
                String updateSQL = "UPDATE Room SET bedsavailable = ? WHERE roomno = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateSQL);
                psUpdate.setInt(1, availableBeds);
                psUpdate.setInt(2, roomNo);
                
                int updated = psUpdate.executeUpdate();
                if (updated > 0) {
                    System.out.printf("Room %d bed availability updated: %d available (total: %d, occupied: %d)\n", 
                                    roomNo, availableBeds, totalBeds, occupiedBeds);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating room bed availability for room " + roomNo + ": " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get bed statistics for a specific room
     */
    public BedStats getRoomBedStats(int roomNo) {
        try (Connection conn = DBConnect.getConnection()) {
            String statsSQL = """
                SELECT 
                    COUNT(*) as total_beds,
                    SUM(CASE WHEN is_occupied = 1 THEN 1 ELSE 0 END) as occupied_beds
                FROM Bed 
                WHERE roomno = ?
            """;
            
            PreparedStatement ps = conn.prepareStatement(statsSQL);
            ps.setInt(1, roomNo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int totalBeds = rs.getInt("total_beds");
                int occupiedBeds = rs.getInt("occupied_beds");
                int availableBeds = totalBeds - occupiedBeds;
                return new BedStats(totalBeds, occupiedBeds, availableBeds);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bed stats for room " + roomNo + ": " + e.getMessage());
        }
        return new BedStats(0, 0, 0);
    }
    
    /**
     * Get all room numbers for batch operations
     */
    public ArrayList<Integer> getAllRoomNumbers() {
        ArrayList<Integer> roomNumbers = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "SELECT roomno FROM Room ORDER BY roomno";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                roomNumbers.add(rs.getInt("roomno"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting room numbers: " + e.getMessage());
        }
        return roomNumbers;
    }
    
    /**
     * Validate room-bed consistency for a specific room
     */
    public boolean validateRoomBedConsistency(int roomNo) {
        try (Connection conn = DBConnect.getConnection()) {
            String validationSQL = """
                SELECT 
                    r.bedsavailable as stored_available,
                    (COUNT(b.bedno) - SUM(CASE WHEN b.is_occupied = 1 THEN 1 ELSE 0 END)) as calculated_available
                FROM Room r 
                LEFT JOIN Bed b ON r.roomno = b.roomno 
                WHERE r.roomno = ?
                GROUP BY r.roomno, r.bedsavailable
            """;
            
            PreparedStatement ps = conn.prepareStatement(validationSQL);
            ps.setInt(1, roomNo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int storedAvailable = rs.getInt("stored_available");
                int calculatedAvailable = rs.getInt("calculated_available");
                
                boolean isConsistent = (storedAvailable == calculatedAvailable);
                if (!isConsistent) {
                    System.out.printf("Room %d inconsistency: stored=%d, calculated=%d\n", 
                                    roomNo, storedAvailable, calculatedAvailable);
                }
                return isConsistent;
            }
        } catch (SQLException e) {
            System.err.println("Error validating room bed consistency for room " + roomNo + ": " + e.getMessage());
        }
        return false;
    }
}
