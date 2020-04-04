package RSAGUI;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

public class HoverTooltip extends Tooltip {
    public HoverTooltip() {
        super();
    }
    public HoverTooltip(String text) {
        super(text);
    }

    public void bindTo(Node n)
    {
        n.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            if(!isShowing())
                show(n, event.getScreenX()+5, event.getScreenY()+5);
        });
        n.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            if(!isShowing()) return;
            setX(event.getScreenX()+5);
            setY(event.getScreenY()+5);
        });
        n.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
        {
            if(isShowing()) hide();
        });
    }
}
