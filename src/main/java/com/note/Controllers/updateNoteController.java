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

import static com.note.Controllers.HelloController.noteInsert;
import static com.note.Controllers.HelloController.rowUpdate;
import static com.note.Controllers.requestsSQLite.FindNoteDB;

public class updateNoteController {
    public ImageView imageIsImportant;
    private Timestamp timestampNodeCreate;
    @FXML
    private ToggleButton isImportant;

    @FXML
    private TextArea newText;

    @FXML
    private TextField newThema;

    @FXML
    private Label timeNodeModification;

    @FXML
    private Label timeNodeCreate;
    @FXML
    void onButtonIsInsertClick() {
        newText.setEditable(true);
        newThema.setEditable(true);

    }

    @FXML
    void onButtonNotSaveInsertClick() {
        //просто закрыть окно
        //закрыть окно
        Stage stage = (Stage) newThema.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onButtonSaveInsertClick() throws SQLException {

        Note note= new Note(noteInsert.getDateModification(),noteInsert.getTextSubject());

        Note noteNew=new Note(new Timestamp(System.currentTimeMillis()), //время изменения
                newThema.getText(), // тема
                newText.getText(), // текст
                isImportant.isSelected(),//,  //важность заметки
                timestampNodeCreate); // дата создания заметки

        //внести изменения в базу данных
        requestsSQLite program = new requestsSQLite();
        program.UpdateDB(note, noteNew);

        //внести изменения в таблице
        HelloController.listNodes.set(rowUpdate,new TabularNoteView(noteNew) );

        //закрыть окно
        Stage stage = (Stage) newThema.getScene().getWindow();
        stage.close();
    }


    @FXML
    void initialize() throws SQLException {
        //при открытии заполнение полей

        //считать из базы данных
        requestsSQLite program = new requestsSQLite();
        program.open(); //открыть базу
        program.CreateDB(); //открыть таблицу
        Note note = FindNoteDB(new Note(noteInsert.getDateModification(),noteInsert.getTextSubject())); //найти заметку которую нужно изменить
        program.close(); //закрыть

        //заполнить считанными данными окно
        newThema.setText(note.getTextSubject());
        newText.setText(note.getText());
        isImportant.setSelected(note.getIsImportant());
        onButtonImportantClick();
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String dataTime = formater.format(note.getDateModification());
        timeNodeModification.setText( dataTime);
        dataTime = formater.format(note.getDateCreate());
        timeNodeCreate.setText("Дата создания: "+dataTime);

        //запомнить дату создания
        timestampNodeCreate=note.getDateCreate();

        // при двойном щелчке на текст - начать редактирование
        newText.setOnMouseClicked(event ->{
            if (event.getClickCount() == 2)  {
                newText.setEditable(true);
                newThema.setEditable(true);
            }});

        newThema.setOnMouseClicked(event ->{
            if (event.getClickCount() == 2)  {
                newText.setEditable(true);
                newThema.setEditable(true);
            }});

    }

    public void onButtonImportantClick() {
        Image imageTrue = new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/starGold.png"))));
        Image imageFalse = new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/star.png"))));

        if (isImportant.isSelected()) imageIsImportant.setImage(imageTrue);
        if (!isImportant.isSelected()) imageIsImportant.setImage(imageFalse);
    }
}
