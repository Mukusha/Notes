package com.note;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NotesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NotesApplication.class.getResource("scenes/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Заметки!");
        Image image= new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/note.png"))));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
         //System.out.println("Shank you for your visit");
    }

    public static void main(String[] args) {
        launch();
    }
}