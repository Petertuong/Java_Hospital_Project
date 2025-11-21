package  service.PersonService;

import java.util.ArrayList;

import  dao.PersonDAO.NurseDAO;
import  model.Staff.Nurse;
import  service.AbstractService;

public class NurseService extends AbstractService<NurseDAO> implements INurseService {

    NurseDAO nursedao;
	public NurseService(){
		super();
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
           return nursedao.update(nurse);
        }

        return -1;

    }

    @Override
    public Integer decrPID(Nurse nurse) {

        if(nurse.getPatient_in_charge() > 0){
            nurse.decrPatient_in_charge();
            return nursedao.update(nurse);
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

        int min = allNurses.get(0).getPatient_in_charge();
        Nurse nurseMin = new Nurse();
        for (Nurse nurse: allNurses){
            if(min > nurse.getPatient_in_charge()){
                min = nurse.getPatient_in_charge();
                nurseMin = nurse;
            }
        }

        return nurseMin;
    }


    
}
