package  service.MultiService;

import  model.Facility.Bed;
import  model.Patients.Patient;
import  model.Staff.Nurse;
import model.Treatment.Status;
import  service.FacilityService.BedService;
import  service.PersonService.*;

public class AdmissionService {

	private PatientService patientS;
	private NurseService nurseS;
	private BedService bedS;

	public AdmissionService(){
		this.patientS = new PatientService();
		this.nurseS = new NurseService();
		this.bedS = new BedService();
	};

	public String admitPatient(String ssn) {
		
		//retrieve patient
		Patient patient = patientS.findPatientById(ssn);

		//prevent duplicate admission
		if(patient.getStatus().toString().equals("Admitted")){
			return "already admitted";
		}
		
		
		
		//get available bed
		Bed bed = bedS.findAvailableBed().getFirst();
		//get the most available nurse
		Nurse nurse = nurseS.getNurseByMinPID();

		//update bed status
		bed.setNurse(nurse);
		bed.setPatient(patient);
		bed.setOccupied(true);
		bedS.updateBedStatus(bed);
		//update nurse PID
		nurseS.incrPID(nurse);

		patient.setStatus(Status.Admit);
		patientS.updatePatientStatus(patient);
		return "admitted";
	}

	public String dischargePatient(String ssn) {
		return dischargePatient(ssn, Status.Discharge); 
	}
	
	
	public String dischargePatient(String ssn, Status newStatus) {
		
		//retrieve patient
		Patient patient = patientS.findPatientById(ssn);

		if(!patient.getStatus().toString().equals("Admitted")){
			return "already discharged";
		}
		//retrieve bed
		Bed bed = bedS.findBedBySSN(ssn);
		//just in case
		if (bed == null) {
			patient.setStatus(newStatus);
			patientS.updatePatientStatus(patient);
			return "discharged (bed not found) - status set to " + newStatus;
		}
		//update nurse pid
		if(bed.getNurse() != null) {
			nurseS.decrPID(bed.getNurse());
		}
		
		//update bed status
		bed.setOccupied(false);
		bed.setPatient(null);
		bed.setNurse(null);
		bedS.updateBedStatus(bed);
		
		//update patient status
		patient.setStatus(newStatus);
		patientS.updatePatientStatus(patient);

		return "discharged - status set to " + newStatus;		
	}
}
