package test;

import dao.DoctorDAO;
import model.Staff.Doctor;
import service.PersonService.PatientService;
import service.PersonService.*;

public class TestUtil {
    public static void main(String[] args) {
        String id = "123456789876";
        PatientService patient = new PatientService();

        patient.readPatient(id);
        
    }
}
