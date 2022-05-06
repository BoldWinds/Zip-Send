package Gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import SendAndReceive.Server;
import MyUtils.OtherMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ReceiveGUIController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="targetPath"
    private TextField targetPath; // Value injected by FXMLLoader

    @FXML
    void check(ActionEvent event) {
        File file = new File(targetPath.getText());
        if(!file.exists()){
            OtherMethod.warning("目标文件夹不存在");
            return;
        }
        OtherMethod.inform("格式正确");
    }

    @FXML
    void clear(ActionEvent event) {
        targetPath.setText("");
    }

    @FXML
    void receive(ActionEvent event) {
        Thread receiveThread = new Thread(new Server(targetPath.getText()));
        receiveThread.start();
    }

    @FXML
    void selectTarget(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择要解压到的目标文件夹");
        File file = directoryChooser.showDialog(new Stage());
        if(file!=null){
            targetPath.setText(file.getPath());
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert targetPath != null : "fx:id=\"targetPath\" was not injected: check your FXML file 'receive-gui.fxml'.";

    }
}

