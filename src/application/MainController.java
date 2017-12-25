package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class MainController implements Initializable {
	@FXML
	private TextField textFileName;
	@FXML
	private TextField textNumber;

	@FXML
	public void selectFileBtnAction(ActionEvent event) {
		System.out.println("Selcted Button");
		textFileName.setText("FILE NAME");
	}

	@FXML
	public void startBtnAction(ActionEvent event) {
		System.out.println("Start Button : " + textNumber.getText().toString());
	}

	@FXML
	public void settingBtnAction(ActionEvent event) {
		System.out.println("Setting Button");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
