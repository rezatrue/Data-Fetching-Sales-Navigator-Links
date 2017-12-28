package application;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {
	@FXML
	private TextField textFileName;
	@FXML
	private TextField textNumber;

	private LinkedList<Info> list = null;

	@FXML
	public void selectFileBtnAction(ActionEvent event) {
		// System.out.println("Selcted Button");
		// textFileName.setText("FILE NAME");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(new Stage());
		String filePath = file.getAbsolutePath();

		CSV_Scanner csv_Scanner = new CSV_Scanner();
		if (filePath.endsWith(".csv")) {
			list = csv_Scanner.dataScan(filePath);
			textFileName.setText(filePath);
		} else
			System.out.println("Not a CSV FILE");

		// possible errors: file is not properly formated
		// wrong file
		// null file / no data

	}

	@FXML
	public void startBtnAction(ActionEvent event) {
		// System.out.println("Start Button : " +
		// textNumber.getText().toString());

		System.out.println(list.get(0).getLink());
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
