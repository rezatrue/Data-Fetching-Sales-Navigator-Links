package application;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import api.ApiClient;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import pojo.SearchType;
import pojo.WorkType;
import scrapper.LinkedinListMain;
import webhandler.FireFoxOperator;

public class MainController  extends Service<String> implements Initializable {
	@FXML
	private TextField textMessage;
	@FXML
	private TextField textUserId, textDbData;
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

	static boolean status = false;
	
	public static Preferences prefs = null;
	
	//..............................
	@FXML
	private Button btnBrowse, btnRun; // btnLaunch, btnLogin, btnSettings,, btnPrintList 
	@FXML
	private TextField tfSelectedFilePath, tfLimits; // , tfLinkedinId, tfMessageBox

	@FXML
	ToggleGroup dataSourceGadioGroup;
	@FXML
	RadioButton dbRadioBtn, csvRadioBtn;

	@FXML
	public void dataSourceBtnAction(ActionEvent event) {
		if(dbRadioBtn.isSelected())
			covertSource("db");
		if(csvRadioBtn.isSelected())
			covertSource("scv");
	}
	
	public void covertSource(String src) {
		if (src.equalsIgnoreCase("db")) {
			System.out.println("Database");
			btnBrowse.setDisable(true);
			tfSelectedFilePath.setDisable(true);
			int num = linkedinListMain.countData(); 
			tfLimits.setText(num+"");
			if(num <= 0) {
				btnRun.setDisable(true);
			}else if(num > 0) {
				btnRun.setDisable(false);
			}
			
		} else if (src.equalsIgnoreCase("scv")){
			System.out.println("CSV file");
			btnBrowse.setDisable(false);
			tfSelectedFilePath.setDisable(false);
		}
	}

	private void convertEnable() {
		dbRadioBtn.setDisable(false);
		csvRadioBtn.setDisable(false);
		btnBrowse.setDisable(true);
		tfSelectedFilePath.setDisable(true);
		btnRun.setDisable(false);
		tfLimits.setDisable(false);		
		covertSource("db");	
	}
	private void convertDisable() {
		dbRadioBtn.setDisable(true);
		csvRadioBtn.setDisable(true);
		btnBrowse.setDisable(true);
		tfSelectedFilePath.setDisable(true);
		btnRun.setDisable(true);
		tfLimits.setDisable(true);
	}
		
	//@FXML
	//private PasswordField pfPassword;
	private int converted = 0;
	//private int totalSalesLink = 0;
	private int listSize = 0;
	@FXML
	private void browseBtnAction(ActionEvent event) {
		System.out.println("Browse Button");
		//stackoverflow.com/questions/25491732/how-do-i-open-the-javafx-filechooser-from-a-controller-class/25491787
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("CSV Files", "*.csv")
			);
		
        File file = fileChooser.showOpenDialog(new Stage());
        
