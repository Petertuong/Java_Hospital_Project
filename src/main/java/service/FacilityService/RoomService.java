package  service.FacilityService;

import java.util.ArrayList;

import  dao.FacilityDAO.RoomDAO;
import  dao.FacilityDAO.IRoomDAO;
import  model.Facility.Room;
import  model.Facility.BedStats;
import  service.AbstractService;


public class RoomService extends AbstractService<RoomDAO> implements IRoomService {

    private RoomDAO roomdao;  
    private IRoomDAO syncDAO;

    public RoomService(){
		super();
		this.roomdao = createEntityDAO();
		this.syncDAO = this.roomdao; 
    }

    @Override
    public RoomDAO createEntityDAO() {
        return new RoomDAO();
    }
    
    
    @Override
    public Room addRoom(Room room) {
        return roomdao.create(room);
    }

    @Override
    public Integer incrBedsavailable(Room room) {
        room.incrBedsAvailable();
        return roomdao.update(room);
    }

    @Override
    public Integer setBedsavailablezero(Room room) {
        room.setBedsAvialabletozero();
        return roomdao.update(room);
    }

    @Override
    public Integer deleteRoom(Integer roomno) {
        return roomdao.delete(roomno);
    }

    @Override
    public Integer updateRoom(Room room) {
        return roomdao.update(room);
    }

    @Override
    public ArrayList<Room> listRoom() {
        return roomdao.selectAll();
    }

    @Override
    public Room findRoomByNo(Integer roomno) {
        return roomdao.selectById(roomno);
    }

    @Override
    public ArrayList<Room> findRoomByBedsSmaller(Integer num) {
        String condition = "BedsAvailable < " + num;
        return roomdao.selectByCondition(condition);
    }

    @Override
    public ArrayList<Room> findRoomByBedsGreater(Integer num) {
        String condition = "BedsAvailable > " + num;
        return roomdao.selectByCondition(condition);
    }

    //Khanh

    public void updateRoomBedAvailability(int roomNo) {
        Room room = roomdao.selectById(roomNo);
        if (room == null) {
            System.err.println("Cannot update bed availability: Room " + roomNo + " does not exist");
            return;
        }
        
        syncDAO.updateRoomBedAvailability(roomNo);
    }

 
    public void updateAllRoomsBedAvailability() {
        System.out.println("Updating bed availability for all rooms...");
        
        ArrayList<Integer> roomNumbers = syncDAO.getAllRoomNumbers();
        
        for (Integer roomNo : roomNumbers) {
            syncDAO.updateRoomBedAvailability(roomNo);
        }
        
        System.out.println("All room bed availabilities updated!");
    }

    
    public BedStats getRoomBedStats(int roomNo) {
        Room room = roomdao.selectById(roomNo);
        if (room == null) {
            System.err.println("Room " + roomNo + " does not exist");
            return new BedStats(0, 0, 0);
        }
        
        return syncDAO.getRoomBedStats(roomNo);
    }

    public boolean validateRoomBedConsistency(int roomNo) {
        Room room = roomdao.selectById(roomNo);
        if (room == null) {
            System.err.println("Room " + roomNo + " does not exist");
            return false;
        }
        
        return syncDAO.validateRoomBedConsistency(roomNo);
    }


    public boolean validateAllRoomsConsistency() {
        ArrayList<Integer> roomNumbers = syncDAO.getAllRoomNumbers();
        boolean allConsistent = true;
        
        for (Integer roomNo : roomNumbers) {
            if (!syncDAO.validateRoomBedConsistency(roomNo)) {
                allConsistent = false;
            }
        }
        
        if (allConsistent) {
            System.out.println("All rooms have consistent bed data");
        } else {
            System.out.println("Some rooms have inconsistent bed data - sync recommended");
        }
        
        return allConsistent;
    }
}
