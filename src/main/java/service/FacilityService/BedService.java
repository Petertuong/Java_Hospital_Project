package  service.FacilityService;

import java.util.ArrayList;

import  dao.FacilityDAO.BedDAO;
import  model.Facility.Bed;
import  service.AbstractService;

public class BedService extends AbstractService<BedDAO> implements IBedService {

    private BedDAO beddao;

    public BedService(){
		super();
		this.beddao = createEntityDAO();
    }

    @Override
    public BedDAO createEntityDAO() {
        return new BedDAO();
    }

    @Override
    public Integer updateBedStatus(Bed bed) {
        return beddao.update(bed);
    }

    @Override
    public Bed updateBed(Bed bed){

        Bed abed = this.findBedByNo(bed.getBedNo());

        this.delete(bed.getBedNo());

        return this.addBed(abed);
    }

    @Override
    public Integer delete(Integer bedno) {
        return beddao.delete(bedno);
    }

    @Override
    public ArrayList<Bed> listBed() {
        return beddao.selectAll();
    }

    @Override
    public Bed findBedByNo(Integer bedno) {
        return beddao.selectById(bedno);
    }

    @Override
    public ArrayList<Bed> findBedByRoomNo(Integer roomno) {
        String condition = "roomno = " + roomno;
        return beddao.selectByCondition(condition);
    }

    @Override
    public ArrayList<Bed> findAvailableBed(){
        String condition = "is_occupied = " + 0;
        return beddao.selectByCondition(condition);
    }

    @Override
    public Bed findBedBySSN(String ssn){
        String condition = "b.ssn = '" + ssn + "'";  
        ArrayList<Bed> beds = beddao.selectByCondition(condition);
        if (beds == null || beds.isEmpty()) {
            return null;
        }
        return beds.getFirst();
    }

    @Override
    public Bed addBed(Bed bed) {
        return beddao.create(bed);
    }
}
