package com.note.Controllers;

import com.note.NotesApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class addNoteController {
    public ImageView imageIsImportant;
    @FXML
    private Label timeNodeCreate;
    @FXML
    private ToggleButton isImportant;

    @FXML
    private TextArea newText;

    @FXML
    private TextField newThema;

    @FXML
    void onButtonAddNoteClick() throws SQLException {
        // считать данные из окна
        Note note = new Note(newThema.getText(),newText.getText(),isImportant.isSelected());
        HelloController.listNodes.add(new TabularNoteView(note) );
        //внести данные в базу данных
        requestsSQLite program = new requestsSQLite();
        program.WriteDB(note); //добавить запись

        //закрыть окно
        Stage stage = (Stage) newThema.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize(){
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        timeNodeCreate.setText(formater.format( new Timestamp(System.currentTimeMillis()) )); //текущая дата
    }

    public void onButtonImportantClick() {
        Image imageTrue = new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/starGold.png"))));
        Image imageFalse = new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/star.png"))));

        if (isImportant.isSelected()) imageIsImportant.setImage(imageTrue);
        if (!isImportant.isSelected()) imageIsImportant.setImage(imageFalse);
    }
}
