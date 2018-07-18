package application;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import db.DBHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import scrapper.LinkedinListMain;
import webhandler.FireFoxOperator;

public class MainController implements Initializable {
	@FXML
	private TextField textMessage;
	@FXML
	private TextField textUserId;
	@FXML
	private PasswordField textPassword;
	@FXML
	private TextField textKeyword;
	@FXML
	private TextField textCurrentPage;
	@FXML
	private TextField textEndPage;
	@FXML
	private TextField textListSize;

	@FXML
	private TextField textFileName;
	@FXML
	private TextField textNumber;

	private Preferences prefs = null;

	// private LinkedList<Info> list = null;
	@FXML
	private ChoiceBox<String> choiceBox = new ChoiceBox<>();
	private String[] choiceBoxItems = { "Profile Search", "Sales Nav", "Company Profile" };
	private LinkedinListMain linkedinListMain;
	private DBHandler dBHandler;
	@FXML
	private Button selectFileBtn;

	@FXML
	public void selectFileBtnAction(ActionEvent event) {
		// activate convertBtn when btn clicked
		// convertBtn.setDisable(true);
		// System.out.println("Selcted Button");
		// textFileName.setText("FILE NAME");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(new Stage());
		String filePath = file.getAbsolutePath();
		textFileName.setText(filePath);
		String msg = linkedinListMain.scanCSV(filePath);
		if (msg.contains("listsize")) {
			convertBtn.setDisable(false);
			textNumber.setText("0");
		}
		textMessage.setText(msg);
	}

	@FXML
	private Button convertBtn;

	@FXML
	public void convertBtnAction(ActionEvent event) {
		int count = Integer.parseInt(textNumber.getText());
		System.out.println(" : " + count);
		if (count > 0) {
			textMessage.setText(linkedinListMain.getPublicLink(count));
			convertBtn.setDisable(true);
		} else
			textMessage.setText("Please enter how many links you want to convert");
	}

	@FXML
	private Button settingBtn;

	@FXML
	public void settingBtnAction(ActionEvent event) {
		System.out.println("Setting Button");
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/application/Settings.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(new Scene(parent));
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private Button startBtn;

	@FXML
	public void startBtnAction(ActionEvent event) {
		System.out.println("Start Button");

		if (startBtn.getText().contains("Pause")) {
			startBtn.setText("Start");
		} else if (startBtn.getText().contains("Start")) {
			startBtn.setText("Pause");

			Thread runSchedule = new Thread(new Runnable() {
				@Override
				public void run() {
					boolean run = true;
					boolean autoSelected;
					int currentPage;
					int endPage;
					currentPage = linkedinListMain.currentpage();
					do {
						System.out.println("-----11-----");
						autoSelected = auto.isSelected();
						textCurrentPage.setText(currentPage + "");
						endPage = Integer.parseInt(textEndPage.getText());
						if (currentPage <= endPage) {
							System.out.println("-----12-----");
							int newadded = linkedinListMain.takeList();
							String sizeText = textListSize.getText();
							textListSize.setText(Integer.parseInt(sizeText) + newadded + "");
							textMessage.setText("Processing page " + currentPage);
							if (autoSelected && currentPage < endPage) {
								currentPage = linkedinListMain.openNextPage();
								System.out.println("-----13-----");
							} else {
								run = false;
							}

						}
						System.out.println("-----14-----");
					} while (autoSelected && startBtn.getText().contains("Pause") && run);
					startBtn.setText("Start");
					textMessage.setText("Process stopped at page " + currentPage);
				}
			});

			runSchedule.start();
		}

	}

	@FXML
	private Button openBrowserBtn;

	@FXML
	public void openBrowserBtnAction(ActionEvent event) {
		String buttonText = openBrowserBtn.getText();

		System.out.println(buttonText);

		if (buttonText.toLowerCase().contains("open")) {
			openBrowserBtn.setText("Close");

			new Thread(new Runnable() {
				@Override
				public void run() {
					linkedinListMain.launcherBrowser();
					textMessage.setText("New Browser is Opened");
				}
			}).start();

			enterBtn.setDisable(false);
			startBtn.setDisable(false);
			selectFileBtn.setDisable(false);
			printListBtn.setDisable(false);
			resetBtn.setDisable(false);
			choiceBox.setDisable(false);

		} else {
			// show an alert message here

			linkedinListMain.signedOut();
			linkedinListMain.closeBrowser();
			openBrowserBtn.setText("Open");
			enterBtn.setDisable(true);
			startBtn.setDisable(true);
			choiceBox.setDisable(true);
			textMessage.setText("Browser is Closed");
		}

	}

	@FXML
	private Button enterBtn;

	@FXML
	public void enterBtnAction(ActionEvent event) {
		System.out.println("enter");
		String userId = textUserId.getText();
		String password = textPassword.getText();
		String keyword = textKeyword.getText();

		String currentPage = linkedinListMain.searchItemOnPage();

		switch (currentPage) {
		case (FireFoxOperator.LOGINPAGE):
			if (userId.length() > 0 && password.length() > 0) {
				System.out.println("userId / password :-- " + password);
				if (linkedinListMain.login(userId, password))
					textMessage.setText("You are now loggedin");
				else
					textMessage.setText("Login Error");
			} else
				textMessage.setText("No credential");
			break;
		case (FireFoxOperator.SEARCHPAGE):
			if (keyword.length() > 0) {
				System.out.println(" keyword :-- " + keyword);
				if (linkedinListMain.search(keyword))
					textMessage.setText("Search Completed");
				else
					textMessage.setText("Search Error");
			} else
				textMessage.setText("No Keyword");
			break;
		default:
			textMessage.setText("Error in the page please check");
			break;
		}

	}

	@FXML
	private Button printListBtn;

	@FXML
	public void printListBtnAction(ActionEvent event) {
		System.out.println("Print List");
		int count = linkedinListMain.printList(textKeyword.getText());
		textMessage.setText("New CSV file created with " + count + " entity.");

	}

	@FXML
	private Button resetBtn;

	@FXML
	public void resetBtnAction(ActionEvent event) {
		System.out.println("Reset");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Please Confirmation");
		alert.setHeaderText("All collected data will be removed");
		alert.setContentText("Are you ok with this?");

		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			int listSize = linkedinListMain.clearList();
			if (listSize == 0)
				textListSize.setText(String.valueOf(listSize));
			textCurrentPage.setText("0");
			textEndPage.setText("25");
			choiceBox.setValue(choiceBoxItems[0]);
			linkedinListMain.setProfileMode(choiceBoxItems[0]);
			textMessage.setText("All data deleted & Profile reset");
			reset();
		}

	}

