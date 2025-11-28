package model.Facility;

//Khanh
public class BedStats {
    private int totalBeds;
    private int occupiedBeds; 
    private int availableBeds;
    
    public BedStats(int totalBeds, int occupiedBeds, int availableBeds) {
        this.totalBeds = totalBeds;
        this.occupiedBeds = occupiedBeds;
        this.availableBeds = availableBeds;
    }
    
    public int getTotalBeds() { 
        return totalBeds; 
    }
    
    public int getOccupiedBeds() { 
        return occupiedBeds; 
    }
    
    public int getAvailableBeds() { 
        return availableBeds; 
    }
    
    public void setTotalBeds(int totalBeds) {
        this.totalBeds = totalBeds;
    }
    
    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }
    
    public void setAvailableBeds(int availableBeds) {
        this.availableBeds = availableBeds;
    }
    
    public String toString() {
        return String.format("Total: %d, Occupied: %d, Available: %d", 
                           totalBeds, occupiedBeds, availableBeds);
    }
    
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BedStats bedStats = (BedStats) obj;
        return totalBeds == bedStats.totalBeds &&
               occupiedBeds == bedStats.occupiedBeds &&
               availableBeds == bedStats.availableBeds;
    }
    
    public int hashCode() {
        return totalBeds * 31 + occupiedBeds * 17 + availableBeds;
    }
}