package com.note.Controllers;

import com.note.NotesApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class TabularNoteView {
public Note note;
    private final String viewData;//представление времени
    private ImageView imageImportant;//представление пометки важно
    private boolean isImportant;
    private final String textSubjectView; // тема

    public String getViewData() {
        return viewData;
    }

    public Note getNote() {
        return note;
    }

    public boolean getIsImportant() {
        return  isImportant;
    }

    public String getTextSubjectView() {
        return  textSubjectView;
    }

    TabularNoteView(Note note){
        this.note=note;
        this.textSubjectView = note.getTextSubject();
        this.isImportant=note.getIsImportant();

        //принимаем время и меняем его вид записи
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp = note.getDateModification();
        long l = (currentTimestamp.getTime()-timestamp.getTime())/(24*60*60*1000);
        SimpleDateFormat formatTimestamp;
        if (l<1) { //если менее дня прошло
            formatTimestamp = new SimpleDateFormat("HH:mm");}
        else if(l<365){ //менее года
            formatTimestamp = new SimpleDateFormat("dd.MM");}
        else { //более года
             formatTimestamp = new SimpleDateFormat("dd.MM.yyyy");}
        viewData= formatTimestamp.format(note.getDateModification());

        //принимаем значение пометки важно и если важно, то ставим картинку
        setImageImportant( isImportant);
    }

    public void setIsImportant(boolean b) {
         this.isImportant=b;
    }

    public ImageView getImageImportant() {
        return imageImportant;
    }

    public void setImageImportant(boolean isImportant) {
        Image imageTrue = new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/starGold.png"))));
        Image imageFalse = null;
        ImageView imageImportantnew;
        if (isImportant) imageImportantnew=  new ImageView(imageTrue);
        else imageImportantnew = new ImageView(imageFalse);
        //размер вставляемой картинки
        imageImportantnew.setFitHeight(15);
        imageImportantnew.setFitWidth(15);
        this.imageImportant = imageImportantnew;
    }
}
