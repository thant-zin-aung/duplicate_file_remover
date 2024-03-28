module com.example.duplicate_files_remover {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.duplicate_files_remover to javafx.fxml;
    exports com.example.duplicate_files_remover;
}