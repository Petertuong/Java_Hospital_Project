package service.PersonService;

import java.util.ArrayList;

import model.Staff.Doctor;

public interface IDoctorService {

    public Doctor insertDoctor(Doctor doctor);

    public Integer updateDoctor(Doctor doctor);

    public Integer deleteDoctor(Integer doctor_id);

    public ArrayList<Doctor> listDoctor();

    public Doctor findDoctorByID(Integer doctor_id);

    public ArrayList<Doctor> findDoctorBySpecialization(String spec);

    public ArrayList<Doctor> findDoctorByQualification(String qual);


}
