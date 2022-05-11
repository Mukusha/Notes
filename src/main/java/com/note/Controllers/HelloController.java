package com.note.Controllers;

import com.note.NotesApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class HelloController {
    protected static final ObservableList<TabularNoteView> listNodes = FXCollections.observableArrayList();
    protected static Note noteInsert; //выделенная строка
    protected static int  rowUpdate; //номер выделенной строки
    public Button buttonAddNote;
    requestsSQLite program = new requestsSQLite();
    @FXML
    private TableView<TabularNoteView> tableView;
    @FXML
    private TableColumn<ImageView, Object> tableIIsImportant;
    @FXML
    private TableColumn<String, Object> tableData;

    @FXML
    private TableColumn<String, Object> tableThema;

    @FXML
    private Button buttonDeleteNote;

    @FXML
    private Button buttonUpdateNote;

    @FXML
    private Button buttonImportant;
    @FXML
    public void onButtonAddNoteClick() throws IOException { // добавить запись
        //открытие нового окна
        //  tableView.getScene().getWindow().hide(); //что бы первое окно пропадало
        FXMLLoader fxmlLoaderAdd = new FXMLLoader(NotesApplication.class.getResource("scenes/addNote-view.fxml"));
        fxmlLoaderAdd.load();
        Parent root=fxmlLoaderAdd.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Создание заметки");
        Image image= new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/add.png"))));
        stage.getIcons().add(image);
        stage.setScene(new Scene(root));
        stage.showAndWait();

    }

    @FXML
    public void onButtonImportantClick()  { // отметить запись как важную/ снять отметку
        try {
            //узнаем номер строки
            int row = tableView.getSelectionModel().getSelectedIndex();

            //изменения в бд
            program.UpdateImportantDB(listNodes.get(row).getNote());

            //изменение в таблице
            listNodes.get(row).setIsImportant(!listNodes.get(row).getIsImportant());
            listNodes.get(row).setImageImportant(listNodes.get(row).getIsImportant());
            listNodes.set(row,  listNodes.get(row));
           // new tabularNoteView(listNodes.get(row));
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
            program.DeleteNoteDB(new Note(tableView.getItems().get(row).getNote().getDateModification() ,tableView.getItems().get(row).getNote().getTextSubject()));

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
        noteInsert=tableView.getItems().get(rowUpdate).getNote(); // new Note(tableView.getItems().get(rowUpdate).getNote().getDateModification() ,tableView.getItems().get(rowUpdate).getNote().getTextSubject());
        //открыть новое окно
        //  tableView.getScene().getWindow().hide(); //что бы первое окно пропадало
        FXMLLoader fxmlLoaderUpdate = new FXMLLoader(NotesApplication.class.getResource("scenes/updateNote-view.fxml"));
        fxmlLoaderUpdate.load();
        Parent root=fxmlLoaderUpdate.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Редактирование заметки");
        Image image= new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/update.png"))));
        stage.getIcons().add(image);
        stage.setScene(new Scene(root));
        stage.showAndWait();

    }

    @FXML
    void initialize() throws SQLException {

        // заполняем данные в таблице
        program.open();
        program.CreateDB();
        listNodes.addAll(requestsSQLite.FillingTV());
        program.close();

        //привязка переменных класса к столбцам
        tableIIsImportant.setCellValueFactory(new PropertyValueFactory<>("imageImportant"));
        tableData.setCellValueFactory(new PropertyValueFactory<>("viewData"));
        tableData.setStyle("-fx-alignment: CENTER;");
        tableThema.setCellValueFactory(new PropertyValueFactory<>("textSubjectView"));
        tableView.setItems(listNodes);
        //сортировка, что бы новые сверху
        // tableView.getSortOrder().setAll(tableData);

        //обработка клика мыши по таблице
        tableView.setRowFactory( tv -> {
            TableRow<TabularNoteView> row = new TableRow<>();
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
                Стек технологий:
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
        Image image = new Image((Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/info.png"))));
        newWindow.getIcons().add(image);
        newWindow.setScene(secondScene);
        newWindow.show();
    }

    @FXML
    void buttonStatisticClick() throws SQLException {
        //запрос на сбор статистики
        program.open();
        program.CreateDB();

        String  statistic = program.StatisticNotesDB();

        program.close();

        //внесение текста на экран
        Label secondLabel = new Label(statistic);

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene newScene = new Scene(secondaryLayout, 250, 200);

        // Создаем небольшое окно
        Stage newWindow = new Stage();
        newWindow.setTitle("Статистика");
        Image image = new Image( Objects.requireNonNull(NotesApplication.class.getResourceAsStream("picture/statistic.png")));
        newWindow.getIcons().add(image);
        newWindow.setScene(newScene);
        newWindow.show();
    }
}