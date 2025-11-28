package  service.MultiService;

import  model.Facility.Bed;
import  model.Patients.Patient;
import  model.Staff.Nurse;
import model.Treatment.Status;
import  service.FacilityService.BedService;
import  service.FacilityService.RoomService;
import  service.PersonService.*;

public class AdmissionService {

	PatientService patientS;
	DoctorService doctorS;
	NurseService nurseS;
	BedService bedS;
	RoomService roomS;

	public AdmissionService(){
		this.patientS = new PatientService();
		this.doctorS = new DoctorService();
		this.nurseS = new NurseService();
		this.bedS = new BedService();
		this.roomS = new RoomService();
	};

	public String admitPatient(String ssn) {
		
		//retrieve patient
		Patient patient = patientS.findPatientById(ssn);

		//prevent duplicate admission
		if(patient.getStatus().toString().equals("Admitted")){
			return "already admitted";
		}
		
		// IMPORTANT: Check if patient has any existing bed assignment and clean it up first
		Bed existingBed = bedS.findBedBySSN(ssn);
		if(existingBed != null) {
			// Clean up existing assignment
			if(existingBed.getNurse() != null) {
				nurseS.decrPID(existingBed.getNurse());
			}
			existingBed.setOccupied(false);
			existingBed.setPatient(null);
			existingBed.setNurse(null);
			bedS.updateBedStatus(existingBed);
		}
		
		// Now proceed with new admission
		patient.setStatus(Status.Admit);
		patientS.updatePatientStatus(patient);
		
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

		return "admitted";
	}

	public String dischargePatient(String ssn) {
		return dischargePatient(ssn, Status.Discharge); // Default to Discharged
	}
	
	
	public String dischargePatient(String ssn, Status newStatus) {
		
		//retrieve patient
		Patient patient = patientS.findPatientById(ssn);

		//prevent duplicate discharge - check if patient is currently admitted
		if(!patient.getStatus().toString().equals("Admitted")){
			return "already discharged";
		}
		
		//get the bed this patient is occupying FIRST
		Bed bed = bedS.findBedBySSN(ssn);
		if (bed == null) {
			// Still update patient status even if bed not found
			patient.setStatus(newStatus);
			patientS.updatePatientStatus(patient);
			return "discharged (bed not found) - status set to " + newStatus;
		}
		
		//decr nurse PID BEFORE clearing bed
		if(bed.getNurse() != null) {
			nurseS.decrPID(bed.getNurse());
		}
		
		//update bed status - clear patient and nurse assignment
		bed.setOccupied(false);
		bed.setPatient(null);
		bed.setNurse(null);
		bedS.updateBedStatus(bed);
		
		// Set patient status to specified status AFTER clearing bed
		patient.setStatus(newStatus);
		patientS.updatePatientStatus(patient);

		return "discharged - status set to " + newStatus;		
	}
}
