package model.Staff;

public class Nurse extends StaffClass {

	private int Patient_in_charge = 0;

	public Nurse(){
		//default constructor
	}
	//constructor for loading new object
	public Nurse(String name, String number, char gender, int SID, String Specification, int patient_in_charge){
		super(name, number, gender, SID, Specification);
		this.Patient_in_charge = patient_in_charge;
	}

	//overloading constructor for creating new object
	public Nurse(String name, String number, char gender, String Specification){
		super(name, number, gender, Specification);
	}

	public int getPatient_in_charge(){
		return Patient_in_charge;
	}

	//when a new patient is assigned -> increase count
	public void incrPatient_in_charge() {
		Patient_in_charge += 1;
	}
	
	//when a patient is discharged -> decrease count
	public void decrPatient_in_charge() {
		Patient_in_charge -= 1;
	}


	@Override
	public String getRole(){
		return "Nurse".toString();
	}
	// public void readNurse() {
	// 	System.out.println(
	// 		"Full name: " + this.getFullname() +
	// 		"Working Phone: " + this.getPhoneNo() +
	// 		"Gender: " + this.getGender() +
	// 		"SID: " + this.getSID() +
	// 		"Specification: " + this.getSpecialization() +
	// 		"Patients in charge: " + Patient_in_charge
	// 	);
	// }
}