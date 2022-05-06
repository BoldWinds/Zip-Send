package Gui;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import MyUtils.OtherMethod;
import ZipAndUnzip.Zip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ZipGUIController {

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
            OtherMethod.inform("您输入的格式正确无误");
        }
    }

    @FXML
    void clear(ActionEvent event) {
        sourcePath.setText("");
        targetPath.setText("");
        name.setText("");
    }

    //压缩
    @FXML
    void compress(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.titleProperty().set("确认");
        alert.headerTextProperty().set("确认开始压缩吗？");
        Optional<ButtonType> result = alert.showAndWait();
        //一直到接收到确认的信号才推出循环
        if (result.get() != ButtonType.OK) {
            return;
        }
        if(check()){
            long size = OtherMethod.countSize(this.sourcePath.getText());
            long time = size/25000;
            OtherMethod.inform("开始压缩,文件大小"+OtherMethod.formatSize(size)+";预计用时:"+OtherMethod.zipTime(size));
            Thread zipThread = new Thread(new Zip(sourcePath.getText(),targetPath.getText(),name.getText()));
            zipThread.start();
        }
    }

    //选择要被压缩的文件s
    @FXML
    void selectSource(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择要压缩的文件夹");
        File file = directoryChooser.showDialog(new Stage());
        if(file!=null){
            sourcePath.setText(file.getPath());
        }
    }

    @FXML
    void selectFileSource(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择要压缩的文件");
        File file = fileChooser.showOpenDialog(new Stage());
        if(file!=null){
            sourcePath.setText(file.getPath());
        }
    }

    @FXML
    void selectTarget(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择存储压缩包的文件夹");
        File file = directoryChooser.showDialog(new Stage());
        if(file!=null){
            targetPath.setText(file.getPath());
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert sourcePath != null : "fx:id=\"sourcePath\" was not injected: check your FXML file 'zip-gui.fxml'.";
        assert targetPath != null : "fx:id=\"targetPath\" was not injected: check your FXML file 'zip-gui.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'zip-gui.fxml'.";

    }

    private boolean check(){
        File source = new File(sourcePath.getText());
        File target = new File(targetPath.getText());
        if(!source.exists()){
            OtherMethod.warning("源文件错误或不存在");
            return false;
        }
        else if (!(target.exists()&&target.isDirectory())){
            OtherMethod.warning("目标文件夹错误或不存在");
            return false;
        }
        else{
            if(!OtherMethod.nameCheck(name.getText())){
                OtherMethod.warning("压缩包名称不合法");
                return false;
            }
        }
        return true;
    }
}
