// Component.java 0996
//
// 2000-11-04 SH This change log is replaced by CVS 
// 2000-10-07 SH added focus support
// 2000-10-05 SH setSize fix
// 2000-09-23 SH background/foreground fixes
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 SH Changed indexOf for J9 compatibility
// 2000-08-08 SH changed getMin/PrefSize defaulting
// 2000-06-18 SH changed parent to Component for simplified 
//            implementation of scrolling
// 2000-05-20 SH improved repaint mechanism
// 2000-05-18 SH visible check added to getGraphics
// 2000-05-15 SH made getGraphics "ScrollPane-aware" 
// 2000-05-01 SH background / foreground added
// 2000-04-09 SH improved getGraphics 
// 1999-11-22 Version 0.12 getSize () added
// 1999-11-07 Version number 0.11 released
//             - internal variables width and height
//               changed to w and h
//
//#include ..\..\license.txt
//
// kAWT version 0.95
//
// Copyright (C) 1999-2000 by Michael Kroll & Stefan Haustein GbR, Essen
//
// Contact: kawt@kawt.de
// General Information about kAWT is available at: http://www.kawt.de
//
// Using kAWT for private and educational and in GPLed open source
// projects is free. For other purposes, a commercial license must be
// obtained. There is absolutely no warranty for non-commercial use.
//
//
// 1. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO
//    WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE
//    LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
//    HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
//    WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT
//    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
//    FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS TO THE
//    QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE
//    PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY
//    SERVICING, REPAIR OR CORRECTION.
//   
// 2. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN
//    WRITING WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY
//    MODIFY AND/OR REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE
//    LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL,
//    INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
//    INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
//    DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU
//    OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY
//    OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN
//    ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
//   
//    END OF TERMS AND CONDITIONS
// 
//#endinclude


package java.awt;

import java.util.Vector;
import java.awt.event.*;
import java.awt.image.*;
import de.kawt.impl.Laf;

/** Important Limitations:
 
    <ul>
    <li>Only one listener of each type is allowed</li>
    <li>Many methods are still missing</li>
    <ul>
 */

public abstract class Component implements ImageObserver {

    static final int NEXT = 1;
    static final int PREV = -1;
    static final int LAST_CHILD = -2;
    static final int THIS_OR_PREV = 0;

    static final int OWN_FOREGROUND = 1;
    static final int OWN_BACKGROUND = 2;
    static final int OWN_FONT = 4;

    int x;
    int y;
    int w;
    int h;
    /*
    int minW = -1;
    int minH = -1;
    */
    boolean enabled = true;
    boolean visible = true;
    boolean hasFocus = false;
    boolean valid = false;
    
    Component parent;
    
    Chain mouseListeners;
    Chain mouseMotionListeners;
    Chain keyListeners;
    Chain focusListeners;

    
    FontMetrics fontMetrics = Toolkit.defaultFontMetrics;

    Color background = Color.white;
    Color foreground = Color.black;

    int ownProperties;
    long eventMask;
    long shadowMask;
    
    public Component () {}


  
    public void addMouseListener (MouseListener l) {
	
	mouseListeners = new Chain (l, mouseListeners);
	eventMask |= AWTEvent.MOUSE_EVENT_MASK;
    }

    
    public void addFocusListener (FocusListener l) {

	focusListeners = new Chain (l, focusListeners);
	eventMask |= AWTEvent.FOCUS_EVENT_MASK;
    }


    public void addMouseMotionListener (MouseMotionListener l) {

        mouseMotionListeners = new Chain (l, mouseMotionListeners);
	eventMask |= AWTEvent.MOUSE_MOTION_EVENT_MASK;
    }


    public void addKeyListener (KeyListener l) {
	keyListeners = new Chain (l, keyListeners);
	eventMask |= AWTEvent.KEY_EVENT_MASK;
    }


    /** internal convenience method, called on unconsumed ENTER/SPACE/ACCEPT or mouse-clicked */

    void action (InputEvent event) {
    }

    

