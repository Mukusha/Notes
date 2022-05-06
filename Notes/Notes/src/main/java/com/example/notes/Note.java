package com.example.notes;

import java.sql.Timestamp;

    public class Note {
        private final Timestamp dateModification; // дата последнего изменения
        private boolean isImportant;  // важная ли заметка
        private final String textSubject; // тема
        private String text; // текст заметки
        private Timestamp dateCreate; // дата создания

        public void setIsImportant(boolean important) {
           this.isImportant = important;
        }

        Note(Timestamp dateModification,String textSubject, String text, Boolean isImportant, Timestamp dateCreate ){
            this.dateCreate= dateCreate;
            this.isImportant=isImportant;
            this.textSubject=textSubject;
            this.text=text;
            this.dateModification =dateModification;

        }

        Note(String textSubject, String text,boolean isImportant ){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //текущая дата
            this.dateCreate= timestamp;
            this.isImportant=isImportant;
            this.textSubject=textSubject;
            this.text=text;
            this.dateModification = timestamp;

        }

        Note(Timestamp dateModification,String textSubject, boolean isImportant ){
            this.isImportant=isImportant;
            this.textSubject=textSubject;
            this.dateModification =dateModification;
        }
        Note(Timestamp dateModification,String textSubject ){
            this.textSubject=textSubject;
            this.dateModification =dateModification;
        }



        public Boolean getIsImportant() {
            return this.isImportant;
        }

        public String getText() {
            return text;
        }

        public Timestamp getDateCreate() {
            return dateCreate;
        }

        public Timestamp getDateModification() {
           return this.dateModification;
        }

        public String getTextSubject() {
          return this.textSubject;
        }


        // Выводим информацию по записи
        public void allPrint() {
            System.out.printf(" Data modificaton: %s | Text Subject: %s  | Is important: %s\n %s \n Data create: %s%n",
                    this.dateModification, this.textSubject, this.isImportant, this.text, this.dateCreate);
        }



}
