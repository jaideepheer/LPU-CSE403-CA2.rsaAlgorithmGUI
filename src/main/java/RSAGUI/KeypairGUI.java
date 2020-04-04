package RSAGUI;

import RSA.RSACipher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeypairGUI {

    public static final Stage stage;
    static {
        Parent root;
        try {
            root = FXMLLoader.load(KeypairGUI.class.getResource("/keypairGUI.fxml"));
        } catch (IOException e) {
            root = new AnchorPane(new ScrollPane(new Label(e.toString())));
        }
        stage = new Stage();
        stage.setTitle("Generate Keypair");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public TextArea kp_pr;
    @FXML
    public TextArea kp_pu;
    @FXML
    public TextArea kp_N;
    @FXML
    public Spinner<Integer> kp_bitlength;
    @FXML
    public Button kp_genbtn;

    static RSACipher.KeyPair keyPair;
    static AtomicBoolean keygenLock = new AtomicBoolean(false);

    private static final AutoHideTooltip copyMsg = new AutoHideTooltip("Copied to clipboard");
    public static final HoverTooltip bitsizeMsg = new HoverTooltip("This specifies the number of bits used while generating random primes.");

    static void copyToClipboard(String text)
    {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(
                content
        );
    }

    @FXML
    private void initialize()
    {
        SpinnerValueFactory<Integer> f = new SpinnerValueFactory.IntegerSpinnerValueFactory(8,4096,512,1);
        kp_bitlength.setValueFactory(f);
        bitsizeMsg.bindTo(kp_bitlength);

        Runnable genKeypair = ()->{
            kp_pu.setDisable(true);
            kp_genbtn.setDisable(true);
            kp_pr.setDisable(true);
            kp_bitlength.setDisable(true);
            kp_N.setDisable(true);
            CompletableFuture.runAsync(()->
            {
                keyPair = RSACipher.KeyPair.generateKeyPair(kp_bitlength.getValue());

                kp_pu.setText(keyPair.getPublic_exp().toString());
                kp_pr.setText(keyPair.getPrivate_exp().toString());
                kp_N.setText(keyPair.getN().toString());

                kp_pu.setDisable(false);
                kp_genbtn.setDisable(false);
                kp_pr.setDisable(false);
                kp_bitlength.setDisable(false);
                kp_N.setDisable(false);
                RSAGUI.LOG.info("Done");
            });
        };

        kp_genbtn.setOnAction((event -> genKeypair.run()));
        if(keygenLock.compareAndSet(false, true)) genKeypair.run();

        kp_N.setOnMouseClicked((event ->
        {
            kp_N.requestFocus();
            copyToClipboard(kp_N.getText());
            copyMsg.show(kp_N, event.getScreenX()+12, event.getScreenY()+6);
        }));
        kp_pr.setOnMouseClicked((event ->
        {
            kp_pr.requestFocus();
            copyToClipboard(kp_pr.getText());
            copyMsg.show(kp_pr, event.getScreenX()+12, event.getScreenY()+6);
        }));
        kp_pu.setOnMouseClicked((event ->
        {
            kp_pu.requestFocus();
            copyToClipboard(kp_pu.getText());
            copyMsg.show(kp_pu, event.getScreenX()+12, event.getScreenY()+6);
        }));
    }
}
