package application;
	
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.view.ChooserViewController;
import application.view.EditorViewController;
import application.view.GeneratorViewController;
import application.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class MainApp extends Application {
    private static Stage primaryStage;
    private BorderPane rootLayout;


	private List<Trasa> listaTras = new ArrayList<>();
	//private ObservableList<Trasa> listaWylosowanych = FXCollections.observableArrayList();
	
	public MainApp() {
		importResourcesFile("trasy.txt");
		showChooserView();
	}
	
	public List<Trasa> getListaTras() {
		return listaTras;
	}
	
	public void setListaTras(List<Trasa> tempList){
		clearListaTras();
		listaTras.addAll(tempList);
	}
	 
	public void clearListaTras() {
		listaTras.clear();
	}
	
	private void importResourcesFile(String path) {
    	clearListaTras();
    	
		String readFile="";
		
		try {
			
			ClassLoader cLoader = getClass().getClassLoader();
			InputStream input = cLoader.getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF8"));
			
			while ((readFile = br.readLine()) != null) {
				String[] item = readFile.split(",");
				listaTras.add(new Trasa(item[0], Double.parseDouble(item[1])));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
			alertEmptyList("Nie znalezino pliku");
		} catch (IOException e) {
			System.out.println("Error: Incorrect file");
			alertEmptyList("B��dny plik");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: File is empty");
			alertEmptyList("Plik jest pusty");
		}
		}
	

	public void importExternalFile(File file) {
    	clearListaTras();
    	
		BufferedReader reader;
		String readFile = "";
		
		try {
			reader = new BufferedReader(new InputStreamReader( new FileInputStream(file), "UTF8"));
			
			while ((readFile = reader.readLine()) != null) {
				String[] item = readFile.split(",");
				listaTras.add(new Trasa(item[0], Double.parseDouble(item[1])));
			}
			if (isListEmpty(listaTras)){
				alertEmptyList("Plik jest pusty");
			}
			else {
	        	writeToFile();
			}

		} catch (NumberFormatException | IOException e) {
			alertEmptyList("Wyst�pi� problem plikiem. Wybierz inny.");
			e.printStackTrace();
		} 
	}
	
	private void writeToFile() {
	    try {
			PrintWriter writer = new PrintWriter(new File(this.getClass().getResource("trasy.txt").getPath()));
			for (Trasa i : listaTras) {
				writer.write(i.printTrasa()+"\n");
				
			}
		} catch (FileNotFoundException e) {
			alertEmptyList("Nie znaleziono pliku docelowego");
			e.printStackTrace();
		}
	}
	

	public void saveToFile(List<Trasa> list) {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(MainApp.getPrimaryStage());

        if (file != null) {
            saveTableToFile(list, file);
        }
	}
	
    private void saveTableToFile(List<Trasa> lista, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);

            for (Trasa tr : lista) {
                writer.println(tr.printTrasa());
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	private boolean isListEmpty(List<Trasa> lista) {
		return lista.isEmpty();
	}
	
	private void alertEmptyList(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("B��dny plik");
        alert.setContentText(errorMessage);
        
        alert.showAndWait();
	}
	
	
	public void showChooserView() {
	    try {

	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/ChooserView.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Wybierz zestaw tras");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
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
	


	public boolean showEditorView(List<Trasa> lista) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EditorView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edytuj/Stw�rz zestaw tras");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        EditorViewController controller = loader.getController();
	        controller.setMainApp(this);
	        controller.setDialogStage(dialogStage);
	        controller.setRouteList(lista);

	        dialogStage.showAndWait();
	        
	        if (controller.isOKClicked()) {
	        	setListaTras(controller.getNewList());
	        	System.out.println(listaTras);
	        }
	        
	        return controller.isOKClicked();
	        

	        
		} catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
		
	}

	public void initRootLayout() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
		
		try {
			rootLayout = (BorderPane) loader.load();
			
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
		loader.setLocation(MainApp.class.getResource("view/GeneratorView.fxml"));
		
		try {
			AnchorPane GeneratorView = (AnchorPane) loader.load();
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
		
			MainApp.primaryStage = primaryStage;
			MainApp.primaryStage.setTitle("Generator Tras");
			
			primaryStage.getIcons().add(new Image("carIcon.png"));
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			initRootLayout();
//			showChooserView();
			showGeneratorView();
			
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
}
