package  model.Staff;

public class Doctor extends StaffClass {

	private String Qualification;

	public Doctor(){

	}
	//creating new doctor object
	public Doctor(String name, String number, char gender, String Specification, String Qualification){
		super(name, number, gender, Specification);
		this.Qualification = Qualification;
	}

	//loading doctor object from database
	public Doctor(String name, String number, char gender, int SID, String Specification, String Qualification){
		super(name, number, gender, SID, Specification);
		this.Qualification = Qualification;
	}

	public void setQualification(String Qualification) {
		this.Qualification = Qualification;
	}
	public String getQualification() {
		return Qualification;
	}

	@Override
	public String getRole(){
		return "Doctor".toString();
	}
}