    public boolean contains (int x, int y) {
        return x >= 0 && x <= w && y >= 0 && y <= h;
    }


    public Image createImage (int w, int h) {
	return Toolkit.defaultToolkit.createImage (w, h);
    }
  
    
    public void doLayout () {  
        valid = true;
    }
    


    /** internal convenience method for drawing possibly focussed strings
    
    void drawFocusString (Graphics g, String text, int x, int y) {
        
        if (hasFocus) {
            FontMetrics fm = g.getFontMetrics ();
            g.fillRect (x, y, fm.stringWidth (text), fm.getHeight ());
            Color c = g.getColor ();
            g.setColor (new Color (0x0ffffff ^ c.getRGB ()));
            g.drawString (text, x, y+fm.getAscent ()); 
            g.setColor (c);
        }
        else g.drawString (text, x, y+fontMetrics.getAscent ()); 
    }
    */

    public void disableEvents (long mask) {
	eventMask &= ~mask;
    }


    public void enableEvents (long mask) {
	eventMask |= mask;
    }

    
    public Component getComponentAt (int px, int py) {
        return contains (px, py) ? this : null; 
    }


    public Color getBackground () {
	return background;
    }

    public Rectangle getBounds () {
        return new Rectangle (x, y, w, h);
    }


    /** return null if not visible */
    
    public Point getLocationOnScreen () {
        
        // avoid recursion in order to save stack space...
        
        int sx = x;
        int sy = y;
        
        Component current = this;

        while (current.parent != null) {
            current = current.parent;
            if (!current.visible) return null;
            sx += current.x;
            sy += current.y;
	    /*
	    if (current instanceof ScrollPane) {
		Insets ins = ((ScrollPane) current).scrollManager.insets;
		sx += ins.left;
		sy += ins.top;
	    }
	    */
        } 
        
        if (!(current instanceof Window)) return null;

         return new Point (sx, sy);
    }


    public Font getFont () {
        return fontMetrics.font;
    }

           
    public FontMetrics getFontMetrics (Font font) {
        return Toolkit.defaultToolkit.getFontMetrics (font);
    }


    public Color getForeground () {
	return foreground;
    }
 

    public Graphics getGraphics () {

        if (!visible) return null;

        int sx = x;
        int sy = y;

        Component current = this;
        Graphics g = Toolkit.createGraphics ();
        
        while (current.parent != null) {
            current = current.parent;
            if (!current.visible) return null;

            sx += current.x;
            sy += current.y;

            if (current instanceof ScrollPane) {
                ScrollManager sm = ((ScrollPane) current).scrollManager;
		/*		sx += sm.insets.left;
				sy += sm.insets.top; */
		g.clipRect (sx, sy, 
                            sm.viewPortW, 
                            sm.viewPortH);
            }
        }

        //      if (!(current instanceof Window)) return null;
        if (current != Laf.laf.getTopWindow ()) return null;

        g.translate (sx, sy);
        g.clipRect (0, 0, w, h);

        /*
        
        // substract all windows in front of the current:
        // getWindow, getFrame, ...
        
        Window window = (Window) current;
        Frame frame = window.owner;
        
        int i0 = 0;

        if (frame != window) {
            i0 = frame.windows.indexOf (window, 0) + 1; 
            if (i0 == 0) return null;
        }
            
        for (int i = i0; i < frame.windows.size (); i++) 
            g._sub ((Window) frame.windows.elementAt (i));
        */

	g.setFont (fontMetrics.font);
	g.setColor (foreground);
        return g;
    }

    public Dimension getMaximumSize () {
        return new Dimension (32767, 32767);
    }
        

    public Dimension getSize () {
        return new Dimension (w, h);
    }
    
    
    public int getX() {
        return x;
    }

    
    public int getY() {
        return y;
    }

    
    public int getWidth() {
        return w;
    }

    
    public int getHeight() {
        return h;
    }
    
    
    public Dimension getPreferredSize () {
        return getMinimumSize ();
    }
    
    
    public Dimension getMinimumSize () {
        return new Dimension (w, h);
    }


