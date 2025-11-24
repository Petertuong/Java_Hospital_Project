package controller;

import model.Patients.Patient;
import service.MultiService.AdmissionService;
import service.PersonService.PatientService;
import dao.PersonDAO.PatientDAO;
import model.Treatment.Status;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/patients/*")
public class PatientController extends BaseController {

    private PatientService patientService;
    private AdmissionService admissionService;

    public PatientController(){
        this.patientService = new PatientService();
        this.admissionService = new AdmissionService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Patient> listPatients = patientService.listPatient();
                sendJson(resp, listPatients);
            }else if (pathInfo.startsWith("/age/")){ //find by age
                int age = parseaInt(pathInfo);
                ArrayList<Patient> patientsByAge = patientService.findPatientByAge(age);
                sendJson(resp, patientsByAge);

            }else if(pathInfo.startsWith("/status/")){ //find by status
                Status Status = model.Treatment.Status.valueOf(parseaString(pathInfo));
                ArrayList<Patient> patientsByStatus = patientService.findPatientByStatus(Status);
                sendJson(resp, patientsByStatus);

            }else { //find by id
                String ssn = parseaString(pathInfo);
                Patient patient = patientService.findPatientById(ssn);
                if (patient != null){
                    sendJson(resp, patient);
                } else{
                    sendError(resp, 404, "Patient not found");
                }
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Patient newPatient = readJson(req, Patient.class);

            if (newPatient.getFullname() == null || newPatient.getFullname().isEmpty()) {
                sendError(resp, 400, "Patient Name is required");
                return;
            }

            if (newPatient.getSSN() == null || newPatient.getSSN().isEmpty()) {
                sendError(resp, 400, "Patient SSN is required");
                return;
            }

            if (!(newPatient.getGender() == 'M'  || newPatient.getGender() == 'F')) {
                sendError(resp, 400, "Patient gender is undefined");
                return;
            }

            if (newPatient.getPhoneNo() == null || newPatient.getPhoneNo().isEmpty()) {
                sendError(resp, 400, "Patient phoen number is required");
                return;
            }

            Patient p = patientService.addPatient(newPatient);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Patient created successfully");
            } else {
                sendError(resp, 400, "Failed to create patient");
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
                sendError(resp, 400, "SSN is required");
                return;
            }

            String ssn = parseaString(pathInfo);
            Patient existing = patientService.findPatientById(ssn);

            if (existing == null) {
                sendError(resp, 404, "Patient not found");
                return;
            }

            Patient updates = readJson(req, Patient.class);
            
            updates.setSSN(ssn); 

            if(updates.getStatus() == "Admitted"){
                String success = admissionService.admitPatient(ssn);

                if (success.equals("Ok")) {
                    sendJson(resp, "Patient updated");
                } else {
                    sendError(resp, 400, "Update failed");
                }
            }else if(updates.getStatus() == "Discharged"){
                String success = admissionService.dischargePatient(ssn);

                if (success.equals("Ok")) {
                    sendJson(resp, "Patient updated");
                } else {
                    sendError(resp, 400, "Update failed");
                }
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
                sendError(resp, 400, "SSN is required");
                return;
            }

            String ssn = parseaString(pathInfo);
            String success = patientService.deletePatient(ssn);

            if (success.equals("Ok")) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting patient");
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
