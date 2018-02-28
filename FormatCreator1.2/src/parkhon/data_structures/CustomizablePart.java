package parkhon.data_structures;

public class CustomizablePart {
	/*
	 * This data structure class contains the relevant data for a fragment of a SymbolWord.
	 * 
	 * This is an explanation for a user's input, which will be displayed on the Customize Element
	 * window. And it also contains the user's input itself. Which is whatever the user desires will
	 * appear in his customized element fragment.
	 */
	//Attributes
	private String explanation;
	private String input;
	//-------------------------------------------
	//-------------------------------------------
	//Constructor
	public CustomizablePart(String customizationExplanation, String userInput) {
		//Full values constructor for this data structure.
		this.explanation = customizationExplanation;
		this.input = userInput;
	}

	public CustomizablePart() {
		//Default values:
		explanation = "";
		input = "";
	}
	//-------------------------------------------
	//-------------------------------------------
	//Methods
	//Getters and setters
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getExplanation() {
		return explanation;
	}

	public String getInput() {
		return input;
	}
	//-------------------------------------------
	//-------------------------------------------

}
