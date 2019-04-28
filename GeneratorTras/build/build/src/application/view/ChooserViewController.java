package application.view;

import application.Main;
import application.model.Route;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooserViewController {

	private Main mainApp;

	@FXML
	private Button btNewList;
	@FXML
	private Button btListFromFile;
	@FXML
	private Button btLastUsedList;

	private Stage chooserStage;

	private List<Route> routeList;
	private boolean isListChosen=false;
	private boolean isImportCorrect=false;
	private File selectedFile;

	public File getSelectedFile() {
		return selectedFile;
	}

	public ChooserViewController() {

	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		// tabelaTras.setItems(mainApp.getRoutesList());
		// routeList.addAll(mainApp.getRoutesList());
	}

	@FXML
	private void initialize() { 

	}

	public void setDialogStage(Stage chooserStage) {
		this.chooserStage = chooserStage;
	}
	
	public boolean isListChosen() {
		return isListChosen;
	}
	

	
	@FXML
	private void createNewList() {
		List<Route> tempList = new ArrayList<>();

		boolean OKClicked = mainApp.showEditorView(tempList);
		
	
			//mainApp.setRoutesList(tempList);

	    if (OKClicked) {
        	//mainApp.setRoutesList(tempList);
        	chooserStage.close();
        }

	}
	
	@FXML
	private void chooseFile() {
//		importExternalFile(showFileChooser());
//		if (isImportCorrect){
//			writeToFile();
//			isListChosen = true;
		selectedFile = showFileChooser();
		if(selectedFile!=null) {
			chooserStage.close();
		}
//		}
		
	}

	@FXML
	private void getLastUsedFile() {
		//importResourcesFile("trasy.txt");
		//if (isImportCorrect){
			chooserStage.close();
		//}
	}

	
	private File showFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Importuj listę z pliku");
		File chosenFile = fileChooser.showOpenDialog(chooserStage);
		
		return chosenFile;		
	}

	private boolean isListEmpty(List<Route> list) {
		return list.isEmpty();
	}
	
	
	private void alertEmptyList(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Błędny plik");
        alert.setContentText(errorMessage);
        
        alert.showAndWait();
	}
	
}
