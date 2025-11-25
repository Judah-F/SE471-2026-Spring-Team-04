package com.weatherboys.ui;

import com.weatherboys.model.ClassInfo;
import com.weatherboys.weatherguard.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminViewController implements Initializable {

    @FXML
    private Button addClassButton;
    @FXML
    private Button removeClassButton;
    @FXML
    private MenuButton viewClassesMenuButton;
    @FXML
    private TableView<ClassInfo> adminTable;
    @FXML
    private TableColumn<ClassInfo, String> classIDColumn;
    @FXML
    private TableColumn<ClassInfo, String> semesterColumn;
    @FXML
    private TableColumn<ClassInfo, String> cityColumn;

    private ObservableList<ClassInfo> classList;
    private DatabaseManager dbManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize database manager
        dbManager = DatabaseManager.getInstance();

        // Initialize observable list
        classList = FXCollections.observableArrayList();

        // Configure TableView columns
        classIDColumn.setCellValueFactory(new PropertyValueFactory<>("classId"));
        semesterColumn.setCellValueFactory(cellData ->
            cellData.getValue().semesterProperty().concat(" ").concat(cellData.getValue().yearProperty().asString()));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        // Load classes from database
        loadClassesFromDatabase();

        // Populate MenuButton with class options
        populateClassMenu();
    }

    /**
     * Loads active classes from the database and populates the TableView
     */
    private void loadClassesFromDatabase() {
        classList.clear();

        // Get only active classes from database (soft delete support)
        for (Document classDoc : dbManager.getActiveClasses()) {
            // Convert Document to ClassInfo
            ClassInfo classInfo = new ClassInfo(
                classDoc.getString("classId"),
                classDoc.getString("className"),
                classDoc.getString("semester"),
                classDoc.getInteger("year", 0),
                classDoc.getString("professorName"),
                classDoc.getString("city"),
                classDoc.getString("startDate"),
                classDoc.getString("endDate")
            );
            classList.add(classInfo);
        }

        // Set the data to TableView
        adminTable.setItems(classList);
    }

    /**
     * Populates the MenuButton with dynamic class options
     */
    private void populateClassMenu() {
        // Clear existing menu items
        viewClassesMenuButton.getItems().clear();

        // Add a MenuItem for each class
        for (ClassInfo classInfo : classList) {
            MenuItem classItem = new MenuItem(classInfo.getClassName() + " (" + classInfo.getClassId() + ")");

            // Navigate to TeacherView when clicked
            classItem.setOnAction(event -> switchToTeacherView(classInfo));

            viewClassesMenuButton.getItems().add(classItem);
        }
    }

    @FXML
    public void addClass(ActionEvent event) {
        // Create file chooser for CSV selection
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Class Roster CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        // Show file chooser
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Upload roster to database
            boolean success = dbManager.uploadRosterCsv(selectedFile.getAbsolutePath());

            if (success) {
                // Refresh the table and menu
                loadClassesFromDatabase();
                populateClassMenu();

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Class roster uploaded successfully!");
            } else {
                // Show error message
                showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to upload class roster. Check logs for details.");
            }
        }
    }

    @FXML
    public void removeClass(ActionEvent event) {
        // Get selected class from table
        ClassInfo selectedClass = adminTable.getSelectionModel().getSelectedItem();

        if (selectedClass == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                "Please select a class to remove.");
            return;
        }

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Remove Class: " + selectedClass.getClassName());
        confirm.setContentText("Are you sure you want to remove this class? (It will be hidden but historical data will be preserved)");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            // Soft delete the class
            boolean success = dbManager.deleteClass(selectedClass.getClassId());

            if (success) {
                // Refresh the table and menu
                loadClassesFromDatabase();
                populateClassMenu();

                showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Class removed successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to remove class. Check logs for details.");
            }
        }
    }

    @FXML
    public void viewClasses(ActionEvent event) {
        // This method is for the MenuButton action itself (if needed)
        // Individual MenuItem actions are handled in populateClassMenu()
    }

    /**
     * Navigates to TeacherView with the selected class data
     */
    private void switchToTeacherView(ClassInfo classInfo) {
        try {
            // Load TeacherView FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TeacherView.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the class data
            TeacherViewController controller = loader.getController();
            controller.setClassInfo(classInfo);

            // Switch to TeacherView
            Stage stage = (Stage) adminTable.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            // e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                "Failed to load TeacherView: " + e.getMessage());
        }
    }

    /**
     * Helper method to show alerts
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
