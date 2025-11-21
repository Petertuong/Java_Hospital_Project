package  model.Patients;

import java.sql.Date;
import  model.Treatment.Status;
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
			switch (status) {
				case Discharge:
					return "Discharged";
				case Admit:
					return "Admitted";
				default:
					return "Null";
			}
	}

	@Override
	public String getRole(){
		return "Patient".toString();
	}

}