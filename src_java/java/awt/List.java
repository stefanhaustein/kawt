// List.java
//
// 2000-10-14 SH Focus line added.
// 2000-10-12 SH Focus border change added
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 SH Changed indexOf for J9 compatibility
// 2000-06-26 SH choice selection bug fixed
// 2000-06-25 SH/MK SelectedItem Color changed
// 2000-06-22 SH Changed to use ScrollManager
// 2000-05-23 SH Event generation improved
// 2000-05-20 SH deselect added, setSelectedIndex renamed to select
// 2000-05-02 SH deselction event bug fixed 
// 2000-03-03 MK remove added
// 2000-03-01 SH setSelectedIndex bug fixed
// 2000-02-17 MK/SH ItemSelectable implemented
// 1999-12-14 SH removed double paint problem 
// 1999-11-30 Version 0.21:
//            - getMinimumSize changed (wg. Choice out of screen)
//            - repaint added to removeAll
// 1999-11-26 Version 0.2;  
//            - getSelectedIndex added (bugrep. by Flemming Bregnvig)
// 1999-11-07 Version number 0.11 released
//            -flickering while itemselection fixed
//            -list srolls now while dragging out of
//            -the list area
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

import java.awt.event.*;
import java.util.Vector;
import de.kawt.impl.Laf;

public class List extends Component implements ItemSelectable {
    
    Vector elements = new Vector ();
    
    int sel = -1;
    int focus = -1;

    Choice choice;
    Menu menu;
    ScrollManager scrollManager;
    
    ItemListener il;

    int minW;

    
    public List () {
	scrollManager = new ScrollManager 
	    (this, 
	     Laf.laf.getBorderInsets (Laf.BORDER_FOCUSABLE));
    }


    List (Choice choice) {
	this.choice = choice;
	scrollManager = new ScrollManager 
	    (this, Laf.laf.getBorderInsets (Laf.BORDER_SHADOW));
    }

    List (Menu menu) {
	this.menu = menu;
	scrollManager = new ScrollManager 
	    (this, Laf.laf.getBorderInsets (Laf.BORDER_SHADOW));
    }



    void addObject (Object item, int index) {

	int iw = fontMetrics.stringWidth (""+item) + 7;
	if (iw > minW) minW = iw;

	if (index <= -1) {
	    elements.addElement (item);
	}
	else {
	    if (index >= elements.size ())
		elements.addElement (item);
	    else
		elements.insertElementAt (item, index);
	}

	doLayout ();
	repaint ();
    }
    

    public void add (String item) {
	addObject (item, -1);
    }

    public void add (String item, int index) {
	addObject (item, index);
    } 

    
    public void addItemListener (ItemListener l) {
	if (il != null) 
	    throw new TooManyListenersException ();
	
	il = l;
    }

 
    public Dimension getMinimumSize () {
	
	int mh = elements.size () * fontMetrics.getHeight ()
	    + scrollManager.insets.top + scrollManager.insets.bottom;

	int mw = minW + scrollManager.insets.left 
	    + scrollManager.insets.right;

	if (mh > 100) {
	    mh = 100;
	    mw += Laf.laf.getMinimumScrollbarSize (Scrollbar.VERTICAL).width;
	}

	return new Dimension (mw, mh);
    }


    public int getSelectedIndex () {
	return sel;
    }


    public String getSelectedItem () {
	if (sel < 0 || sel >= elements.size ()) return null;
	return (String) elements.elementAt (sel);
    }


    public Object [] getSelectedObjects () {
	return sel == -1 ? new Object [0] : new Object [] {elements.elementAt (sel)};
    }
    
    
    public Component getComponentAt (int px, int py) {
	return scrollManager.findComponentAt (px, py);
    }


    public void update (Graphics g) {
	paint (g);
    }
    

    public void paint (Graphics g) {
 
	Rectangle clip =scrollManager.paint (g, (choice != null | menu != null)
			     ? Laf.BORDER_SHADOW
			     : (hasFocus && focus == -1 
				? Laf.BORDER_FOCUSSED 
				: Laf.BORDER_FOCUSABLE));

	// draw list

	int lineH = fontMetrics.getHeight ();

	int i = clip.y / lineH;

	int ty = i * lineH;
    	int tx = scrollManager.xOffset;
	

	while ( ty < clip.y + clip.height) {
	    
	    boolean invert = (menu != null || choice != null)
		? (i == focus)
		: (i == sel && (i != focus || !hasFocus)); 

	    g.setColor (invert 
			? SystemColor.activeCaption 
			: Color.white);

	    g.fillRect (tx, ty, scrollManager.viewPortW, lineH);

	    if (i < elements.size ()) {
		g.setColor (invert ? Color.white : Color.black);
		g.drawString (elements.elementAt (i).toString (), 
			      1, ty + fontMetrics.getAscent ());

		if (i == focus && !invert) 
		    g.drawRect (tx, ty, scrollManager.viewPortW-1, lineH-1);
	    }
	    
	    i++;
	    ty += lineH;
	}
    }

    public void processItemEvent (ItemEvent ev) {
	il.itemStateChanged (ev);
    }