    /** find previous component in focus order. does not move focus
        directly in order to save stack space */

    Component getPrev (boolean strong, boolean checkChildren) {
        
        if (visible && checkChildren && (this instanceof Container)) {
            Container c = (Container) this;
            int cs = c.childCount;
            if (cs > 0) return c.getComponent (cs-1).getPrev (false, true);
        }
        
        if (!strong && visible && isFocusTraversable ()) return this;
        
        Container parent = getParent ();
        
        if (parent == null) {
           if (!strong && checkChildren) return this; // impossible!
           return getPrev (false, true);
        }     

        int i = parent.indexOf (this);
        while (--i >= 0) {
            Component cand = parent.getComponent (i);
            if (cand.visible 
		&& (cand instanceof Container 
		    || cand.isFocusTraversable ())) 

               return cand.getPrev (false, true); 
         }

         return parent.getPrev (false, false); 
     }
                     
                 
     Component getNext (boolean strong, boolean checkChildren) {
         if (!strong && visible && isFocusTraversable ()) return this;
         
         if (visible && checkChildren && (this instanceof Container)) {
            Container c = (Container) this;
            if (c.childCount > 0) 
		return c.children [0].getNext (false, true);
         } 
         
         Container parent = getParent ();
         if (parent == null) { 
            if (!strong && checkChildren) return this; // should be impossible!
            else return this.getNext (false, true); 
         }    
         int i = parent.indexOf (this);   
         while (++i < parent.childCount) {
             Component cand = parent.getComponent (i);
             if (cand.visible 
		 && (cand instanceof Container 
		     || cand.isFocusTraversable ()))

                return cand.getNext (false, true);
         }
                
         return parent.getNext (true, false);
     }

            

    public Container getParent () {
        return (parent instanceof Container) ? ((Container) parent) : null;
    }


    // Focus handling is performed "outside" in EventQueue
    
    public void dispatchEvent (AWTEvent event) {

	if (event instanceof KeyEvent) {
	    if ((eventMask & AWTEvent.KEY_EVENT_MASK) != 0)

		processEvent (event);
	}
	else if (event instanceof MouseEvent) {
	    if (event.id == MouseEvent.MOUSE_DRAGGED) {
		if ((eventMask & AWTEvent.MOUSE_MOTION_EVENT_MASK) != 0)
 
		    processEvent (event); 
	    }
	    else {
		if ((eventMask & AWTEvent.MOUSE_EVENT_MASK) != 0) 

		    processEvent (event);
		
		if (event.id == MouseEvent.MOUSE_PRESSED && !event.consumed) 
		    action ((InputEvent) event);
	    }
	}
	else if (event instanceof ActionEvent) {
	    if ((eventMask & AWTEvent.ACTION_EVENT_MASK) != 0)
		processEvent (event);
	}
	else if (event instanceof FocusEvent) {
	    Laf.laf.focusChanged (this);

	    if ((eventMask & AWTEvent.FOCUS_EVENT_MASK) != 0) 
		processEvent (event);
	}
	else 
	    processEvent (event);
    }



    public boolean hasFocus () {
        return hasFocus;
    }

    //public boolean imageUpdate (Image img, int infoflags, int x, int y, int width, int height) {
    //return false;
    //}
    

    public void invalidate () {
        Component current = this;
        do {
            current.valid = false;
            current = current.parent;
        } 
        while (current != null);
    }
    

    public boolean isEnabled () {
        return enabled;
    }


    public boolean isVisible () {
        return visible;
    }

    public boolean isValid () {
        return valid;
    }

    
    public boolean isShowing () {
	Component c = this;
	while (c.parent != null) {
	    if (!c.visible) return false;
	    c = c.parent;
	}

	return c.visible && (c instanceof Window);
    }


    public boolean isFocusTraversable () {
        return true;
    }



