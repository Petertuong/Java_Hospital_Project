package  service.TreatmentService;

import java.util.ArrayList;

import  dao.TreatmentDAO.PrescriptionDAO;
import  model.Treatment.Prescription;
import  service.AbstractService;

public class PrescriptionService extends AbstractService<PrescriptionDAO> implements IPrescriptionService{

    PrescriptionDAO prescriptiondao;
    
    @Override
    public PrescriptionDAO createEntityDAO() {
        return new PrescriptionDAO();
    }

    @Override
    public Prescription addPrescription(Prescription Prescription) {
        return prescriptiondao.create(Prescription);
    }

    @Override
    public Integer changeDrug(Prescription Prescription) {
        return prescriptiondao.update(Prescription);
    }

    @Override
    public Integer deletePrescription(Integer treatment_id) {
        return prescriptiondao.delete(treatment_id);
    }

    @Override
    public ArrayList<Prescription> listPrescription() {
        return prescriptiondao.selectAll();
    }

    @Override
    public Prescription findPrescriptionByID(Integer treatment_id) {
        return prescriptiondao.selectById(treatment_id);
    }

    @Override
    public ArrayList<Prescription> findPrescriptionBySSN(String ssn) {
        String condition = "ssn = " + ssn;
        return prescriptiondao.selectByCondition(condition);
    }



}
