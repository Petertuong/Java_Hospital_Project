package  model.Treatment;

public enum Status {

	Discharge("Discharged"),
	Admit("Admitted"),
	Null("Waiting"); // Change from "null" to "Waiting"

	private final String state;

	Status(String s){
		state = s;
	}

	public String toString(){
		return state;
	}

}