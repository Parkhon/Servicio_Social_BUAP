package parkhon.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import parkhon.ElementStyleTuple;
import parkhon.ErrorNotifier;
import parkhon.data_structures.FormOutputManager;

public class FormCreatorLogic {
	//Attributes
	//Position logic. Tracks the element type and its style.
	private static ArrayList<ElementStyleTuple> targets = new ArrayList<ElementStyleTuple>();
	//Form
	private static ArrayList<FormSymbol> form = null;	//This is the form that was loaded by the FormProcessor.
	private static ArrayList <StyleSymbol> styleSheets = null;	//All the successfully loaded stylesheets.
	//Input verification
	private static boolean formsLoaded = false;
	private static boolean stylesLoaded = false;
	/*
	 * The real default name will be Default_Style. If no default style is found, throw an error.
	 * If the program fails to find an element style in the current stylesheet, fallback to the
	 * default one.
	 * 
	 * If the default style does not have an element when fallback is tried, throw an error.
	 * 
	 * This class loads and symbolizes all the styles at the beginning of the program.
	 */
	private  static StyleSymbol defaultStyle = null;	// The default style is placed here. Else the program closes. Loads on program boot.
	//Cursor placement
	private static int cursorIndex = 0;
	//Custom name generation
	private static int autoNameCounter = 0;
	//PHP Code generation
	private static String targetEmail = "example@example.com";
	private static ArrayList<String> phpNames = new ArrayList<>();
	
	//Error Handling:
	
