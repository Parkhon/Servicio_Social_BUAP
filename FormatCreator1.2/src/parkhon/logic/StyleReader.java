package parkhon.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import parkhon.ErrorNotifier;

public class StyleReader 
{
	/*
	 * This class is responsible from finding all the CSS files
	 * in a given address and converting them to usable internal system
	 * symbols.
	 * 
	 * The likelihood that a CSS style sheet will not have the correct
	 * Format Creator metadata is high. Thus a high degree of resilience
	 * is expected from this component.
	 */
	//Attributes
	private ArrayList<File> rawFiles;	//This list contains all of the unprocessed CSS files from the OS.
	private ArrayList<StyleSymbol> styleSymbols;	//This list contains all the processed internal style symbols.
	private StyleSymbol defaultStyle;
	private boolean scanSuccessPartial;	//This boolean indicates if the file scanning was a success.
	private boolean scanFailurePartial;
	private boolean symbolizationSuccessPartial;	//This boolean indicates if all the raw CSS files were symbolized correctly.
	private boolean symbolizationFailurePartial;
	private StringBuilder errors;
	private String currentCssCode;
	//---------------------------------------------
	//---------------------------------------------
	//Constructor
	public StyleReader()
	{
		//Default values.
		currentCssCode = "";
		scanSuccessPartial = false;
		symbolizationSuccessPartial = false;
		scanFailurePartial = false;
		symbolizationFailurePartial = false;
		errors = new StringBuilder();
		//Initialization of lists.
		rawFiles = new ArrayList<File>();
		styleSymbols = new ArrayList<StyleSymbol>();
	}
	//---------------------------------------------
	//---------------------------------------------
	//Methods
	
