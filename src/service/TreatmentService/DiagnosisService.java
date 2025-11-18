package service.TreatmentService;

import java.util.ArrayList;

import dao.TreatmentDAO.DiagnosisDAO;
import model.Treatment.Diagnosis;
import service.AbstractService;;

public class DiagnosisService extends AbstractService<DiagnosisDAO> implements IDiagnosisService{

    DiagnosisDAO diagnosisdao;

    @Override
    public DiagnosisDAO createEntityDAO() {
        return new DiagnosisDAO();
    }


    @Override
    public Diagnosis addDiagnosis(Diagnosis Diagnosis) {
        return diagnosisdao.create(Diagnosis);
    }

    @Override
    public Integer changeResult(Diagnosis Diagnosis, String result) {
        Diagnosis.setResult(result);
        return diagnosisdao.update(Diagnosis);
    }

    @Override
    public Integer deleteDiagnosis(Integer diag_id) {
        return diagnosisdao.delete(diag_id);
    }

    @Override
    public ArrayList<Diagnosis> listDiagnosis() {
        return diagnosisdao.selectAll();
    }

    @Override
    public Diagnosis findDiagnosisByID(Integer diag_id) {
        return diagnosisdao.selectById(diag_id);
    }

    @Override
    public ArrayList<Diagnosis> findDiagnosisBySSN(String ssn) {
        String condition = "ssn = " + ssn;
        return diagnosisdao.selectByCondition(condition);
    }

}
