package controller;

import model.Facility.Medicine;
import service.FacilityService.MedicineService;
import dao.FacilityDAO.MedicineDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@WebServlet("api/medicine/*")
public class MedicineController extends BaseController {

    private MedicineService medicineService;

    public MedicineController(){
        this.medicineService = new MedicineService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String pathInfo = req.getPathInfo();

        try{
            if (pathInfo == null || pathInfo.equals("/")){
                ArrayList<Medicine> listMedicine = medicineService.listMedicine();
                sendJson(resp, listMedicine);
            }else if (pathInfo.startsWith("/id/")){ //find by ID
                int id = parseaInt(pathInfo);
                Medicine medicine = medicineService.findMedicineByNo(id);
                if (medicine != null){
                    sendJson(resp, medicine);
                } else{
                    sendError(resp, 404, "Medicine not found");
                } 
            }else{ //find by name
                String drug_name = parseaString(pathInfo);
                ArrayList<Medicine> medicineByssn = medicineService.findMedicineByName(drug_name);
                sendJson(resp, medicineByssn);
            }
        }catch (Exception e){
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Medicine newMedicine = readJson(req, Medicine.class);

            if (String.valueOf(newMedicine.getDrugID())== null || String.valueOf(newMedicine.getDrugID()).isEmpty()) {
                sendError(resp, 400, "Drug id is required");
                return;
            }

            if (newMedicine.getDrugName() == null || newMedicine.getDrugName().isEmpty()) {
                sendError(resp, 400, "Drug name is required");
                return;
            }


            Medicine p = medicineService.addMedicine(newMedicine);

            if (p != null) {
                resp.setStatus(201); 
                sendJson(resp, "Medicine created successfully");
            } else {
                sendError(resp, 400, "Failed to create medicine");
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
            Medicine existing = medicineService.findMedicineByNo(id);

            if (existing == null) {
                sendError(resp, 404, "Medicine not found");
                return;
            }

            Medicine updates = readJson(req, Medicine.class);
            
            updates.setDrugID(id); 

            int success = medicineService.fillMedicineStock(updates);

            if (success == 1) {
                sendJson(resp, "Medicine updated");
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
            int success = medicineService.deleteMedicine(id);

            if (success == 1) {
                resp.setStatus(204); 
            } else {
                sendError(resp, 404, "Error deleting medicine");
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
