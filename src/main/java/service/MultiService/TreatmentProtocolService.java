package service.MultiService;

import model.Facility.Medicine;
import model.Treatment.Prescription;
import service.FacilityService.MedicineService;
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

        // Create a Medicine object with the amount to decrease
        Medicine decreaseAmount = new Medicine(med.getDrugID(), med.getDrugName(), prescription.getTotal());
        medicineS.decreaseMedicineStock(decreaseAmount);
        prescriptionS.addPrescription(prescription);

        return "Ok";
    }

}
