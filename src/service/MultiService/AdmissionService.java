package service;

public class AdmissionService {



	// //Helper methods for hospitalize and discharge patient
	// private Bed hospitalizePatient(Patient P, Nurse N, Room R) {
		
	// 	if (!P.getStatus().equals("Admit")){
	// 		System.out.println("Cannot hospitalize patient: Patient is not admitted.");
	// 		return null;
	// 	}

	// 	N.incrPatient_in_charge();
	// 	Bed bed = R.assignBed();

	// 	System.out.println("Patient " + P.getFullname() + 
	// 	" is hospitalized under Nurse " + N.getFullname() + 
	// 	" in Room " + R.getRoomNo() +
	// 	" at Bed " + bed.getBedNo() + "."
	// 	);

	// 	return bed;
	// }

	// private void dischargePatient(Bed bed) {
		
	// 	if (!bed.getPatient().getStatus().equals("Discharge")){
	// 		System.out.println("Cannot discharge patient: Patient is not discharged.");
	// 		return;
	// 	}

	// 	bed.getNurse().decrPatient_in_charge();
	// 	bed.getRoom().releaseBed(bed);

	// 	System.out.println("Patient " + bed.getPatient().getFullname() + 
	// 	" is discharged from Room " + bed.getRoom().getRoomNo() + 
	// 	"at bed " + bed.getBedNo() + "."
	// 	);
	// }

	// //set status method, pass Doctor to gain access
	// public void setStatus(Status Stat, Doctor D, Nurse N, Room R) {
	// 	status = Stat;

	// 	if(status == Status.Admit){ 
	// 		this.hospitalizePatient(this, N, R);
	// 	}
	// 	else if (status == Status.Discharge){
	// 		throw new UnsupportedOperationException();
	// 	}
	// }

	// //overloading set status method
	// public void setStatus(Status Stat, Doctor D, Bed bed) {
	// 	bed.getPatient().status = Stat;

	// 	if(bed.getPatient().status == Status.Admit){
	// 		throw new UnsupportedOperationException();
	// 	}
	// 	else if (bed.getPatient().status == Status.Discharge){
	// 		this.dischargePatient(bed);
	// 	}
	// }
}
