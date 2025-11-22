package  service.FacilityService;

import java.util.ArrayList;

import  model.Facility.Medicine;

public interface IMedicineService {

    public Medicine addMedicine(Medicine Medicine);

    public Integer fillMedicineStock(Medicine Medicine);

    public Integer decreaseMedicineStock(Medicine Medicine);

    public Integer deleteMedicine(Integer drugid);

    public ArrayList<Medicine> listMedicine();
    
    public Medicine findMedicineByNo(Integer drugid);

    public ArrayList<Medicine> findMedicineByName(String name);
}
