package parkhon;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import parkhon.data_structures.FormOutputManager;
import parkhon.logic.FormCreatorLogic;
import parkhon.logic.FormProcessor;
import parkhon.logic.FormSymbol;
import parkhon.logic.StyleSymbol;
public class StyleChooserController {

    //----------------------------------------------
    //----------------------------------------------
	//FXML Attributes
    @FXML
    private Button b_style_accept;

    @FXML
    private Button b_style_cancel;

    @FXML
    private ChoiceBox<String> cb_style_picker;
    
    @FXML
    private StackPane p_display;
    //----------------------------------------------
    //----------------------------------------------
    //Attributes
    private ArrayList<String> stylenames;
    private ObservableList<String> ol;		//The list of our choicebox.
    private String selectedStyleCSS;	//The currently selected style.
    private String selectedStyleName;	//The style name of the selection only.
    private boolean enablePreviewer;	//Will the previewer be activated? This depends if preview file creation was a success.	
    private WebView webView;		//The javafx control for the web preview component.
    private boolean globalConfiguration;
    //----------------------------------------------
    //----------------------------------------------
    //FXML Methods
    @FXML
    private void initialize()
    {
    	//Initialization
    	ol = FXCollections.observableArrayList();
    	updateAvailableStyles();	//Updating the styles that the user can choose from.
		cb_style_picker.setItems(ol);
		selectedStyleCSS = "";
		selectedStyleName = "";
		enablePreviewer = false;	//The previewer starts disabled and is enabled after a successful load.
		webView = new WebView();
		globalConfiguration = false;
		p_display.getChildren().addAll(webView);	//Initializing the web view for the previewer.
		//Customizing the ChoiceBox -------------> Java lambda.
		cb_style_picker.getSelectionModel()
	    .selectedItemProperty()
	    .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> setSelectedStyle(newValue));
		//Setting the default style as a workaround to new window not reloading resources.
		setSelectedStyle("Default");
    }
    @FXML
    void onStyleAccept(ActionEvent event) {
    	/*
    	 * This method gives gives the FormCreatorLogic the style
    	 * to change either the global settings or a specific element
    	 * Targeted by the cursor's style.
    	 */
    	System.out.println("Style Accept");
    	if(globalConfiguration)
    	{
    		//This is a global change
    		FormCreatorLogic.setGlobalStyling(FormCreatorLogic.getStyleByName(selectedStyleName));
    	} else
    	{
    		//This is only a local change
    		FormCreatorLogic.setElementStyle(FormCreatorLogic.getStyleByName(selectedStyleName));
    	}
    	//Closing on a controlled way.
    	Stage stage = (Stage)b_style_cancel.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void onStyleCancel(ActionEvent event) {
    	System.out.println("Style Cancel");
    	Stage stage = (Stage)b_style_cancel.getScene().getWindow();
    	stage.close();
    }
    //----------------------------------------------
    //----------------------------------------------
    //Methods
    public void setGlobalConfiguration(boolean selection)
    {
    	/*
    	 * This method is used to set weather this alters a local
    	 * element or the entire form.
    	 */
    	globalConfiguration = selection;
    }
    private void updateAvailableStyles()
    {
    	/*
    	 * This method populates the Choicebox with
    	 * whatever styles are loaded.
    	 */
    	//If none, then this is an empty list but initiated.
    	stylenames = FormCreatorLogic.getStylesNames();	//This are all the names from the styles.
    	//Populating the choicebox
    	ol.addAll(stylenames.toArray(new String[0]));	//Populating in one fell swoop.
    }
    private void setSelectedStyle(String styleName)
    {
    	/*
    	 * This method gets the name of the selected style and attempts
    	 * to create the CSS code required for the preview. If it cannot, 
    	 * it just disables the previewer.
    	 */
    	selectedStyleName = styleName;	//Saving the name of the current selection.
    	StyleSymbol style = FormCreatorLogic.getStyleByName(styleName);	//Getting the style symbol for the current style.
    	StringBuilder cssBuilder = new StringBuilder();
    	//Nullity guard
    	if(style != null)
    	{
    		boolean allCodeRetrieved = true;
    		//Then we can proceed. Generating the CSS code.
    		String headerCode = style.getElementCode("Header");
    		String labelCode = style.getElementCode("Label");
    		String marginCode = style.getElementCode("Margin");
    		String submitCode = style.getElementCode("Submit");
    		String openQuestionCode = style.getElementCode("OpenQuestion");
    		String tripleBoxTextCode = style.getElementCode("TripleBoxText");
    		String generalCode = style.getElementCode("General");
    		//Checking that all code exists and is well. If so then we add it to
    		//the css builder.
    		if(headerCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		if(labelCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		if(marginCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		if(submitCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		if(openQuestionCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		if(tripleBoxTextCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		if(generalCode.length() == 0)
    		{
    			//Then the current element type has no CSS code.
    			allCodeRetrieved = false;
    		}
    		//If every element was found then we can safely create our temporal
    		//css file to style the preview.
    		if(allCodeRetrieved)
    		{
    			//Then we create the CSS code for the preview.
    			cssBuilder.append(headerCode);
    			cssBuilder.append(labelCode);
    			cssBuilder.append(marginCode);
    			cssBuilder.append(submitCode);
    			cssBuilder.append(openQuestionCode);
    			cssBuilder.append(tripleBoxTextCode);
    			cssBuilder.append(generalCode);
    			selectedStyleCSS = cssBuilder.toString();	//Compiling the selected style css code for the preview.
    			enablePreviewer = true;
    			//DEBUG
    			System.out.println("Full style loaded. Previewer enabled.");
    		} else
    		{
    			//Else we will just disable the previewer as this style is incomplete.
    			enablePreviewer = false;
    		}
    	} else
    	{
    		//Else we have an error.
    		//PROGRAMMER ERROR, a style name has no corresponding style.
    	}
    	if(enablePreviewer)
    	{
    		activatePreviewer(selectedStyleCSS);
    	}
    }
    private void activatePreviewer(String finishedCSSCode)
    {
    	/*
    	 * This method fires up the previewer.
    	 * 
    	 * It creates form symbols for the preview and gives them the
    	 * selected style.
    	 * 
    	 * Once that is done, it saves a temporary CSS file and HTML file for
    	 * previe.
    	 * 
    	 * Then it uses the JavaFX HTML viewer to show the created preview.
    	 */
    	//Creating the form symbols for the preview:
    	FormProcessor formProcessor = new FormProcessor();	//The form processor for the creation of the preview.
    	FormSymbol prHeader = formProcessor.createFormElement("Header");
    	prHeader.setStyle(selectedStyleName);
    	FormSymbol prLargeMargin = formProcessor.createFormElement("LargeMargin");
    	prLargeMargin.setStyle(selectedStyleName);
    	FormSymbol prOneLinerQuestion = formProcessor.createFormElement("OneLinerQuestion");
    	prOneLinerQuestion.setStyle(selectedStyleName);
    	FormSymbol prTwoLinerQuestion = formProcessor.createFormElement("TwoLinerQuestion");
    	prTwoLinerQuestion.setStyle(selectedStyleName);
    	FormSymbol prThreeLinerQuestion = formProcessor.createFormElement("ThreeLinerQuestion");
    	prThreeLinerQuestion.setStyle(selectedStyleName);
    	FormSymbol prSmallMargin = formProcessor.createFormElement("SmallMargin");
    	prSmallMargin.setStyle(selectedStyleName);
    	FormSymbol prTripleBoxText = formProcessor.createFormElement("TripleBoxText");
    	prTripleBoxText.setStyle(selectedStyleName);
    	FormSymbol prLabel = formProcessor.createFormElement("Label");
    	prLabel.setStyle(selectedStyleName);
    	FormSymbol prSubmit = formProcessor.createFormElement("Submit");
    	prSubmit.setStyle(selectedStyleName);
    	//Preparing the general document structures
    	String openerCode = "<!DOCTYPE html>\r\n" + 
    			"	<!-- Como esto va indentado en una pagina ya existente la head no es importante -->\r\n" + 
    			"	<head>\r\n" + 
    			"		<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n" + 
    			"		<title>App de Reservaciones y Formulas</title>\r\n" + 
    			"		<link rel=\"stylesheet\" href=\"sample.css\" type=\"text/css\" />\r\n" + 
    			"	</head>\r\n" + 
    			"	<body>\r\n" + 
    			"					<!-- Este div es el contenedor de la app, si se le mueve se mueve toda la app -->\r\n" + 
    			"			<div id=\"content\">\r\n" + 
    			"							<form>";
    	String closerCode = "							</form>\r\n" + 
    			"			</div>\r\n" + 
    			"	</body>\r\n" + 
    			"</html>\r\n" + 
    			"";
    	//Compiling full HTML Code string
    	StringBuilder htmlBuilder = new StringBuilder();
    	htmlBuilder.append(openerCode);
    	htmlBuilder.append(prHeader.outputHTMLCode());
    	htmlBuilder.append(prLargeMargin.outputHTMLCode());
    	htmlBuilder.append(prOneLinerQuestion.outputHTMLCode());
    	htmlBuilder.append(prTwoLinerQuestion.outputHTMLCode());
    	htmlBuilder.append(prThreeLinerQuestion.outputHTMLCode());
    	htmlBuilder.append(prSmallMargin.outputHTMLCode());
    	htmlBuilder.append(prTripleBoxText.outputHTMLCode());
    	htmlBuilder.append(prLabel.outputHTMLCode());
    	htmlBuilder.append(prSubmit.outputHTMLCode());
    	htmlBuilder.append(closerCode);
    	String finishedHTMLCode = htmlBuilder.toString();
    	System.out.println(finishedHTMLCode);	//DEBUG
    	//Creating the HTML file
    	boolean proceed = createHTMLFile(finishedHTMLCode);
    	if(proceed)
    		proceed = createCSSFile(finishedCSSCode);
    	if(proceed)
    	{
    		//Then the two above file creations were a success.
    		//So new the previewer is activated.
    		WebEngine webEngine = webView.getEngine();	//The web engine that will be used to load our resource.
    		String localURL = FormOutputManager.getResourceURL("demo.html");
    		//webEngine.loadContent("File:"+localURL);
    		webEngine.load("File:" + localURL);	//Getting the file to render
    		webEngine.reload();	//Forcing reload.
    	}
    }
    private boolean createHTMLFile(String htmlCode)
    {
    	/*
    	 * This method uses the formatter and a buffered writer to create
    	 * the preview HTML file. The boolean signals if the entire process
    	 * was a success. <Cheap anonymous error management>.
    	 */
    	boolean methodSuccess = false;	//Nothing in done yet, so it starts as false.
    	//Creating the HTML File
    	methodSuccess = FormOutputManager.writeFile("demo.html", htmlCode);
    	//Returning success flag.
    	return methodSuccess;
    }
    private boolean createCSSFile(String cssCode)
    {
    	/*
    	 * This creates the CSS file on disk.
    	 */
    	boolean methodSuccess = false;
    	//Creating the CSS code
    	methodSuccess = FormOutputManager.writeFile("sample.css", cssCode);
    	return methodSuccess;
    }
    //----------------------------------------------
    //----------------------------------------------
}
