package com.example.notes;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;

public class dataSQLite {
    private static Connection co;
    private static Statement statmt;
    private static ResultSet resSet;
    private static final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //открыть заметки
    protected void open(){
        try
        {
            //Подключение к базе данных
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection(
                    "jdbc:sqlite:notes.db");
           // System.out.println("Connected");
        }
        catch (Exception e)
        {
            System.out.println (e.getMessage());
        }
    }

    //закрыть заметки
    protected void close()
    {
        try
        {
            co.close();
        }
        catch (Exception e)
        {
            System.out.println (e.getMessage());
        }
    }

    // --------Создание таблицы--------
    protected void CreateDB() throws  SQLException
    {
        statmt = co.createStatement();
        statmt.execute("CREATE TABLE if not exists 'notes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'dateModification' datetime,'isImportant' BOOLEAN, 'textSubject' text, 'text' text, 'dateCreate' datetime);");
    }

    // --------Создание заметки--------
   protected void WriteDB(Note note) throws SQLException
    {
        open();
        CreateDB();

        String insertSQL = "INSERT INTO 'notes' ( 'dateModification' ,'isImportant' , 'textSubject' , 'text' , 'dateCreate' )"+
                " VALUES ( " +
                "'"+formater.format(note.getDateModification())+"',"+
                "'"+ note.getIsImportant()+ "'," +
                "'"+note.getTextSubject()+ "'," +
                "'"+note.getText()+ "'," +
                "'"+formater.format(note.getDateCreate())+"');";
        statmt.execute(insertSQL);

        close();
    }

    // --------Удаление заметки--------
   protected void DeleteNoteDB(Note note) throws SQLException
    {
        open();
        CreateDB();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        statmt.execute("DELETE FROM notes "+
                             "WHERE dateModification ='"+formater.format(note.getDateModification()) +
                             "' AND textSubject = '"+note.getTextSubject()+"';" );
        close();
    }

    // -------- Вывод всей таблицы (для проверки) --------
    protected void ReadDB() throws  SQLException
    {
        open();
        CreateDB();
        resSet = statmt.executeQuery("SELECT * FROM notes");
        while(resSet.next())
        {
            int id = resSet.getInt("id");
            Timestamp dateModification = resSet.getTimestamp("dateModification") ;
            boolean isImportant = Boolean.parseBoolean(resSet.getString("isImportant"));
            String  textSubject =resSet.getString("textSubject");
            String  text =resSet.getString("text");
            System.out.println( "ID = " + id );
            System.out.println( "dateModification = " + dateModification );
            System.out.println( "isImportant = " + isImportant );
            System.out.println( "textSubject = " + textSubject );
            System.out.println( "text = " + text );
            System.out.println( "dateModification = " + resSet.getTimestamp("dateCreate")  );
            System.out.println();
        }

        close();
    }

    // -------- Заполнение таблицы tableView--------
  protected static ObservableList<Note> FillingTV() throws  SQLException
    {

        ObservableList<Note> listNodes = FXCollections.observableArrayList();
        resSet = statmt.executeQuery("SELECT dateModification,textSubject,isImportant  FROM notes");
        while(resSet.next())
        {
            listNodes.add(new Note(
                    resSet.getTimestamp("dateModification"),
                    resSet.getString("textSubject"),
                    Boolean.parseBoolean(resSet.getString("isImportant"))
            ));

        }

        return listNodes;
    }

     //--------Поиск заметки--------
    protected static Note FindNoteDB(Note note) throws SQLException {
        String selectSql = "SELECT *  FROM notes "+
                "WHERE dateModification ='"+formater.format(note.getDateModification()) +"' AND textSubject = '"+note.getTextSubject()+"';";
      resSet = statmt.executeQuery(selectSql);
       while (resSet.next()) {
           note = new Note(
                   resSet.getTimestamp("dateModification"),
                   resSet.getString("textSubject"),
                   resSet.getString("text"),
                   Boolean.valueOf(resSet.getString("isImportant")),
                   resSet.getTimestamp("dateCreate") );
       }
    //    Timestamp dateModification,String textSubject, String text, Boolean isImportant, Timestamp dateCreate
        return note;
    }


    // -------- Изменение записи--------
   protected void  UpdateDB(Note note, Note noteNew) throws  SQLException
    {
        open();
        CreateDB();

        String updateSql = "UPDATE notes "+
                           "SET dateModification ='"+formater.format(noteNew.getDateModification()) +
                             "', isImportant= '"+noteNew.getIsImportant()+
                             "', textSubject = '"+noteNew.getTextSubject()+
                             "', text = '"+noteNew.getText()+
                           "' WHERE dateModification ='"+formater.format(note.getDateModification()) +
                                "' AND textSubject = '"+note.getTextSubject()+"';";
         statmt.execute(updateSql);
         close();
    }

    // --------Изменение важности заметки--------
   protected void UpdateImportantDB(Note note) throws  SQLException
    {
        open();
        CreateDB();

        String updateSql = "UPDATE notes "+
                "SET  isImportant= '"+!note.getIsImportant()+
                "' WHERE dateModification ='"+formater.format(note.getDateModification()) +
                "' AND textSubject = '"+note.getTextSubject()+"';";
        statmt.execute(updateSql);
        close();
      }

    // --------Сбор статистики--------
    protected String StatisticNotesDB() throws SQLException
    {

        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        String countSql = "SELECT COUNT(*) FROM notes";
        resSet = statmt.executeQuery(countSql);
        resSet.next();
        String allNotes = "Всего заметок: "+resSet.getInt(1);

        countSql = "SELECT dateCreate, textSubject FROM notes WHERE dateCreate = (SELECT min(dateCreate) FROM notes) ";
        resSet = statmt.executeQuery(countSql);
        resSet.next();
        String firstNoteCreated  = "\nПервая заметка:\n   "
                +formater.format(resSet.getTimestamp("dateCreate"))+"    "
                +resSet.getString("textSubject");

        countSql = "SELECT dateModification, textSubject FROM notes WHERE dateModification = (SELECT max(dateModification) FROM notes) ";
        resSet = statmt.executeQuery(countSql);
        resSet.next();
        String lastNoteCreated   = "\nПоследняя заметка:\n   "
                +formater.format(resSet.getTimestamp("dateModification"))+"    "
                +resSet.getString("textSubject");

        countSql = "SELECT COUNT(*) FROM notes WHERE isImportant='true'";
        resSet = statmt.executeQuery(countSql);
        resSet.next();
        String numberOfImportant = "\nКоличество важных заметок: "+resSet.getInt(1);

        return allNotes+"\n"+firstNoteCreated+"\n"+lastNoteCreated+"\n"+numberOfImportant;
    }
}
