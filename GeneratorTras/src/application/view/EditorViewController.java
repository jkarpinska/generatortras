package application.view;

import application.Main;
import application.model.Route;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class EditorViewController {
	
	private Main mainApp;
	private Stage editorStage;
	private ObservableList<Route> routesList = FXCollections.observableArrayList();
	private boolean isOKClicked=false;
	private Route selectedRoute;
	
	@FXML
	private TableView<Route> tableRoutes;
	@FXML
	private TableColumn<Route, String> colName;
	@FXML
	private TableColumn<Route, Double> colLength;
	@FXML
	private TextArea txtNewName;
	@FXML
	private TextField txtNewKm;
	@FXML
	private TextArea txtEditName;
	@FXML
	private TextField txtEditKm;
	@FXML
	private Button btNewRoute;
	@FXML
	private Button btEditRoute;
	@FXML
	private Button btDeleteRoute;
	@FXML
	private Button btOK;
	@FXML
	private Button btCancel;

	
	public void setMainApp(Main mainApp) {
		  this.mainApp = mainApp;
		  //routesList.addAll(mainApp.getRoutesList());
	}
	
	@FXML
	private void initialize() {
        colName.setCellValueFactory(cellData -> cellData.getValue().getName());
        colLength.setCellValueFactory(cellData -> cellData.getValue().getLength().asObject());
		
		checkInputIsNumber(txtNewKm);
		checkInputIsNumber(txtEditKm);		
		
		showList(routesList);
		
	}
	
	public void setDialogStage(Stage editorStage) {
		this.editorStage = editorStage;
	}
	
	public void setRouteList(List<Route> routesList) {
		this.routesList.addAll(routesList);
		
	}
	
	public List<Route> getNewList(){
		return routesList;
	}
	
	public void showList(List<Route> routesList) {
		if(routesList!=null) {
			tableRoutes.setItems((ObservableList<Route>) routesList);
		    tableRoutes.getSelectionModel().selectedItemProperty().addListener(
		            (observable, oldValue, newValue) -> showRoute(newValue));
		}
	}

	private void checkInputIsNumber(TextField input) {
		input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                input.setText(oldValue);
            }
        });
	}
	
	private void showRoute(Route selectedRoute) {
		this.selectedRoute = selectedRoute;
		
		if (selectedRoute!=null) {
			txtEditName.setText(selectedRoute.getName().getValue());
			txtEditKm.setText(""+selectedRoute.getLength().getValue());

		}
		else {
			txtEditName.setText("");
			txtEditKm.setText("");
//			btEditRoute.setDisable(true);
//			btDeleteRoute.setDisable(true);
		}
	}
	
	public boolean isOKClicked() {
		return isOKClicked;
	}
	
	@FXML
	private void deleteRoute() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Usuwanie trasy");
		alert.setHeaderText(null);
		alert.setContentText("Czy na pewno chcesz usunąć tę trasę?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			routesList.remove(selectedRoute);
		} else {
		    // do nothing
		}
		

	}
	
	@FXML
	private void editRoute() {
		String name = txtEditName.getText();
		double length = Double.parseDouble(txtEditKm.getText());
		
		if (isInputCorrect(name, length)) {
			routesList.set(routesList.indexOf(selectedRoute),new Route(name, length));
		}
	}
	
	@FXML
	private void createNewRoute() {
		String name = txtNewName.getText();
		double length = Double.parseDouble(txtNewKm.getText());
		
		if (isInputCorrect(name, length)) {
			routesList.add(new Route(name, length));
			txtNewName.setText("");
			txtNewKm.setText("");
		}
	}
	
	@FXML
	private void handleOKButton() {
		isOKClicked = true;
		editorStage.close();
	}
	
	@FXML
	private void handleCancelButton() {
		editorStage.close();
	}
	
	private boolean isInputCorrect(String name, double length) {
		String errorMessage="";
		if (name==null || name.length()==0) {
			errorMessage += "Wpisz nazwę trasy\n";
		}
		if (length==0) {
			errorMessage += "Wpisz długość trasy\n";
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
	


}
