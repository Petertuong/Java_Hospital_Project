package  service.PersonService;

import java.util.ArrayList;

import  dao.PersonDAO.DoctorDAO;
import  model.Staff.Doctor;
import  service.AbstractService;

public class DoctorService extends AbstractService<DoctorDAO> implements IDoctorService {

	DoctorDAO doctordao;

	public DoctorService(){
		super();
	}

	@Override
	public DoctorDAO createEntityDAO(){
		return new DoctorDAO();
	}

    @Override
    public Doctor insertDoctor(Doctor doctor) {
        return doctordao.create(doctor);
    }

    @Override
    public Integer updateDoctor(Doctor doctor) {
        return doctordao.update(doctor);
    }

    @Override
    public Integer deleteDoctor(Integer doctor_id) {
        return doctordao.delete(doctor_id);
    }

    @Override
    public ArrayList<Doctor> listDoctor() {
        return doctordao.selectAll();
    }

    @Override
    public Doctor findDoctorByID(Integer doctor_id) {
        return doctordao.selectById(doctor_id);
    }

    @Override
    public ArrayList<Doctor> findDoctorBySpecialization(String spec) {
        String condition = "specialization = " + spec;
        return doctordao.selectByCondition(condition);
    }

    @Override
    public ArrayList<Doctor> findDoctorByQualification(String qual) {
        String condition = "specialization = " + qual;
        return doctordao.selectByCondition(condition);
    }

}
