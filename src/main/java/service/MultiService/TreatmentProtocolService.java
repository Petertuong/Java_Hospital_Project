package service.MultiService;

import model.Facility.Medicine;
import model.Treatment.Prescription;
import service.FacilityService.MedicineService;
import service.TreatmentService.DiagnosisService;
import service.TreatmentService.PrescriptionService;

public class TreatmentProtocolService{

    PrescriptionService prescriptionS;
    MedicineService medicineS;
    
    public TreatmentProtocolService(){
        prescriptionS = new PrescriptionService();
        medicineS = new MedicineService();
    };

    public String prescribe(Prescription prescription){

        int drug_id = prescription.getMedicine().getDrugID();
        Medicine med = medicineS.findMedicineByNo(drug_id);

        if(med.getQuantity() < prescription.getTotal()){
            return "Failed";
        }

        med.decreaseStock(prescription.getTotal());

        medicineS.decreaseMedicineStock(med);
        prescriptionS.addPrescription(prescription);

        return "Ok";
    }


}
