package model.Treatment;

public enum Status {

	Discharge("Discharged"),
	Admit("Admitted"),
	Null("null");

	private final String state;

	Status(String s){
		state = s;
	}

	public String toString(){
		return state;
	}

}