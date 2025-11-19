package model.Facility;

public class Room {

	private int RoomNo;
	private int BedsAvailable = 0;

	public Room() {
	}; // default constructor

	// constructor for creating room
	public Room(int RoomNo) {
		this.RoomNo = RoomNo;
	}

	// constructor for loading room
	public Room(int RoomNo, int BedsAvailable) {
		this.RoomNo = RoomNo;
		this.BedsAvailable = BedsAvailable;
	}

	public void setRoomNo(int RoomNo) {
		this.RoomNo = RoomNo;
	}

	public int getRoomNo() {
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

	public void setBedsAvialabletozero(){
		BedsAvailable = 0;
	}
}