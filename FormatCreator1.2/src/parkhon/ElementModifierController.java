package parkhon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import parkhon.data_structures.SymbolWord;
import parkhon.logic.FormSymbol;

public class ElementModifierController {

	//Controller Attributes
    @FXML
    private BorderPane p_root;

    @FXML
    private GridPane p_accept_bar;

    @FXML
    private Button b_cancel;

    @FXML
    private Button b_next;

    @FXML
    private Button b_finish;

    @FXML
    private BorderPane p_toolbar;

    @FXML
    private TextArea a_explanation;

    @FXML
    private TextArea a_input;
    //------------------------------------------
    //------------------------------------------
    //Attributes
    private FormSymbol form;
    int wordCounter;
    //------------------------------------------
    //------------------------------------------
    //Controller Methods
    @FXML
    private void initialize()
    {
    	//Default Values:
    	form = null;
    	wordCounter = 0;
    }
    @FXML
    void onCancel(ActionEvent event) {
    	//This method just closes the window.
    	//TODO Could alter to make it restore the old window state.
    	closeForm();
    }

    @FXML
    void onFinish(ActionEvent event) {
    	//Getting the user's input
    	String userInput = a_input.getText();
    	// The possition that was just read, thus the -1.
    	form.getSymbolWordAt(wordCounter - 1).setCustomizableInput(userInput);
    	//Closing the window
    	saveAndCloseForm();
    }

    @FXML
    void onNext(ActionEvent event) {
    	//Getting the user's input
    	String userInput = a_input.getText();
    	// The possition that was just read, thus the -1.
    	form.getSymbolWordAt(wordCounter - 1).setCustomizableInput(userInput);
    	//Flushing the input area.
    	a_input.setText("");
    	//Updating the window to prime the next word for modification
    	// or close the window if there is nothing else to edit.
    	updateModifierStatus();
    }
    //------------------------------------------
    //------------------------------------------
    //Methods
    public void uploadFormSymbol(FormSymbol symbol)
    {
    	form = symbol;
    	//Preparing original window state:
    	updateModifierStatus();
    }
    private void updateModifierStatus()
    {
    	/*
    	 * This method updates the explanation for the new element
    	 * to create. If a word is not meant to be written on, it
    	 * closes the customization window, as there is nothing to
    	 * customize.
    	 */
    	//Necessary structures
    	SymbolWord word = null;
    	//Checing to see if everything is in consistent state
    	boolean proceed = (form != null) ? true : false ;
    	if(proceed)
    	{
    		word = form.getSymbolWordAt(wordCounter);	//Getting the words
    		if(word != null)
    		{
    			//The next word has been successfully loaded.
    			wordCounter++;	//Increasing the counter for subsequent reads.
    			//Checking to see if this word should have user input
    			if(!word.getCustomizableExplanation().equals(""))
    			{
    				//Then this word is open for customization
    				a_explanation.setText(word.getCustomizableExplanation());
    			}
    			else
    			{
    				//Else this is a non customizable word. Closing window.
    				saveAndCloseForm();
    			}
    		}
    		else
    		{
    			//No word at wordCounter found. Which mean it is time to close this window on this
    			//"next" button click.
    			//
    			//Closing window:
    			saveAndCloseForm();
    		}
    	}
    	else
    	{
    		//ERROR: the form is null.
    		//Closing window
    		closeForm();
    	}
    }
    private void saveAndCloseForm()
    {
    	form.recreateHTMLCode();
    	Stage stage = (Stage)b_cancel.getScene().getWindow();
    	stage.close();
    }
    private void closeForm()
    {
    	Stage stage = (Stage)b_cancel.getScene().getWindow();
    	stage.close();
    }
    //------------------------------------------
    //------------------------------------------

}
