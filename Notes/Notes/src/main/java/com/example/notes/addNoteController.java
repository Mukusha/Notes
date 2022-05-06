package com.example.notes;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class addNoteController {

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
        HelloController.listNodes.add(note);
        //внести данные в базу данных
        dataSQLite program = new dataSQLite();
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


}
