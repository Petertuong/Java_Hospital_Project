package  service.PersonService;

import java.util.ArrayList;

import  dao.PersonDAO.NurseDAO;
import  model.Staff.Nurse;
import  service.AbstractService;

public class NurseService extends AbstractService<NurseDAO> implements INurseService {

    private NurseDAO nursedao;
	public NurseService(){
        super();
        this.nursedao = createEntityDAO();
	}

	@Override
	public NurseDAO createEntityDAO(){
		return new NurseDAO();
	}

    @Override
    public Nurse insertNurse(Nurse nurse) {
        return nursedao.create(nurse);
    }

    @Override
    public Integer incrPID(Nurse nurse) {

        if(nurse.getPatient_in_charge() < 50){
           nurse.incrPatient_in_charge();
           return nursedao.updatePatientCount(nurse.getSID(), nurse.getPatient_in_charge());
        }

        return -1;

    }

    @Override
    public Integer decrPID(Nurse nurse) {

        if(nurse.getPatient_in_charge() > 0){
            nurse.decrPatient_in_charge();
            return nursedao.updatePatientCount(nurse.getSID(), nurse.getPatient_in_charge());
        }

        return -1;
    }

    @Override
    public ArrayList<Nurse> listNurse() {
        return nursedao.selectAll();
    }

    @Override
    public Nurse findNurseByID(Integer k) {
        return nursedao.selectById(k);
    }

    @Override
    public ArrayList<Nurse> findNurseByGender(char c) {
        char cUpper = Character.toUpperCase(c);
        String condition = "gender = " + cUpper;
        return nursedao.selectByCondition(condition);
    }

    @Override
    public ArrayList<Nurse> findNurseByPIDMax() {
        String condition = "patient_in_chare = 50";
        return nursedao.selectByCondition(condition);
    }

    @Override
    public Nurse getNurseByMinPID() {
        
        ArrayList<Nurse> allNurses = listNurse();
        
        if (allNurses.isEmpty()) {
            return null; // No nurses available
        }

        Nurse nurseMin = allNurses.getFirst(); // Start with first nurse
        int min = nurseMin.getPatient_in_charge();
        
        for (Nurse nurse: allNurses){
            if(nurse.getPatient_in_charge() < min){
                min = nurse.getPatient_in_charge();
                nurseMin = nurse;
            }
        }

        return nurseMin;
    }

    @Override
    public Integer deleteNurse(Integer k) {
        return nursedao.delete(k);
    }

    @Override
    public Integer updateNurse(Nurse nurse) {
        return nursedao.update(nurse);
    }

    
}
