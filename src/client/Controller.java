/**
 * Sample Skeleton for 'client.fxml' Controller Class
 */

package client;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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

    @FXML // fx:id="lblIH"
    private Label lblIH; // Value injected by FXMLLoader

    @FXML // fx:id="lblW"
    private Label lblW; // Value injected by FXMLLoader

    @FXML // fx:id="lblSO"
    private Label lblSO; // Value injected by FXMLLoader

    @FXML // fx:id="lblLH"
    private Label lblLH; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnect"
    private Button btnConnect; // Value injected by FXMLLoader

    @FXML // fx:id="btnSend"
    private Button btnSend; // Value injected by FXMLLoader

    @FXML // fx:id="btnClose"
    private Button btnClose; // Value injected by FXMLLoader

    private Client client;

    private ObservableList<String> soList;

    private ObservableList<String> inList;

    private ObservableList<String> logList;

    @FXML
    void connect(ActionEvent event) {
        try {
            client = new Client(ip.getText(), Integer.parseInt(port.getText()));
            if (client.getSocket() != null) {
                client.initWriter();
                client.initReader();
                logList.add("> Connected to server " + ip.getText() + " on port " + port.getText());
                toggleDisable();
                soList.add("> " + client.recv());
            } else {
                logList.add("> Connection failed");
            }
        } catch (NumberFormatException e) {
            logList.add("> Invalid parameters");
        }
    }

    @FXML
    void close(ActionEvent event) {
        client.send("x");
        closeConnection();
    }

    @FXML
    void sendMsg(ActionEvent event) {
        client.send(msgBox.getText());
        if (msgBox.getText().equals("x")) {
            closeConnection();
        } else {
            inList.add("> " + msgBox.getText());
            logList.add("> Sent message ; Size : " + msgBox.getText().length());
            msgBox.setText("");
            String s = client.recv();
            soList.add("> " + s);
            logList.add("> Received reversed echo ; Size : " + s.length());
        }
    }

    private void toggleDisable() {
        ip.setDisable(true);
        port.setDisable(true);
        btnConnect.setDisable(true);
        btnConnect.setVisible(false);

        btnClose.setDisable(false);
        btnClose.setVisible(true);
        inputHistory.setDisable(false);
        msgBox.setDisable(false);
        serverOutput.setDisable(false);
        lblIH.setDisable(false);
        lblW.setDisable(false);
        lblSO.setDisable(false);
        btnSend.setDisable(false);
    }

    private void initLists() {
        soList = FXCollections.observableArrayList();
        soList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        serverOutput.appendText((soList.size() <= 1 ? "" : "\n") + soList.get(change.getFrom()));
                    }
                }
            }
        });

        inList = FXCollections.observableArrayList();
        inList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        inputHistory.appendText((inList.size() <= 1 ? "" : "\n") + inList.get(change.getFrom()));
                    }
                }
            }
        });

        logList = FXCollections.observableArrayList();
        logList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        logHistory.appendText((logList.size() <= 1 ? "" : "\n") + logList.get(change.getFrom()));
                    }
                }
            }
        });
    }

    private void closeConnection() {
        logList.add("> Closing connection");
        logList.add("> Exitting application");
        client.close();
        System.exit(0);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert ip != null : "fx:id=\"ip\" was not injected: check your FXML file 'client.fxml'.";
        assert port != null : "fx:id=\"port\" was not injected: check your FXML file 'client.fxml'.";
        assert inputHistory != null : "fx:id=\"inputHistory\" was not injected: check your FXML file 'client.fxml'.";
        assert msgBox != null : "fx:id=\"msgBox\" was not injected: check your FXML file 'client.fxml'.";
        assert serverOutput != null : "fx:id=\"serverOutput\" was not injected: check your FXML file 'client.fxml'.";
        assert logHistory != null : "fx:id=\"logHistory\" was not injected: check your FXML file 'client.fxml'.";

        initLists();
    }
}
