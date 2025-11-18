package service.FacilityService;

import java.util.ArrayList;

import dao.FacilityDAO.BedDAO;
import model.Facility.Bed;
import service.AbstractService;

public class BedService extends AbstractService<BedDAO> implements IBedService {

    BedDAO beddao;

    public BedService(){
        super();
    }
    @Override
    public BedDAO createEntityDAO() {
        return new BedDAO();
    }

    @Override
    public Bed addBed(Bed bed) {
        return beddao.create(bed);
    }

    @Override
    public Integer updateBedStatus(Bed bed) {
        return beddao.update(bed);
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


}
