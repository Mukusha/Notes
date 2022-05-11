module com.note {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.note to javafx.fxml;
    exports com.note;
    exports com.note.Controllers;
    opens com.note.Controllers to javafx.fxml;
}