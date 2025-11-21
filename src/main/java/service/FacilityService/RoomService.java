package  service.FacilityService;

import java.util.ArrayList;

import  dao.FacilityDAO.RoomDAO;
import  model.Facility.Room;
import  service.AbstractService;

public class RoomService extends AbstractService<RoomDAO> implements IRoomService {

    RoomDAO roomdao;

    public RoomService(){
        super();
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
    public ArrayList<Room> listRoom() {
        return roomdao.selectAll();
    }

    @Override
    public Room findRoomByNo(Integer roomno) {
        return roomdao.selectById(roomno);
    }

    @Override
    public ArrayList<Room> findRoomByBeds(Integer num, boolean compare) {
        //if true compare: > num
        if(compare){
            String condition = "BedsAvailable > " + num;
            return roomdao.selectByCondition(condition);
        }else{//if false compare: < num
            String condition = "BedsAvailable < " + num;
            return roomdao.selectByCondition(condition);
        }
    }



}
