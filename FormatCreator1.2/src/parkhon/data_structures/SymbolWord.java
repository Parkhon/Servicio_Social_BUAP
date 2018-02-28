package parkhon.data_structures;

public class SymbolWord 
{

	/*
	 * This represents a form element part. They can be stacked in front of each other
	 * through a list. Each is composed of its HTML code that goes before the customizable element,
	 * then it also has the customizable part itself.
	 * 
	 * By customizable part it is meant the part of the HTML code of a form element that is customized by
	 * the user of the Form Creator, all customizable parts.
	 * 
	 * The customizable part is also an object, and it is possesses encapsulation for its data.
	 */
	//Attribute
	private String previousHTMLCode;	//The code that goes before a customizable part.
	private CustomizablePart customizable;	//The things the user types into their form. WILL NOT VALIDATE INPUTS, INJECTIONS POSSIBLE.
	//-------------------------------------------
	//-------------------------------------------
	//Constructor
	public SymbolWord()
	{
		//Default constructor.
		previousHTMLCode = "";
		customizable = null;
	}
	public SymbolWord(String previousHTMLCode, String customizationExplanation, String userInput)
	{
		//Complete constructor. Also fills its subordinate CustomizablePart.
		this.previousHTMLCode = previousHTMLCode;
		customizable = new CustomizablePart(customizationExplanation, userInput);
	}
	public SymbolWord(String previousHTMLCode, String customizationExplanation)
	{
		//Constructor without input. Useful for creating new FormSymbols and thus new forms.
		String userInput = "";
		this.previousHTMLCode = previousHTMLCode;
		customizable = new CustomizablePart(customizationExplanation, userInput);
	}
	//-------------------------------------------
	//-------------------------------------------
	//Methods:
	public String getPreviousHTMLCode() {
		return previousHTMLCode;
	}
	public void setPreviousHTMLCode(String previousHTMLCode) {
		this.previousHTMLCode = previousHTMLCode;
	}
	public CustomizablePart getCustomizable() {
		return customizable;
	}
	//Complex methods. Easier handling.
	public String getCustomizableExplanation()
	{
		//Easier way to get the explanation.
		if(customizable != null)
		{
			return customizable.getExplanation();
		}
		else return "";
	}
	public String getCustomizableInput()
	{
		//Easier way to get the input.
		if(customizable != null)
		{
			return customizable.getInput();
		}
		else return "";
	}
	public void setCustomizableExplanation(String explanation) 
	{
		//If customizable is not yet created then create, else just change its value.
		if(customizable == null)
		{
			this.customizable = new CustomizablePart();
		}
		this.customizable.setExplanation(explanation);
	}
	public void setCustomizableInput(String input)
	{
		//If customizable is not yet created then create, else just change its value.
		if(customizable == null)
		{
			this.customizable = new CustomizablePart();
		}
		this.customizable.setInput(input);
	}
	//-------------------------------------------
	//-------------------------------------------
}
