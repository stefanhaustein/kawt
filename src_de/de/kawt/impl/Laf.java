package de.kawt.impl;

import java.awt.*;
import java.util.*;

/** A simple and limited look-and-feel encapsulation class for kAWT.
    If we will add some kind of "Window Policy", we will add it here.
    Frames will allways be full screen, but positioning of Dialogs
    should be device-dependent... This class also contains some helper
    code for the system dependent part of the implementation like
    a method for determining the top window and methods for determining
    the event source of keyboard and mouse events.*/

public class Laf {

    public static Laf laf;

    public static final int ARROW_UP = 1;
    public static final int ARROW_DOWN = 2;
    public static final int ARROW_LEFT = 3;
    public static final int ARROW_RIGHT = 4;

    public static final int CHOICE = 5;
    public static final int CHECKBOX = 6;
    public static final int RADIOBOX = 7;

    public static final int BORDER_NONE = 0;
    public static final int BORDER_SINGLE = 1;
    public static final int BORDER_DOUBLE = 2;
    public static final int BORDER_SHADOW = 3;
    public static final int BORDER_FOCUSABLE = 4;
    public static final int BORDER_FOCUSSED = 5;

    public static final int WINDOW_DIALOG = 0;
    public static final int WINDOW_FRAME = 1;


    //    public static final int SCREEN_EVENT = AWTEvent.RESERVED_ID_MAX - 1;


    public static Vector windows = new Vector ();

    public static Window getTopWindow () {
        int sz = windows.size ();
        return sz == 0 ? null : (Window) windows.elementAt (sz-1);
    }


    /** Returns Mouse Event source based on given x and y coordinates.
	The Point parameter is */


    public Component getMouseEventSource (int x, int y, Point org) {

	Component mouseOwner = null;     
	
	while (true) {
	    Window top = getTopWindow ();
	    if (top == null) break;

	    mouseOwner = top;

	    do {
		x -= mouseOwner.getX ();
		y -= mouseOwner.getY ();

		Component c2 = mouseOwner.getComponentAt (x, y);
		if (mouseOwner == c2) break;
		mouseOwner = c2;
	    }
	    while (mouseOwner != null);


	    if (mouseOwner != null) break;
	    
	    if (!(top instanceof Frame) && !(top instanceof Dialog)) {
		top.setVisible (false);
	    }
	    else {
		mouseOwner = top;
		break; 
	    }
	}
	
	if (mouseOwner != null) {
	    Point p = mouseOwner.getLocationOnScreen ();
	    org.x = p.x;
	    org.y = p.y;
	}

	return mouseOwner;
    }

    

    
    public Component getKeyEventSource () {
	Window top = getTopWindow ();
	if (top == null) return null;

	return top.getFocusOwner ();
    }



    /** radius of the corners of a button. */

    public int buttonRadius = 2;

    /** default text height */
    
    public  int textHeight = 12;

    /** size in pixel of the arrow arrea for scrolling a single line */

    public int scrollArrowSize = 0;


    /** Draws a symbol that can be selected and a label to the right
	of the symbol. */
    
    public void drawSelectable (Graphics g, Color bg, 
				int w, int h, 
				int symbol, boolean selected, 
				String label,
				boolean hasFocus) {

	Color fg = g.getColor ();
	FontMetrics fm = g.getFontMetrics ();

	// center wrt. text baseline if possible!!!
	
	int sh = getSymbolHeight (symbol);
	int th = fm.getHeight ();
	int sy = (h - sh) / 2;
	int ty = (h - th) / 2; 

	if (th > sh) {
	    sy = ty + (fm.getAscent () - sh) / 2;
	    if (sy < 0) sy = 0;
	}
	else if (sh > th) {
	    ty = sy + sh - th;
	}

	drawSymbol 
	    (g, bg, 0, sy, symbol, selected);

	drawFocusString 
	    (g, bg, getSymbolWidth(symbol) + 2, ty, label, hasFocus); 
    }


    /** Draws a button. The pressing flag determines if the button
	is currently being pressed. */

