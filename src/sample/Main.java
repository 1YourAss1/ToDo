package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.Synchronization;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        new Synchronization().Synchronize(new File("todo.txt"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/mainView.fxml"));
        Parent root = loader.load();
//        root.getStylesheets().addAll("style.css");
        primaryStage.setTitle("ToDo");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}