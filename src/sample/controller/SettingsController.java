package sample.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SettingsController implements Initializable {
    Preferences preferences;
    @FXML
    public Button okButton;
    @FXML
    public Button applyButton;
    @FXML
    public TextField urlTextField, loginTextField;
    @FXML
    public PasswordField passwordTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preferences = Preferences.userNodeForPackage(Main.class);
        urlTextField.setText(preferences.get("url", "Url"));
        loginTextField.setText(preferences.get("login", "Login"));
        passwordTextField.setText(preferences.get("password", "Password"));

    }

    // OK button
    public void onOK() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }


    public void onApply() {
        preferences.put("url", urlTextField.getText());
        preferences.put("login", loginTextField.getText());
        preferences.put("password", passwordTextField.getText());

        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close();
    }
}
