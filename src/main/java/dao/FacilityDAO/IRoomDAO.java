package dao.FacilityDAO;

import java.util.ArrayList;
import model.Facility.BedStats;

//Khanh
public interface IRoomDAO {

    boolean updateRoomBedAvailability(int roomNo);
   
    BedStats getRoomBedStats(int roomNo);
    
    ArrayList<Integer> getAllRoomNumbers();
    
    boolean validateRoomBedConsistency(int roomNo);
}