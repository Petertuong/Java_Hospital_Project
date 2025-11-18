package service.FacilityService;

import java.util.ArrayList;

import model.Facility.Medicine;

public interface IMedicineService {

    public Medicine addMedicine(Medicine Medicine);

    public Integer fillMedicineStock(Medicine Medicine, int amount);

    public Integer decreaseMedicineStock(Medicine Medicine, int amount);

    public Integer deleteMedicine(Integer drugid);

    public ArrayList<Medicine> listMedicine();
    
    public Medicine findMedicineByNo(Integer drugid);

    public ArrayList<Medicine> findMedicineByName(String name);
}
