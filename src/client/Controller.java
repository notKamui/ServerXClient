/**
 * Sample Skeleton for 'client.fxml' Controller Class
 */

package client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="ip"
    private TextField ip; // Value injected by FXMLLoader

    @FXML // fx:id="port"
    private TextField port; // Value injected by FXMLLoader

    @FXML // fx:id="inputHistory"
    private TextArea inputHistory; // Value injected by FXMLLoader

    @FXML // fx:id="msgBox"
    private TextField msgBox; // Value injected by FXMLLoader

    @FXML // fx:id="serverOutput"
    private TextArea serverOutput; // Value injected by FXMLLoader

    @FXML // fx:id="logHistory"
    private TextArea logHistory; // Value injected by FXMLLoader

    @FXML
    void connect(ActionEvent event) {

    }

    @FXML
    void sendMsg(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert ip != null : "fx:id=\"ip\" was not injected: check your FXML file 'client.fxml'.";
        assert port != null : "fx:id=\"port\" was not injected: check your FXML file 'client.fxml'.";
        assert inputHistory != null : "fx:id=\"inputHistory\" was not injected: check your FXML file 'client.fxml'.";
        assert msgBox != null : "fx:id=\"msgBox\" was not injected: check your FXML file 'client.fxml'.";
        assert serverOutput != null : "fx:id=\"serverOutput\" was not injected: check your FXML file 'client.fxml'.";
        assert logHistory != null : "fx:id=\"logHistory\" was not injected: check your FXML file 'client.fxml'.";

    }
}
