package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class LoadingProgressBar  implements Initializable {
	
	@FXML
	private ProgressBar pb; 
	
	
	public void closeProgressBar() {
		
		for(int i = 10 ; i < 100 ; i +=10) {
			pb.setProgress((double)i/100);
			System.out.println((double)i/100);
			
		}
	
		
//		Stage stage = (Stage) pb.getScene().getWindow();
//		stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		System.out.println("11");
	    pb = new ProgressBar(0.25F);		
	    closeProgressBar();
	}

	

}
