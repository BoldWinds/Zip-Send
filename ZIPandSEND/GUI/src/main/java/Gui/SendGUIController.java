/**
 * Sample Skeleton for 'send-gui.fxml' Controller Class
 */

package Gui;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import SendAndReceive.Client;
import MyUtils.OtherMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SendGUIController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="sourcePath"
    private TextField sourcePath; // Value injected by FXMLLoader

    @FXML // fx:id="ipAddress"
    private TextField ipAddress; // Value injected by FXMLLoader


    @FXML
    void check(ActionEvent event) {
        File file = new File(sourcePath.getText());
        if(!file.exists()){
            OtherMethod.warning("要发送的文件不存在！");
            return;
        }
        if(!OtherMethod.checkIP(ipAddress.getText())){
            OtherMethod.warning("目标IP地址格式错误");
            return;
        }
        OtherMethod.inform("格式正确");
    }

    @FXML
    void clear(ActionEvent event) {
        sourcePath.setText("");
        ipAddress.setText("");
    }

    @FXML
    void selectSource(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择要发送的文件夹");
        File file = directoryChooser.showDialog(new Stage());
        if(file!=null){
            sourcePath.setText(file.getPath());
        }
    }

    @FXML
    void selectFileSource(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要发送的文件");
        File file = fileChooser.showOpenDialog(new Stage());
        if(file!=null){
            sourcePath.setText(file.getPath());
        }
    }

    @FXML
    void send(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.titleProperty().set("确认");
        alert.headerTextProperty().set("确认开始发送吗？");
        Optional<ButtonType> result = alert.showAndWait();
        //一直到接收到确认的信号才推出循环
        if (result.get() != ButtonType.OK) {
            return;
        }
        Thread sendThread = new Thread(new Client(ipAddress.getText(),sourcePath.getText()));
        sendThread.start();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert sourcePath != null : "fx:id=\"sourcePath\" was not injected: check your FXML file 'send-gui.fxml'.";
        assert ipAddress != null : "fx:id=\"ipAddress\" was not injected: check your FXML file 'send-gui.fxml'.";

    }
}
