package model.Treatment;

import model.Facility.*;
import model.Patients.Patient;
import model.Staff.*;

public class Prescription {

	private Medicine Medicine;
	private int DosagePerDay;
	private int NumberOfDay;
	private int TreatmentID;
	private Patient patient;
	private Doctor Doctor;
	private int Total;
	private String description;

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

	// //Method to set Prescription and Diagnosis with access control
	// public Prescription setPrescription(Medicine med, int DPD, int NOD, String
	// description, Patient Patient, Doctor Doctor){

	// if(!Prescription.AccessKey.EditPrescKey(Doctor.getSpecialization())){
	// System.out.println("Access Denied: Intern Doctors can't set Prescriptions.");
	// return null;
	// }

	// Prescription pre = new Prescription();

	// pre.setMedicine(med, DPD, NOD);
	// pre.setPatient(Patient);
	// pre.setDoctor(Doctor);
	// pre.setDescription(description);

	// return pre;
	// }

	// private void setMedicine(Medicine Medicine, int DosagePerDay, int
	// NumberOfDay){

	// if (Medicine.isEmpty()){
	// System.out.println("Cannot set prescription: Medicine is out of stock.");
	// return;
	// }

	// this.Medicine = Medicine;
	// this.DosagePerDay = DosagePerDay;
	// this.NumberOfDay = NumberOfDay;
	// this.setTotal(Medicine, DosagePerDay, NumberOfDay);
	// }

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

	// //access key for access control
	// public class AccessKey{
	// public static boolean EditPrescKey(String role){
	// return !role.equals("Intern");
	// }
	// }
}