    public void dispatchEvent (AWTEvent event) {
	super.dispatchEvent (event);

	if (event.consumed) return;

	if (event.id == KeyEvent.KEY_PRESSED) {
	    KeyEvent ke = (KeyEvent) event;

	    switch (ke.getKeyCode ()) {
	    case KeyEvent.VK_CANCEL:
		if (focus != -1) { 
		    focus = -1;
		    ke.consume ();
		    repaint ();
		}
		break;
		
	    case KeyEvent.VK_ACCEPT:
	    case KeyEvent.VK_SPACE:
		if (focus != -1) {
		    select (focus);
		    focus = -1;
		}
		else 
		    focus = (sel == -1 && elements.size () > 0) ? 0 : sel;
		
		ke.consume ();
		repaint ();
		break;

	    case KeyEvent.VK_UP:
		if (focus != -1) {
		    if (focus > 0) moveFocus (focus-1);
		    ke.consume ();
		}
		break;

	    case KeyEvent.VK_DOWN:
		if (focus != -1) {
		    if (focus < elements.size ()-1) moveFocus (focus+1);
		    ke.consume ();
		}
		break;
	    }
	}
	else if (event instanceof MouseEvent) {
	    MouseEvent me = (MouseEvent) event;

	    if (event.id == MouseEvent.MOUSE_RELEASED)  
		select (focus);

	    if (event.id != MouseEvent.MOUSE_DRAGGED 
		&& event.id != MouseEvent.MOUSE_PRESSED) return;

	    int newNewSel = (scrollManager.yOffset + me.getY ()) 
		/ fontMetrics.getHeight ();

	    if (newNewSel < 0) newNewSel = 0;
	    if (newNewSel >= elements.size ()) newNewSel = elements.size () -1;
	
	    else if (newNewSel != focus) 
		moveFocus (newNewSel);
	}
    }


    public void removeAll () {
	elements.removeAllElements ();
	deselect (sel);
	minW = 20;
	doLayout ();
	repaint ();
    }

    public void moveFocus (int line) {

	if (focus == line) return;
	
	int old = focus;
	focus = line;
	boolean scrolled = false;
	int lineH = fontMetrics.getHeight ();

	if (scrollManager.vBar != null) {
	    int sv = focus * lineH;

	    if (sv < scrollManager.vBar.currValue) { 
		scrollManager.vBar.currValue = sv;
		scrolled = true;
	    }
	    else if (sv + lineH > scrollManager.vBar.currValue 
		     + scrollManager.vBar.currVisible) {

		scrollManager.vBar.currValue = 
		    sv + lineH - scrollManager.vBar.currVisible;
		scrolled = true;
	    }
	}

	if (scrolled) 
	    repaint ();
	else 
	    repaint (focus, old);
    }

    public synchronized void doLayout () {  

	scrollManager.doLayout 
	    (minW, elements.size () * fontMetrics.getHeight ());
    }


    public String getItem (int index) {
	return (String) elements.elementAt (index);
    }

    public int getItemCount () {
	return elements.size ();
    } 

    
    public void remove (int position) {
	if (sel == position)	
	    deselect (sel);
	elements.removeElementAt (position);
	doLayout ();
	repaint ();
    }

    public void remove (String item) {
	remove (elements.indexOf(item, 0)); 
    }

    
    public void removeItemListener (ItemListener l) {
	if (l == il) il = null;
    }


    public void deselect (int old) {

	if (old != sel || old == -1 || old >= elements.size ()) return;
	
	//	newSel = -1;
	sel = -1;

	if (il != null)
	    il.itemStateChanged 
		(new ItemEvent 
		    (this, ItemEvent.ITEM_STATE_CHANGED, 
		     elements.elementAt (old), 
		     ItemEvent.DESELECTED));

	repaint (old, old);
    }
    

    void repaint (int mn, int mx) {
	int lh = fontMetrics.getHeight ();

	if (mx < mn) {
	    int tmp = mn;
	    mn = mx;
	    mx = tmp;
	}

	mn = mn * lh - scrollManager.yOffset + scrollManager.insets.top;
	mx = (mx+1) * lh - scrollManager.yOffset + scrollManager.insets.top;

	if (mx >= 0 && mn < h)
	    repaint (scrollManager.insets.left, mn, 
		     scrollManager.viewPortW, mx-mn);
    }


    public void replaceItem (String value, int index) {
	int iw = fontMetrics.stringWidth (""+value) + 7;
	elements.setElementAt (value, index);
	if (iw > minW) {
	    minW = iw;
	    doLayout ();
	    repaint ();
	}
	else repaint (index, index);
    }


    public void select (int index) {

	if (menu != null) {

	    while (true) {
		Window top = Laf.getTopWindow ();
		if (top == null || !top.isMenu) break;
		top.setVisible (false);
	    }
	    
	    if (index != -1) {
		MenuItem item = (MenuItem) elements.elementAt (index);
		if (item.al != null) 
		    item.al.actionPerformed 
			(new ActionEvent 
			    (item, ActionEvent.ACTION_PERFORMED, 
			     item.command == null 
			     ? item.label 
			     : item.command));
	    }
	    
	    return;
	}

	ItemSelectable source = this;
	
	if (choice != null) { 
	    if (choice.win != null) choice.win.setVisible (false);
	    //	    choice.repaint ();
	    source = choice;
	}
	

	if (index != sel) {
	    
	    if (choice != null) 
		choice.repaint ();
	    else {
		int mn = Math.min (Math.min (sel, index), focus);
		int mx = Math.max (Math.max (sel, index), focus);
		repaint (mn, mx);
	    }

	    deselect (sel);

	    sel = index;
	    focus = sel;

	    if (il != null && index != -1)
		il.itemStateChanged 
		    (new ItemEvent 
			(source, ItemEvent.ITEM_STATE_CHANGED, 
			 elements.elementAt (index), 
			 ItemEvent.SELECTED));
	}
    }
}


