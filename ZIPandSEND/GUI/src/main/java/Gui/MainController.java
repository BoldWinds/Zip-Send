package Gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Zip"
    private Button Zip; // Value injected by FXMLLoader

    @FXML // fx:id="Unzip"
    private Button Unzip; // Value injected by FXMLLoader

    @FXML // fx:id="Send"
    private Button Send; // Value injected by FXMLLoader

    @FXML // fx:id="Receive"
    private Button Receive; // Value injected by FXMLLoader

    @FXML
    void TryToReceive(ActionEvent event) {
        Stage ReceiveStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("receive-gui.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),600,430);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("fxml文件加载异常");
        }
        ReceiveStage.setScene(scene);
        ReceiveStage.show();
    }

    @FXML
    void TryToSend(ActionEvent event) {
        Stage SendStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("send-gui.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),600,430);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("fxml文件加载异常");
        }
        SendStage.setScene(scene);
        SendStage.show();
    }

    @FXML
    void TryToUnzip(ActionEvent event) {
        Stage UnzipStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("unzip-gui.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),600,430);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("fxml文件加载异常");
        }
        UnzipStage.setScene(scene);
        UnzipStage.show();
    }

    @FXML
    void TryToZip(ActionEvent event) {
        Stage ZipStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("zip-gui.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),600,430);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("fxml文件加载异常");
        }
        ZipStage.setScene(scene);
        ZipStage.show();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert Zip != null : "fx:id=\"Zip\" was not injected: check your FXML file 'main-gui.fxml'.";
        assert Unzip != null : "fx:id=\"Unzip\" was not injected: check your FXML file 'main-gui.fxml'.";
        assert Send != null : "fx:id=\"Send\" was not injected: check your FXML file 'main-gui.fxml'.";
        assert Receive != null : "fx:id=\"Receive\" was not injected: check your FXML file 'main-gui.fxml'.";

    }
}