        if(file != null) {
        	String filepath = file.getAbsolutePath();
    		if(filepath.endsWith(".csv")) {
    			int number = linkedinListMain.readCsvFile(filepath); // new 
    			//list = csvFileHandeler.read(filepath);
    			//list.size() == 0
    			if(number == 0) {
    				btnRun.setDisable(true);
    				filepath = "";
    				textMessage.setText("File is not in proper format");
    			}else if(number > 0) {
    				listSize = number;
    				btnRun.setDisable(false);
    				textMessage.setText("List size : "+ listSize);
    				textListSize.setText(listSize+"");
    				tfLimits.setText(100+"");
    			}
    		}
    		tfSelectedFilePath.setText(filepath);
    		//totalSalesLink = linkedinListMain.numberOfSalesLink();
        	//tfLimits.setText(totalSalesLink+"");
        }
        
	}
	
	LinkConversionService linkConversionService;
	@FXML
	private void runBtnAction(ActionEvent event) {
		System.out.println("Run Button");
		
		// checking limits, how many links need to convert
		int limits = 0;
		//totalSalesLink = linkedinListMain.numberOfSalesLink();
		if(!tfLimits.getText().isEmpty()) {
			limits = Integer.parseInt(tfLimits.getText());
			//int numberLimit = Integer.parseInt(tfLimits.getText());
			//limits = (numberLimit > totalSalesLink) ? totalSalesLink : numberLimit;
			//tfLimits.setText(limits+"");
			}
		System.out.println("limits : "+ limits);
		if(limits == 0) {
			textMessage.setText("Limit Zero process terminated");
			return;
		}
		
			
		linkConversionService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Done : " + event.getSource().getValue()) ;
				btnRun.setText(event.getSource().getValue().toString());
				
			}
		});
		
		System.out.println("service Status : " + linkConversionService.getState());
		
		if(btnRun.getText().equals("Run")){
			btnRun.setText("Pause"); 
			String statustxt = linkConversionService.getState().toString();
			if(statustxt == "READY") linkConversionService.start();
			if(statustxt == "SUCCEEDED" || statustxt == "CANCELLED") linkConversionService.restart();
			if(statustxt == "FAILED") { linkConversionService.reset(); linkConversionService.restart(); }
		}
		else if(btnRun.getText().equals("Pause")) {
			btnRun.setText("Run");
			if(linkConversionService.getState().toString() == "RUNNING") linkConversionService.cancel();
			if(linkConversionService.getState().toString() == "FAILED") linkConversionService.reset();
		}
				
				
	}
	
	private class LinkConversionService extends Service<String>{
		
		protected Task<String> createTask() {
			// TODO Auto-generated method stub
			return new Task<String>() {
				
				@Override
				protected String call() throws Exception {
					System.out.println("I am doing assigned task ") ;
					int index = 0; // number of loop iteration / list serial number
					int limits = Integer.parseInt(tfLimits.getText());
					int workCount = 0;
					// loading sales link for first time 
					linkedinListMain.login();
					while (limits != 0 && btnRun.getText().contains("Pause")) {
						
						if(workCount == 81) {
							linkedinListMain.closeBrowser();
							textMessage.setText("");
							textMessage.setText("Browser rebooting...");
							linkedinListMain.launcherBrowser();
							linkedinListMain.login();
							workCount = 0;
						}

						//int res = linkedinListMain.getPublicLinkDetails(index);
						int res = linkedinListMain.getCompanyLinkDetails(index);
						index++;
						//totalSalesLink--;
						if(res == 0) {
							
							}
						if(res == 1) {
							converted++;
							workCount++;
							limits--;
							textMessage.setText(converted + " Links converted, process continues....");
							}
						
						System.out.println("res -->>" + res);
						if (index == listSize || btnRun.getText().contains("Run") || limits <= 0) {
						//if (totalSalesLink == 0 || index == listSize || btnRun.getText().contains("Run") || limits <= 0) {	
							textMessage.setText("Conversion Completed. Total : "+ converted + " links converted.");
							tfLimits.setText(String.valueOf(limits));
							return "Run";
						}
							
					}
					
					return "Run";
				}
			};
		}

	}
	//..............................
	

	@FXML
	private ChoiceBox<String> modeChoiceBox = new ChoiceBox<>();
	private String[] modeChoiceBoxItems = {"List", "Convert"};
	@FXML
	private ChoiceBox<String> taskChoiceBox = new ChoiceBox<>();
	private String[] taskChoiceBoxItems = {"People", "Companies", "Jobs", "Lead results", "Account results"};
	private LinkedinListMain linkedinListMain;
	private ApiClient apiClient; 
	//private int listSize;

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

	MyService runService;
	@FXML
	public void startBtnAction(ActionEvent event) {
		System.out.println("Start Button");

		runService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				// event.getSource().getValue() will take return value from service thread 
				startBtn.setText(event.getSource().getValue().toString());
			}
		});
		
		if (startBtn.getText().contains("Pause")) {
			startBtn.setText("Start");
			openBrowserBtn.setDisable(false);
			System.out.println("--------------paused---------------------" + runService.getState().toString());

			switch(runService.getState().toString()) {
			case "RUNNING":
				runService.cancel();
				break;
			}	
		} else if (startBtn.getText().contains("Start")) {
			startBtn.setText("Pause");
			openBrowserBtn.setDisable(true);
			textCurrentPage.setText(0 + "");
			// calling MyService start / cancel /restart based on getState() 
			System.out.println("--------------started---------------------" + runService.getState().toString());

			System.out.println(runService.getState().toString());
			
			switch(runService.getState().toString()) {
			case "READY":
				runService.start();
				break;
			case "CANCELLED":
			case "SUCCEEDED":
				runService.restart();
				break;
			}

		}

	}

	private class MyService extends Service<String>{

		@Override
		protected Task<String> createTask() {
			
			return new Task<String>() {
				
				@Override
				protected String call() throws Exception {
					boolean run = true;
					boolean autoSelected;
					int currentPage = 0;
					int endPage;
					
					//linkedinListMain.fullPageScroll(); // ---
					//linkedinListMain.salesPageScroll(); // ---
					
						do {
							autoSelected = auto.isSelected();
							textCurrentPage.setText(currentPage + "");
							endPage = Integer.parseInt(textEndPage.getText());
							if (currentPage <= endPage) {
								
								String result = linkedinListMain.startListing(); // "data:25"; // "page:10" // "error:msg"
								int newadded = 0;
								String msg = result.substring(result.indexOf(":"), result.length());
								if(result.startsWith("data")) // newadded = 
									newadded = Integer.parseInt(msg);
								if(result.startsWith("page")) // currentPage = 
									currentPage = Integer.parseInt(msg);
								if(result.startsWith("error"))
									{textMessage.setText(msg); break;}
								
								//int newadded = linkedinListMain.takeList();// ---
								int previousListSize = Integer.parseInt(textListSize.getText());
								listSize = previousListSize + newadded;
								textListSize.setText( listSize + "");
								textCurrentPage.setText(currentPage + "");
								if (autoSelected && currentPage < endPage) {
									currentPage = linkedinListMain.openNextPage(); 
									textMessage.setText("Processing page " + currentPage);
								} else {
									run = false;
								}
								
							}
						} while (autoSelected && startBtn.getText().contains("Pause") && run);
					
					if(!run) textMessage.setText("Process stopped at page " + currentPage);
					openBrowserBtn.setDisable(false);
					textCurrentPage.setText(0 + "");
					return "Start";
				}
			};
			
		}
		
	}
	
	
	@FXML
	private Button openBrowserBtn;

	
	
	@FXML
	public void openBrowserBtnAction(ActionEvent event) {
		
		
		System.out.println("Open Browser Button");
		String buttonText = openBrowserBtn.getText();
	
		if (buttonText.toLowerCase().contains("open")) {
			openBrowserBtn.setText("Close");
			openBrowserBtn.setDisable(true);

			//textListSize.setText(String.valueOf(listSize));
			textListSize.setText(Integer.toString(listSize));
			
			ShowProgressBar showProgress = new ShowProgressBar();

			this.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				
				@Override
				public void handle(WorkerStateEvent event) {
					String status = event.getSource().getValue().toString();
					System.out.println(status);
					if(status == "Done") {
						showProgress.close();
						textMessage.setText("New Browser is lunched, please proceed . . .");
						openBrowserBtn.setDisable(false);
						signInBtn.setDisable(false);
						startBtn.setDisable(false);
						resetBtn.setDisable(false);
					}
				}
			});	
			
			// calling service start / restart based on getState() 
			System.out.println(this.getState().toString());
			
			switch(this.getState().toString()) {
			case "READY":
				this.start();
				break;
			case "CANCELLED":
			case "SUCCEEDED":
				this.restart();
				break;
			}
			
		} else {
			// show an alert message here
			linkedinListMain.signedOut();
			linkedinListMain.closeBrowser();
			openBrowserBtn.setText("Open");
			signInBtn.setDisable(true);
			startBtn.setDisable(true);
			textMessage.setText("Browser is Closed");
		}
		

		
	}

	@FXML
	private Button signInBtn;

	@FXML
	public void signInBtnAction(ActionEvent event) {
		System.out.println("Sign in");
		String userId = textUserId.getText();
		String password = textPassword.getText();
		//String keyword = textKeyword.getText();
		
		String prefUser = prefs.get("linkedinUser", "");
		String prefpass = prefs.get("linkedinPassword", "");
		
		if(userId != prefUser) 
			prefs.put("linkedinUser", userId);
		if (password == prefpass)
			prefs.put("linkedinPassword", password);
		
		if(linkedinListMain.login())
			textMessage.setText("Successfully Sign in");
		else
			textMessage.setText("Sign in failed");
		
		// i will delete later
		//String currentPage = linkedinListMain.searchItemOnPage();


	}

	@FXML
	private Button printListBtn;

	@FXML
	public void printListBtnAction(ActionEvent event) {
		System.out.println("Print List");
		int num = prefs.getInt("unUpdatedListCount", 0);
		prefs.putInt("unUpdatedListCount", 0);
		int remainingLimits =  apiClient.updateUseage(num);
		if(remainingLimits >= 0) {
			System.out.println("listSize-> "+ listSize);//
			int count = linkedinListMain.printList(textKeyword.getText(), listSize);
			textMessage.setText("New CSV file created with " + count + " entity.");
		}
		// this will create problem if picked data limits accessed by 1 , that's why return code modified
		if(remainingLimits == -1) textMessage.setText("Please Check your connection");
		
		if(remainingLimits < -1) {
			
			int printlistSize = listSize + remainingLimits;
			System.out.println(printlistSize + " : -PT");
			if(printlistSize > 0) {
				int count = linkedinListMain.printList(textKeyword.getText(), printlistSize);
				textMessage.setText("New CSV with " + count + " entity has been created. Pls upgrade package for full list");
			}else {
				textMessage.setText("Out of Limits !! Please upgrade your package");
			}
		}
			

	}

	@FXML
	private Button resetBtn; // db data reset

	@FXML
	public void resetBtnAction(ActionEvent event) {
		System.out.println("DB Reset");
		String takatype = linkedinListMain.getTaskType();
		if (clearDataNotification(takatype)) {
			linkedinListMain.clearList();
			textDbData.setText(0+"");
			textMessage.setText("All "+ takatype +" data have been deleted");
		}else {
			textMessage.setText(takatype + " Data remains in Database");
		}

	}

	private boolean clearDataNotification(String takatype) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Please Confirm");
		alert.setHeaderText(takatype + " all data will be removed From Database.");
		alert.setContentText("Do you want to delete?");

		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
			return true;
		return false;
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
		listSize = 0;
		runService = new MyService();
		linkConversionService = new LinkConversionService();
		prefs = Preferences.userRoot().node("db");
		signInBtn.setDisable(true);
		startBtn.setDisable(true);
		openBrowserBtn.setDisable(true);
		
		
		dbRadioBtn.setDisable(true);
		csvRadioBtn.setDisable(true);
		//cbConvertSalesLink.setDisable(false);
		btnBrowse.setDisable(true);
		btnRun.setDisable(true);
		tfSelectedFilePath.setDisable(true);
		tfLimits.setDisable(true);
		guireset();
		
		modeChoiceBox.getItems().addAll(modeChoiceBoxItems);
		modeChoiceBox.setValue(modeChoiceBoxItems[0]);
		modeChoiceBox.setOnAction(e -> modeChoiceBoxSetup(modeChoiceBox));
		
		taskChoiceBox.getItems().addAll(taskChoiceBoxItems);
		taskChoiceBox.setValue(taskChoiceBoxItems[0]);
		taskChoiceBox.setOnAction(e -> taskChoiceBoxSetup(taskChoiceBox));

		textUserId.setText(prefs.get("linkedinUser", ""));
		textPassword.setText(prefs.get("linkedinPassword", ""));

		linkedinListMain = new LinkedinListMain();
		int dbCount = linkedinListMain.setTaskType(SearchType.PEOPLESEARCH);
		textDbData.setText(dbCount+"");
