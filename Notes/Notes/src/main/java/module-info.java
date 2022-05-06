module com.example.notes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.notes to javafx.fxml;
    exports com.example.notes;
}