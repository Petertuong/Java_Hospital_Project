package service.PersonService;

import java.util.ArrayList;

import dao.PersonDAO.PatientDAO;
import model.Patients.Patient;
import model.Treatment.Status;
import service.AbstractService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientService extends AbstractService<PatientDAO> implements IPatientService{
    
	PatientDAO patientdao;

	public PatientService(){
		super();
	}

	@Override
	public PatientDAO createEntityDAO(){
		return new PatientDAO();
	}

	@Override
	public Patient addPatient(Patient patient) {
		return patientdao.create(patient);
	}

	@Override
	public Patient updatePatientStatus(Patient patient) {
		return patientdao.update(patient);
	}

	@Override
	public Patient deletePatient(String ssn) {
		return patientdao.delete(ssn);
	}

	@Override
	public ArrayList<Patient> listPatient() {
		return patientdao.selectAll();
	}

	@Override
	public Patient findPatientById(String ssn) {
		return patientdao.selectById(ssn);
	}

	@Override
	public ArrayList<Patient> findPatientByAge(int age) {

		ArrayList<Patient> allPatients = patientdao.selectAll();
		ArrayList<Patient> filteredPatients = new ArrayList<>();

		int currentYear = LocalDate.now().getYear();

		for (Patient patient: allPatients){
			int yearofbirth = patient.getDOB().toLocalDate().getYear();
			int patientAge = currentYear - yearofbirth;

			if (patientAge == age){
				filteredPatients.add(patient);
			}
		}

		return filteredPatients;
	}

	@Override
	public ArrayList<Patient> findPatientByStatus(Status status){
		return patientdao.selectByCondition(status.toString());
	}

	
}
