package  service.PersonService;

import java.util.ArrayList;

import  model.Staff.Nurse;

public interface INurseService {

    public Nurse insertNurse(Nurse nurse);

    public Integer decrPID(Nurse nurse);

    public Integer incrPID(Nurse nurse);

    public ArrayList<Nurse> listNurse();

    public Integer deleteNurse(Integer k);
    
    public Nurse findNurseByID(Integer k);

    public ArrayList<Nurse> findNurseByGender(char c);

    public ArrayList<Nurse> findNurseByPIDMax();//get nurse who can't be assigned more patient

    public Nurse getNurseByMinPID();
}
