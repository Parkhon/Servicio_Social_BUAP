package parkhon.logic;

import java.util.ArrayList;

import parkhon.data_structures.SymbolWord;

public class FormSymbol 
{
	/*
	 * Important! I just need to store the HTML code, position and style (maybe type to optimize the CSS). 
	 * This will make this class much simpler.
	 */
	//Attributes
	private String style;
	private String data;
	private String type;
	private ArrayList<SymbolWord> words;	//The data itself is represented as blocks of HTML Code and customizable inputs.
	//
	//--------------------------------
	//--------------------------------
	//Constructor
	public FormSymbol(String style, String type, String data)
	{
		//This is the constructor of the form symbol. It receives all of its
		//values upon being created.
		this.style = style;
		this.type = type;
		this.data = data;
		//Initiating words list.
		words = new ArrayList<>();
	}
	public FormSymbol(String style, String type)
	{
		//This is the constructor of the form symbol. It does not have data.
		this.style = style;
		this.type = type;
		this.data = "";
		//Initiating words list.
		words = new ArrayList<>();
	}
	//--------------------------------
	//--------------------------------
	//Methods
	public void recreateHTMLCode()
	{
		/*
		 * This method refreshes the HTML code by utilizing the SymbolWords. Can be
		 * used to update the HTML Code of a symbol after it has been edited. Or to
		 * erase any errors in the HTML code after the metadata has been successfully read.
		 * 
		 * It also changes the style of its code to match its metadata style.
		 */
		if(words.size() > 0)
		{
			StringBuilder codeBuilder = new StringBuilder();
			//If there are words to recreate the HTML Code.
			int i = 0;	//Counter to find the first word and change the styled_ class.
			for(SymbolWord word : words)
			{
				if(i == 0)
				{
					//Then this the first word and contains the styled_ class.
					//Now we look for the styled text, which should be delimited by either
					//a space, or double quotation: "
					String previousCode = null;	//The text that goes before the styled_<style> class.
					String posteriorCode = null;	//The text that goes after the styled_<style> class.
					//Looking for the index of the style class:
					int styleIndex = word.getPreviousHTMLCode().indexOf("styled_");
					//If the search was a success:
					if(styleIndex != -1)
					{
						//Then we have found the code that regulates which style to display.
						//And now we shall change it.
						//
						//Getting the code previous to the specific style
						previousCode = word.getPreviousHTMLCode().substring(0, styleIndex + 7);
						//Discover the actual style, to be able to get the posterior code and then rebuild this word.
						int finalIndex = -1;	//The actual index we will use to find the posterior code.
						int spaceIndex = word.getPreviousHTMLCode().indexOf(" ", styleIndex + 7);
						int doubleQuoteIndex = word.getPreviousHTMLCode().indexOf("\"", styleIndex + 7);
						//Finding the closure for the style class.
						if(spaceIndex != -1)
						{
							//Then the space index does exist.
							if(doubleQuoteIndex != -1)
							{
								//Then the double quotes also exist
								if(spaceIndex < doubleQuoteIndex)
								{
									//Both a space and double quotes exist, but the space is first.
									finalIndex = spaceIndex;
								}
								else
								{
									//Thin both exist but the double quote was first.
									finalIndex = doubleQuoteIndex;
								}
							}
							else
							{
								//Then only the space exists
								finalIndex = spaceIndex;
							}
						}
						else
						{
							//Else the space index does not exist
							if(doubleQuoteIndex != 1)
							{
								//The double quote does exist, thus it is it.
								finalIndex = doubleQuoteIndex;
							}
							else
							{
								//None of the two indexes exist.
								//Thus we will do nothing.
							}
						}
						//Now we get the posterior code.
						posteriorCode = word.getPreviousHTMLCode().substring(finalIndex);
						String newCode = previousCode + style + posteriorCode;
						//Rebuilding this part of the element's code, with the new style.
						word.setPreviousHTMLCode(newCode);	//Setting the corrected new code.
					}
					//It also contains the style metadata:
					String metaSearchTarget = "$FormDesignerMeta";
					int metaStyleIndex = word.getPreviousHTMLCode().indexOf(metaSearchTarget);
					if(metaStyleIndex != -1)
					{
						//Where the relevant string commences.
						int relevanceStartMeta = metaStyleIndex + metaSearchTarget.length() + 1;	//Ignoring the previous style.
						//Where the relevant string ends
						int metaStyleEnd = word.getPreviousHTMLCode().indexOf("$", relevanceStartMeta);
						if(metaStyleEnd != -1)
						{
							int relevanceEndMeta = metaStyleEnd;
							//Getting the relevant string
							String relOpener = word.getPreviousHTMLCode().substring(0, relevanceStartMeta);
							String relCloser = word.getPreviousHTMLCode().substring(relevanceEndMeta);
							String replacerCode = relOpener + style + relCloser;	//The code to replace
							word.setPreviousHTMLCode(replacerCode);
						}
					}
				}
				String newCode = word.getPreviousHTMLCode() + word.getCustomizableInput();
				codeBuilder.append(newCode);
				i++;	//Finding first element counter.
			}
			this.data = codeBuilder.toString();	//Refreshing the HTML Code.
			//DEBUG
			System.out.println(this.data);
		}
	}
	public void addSymbolWord(SymbolWord word)
	{
		/*
		 * Receives a processed data word and stores it as part of this symbol.
		 */
		words.add(word);
	}
	public ArrayList<SymbolWord> getSymbolWords()
	{
		/*
		 * Gives the symbol words that represent the processed data. Used
		 * by the GUI to get the explanations needed for each SymbolWord and
		 * adding the input.
		 */
		return words;
	}
	public SymbolWord getSymbolWordAt(int place) 
	{
		SymbolWord output = null;
		if(words.size() > place && place >= 0)
		{
			//Then this is a valid request:
			output =  words.get(place);
		}
		return output;
	}
	public int getSymbolWordsSize()
	{
		return words.size();
	}
	public String outputHTMLCode()
	{
		/*
		 * This method creates the HTML code to be rendered by the program and in the end
		 * the output form itself.
		 */
		return data;
	}
	public String getType()
	{
		//Simply returns the style id for this symbol, to allow the Form Processor to create the
		//final CSS code.
		return type;
	}
	public String getStylingType()
	{
		/*
		 * This returns the type of styling to import. In
		 * most cases this is just the normal element type,
		 * but on open questions and labels, this is different.
		 */
		String value = "";
		if(type.equals("OneLinerQuestion") || type.equals("TwoLinerQuestion") || type.equals("ThreeLinerQuestion"))
		{
			value = "OpenQuestion";
		} else if(type.equals("LargeMargin") || type.equals("SmallMargin"))
		{
			value = "Margin";
		}
		else
		{
			value = type;
		}
		return value;
	}
	public String getStyle()
	{
		return style;
	}
	public void setStyle(String newStyle)
	{
		this.style = newStyle;
		if(words.size() > 0)
		{
			//If there are symbol words, we also recreate the HTML code
			//with the new styling.
			recreateHTMLCode();
		}
	}
	//--------------------------------
	//--------------------------------
	
}
