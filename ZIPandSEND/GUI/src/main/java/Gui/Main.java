package Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application{
    @Override
    public void start(Stage stage){
        //主页面
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-gui.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),860,620);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fxml文件加载错误");
        }
        stage.setTitle("Zip&Send");
        stage.setScene(scene);
        //关闭主页面时进程也要结束。避免用户关闭页面但是仍有线程在继续运行
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}