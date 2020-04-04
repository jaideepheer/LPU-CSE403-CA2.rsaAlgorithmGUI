package RSAGUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logger.Log;
import logger.Logger;

import java.io.IOException;

public class RSAGUI extends Application {

    public static final Logger LOG = new Logger(new Log(), "[RSAGUI] -");
    private static Stage logWindow;

    @FXML
    private Pane logPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/RSAGUI.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("JavaFX and Gradle");
        primaryStage.setScene(scene);
        primaryStage.show();

        LogView lg = new LogView(LOG);
        lg.tailProperty().setValue(false);
        lg.showTimeStampProperty().setValue(true);
        ((TabPane)root).getTabs().add(new Tab("Logging", new BorderPane(lg)));
    }

    @FXML
    private void initialize()
    {
        LOG.debug("test debug");
        LOG.error("test error");
    }
}
