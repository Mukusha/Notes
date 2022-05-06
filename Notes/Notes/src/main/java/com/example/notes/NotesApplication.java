package com.example.notes;

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
        FXMLLoader fxmlLoader = new FXMLLoader(NotesApplication.class.getResource("notes-basic.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // размер сцены
        stage.setTitle("Заметки"); //название окна
        stage.getIcons().add(new Image(Objects.requireNonNull(NotesApplication.class.getResourceAsStream("note.png"))));
        stage.setScene(scene); // установка сцены
        stage.show();


    }
    @Override
    public void stop(){
       // System.out.println("Shank you for your visit");
    }

    public static void main(String[] args) {
        launch();
    }
}