package de.kawt.impl.kjava;

import de.kawt.impl.*;
import java.awt.*;
import java.awt.event.*;


public class LafImpl extends Laf {

    static boolean monochrome;

    public LafImpl () {
	scrollArrowSize = 6;
	buttonRadius = 4;
    }


    public void drawSymbol (Graphics g, Color bg, int x, int y, 
			    int type, boolean selected) {

	Color fg = g.getColor ();

	switch (type) {
	case CHECKBOX: 
	    int middle = y+4;
	    g.drawRect (x, middle-3, 7, 7);

	    if (selected) {
		g.setColor (bg);
		g.drawLine (x+7, middle-3, x+6, middle-3);
		g.drawLine (x+7, middle-3, x+7, middle);

		g.setColor (fg);
		g.drawLine (x+4, middle+2, x+9, middle-3);
		g.drawLine (x+4, middle+1, x+9, middle-4);
		g.drawLine (x+3, middle+1, x+8, middle-4);
		g.fillRect (x+2, middle-1, 2, 2);
	    }
	    else {
		g.setColor (bg);
		g.fillRect (x+1, middle-2, 6, 6);
		g.fillRect (x+8, y, 2, 6);
		g.setColor (fg);
	    }
	    break;

	case RADIOBOX:
	    
	    g.drawRoundRect (x, y, 9, 9, 4, 4);
	    if (!selected) {
		g.setColor (bg);
		g.fillRect (x+3, y+3, 4, 4);
		g.setColor (fg);
	    }
	    else
		g.fillRect (x+3, y+3, 4, 4);

	    break;

	default: 
	    super.drawSymbol (g, bg, x, y, type, selected);
	}
    }


    public void drawScrollbar (Graphics g, Color bg, int w, int h, 
			       int orientation, int scrPos, int scrBarSize,
			       int scrollType, boolean focus) {
	
	Color fg = g.getColor ();

    	if (orientation != Scrollbar.HORIZONTAL) { // VERT
	    
	    int middle = w / 2;

	    Laf.laf.drawSymbol (g, bg, middle-3, 0, Laf.ARROW_UP,
			       scrollType == AdjustmentEvent.UNIT_DECREMENT);

	    Laf.laf.drawSymbol (g, bg, middle-3, h-6, Laf.ARROW_DOWN,
			       scrollType == AdjustmentEvent.UNIT_INCREMENT);
		
	    g.setColor(Color.gray);

	    if (monochrome) {
		g.drawLine(middle-1, 6, middle-1, h-7);
		g.drawLine(middle, 7, middle, h-8);
		g.drawLine(middle+1, 6, middle+1, h-7);
	    }
	    else
		g.fillRect (middle-1, 6, 3, h-12);
	    
	    g.setColor (fg);
	    g.fillRect (middle-1, scrPos, 3, scrBarSize);

	    g.setColor (bg);

	    g.drawLine (middle-1, scrPos-1, middle+1, scrPos-1); 
	    g.drawLine (middle-1, scrPos + scrBarSize, 
			middle+1, scrPos + scrBarSize); 
    	} 
	else {
	    int middle = h / 2;

	    Laf.laf.drawSymbol (g, bg, 0, middle-3, Laf.ARROW_LEFT, 
			       scrollType == AdjustmentEvent.UNIT_DECREMENT);

	    Laf.laf.drawSymbol (g, bg, w-6, middle-3, Laf.ARROW_RIGHT,
			       scrollType == AdjustmentEvent.UNIT_INCREMENT);

	    g.setColor(Color.gray);
          	
	    if (monochrome) {
		g.drawLine(6, middle-1, w-7, middle-1);
		g.drawLine(6, middle, w-7, middle);
		g.drawLine(6, middle+1, w-7, middle+1);
		g.setXORMode(Color.black);
		g.drawLine(6, middle, w-7, middle);
		g.setPaintMode();
	    }
	    else
		g.fillRect (6, middle-1, w-12, 3);
	    
	    g.setColor(fg);
	    g.fillRect (scrPos, middle-1, scrBarSize, 3);
	    
	    g.setColor (bg);
	    g.drawLine (scrPos, middle-1, scrPos, middle+1);
	    g.drawLine (scrPos+scrBarSize, middle -1, 
			scrPos+scrBarSize, middle+1);
	}
    }



    public int getSymbolWidth (int type) {
	return (type == CHECKBOX || type == RADIOBOX) ? 10 : 7;
    }


    public int getSymbolHeight (int type) {
	return (type == CHECKBOX || type == RADIOBOX) ? 10 : 7;
    }



    public void drawWindow (Graphics g, int w, int h,
			    int type, String title, boolean focus) {

	if (type == WINDOW_FRAME) {
	
	    if (title != null) {
		int th = 12;
		Color fg = g.getColor ();
		Font f = g.getFont ();
		g.setFont (f.deriveFont (Font.BOLD));
		int tw = g.getFontMetrics ().stringWidth (title);
		
		g.setColor (Color.white);
		g.fillRect (tw + 3, 0, w, th + 1);
		g.setColor (SystemColor.activeCaption);
		g.fillRoundRect (0, 0, tw + 5, th+2, 3, 3);
		g.drawLine (0, th+1, w, th+1);
		g.drawLine (0, th+2, w, th+2);
		g.setColor (Color.white);
		drawFocusString (g, SystemColor.activeCaption, 3, 2, title, focus);
		g.setColor (fg);
		g.setFont (f);
	    }
        }
    }

    
    
    public Insets getWindowInsets (int type) {
        if (type == WINDOW_DIALOG) 
            return new Insets (12, 3, 3, 3);
        else
            return new Insets (16, 0, 0, 0);
    }

    public Dimension getMinimumScrollbarSize (int orientation) {
        return orientation == Scrollbar.VERTICAL
            ? new Dimension (7, 3*scrollArrowSize)
                : new Dimension (3*scrollArrowSize, 7);
    }

}
