package java.awt.event;

import java.awt.*;

public class FocusEvent extends AWTEvent {
    
    public static final int FOCUS_GAINED = 1004;
    public static final int FOCUS_LOST = 1005;

    public FocusEvent (Component source, int id) {
	super (source, id);
    }
}
