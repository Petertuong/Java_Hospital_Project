package ui;

import model.Facility.Medicine;
import service.FacilityService.MedicineService;
import ui.util.TableUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MedicineController extends BaseController implements ReadOnlyController {

    private boolean readOnlyMode = false;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtStock;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete, btnClear;

    @FXML
    private TableView<Medicine> tblMedicines;
    @FXML
    private TableColumn<Medicine, String> colId;
    @FXML
    private TableColumn<Medicine, String> colName;
    @FXML
    private TableColumn<Medicine, String> colStock;

    @FXML
    private Label lblMessage;

    private final ObservableList<Medicine> data = FXCollections.observableArrayList();
    private final MedicineService service = new MedicineService();

    @FXML
    public void initialize() {
        // cột -> property via TableUtil
        TableUtil.setStringColumn(colId, m -> String.valueOf(m.getDrugID()));
        TableUtil.setStringColumn(colName, Medicine::getDrugName);
        TableUtil.setStringColumn(colStock, m -> String.valueOf(m.getQuantity()));

        tblMedicines.setItems(data);

        // id không cho sửa
        if (txtId != null) {
            txtId.setEditable(false);
        }

        loadMedicinesFromServer();

        // khi chọn 1 dòng -> đổ lên form
        tblMedicines.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showDetails(newSel)
        );

        updateButtons();
        if (lblMessage != null) lblMessage.setText("");
    }

    // Read-Only Mode Implementation

    @Override
    public void setReadOnlyMode(boolean readOnly) {
        this.readOnlyMode = readOnly;
        
        if (readOnly) {
            // STAFF MODE: READ-ONLY access
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            btnClear.setDisable(true);
            
            txtName.setEditable(false);
            txtStock.setEditable(false);
            
            lblMessage.setText("READ-ONLY MODE: Staff can view but not modify medicine data");
            lblMessage.setStyle("-fx-text-fill: #2c3e50; -fx-font-style: italic;");
        } else {
            // 👑 ADMIN MODE: FULL access
            lblMessage.setText("");
        }
    }

    private void loadMedicinesFromServer() {
        try {
            java.util.ArrayList<Medicine> list = service.listMedicine();
            data.setAll(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Load Medicines Error", "Cannot load medicines.\n\n" + e.getMessage());
        }
    }

    private void showDetails(Medicine m) {
        if (m == null) {
            clearForm();
        } else {
            if (txtId != null) txtId.setText(String.valueOf(m.getDrugID()));
            txtName.setText(m.getDrugName());
            txtStock.setText(String.valueOf(m.getQuantity()));
        }
        updateButtons();
        if (lblMessage != null) lblMessage.setText("");
    }

    private void updateButtons() {
        boolean selected = tblMedicines.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!selected);
        btnDelete.setDisable(!selected);
    }

    private void clearForm() {
        if (txtId != null) txtId.clear();
        txtName.clear();
        txtStock.clear();
        tblMedicines.getSelectionModel().clearSelection();
        updateButtons();
        if (lblMessage != null) lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private String validateForm() {
        String name = txtName.getText();
        if (name == null || name.trim().isEmpty()) {
            return "Name is required.";
        }
        String stockText = txtStock.getText();
        if (stockText == null || stockText.trim().isEmpty()) {
            return "Stock is required.";
        }
        if (!stockText.trim().matches("\\d+")) {
            return "Stock must be a non-negative number.";
        }
        return null;
    }

    @FXML
    private void handleAddMedicine() {
        String error = validateForm();
        if (error != null) {
            if (lblMessage != null) lblMessage.setText(error);
            return;
        }
        String name = txtName.getText().trim();
        int stock = Integer.parseInt(txtStock.getText().trim());

        Medicine m = new Medicine(name, stock);
        try {
            Medicine created = service.addMedicine(m);
            if (created == null) {
                showErrorAlert("Add Medicine Error", "Cannot add medicine.\n\nUnknown error");
                return;
            }
            data.add(created);
            tblMedicines.getSelectionModel().select(created);
            if (lblMessage != null) lblMessage.setText("Medicine added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Add Medicine Error", "Cannot add medicine.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateMedicine() {
        Medicine selected = tblMedicines.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (lblMessage != null) lblMessage.setText("No medicine selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            if (lblMessage != null) lblMessage.setText(error);
            return;
        }

        int newStock = Integer.parseInt(txtStock.getText().trim());
        int current = selected.getQuantity();
        try {
            if (newStock > current) {
                Medicine delta = new Medicine(selected.getDrugID(), selected.getDrugName(), newStock - current);
                service.fillMedicineStock(delta);
            } else if (newStock < current) {
                Medicine delta = new Medicine(selected.getDrugID(), selected.getDrugName(), current - newStock);
                service.decreaseMedicineStock(delta);
            }
            // update name if changed
            if (!selected.getDrugName().equals(txtName.getText().trim())) {
                selected.setDrugName(txtName.getText().trim());
                service.addMedicine(selected); // best-effort persist; DAO update will handle if implemented
            }
            // refresh list from server
            loadMedicinesFromServer();
            if (lblMessage != null) lblMessage.setText("Medicine updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Update Medicine Error", "Cannot update medicine.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteMedicine() {
        Medicine selected = tblMedicines.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (lblMessage != null) lblMessage.setText("No medicine selected.");
            return;
        }

        this.showConfirm("Delete Medicine", "Delete this medicine?").ifPresent(res -> {
            if (res == ButtonType.OK) {
                try {
                    Integer r = service.deleteMedicine(selected.getDrugID());
                    if (r == null || r < 0) {
                        showErrorAlert("Delete Medicine Error", "Cannot delete medicine.\n\nService returned error.");
                        return;
                    }
                    data.remove(selected);
                    clearForm();
                    if (lblMessage != null) lblMessage.setText("Medicine deleted successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Delete Medicine Error", "Cannot delete medicine.\n\n" + e.getMessage());
                }
            }
        });
    }

    
}