    void moveFocus (KeyEvent event, int dir) {
    
	if (dir == LAST_CHILD || dir == THIS_OR_PREV) {
	    requestFocus ();
            return;
        }

        Container parent = getParent ();
        if (parent == null) return;  
        int i = parent.indexOf (this);
        
        while (true) {
          i += dir;
          if (i < 0) {
             parent.moveFocus (event, THIS_OR_PREV);
             return;
          }
          if (i >= parent.childCount) {
             parent.moveFocus (event, NEXT);
             return;
          }
          Component rf = (Component) parent.children [i];
          if (rf.isFocusTraversable () && rf.visible) {
              rf.moveFocus (event, LAST_CHILD);
              return;
          }
       }
    }


    public void prepareImage (Image img, ImageObserver io) {
    } 


   /** Processes the given event. Only active when a listener
	is registered or events are enabled by enableEvents (). */

    protected void processEvent (AWTEvent e) {
	if (e instanceof KeyEvent) 
	    processKeyEvent ((KeyEvent) e);
	else if (e instanceof MouseEvent) { 
	    if (e.id == MouseEvent.MOUSE_DRAGGED) 
		processMouseMotionEvent ((MouseEvent) e);
	    else 
		processMouseEvent ((MouseEvent) e);
	}
	else if (e instanceof FocusEvent) {
	    processFocusEvent ((FocusEvent) e);
	}
    }



   
    protected void processFocusEvent (FocusEvent fe) {
	Chain c = focusListeners;
	while (c != null) {  
	    FocusListener fl = (FocusListener) c.element;
	    if (fe.id == FocusEvent.FOCUS_GAINED) fl.focusGained (fe);
	    else fl.focusLost (fe);
	    c = c.next;
        }
    }



    protected void processKeyEvent (KeyEvent ke) {
	Chain c = keyListeners; 
	while (c != null) {
	    KeyListener kl = (KeyListener) c.element;

	    switch (ke.id) {
	    case KeyEvent.KEY_RELEASED: 
		kl.keyReleased (ke); 
		break;
	    case KeyEvent.KEY_TYPED: 
		kl.keyTyped (ke); 
		break;
	    case KeyEvent.KEY_PRESSED: 
		kl.keyPressed (ke); 
		break;
	    }
	    c = c.next;
	}
    }
    
   
    protected void processMouseEvent (MouseEvent me) {

	Chain c = mouseListeners;
	while (c != null) {
	    MouseListener ml = (MouseListener) c.element;
	    switch (me.id) {
	    case MouseEvent.MOUSE_PRESSED: 
		ml.mousePressed (me); 
		break;
        
	    case MouseEvent.MOUSE_RELEASED: 
		ml.mouseReleased (me); 
		break;
            
	    case MouseEvent.MOUSE_CLICKED: 
		ml.mouseClicked (me); 
		break;
	    }
	    c = c.next;
	}
    }


    protected void processMouseMotionEvent (MouseEvent me) {
	Chain c = mouseMotionListeners;
	while (c != null) {
	    ((MouseMotionListener) c.element).mouseDragged (me);
	    c = c.next;
	}
    }

    
    /** propagates foreground, font and background from parent.
        For components, this is recursively called for all children. */

    void propagateProperties () {
	if (parent != null) {
	    if ((ownProperties & OWN_FOREGROUND) == 0)
		foreground = parent.foreground;
	    
	    if ((ownProperties & OWN_BACKGROUND) == 0)
		background = parent.background;

	    if ((ownProperties & OWN_FONT) == 0) 
		fontMetrics = parent.fontMetrics;
	}
    }

    public void paint (Graphics g){
    }

    
    public void paintAll (Graphics g) {
        if (visible && g != null) {
            paint (g);
        }
    }
    
	

    public void requestFocus () {
        Component current = this;
        do {
            if (current instanceof Window) {
                ((Window) current).proxyRequestFocus (this);
                return;
            }
            current = current.parent;
        }
        while (current != null); 
    }


