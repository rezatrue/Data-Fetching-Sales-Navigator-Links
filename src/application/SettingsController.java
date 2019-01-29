package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
	@FXML
	private TextField txtuserName;
	@FXML
	private PasswordField txtpassword;
	@FXML
	private TextField txtLinkedinUser;
	@FXML
	private PasswordField txtLinkedinPassword;
	@FXML
	private TextField txtprofile;
	@FXML
	private TextField txtdriver;
	@FXML
	private PasswordField txtdbServer;
	@FXML
	private Button saveBtn;

	private Preferences prefs;

	@FXML
	public void saveBtnAction() {
		prefs.put("user", txtuserName.getText());
		prefs.put("password", txtpassword.getText());

		prefs.put("linkedinUser", txtLinkedinUser.getText());
		prefs.put("linkedinPassword", txtLinkedinPassword.getText());

		prefs.put("dataserver", txtdbServer.getText());

		prefs.put("profilename", txtprofile.getText());
		prefs.put("geckodriverlocation", txtdriver.getText());

		Stage stage = (Stage) txtuserName.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		prefs = Preferences.userRoot().node("db");
		txtuserName.setText(prefs.get("user", ""));
		txtpassword.setText(prefs.get("password", ""));
		txtLinkedinUser.setText(prefs.get("linkedinUser", ""));
		txtLinkedinPassword.setText(prefs.get("linkedinPassword", ""));
		txtdbServer.setText(prefs.get("dataserver", ""));
		
		txtprofile.setText(prefs.get("profilename", ""));
		txtdriver.setText(prefs.get("geckodriverlocation", ""));
		

	}

}
