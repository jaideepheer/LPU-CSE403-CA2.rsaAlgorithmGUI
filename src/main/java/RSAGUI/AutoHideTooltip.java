package RSAGUI;

import javafx.animation.PauseTransition;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class AutoHideTooltip extends Tooltip {
    public final PauseTransition hideAnimation = new PauseTransition();
    private void setDefaults()
    {
        setAutoHide(true);
        setAutoFix(true);
        setHideOnEscape(true);

        setShowDuration(Duration.seconds(1.0));
        hideAnimation.durationProperty().bind(showDurationProperty());
        hideAnimation.setOnFinished(e -> hide());

        showingProperty().addListener((isShowing, oldValue, newValue)->{
            if(!oldValue && newValue)
            {
                // just started showing
                hideAnimation.playFromStart();
            }
        });

    }

    public AutoHideTooltip() {
        super();
        setDefaults();
    }
    public AutoHideTooltip(String text) {
        super(text);
        setDefaults();
    }

}
