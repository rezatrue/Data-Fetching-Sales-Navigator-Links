package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.hamcrest.core.IsNull;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class LoadingProgressBar implements Initializable {
	
	@FXML
	private ProgressBar progressBar; 
	@FXML
	private Label labelTf; 
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//progressBar.setProgress(-1.0f);
		progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		
	}

	

}
