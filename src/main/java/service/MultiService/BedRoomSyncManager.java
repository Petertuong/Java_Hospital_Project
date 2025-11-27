package service.MultiService;

import service.FacilityService.RoomService;
import util.DBConnect;
import java.sql.*;

public class BedRoomSyncManager {
    
    private RoomService roomService = new RoomService();
    
    /**
     * Updates room's beds available count based on actual bed data
     * Called automatically after any bed add/update/delete operation
     */
    public void updateRoomBedAvailability(int roomNo) {
        try (Connection conn = DBConnect.getConnection()) {
            // Count total beds in this room
            String countBedsSQL = "SELECT COUNT(*) as total_beds FROM Bed WHERE roomno = ?";
            PreparedStatement psCount = conn.prepareStatement(countBedsSQL);
            psCount.setInt(1, roomNo);
            ResultSet rsCount = psCount.executeQuery();
            
            int totalBeds = 0;
            if (rsCount.next()) {
                totalBeds = rsCount.getInt("total_beds");
            }
            
            // Count occupied beds in this room
            String countOccupiedSQL = "SELECT COUNT(*) as occupied_beds FROM Bed WHERE roomno = ? AND is_occupied = 1";
            PreparedStatement psOccupied = conn.prepareStatement(countOccupiedSQL);
            psOccupied.setInt(1, roomNo);
            ResultSet rsOccupied = psOccupied.executeQuery();
            
            int occupiedBeds = 0;
            if (rsOccupied.next()) {
                occupiedBeds = rsOccupied.getInt("occupied_beds");
            }
            
            // Calculate available beds
            int availableBeds = totalBeds - occupiedBeds;
            
            // Update room's beds available count
            String updateRoomSQL = "UPDATE Room SET bedsavailable = ? WHERE roomno = ?";
            PreparedStatement psUpdate = conn.prepareStatement(updateRoomSQL);
            psUpdate.setInt(1, availableBeds);
            psUpdate.setInt(2, roomNo);
            int updated = psUpdate.executeUpdate();
            
            if (updated > 0) {
                System.out.printf("Room %d bed availability updated: %d available (total: %d, occupied: %d)\n", 
                                roomNo, availableBeds, totalBeds, occupiedBeds);
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating room bed availability: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void updateAllRoomsBedAvailability() {
        try (Connection conn = DBConnect.getConnection()) {
            // Get all rooms
            String getRoomsSQL = "SELECT DISTINCT roomno FROM Room";
            PreparedStatement psRooms = conn.prepareStatement(getRoomsSQL);
            ResultSet rsRooms = psRooms.executeQuery();
            
            System.out.println("Updating bed availability for all rooms...");
            
            while (rsRooms.next()) {
                int roomNo = rsRooms.getInt("roomno");
                updateRoomBedAvailability(roomNo);
            }
            
            System.out.println("All room bed availabilities updated!");
            
        } catch (SQLException e) {
            System.err.println("Error updating all rooms bed availability: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public BedStats getRoomBedStats(int roomNo) {
        try (Connection conn = DBConnect.getConnection()) {
            String statsSQL = """
                SELECT 
                    COUNT(*) as total_beds,
                    SUM(CASE WHEN is_occupied = 1 THEN 1 ELSE 0 END) as occupied_beds,
                    SUM(CASE WHEN is_occupied = 0 THEN 1 ELSE 0 END) as available_beds
                FROM Bed 
                WHERE roomno = ?
            """;
            
            PreparedStatement ps = conn.prepareStatement(statsSQL);
            ps.setInt(1, roomNo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new BedStats(
                    rs.getInt("total_beds"),
                    rs.getInt("occupied_beds"),
                    rs.getInt("available_beds")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting room bed stats: " + e.getMessage());
        }
        
        return new BedStats(0, 0, 0);
    }
    
    
    public static class BedStats {
        public final int totalBeds;
        public final int occupiedBeds;
        public final int availableBeds;
        
        public BedStats(int total, int occupied, int available) {
            this.totalBeds = total;
            this.occupiedBeds = occupied;
            this.availableBeds = available;
        }
        
        @Override
        public String toString() {
            return String.format("Total: %d, Occupied: %d, Available: %d", 
                               totalBeds, occupiedBeds, availableBeds);
        }
    }
    
    
    public boolean validateRoomBedConsistency(int roomNo) {
        try (Connection conn = DBConnect.getConnection()) {
            // Get room's stored beds available
            String getRoomSQL = "SELECT bedsavailable FROM Room WHERE roomno = ?";
            PreparedStatement psRoom = conn.prepareStatement(getRoomSQL);
            psRoom.setInt(1, roomNo);
            ResultSet rsRoom = psRoom.executeQuery();
            
            if (rsRoom.next()) {
                int storedAvailable = rsRoom.getInt("bedsavailable");
                BedStats actualStats = getRoomBedStats(roomNo);
                
                boolean isConsistent = (storedAvailable == actualStats.availableBeds);
                
                if (!isConsistent) {
                    System.out.printf("Room %d inconsistency: stored=%d, actual=%d\n", 
                                    roomNo, storedAvailable, actualStats.availableBeds);
                }
                
                return isConsistent;
            }
            
        } catch (SQLException e) {
            System.err.println("Error validating room bed consistency: " + e.getMessage());
        }
        
        return false;
    }
}