    public void drawButton (Graphics g, Color bg, 
                            int w, int h,
                            String label, 
                            boolean hasFocus, boolean pressing) {

        Color fg = g.getColor ();
        FontMetrics fm = g.getFontMetrics ();
	Color bc = pressing ? SystemColor.activeCaption : bg;
        g.setColor (bc);
        g.fillRoundRect (1, 1, w-2, h-2, buttonRadius-1, buttonRadius-1);
	g.setColor (pressing ? Color.white : fg);
        drawFocusString (g, bc, 
                         (w - fm.stringWidth (label))/2, 
                         (h - fm.getHeight ())/2,
                         label, hasFocus); 

        g.setColor (fg);
        g.drawRoundRect (0, 0, w-1, h-1, buttonRadius, buttonRadius);
    }


    /** Draws a String. If the hasFocus flag is set, the string
	is drawn inverted. For larger screens, it may look better
	to draw a gray (or dotted) line around the string. */

    public void drawFocusString (Graphics g, Color bg,  int x, int y, 
                                 String text, boolean hasFocus) {

	//        if (hasFocus) {
	FontMetrics fm = g.getFontMetrics ();

	if (hasFocus) {
	    Color fg = g.getColor ();
	    g.fillRect (x, y, fm.stringWidth (text), fm.getHeight ());
	    g.setColor (bg);
	    g.drawString (text, x, y+fm.getAscent ()); 
	    g.setColor (fg);
	}
	else g.drawString (text, x, y+fm.getAscent ()); 

	//}
	//else g.drawString (text, x, y+g.getFontMetrics().getAscent ()); 
    }

    
    /** Draws one of the arrow or checkbox symbols at the given position */

    public void drawSymbol (Graphics g, Color bg, int x, int y, 
                            int type, boolean selected) {
        
        Color fg = g.getColor ();

        switch (type) {
	case CHOICE:
        case ARROW_UP:
        case ARROW_DOWN:
            if (!selected) g.setColor (bg);
            g.fillRect (x, y++, 7, 6);
            g.setColor (selected ? bg : fg);
            
            if (type == ARROW_UP)
                for (int i = 3; i >= 0; i--) 
                    g.drawLine (x+i, y, x+6-i, y++);
            else {
		if (type == CHOICE) y++;
                for (int i = 0; i < 4; i++) 
                    g.drawLine (x+i, y, x+6-i, y++);
	    }
            break;

        case ARROW_LEFT:
        case ARROW_RIGHT:
            if (!selected) g.setColor (bg);
            g.fillRect (x++, y, 6, 7);
            g.setColor (selected ? bg : fg);

            if (type == ARROW_LEFT)
                for (int i = 3; i >= 0; i--) 
                    g.drawLine (x, y+i, x++, y+6-i);
            else
                for (int i = 0; i < 4; i++) 
                    g.drawLine (x, y+i, x++, y+6-i);
            break;

	case CHECKBOX:
	    g.drawRect (x, y, 6, 6); //7,7-1,1
	    if (selected) {
		g.drawLine (x+1, y+1, x+6, y+6);
		g.drawLine (x+6, y+1, x+1, y+6);
	    }
	    else {
		g.setColor (bg);
		g.fillRect (x+1, y+1, 5,5);
	    }
	    break;

	case RADIOBOX:
	    g.drawRoundRect (x, y, 6, 6, buttonRadius, buttonRadius);
	    if (selected) g.fillRect (x + 2, y + 2, 3, 3);
	    break;
        }

        g.setColor (fg);
    }


    /** Draws the frame of a window with the given parameters */ 

    public void drawWindow (Graphics g, int w, int h,
                            int type, String title, boolean focus) {

        if (type == WINDOW_FRAME) {
        
            if (title != null) {
                drawFocusString (g, Color.white, 0, 0, title, focus);
                g.drawLine (0, textHeight, 320, textHeight);
            }
        }
    }
    
    /** Draws a border of the given style */
    
