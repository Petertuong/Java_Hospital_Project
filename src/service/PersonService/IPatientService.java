package service.PersonService;

import java.util.ArrayList;

import model.Patients.Patient;
import model.Treatment.Status;

public interface IPatientService {

    public Patient addPatient(Patient patient);
    
    public Patient updatePatientStatus(Patient patient);

    public Patient deletePatient(String ssn);

    public ArrayList<Patient> listPatient();

    public Patient findPatientById(String ssn);

    public ArrayList<Patient> findPatientByAge(int age);

    public ArrayList<Patient> findPatientByStatus(Status status);
}
