HMS (HospitalProject) — Smoke Test Checklist

Purpose: quick, copyable steps to verify main CRUD flows after build.

Prerequisites
- Build the JAR: `mvn -DskipTests clean package` (should produce `target/HMS.jar`).
- Ensure DB is running and `src/main/java/util/DBConnect.java` has correct URL/USER/PASSWORD.
- If JavaFX is not on your runtime, use `run-with-javafx.bat` and set `JAVA_FX_LIB`.

Quick test values (examples)
- Patient SSN: `TEST-SSN-001` (use unique values for add/delete)
- Patient name: `Nguyen Van A`, phone: `0123456789`
- Doctor ID / Nurse ID / Medicine ID: check lists in UI or fetch from DB

Checklist
1) Start app
   - Windows: double-click `run.bat` or run in Powershell: `.
\run.bat`
   - If JavaFX required: set `JAVA_FX_LIB` then run `.
\run-with-javafx.bat`.

2) Patients
   - Add: create patient with SSN=`TEST-SSN-001`, Name, DOB (yyyy-mm-dd), phone.
   - Verify patient appears in list.
   - Update: change address or emergency contact, save. Verify change.
   - Delete: delete the patient, confirm removal.

3) Rooms / Beds
   - Add a Room (room no, floor), add a Bed under that room.
   - Assign a patient to bed (if UI supports) and then clear assignment.
   - Update bed (change status/room), delete bed.

4) Nurses / Doctors
   - Add a nurse/doctor; update fields; delete.

5) Medicines
   - Add medicine with name and stock.
   - Update stock (increase/decrease) via UI.
   - Delete medicine.

6) Prescriptions
   - Add prescription referencing existing patient SSN and medicine ID.
   - Try adding with null doctor or drug to verify optional handling.
   - Update dosage/days; delete.

7) Diagnosis
   - Add diagnosis for patient SSN; update result; delete.

What to collect if something fails
- Exact UI action that caused the error.
- Any popup message shown.
- Full stacktrace from terminal (if app prints error there).
- Relevant DB state (e.g., patient SSN missing).

If you prefer, run tests one screen at a time and tell me which screen fails — I will patch the relevant controller/service/DAO.
