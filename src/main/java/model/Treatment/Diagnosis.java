package  model.Treatment;

import model.Patients.Patient;

public class Diagnosis {

	private int diag_id;
	private String Result;
	private Patient patient;

	public Diagnosis(){
		//default constructor
	}
	
	//create constructor
	public Diagnosis(String Result, Patient p){
		this.Result = Result;
		patient = p;
	}

	//load constructor
	public Diagnosis(int diag_id, String Result, Patient p){
		this.diag_id = diag_id;
		this.Result = Result;
		patient = p;
	}

	public void setPatient(Patient p){
		patient = p;
	}

	public void setResult(String Result){
		this.Result = Result;
	}

	
	public Patient getPatient(){
		return patient;
	}

	public String getResult(){
		return Result;
	}


    public int getDiag_id() {
        return diag_id;
    }

    public void setDiag_id(int diag_id) {
        this.diag_id = diag_id;
    }

}