	//--------------------------------------------
	//--------------------------------------------	
	//Methods
	public static boolean readStyles(StyleReader reader)
	{
		/*
		 * This reads all the CSS files from the StyleReader and loads them.
		 */
		boolean methodSuccess = false;
		styleSheets = reader.scanStyles();//Reading and populating all the styles.
		if (styleSheets.size() != 0)
		{
			//Then the CSS sheets were read successfully
			stylesLoaded = true;
		}
		if(stylesLoaded)
		{
			//Then we look for a default style. Without it the program cannot work or create forms.
			boolean defaultStyleLoaded = reader.checkForDefaultStyle();
			if(defaultStyleLoaded)
			{
				defaultStyle = reader.getDefaultStyle();	//The default style was successfully identified.
				methodSuccess = true;	//Method succeeded by completing the last instruction in a linear checked process.
			}
		}
		return methodSuccess;
	}
	public static boolean readForm(FormProcessor reader, File inputFile)
	{
		/*
		 * This loads the form that the form processor prepared.
		 */
		boolean methodSuccess = false;
		form = reader.loadForm(inputFile);
		if(form.size() != 0)
		{
			//Then the form was successfully read.
			formsLoaded = true;
			methodSuccess = true;
		}
		else
		{
			formsLoaded = false;	//Preventing form inconsistencies.
			methodSuccess = false;
		}
		//Creating internal targets
		if(formsLoaded && stylesLoaded)
		{
			//Then we populate our internal model.
			HashMap<String, StyleSymbol> stylesByName = new HashMap<String, StyleSymbol>();	//For registering styles by name.
			for(StyleSymbol ss : styleSheets)
			{
				//Storing the loaded styles in the stylesByName hash.
				stylesByName.put(ss.getStyleName(), ss);
			}
			//Now creating the targets list, which holds the internal logic and positions.
			for(FormSymbol fs : form)
			{
				//Identifying the correct styling if any. Else falling back to the default.
				//The error will be thrown if no default element exists, and the element dumped out.
				StyleSymbol styleUsed = stylesByName.get(fs.getStyle());
				if(styleUsed == null)
				{
					//If it is null then that means that the style was not successfully read or
					//did not exist in the res folder.
					styleUsed = defaultStyle;
					//DEBUG
					System.out.println("It FormCreatorLogic readForm, a style was not found and was set to the default style.");
				}
				//Adding the correct style to the internal logic.
				ElementStyleTuple tuple = new ElementStyleTuple(fs, styleUsed);
				//LEFT HERE :
				//TODO:
				/*
				 * Write rejection and error launch when a tuple
				 * element type does not exist in its style.
				 * 
				 * Previously, we had all elements with unknown styles 
				 * become defaultly styled.
				 */
				targets.add(tuple);	//Now part of the internal logic.
			}
			
		}
		//DEBUG:
		if(formsLoaded && stylesLoaded)
		{
			//Debug CSS code Geration
			System.out.println("Generating CSS Code.");
			System.out.println(generateCSSCode());
		}
		return methodSuccess;
	}
	private static void createNewForm()
	{
		/*
		 * This method creates a new form with an empty state.
		 * It can also be used to flush a previously loaded form.
		 */
		form = new ArrayList<>();
		targets = new ArrayList<>();
		formsLoaded = false;
		cursorIndex = 0;
	}
	public static void setGlobalStyling(StyleSymbol style) {
		//Error Handling
		if(!formsLoaded)
		{
			//Not enough content has been loaded. Either the Form has not been
			//read or the style has not been chosen. Either way, considering
			//the default style, this is a developer bug.
			//Unchecked Error. Developer Error.
			return;
		}
		//Method Proper
		// This method sets to alter global settings. Thus all previous targets
		for(ElementStyleTuple tuple : targets)
		{
			//Changing all the styles:
			tuple.setStyle(style);
		}
		updatePreviewer(true);	//Creating the render files again.
	}
	public static void setElementStyle(StyleSymbol style)
	{
		//Error Handling
		if(!formsLoaded)
		{
			//Not enough content has been loaded. Either the Form has not been
			//read or the style has not been chosen. Either way, considering
			//the default style, this is a developer bug.
			//Unchecked Error. Developer Error.
			return;
		}
		if(cursorIndex < 0)
		{
			//Out of bounds control
			cursorIndex = 0;
		}
		if(cursorIndex == targets.size())
		{
			//Out of bounds control
			cursorIndex = targets.size() - 1;
		}
		//Method Proper
		//This method sets the style for a single element.
		targets.get(cursorIndex).setStyle(style);
		updatePreviewer(true);	//Creating the render files again.
	}
	public static void addNewElement(FormSymbol element)
	{
		/*
		 * This method creates a new element and adds it into the position logic.
		 * The new element will inherit the style of the element above it. Else
		 * it will have the default style (Which is the fist style to be read).
		 */
		if (cursorIndex < 0)	//InputGuard
		{
			//Create the new element at the very beginning of the form.
			ElementStyleTuple tuple = new ElementStyleTuple(element, defaultStyle);	//New elements are styled default
			targets.add(0, tuple);
		}
		//Since this can start a new format, targetLoaded should be set to true on success.
		else if(cursorIndex == 0)
		{
			//This means there may or may not be elements above.
			//Then the new element falls back to the default style if no others are present above.
			//Precautionary if, might not be necessary
			if(targets.size() == 0)
			{
				//Then this is the first element to be added in an empty form.
				ElementStyleTuple tuple = new ElementStyleTuple(element, defaultStyle);	//New elements are styled default
				targets.add(tuple);
			}
			else
			{
				//This is not the first element in this form
				StyleSymbol stylingAbove = targets.get(cursorIndex).getStyle();	//Gets the style of the tuple above.
				ElementStyleTuple tuple = new ElementStyleTuple(element, stylingAbove);	//Creates the new tuple.
				targets.add(cursorIndex + 1, tuple);	//Add the new tuple bellow the specified position.
			}
		}
		else if(cursorIndex == targets.size())
		{
			//ERROR. UNREPORTED.
		}
		else
		{
			//Case for normal insertion.
			//Then there is already a tuple above. Its style will be utilized.
			StyleSymbol stylingAbove = targets.get(cursorIndex).getStyle();	//Gets the style of the tuple above.
			ElementStyleTuple tuple = new ElementStyleTuple(element, stylingAbove);	//Creates the new tuple.
			targets.add(cursorIndex + 1, tuple);	//Add the new tuple at the specified position.
		}
		formsLoaded = true;
	}
	public static void deleteElement()
	{
		/*
		 * This method deletes the element in the position specified.
		 */
		//Error handling
		if(targets.size() == 0)
		{
			//There is nothing to delete.
			//USER ERROR
			String error = "The form is empty, so there is nothing to delete." + System.lineSeparator();
			ErrorNotifier.pushErrorMessage(error);
			return;	//-------------------------------ERROR EXIT!
		}
		if(cursorIndex < 0)	//Input Guard
		{
			//The position before the first element is
			//valid and creates elements at index 0.
			cursorIndex = 0;
		}
		if (cursorIndex >= targets.size())
		{
			//The position is greater than the available places where it would make sense.
			//Unreported Error. Developer Error.
			cursorIndex = targets.size() - 1;
		}
		//Method proper
		targets.remove(cursorIndex);	//Removing the tuple.
	}
	private static String generateCSSCode()
	{
		/*
		 * This method generates the CSS styling code, given the internal state of this class.
		 * 
		 * TODO:
		 * Lacks the general code.
		 * The general style name should already be present at the top when this method is triggered.
		 * 
		 * THE IMPORTANCE OF UML!
		 */
		// This helps identify which elements have which styles, in order to create the final CSS efficiently.
		HashMap<String, String> pairsFound = new HashMap<String, String>();	//Identifies if a pair had been found before.
		ArrayList<ElementStyleTuple> pairsRegistered = new ArrayList<ElementStyleTuple>();	//Adds unidentified new pairs for later CSS generation.
		//Create String hash for a pair of element names and of style names.
		for(ElementStyleTuple tuple : targets)
		{
			//Generating the hash key for a pair of element-tuple. Defined by the type and style name.
			String keyHash = tuple.getElement().getStylingType() + tuple.getStyle().getStyleName();
			if( pairsFound.get(keyHash) == null )
			{
				//Then we have a newly occurring element type with style name combination.
				pairsFound.put(keyHash, "placeholder");	//Since we found it before, it is now added to avoid duplication.
				pairsRegistered.add(tuple);		//This tuple contains an element type and style name we must add.
			}
		}
		//LEFT HERE
		//Generate CSS code from pairsFound.
		StringBuilder completeCSSCode = new StringBuilder();
		for(ElementStyleTuple tuple : pairsRegistered)
		{
			//Per every unique tuple found
			String elementType = tuple.getElement().getStylingType();	//Extracting the type metadata from the element.
			String cssCode = tuple.getStyle().getElementCode(elementType);	//Getting the CSS code of the given style for that element type.
			completeCSSCode.append(cssCode + System.lineSeparator());
		}
		//Adding the general CSS code from the default style:
		String generalCode = defaultStyle.getElementCode("General");
		completeCSSCode.append(generalCode);
		//returning the finished code.
		return completeCSSCode.toString();	//Returning the complete CSS code for these elements.
	}
	private static String generateHTMLCode(boolean previewVersion)
	{
		/*
		 * This method generates the HTML form code, given the internal state of the class.
		 */
		//Prepare to create the HTML code
		phpNames = new ArrayList<>();	//Preparing to create new PHP fields.
		//Creating the necessary builder
		StringBuilder htmlBuilder = new StringBuilder();
		autoNameCounter = 0;	//This auto creates the name attribute for open questions.
		//Prepare the wrapper code.
		String wrapperCodeOpener = "<!DOCTYPE html>\r\n" + 
				"	<!-- Como esto va indentado en una pagina ya existente la head no es importante -->\r\n" + 
				"	<head>\r\n" + 
				"		<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\r\n" + 
				"		<title>App de Reservaciones y Formulas</title>\r\n" + 
				"		<link rel=\"stylesheet\" href=\"" + (previewVersion ? "preview.css" : "form.css")
				+ "\" type=\"text/css\" />\r\n" + 
				"	</head>\r\n" + 
				"	<body>\r\n" + 
				"					<!-- Este div es el contenedor de la app, si se le mueve se mueve toda la app -->\r\n" + 
				"			<div id=\"content\">\r\n" + 
				"							<form action=\"\" method=\"post\">" + System.lineSeparator();
		String wrapperCodeCloser = System.lineSeparator() +
				"							</form>\r\n" + 
				"			</div>\r\n" + 
				"	</body>\r\n" + 
				"</html>\r\n";
		//Adding the opener code
		htmlBuilder.append(wrapperCodeOpener);
		//Iterate through all the html constructs and get their code:
		int i = 0;	//Index for tracking where to place the cursor
		boolean primeCursorCloser = false;	//Boolean for adding the closer code where required.
		FormProcessor formProcessor = new FormProcessor();	//FormProcessor object to get the cursor code from.
		if(cursorIndex == -1 && previewVersion)	//Cursor if
		{
			//Then this form is empty, so we just draw the cursor.
			htmlBuilder.append(formProcessor.getOpenerCursorCode());
			htmlBuilder.append(formProcessor.getCloserCursorCode());
		}
		for(ElementStyleTuple tuple: targets)
		{
			FormSymbol formElement = tuple.getElement();
			if(i == cursorIndex && previewVersion)	//Cursor if
			{
				//Then this is the index where the cursor must be drawn.
				htmlBuilder.append(formProcessor.getOpenerCursorCode());
				primeCursorCloser = true;	//Preparing to add the closer code surrounding this element
			}
			//For each, get their new HTML Code.
			String elementCode = formElement.outputHTMLCode();
			//Checking for form name:
			if(formElement.getStylingType().equals("OpenQuestion"))
			{
				//Then this must have a name attribute:
				//Adding the custom name to the snippet of code.
				elementCode = autoGenerateName(elementCode);
			}
			//Append the code
			htmlBuilder.append(elementCode);
			if(primeCursorCloser && previewVersion)	//Cursor if
			{
				//Then the closer code must be added.
				htmlBuilder.append(formProcessor.getCloserCursorCode());	//Adding the cursor code
				primeCursorCloser = false;	//Priming to not add the cursor closer again in this render.
			}
			i++;	//Counter increaser.
		}
		//Adding the closer code.
		htmlBuilder.append(wrapperCodeCloser);
		//Preparing to deliver the final code.
		String outputCode = (previewVersion) ? htmlBuilder.toString() : generatePHPCode() + htmlBuilder.toString() ;
		return outputCode;
	}
	private static String generatePHPCode()
	{
		//StringBuilder
		StringBuilder phpBuilder = new StringBuilder();
		//The wrapper PHP Code
		String headerOpenerCode = "<?php\r\n" + 
				"if(isset($_POST['submit'])){\r\n" + 
				"    $targetEmail = \"";
		String headerCloserCode = "\"; //The email to deliver to\r\n" + 
				"    //Form variable capture";
		//This is the finished header code for the PHP Mailer
		String headerCode = headerOpenerCode + targetEmail + headerCloserCode + System.lineSeparator();
		phpBuilder.append(headerCode);
		//Creating variable capture logic
		for(String name : phpNames)
		{
			//Creating the php variable capture code for all names
			String varCapCode = "    $var_" + name + " = $_POST['" + name + "'];" + System.lineSeparator();
			phpBuilder.append(varCapCode);
		}
		//Appending into the message variable
		String messageAppendOpenerCode = "    //Message Appending\r\n" + 
				"    $message = ";
		String messageAppendCloserCode = ";" + System.lineSeparator();
		phpBuilder.append(messageAppendOpenerCode);
		int i = 0;
		for(String name : phpNames)
		{
			//Creating the appended message
			String appendedText = "$var_" + name + " . \" \" ";
			if(i != 0)
			{
				//Then another appended text goes before, so the append character must be added
				phpBuilder.append(". ");
			}
			phpBuilder.append(appendedText);	//Appending the new text
			i++;
		}
		phpBuilder.append(messageAppendCloserCode);	//Adding the last semicolon at the end of the message variable.
		//Creating the mailing code
		String mailCode = "    mail($targetEmail,'Form Processed',$message);" + System.lineSeparator();
		//Adding the PHP closing code
		String phpCloserCode = "    }\r\n" + 
				"?>" + System.lineSeparator() + System.lineSeparator();
		phpBuilder.append(mailCode);
		phpBuilder.append(phpCloserCode);
		//Returning the output
		return phpBuilder.toString();
	}
	private static void uploadForm(ArrayList<FormSymbol> loadedForm)	//UNUSED FLAGGED TO DELETE
	{
		/*
		 * Formats a loaded form into the internal targets model.
		 * 
		 * When the stylesheets are loaded and processed, this method should be invoked.
		 */
		form = loadedForm;
	}
	public static ArrayList<String> getStylesNames()
	{
		/*
		 * Compile the names of all the loaded styles and deliver them.
		 * This will be used by the Style Chooser to populate its
		 * choice box.
		 */
		//If styleSheets is empty send the empty list only.
		ArrayList<String> stylesNames = new ArrayList<>();	//Prepare the container for the styles names.
		for(StyleSymbol symbol : styleSheets)
		{
			String name = symbol.getStyleName();
			stylesNames.add(name);
		}
		return stylesNames;
	}
	
