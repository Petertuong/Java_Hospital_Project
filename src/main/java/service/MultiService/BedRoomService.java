package  service.MultiService;

import java.util.ArrayList;

import  model.Facility.Bed;
import  model.Facility.Room;
import  service.FacilityService.BedService;
import  service.FacilityService.RoomService;

public class BedRoomService {

    private BedService bedS;
    private RoomService roomS;
    
    public BedRoomService(){
        this.bedS = new BedService();
        this.roomS = new RoomService();
    }
    
    public int updateBedsAvailable(Integer roomno){
        
        Room room = roomS.findRoomByNo(roomno);

        ArrayList<Bed> allBeds = bedS.findBedByRoomNo(roomno);

        int count;
        roomS.setBedsavailablezero(room);
        for (count = 0; count <= allBeds.size(); count++){
            roomS.incrBedsavailable(room);
        }

        return count;
    }



}