    public void repaint (int rx, int ry, int rw, int rh) {
        
        if (rx < 0) {
            rw += rx;
            rx = 0;
        }
        if (ry < 0) {
            rh += ry;
            ry = 0;
        }

        if (rw + rx > w) 
            rw = w - rx;

        if (rh + ry > h) 
            rh = h - ry;

        Component current = this;
        while (true) {
            if (!current.visible) return;
            if (current.parent == null) break;
            rx += current.x;
            ry += current.y;
            current = current.parent;
        }

        if (! (current instanceof Window)) return;

        ((Window) current).addRepaint (new Rectangle (rx, ry, rw, rh));
    }

    
    public void repaint () {
        repaint (0, 0, w, h);
    }


    public void setBackground (Color c) {

        if (c == null) 
	    ownProperties &= ~OWN_BACKGROUND;
	else {
	    background = c;
	    ownProperties |= OWN_BACKGROUND;
	}

	propagateProperties ();
        repaint ();
    }

 
    
    public void setBounds (int newX, int newY, int newW, int newH) {
	
	if (newX != x || newY != y || newW != w || newH != h) {
	    repaint ();
	    x = newX;
	    y = newY;
	    w = newW;
	    h = newH;
	    invalidate ();
	    repaint ();
	}
    }
    
    
    public void setEnabled (boolean b) {
        if (enabled != b) {
            enabled = b;
            repaint ();
        }
    }


    /** sets foreground color */
    
    public void setForeground (Color c) {

        if (c == null) 
	    ownProperties &= ~OWN_FOREGROUND;
	else {
	    foreground = c;
	    ownProperties |= OWN_FOREGROUND;
	}
	propagateProperties ();
	
        repaint ();
    }


    public void setFont (Font f) {

        if (f == null) 
	    ownProperties &= ~OWN_FONT;
	else {
      	    fontMetrics =  Toolkit.defaultToolkit.getFontMetrics (f);
	    ownProperties |= OWN_FONT;
	}
	propagateProperties ();
	
        repaint ();
    }

    public void setLocation (int x, int y) {
        setBounds (x, y, w, h);
    }
    

    public void setSize (int w, int h) {
	setBounds (x, y, w, h);
    }
    

    public void setSize (Dimension d) {
        setBounds (x, y, d.width, d.height);
    }
    
    /*
    public void setMinimumSize (Dimension d) {
        minW = d.width;
        minH = d.height;
    }
    */


    public void update (Graphics g) {
        if (g== null) return;
        g.setColor (background);
        g.fillRect (0, 0, w, h);
        g.setColor (foreground); 
        paintAll (g);
    }
    

    public void setVisible (boolean v) {
        if (v != visible) {
            visible = v;
            invalidate ();
	    if (parent != null && !visible)
		parent.repaint (x, y, w, h);
	    else 
		repaint ();
        }
    }

    public void transferFocus () {
	getNext (true, true).requestFocus ();
    }


    public void validate () {
        valid = true;
    }

    /*
    void _validate () {
        valid = true;
    }
    */
    
    Window _getWindow () {
        Component w = this;
        while (w.parent != null) w=w.parent;

        return (Window) w;
    }
    
/*
 * $Log: Component.java,v $
 * Revision 1.23  2001/10/16 21:17:55  haustein
 * changes f. 0996
 *
 * Revision 1.22  2001/09/30 21:46:48  haustein
 * midlet added
 *
 * Revision 1.21  2001/09/22 20:44:04  haustein
 * changed the Component listeners to multicast model
 *
 * Revision 1.20  2001/09/17 22:34:02  haustein
 * Fixed make_pdb.bat AGAIN!!! AHRG!!!!!
 *
 * Revision 1.19  2001/09/16 22:51:03  haustein
 * fixed repaint problem when components are hidden
 *
 * Revision 1.18  2001/09/16 15:13:21  mkroll
 * Changed doLayout() to public.
 *
 * Revision 1.17  2001/08/28 23:09:02  haustein
 * ActionEvent issue fixed, Button fixess for 0996.
 *
 *
 */


}
