package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import db.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	@FXML
	private TextField usernameTF;
	@FXML
	private TextField passwordTF;
	@FXML
	private Button loginBtn;

	private Preferences prefs;

	private DBHandler dBHandler;

	public void loginAction() {
		String name = usernameTF.getText();
		String pass = passwordTF.getText();

		System.out.println("User name :" + name + " Password : " + pass);

		// varify credential than close

		// https://stackoverflow.com/questions/25037724/how-to-close-a-java-window-with-a-button-click-javafx-project

		// Preferences prefs = Preferences.userRoot().node("db");
		// dBHandler = new DBHandler();
		// String msg = dBHandler.userAuth(name, pass);
		//
		// textMessage.setText(msg);
		// if (msg.toLowerCase().contains("welcome"))
		// openBrowserBtn.setDisable(false);
		// else
		// openBrowserBtn.setDisable(true);

		// closing scene
		Stage stage = (Stage) usernameTF.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		prefs = Preferences.userRoot().node("db");
		usernameTF.setText(prefs.get("user", ""));
		passwordTF.setText(prefs.get("password", ""));

		// loginBtn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// // TODO Auto-generated method stub
		// // setMainMsg(subTextField.getText());
		// System.out.println("login ---- ");
		// Stage stage = (Stage) usernameTF.getScene().getWindow();
		// stage.close();
		// }
		// });

	}

}
