package parkhon.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import parkhon.ErrorNotifier;
import parkhon.data_structures.SymbolWord;

public class FormProcessor 
{
	//Attributes
	private File form;	//This is the form, be it proper or not.
	private ArrayList<FormSymbol> formSymbols;//This is the list that contains all the form symbols internally.
	//Error handling
	private StringBuilder errors;
	private boolean loadFormPartialSuccess;
	private boolean loadFormPartialFailure;
	private boolean symbolizeFormPartialSuccess;
	private boolean symbolizeFormPartialFailure;
	//--
	private boolean formElementCreationPartialSuccess;
	private boolean formElementCreationPartialFailure;
	private boolean symbolWordCreationPartialSuccess;
	private boolean symbolWordCreationPartialFailure;
	//Form editing
	//Explanations:
	private ArrayList<String> headerExplanations;
	private ArrayList<String> oneLinerQuestionExplanations;
	private ArrayList<String> twoLinerQuestionExplanations;
	private ArrayList<String> threeLinerQuestionExplanations;
	private ArrayList<String> tripleBoxTextExplanations;
	private ArrayList<String> labelExplanations;	
	//HTML Code
	private ArrayList<String> headerHTMLCode;
	private ArrayList<String> smallMarginHTMLCode;
	private ArrayList<String> largeMarginHTMLCode;
	private ArrayList<String> oneLinerQuestionHTMLCode;
	private ArrayList<String> twoLinerQuestionHTMLCode;
	private ArrayList<String> threeLinerQuestionHTMLCode;
	private ArrayList<String> tripleBoxTextHTMLCode;
	private ArrayList<String> labelHTMLCode;
	private ArrayList<String> submitHTMLCode;
	private ArrayList<String> logoHTMLCode;
	//HTML Cursor Code
	private String openerCursorCode;
	private String closerCursorCode;
	//-----------------------------------
	//-----------------------------------
	//Constructor
 	public FormProcessor()
	{
		formSymbols = new ArrayList<FormSymbol>();
		//Error handling
		errors = new StringBuilder();
		loadFormPartialSuccess = false;
		loadFormPartialFailure = false;
		symbolizeFormPartialSuccess = false;
		symbolizeFormPartialSuccess = false;
		formElementCreationPartialFailure = false;
		formElementCreationPartialSuccess = false;
		symbolWordCreationPartialFailure = false;
		symbolWordCreationPartialSuccess = false;
		//Initializing Explanations:
		headerExplanations = new ArrayList<>(Arrays.asList("Input the header's main text title."));
		oneLinerQuestionExplanations = new ArrayList<>(Arrays.asList("Input the first and only question of this element."));
		twoLinerQuestionExplanations = new ArrayList<>(Arrays.asList("Input the first question of this element.", "Input the second question of this element."));
		threeLinerQuestionExplanations = new ArrayList<>(Arrays.asList("Input the first question of this element.", "Input the second question of this element.", "Input the third question of this element"));
		tripleBoxTextExplanations = new ArrayList<>(Arrays.asList("Input the contents of the first box", "Input the contents of the second box", "Input the contents of the third box"));
		labelExplanations = new ArrayList<>(Arrays.asList("Input the text of the message to display."));
		//Initializing the HTML code for element creation.
		initializeHTMLCodeCreation();
	}
	//-----------------------------------
	//-----------------------------------
	//Methods
 	private void initializeHTMLCodeCreation()
 	{
 		/*
 		 * This method creates the necessary HTML code arrays for the creation of all
 		 * the elements that will be used for the forms.
 		 * 
 		 * The style modification will be done with a string parsing script.
 		 */
 		//Headers
 		String header_1 = "                                <!-- $FormDesignerMeta$Default$Header$End -->\r\n" + 
 				"								<div class=\"buap_form_header styled_Default\">\r\n" + 
 				"									<h2>";
 		String header_2 = "</h2>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		headerHTMLCode = new ArrayList<>(Arrays.asList(header_1, header_2));
 		//Margins
 		String margin_large = "                                <!-- $FormDesignerMeta$Default$LargeMargin$End -->\r\n" + 
 				"								<div class=\"buap_form_large_margin styled_Default\">\r\n" + 
 				"												<p> </p>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		largeMarginHTMLCode = new ArrayList<>(Arrays.asList(margin_large));
 		String margin_small = "                                <!-- $FormDesignerMeta$Default$SmallMargin$End -->\r\n" + 
 				"								<div class=\"buap_form_small_margin styled_Default\">\r\n" + 
 				"												<p> </p>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		smallMarginHTMLCode = new ArrayList<>(Arrays.asList(margin_small));
 		//Open Questions
 		String oneLiner_1 = "                                <!-- $FormDesignerMeta$Default$OneLinerQuestion$End -->\r\n" + 
 				"								<div class=\"buap_open_question buap_one_liner_open_question styled_Default\">\r\n" + 
 				"											<!-- -->\r\n" + 
 				"									<span>\r\n" + 
 				"											<p>";
 		String oneLiner_2 = "</p>\r\n" + 
 				"									</span\r\n" + 
 				"									><span>\r\n" + 
 				"											<input type=\"text\" name=\"\"/>\r\n" + 
 				"									</span>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		oneLinerQuestionHTMLCode = new ArrayList<>(Arrays.asList(oneLiner_1, oneLiner_2));
 		String twoLiner_1 = "                                <!-- $FormDesignerMeta$Default$TwoLinerQuestion$End -->\r\n" + 
 				"								<div class=\"buap_open_question buap_two_liner_open_question styled_Default\">\r\n" + 
 				"												<!-- First question slot -->\r\n" + 
 				"												<span>\r\n" + 
 				"																<p>";
 		String twoLiner_2 = "</p>\r\n" + 
 				"												</span\r\n" + 
 				"												><span class=\"subject_end\">\r\n" + 
 				"																<input type=\"text\" name=\"\"/>\r\n" + 
 				"												</span><!--\r\n" + 
 				"												Second question slot\r\n" + 
 				"												--><span>\r\n" + 
 				"																<p>";
 		String twoLiner_3 = "</p>\r\n" + 
 				"												</span\r\n" + 
 				"												><span class=\"last_element\">\r\n" + 
 				"																<input type=\"text\" name=\"\"/>\r\n" + 
 				"												</span>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		twoLinerQuestionHTMLCode = new ArrayList<>(Arrays.asList(twoLiner_1, twoLiner_2, twoLiner_3));
 		String threeLiner_1 = "                                <!-- $FormDesignerMeta$Default$ThreeLinerQuestion$End -->\r\n" + 
 				"								<div class=\"buap_open_question buap_three_liner_open_question styled_Default\">\r\n" + 
 				"												\r\n" + 
 				"												<!-- First question slot -->\r\n" + 
 				"												<span>\r\n" + 
 				"																<p>";
 		String threeLiner_2 = "</p>\r\n" + 
 				"												</span\r\n" + 
 				"												><span class=\"subject_end\">\r\n" + 
 				"																<input type=\"text\" name=\"\"/>\r\n" + 
 				"												</span><!--\r\n" + 
 				"												Second question slot\r\n" + 
 				"												--><span>\r\n" + 
 				"																<p>";
 		String threeLiner_3 = "</p>\r\n" + 
 				"												</span\r\n" + 
 				"												><span class=\"subject_end\">\r\n" + 
 				"																<input type=\"text\"  name=\"\"/>\r\n" + 
 				"												</span><!-- Third question slot\r\n" + 
 				"												--><span>\r\n" + 
 				"																<p>";
 		String threeLiner_4 = "</p>\r\n" + 
 				"												</span\r\n" + 
 				"												><span class=\"last_element\">\r\n" + 
 				"																<input type=\"text\" name=\"\"/>\r\n" + 
 				"												</span>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		threeLinerQuestionHTMLCode = new ArrayList<>(Arrays.asList(threeLiner_1, threeLiner_2, threeLiner_3, threeLiner_4));
 		//TripleBoxText
 		String tripleBoxText_1 = "                                <!-- $FormDesignerMeta$Default$TripleBoxText$End -->\r\n" + 
 				"								<div class=\"buap_triple_text_box styled_Default\">\r\n" + 
 				"												<span><p>";
 		String tripleBoxText_2 = "</p></span\r\n" + 
 				"												><span><p>";
 		String tripleBoxText_3 = "</p></span\r\n" + 
 				"												><span><p>";
 		String tripleBoxText_4 = "</p></span>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		tripleBoxTextHTMLCode = new ArrayList<>(Arrays.asList(tripleBoxText_1, tripleBoxText_2, tripleBoxText_3, tripleBoxText_4));
 		//Label
 		String label_1 = "                                <!-- $FormDesignerMeta$Default$Label$End -->\r\n" + 
 				"								<div class=\"buap_form_label styled_Default\">\r\n" + 
 				"												<p>";
 		String label_2 = "</p>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		labelHTMLCode = new ArrayList<>(Arrays.asList(label_1, label_2));
 		//Submit
 		String submit_1 = "                                <!-- $FormDesignerMeta$Default$Submit$End -->\r\n" + 
 				"								<div class=\"buap_form_submit styled_Default\">\r\n" + 
 				"												<span>\r\n" + 
 				"																<input type=\"submit\" name=\"submit\" value=\"Enviar Forma\" />\r\n" + 
 				"												</span>\r\n" + 
 				"								</div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		submitHTMLCode = new ArrayList<>(Arrays.asList(submit_1));
 		//Logo Code
 		String logo_1 = "                                <!-- $FormDesignerMeta$Default$Logo$End -->\r\n" + 
 				"                                <div class=\"buap_logo styled_Grassland\">\r\n" + 
 				"                                    <span>\r\n" + 
 				"                                        <img src=\"fc_logo.jpg\" alt=\"Logo\">\r\n" + 
 				"                                    </span\r\n" + 
 				"                                    ><span class=\"logo_decoration\">\r\n" + 
 				"                                        <p>&nbsp</p>\r\n" + 
 				"                                    </span>      \r\n" + 
 				"                                </div>\r\n" + 
 				"                                <!-- $FormDesignerEndTag -->" + System.lineSeparator();
 		logoHTMLCode = new ArrayList<>(Arrays.asList(logo_1));
 		//Cursor code
 		openerCursorCode = "\r\n" + 
 				"                                <!--Cursor-->\r\n" + 
 				"                                <div class=\"buap_cursor styled_Default\">" 
 				+ System.lineSeparator();
 		closerCursorCode = "                                </div>" + System.lineSeparator();
 	}
 	public String getOpenerCursorCode()
 	{
 		//Used to get the early part of the cursor to surround the currently selected element.
 		return openerCursorCode;
 	}
 	public String getCloserCursorCode()
 	{
 		//Used to get the later part of the cursor to surround the currently selected element.
 		return closerCursorCode;
 	}
 	public FormSymbol createFormElement(String type)
 	{
 		/*
 		 * This method returns a FormSymbol with stock code and fully processed
 		 * Data and Metadata. The only thing it will lack is the inputs, which can
 		 * be tweaked and added by the GUI or program logic that asks for the Symbol.
 		 */
 		//Preparing Symbol to create
 		FormSymbol newSymbol = new FormSymbol("Default", type);	//Needs type and style to create, will start with the Default style.
 		ArrayList<String> elementHTMLCode = null;	//This will enable for SymbolWord creation anew.
 		ArrayList<String> elementExplanationCode = null;  //This will enable for SymbolWord creation anew.
 		int numberOfInputs = 0;
 		//Identifying type to create
 		if(type.equals("Header"))
		{
			//Then the required code was found for the header
			elementHTMLCode = headerHTMLCode;
			elementExplanationCode = headerExplanations;
			numberOfInputs = 1;
		}
		else if(type.equals("Label"))
		{
			//Then the required code was found for the header
			elementHTMLCode = labelHTMLCode;
			elementExplanationCode = labelExplanations;
			numberOfInputs = 1;
		}
		else if(type.equals("LargeMargin"))
		{
			//Then the required code was found for the header
			elementHTMLCode = largeMarginHTMLCode;
			numberOfInputs = 0;
		}
		else if(type.equals("SmallMargin"))
		{
			//Then the required code was found for the header
			elementHTMLCode = smallMarginHTMLCode;
			numberOfInputs = 0;
		}
		else if(type.equals("Submit"))
		{
			//Then the required code was found for the header
			elementHTMLCode = submitHTMLCode;
			numberOfInputs = 0;
		}
		else if(type.equals("OneLinerQuestion"))
		{
			//Then the required code was found for the header
			elementHTMLCode = oneLinerQuestionHTMLCode;
			elementExplanationCode = oneLinerQuestionExplanations;
			numberOfInputs = 1;
		}
		else if(type.equals("TwoLinerQuestion"))
		{
			//Then the required code was found for the header
			elementHTMLCode = twoLinerQuestionHTMLCode;
			elementExplanationCode = twoLinerQuestionExplanations;
			numberOfInputs = 2;
		}
		else if(type.equals("ThreeLinerQuestion"))
		{
			//Then the required code was found for the header
			elementHTMLCode = threeLinerQuestionHTMLCode;
			elementExplanationCode = threeLinerQuestionExplanations;
			numberOfInputs = 3;
		}
		else if(type.equals("TripleBoxText"))
		{
			//Then the required code was found for the header
			elementHTMLCode = tripleBoxTextHTMLCode;
			elementExplanationCode = tripleBoxTextExplanations;
			numberOfInputs = 3;
		}
		else if(type.equals("Logo"))
		{
			elementHTMLCode = logoHTMLCode;
			numberOfInputs = 0;
		}
		else
		{
			//Thin it is none of the above, an error should be thrown
			//ERROR
			formElementCreationPartialFailure = true;
			String error = "Attempted to create an invalid form type " + type + " but the program does not have the code for it."
					+ " This is a programmer error, return the software to the current "
					+ "developer for reviewal and repair." + System.lineSeparator();
			errors.append(error);
			disposeProcess();//Error handling
			return null;	//Fatal method error.----------------------------------ERROR EXIT POINT!
		}
 		//Filling with completely new data.
 		int i = 0;
 		while(i < numberOfInputs)
 		{
 			//Handling the inputs.
 			//Safely retrieving the HTML Code and build explanations for the element type.
 			String newHTMLCode = ( elementHTMLCode.size() > i ) ? elementHTMLCode.get(i) : "";
 			String newExplanation = (elementExplanationCode.size()) > i ? elementExplanationCode.get(i) : "";
 			//Building the new SymbolWord for this input.
 			SymbolWord newWord = new SymbolWord(newHTMLCode, newExplanation);
 			newSymbol.addSymbolWord(newWord); 	//Adding the newly invented data SymbolWord to the new FormSymbol.
 			i++;	//WHILE ITERATOR
 		}
 		//Handling the closing HTML code.
 		String closingHTMLCode = (elementHTMLCode.size() > i) ? elementHTMLCode.get(i) : "";	//Safely getting the finisher HTML code.
 		SymbolWord closerWord = new SymbolWord();
 		closerWord.setPreviousHTMLCode(closingHTMLCode);
 		newSymbol.addSymbolWord(closerWord); 	//Adding the last word of the symbol. It should not normally contain any inputs, just code.
 		//Creating the Symbol stock HTML Code
 		newSymbol.recreateHTMLCode();
 		//------------------------------
 		//Returning the complete symbol
 		return newSymbol;
 	}
	public ArrayList<FormSymbol> loadForm(File inputFile)
	{
		/*This method loads a file, given to it by the Format Creator Controller load form action.
		  It then holds the object inside it as a data fragment. It verifies the file exists and
		  if it does not, then it notifies its invoker.*/
		if(inputFile != null)
		{
			//The file does exist. Now verifying its HTML type.
			String fileName = inputFile.getAbsolutePath();
			String extension = "";

			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

			if (i > p) {
			    extension = fileName.substring(i+1);
			}
			if(extension.equals("html"))
			{
				//Success gate. If the file is a proper html file.
				form = inputFile;
				loadFormPartialSuccess = true;
				disposeProcess(); //Error handling.
				symbolizeForm();
				return formSymbols;
			}
			else
			{
				//Error handling. The form is not an html document.
				loadFormPartialFailure = true;
				String error = "The document " + inputFile.getAbsolutePath() + " is not an HTML document. All forms are HTML documents as well,"
						+ " provide an HTML document to continue." + System.lineSeparator();
				errors.append(error);
			}
		}
		disposeProcess();	//Error handling
		symbolizeForm();
		return formSymbols;
	}
	private void symbolizeForm()
	{
		/*
		 * This class identifies the meta data and extracts all the HTML code in between.
		 * 
		 * It then creates FormSymbols with the extracted data and stores them in the 
		 * formSymbols list.
		 */
		if(loadFormPartialSuccess) //Checking that a file exists, it was already verified as HTML by loadForm().
		{
			//Then the file should be read and the Symbols created and stored.
			boolean proceed = false; //Proceed flag if the file was indeed found.
			BufferedReader br = null;	//Buffered reader to scan the file.
			try {
				br = new BufferedReader(new FileReader(form));
				proceed = true; //The loading and creation of the BufferedReader was a success.
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				//File was moved, very unlikely. Otherwise a developer problem.
				//Error Handling
				symbolizeFormPartialFailure = true;
				String error = "The operation failed because the file " + form.getAbsolutePath() 
				+ " was open or manipulated while the program was" + " still working." + System.lineSeparator();
				errors.append(error);
				System.out.println("The file was not found in the symbolizeForm() method in the FormProcessor class.");
			}
			//BufferedReader should be ready.
			if(proceed)
			{
				//Then all goes as intended and the file is ready to be read.
				String rawLine = "";
				int lineCount = 0;
				try {
					while((rawLine = br.readLine()) != null)
					{
						lineCount ++; //Error handling
						//Finding if this is the line we want:
						int metaIndex = rawLine.indexOf("$FormDesignerMeta");
						if(metaIndex != -1)
						{
							//This is a meta line:
							//Substringing to find relevant metadata information:
							int relevanceStart = metaIndex + 17;
							int relevanceEnd = rawLine.indexOf("$End");
							if(relevanceEnd == -1 || relevanceEnd == relevanceStart)	//Error check
							{
								//RECOVERABLE ERROR
								//An error was found, the metadata has been wrongly modified
								symbolizeFormPartialFailure = true;
								String error = "The HTML file " + form.getAbsolutePath() + " has"
										+ " incorrectly modified metadata tags in line " + lineCount + " Replace with a previous"
										+ " version with correct metadata tags or repair the tags manually."
										+ System.lineSeparator();
								errors.append(error);
								continue;	//This element is broken jumping to the next one.
							}
							String relevantText = rawLine.substring(relevanceStart, relevanceEnd);
							//Subdividing to isolate relevant meta information:
							//The first array element is empty and should be ignored.
							String[] metaData = relevantText.split("\\$");
							if(metaData.length < 2)	//Error check
							{
								//Error Handling
								symbolizeFormPartialFailure = true;
								String error = "The HTML file " + form.getAbsolutePath() + " has"
										+ " incorrectly modified metadata tags in line " + lineCount + " Replace with a previous"
										+ " version with correct metadata tags or repair the tags manually."
										+ System.lineSeparator();
								errors.append(error);
							}
							String style = metaData[1];
							String type = metaData[2];
							//Continuing to read to accumulate the HTML Data text:
							boolean endTag = false; //to exit the subloop when an end tag is met.
							StringBuilder dataBuilder = new StringBuilder(); //To create a fully appended data string.
							String metaDataHeader = rawLine;
							dataBuilder.append(metaDataHeader); //Appending the header line unprocessed.
							//Error Handling
							boolean endTagSkipped = false;	//If an end tag was deleted and an element remains open ended.
							while(br.ready() && !endTag)
							{
								rawLine = br.readLine();
								//Error Handling:
								//The start of another meta tag can be found, thus an end tag would be missing
								int errorIndex = rawLine.indexOf("$FormDesignerMeta");
								if(errorIndex != -1)
								{
									//Then an end tag was missing.
									//One end tag was missing or corrupted and the document ended.
									symbolizeFormPartialFailure = true;
									String error = "An end tag: $FormDesignerEndTag is missing from the form. "
											+ "This means the metadata was modified and errased and some elements could "
											+ "not be loaded"
											+ ". Load an earlier version of the form with metadata or repair"
											+ " the metadata manually." + System.lineSeparator();
									errors.append(error);
									endTagSkipped = true;
									disposeProcess();	//Error handler.
									break;	//Exiting while to handle the error.
								}
								//Looking for end tag:
								int endTagPos = rawLine.indexOf("$FormDesignerEndTag");
								if(endTagPos != -1)
								{
									//The end tag was found and we should prime to exit the loop.
									endTag = true;
								}
								//Storing Data.
								String capturedLine = rawLine; //Copying immutable string.
								capturedLine += System.lineSeparator();	//Adding line breaks portably.
								dataBuilder.append(capturedLine); //Preparing for final result build.
							}
							if(endTagSkipped)
							{
								//Error Handling:
								//This element is already corrupted.
								//Skipping and proceeding with a new search.
								continue;	//LOOP EXIT POINT
							}
							if(!endTag)
							{
								//One end tag was missing or corrupted and the document ended.
								symbolizeFormPartialFailure = true;
								String error = "An end tag: $FormDesignerEndTag is missing from the form. "
										+ "This means the metadata was modified and errased and has made"
										+ " the form unloadable by this software. Load an earlier version of the"
										+ " form with metadata or repair the metadata manually." + System.lineSeparator();
								errors.append(error);
								disposeProcess();	//Error handler.
								return;	//This will have the program end in failure.
							}
							//Building the full data string.
							String data = dataBuilder.toString();
							//Creating Symbol Style, Type, Data
							FormSymbol formSymbol = new FormSymbol(style, type, data);
							//Processing data
							buildSymbolWords(formSymbol);
							//------
							formSymbols.add(formSymbol);	//Saving the form symbol;
							//Partial success has been accomplished
							symbolizeFormPartialSuccess = true;
						}
						
					}
					if(!symbolizeFormPartialSuccess)
					{
						//No elements were loaded at all, thus this is a failure in itself.
						symbolizeFormPartialFailure = true;
						String error = "No metadata was registered in the form " + form.getAbsolutePath()
						+ " it is either not a Form Creator product or had its metadata removed. Reload a"
						+ " previous version with the metadata intact or restore the metadata manually."
						+ System.lineSeparator();
						errors.append(error);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					//Throw an error to load alert.
					String errorMessage = "The Java BufferedReader could not read line " + lineCount +
							" in " + form.getAbsolutePath();
					errors.append(errorMessage);	//Appending the error message for later recovery.
				}
			}
			else
			{
				//Then something went wrong, should be covered by a previous error handling.
				System.out.println("The form could not be Symbolized in the Form Processor.");
			}
			try {
				br.close();	//Closing the reader.
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//This is a developer problem, if any.
			//Else this method was called when a form was not ready.
		}
		disposeProcess();	//Error Handling.
	}
	private boolean buildSymbolWords(FormSymbol symbol)
	{
		/*
		 * This method will receive a data String and further process it.
		 * Discovering its customizable inputs and storing them as SymbolWords
		 * which properly classify the data itself.
		 */
		if (symbol == null)
		{
			//Error Handling:
			//ERROR
			symbolWordCreationPartialFailure = true;
			String error = "This is a programmer error. A symbol provided to the buildSymbolWords method in the FormProcessor was null. "
					+ "deliver this error to the current programerng for correction." + System.lineSeparator();
			errors.append(error);
			disposeProcess();
			return false;	//Fatal error ---------------------------- ERROR EXIT POINT!
			
		}
		boolean methodSuccess = true;
		String data = symbol.outputHTMLCode();	//Getting the HTML code to find the inputs.
		String type = symbol.getType();	//The type of the symbol, which means of this HTML form entity.
		ArrayList<String> inputs = new ArrayList<>();	//Here the inputs found are stored.
		ArrayList<String> elementHTMLCode = null;	//For each element type, there is a number of HTML code fragments that separate the inputs.
		ArrayList<String> elementExplanationCode = null;	//To add the editing explanation to each element. Might not be necessary but for standardization.
		int numberOfInputs = 0;	//If this remains 0 then it has no inputs.
		//Identify the number of inputs according to type.
		if(type.equals("Header"))
		{
			//Then the required code was found for the header
			elementHTMLCode = headerHTMLCode;
			elementExplanationCode = headerExplanations;
			numberOfInputs = 1;
		}
		else if(type.equals("Label"))
		{
			//Then the required code was found for the header
			elementHTMLCode = labelHTMLCode;
			elementExplanationCode = labelExplanations;
			numberOfInputs = 1;
		}
		else if(type.equals("LargeMargin"))
		{
			//Then the required code was found for the header
			elementHTMLCode = largeMarginHTMLCode;
			numberOfInputs = 0;
		}
		else if(type.equals("SmallMargin"))
		{
			//Then the required code was found for the header
			elementHTMLCode = smallMarginHTMLCode;
			numberOfInputs = 0;
		}
		else if(type.equals("Submit"))
		{
			//Then the required code was found for the header
			elementHTMLCode = submitHTMLCode;
			numberOfInputs = 0;
		}
		else if(type.equals("OneLinerQuestion"))
		{
			//Then the required code was found for the header
			elementHTMLCode = oneLinerQuestionHTMLCode;
			elementExplanationCode = oneLinerQuestionExplanations;
			numberOfInputs = 1;
		}
		else if(type.equals("TwoLinerQuestion"))
		{
			//Then the required code was found for the header
			elementHTMLCode = twoLinerQuestionHTMLCode;
			elementExplanationCode = twoLinerQuestionExplanations;
			numberOfInputs = 2;
		}
		else if(type.equals("ThreeLinerQuestion"))
		{
			//Then the required code was found for the header
			elementHTMLCode = threeLinerQuestionHTMLCode;
			elementExplanationCode = threeLinerQuestionExplanations;
			numberOfInputs = 3;
		}
		else if(type.equals("TripleBoxText"))
		{
			//Then the required code was found for the header
			elementHTMLCode = tripleBoxTextHTMLCode;
			elementExplanationCode = tripleBoxTextExplanations;
			numberOfInputs = 3;
		}
		else if(type.equals("Logo"))
		{
			//Then the required code was found for the header
			elementHTMLCode = logoHTMLCode;
			numberOfInputs = 0;
		}
		else
		{
			methodSuccess = false;
			//Thin it is none of the above, an error should be thrown
			//ERROR
			symbolWordCreationPartialFailure = true;
			String error = "A symbol given to buildSymbolWords in the FormProcessor was of type " + type +
					" but the program is unable to handle that type. This is a programmer error, show this to your"
					+ " current programmer for correction." + System.lineSeparator();
			errors.append(error);
			disposeProcess();
			return false;	//Fatal error ------------------------------------ ERROR EXIT
		}
		//Find inputs from <p> and <h2> HTML tags
		int lastFoundIndex = 0;
		for(int i = 0; i < numberOfInputs; i++)
		{
			boolean foundPInput = false;	//If a single input tag was found.
			boolean foundHInput = false;
			//Looking for all possible customizable input tags.
			int foundTag = data.indexOf("<p>", lastFoundIndex);
			if(foundTag == -1)
			{
				//Then there is no input <p> tag.
				foundTag = data.indexOf("<h2>", lastFoundIndex);
				if(foundTag == -1) 
				{
					methodSuccess = false;
					//There are no input tags left. Launch error
					//ERROR
				}
				else
				{
					foundHInput = true;
					//Found an h2 tag
					lastFoundIndex = foundTag;
				}
			}
			else
			{
				foundPInput = true;
				//Found a p tag
				lastFoundIndex = foundTag;
			}
			if(foundPInput)
			{
				//Then lastFoundTag contains the position of the input we are looking for.
				//<p> tag found
				int relevanceEnd = data.indexOf("</p>", lastFoundIndex);	//The closing tag.
				//Error Handling
				if(relevanceEnd == -1)
				{
					methodSuccess= false;
					//ERROR. Open tag. Exit method, this word is corrupted fatally.
					symbolWordCreationPartialFailure = true;
					String error = "The HTML code loaded is corrupted. It lacks a closing tag </p>."
							+ " reload a previous version of the HTML file." + System.lineSeparator();
					errors.append(error);
					disposeProcess();
					return false;	//Fatal error -------------------------------- ERROR EXIT
				}
				//--------
				lastFoundIndex += 3;	//Ignoring the <p> tag.
				inputs.add(data.substring(lastFoundIndex, relevanceEnd));
				methodSuccess = true;
			}
			else if (foundHInput)
			{
				//Then lastFoundTag contains the position of the input we are looking for.
				//<h2> tag found
				int relevanceEnd = data.indexOf("</h2>", lastFoundIndex);	//The closing tag.
				//Error Handling
				if(relevanceEnd == -1)
				{
					methodSuccess = false;
					//ERROR. Open tag. Exit method, this word is corrupted fatally.
					symbolWordCreationPartialFailure = true;
					String error = "The HTML code loaded is corrupted. It lacks a closing tag </h2>."
							+ " reload a previous version of the HTML file." + System.lineSeparator();
					errors.append(error);
					disposeProcess();
					return false;	//Fatal error -------------------------------- ERROR EXIT
				}
				//--------
				lastFoundIndex += 4;	//Ignoring the <h2> tag.
				inputs.add(data.substring(lastFoundIndex, relevanceEnd));
				methodSuccess = true;
			}
		}
		//Read inputs and store
		//For each input build the SymbolWord and add it into the symbol itself.
		if(methodSuccess) 	//Precaution, may be largely unnecessary.
		{
			int i = 0;
			while(i < numberOfInputs)
			{
				//Data needed to create the WordSymbol:
				//Getting the HTML code safely.
				String previousHTMLCode = (elementHTMLCode.size() > i) ? elementHTMLCode.get(i) : "";
				//Getting the explanation code safely.
				String currentExplanation = (elementExplanationCode != null) ? 
						( (elementExplanationCode.size() > i ) ? elementExplanationCode.get(i) : "" )
						: "";	//If the element has an explanation at all.
				String input = inputs.get(i);	//The current input that will be symbolized. Should be nullity protected by method logic.
				//Creating SymbolWord: Code, Explanation, Input
				SymbolWord symbolWord = new SymbolWord(previousHTMLCode, currentExplanation, input);
				symbol.addSymbolWord(symbolWord);	//Add the new SymbolWord to the existing symbol. Augmenting.
				i++;	//WHILE ITERATOR
				//If any of the above jumps out of scope that would mean there are some logic errors. Else it is a safely ignorable text inputs.
			}
			//Last bit of HTML Code.
			String closingHTMLCode = (elementHTMLCode.size() > i) ? elementHTMLCode.get(i) : "";	//Safely retrieving finisher code
			SymbolWord symbolWord = new SymbolWord();
			symbolWord.setPreviousHTMLCode(closingHTMLCode);
			symbol.addSymbolWord(symbolWord);	//Adding the finished symbol word to the symbol fed to the method.
			//Ordering the symbol to recreate its HTML Code. Standardizing it with the internal elements' model.
			symbol.recreateHTMLCode();
			//DEBUG
			System.out.println("Rebuilt HTML Code " + System.lineSeparator() + symbol.outputHTMLCode());
			//--------------------
		}
		//----------
		//If failure, handle error, with its notification service.
		return methodSuccess;
	}
	private void disposeProcess()
	{
		/*
		 * This is the error handler for the From Processor.
		 */
		StringBuilder generalError = new StringBuilder();	//For reporting the complete error.
		boolean hadError = false;
		if(loadFormPartialFailure || symbolizeFormPartialFailure)
		{
			hadError = true;
			//Then something has failed, and the errors must be notified.
			if(symbolizeFormPartialSuccess)
			{
				String error1 = "There were some errors in the metadata of the target form, but some components could still be loaded."
						+ System.lineSeparator();
				generalError.append(error1);
			}
			else
			{
				String error2 = "Due to errors in the metadata of the target form, it could not be loaded."
						+ System.lineSeparator();
				generalError.append(error2);
			}
			String error3 = "The errors encountered when loading the form were:" + System.lineSeparator();
			generalError.append(error3);
			generalError.append(errors.toString());	//Delivering all the errors encountered.
			//DEBUG
			System.out.println(generalError.toString());
		}
		if(formElementCreationPartialFailure)
		{
			hadError = true;
			String error4 = "A form element could not be created. Because of the following errors:" + System.lineSeparator();
			generalError.append(error4);
			generalError.append(errors.toString());	//Adding errors.
			//DEBUG
			System.out.println(generalError.toString());
		}
		if(symbolWordCreationPartialFailure)
		{
			hadError = true;
			String error5 = "The HTML code loaded in the form could not be processed correctly, here are the thrown errors:" +
			System.lineSeparator();
			generalError.append(error5);
			generalError.append(errors.toString());
			//DEBUG
			System.out.println(generalError.toString());
		}
		if(hadError)	//If any form of error was encountered, it will be reported.
		{
			ErrorNotifier.pushErrorMessage(generalError.toString()); //Delivering to the error notifier for other use in the software.
		}
		resetErrorState();	//Now that the report was raised, the flags that signaled it are reset.
		//This prevents double reporting of the same problem.
	}
	private void resetErrorState()
	{
		//This resets all error reporting information;
		loadFormPartialFailure = false;
		symbolizeFormPartialFailure = false;
		formElementCreationPartialFailure = false;
		symbolWordCreationPartialFailure = false;
		errors = new StringBuilder();
	}
	//-----------------------------------
	//-----------------------------------
	//Debug
	public void checkFileIntegrity()
	{
		System.out.println(form.getAbsolutePath());
	}
	public void checkSymbolsIntegrity()
	{
		for(int i = 0; i < formSymbols.size(); i++)
		{
			System.out.println(formSymbols.get(i).outputHTMLCode());
		}
	}
}
