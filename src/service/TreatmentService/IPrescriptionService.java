package service.TreatmentService;

import java.util.ArrayList;

import model.Treatment.Prescription;

public interface IPrescriptionService {

    public Prescription addPrescription(Prescription Prescription);

    public Integer changeDrug(Prescription Prescription);

    public Integer deletePrescription(Integer treatment_id);

    public ArrayList<Prescription> listPrescription();
    
    public Prescription findPrescriptionByID(Integer treatment_id);

    public ArrayList<Prescription> findPrescriptionBySSN(String ssn);
}
