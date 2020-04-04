module CSE403_CA2_main {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;

    opens RSAGUI to javafx.fxml;
    exports RSAGUI;
}