package controller;

import model.Staff.Nurse;
import service.PersonService.NurseService;
import dao.PersonDAO.NurseDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/nurses/*")
public class NurseController extends BaseController {

    private NurseService nurseService;

    public NurseController(){
        this.nurseService = new NurseService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Nurse> listNurses = nurseService.listNurse();
                sendJson(resp, listNurses);
            }else if (pathInfo.startsWith("/id/")){ //find by id
                int id = parseaInt(pathInfo);
                Nurse nurse = nurseService.findNurseByID(id);
                if (nurse != null){
                    sendJson(resp, nurse);
                } else{
                    sendError(resp, 404, "Nurse not found");
                }

            }else if(pathInfo.startsWith("/gender/")){ //find by status
                char gender = parseaString(pathInfo).charAt(0);
                ArrayList<Nurse> nursesByGender = nurseService.findNurseByGender(gender);
                sendJson(resp, nursesByGender);

            }else if(pathInfo.startsWith("/pidmax/")){ //find by pidmax
                ArrayList<Nurse> nurses = nurseService.findNurseByPIDMax();
                sendJson(resp, nurses);
            }else{
                Nurse nurse = nurseService.getNurseByMinPID();
                if (nurse != null){
                    sendJson(resp, nurse);
                } else{
                    sendError(resp, 404, "Nurse not found");
                }
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Nurse newNurse = readJson(req, Nurse.class);

            if (newNurse.getFullname() == null || newNurse.getFullname().isEmpty()) {
                sendError(resp, 400, "Nurse Name is required");
                return;
            }

            if (String.valueOf(newNurse.getSID()) == null || String.valueOf(newNurse.getSID()).isEmpty()) {
                sendError(resp, 400, "Nurse SID is required");
                return;
            }

            if (!(newNurse.getGender() == 'M'  || newNurse.getGender() == 'F')) {
                sendError(resp, 400, "Nurse gender is undefined");
                return;
            }

            if (newNurse.getPhoneNo() == null || newNurse.getPhoneNo().isEmpty()) {
                sendError(resp, 400, "Nurse phone number is required");
                return;
            }

            if (newNurse.getPatient_in_charge() != 0) {
                sendError(resp, 400, "New Nurse cannot have patients assigned");
                return;
            }

            Nurse p = nurseService.insertNurse(newNurse);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Nurse created successfully");
            } else {
                sendError(resp, 400, "Failed to create nurse");
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
                sendError(resp, 400, "SID is required");
                return;
            }

            int id = parseaInt(pathInfo);
            Nurse existing = nurseService.findNurseByID(id);

            if (existing == null) {
                sendError(resp, 404, "Nurse not found");
                return;
            }

            String success = "Failed";
            // no nurse update functionality for now
            // Nurse updates = readJson(req, Nurse.class);
            
            // updates.setSID(id); 

            // String success = nurseService.updateNurseStatus(updates);

            if (success.equals("Ok")) {
                sendJson(resp, "Nurse updated");
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
                sendError(resp, 400, "SID is required");
                return;
            }

            int id = parseaInt(pathInfo);
            int success = nurseService.deleteNurse(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting nurse");
            }
        } catch (Exception e) {
            sendError(resp, 500, e.getMessage());
        }
    }

    private String parseaString(String pathInfo){
            return pathInfo.substring(1);
    }

    private int parseaInt(String pathInfo){
        return Integer.parseInt(pathInfo.substring(1));
    }
    
}