    public void drawBorder (Graphics g, Color bg,
                            int w, int h, 
                            int style) {

        Color fg = g.getColor ();
        switch (style) {
        case BORDER_SHADOW: 
            g.drawLine (2, 1, w-4, 1);
            g.drawLine (2, h-3, w-3, h-3);
            g.drawLine (3, h-2, w-4, h-2);
            
            g.drawLine (1, 2, 1, h-4);
            g.drawLine (w-3, 2, w-3, h-3);
            g.drawLine (w-2, 3, w-2, h-4);
            break;

        case BORDER_FOCUSABLE:
            g.setColor (bg);
        case BORDER_FOCUSSED:
        case BORDER_DOUBLE:
            g.drawRect (1, 1, w-3, h-3);
            g.setColor (fg);
        case BORDER_SINGLE:
            g.drawRect (0, 0, w-1, h-1);
        }
    }

    /** size and position are measued in 1/1024 nds */

    public void drawScrollbar (Graphics g, Color bg, int w, int h, 
                               int orientation, int position, int size,
                               int scrollType, boolean focus) {

        Color fg = g.getColor ();
        g.setColor (bg);
        g.fillRect (0, 0, w, h);

        g.setColor (fg);
        
        if (orientation == Scrollbar.VERTICAL) {
            int m = w / 2;
	    // g.drawLine (m-1, 0, m+1, 0);
	    //            g.drawLine (m-1, h-1, m+1, h-1);
            g.drawLine (m, 0, m, h);
            g.fillRect (m-1, position, 3, size);
        }
        else {
            int m = h / 2;
            //g.drawLine (0, m-1, 0, m+1);
            //g.drawLine (w-1, m-1, w-1, m+1);
            g.drawLine (0, m, w, m);
            g.fillRect (position, m-1, size, 3);
        }
    }


    public void focusChanged (Component c) {

	if (c instanceof Canvas) return;

	Graphics g = c.getGraphics ();

	if (g != null) {
	    if (c instanceof Window) c.paint (g);
	    else c.update (g);
	}
	else
	    c.repaint ();
    }



    public Dimension getMinimumButtonSize (FontMetrics fm, String label) {
        return new Dimension 
            (fm.stringWidth (label) + 4,
             fm.getHeight () + 2);
    }

    public Dimension getMinimumSelectableSize (int symbol, 
					       FontMetrics fm, 
					       String label) {

	return new Dimension 
	    (getSymbolWidth (symbol) + 2 + fm.stringWidth (label), 
	     Math.max (fm.getHeight (), getSymbolHeight (symbol)));
    }


    /** @deprecated */

    public Dimension getSymbolSize (int type) {
        return new Dimension (7, 7);
    }


    public int getSymbolWidth (int type) {
	return 7;
    }


    public int getSymbolHeight (int type) {
	return 7;
    }


    public Dimension getMinimumScrollbarSize (int orientation) {
        return orientation == Scrollbar.VERTICAL
            ? new Dimension (5, 3*scrollArrowSize)
                : new Dimension (3*scrollArrowSize, 5);
    }

    public Insets getWindowInsets (int type) {
	
        if (type == WINDOW_DIALOG) 
            return new Insets (textHeight + 2, 3, 3, 3);
        else
            return new Insets (textHeight + 2, 0, 0, 0);
    }

    public Insets getBorderInsets (int style) {
        
        switch (style) {
        case BORDER_SHADOW: return new Insets (2, 2, 3, 3);
        case BORDER_SINGLE: return new Insets (1, 1, 1, 1);
        case BORDER_FOCUSABLE:
        case BORDER_FOCUSSED:
        case BORDER_DOUBLE: return new Insets (2, 2, 2, 2);
        default:
            return new Insets (0, 0, 0, 0);
        }
    }

    /** repaints all windows */

    public void paintAll () {
	Vector v = windows;
	windows = new Vector ();
	
	for (int i = 0; i < v.size (); i++) {
	    Window w = (Window) v.elementAt (i);
	    windows.addElement (w);
	    Dimension oldSz = w.getSize ();
	    if (w instanceof Frame || w instanceof Dialog) w.pack ();
	    if (i < v.size () - 1 || oldSz.equals (w.getSize ())) {
		// otherwisw, repaint is scheduled by pack anyway
		Graphics g = w.getGraphics ();
		if (g != null) w.update (g);
	    }
	}
    }
}
