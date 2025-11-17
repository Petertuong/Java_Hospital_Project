package model.Patients;

import java.sql.Date;
import model.Treatment.Status;
public class Patient extends Person {

	private String SSN;
	private Date DOB;
	private String Address;
	private String EmergencyContact;
	private Status status;

	//default constructor
	public Patient(){
		super();
		this.SSN = "";
		this.DOB = null;
		this.Address = "";
		this.EmergencyContact = "";
		this.status = Status.Null;
	}

	public Patient(String name, String number, char gender, String SSN, Date DOB, String Address, String EmergencyContact){
		super(name, number, gender);
		this.SSN = SSN;
		this.DOB = DOB;
		this.Address = Address;
		this.EmergencyContact = EmergencyContact;
		this.status = Status.Null;
	}

	public Patient(String name, String number, char gender, String SSN, Date DOB, String Address, String EmergencyContact, Status status){
		super(name, number, gender);
		this.SSN = SSN;
		this.DOB = DOB;
		this.Address = Address;
		this.EmergencyContact = EmergencyContact;
		this.status = status;
	}

	//compare patients by SSN
	public boolean equals(Patient other){
		return this.SSN.equals(other.SSN);
	}

	public void setSSN(String SSN) {
		this.SSN = SSN;
	}

	public void setDOB(Date DOB) {
		this.DOB = DOB;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public void setEmergencyContact(String EmergencyContact) {
		this.EmergencyContact = EmergencyContact;
	}

	public void setStatus(Status stat){
		status = stat;
	}
	public String getSSN() {
		return SSN;
	}

	public Date getDOB() {
		return DOB;
	}

	public String getAddress() {
		return Address;
	}

	public String getEmergencyContact() {
		return EmergencyContact;
	}

	public String getStatus() {
            return switch (status) {
                case Discharge -> "Discharge";
                case Admit -> "Admit";
                default -> "Null";
            };
	}

	@Override
	public String getRole(){
		return "Patient".toString();
	}

	//still need to figure out how to read prescriptions
	// public List<Prescription> getPrescription() {
	// 	throw new UnsupportedOperationException();
	// }
	// //still need to figure out how to read diagnosis without needing to pass diagnosis object
	// public String getDiagnosis(Diagnosis diag) {
		
	// 	if(!diag.getPatient().getSSN().equals(SSN)){
	// 		System.out.println("Access Denied: Diagnosis SSN does not match Patient SSN.");
	// 		return null;
	// 	}

	// 	return diag.getResult();
	// }

	// public void readPatient() {
	// 	System.out.println(
	// 		"{" + "Full name: " + this.getFullname() +
	// 		"; Contact: " + this.getPhoneNo() +
	// 		"; Gender: " + this.getGender() +
	// 		"; SSN: " + SSN +
	// 		"; DOB: " + DOB +
	// 		"; Address: " + Address +
	// 		"; Status: " + status +
	// 		"; Family contact: " + EmergencyContact + "}"
	// 	);
	// }

	// //Helper methods for hospitalize and discharge patient
	// private Bed hospitalizePatient(Patient P, Nurse N, Room R) {
		
	// 	if (!P.getStatus().equals("Admit")){
	// 		System.out.println("Cannot hospitalize patient: Patient is not admitted.");
	// 		return null;
	// 	}

	// 	N.incrPatient_in_charge();
	// 	Bed bed = R.assignBed();

	// 	System.out.println("Patient " + P.getFullname() + 
	// 	" is hospitalized under Nurse " + N.getFullname() + 
	// 	" in Room " + R.getRoomNo() +
	// 	" at Bed " + bed.getBedNo() + "."
	// 	);

	// 	return bed;
	// }

	// private void dischargePatient(Bed bed) {
		
	// 	if (!bed.getPatient().getStatus().equals("Discharge")){
	// 		System.out.println("Cannot discharge patient: Patient is not discharged.");
	// 		return;
	// 	}

	// 	bed.getNurse().decrPatient_in_charge();
	// 	bed.getRoom().releaseBed(bed);

	// 	System.out.println("Patient " + bed.getPatient().getFullname() + 
	// 	" is discharged from Room " + bed.getRoom().getRoomNo() + 
	// 	"at bed " + bed.getBedNo() + "."
	// 	);
	// }

	// //set status method, pass Doctor to gain access
	// public void setStatus(Status Stat, Doctor D, Nurse N, Room R) {
	// 	status = Stat;

	// 	if(status == Status.Admit){ 
	// 		this.hospitalizePatient(this, N, R);
	// 	}
	// 	else if (status == Status.Discharge){
	// 		throw new UnsupportedOperationException();
	// 	}
	// }

	// //overloading set status method
	// public void setStatus(Status Stat, Doctor D, Bed bed) {
	// 	bed.getPatient().status = Stat;

	// 	if(bed.getPatient().status == Status.Admit){
	// 		throw new UnsupportedOperationException();
	// 	}
	// 	else if (bed.getPatient().status == Status.Discharge){
	// 		this.dischargePatient(bed);
	// 	}
	// }
}