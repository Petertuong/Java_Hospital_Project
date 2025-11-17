package model.Patients;

public abstract class Person {

	private String Fullname;
	private String PhoneNo;
	private char Gender;

	public Person(){
		//default constructor
	}
	
	public Person(String name, String number, char gender){
		Fullname = name;
		PhoneNo = number;
		Gender = gender;
	}
	public void setFullname(String Fullname) {
		this.Fullname = Fullname;
	}
	
	public void setPhoneNo(String PhoneNo) {
		this.PhoneNo = PhoneNo;
	}

	public void setGender(char Sex) {
		Gender = Sex;
	}

	public String getFullname() {
		return Fullname;
	}
	
	public String getPhoneNo() {
		return PhoneNo;
	}

	public char getGender() {
		return Gender;
	}

	public abstract String getRole();
}