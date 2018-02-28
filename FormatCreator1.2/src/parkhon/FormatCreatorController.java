package parkhon;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parkhon.data_structures.EnclosedProcedureLambda;
import parkhon.data_structures.FormOutputManager;
import parkhon.logic.FormCreatorLogic;
import parkhon.logic.FormProcessor;
import parkhon.logic.FormSymbol;
import parkhon.logic.StyleReader;

public class FormatCreatorController {

	@FXML
	private StackPane p_display;
	
    @FXML
    private Button b_margin;
    
    @FXML
    private Button b_logo;

    @FXML
    private CheckBox c_margin_small;

    @FXML
    private CheckBox c_margin_large;

    @FXML
    private Button b_open_question;

    @FXML
    private CheckBox c_open_question_1;

    @FXML
    private CheckBox c_open_question_2;

    @FXML
    private CheckBox c_open_question_3;

    @FXML
    private Button b_header;

    @FXML
    private Button b_triple_text_box;

    @FXML
    private Button b_label;

    @FXML
    private Button b_submit;

    @FXML
    private Button b_load_form;

    @FXML
    private Button b_save_form;

    @FXML
    private Button b_edit_element_style;

    @FXML
    private Button b_edit_global_style;
    
    @FXML
    private Button b_cursor_back;

    @FXML
    private Button b_cursor_next;
    
    @FXML
    private Button b_delete_element;

    @FXML
    private Button b_edit_element;
    
