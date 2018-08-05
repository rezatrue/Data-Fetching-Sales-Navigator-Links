package application;

import java.io.File;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ShowProgressBar {
	
	Stage stage;
	
	public ShowProgressBar() {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/application/LoadingProgressBar.fxml"));
			stage = new Stage();
			stage.setTitle("Please wait");
			stage.setScene(new Scene(parent));
			stage.getIcons().add(new Image(new File("image/linkedin_icon.gif").toURI().toString()));
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public boolean close() {
		stage.close();
		return true;
	}
	
	
}
