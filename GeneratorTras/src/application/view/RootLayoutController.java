package application.view;

import application.Main;
import application.model.Route;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RootLayoutController {
	
	private Main mainApp;
	
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void handleNew() {
		List<Route> tempList = new ArrayList<>();
		mainApp.showEditorView(tempList);

	}
	
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Importuj listę z pliku");
		File chosenFile = fileChooser.showOpenDialog(Main.getPrimaryStage());
		
		if(chosenFile!=null) {
			mainApp.importExternalFile(chosenFile);
		}
	}
	
	@FXML
	private void handleEdit() {
		mainApp.showEditorView(mainApp.getRoutesList());
	}
	
	@FXML
	private void handleSave() {
		mainApp.chooseTxtFile(mainApp.getRoutesList());
	}
	
	@FXML
	private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Generator Tras");
        alert.setHeaderText(null);
        alert.setContentText("Autor: Joanna Karpińska\nWersja: 1.0");

        alert.showAndWait();
	}
	
	@FXML
	private void handleClose() {
		System.exit(0);
	}

}
