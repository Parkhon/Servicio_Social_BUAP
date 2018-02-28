package parkhon;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import parkhon.data_structures.FormOutputManager;
import parkhon.logic.FormCreatorLogic;
import parkhon.logic.StyleReader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			TitledPane root = (TitledPane)FXMLLoader.load(Main.class.getResource("MainScreen.fxml"));
			//WebView:
			WebView wb = new WebView();
			WebEngine wbe = wb.getEngine();
			wbe.load("http://www.google.com");
			
			//-------------
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//Loading
		//Preparing to read the styles
		StyleReader reader = new StyleReader();
		boolean stylesCheck = FormCreatorLogic.readStyles(reader);
		//Creating tmp directory
		FormOutputManager.initialize();
		//--------------
		//Launching GUI
		if(stylesCheck)
		{
			//Only launch app if the styles were correcty loaded.
			launch(args);
		}
		else
		{
			//Else an error was encountered, and the application should not launch. See if I can launch a fatal
			//alert informing the user of the problem.
			//Did not trigger when no default was found.
			System.out.println("The program failed to read the styles. Either there are none, there is no default style, or some other unknown"
					+ " problem has occured.");
		}
	}
}
