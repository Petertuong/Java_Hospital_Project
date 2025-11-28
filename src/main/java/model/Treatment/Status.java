package  model.Treatment;
//Khanh
public enum Status {

	Discharge("Discharged"),
	Admit("Admitted"),
	Null("Waiting"); 

	private final String state;

	Status(String s){
		state = s;
	}

	public String toString(){
		return state;
	}

}