//		Stage stage = (Stage) openBrowserBtn.getScene().getWindow();
//	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//	          public void handle(WindowEvent we) {
//	              System.out.println("Stage is closing");
//	          }
//	      }); 
		
		String msg = authenticateUser();

		//msg = "welcome test";
		textMessage.setText(msg);
		if (msg.toLowerCase().contains("welcome"))
			openBrowserBtn.setDisable(false);
		else
			openBrowserBtn.setDisable(true);

	}

	
	
	private String authenticateUser() {
		
		prefs = Preferences.userRoot().node("db");
		apiClient = new ApiClient();
		String msg = "";
		
		String user = prefs.get("user", "");
		String pass = prefs.get("password", "");
		//return loginDialoag(user, pass); // deprecate
		return msg = apiClient.userAuth(user, pass);
	}
	
	// Not using loginDialoag box 13 April 2021
	// source http://code.makery.ch/blog/javafx-dialogs-official/
	// just copy pest
	private String loginDialoag(String user, String pass) {

		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("LLF Login");
		dialog.setHeaderText("Please Enter your Credentials");
		
		File file = new File("image/login.png");
        Image imageLock = new Image(file.toURI().toString());
        ImageView lockView = new ImageView();
        lockView.setImage(imageLock);
        lockView.setFitHeight(75);
        lockView.setFitWidth(75);
        dialog.setGraphic(lockView);
		
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
		username.setText(user);
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		password.setText(pass);

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

		return apiClient.userAuth(username.getText(), password.getText());

	}

	private void modeChoiceBoxSetup(ChoiceBox<String> mChoiceBox) {
		String item = mChoiceBox.getValue();
		WorkType type = WorkType.LIST;
		if (item.equalsIgnoreCase(modeChoiceBoxItems[0])) {
			type = WorkType.LIST;
			}
		if (item.equalsIgnoreCase(modeChoiceBoxItems[1])) {
			type = WorkType.CONVERT;
			}
		linkedinListMain.setWorkMode(type);
	}
	
	private void taskChoiceBoxSetup(ChoiceBox<String> taskChoiceBox) {
		String item = taskChoiceBox.getValue();
		SearchType type = SearchType.PEOPLESEARCH;
		
		if (item == taskChoiceBoxItems[0]) {
			type = SearchType.PEOPLESEARCH;
			}
		if (item == taskChoiceBoxItems[1]) {
			type = SearchType.COMPANIESSEARCH;
			}
		if (item == taskChoiceBoxItems[2]) {
			type = SearchType.JOBSEARCH; 
			}
		if (item == taskChoiceBoxItems[3]) {
			type = SearchType.LEADSEARCH; 
			}
		if (item == taskChoiceBoxItems[4]) {
			type = SearchType.ACCOUNTSEARCH; 
			}
		
		int dbCount = linkedinListMain.setTaskType(type);
		textDbData.setText(dbCount+"");
		
	}

	private void guireset() {
		previousPageBtn.setDisable(true);
		nextPageBtn.setDisable(true);
	}

	@Override
	protected Task<String> createTask() {
		return new Task<String>() {
			
			@Override
			protected String call() throws Exception {
				linkedinListMain.launcherBrowser();
				return "Done";
			}
			
		};
		
	}

}