	public void readStyleFiles(String url)
	{
		/*
		 * This method communicates with the OS and extracts the
		 * necessary files into a file array. If it is successful
		 * then the scanSuccess flag is toggled to true.
		 */
		
		//UNUSED. MERGED WITH SCAN STYLES
	}
	public void chooseSymbol(String name, ArrayList<String> elements)
	{
		/*
		 * This method extracts all the relevant CSS code from the
		 * internal symbols and compiles a currentCssCode string
		 * with the code required for the current configuration.
		 */
	}
	public StyleSymbol getDefaultStyle()
	{
		//Giving the default style.
		return defaultStyle;
	}
	public boolean checkForDefaultStyle()
	{
		/*
		 * This method looks for the default style in all the processed symbols.
		 * If it is found, then the program will have a fallback for stylyng and new element.
		 * 
		 * All the necessary constructs will be tested to ensure that there is a possibility to
		 * use them all.
		 */
		boolean elementsLoaded = false;	//If any element failed to load then this should be false.
		//Searching for the default style.
		boolean defaultStyleFound = false;
		int i = 0;
		while(i < styleSymbols.size() && !defaultStyleFound)	//Iterating through the symbol CSS files and so long as the default has not been encountered.
		{
			//Evaluating to see if the style is the default.
			StyleSymbol current = styleSymbols.get(i);
			if(current.getStyleName().equals("Default"))
			{
				defaultStyle = current;	//Setting the default style, ready for usage.
				defaultStyleFound = true;	//Priming to exit the loop, as this style should be unique.
				elementsLoaded = true;	//Until one is not found, then we assume there was success.
				//This is the default style CSS.
				//Now looking for all elements we need to create a form.
				if(current.getElementCode("Header") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
				if(current.getElementCode("Label") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
				if(current.getElementCode("Margin") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
				if(current.getElementCode("Submit") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
				if(current.getElementCode("OpenQuestion") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
				if(current.getElementCode("TripleBoxText") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
				if(current.getElementCode("General") == null)
				{
					//Then the required code was found for the header
					defaultStyleFound = false;
				}
			}
			i++;	//Counter tick.
		}
		//Method output.
		return elementsLoaded;
	}
	public ArrayList<StyleSymbol> scanStyles()
	{
		/*
		 * Internal process for scanning the hard drive through the 
		 * OS. Looking for the raw CSS stylesheets.
		 */
		//File stylesDirectory = new File("../../../res/styles/");
		File stylesDirectory = new File("res/styles/");	//Opening the directory where the styles are contained.
		String[] fileNames = stylesDirectory.list(); //This gets all the files in the directory.
		if(fileNames.length <= 0)
		{
			//User error Handling:
			/*
			 * The styles directory is empty, thus no styles can be loaded.
			 */
			String error = "There are no CSS styles in " + stylesDirectory.getAbsolutePath() + " were the"
					+ " previous ones provided with the software deleted or modified? If so, copy them back."
					+ System.lineSeparator();
			scanFailurePartial = true;
			errors.append(error);
		}
		for(String target : fileNames)	//Iterating through all the style names.
		{
			System.out.println(target);	//File name DEBUG
			String filepath = stylesDirectory.getAbsolutePath() + "/" + target;	//Creating the current filepath.
			File inputFile = new File(filepath);	//Creating a file with the URL path loaded.
			if(inputFile.exists())
			{
				//Then the file exists and was loaded.
				rawFiles.add(inputFile);	//It is added to the programs logic.
				scanSuccessPartial = true;
				System.out.println("A file was successfully loaded!");
			}
			else
			{
				//The file does not exist, thus it is dumped and an error message built.
				scanFailurePartial = true;
				String errorMessage = "A style in the res/styles folder failed to load:" + filepath + System.lineSeparator();
				errors.append(errorMessage);
			}
		}
		//Symbolizing
		symbolizeStyles();
		//Error handling
		disposeProcess();
		return styleSymbols;
	}
	private void symbolizeStyles()
	{
		/*
		 * Internal process that creates the necessary symbols for
		 * an identified raw file. If it fails to validate, a false
		 * should be thrown and the process disposal method delegated
		 * with the cleanup.
		 */
		if(scanSuccessPartial)	//If the previous step was a success, then we can proceed.
		{
			for(File file : rawFiles)
			{
				//The finished product
				StyleSymbol symbol = new StyleSymbol();
				//Data and metadata the method is looking for in this file
				boolean styleNameAcquired = false; //Signals if the style name has already been read.
				String styleName = "";
				boolean elementTypeAcquired = false;	//Iterative, to seek a single partial successful read.
				boolean cssCodeAcquired = false;		//Iterative, to seek a single partial successful read.
				boolean elementAcquired = false;	//If so, then at least partial success was met.
				String elementType = "";
				String cssCode = "";
				//----------------
				boolean fileProceed = true;
				//For each file that was previously stored.
				//The search for metadata and the subsequent code commences.
				BufferedReader reader = null; //Preparing the buffered reader.
				try {
					reader = new BufferedReader(new FileReader(file));
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					fileProceed = false; //This file should no longer be worked upon but dumped instead.
					//Signaling there was a problem in the opening of the buffered reader
					//with the file.
					symbolizationFailurePartial = true;	//There was a problem.
					String errorMessage = "The Java BufferedReader failed to open " + file.getAbsolutePath()
					+ System.lineSeparator();
					errors.append(errorMessage);	//Appending error message.
					//The disposeProcess method will handle alerts of errors and the subsequent
					//internal object state cleanup.
				}
				if(fileProceed)
				{
					//The file exists, now proceeding to read it.
					String rawLine = "";	//Preparing the string that will read the CSS file line by line.
					long lineCount = 0;
					StringBuilder dataBuilder = new StringBuilder();	//To build the finished data.
					try {
						while((rawLine = reader.readLine()) != null)
						{
							lineCount++;	//Error recovery information.
							//Reading line by line
							if(!styleNameAcquired)
							{
								int metaIndex = rawLine.indexOf("$FormatEditorStyle");	//Seeking name tag
								if(metaIndex != -1)
								{
									//Then the line was found.
									int relevanceStart = metaIndex + 19;	//Where the metadata starts
									int relevanceEnd = rawLine.indexOf("$End");  //Where the metadata ends
									if(relevanceEnd == -1 || relevanceEnd == relevanceStart)	//Error check
									{
										//FATAL ERROR
										//An error was found, the metadata has been wrongly modified
										symbolizationFailurePartial = true;		//A fatal error was encountered.
										symbolizationSuccessPartial = false;	//Cannot save style without a name.
										String error = "The CSS style file " + file.getAbsolutePath() + " has"
												+ " incorrectly modified metadata tags. Replace with a previous"
												+ " version with correct metadata tags or repair the tags manually."
												+ System.lineSeparator();
										errors.append(error);
										disposeProcess();	//Invoking error control.
										return;	//Fatal error in the style, cannot proceed.
									}
									styleNameAcquired = true;	//We no longer need to search for the style name.
									rawLine = rawLine.substring(relevanceStart, relevanceEnd );	//This line contains metadata only.
									//rawLine now only contains metadata
									String[] metadata = rawLine.split("\\$");
									//metadata[0] now contains the name of the style.
									styleName = metadata[0];	//With this, the name of the style has been recorded.
									symbol.setStyleName(styleName); //Updating the symbol with its name.
									continue;	//If this line contains the style name it will not contain anything else.
								}
							}
							//Searching for the metadata that points to CSS code:
							if(!elementTypeAcquired)
							{
								int metaIndex =  rawLine.indexOf("$FormatEditorMetadata"); //Seeking the tag
								if(metaIndex != -1 )
								{
									//Then the line was found.
									//Preparing CSS code for storage.
									String metaLine = rawLine + System.lineSeparator();
									dataBuilder.append(metaLine);	//The metadata header itself.
									//Extracting metadata
									int relevanceStart = metaIndex + 22;
									int relevanceEnd = rawLine.indexOf("$End");
									if(relevanceEnd == -1 || relevanceEnd == relevanceStart)	//Error check
									{
										//RECOVERABLE ERROR
										//An error was found, the metadata has been wrongly modified
										symbolizationFailurePartial = true;
										String error = "The CSS style file " + file.getAbsolutePath() + " has"
												+ " incorrectly modified metadata tags. Replace with a previous"
												+ " version with correct metadata tags or repair the tags manually."
												+ System.lineSeparator();
										errors.append(error);
										continue;	//This element is broken jumping to the next one.
									}
									rawLine = rawLine.substring(relevanceStart, relevanceEnd);	//Triming the original line
									String[] metadata = rawLine.split("\\$");	//Getting the metadata fields.
									//metadata position 0 now has the type of the construct.
									elementType = metadata [0];	//The information we were looking for.
									elementTypeAcquired = true;	//It was successfully found.
									continue;	//This line was successfully parsed.
								}
							}
							else
							{
								//Else we already have the metadata and must scan the CSS code.
								//Looking for an end tag to stop reading.
								int endIndex = rawLine.indexOf("$FormatEditorEndTag");
								//Error handling
								int startIndexError = rawLine.indexOf("$FormatEditorMetadata");
								if (startIndexError != -1)
								{
									//RECOVERABLE ERROR
									//A start tag was found when none should have.
									//A FormatEditorEndTag could not be read as it was not there
									//or broken.
									//Resetting the element capture data to prevent inconsistency
									elementTypeAcquired = false;
									cssCodeAcquired = false;
									dataBuilder = new StringBuilder();	//To start adding completely new Css code.
									symbolizationFailurePartial = true;
									String error = "The CSS style file " + file.getAbsolutePath() + " has"
											+ " incorrectly modified metadata tags. Replace with a previous"
											+ " version with correct metadata tags or repair the tags manually."
											+ System.lineSeparator();
									errors.append(error);
									//It still skips a perfectly useable css element. But to fix it,
									//the code of finding elements would have to be copied here,
									//and that would be unacceptably sloppy.
									continue;	//This element is broken jumping to the next one.
								}
								//---------------
								if(endIndex != -1)
								{
									//Then the End Tag was found
									dataBuilder.append(rawLine + System.lineSeparator());	//Appending end tag to code
									cssCodeAcquired = true;	//And thus all the CSS code was successfully captured.
								}
								else
								{
									//Else it is just regular CSS code, as it is not an End Tag.
									dataBuilder.append(rawLine + System.lineSeparator());	//The line is just appended.
									//The CSS code scan is not complete yet.
								}
							}
							if(cssCodeAcquired)
							{
								//The CSS code was completely scanned. To do that the header
								//must also have been scanned, which means all that is needed
								//is now aquired for this element.
								cssCode = dataBuilder.toString();
								symbol.addElement(elementType, cssCode); //Successfully scanned the CSS element.
								//Resetting the element capture data to prevent inconsistency
								elementTypeAcquired = false;
								cssCodeAcquired = false;
								dataBuilder = new StringBuilder();	//To start adding completely new Css code.
								//Pointer precaution, might be unnecessary
								cssCode = new String("");
								elementType = new String("");
								//-----------
								//And so we know that at least partial success was accomplished.
								elementAcquired = true;
								//And now we continue to loop through this process.
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						//Failure to read a line from the buffered reader.
						symbolizationFailurePartial = true;	//Signaling error
						String errorMessage = "The Java BufferedReader could not read line " + lineCount +
								" in " + file.getAbsolutePath();
						errors.append(errorMessage);	//Appending the error message for later recovery.
						//Resetting the element capture data to prevent inconsistency
						elementTypeAcquired = false;
						cssCodeAcquired = false;
						dataBuilder = new StringBuilder();	//To start adding completely new Css code.
					}
					//Continuing out of the try catch block
					//Preparing the internal symbol
					if(elementAcquired && styleNameAcquired)	//If at least an element was successfully acquired. And a name was registered.
					{
						//Preparing more symbol data
						symbol.setStylePath(file.getAbsolutePath());
						//
						styleSymbols.add(symbol);	//And thus a Css style was fully read and symbolized.
						//Then at least partial success was had.
						symbolizationSuccessPartial = true;
					}
				}
				//Error Control
				if(!styleNameAcquired)
				{
					//The entire CSS file was read and no stylename was found. Thus
					//nothing could be saved.
					symbolizationFailurePartial = true;
					String errorMessage = "The CSS document " + file.getAbsolutePath() + " was completely"
							+ " read, but no style name was found. Therefore it could not be loaded. "
							+ "Please add a title to the style on by writing //* $FormatEditorStyle$<title>$End *//"
							+ " on the CSS file." + System.lineSeparator();
					errors.append(errorMessage);
				}
				if(!elementAcquired)
				{
					//The entire CSS file was read and not a single element was successfully read. Thus
					//nothing could be saved.
					symbolizationFailurePartial = true;
					String errorMessage = "The CSS document " + file.getAbsolutePath() + " was completely read"
							+ " but not a single element could be loaded. This means that the metadata that allows"
							+ " for the Header, Label, OpenQuestion, etc. to be loaded (start and finish) is wrongfully"
							+ " modified. Retry with a previous unaffected version of the form or repair the metadata manually."
							+ System.lineSeparator();
					errors.append(errorMessage);
				}
				try {
					reader.close();	//Closing the reader.
				} catch (IOException e) {
					//The files were probably removed during runtime. Nothing we can do to help here.
					//But this exceptional and intentional bug does not impact program performance either.
					e.printStackTrace();
				}
			}
		}
		else
		{
			//Else symbolizeStyles was called with an inconsistent object state.
			symbolizationFailurePartial = true; //There was a problem.
			String errorMessage = "The program failed to process the styles, as none had been loaded.";
		}
		//Error handling
		disposeProcess();
	}
	private void disposeProcess()
	{
		/*
		 * Error handling method, it cleans up after improper inputs.
		 * It also notifies the user of the suspected reason behind the
		 * error.
		 * 
		 * This method also repairs the internal state of the StyleReader
		 * if necessary. The success classes and the default values can
		 * be used to regenerate the internal object integrity.
		 * 
		 * To see if something did not finish successfully the booleans that
		 * signal success for a part of the process are checked.
		 * 
		 * Error messages may be recovered using the errors StringBuilder for
		 * a more specific perspective.
		 */
		StringBuilder generalError = new StringBuilder();
		if(scanFailurePartial || symbolizationFailurePartial)
		{
			//The file could not be read:
			String error1 = "Errors were encountered during the reading of a style CSS sheet." + System.lineSeparator();
			generalError.append(error1);
			if(symbolizationSuccessPartial)
			{
				String error2 = "The program still managed to recover from the error and load as much of the CSS sheet"
						+ " as was possible." + System.lineSeparator();
				generalError.append(error2);
			}
			else
			{
				String error3 = "Regretably, the program was unable to load the CSS sheet and as a result, and the operation"
						+ " had to be aborted." + System.lineSeparator();
				generalError.append(error3);
			}
			//Outputting all the error messages.
			String error4 = "The following were the error messages received during the program's runtime:"
					+ System.lineSeparator();
			generalError.append(error4);
			generalError.append(errors.toString());
			String errorMessage = generalError.toString();
			ErrorNotifier.pushErrorMessage(errorMessage);
			//DEBUG
			System.out.println(errorMessage);	//DEBUG
			//------------------
		}
		resetErrorState();	//Resetting symbolization
		//Else all is normal.
	}
	private void resetErrorState()
	{
		/*
		 * This object restores the object to its original state. Might not
		 * be necessary at all.
		 */
		//Default values.
		symbolizationFailurePartial = false;
		symbolizationSuccessPartial = false;
		scanFailurePartial = false;
		errors = new StringBuilder();
	}
	//---------------------------------------------
	//---------------------------------------------
}
