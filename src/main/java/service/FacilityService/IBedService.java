package  service.FacilityService;

import java.util.ArrayList;

import  model.Facility.Bed;

public interface IBedService {

    public Bed addBed(Bed bed);

    public Integer updateBedStatus(Bed bed);

    public Integer delete(Integer bedno);

    public ArrayList<Bed> listBed();

    public Bed findBedByNo(Integer bedno);

    public ArrayList<Bed> findBedByRoomNo(Integer roomno);

    public Bed findBedBySSN(String ssn);

    public ArrayList<Bed> findAvailableBed();
    
}
