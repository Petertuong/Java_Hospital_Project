package ui;

import model.Treatment.Diagnosis;
import model.Patients.Patient;
import service.TreatmentService.DiagnosisService;
import service.PersonService.PatientService;
import ui.util.TableUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DiagnosisController extends BaseController {

	@FXML
	private TextField txtDiagId;
	@FXML
	private TextField txtSSN;
	@FXML
	private TextArea txtResult;

	@FXML
	private Button btnAdd, btnUpdate, btnDelete, btnClear;

	@FXML
	private TableView<Diagnosis> tblDiagnoses;
	@FXML
	private TableColumn<Diagnosis, String> colDiagId;
	@FXML
	private TableColumn<Diagnosis, String> colSSN;
	@FXML
	private TableColumn<Diagnosis, String> colResult;

	@FXML
	private Label lblMessage;

	private final ObservableList<Diagnosis> data = FXCollections.observableArrayList();
	private final DiagnosisService diagnosisService = new DiagnosisService();
	private final PatientService patientService = new PatientService();

	@FXML
	public void initialize() {
		if (txtDiagId != null) txtDiagId.setEditable(false);

		TableUtil.setStringColumn(colDiagId, d -> String.valueOf(d.getDiag_id()));
		TableUtil.setStringColumn(colSSN, d -> d.getPatient() == null ? "" : d.getPatient().getSSN());
		TableUtil.setStringColumn(colResult, Diagnosis::getResult);

		tblDiagnoses.setItems(data);
		loadDiagnosesFromServer();

		tblDiagnoses.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> showDetails(newSel));
		updateButtons();
		if (lblMessage != null) lblMessage.setText("");
	}

	private void loadDiagnosesFromServer() {
		try {
			java.util.ArrayList<Diagnosis> list = diagnosisService.listDiagnosis();
			data.setAll(FXCollections.observableArrayList(list));
		} catch (Exception e) {
			e.printStackTrace();
			showErrorAlert("Load Error", "Cannot load diagnoses.\n\n" + e.getMessage());
		}
	}

	private void showDetails(Diagnosis d) {
		if (d == null) {
			clearForm();
		} else {
			if (txtDiagId != null) txtDiagId.setText(String.valueOf(d.getDiag_id()));
			txtSSN.setText(d.getPatient() == null ? "" : d.getPatient().getSSN());
			txtResult.setText(d.getResult() == null ? "" : d.getResult());
		}
		updateButtons();
		if (lblMessage != null) lblMessage.setText("");
	}

	private void updateButtons() {
		boolean selected = tblDiagnoses.getSelectionModel().getSelectedItem() != null;
		btnUpdate.setDisable(!selected);
		btnDelete.setDisable(!selected);
	}

	private void clearForm() {
		if (txtDiagId != null) txtDiagId.clear();
		txtSSN.clear();
		txtResult.clear();
		tblDiagnoses.getSelectionModel().clearSelection();
		updateButtons();
		if (lblMessage != null) lblMessage.setText("");
	}

	@FXML
	private void handleClearForm() { clearForm(); }

	private String validateForm() {
		String ssn = txtSSN.getText();
		if (ssn == null || ssn.trim().isEmpty()) return "SSN is required.";
		String res = txtResult.getText();
		if (res == null || res.trim().isEmpty()) return "Result is required.";
		return null;
	}

	@FXML
	private void handleAddDiagnosis() {
		String error = validateForm();
		if (error != null) { if (lblMessage != null) lblMessage.setText(error); return; }

		String ssn = txtSSN.getText().trim();
		String res = txtResult.getText().trim();
		try {
			Patient pat = patientService.findPatientById(ssn);
			if (pat == null) { showErrorAlert("Add Error", "Patient with SSN not found: " + ssn); return; }
			Diagnosis d = new Diagnosis(res, pat);
			Diagnosis created = diagnosisService.addDiagnosis(d);
			if (created == null) { showErrorAlert("Add Error", "Cannot add diagnosis."); return; }
			data.add(created);
			tblDiagnoses.getSelectionModel().select(created);
			if (lblMessage != null) lblMessage.setText("Diagnosis added successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			showErrorAlert("Add Error", "Cannot add diagnosis.\n\n" + e.getMessage());
		}
	}

	@FXML
	private void handleUpdateDiagnosis() {
		Diagnosis selected = tblDiagnoses.getSelectionModel().getSelectedItem();
		if (selected == null) { if (lblMessage != null) lblMessage.setText("No diagnosis selected."); return; }
		String error = validateForm();
		if (error != null) { if (lblMessage != null) lblMessage.setText(error); return; }
		try {
			Patient pat = patientService.findPatientById(txtSSN.getText().trim());
			if (pat == null) { showErrorAlert("Update Error", "Patient with SSN not found: " + txtSSN.getText().trim()); return; }
			selected.setPatient(pat);
			selected.setResult(txtResult.getText().trim());
			Integer r = diagnosisService.changeResult(selected);
			if (r == null || r < 0) { showErrorAlert("Update Error", "Cannot update diagnosis."); return; }
			loadDiagnosesFromServer();
			if (lblMessage != null) lblMessage.setText("Diagnosis updated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			showErrorAlert("Update Error", "Cannot update diagnosis.\n\n" + e.getMessage());
		}
	}

	@FXML
	private void handleDeleteDiagnosis() {
		Diagnosis selected = tblDiagnoses.getSelectionModel().getSelectedItem();
		if (selected == null) { if (lblMessage != null) lblMessage.setText("No diagnosis selected."); return; }
		this.showConfirm("Delete Diagnosis", "Delete this diagnosis?").ifPresent(res -> {
			if (res == ButtonType.OK) {
				try {
					Integer r = diagnosisService.deleteDiagnosis(selected.getDiag_id());
					if (r == null || r < 0) { showErrorAlert("Delete Error", "Cannot delete diagnosis."); return; }
					data.remove(selected);
					clearForm();
					if (lblMessage != null) lblMessage.setText("Diagnosis deleted successfully.");
				} catch (Exception e) {
					e.printStackTrace();
					showErrorAlert("Delete Error", "Cannot delete diagnosis.\n\n" + e.getMessage());
				}
			}
		});
	}

}