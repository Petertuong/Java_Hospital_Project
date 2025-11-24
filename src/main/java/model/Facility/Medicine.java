package  model.Facility;

public class Medicine {

	private int DrugID;
	private String DrugName;
	private int Quantity;

	public Medicine() {};

	public Medicine(int drugid, String drugname, int quantity) {
		DrugID = drugid;
		DrugName = drugname;
		Quantity = quantity;
	}

	public Medicine(String drugname, int quantity) {
		DrugName = drugname;
		Quantity = quantity;
	}

	public int getDrugID() {
		return DrugID;
	}

	public String getDrugName() {
		return DrugName;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setDrugID(int drugid) {
		DrugID = drugid;
	}

	public void decreaseStock(int amount) {
		this.Quantity -= amount;
	}

	public void fillStock(int amount) {
		this.Quantity += amount;
	}

	public boolean isEmpty() {
		return this.Quantity <= 0;
	}

	public void setDrugName(String drugname) {
		DrugName = drugname;
	}
}