	@FXML
	private CheckBox auto;

	@FXML
	public void autoCheckBoxAction(ActionEvent event) {
		if (auto.isSelected()) {
			previousPageBtn.setDisable(true);
			nextPageBtn.setDisable(true);
		} else {
			previousPageBtn.setDisable(false);
			nextPageBtn.setDisable(false);
		}
	}

	@FXML
	private Button previousPageBtn;

	@FXML
	public void previousPageBtnAction(ActionEvent event) {
		System.out.println("Previous Page");
		linkedinListMain.openPreviousPage();
	}

	@FXML
	private Button nextPageBtn;

	@FXML
	public void nextPageBtnAction(ActionEvent event) {
		System.out.println("Next Page");
		linkedinListMain.openNextPage();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		prefs = Preferences.userRoot().node("db");
		enterBtn.setDisable(true);
		startBtn.setDisable(true);
		selectFileBtn.setDisable(true);
		convertBtn.setDisable(true);
		printListBtn.setDisable(true);
		resetBtn.setDisable(true);
		openBrowserBtn.setDisable(true);

		choiceBox.getItems().addAll(choiceBoxItems);
		choiceBox.setValue(choiceBoxItems[0]);
		choiceBox.setOnAction(e -> choiceBoxSetup(choiceBox));
		choiceBox.setDisable(true);
		reset();

		textUserId.setText(prefs.get("linkedinUser", ""));
		textPassword.setText(prefs.get("linkedinPassword", ""));

		linkedinListMain = new LinkedinListMain();
		/*
		 * try { Parent parent =
		 * FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
		 * Stage stage = new Stage(); stage.setTitle("Please login");
		 * stage.setScene(new Scene(parent)); stage.setResizable(false);
		 * stage.show(); } catch (Exception e) { e.printStackTrace(); }
		 */

		String msg = loginDialoag();

		textMessage.setText(msg);
		if (msg.toLowerCase().contains("welcome"))
			openBrowserBtn.setDisable(false);
		else
			openBrowserBtn.setDisable(true);

	}

	// source http://code.makery.ch/blog/javafx-dialogs-official/
	// just copy pest

	private String loginDialoag() {
		prefs = Preferences.userRoot().node("db");
		dBHandler = new DBHandler();
		String msg = "";

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("Look, a Custom Login Dialog");

		// Set the icon (must be included in the project).
		// dialog.setGraphic(new
		// ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		username.setText(prefs.get("user", ""));
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		password.setText(prefs.get("password", ""));

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
		});

		return msg = dBHandler.userAuth(username.getText(), password.getText());

	}

	private Object choiceBoxSetup(ChoiceBox<String> choiceBox) {
		String item = choiceBox.getValue();
		System.out.println(item);
		String type;
		if (item == choiceBoxItems[1])
			type = "salesnavleads";
		else if (item == choiceBoxItems[2])
			type = "salesnavaccounts";
		else
			type = "profilesearch";
		linkedinListMain.setProfileMode(type);
		return null;
	}

	protected void reset() {
		previousPageBtn.setDisable(true);
		nextPageBtn.setDisable(true);
	}

}
