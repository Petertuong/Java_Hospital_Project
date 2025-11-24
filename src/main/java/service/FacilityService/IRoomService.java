package  service.FacilityService;

import java.util.ArrayList;

import  model.Facility.Room;

public interface IRoomService {

    public Room addRoom(Room room);

    public Integer incrBedsavailable(Room room);

    public Integer setBedsavailablezero(Room room);

    public Integer deleteRoom(Integer roomno);

    public ArrayList<Room> listRoom();
    
    public Room findRoomByNo(Integer roomno);

    public ArrayList<Room> findRoomByBedsSmaller(Integer num);

    public ArrayList<Room> findRoomByBedsGreater(Integer num);

}
//bedsavaible is just the number of bed in a room
