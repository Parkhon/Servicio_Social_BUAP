package parkhon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import parkhon.logic.FormCreatorLogic;

public class EmailEntryController {

    @FXML
    private BorderPane p_root;

    @FXML
    private GridPane p_toolbar;

    @FXML
    private TextField a_email_entry;

    @FXML
    private GridPane p_accept_bar;

    @FXML
    private Button b_cancel;

    @FXML
    private Button b_accept;

    @FXML
    void onAccept(ActionEvent event) {
    	String email = a_email_entry.getText();
    	if(email != null)
	    	if(!email.equals(""))
	    	{
	    		//If some address was entered:
	    		FormCreatorLogic.generateFormForEmail(email);
	    		//Exit window
	        	Stage stage = (Stage)b_accept.getScene().getWindow();
	        	stage.close();
	    	}
    }

    @FXML
    void onCancel(ActionEvent event) {
    	//Exit window
    	Stage stage = (Stage)b_cancel.getScene().getWindow();
    	stage.close();
    }

}
