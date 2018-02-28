package parkhon;

import parkhon.logic.FormSymbol;
import parkhon.logic.StyleSymbol;

public class ElementStyleTuple 
{
/*
 * This class represents the tuple of a form HTML element, paired
 * with its CSS styling. It is to be used by the StylingContext, and
 * is required to produce the CSS style sheet dynamically.
 */
	//Attributes
	//-----------------------------------
	//-----------------------------------
	private FormSymbol element;	//The HTML
	private StyleSymbol style;	//The CSS
	//Constructor
	//-----------------------------------
	//-----------------------------------
	public ElementStyleTuple(FormSymbol element, StyleSymbol style)
	{
		this.setElement(element);
		this.setStyle(style);
	}
	public ElementStyleTuple()
	{
		//Default constructor.
	}
	//Methods
	public FormSymbol getElement() {
		return element;
	}
	public void setElement(FormSymbol element) {
		this.element = element;
	}
	public StyleSymbol getStyle() {
		return style;
	}
	public void setStyle(StyleSymbol style) {
		this.style = style;
		this.element.setStyle(style.getStyleName());
	}
	//-----------------------------------
	//-----------------------------------
}
