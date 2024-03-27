module org.example.huffmanencodinggui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.huffmanencodinggui to javafx.fxml;
    exports org.example.huffmanencodinggui;
}