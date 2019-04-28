package application.view;

import application.Main;
import application.model.Draw;
import application.model.Route;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

public class GeneratorViewController {
	private Main mainApp;
	private List<Route> routesList = new ArrayList<>();
	private List<Route> drawnRoutesList =  new ArrayList<>();
	
	@FXML
	private TableView<Route> tableRoutes;
	@FXML
	private TableColumn<Route, String> colName;
	@FXML
	private TableColumn<Route, Double> colLength;
	@FXML
	private TextField txtDays;
	@FXML
	private TextField txtMinKm;
	@FXML
	private TextField txtMaxKm;
	@FXML
	private RadioButton rdDays;
	@FXML
	private RadioButton rdKm;
	@FXML
	private CheckBox chDelta;
	@FXML
	private Button btGenerate;
	@FXML
	private Button btSaveFile;
	
	
	
	
	public GeneratorViewController() {
		
	}
	

	
	public void setMainApp(Main mainApp) {
		  this.mainApp = mainApp; 
		  //tableRoutes.setItems(mainApp.getRoutesList());
		  routesList.addAll(mainApp.getRoutesList());
		  //isListEmpty(routesList);
		  
	  }
	  

	
	  
	 
	
	@FXML
	private void initialize() {
        colName.setCellValueFactory(cellData -> cellData.getValue().getName());
        colLength.setCellValueFactory(cellData -> cellData.getValue().getLength().asObject());
        
        checkInputIsNumber(txtDays);
        checkInputIsNumber(txtMinKm);
        checkInputIsNumber(txtMaxKm);
        
	}
	
	private void checkInputIsNumber(TextField input) {
		input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                input.setText(oldValue);
            }
        });
	}
	
	@FXML
	private void handleGenerateBt() {
		if(mainApp.isListUpdated()) {
			mainApp.synchList(routesList);
		}
		if (isInputValid()) {
			showList(runGenerator());
		}
		
	}
	
	
	private boolean isInputValid() {
		String errorMessage="";
		if(rdDays.isSelected()) {
			if(txtDays.getText()==null || txtDays.getText().length()==0) {
				errorMessage += "Wprowadź liczbę dni\n";
			}
		}
		else if(rdKm.isSelected()) {
			if(txtMinKm.getText()==null || txtMinKm.getText().length()==0){
				errorMessage += "Wprowadź minimum kilometrów\n";
			}
			if(txtMaxKm.getText()==null || txtMaxKm.getText().length()==0){
				errorMessage += "Wprowadź maximum kilometrów\n";
			}
		}
		else {
			errorMessage += "Wybierz metodę generowania\n";
		}

		if(errorMessage.length()==0) {
			return true;
		}
		else {
            Alert alert = new Alert(AlertType.ERROR);
            //alert.initOwner(stage);
            alert.setTitle("Brakujące dane");
            alert.setHeaderText("Wprowadź brakujące informacje:");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
		}
		return false;
	}
	
	public void showList(List<Route> routeList) {
		if(routeList!=null) {
			tableRoutes.setItems((ObservableList<Route>) routeList);
		}
	}
	
	
	private List<Route> runGenerator() {
		boolean deltaSelected = chDelta.isSelected();
		
		if(rdDays.isSelected()) {
			double days = Double.parseDouble(txtDays.getText());
			Draw generateRoutes = new Draw(days, routesList, deltaSelected);
			drawnRoutesList = generateRoutes.getDrawnRoutes();
		}
		else if(rdKm.isSelected()) {
			double minKm = Double.parseDouble(txtMinKm.getText());
			double maxKm = Double.parseDouble(txtMaxKm.getText());
			Draw generateRoutes = new Draw(minKm, maxKm, routesList, deltaSelected);
			drawnRoutesList = generateRoutes.getDrawnRoutes();
		}
		
		return drawnRoutesList;
	}
	
	@FXML
	private void handleSaveBt() {
		mainApp.saveToExcel(drawnRoutesList, mainApp.chooseFile());
	}
	
//	@FXML
//	private void handleSaveBt() {
//        FileChooser fileChooser = new FileChooser();
//        
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
//
//        if (file != null) {
//            saveTableToFile(drawnRoutesList, file);
//        }
//	}
//	
//    private void saveTableToFile(List<Route> lista, File file) {
//        try {
//            PrintWriter writer;
//            writer = new PrintWriter(file);
//
//            for (Route tr : lista) {
//                writer.println(tr.printRoute());
//            }
//            writer.close();
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
