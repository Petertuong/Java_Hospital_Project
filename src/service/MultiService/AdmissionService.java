package service.MultiService;

import model.Facility.Bed;
import model.Patients.Patient;
import model.Staff.Nurse;
import service.FacilityService.BedService;
import service.FacilityService.RoomService;
import service.PersonService.*;

public class AdmissionService {

	PatientService patientS;
	DoctorService doctorS;
	NurseService nurseS;
	BedService bedS;
	RoomService roomS;

	public AdmissionService(){};

	public String admitPatient(String ssn) {
		
		//retrieve patient
		Patient patient = patientS.findPatientById(ssn);

		//prevent duplicate admission
		if(patient.getStatus().equals("Admitted")){
			return "already admitted";
		}
		//get available bed
		Bed bed = bedS.findAvailableBed().get(0);
		//get the most available nurse
		Nurse nurse = nurseS.getNurseByMinPID();

		//update bed status
		bed.setNurse(nurse);
		bed.setPatient(patient);
		bed.setOccupied(true);
		bedS.updateBedStatus(bed);
		//update nurse PID
		nurseS.incrPID(nurse);

		return "admitted";
	}

	public String dischargePatient(String ssn) {
		
		//retrieve patient
		Patient patient = patientS.findPatientById(ssn);

		//prevent duplicate admission
		if(patient.getStatus().equals("Discharged")){
			return "already discharged";
		}

		//get available bed
		Bed bed = bedS.findBedBySSN(ssn);
		//decr nurse PID
		nurseS.decrPID(bed.getNurse());
		//update bed status
		bed.setOccupied(false);
		bedS.updateBedStatus(bed);

		return "discharged";		
	}
}
