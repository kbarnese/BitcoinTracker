package com.mycompany.barnesejava2finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primaryAsync"), 640, 480);
         //URL url = this.getClass().getResource("controlStyle1.css");
    //String css = url.toExternalForm();
        //scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
    

    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        
        
        
      
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}