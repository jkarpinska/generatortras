package application;

import application.model.Route;
import application.view.ChooserViewController;
import application.view.EditorViewController;
import application.view.GeneratorViewController;
import application.view.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {
    private static Stage primaryStage;
    private BorderPane rootLayout;
    private boolean isListUpdated;


	private List<Route> routesList = new ArrayList<>();
	//private ObservableList<Route> listaWylosowanych = FXCollections.observableArrayList();
	
	public Main() {
		importResourcesFile();
		showChooserView();
	}
	
	public List<Route> getRoutesList() {
		return routesList;
	}
	
	public void setRoutesList(List<Route> tempList){
		clearRoutesList();
		routesList.addAll(tempList);
	}
	 
	public void clearRoutesList() {
		routesList.clear();
	}
	
	public boolean isListUpdated() {
		return isListUpdated;
	}
	
	public void synchList(List<Route> list) {
		list.clear();
		list.addAll(getRoutesList());
		isListUpdated = false;
	}
	
	private void importResourcesFile() {
    	clearRoutesList();
    	
		String readFile="";
		
		try {
			
			ClassLoader cLoader = getClass().getClassLoader();
			InputStream input = cLoader.getResourceAsStream("trasy.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF8"));
			
			while ((readFile = br.readLine()) != null) {
				String[] item = readFile.split(",");
				routesList.add(new Route(item[0], Double.parseDouble(item[1])));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
			alertEmptyList("Nie znalezino pliku");
		} catch (IOException e) {
			System.out.println("Error: Incorrect file");
			alertEmptyList("Błędny plik");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: File is empty");
			alertEmptyList("Plik jest pusty");
		}
		}
	

	public void importExternalFile(File file) {
    	clearRoutesList();
    	
		BufferedReader reader;
		String readFile = "";
		
		try {
			reader = new BufferedReader(new InputStreamReader( new FileInputStream(file), "UTF8"));
			
			while ((readFile = reader.readLine()) != null) {
				String[] item = readFile.split(",");
				routesList.add(new Route(item[0], Double.parseDouble(item[1])));
			}
			if (isListEmpty(routesList)){
				alertEmptyList("Plik jest pusty");
			}
			else {
	        	writeToFile();
			}

		} catch (NumberFormatException | IOException e) {
			alertEmptyList("Wystąpił problem plikiem. Wybierz inny.");
			e.printStackTrace();
		} 
	}
	
	private void writeToFile() {
	    try {
			PrintWriter writer = new PrintWriter(new File(this.getClass().getResource("trasy.txt").getPath()));
			for (Route i : routesList) {
				writer.write(i.printRoute()+"\n");
				
			}
		} catch (FileNotFoundException e) {
			alertEmptyList("Nie znaleziono pliku docelowego");
			e.printStackTrace();
		}
	}
	

	public File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files", "*.xls", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
        
        return file;
	}
	
	
	public void chooseTxtFile(List<Route> list) {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());

        if (file != null) {
            saveToTxt(list, file);
        }
        

	}
	
    private void saveToTxt(List<Route> list, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);

            for (Route rt : list) {
                writer.println(rt.printRoute());
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
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
	
	
	public void showChooserView() {
	    try {

	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("view/ChooserView.fxml"));
	        AnchorPane page = loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Wybierz zestaw tras");
			dialogStage.getIcons().add(new Image("iconCar.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initStyle(StageStyle.UTILITY);
	        dialogStage.setResizable(false);
	        dialogStage.initOwner(primaryStage);
	        
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        ChooserViewController controller = loader.getController();
	        controller.setMainApp(this);
	        controller.setDialogStage(dialogStage);

	        dialogStage.showAndWait();

	        if (controller.isListChosen()) {

	        	
	        	importExternalFile(controller.getSelectedFile());

	        }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	


	public boolean showEditorView(List<Route> list) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/EditorView.fxml"));
			AnchorPane page = loader.load();
			
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edytuj/Stwórz zestaw tras");
			dialogStage.getIcons().add(new Image("file:Resources/iconCar.png"));
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        EditorViewController controller = loader.getController();
	        controller.setMainApp(this);
	        controller.setDialogStage(dialogStage);
	        controller.setRouteList(list);

	        dialogStage.showAndWait();
	        
	        if (controller.isOKClicked()) {
	        	setRoutesList(controller.getNewList());
	        	isListUpdated = true;
	        }
	        
	        return controller.isOKClicked();
	        

	        
		} catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
		
	}

	public void initRootLayout() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
		
		try {
			rootLayout = loader.load();
			
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			
			Scene mainScene = new Scene(rootLayout);
			
			primaryStage.setScene(mainScene);
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showGeneratorView() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/GeneratorView.fxml"));
		
		try {
			AnchorPane GeneratorView = loader.load();
			rootLayout.setCenter(GeneratorView);
			
			GeneratorViewController controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
    public static Stage getPrimaryStage() {

		return primaryStage;
    }

	@Override
	public void start(Stage primaryStage) {
		
			Main.primaryStage = primaryStage;
			Main.primaryStage.setTitle("Generator Tras");
			
			primaryStage.getIcons().add(new Image("file:Resources/iconCar.png"));

			initRootLayout();
//			showChooserView();
			showGeneratorView();
			
	}
	
	public static void main(String[] args) {

		launch(args);
	}
	
	
	
	public void saveToExcel(List<Route> list, File outputFile) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Wygenerowane trasy");
	
		HSSFRow headerRow = sheet.createRow((short) 0);
		HSSFCell cellH0 = headerRow.createCell((short) 0);
		cellH0.setCellValue("Trasa");
		HSSFCell cellH1 = headerRow.createCell((short) 1);
		cellH1.setCellValue("Km");
		
		for (int i=0; i<list.size(); i++) {
			HSSFRow row = sheet.createRow((short) i+1);
			HSSFCell cellName = row.createCell((short) 0);
			cellName.setCellValue(list.get(i).getName().getValue());
			HSSFCell cellKm = row.createCell((short) 1);
			cellKm.setCellValue(list.get(i).getLength().getValue());
		}
		
		FileOutputStream output;
		try {
			output = new FileOutputStream(outputFile);
			workbook.write(output);
			output.flush();
			output.close();
		} catch (NullPointerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	
}