	public static StyleSymbol getStyleByName(String styleName)
	{
		/*
		 * This method delivers a requested style symbol by name.
		 * It will be used by the style chooser to generate its preview.
		 */
		StyleSymbol symbol = null;
		for (StyleSymbol inSymbol : styleSheets)
		{
			if(inSymbol.getStyleName().equals(styleName))
			{
				//Then this is the correct style we are looking for.
				symbol = inSymbol;
			}
		}
		return symbol;
	}
	public static boolean updatePreviewer(boolean renderCursor)
	{
		/*
		 * This method creates the files the previewer needs and
		 * returns true if the files were created successfully.
		 */
		boolean methodSuccess = false;
		//Check if all required code can be acquired and store it
		boolean proceed = (targets.size() > 0);
		String formHTMLCode = generateHTMLCode(renderCursor);
		String formCSSCode = generateCSSCode();
		//Create the files for the previewer using the FormOutputManager
		if(proceed) {
			if(!renderCursor)
			{
				//Then this is the final save request
				FormOutputManager.writeFile("form.html", formHTMLCode);
				FormOutputManager.writeFile("form.css", formCSSCode);
			}
			//Then there are enough elements stored to create the visuals.
			boolean success_1 = FormOutputManager.writeFile("preview.html", formHTMLCode);
			boolean success_2 = FormOutputManager.writeFile("preview.css", formCSSCode);
			proceed = (success_1 && success_2);
		}
		if(proceed)
		{
			methodSuccess = true;
		}
		//Return Success report.
		return methodSuccess;
	}
	public static void moveCursorDown()
	{
		/*
		 * This method moves the cursor to a lower position. If invalid,
		 * then just sets it to 0, assuming it is the lowest valid
		 * position.
		 * 
		 * Updating the previewer is controlled by the FormatCreatorController
		 * on button press.
		 */
		if(cursorIndex <= -1)
		{
			cursorIndex = -1;
		}
		else {
			cursorIndex --;
		}
	}
	public static void moveCursorUp()
	{
		/*
		 * This method moves the cursor to a superior position. If invalid,
		 * thin just sets it to the maximum possible position according to 
		 * the size of the loaded elements in the form.
		 * 
		 * Updating the previewer is controlled by the FormatCreatorController
		 * on button press.
		 */
		if(cursorIndex + 1 >= targets.size())
		{
			if(targets.size() == 0)
			{
				cursorIndex = 0;
			}
			else
			{
				cursorIndex = targets.size() - 1;
			}
		}
		else
		{
			cursorIndex ++;
		}
	}
	public static void handleFormCreation()
	{
		/*
		 * This handler creates a new empty form if none exists,
		 * else it does nothing and allows whatever is interacting
		 * with the form to do its work directly.
		 */
		//If in inconsistent state create new form
		if(form == null || targets == null)
		{
			createNewForm();
		} else
		{
			if(targets.size() == 0) {
				createNewForm();
			}
		}
	}
	public static FormSymbol getFormSymbolAtCursor()
	{
		FormSymbol output = null;
		//Checking input
		boolean proceed = (targets != null);
		if(proceed) proceed = (targets.size() > cursorIndex);
		if(proceed) proceed = cursorIndex >= 0;
		//
		if(proceed)
		{
			//Then this operation is safe.
			output = targets.get(cursorIndex).getElement();	//What was requested.
		}
		return output;
	}
	private static String autoGenerateName(String rawCode)
	{
		String output = rawCode;	//Output, copying the entering code for processing.
		//Search for the name attribute
		int lookFrom = 0;
		int nameIndex = output.indexOf("name=\"");
		while(nameIndex >= 0)
		{
			//Getting relevance start and end.
			int relevanceStart = nameIndex + 6;		//The start of the name attribute.
			int relevanceEnd = output.indexOf("\"", relevanceStart);	//The end of the name attribute
			String openerCode = output.substring(0, relevanceStart);	//The first part of the processed code
			String closerCode = output.substring(relevanceEnd);	//The later part of the processed code
			int deletedLength = relevanceEnd - relevanceStart;	//The amount of text that will be ignored.
			//Auto generating the name
			String autoName = "question_" + Integer.toString(autoNameCounter);	//The new name for this HTML input.
			phpNames.add(autoName);	//Registering this name for PHP email code creation.
			autoNameCounter++;	//Increasing the counter for a new name generation later on.
			int processedLengthDifference = autoName.length() - deletedLength;	//The difference of size of the processed string.
			lookFrom = relevanceEnd + processedLengthDifference;	//Where to start looking for the next occurrence.
			output = openerCode + autoName + closerCode;	//Feeding back the processed string for more processing.
			//Searching for another name in the string.
			nameIndex = output.indexOf("name=\"", lookFrom);	//Accounting for the changes that happened in the string.
		}
		//Check if the name attribute even exists
		return output;	//Returning the process.
	}
	public static void generateFormForEmail(String inEmail)
	{
		targetEmail = inEmail;
		updatePreviewer(false);	//Update, this time its not a preview
	}
	//--------------------------------------------
	//--------------------------------------------
}
