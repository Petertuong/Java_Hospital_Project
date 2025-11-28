package  model.Staff;

public class Nurse extends StaffClass {

	private int Patient_in_charge = 0;

	public Nurse(){
	}

	//load
	public Nurse(String name, String number, char gender, int SID, String Specification, int patient_in_charge){
		super(name, number, gender, SID, Specification);
		this.Patient_in_charge = patient_in_charge;
	}
	//create
	public Nurse(String name, String number, char gender, String Specification){
		super(name, number, gender, Specification);
	}

	public int getPatient_in_charge(){
		return Patient_in_charge;
	}

	public void incrPatient_in_charge() {
		Patient_in_charge += 1;
	}
	
	public void decrPatient_in_charge() {
		Patient_in_charge -= 1;
	}
	
	public void setPatient_in_charge(int count) {
		Patient_in_charge = count;
	}

	@Override
	public String getRole(){
		return "Nurse".toString();
	}

}
