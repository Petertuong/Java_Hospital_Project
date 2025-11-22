package controller;

import model.Staff.Doctor;
import service.PersonService.DoctorService;
import dao.PersonDAO.DoctorDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/doctors/*")
public class DoctorController extends BaseController {

    private DoctorService doctorService;

    public DoctorController(){
        this.doctorService = new DoctorService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Doctor> listDoctors = doctorService.listDoctor();
                sendJson(resp, listDoctors);
            }else if (pathInfo.startsWith("/id/")){ //find by id
                int id = parseaInt(pathInfo);
                Doctor doctor = doctorService.findDoctorByID(id);
                if (doctor != null){
                    sendJson(resp, doctor);
                } else{
                    sendError(resp, 404, "Doctor not found");
                }

            }else if(pathInfo.startsWith("/spec/")){ //find by specialization
                String spec = parseaString(pathInfo);
                ArrayList<Doctor> doctors = doctorService.findDoctorBySpecialization(spec);
                sendJson(resp, doctors);

            }else if(pathInfo.startsWith("/qual/")){ //find by qualification
                String qual = parseaString(pathInfo);
                ArrayList<Doctor> doctors = doctorService.findDoctorByQualification(qual);
                sendJson(resp, doctors);
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Doctor newDoctor = readJson(req, Doctor.class);

            if (newDoctor.getFullname() == null || newDoctor.getFullname().isEmpty()) {
                sendError(resp, 400, "Doctor Name is required");
                return;
            }

            if (String.valueOf(newDoctor.getSID()) == null || String.valueOf(newDoctor.getSID()).isEmpty()) {
                sendError(resp, 400, "Doctor SID is required");
                return;
            }

            if (!(newDoctor.getGender() == 'M'  || newDoctor.getGender() == 'F')) {
                sendError(resp, 400, "Doctor gender is undefined");
                return;
            }

            if (newDoctor.getPhoneNo() == null || newDoctor.getPhoneNo().isEmpty()) {
                sendError(resp, 400, "Doctor phone number is required");
                return;
            }

            Doctor p = doctorService.insertDoctor(newDoctor);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Doctor created successfully");
            } else {
                sendError(resp, 400, "Failed to create doctor");
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
            Doctor existing = doctorService.findDoctorByID(id);

            if (existing == null) {
                sendError(resp, 404, "Doctor not found");
                return;
            }

            Doctor updates = readJson(req, Doctor.class);
            
            updates.setSID(id); 

            int success = doctorService.updateDoctor(updates);

            if (success == 1) {
                sendJson(resp, "Doctor updated");
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
            int success = doctorService.deleteDoctor(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting doctor");
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
