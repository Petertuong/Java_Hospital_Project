package controller;

import model.Facility.Room;
import service.FacilityService.RoomService;
import service.MultiService.BedRoomService;
import dao.FacilityDAO.RoomDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/room/*")
public class RoomController extends BaseController {

    private RoomService roomService;
    private BedRoomService bedroomService;

    public RoomController(){
        this.roomService = new RoomService();
        this.bedroomService = new BedRoomService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Room> listRoom = roomService.listRoom();
                sendJson(resp, listRoom);
            }else if (pathInfo.startsWith("/id/")){ //find by ID
                int id = parseaInt(pathInfo);
                Room room = roomService.findRoomByNo(id);
                if (room != null){
                    sendJson(resp, room);
                } else{
                    sendError(resp, 404, "Room not found");
                } 
            }else if(pathInfo.startsWith("/bedsmaller/")){ //find by number of beds available
                int bedsAvailable = parseaInt(pathInfo);
                ArrayList<Room> roomByBeds = roomService.findRoomByBedsSmaller(bedsAvailable);
                sendJson(resp, roomByBeds);
            }else{
                int bedsAvailable = parseaInt(pathInfo);
                ArrayList<Room> roomByBeds = roomService.findRoomByBedsGreater(bedsAvailable);
                sendJson(resp, roomByBeds);
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Room newRoom = readJson(req, Room.class);

            if (String.valueOf(newRoom.getRoomNo())== null || String.valueOf(newRoom.getRoomNo()).isEmpty()) {
                sendError(resp, 400, "Room#  is required");
                return;
            }

            if (newRoom.getBedsAvailable() != 0) {
                sendError(resp, 400, "Can't create room with beds != 0");
                return;
            }


            Room p = roomService.addRoom(newRoom);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Room created successfully");
            } else {
                sendError(resp, 400, "Failed to create room");
            }
        } catch (Exception e) {
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(resp, 400, "Room# is required");
                return;
            }

            int id = parseaInt(pathInfo);
            Room existing = roomService.findRoomByNo(id);

            if (existing == null) {
                sendError(resp, 404, "Room not found");
                return;
            }
            //no needs since bedsavailable is updated automatically
           // Room updates = readJson(req, Room.class);
            
            int success = bedroomService.updateBedsAvailable(id);

            if (success == 1) {
                sendJson(resp, "Room updated");
            } else {
                sendError(resp, 400, "Update failed");
            }

        } catch (Exception e) {
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(resp, 400, "id is required");
                return;
            }

            int id = parseaInt(pathInfo);
            int success = roomService.deleteRoom(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting room");
            }
        } catch (Exception e) {
            sendError(resp, 500, e.getMessage());
        }
    }

    private int parseaInt(String pathInfo){
        return Integer.parseInt(pathInfo.substring(1));
    }
    

}
