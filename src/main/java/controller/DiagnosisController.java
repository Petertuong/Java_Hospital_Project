package controller;

import model.Treatment.Diagnosis;
import service.TreatmentService.DiagnosisService;
import dao.TreatmentDAO.DiagnosisDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/diagnosis/*")
public class DiagnosisController extends BaseController {

    private DiagnosisService diagnosisService;

    public DiagnosisController(){
        this.diagnosisService = new DiagnosisService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Diagnosis> listDiagnosis = diagnosisService.listDiagnosis();
                sendJson(resp, listDiagnosis);
            }else if (pathInfo.startsWith("/id/")){ //find by ID
                int id = parseaInt(pathInfo);
                Diagnosis diagnosis = diagnosisService.findDiagnosisByID(id);
                if (diagnosis != null){
                    sendJson(resp, diagnosis);
                } else{
                    sendError(resp, 404, "Diagnosis not found");
                } 
            }else{ //find by ssn
                String ssn = parseaString(pathInfo);
                ArrayList<Diagnosis> diagnosisByssn = diagnosisService.findDiagnosisBySSN(ssn);
                sendJson(resp, diagnosisByssn);
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Diagnosis newDiagnosis = readJson(req, Diagnosis.class);

            if (String.valueOf(newDiagnosis.getDiag_id())== null || String.valueOf(newDiagnosis.getDiag_id()).isEmpty()) {
                sendError(resp, 400, "Diagnosis id is required");
                return;
            }

            if (newDiagnosis.getPatient().getSSN() == null || newDiagnosis.getPatient().getSSN().isEmpty()) {
                sendError(resp, 400, "Diagnosis SSN is required");
                return;
            }


            Diagnosis p = diagnosisService.addDiagnosis(newDiagnosis);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Diagnosis created successfully");
            } else {
                sendError(resp, 400, "Failed to create diagnosis");
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
            Diagnosis existing = diagnosisService.findDiagnosisByID(id);

            if (existing == null) {
                sendError(resp, 404, "Diagnosis not found");
                return;
            }

            Diagnosis updates = readJson(req, Diagnosis.class);
            
            updates.setDiag_id(id); 

            int success = diagnosisService.changeResult(updates);

            if (success == 1) {
                sendJson(resp, "Diagnosis updated");
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
            int success = diagnosisService.deleteDiagnosis(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting diagnosis");
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
