package parkhon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class UserErrorController {

    @FXML
    private Button b_accept;

    @FXML
    private ImageView i_error_icon;

    @FXML
    private TextArea te_errors;

    @FXML
    void onAccept(ActionEvent event) {
    	//Closing the window.
        Stage stage = (Stage) b_accept.getScene().getWindow();
        stage.close();
    }
    
    //Methods:
    public void setErrorMessage(String errorMessage)
    {
    	//Deliver what error text should be displayed on this window.
    	te_errors.setText(errorMessage);
    }

}
