package model.Facility;

public class Room {

	private String RoomNo;
	private int BedsAvailable = 0;

	public Room() {
	}; // default constructor

	// constructor for creating room
	public Room(String RoomNo) {
		this.RoomNo = RoomNo;
	}

	// constructor for loading room
	public Room(String RoomNo, int BedsAvailable) {
		this.RoomNo = RoomNo;
		this.BedsAvailable = BedsAvailable;
	}

	public void setRoomNo(String RoomNo) {
		this.RoomNo = RoomNo;
	}

	public String getRoomNo() {
		return RoomNo;
	}

	public int getBedsAvailable() {
		return BedsAvailable;
	}

	public void incrBedsAvailable() {
		BedsAvailable += 1;
	}

	public void decrBedsAvailable() {
		BedsAvailable -= 1;
	}

	// public List<Bed> getBedsList() {
	// return BedsList;
	// }

	// public void addBed(Bed bed) {

	// if (!bed.getRoom().getRoomNo().equals(RoomNo)) {
	// System.out.println("Error: Bed's room does not match.");
	// return;
	// }

	// BedsList.add(bed);
	// BedsAvailable += 1;
	// }
	// //for hospitalizePatient() method
	// public Bed assignBed() {
	// //for each bed in the assigned room, check if it's unoccupied
	// //if not, assign the bed to the patient and mark it as occupied
	// for(Bed bed: BedsList){
	// if (!bed.isOccupied()){
	// bed.setOccupied(true);
	// BedsAvailable -= 1;
	// return bed;
	// }
	// }

	// return null;
	// }

	// //for dischargepatient() method
	// public void releaseBed(Bed otherBed) {

	// for(Bed bed: BedsList){
	// if (bed.equals(otherBed)){
	// if (bed.isOccupied()){
	// bed.setOccupied(false);
	// BedsAvailable += 1;
	// return;
	// }
	// else{
	// System.out.println("Error: Bed is already unoccupied.");
	// return;
	// }
	// }
	// }

	// System.out.println("Error: Bed not found in this room.");
	// }

	// public void setBedsList(List<Bed> BedsList) {
	// this.BedsList = BedsList;
	// }
}