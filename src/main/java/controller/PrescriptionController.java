package controller;

import model.Treatment.Prescription;
import service.TreatmentService.PrescriptionService;
import dao.TreatmentDAO.PrescriptionDAO;
import service.MultiService.TreatmentProtocolService;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/prescriptions/*")
public class PrescriptionController extends BaseController {

    private PrescriptionService prescriptionService;
    private TreatmentProtocolService treatmentProtocolService;

    public PrescriptionController(){
        this.prescriptionService = new PrescriptionService();
        this.treatmentProtocolService = new TreatmentProtocolService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Prescription> listPrescriptions = prescriptionService.listPrescription();
                sendJson(resp, listPrescriptions);
            }else if (pathInfo.startsWith("/id/")){ //find by ID
                int id = parseaInt(pathInfo);
                Prescription prescription = prescriptionService.findPrescriptionByID(id);
                if (prescription != null){
                    sendJson(resp, prescription);
                } else{
                    sendError(resp, 404, "Prescription not found");
                } 
            }else{ //find by ssn
                String ssn = parseaString(pathInfo);
                ArrayList<Prescription> prescriptionByssn = prescriptionService.findPrescriptionBySSN(ssn);
                sendJson(resp, prescriptionByssn);
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Prescription newPrescription = readJson(req, Prescription.class);

            if (String.valueOf(newPrescription.getMedicine().getDrugID())== null || String.valueOf(newPrescription.getMedicine().getDrugID()).isEmpty()) {
                sendError(resp, 400, "Prescription drug id is required");
                return;
            }

            if (newPrescription.getPatient().getSSN() == null || newPrescription.getPatient().getSSN().isEmpty()) {
                sendError(resp, 400, "Prescription SSN is required");
                return;
            }

            if (String.valueOf(newPrescription.getTreatmentID()) == null  || String.valueOf(newPrescription.getTreatmentID()).isEmpty())) {
                sendError(resp, 400, "Prescription treatment id is required");
                return;
            }

            if (String.valueOf(newPrescription.getDosagePerDay()) == null || String.valueOf(newPrescription.getDosagePerDay()).isEmpty()) {
                sendError(resp, 400, "Prescription dosage per day is required");
                return;
            }
    
            if (String.valueOf(newPrescription.getNumberOfDay()) == null || String.valueOf(newPrescription.getNumberOfDay()).isEmpty()) {
                sendError(resp, 400, "Prescription number of day is required");
                return;
            }

            String s = treatmentProtocolService.prescribe(newPrescription);

            if (s.equals("Ok")) {
                resp.setStatus(201); 
                sendJson(resp, "Prescription created successfully");
            } else {
                sendError(resp, 400, "Failed to create prescription");
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
                sendError(resp, 400, "id is required");
                return;
            }

            int id = parseaInt(pathInfo);
            Prescription existing = prescriptionService.findPrescriptionByID(id);

            if (existing == null) {
                sendError(resp, 404, "Prescription not found");
                return;
            }

            Prescription updates = readJson(req, Prescription.class);
            
            updates.setTreatmentID(id); 

            int success = prescriptionService.changeDrug(updates);

            if (success == 1) {
                sendJson(resp, "Prescription updated");
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
            int success = prescriptionService.deletePrescription(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting prescription");
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
