package controller;

import model.Facility.Bed;
import service.FacilityService.BedService;
import dao.FacilityDAO.BedDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/bed/*")
public class BedController extends BaseController {

    private BedService bedService;

    public BedController(){
        this.bedService = new BedService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Bed> listBed = bedService.listBed();
                sendJson(resp, listBed);
            }else if (pathInfo.startsWith("/id/")){ //find by ID
                int id = parseaInt(pathInfo);
                Bed bed = bedService.findBedByNo(id);
                if (bed != null){
                    sendJson(resp, bed);
                } else{
                    sendError(resp, 404, "Bed not found");
                } 
            }else if(pathInfo.startsWith("/roomid/")){ //find by roomno
                int roomno = parseaInt(pathInfo);
                ArrayList<Bed> beds = bedService.findBedByRoomNo(roomno);
                sendJson(resp, beds);
            }else if(pathInfo.startsWith("/ssn/")){ //find by ssn
                int SSN = parseaInt(pathInfo);
                ArrayList<Bed> beds = bedService.findBedBySSN(SSN);
                sendJson(resp, beds);
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Bed newBed = readJson(req, Bed.class);

            if (String.valueOf(newBed.getBedNo())== null || String.valueOf(newBed.getBedNo()).isEmpty()) {
                sendError(resp, 400, "Bed#  is required");
                return;
            }

            if (String.valueOf(newBed.getRoom().getRoomNo())== null || String.valueOf(newBed.getRoom().getRoomNo()).isEmpty()) {
                sendError(resp, 400, "Room#  is required");
                return;
            }



            Bed p = bedService.addBed(newBed);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Bed created successfully");
            } else {
                sendError(resp, 400, "Failed to create bed");
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
                sendError(resp, 400, "Bed# is required");
                return;
            }

            int id = parseaInt(pathInfo);
            Bed existing = bedService.findBedByNo(id);

            if (existing == null) {
                sendError(resp, 404, "Bed not found");
                return;
            }
           
            Bed updates = readJson(req, Bed.class);
            updates.setBedno(id);
            Bed success = bedService.updateBed(updates);

            if (success != null) {
                sendJson(resp, "Bed updated");
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
            int success = bedService.delete(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting bed");
            }
        } catch (Exception e) {
            sendError(resp, 500, e.getMessage());
        }
    }

    private int parseaInt(String pathInfo){
        return Integer.parseInt(pathInfo.substring(1));
    }
    

}
