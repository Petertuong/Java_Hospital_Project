package  model.Treatment;

import model.Facility.*;
import model.Patients.Patient;
import model.Staff.*;

public class Prescription {

	private Medicine Medicine;
	private int DosagePerDay;
	private int NumberOfDay;
	private int TreatmentID;
	private Patient patient;
	private Doctor Doctor; //nullable
	private int Total;
	private String description; //nullable

	public Prescription() {
		// default constructor
	}

	// for loading from database
	public Prescription(Medicine Medicine, int DosagePerDay, int NumberOfDay, int TreatmentID, Patient patient,
			Doctor Doctor, String description) {

		this.TreatmentID = TreatmentID;
		this.patient = patient;
		this.Doctor = Doctor;
		this.description = description;
		this.Medicine = Medicine;
		this.DosagePerDay = DosagePerDay;
		this.NumberOfDay = NumberOfDay;
		Total = DosagePerDay * NumberOfDay;
	}

	// overload constructor (for creating new object)
	public Prescription(Medicine Medicine, int DosagePerDay, int NumberOfDay, Patient patient, Doctor Doctor,
			String description) {

		this.Medicine = Medicine;
		this.DosagePerDay = DosagePerDay;
		this.NumberOfDay = NumberOfDay;
		this.patient = patient;
		this.Doctor = Doctor;
		this.description = description;
		Total = DosagePerDay * NumberOfDay;

	}

	public void setTreatmentID(int id) {
		TreatmentID = id;
	}
	
	public void setMedicine(Medicine med) {
		Medicine = med;
	}

	public void setNOD(int NOD) {
		NumberOfDay = NOD;
	}

	public void setDPD(int DPD) {
		DosagePerDay = DPD;
	}

	public void setPatient(Patient p) {
		patient = p;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDoctor(Doctor Doctor) {
		this.Doctor = Doctor;
	}

	public Medicine getMedicine() {
		return Medicine;
	}

	public int getDosagePerDay() {
		return DosagePerDay;
	}

	public int getNumberOfDay() {
		return NumberOfDay;
	}

	public int getTreatmentID() {
		return TreatmentID;
	}

	public Patient getPatient() {
		return patient;
	}

	public Doctor getDoctor() {
		return Doctor;
	}

	public String getDescription() {
		return description;
	}

	public int getTotal() {
		return Total;
	}

}