
package Gui;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import MyUtils.OtherMethod;
import ZipAndUnzip.Unzip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UnzipGUIController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="sourcePath"
    private TextField sourcePath; // Value injected by FXMLLoader

    @FXML // fx:id="targetPath"
    private TextField targetPath; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private TextField name; // Value injected by FXMLLoader

    @FXML
    void check(ActionEvent event) {
        if(check()){
            OtherMethod.inform("格式正确");
        }
    }

    @FXML
    void clear(ActionEvent event) {
        sourcePath.setText("");
        targetPath.setText("");
        name.setText("");
    }

    @FXML
    void uncompress(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.titleProperty().set("确认");
        alert.headerTextProperty().set("确认开始解压缩吗？");
        Optional<ButtonType> result = alert.showAndWait();
        //一直到接收到确认的信号才推出循环
        if (result.get() != ButtonType.OK) {
            return;
        }
        if(check()){
            long size = OtherMethod.countSize(this.sourcePath.getText());
            long time = size/90000;
            OtherMethod.inform("开始解压缩,文件大小"+OtherMethod.formatSize(size)+";预计用时:"+OtherMethod.unzipTime(size));
            Thread unzipThread = new Thread(new Unzip(sourcePath.getText(),targetPath.getText(),name.getText()));
            unzipThread.start();
        }
    }

    @FXML
    void selectSource(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择zip压缩包");
        File file = fileChooser.showOpenDialog(new Stage());
        if(file!=null){
            sourcePath.setText(file.getPath());
        }
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
        assert sourcePath != null : "fx:id=\"sourcePath\" was not injected: check your FXML file 'unzip-gui.fxml'.";
        assert targetPath != null : "fx:id=\"targetPath\" was not injected: check your FXML file 'unzip-gui.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'unzip-gui.fxml'.";

    }

    private boolean check(){
        File file1 = new File(sourcePath.getText());
        File file2 = new File(targetPath.getText());
        if(!(file1.exists()&&file1.isFile())){
            OtherMethod.warning("源文件不存在");
            return false;
        }
        if(!(file2.exists()&&file2.isDirectory())){
            OtherMethod.warning("目标文件夹不存在");
            return false;
        }
        if(!file1.getPath().contains(".zip")){
            OtherMethod.warning("源文件不是zip类型");
            return false;
        }
        return true;
    }
}