    //Variables
    private WebView wb;
    FormProcessor formProcessor;
    //--------
    @FXML
    private void initialize()
    {
    	//Initialization
    	formProcessor = new FormProcessor();
    	//Starting the previewer:
    	startPreviewer();
    }
    @FXML
    void onCursorBack(ActionEvent event) {
    	/*
    	 * This method uses lambda to move the cursor.
    	 */
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.moveCursorDown();
    	};
    	reloadPreview(procedure);
    	//----
    }

    @FXML
    void onCursorNext(ActionEvent event) {
    	/*
    	 * This method uses lambda to move the cursor.
    	 */
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.moveCursorUp();
    	};
    	reloadPreview(procedure);
    	//----------
    }
    
    @FXML
    void onEditElementStyle(ActionEvent event) {
    	System.out.println("Edit Element Style");
    	createStyleChooserWindow(false);	//False as it does not aim to change the global configuration
    	handleErrorWindow();
    }

    @FXML
    void onEditGlobalStyle(ActionEvent event) {
    	System.out.println("Edit Global Style");
    	createStyleChooserWindow(true);	//True as it aims to modify the global configuration.
    	handleErrorWindow();
    }

    @FXML
    void onHeader(ActionEvent event) {
    	System.out.println("Header");
    	//Creating the element
    	FormSymbol element = formProcessor.createFormElement("Header");	//The element to add.
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.addNewElement(element);
    	};
    	reloadPreview(procedure);
    	//---
    	handleErrorWindow();
    }

    @FXML
    void onLabel(ActionEvent event) {
    	System.out.println("Label");
    	//Creating the element
    	FormSymbol element = formProcessor.createFormElement("Label");	//The element to add.
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.addNewElement(element);
    	};
    	reloadPreview(procedure);
    	//---
    	//DEBUG
    	formProcessor.createFormElement("OneLinerQuestion");
    	//-------------
    	handleErrorWindow();
    }

    @FXML
    void onLoadForm(ActionEvent event) {
    	System.out.println("Load Form");
    	FileChooser fc = new FileChooser();
    	File file = fc.showOpenDialog(null);
    	formProcessor = new FormProcessor();
    	boolean fileLoaded = FormCreatorLogic.readForm(formProcessor, file);
    	if(fileLoaded)
    	{
    		System.out.println("Success");
			WebEngine we = wb.getEngine();
			//Starting up the previewer:
			boolean previewerReady = FormCreatorLogic.updatePreviewer(true);
			if(previewerReady) {
				String resUrl = FormOutputManager.getResourceURL("preview.html");
				we.load("File:"+resUrl);
				we.reload();//Force reload
			}
    	}
    	handleErrorWindow();
    }

    @FXML
    void onMargin(ActionEvent event) {
    	boolean proceed = true;	//Error invalidating proceed flag
    	System.out.println("Margin");
    	//Check the checkboxes.
    	String elementType = "";
    	if(c_margin_large.isSelected())
    	{
    		elementType = "LargeMargin";
    	} else if (c_margin_small.isSelected())
    	{
    		elementType = "SmallMargin";
    	}
    	else {
    		//ERROR
    		//User error, no elements selected.
    		String error = "Error in form element creation. To create a margin you must select"
    				+ " a value in the right. Either small or large." + System.lineSeparator();
    		ErrorNotifier.pushErrorMessage(error);
    		proceed = false;
    	}
    	if(proceed)
    	{
    		//Creating the apropriate element
	    	FormSymbol element = formProcessor.createFormElement(elementType);
	    	//Adding new element to the form
	    	EnclosedProcedureLambda procedure = () -> {
	    		FormCreatorLogic.addNewElement(element);
	    	};
	    	reloadPreview(procedure); //Recreating the preview with the new addition included.
	    	//
    	}
    	handleErrorWindow();
    }

    @FXML
    void onMarginLarge(ActionEvent event) {
    	System.out.println("Margin Large");
    	//Handling toggles
    	if(c_margin_small.isSelected())
    	{
    		c_margin_small.setSelected(false);
    	} if(!c_margin_large.isSelected())
    	{
    		c_margin_large.setSelected(true);
    	}
    	//
    	handleErrorWindow();
    }

    @FXML
    void onMarginSmall(ActionEvent event) {
    	System.out.println("Margin Small");
    	//Handling toggles
    	if(c_margin_large.isSelected())
    	{
    		c_margin_large.setSelected(false);
    	} if(!c_margin_small.isSelected())
    	{
    		c_margin_small.setSelected(true);
    	}
    	//
    	handleErrorWindow();
    }

    @FXML
    void onOpenQuestion(ActionEvent event) {
    	System.out.println("Open Question");
    	boolean proceed = true;	//Error invalidating proceed flag
    	//Check the checkboxes.
    	String elementType = "";
    	if(c_open_question_1.isSelected())
    	{
    		elementType = "OneLinerQuestion";
    	} else if (c_open_question_2.isSelected())
    	{
    		elementType = "TwoLinerQuestion";
    	} else if (c_open_question_3.isSelected())
    	{
    		elementType ="ThreeLinerQuestion";
    	}
    	else {
    		//ERROR
    		//User error, no elements selected.
    		String error = "Error in form element creation. To create an open"
    				+ " question you must specify the number of inputs"
    				+ " in the right side controls." + System.lineSeparator();
    		ErrorNotifier.pushErrorMessage(error);
    		proceed = false;
    	}
    	if(proceed)
    	{
    		//Creating the apropriate element
	    	FormSymbol element = formProcessor.createFormElement(elementType);
	    	//Adding new element to the form
	    	EnclosedProcedureLambda procedure = () -> {
	    		FormCreatorLogic.addNewElement(element);
	    	};
	    	reloadPreview(procedure); //Recreating the preview with the new addition included.
	    	//
    	}
    	handleErrorWindow();
    }

    @FXML
    void onOpenQuestion1(ActionEvent event) {
    	System.out.println("OQ 1");
    	//Handling toggles
    	if(c_open_question_2.isSelected())
    	{
    		//Untoggle other option
    		c_open_question_2.setSelected(false);
    	}
    	if(c_open_question_3.isSelected())
    	{
    		//Untoggle other option
    		c_open_question_3.setSelected(false);
    	}
    	if(!c_open_question_1.isSelected())
    	{
    		//Self toggle
    		c_open_question_1.setSelected(true);
    	}
    	//
    	handleErrorWindow();
    }

    @FXML
    void onOpenQuestion2(ActionEvent event) {
    	System.out.println("OQ2");
    	//Handling toggles
    	if(c_open_question_1.isSelected())
    	{
    		//Untoggle other option
    		c_open_question_1.setSelected(false);
    	}
    	if(c_open_question_3.isSelected())
    	{
    		//Untoggle other option
    		c_open_question_3.setSelected(false);
    	}
    	if(!c_open_question_2.isSelected())
    	{
    		//Self toggle
    		c_open_question_2.setSelected(true);
    	}
    	//
    	handleErrorWindow();
    }

    @FXML
    void onOpenQuestion3(ActionEvent event) {
    	System.out.println("OQ3");
    	//Handling toggles
    	if(c_open_question_1.isSelected())
    	{
    		//Untoggle other option
    		c_open_question_1.setSelected(false);
    	}
    	if(c_open_question_2.isSelected())
    	{
    		//Untoggle other option
    		c_open_question_2.setSelected(false);
    	}
    	if(!c_open_question_3.isSelected())
    	{
    		//Self toggle
    		c_open_question_3.setSelected(true);
    	}
    	//
    	handleErrorWindow();
    }

    @FXML
    void onSaveForm(ActionEvent event) {
    	System.out.println("Save Form");
    		//Generating the final save
    	createEmailEntryWindow();
    	handleErrorWindow();
    }

    @FXML
    void onSubmit(ActionEvent event) {
    	System.out.println("Submit");
    	//Creating the element
    	FormSymbol element = formProcessor.createFormElement("Submit");	//The element to add.
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.addNewElement(element);
    	};
    	reloadPreview(procedure);
    	//---
    	handleErrorWindow();
    }

    @FXML
    void onTripleTextBox(ActionEvent event) {
    	System.out.println("Triple Text Box");
    	//Creating the element
    	FormSymbol element = formProcessor.createFormElement("TripleBoxText");	//The element to add.
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.addNewElement(element);
    	};
    	reloadPreview(procedure);
    	//---
    	handleErrorWindow();
    }
    
    @FXML
    void onDeleteElement(ActionEvent event) {
    	//Deleting the element instruction
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.deleteElement(); 	//Deletes the element at the cursor position.
    	};
    	reloadPreview(procedure);
    	handleErrorWindow();
    }

    @FXML
    void onEditElement(ActionEvent event) {
    	FormSymbol element = FormCreatorLogic.getFormSymbolAtCursor();
    	createEditElementWindow(element);
    }
    
    @FXML
    void onLogo()
    {
    	System.out.println("Logo");
    	//Creating the element
    	FormSymbol element = formProcessor.createFormElement("Logo");	//The element to add.
    	EnclosedProcedureLambda procedure = () -> {
    		FormCreatorLogic.addNewElement(element);
    	};
    	reloadPreview(procedure);
    	//---
    	handleErrorWindow();
    }
    
    //Controller Methods:
    private void startPreviewer()
    {
    	//WebView:
    	wb = new WebView();
		p_display.getChildren().addAll(wb);
		//-------------
    }
    private void handleErrorWindow()
    {
    	/*
    	 * This method creates error windows detailing whatever went wrong.
    	 * 
    	 * It uses the ErrorNotifier to get its error messages if any errors exist.
    	 * Otherwise it just returns control over to its parent method.
    	 */
    	if(ErrorNotifier.isErrorsExist())	//If there are errors to report.
    	{
    		try {
	    		FXMLLoader windowLoader = new FXMLLoader();
	        	Pane windowRoot = (Pane)windowLoader.load(getClass().getResource("UserError.fxml").openStream());
	        	UserErrorController errorController = windowLoader.getController();
	        	Scene scene = new Scene(windowRoot);
	        	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        	Stage stage = new Stage();
	        	stage.setTitle("Error Screen");
	        	stage.setScene(scene);
	        	//---------
        		//Then launch an error.
        		String error = ErrorNotifier.popErrorMessage();
        		errorController.setErrorMessage(error);
        		ErrorNotifier.resetErrorMessage();
	        	//---------
	        	stage.show();
	        	} 
    		catch(Exception e)
	        	{
	        		System.out.println("Failure to create a style chooser window");
	        		e.printStackTrace();
	        	}
    	}
    }
    private void createStyleChooserWindow(boolean globalConfiguration)
    {
    	try {
    	FXMLLoader windowLoader = new FXMLLoader();
    	SplitPane windowRoot = (SplitPane)windowLoader.load(getClass().getResource("StyleChooser.fxml").openStream());
    	StyleChooserController controller = windowLoader.getController();
    	controller.setGlobalConfiguration(globalConfiguration);
    	Scene scene = new Scene(windowRoot);
    	scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    	Stage stage = new Stage();
    	stage.setTitle("Style Chooser");
    	stage.setScene(scene);
    	stage.show();
    	} catch(Exception e)
    	{
    		System.out.println("Failure to create a style chooser window");
    		e.printStackTrace();
    	}
    }
    private void createEditElementWindow(FormSymbol element)
    {
    	try{
    		FXMLLoader windowLoader = new FXMLLoader();
    		BorderPane windowRoot = (BorderPane)windowLoader.load(getClass().getResource("ElementModifier.fxml").openStream());
    		ElementModifierController controller = windowLoader.getController();
    		controller.uploadFormSymbol(element);
    		Scene scene = new Scene(windowRoot);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.show();
    	} catch(Exception e) {
    		//
    	}
    }
    private void createEmailEntryWindow()
    {
    	try{
    		FXMLLoader windowLoader = new FXMLLoader();
    		BorderPane windowRoot = (BorderPane)windowLoader.load(getClass().getResource("EmailEntry.fxml").openStream());
    		Scene scene = new Scene(windowRoot);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		Stage stage = new Stage();
    		stage.setScene(scene);
    		stage.show();
    	} catch(Exception e) {
    		//
    		e.printStackTrace();
    	}
    }
    private void reloadPreview(EnclosedProcedureLambda process)
    {
    	/*
    	 * This method takes lambda executable code and implements
    	 * whatever work needs to get done in the middle of the 
    	 * preview reloading.
    	 */
    	WebEngine we = wb.getEngine();
    	FormCreatorLogic.handleFormCreation();
    	process.work();
    	boolean previewerReady = FormCreatorLogic.updatePreviewer(true);
		if(previewerReady) {
			String resUrl = FormOutputManager.getResourceURL("preview.html");
			we.load("File:"+resUrl);
			we.reload();//Force reload
		}
    }

}
