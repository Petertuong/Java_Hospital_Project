package  service.TreatmentService;

import java.util.ArrayList;

import  model.Treatment.Diagnosis;

public interface IDiagnosisService {

    public Diagnosis addDiagnosis(Diagnosis Diagnosis);

    public Integer changeResult(Diagnosis Diagnosis, String result);

    public Integer deleteDiagnosis(Integer diag_id);

    public ArrayList<Diagnosis> listDiagnosis();
    
    public Diagnosis findDiagnosisByID(Integer diag_id);

    public ArrayList<Diagnosis> findDiagnosisBySSN(String ssn);
}
