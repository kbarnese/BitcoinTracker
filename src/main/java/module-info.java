module com.mycompany.barnesejava2finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.mycompany.barnesejava2finalproject to javafx.fxml;
    exports com.mycompany.barnesejava2finalproject;
}
