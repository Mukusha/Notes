package com.example.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class HelloController {
    protected static final ObservableList<Note> listNodes = FXCollections.observableArrayList();
    protected static Note noteInsert; //выделенная строка
    protected static int  rowUpdate; //номер выделенной строки
    dataSQLite program = new dataSQLite();
    @FXML
    private TableView<Note> tableView;
    @FXML
    private TableColumn<Note, Boolean> tableIIsImportant;
    @FXML
    private TableColumn<Note, Timestamp> tableData;

    @FXML
    private TableColumn<Note, String> tableThema;

    @FXML
    private Button buttonDeleteNote;

    @FXML
    private Button buttonUpdateNote;

    @FXML
    private Button buttonImportant;
    @FXML
    public void onButtonAddNoteClick() throws IOException { // добавть запись
        //открытие нового окна
      //  tableView.getScene().getWindow().hide(); //что бы первое окно пропадало
        FXMLLoader fxmlLoaderAdd = new FXMLLoader();
        fxmlLoaderAdd.setLocation(getClass().getResource("addNote.fxml"));
        fxmlLoaderAdd.load();
        Parent root=fxmlLoaderAdd.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Создание заметки");
        stage.getIcons().add(new Image(Objects.requireNonNull(NotesApplication.class.getResourceAsStream("add.png"))));
        stage.setScene(new Scene(root));
        stage.showAndWait();

    }

    @FXML
    public void onButtonImportantClick()  { // отметить запись как важную/ снять отметку
try {
    //узнаем номер строки
    int row = tableView.getSelectionModel().getSelectedIndex();

    //изменения в бд
    program.UpdateImportantDB(listNodes.get(row));

    //изменение в таблице
    listNodes.get(row).setIsImportant(!listNodes.get(row).getIsImportant());
    listNodes.set(row,  listNodes.get(row));
} catch (SQLException e) {
   // System.out.println("Не выбрана строка!!!");
    //ничего не делаем, так как не выбрана строка
}

    }

    @FXML
    void onButtonDeleteNoteClick() {
        try {
            //узнаем номер строки
            int row = tableView.getSelectionModel().getSelectedIndex();
            //удаление из бд
            program.DeleteNoteDB(new Note(tableView.getItems().get(row).getDateModification() ,tableView.getItems().get(row).getTextSubject()));

            //удаление из таблицы
            tableView.getItems().remove(row);

        } catch (Exception e) {
          //  System.out.println("Не выбрана строка!!!");
            //ничего не делаем, так как не выбрана строка или ошибка с запросом к базе
        }
    }

    @FXML
    void onButtonUpdateNoteClick() throws IOException {
        rowUpdate = tableView.getSelectionModel().getSelectedIndex();
        noteInsert= new Note(tableView.getItems().get(rowUpdate).getDateModification() ,tableView.getItems().get(rowUpdate).getTextSubject());
        //открыть новое окно
        //  tableView.getScene().getWindow().hide(); //что бы первое окно пропадало
        FXMLLoader fxmlLoaderInsert = new FXMLLoader();
        fxmlLoaderInsert.setLocation(getClass().getResource("insertNote.fxml"));
        fxmlLoaderInsert.load();
        Parent root=fxmlLoaderInsert.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Редактирование заметки");
        stage.getIcons().add(new Image(Objects.requireNonNull(NotesApplication.class.getResourceAsStream("update.png"))));
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    @FXML
     void initialize() throws SQLException {

        // заполняем данные в таблице
        program.open();
        program.CreateDB();
        listNodes.addAll(dataSQLite.FillingTV());
        program.close();

        //привязка переменных класса к столбцам
        tableIIsImportant.setCellValueFactory(new PropertyValueFactory<>("isImportant"));
        tableData.setCellValueFactory(new PropertyValueFactory<>("dateModification"));
        tableThema.setCellValueFactory(new PropertyValueFactory<>("textSubject"));
        tableView.setItems(listNodes);
        //сортировка, что бы новые сверху
       // tableView.getSortOrder().setAll(tableData);

        //обработка клика мыши по таблице
        tableView.setRowFactory( tv -> {
            TableRow<Note> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                //при одинарном щелчке на строку появляется значок удаления и редактирования
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    buttonDeleteNote.setVisible(true);
                    buttonUpdateNote.setVisible(true);
                    buttonImportant.setVisible(true);
                }
                // при двойном щелчке на строку открывается окно редактирования
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {

                    try {
                        onButtonUpdateNoteClick();
                    } catch (IOException e) {
                        //ничего не делаем
                    }

                }
            });
            return row ;
        });
    }

    @FXML
    void buttonAboutTheProgramClick() {
        Label secondLabel = new Label("""
                Создатель:
                   Гуринович Светлана\s
                Последние изменения внесены:
                   6.05.2022
                Для написания использовались:
                   IntelliJ IDEA
                   SceneBuilder
                   SQLite
                   Javafx
                   Maven
                   JDBC""");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 250, 200);

        // Создаем небольшое окно
        Stage newWindow = new Stage();
        newWindow.setTitle("О программе");
        newWindow.getIcons().add(new Image(Objects.requireNonNull(NotesApplication.class.getResourceAsStream("info.png"))));
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    @FXML
    void buttonStaticticClick() throws SQLException {
        //запрос на сбор статистики
        program.open();
        program.CreateDB();
        String statictic = program.StatisticNotesDB();
        program.close();

        //внесение текста на экран
        Label secondLabel = new Label(statictic);

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 250, 200);

        // Создаем небольшое окно
        Stage newWindow = new Stage();
        newWindow.setTitle("Статистика");
        newWindow.getIcons().add(new Image(Objects.requireNonNull(NotesApplication.class.getResourceAsStream("statistic.png"))));
        newWindow.setScene(secondScene);
        newWindow.show();
    }
}