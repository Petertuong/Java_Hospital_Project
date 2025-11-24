package  model.Facility;

import model.Patients.Patient;
import model.Staff.Nurse;

public class Bed {

	private Room Room;
	private Integer BedNo;
	private boolean IsOccupied = false;
	private Patient patient;
	private Nurse nurse;

	//default constructor
	public Bed() {};
	//loading constructor
	public Bed(Room room, int bedNo, boolean isOccupied, Patient patient, Nurse nurse) {
		this.Room = room;
		this.BedNo = bedNo;
		this.IsOccupied = isOccupied;
		this.patient = patient;
		this.nurse = nurse;
	}

	//creating constructor
	public Bed(Room room, int bedNo) {
		this.Room = room;
		this.BedNo = bedNo;
		this.IsOccupied = false;
		this.patient = null;
		this.nurse = null;
	}
	
	public boolean equals(Bed other){
		return this.Room.equals(other.Room) && 
				this.BedNo.equals(other.BedNo) &&
				this.patient.equals(other.patient);
	}
	public Room getRoom() {
		return Room;
	}

	public int getBedNo() {
		return BedNo;
	}

	public boolean isOccupied() {
		return IsOccupied;
	}

	public Patient getPatient() {
		return patient;
	}

	public Nurse getNurse() {
		return nurse;
	}

	public void setOccupied(boolean isOccupied) {
		IsOccupied = isOccupied;
	}


	public void setPatient(Patient patient) {
		this.patient = patient;
	}


	public void setNurse(Nurse nurse) {
		this.nurse = nurse;
	}

	public void setRoom(Room room){
		Room = room;
	}

	public void setBedno(Integer bedno){
		BedNo = bedno;
	}
}