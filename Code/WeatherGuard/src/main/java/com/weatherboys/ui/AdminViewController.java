package com.weatherboys.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;

import java.net.URL;
import java.util.ResourceBundle;


public class AdminViewController implements Initializable {

    @FXML
    private Button addClassButton;
    @FXML
    private Button removeClassButton;
    @FXML
    private MenuButton viewClassesMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("AdminViewController initialized");

    }

    @FXML
    public void addClass(ActionEvent event) {
        // TODO: Implement addClass logic
    }

    @FXML
    public void removeClass(ActionEvent event) {
        // TODO: Implement removeClass logic
    }

    @FXML
    public void viewClasses(ActionEvent event) {
        // TODO: Implement viewClasses logic
    }

}
