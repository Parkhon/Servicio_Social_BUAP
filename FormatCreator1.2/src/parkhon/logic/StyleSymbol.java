package parkhon.logic;

import java.util.HashMap;

public class StyleSymbol 
{
	/*
	 * This class will represent all the loaded CSS elements, and facilitates
	 * the generation of the Ad Hoc CSS files during runtime. As well as the
	 * creation of the ultimate finished CSS file for the finished form.
	 */
	//Attributes
	private HashMap<String, String> elements;	//This Hash can deliver the CSS code for an element from its name.
	private String styleName; //This is the style name. Useful for recognition.
	private String stylePath;
	//----------------------------------------------
	//----------------------------------------------
	//Constructor
	public StyleSymbol()
	{
		elements = new HashMap<String, String>();
	}
	//----------------------------------------------
	//----------------------------------------------
	//Methods
	public void setStyleName(String styleName)
	{
		this.styleName = styleName;
	}
	public void addElement(String type, String code)
	{
		/*
		 * This method adds an element to the elements hash. Registering both
		 * the type name as well as the code that it should have.
		 * 
		 * To retrieve elements use the get method.
		 */
		elements.put(type, code);
	}
	public String getElementCode(String type)
	{
		/*
		 * This gets the CSS code for the target element.
		 */
		String result = elements.get(type);		//Recovering the CSS code for the target type.
		return (result == null ? "" : result);	//Safety if the key is not in the hash.
	}
	public String getStyleName( )
	{
		/*
		 * Getter for the style name.
		 */
		return styleName;
	}
	public String getStylePath() {
		return stylePath;
	}
	public void setStylePath(String stylePath) {
		this.stylePath = stylePath;
	}
	
	//----------------------------------------------
	//----------------------------------------------
}
