package service.FacilityService;

import java.util.ArrayList;

import dao.FacilityDAO.MedicineDAO;
import model.Facility.Medicine;
import service.AbstractService;

public class MedicineService extends AbstractService<MedicineDAO> implements IMedicineService{

    MedicineDAO medicinedao;

    public MedicineService(){
        super();
    }

    @Override
    public MedicineDAO createEntityDAO() {
        return new MedicineDAO();
    }

    @Override
    public Medicine addMedicine(Medicine Medicine) {
        return medicinedao.create(Medicine);
    }

    @Override
    public Integer fillMedicineStock(Medicine Medicine, int amount) {
        Medicine.fillStock(amount);
        return medicinedao.update(Medicine);
    }

    @Override
    public Integer decreaseMedicineStock(Medicine Medicine, int amount) {
        Medicine.decreaseStock(amount);
        return medicinedao.update(Medicine);       
    }

    @Override
    public Integer deleteMedicine(Integer drugid) {
        return medicinedao.delete(drugid);
    }

    @Override
    public ArrayList<Medicine> listMedicine() {
        return medicinedao.selectAll();
    }

    @Override
    public Medicine findMedicineByNo(Integer drugid) {
        return medicinedao.selectById(drugid);
    }

    @Override
    public ArrayList<Medicine> findMedicineByName(String name) {
        String condition = "drugname = " + name;
        return medicinedao.selectByCondition(condition);
    }


}