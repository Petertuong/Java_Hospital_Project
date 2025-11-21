package  model.Staff;

import  model.Patients.Person;

public class StaffClass extends Person {

	private int SID;
	private String Specialization;

	public StaffClass(){
		//default constructor
	}
	//constructor for loading new object
	public StaffClass(String name, String number, char gender, int SID, String Spec){
		super(name, number, gender);
		this.SID = SID;
		this.Specialization = Spec;
	}
	//overloading constructor for creating new object without SID
	public StaffClass(String name, String number, char gender, String Spec){
		super(name, number, gender);
		this.Specialization = Spec;
	}

	public int getSID() {
		return SID;
	}

	public String getSpecialization() {
		return Specialization;
	}

	public void setSpecification(String Spec) {
		this.Specialization= Spec;
	}

	public void setSID(int SID) {
		this.SID = SID;
	}

	@Override
	public String getRole(){
		return "Staff".toString();
	}
}