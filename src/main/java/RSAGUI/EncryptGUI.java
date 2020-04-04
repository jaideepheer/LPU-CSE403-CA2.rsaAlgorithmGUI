package RSAGUI;

import RSA.RSACipher;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static RSAGUI.RSAGUI.LOG;

public class EncryptGUI {
    @FXML
    public SplitMenuButton splitMenuButton;
    @FXML
    public ToggleSwitch toggleSwitch;
    @FXML
    public Button copyButton;
    @FXML
    public MenuItem setEncryptMode;
    @FXML
    public MenuItem setDecryptMode;
    @FXML
    public Label lb_key;
    @FXML
    public Label lb_IP;
    @FXML
    public Label lb_OP;
    @FXML
    public TextArea tx_key;
    @FXML
    public TextArea tx_N;
    @FXML
    public TextArea tx_IP;
    @FXML
    public TextArea tx_OP;

    public boolean isEncryptMode = true;
    public boolean isStringInput = false;
    private final RSACipher cipher = new RSACipher();
    private RSACipher.KeyPair keyPair;

    private static final AutoHideTooltip swapMsg = new AutoHideTooltip("Input/output swapped for easy of use");
    private static final AutoHideTooltip copyMsg = new AutoHideTooltip("Copied to clipboard");
    private static final HoverTooltip doesNothing = new HoverTooltip("[Unimplemented/Useless] This does nothing right now");
    static {
        swapMsg.setShowDuration(Duration.seconds(2));
    }

    @FXML
    private void initialize()
    {
        Supplier<TextFormatter<String>> positiveNumericOrNullOnly = () -> new TextFormatter<String>(
                change -> change.getText().matches("\\d*") ? change : null
        );

        tx_N.setTextFormatter(positiveNumericOrNullOnly.get());
        tx_key.setTextFormatter(positiveNumericOrNullOnly.get());
        tx_IP.setTextFormatter(positiveNumericOrNullOnly.get());

        splitMenuButton.setOnAction(event -> {
            if(isStringInput)
            {
                // TODO: allow string input
            }
            else {
                // disable ip
                tx_IP.setDisable(true);
                tx_key.setDisable(true);
                tx_N.setDisable(true);
                copyButton.setDisable(true);
                splitMenuButton.setDisable(true);
                toggleSwitch.setDisable(true);
                
                CompletableFuture.runAsync(()->{
                    RSACipher.KeyPair fake_kp = RSACipher.KeyPair.makeKeyPair(
                            new BigInteger(tx_key.getText()),
                            new BigInteger(tx_key.getText()),
                            new BigInteger(tx_N.getText())
                    );

                    LOG.debug("Performing encryption/decryption action. (Will disable user input.)");
                    LOG.debug(String.format("Input: %s", tx_IP.getText()));
                    LOG.debug(String.format("Key: %s", tx_key.getText()));
                    LOG.debug(String.format("N: %s", tx_N.getText()));
                    LOG.debug(String.format("fake keypair: %s", fake_kp));

                    String enc = isEncryptMode ? 
                            cipher.encrypt(new BigInteger(tx_IP.getText()), fake_kp)
                            : cipher.decrypt(new BigInteger(tx_IP.getText()), fake_kp);

                    LOG.debug("Encryption/decryption action complete. (Will enable user input.)");
                    LOG.debug(String.format("Output: %s", enc));

                    tx_OP.setText(enc);
                })
                .whenComplete((i,t)->{
                    // enable ip
                    tx_IP.setDisable(false);
                    tx_key.setDisable(false);
                    tx_N.setDisable(false);
                    copyButton.setDisable(false);
                    splitMenuButton.setDisable(false);
                    toggleSwitch.setDisable(false);
                });
            }
        });

        setEncryptMode.setOnAction(event -> {
            isEncryptMode = true;

            LOG.debug("Set to encryption mode. (Will swap input/output text for convenience.)");

            // clear tx
            tx_key.clear();

            // replace tx
            tx_IP.setText(tx_OP.getText());
            tx_OP.clear();
            Point2D p = splitMenuButton.localToScreen(-splitMenuButton.getLayoutBounds().getCenterX()*0.50,-splitMenuButton.getBoundsInLocal().getMaxY()-9);
            swapMsg.show(splitMenuButton, p.getX(),p.getY());

            lb_IP.setText("Plain Text");
            lb_OP.setText("Encrypted CT");
            lb_key.setText("Public Key");
            splitMenuButton.setText("Encrypt");
        });

        setDecryptMode.setOnAction(event -> {
            isEncryptMode = false;

            LOG.debug("Set to decryption mode. (Will swap input/output text for convenience.)");

            // clear tx
            tx_key.clear();

            // replace tx
            tx_IP.setText(tx_OP.getText());
            tx_OP.clear();
            Point2D p = splitMenuButton.localToScreen(-splitMenuButton.getLayoutBounds().getCenterX()*0.50,-splitMenuButton.getBoundsInLocal().getMaxY()-9);
            swapMsg.show(splitMenuButton, p.getX(),p.getY());

            lb_IP.setText("Cipher Text");
            lb_OP.setText("Decrypted PT");
            lb_key.setText("Private Key");
            splitMenuButton.setText("Decrypt");
        });

        copyButton.setOnAction(event -> {
            tx_N.setText(KeypairGUI.keyPair.getN().toString());
            tx_key.setText(
                    (isEncryptMode ? KeypairGUI.keyPair.getPublic_exp() : KeypairGUI.keyPair.getPrivate_exp())
                    .toString()
            );
            LOG.debug("Copied key and N value from latest generated keypair.");
        });

        tx_OP.setOnMouseClicked(event -> {
            KeypairGUI.copyToClipboard(tx_OP.getText());
            copyMsg.show(tx_OP, event.getScreenX()+12, event.getScreenY()+6);
        });

        doesNothing.bindTo(toggleSwitch);
    }
}
