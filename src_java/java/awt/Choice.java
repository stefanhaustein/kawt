// Choice.java - 0996
//
// 2000-10-09 SH Added Javadoc comments and tab activation
// 2000-10-09 SH Changed arrow drawing              
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 SH Changed indexOf for J9 compatibility
// 2000-09-02 Several problems related to remove/add fixed
// 2000-02-17 ItemSelectable interface implemented etc.
// 1999-11-07 Version number 0.11 released
//                - paint error removed
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
import de.kawt.impl.Laf;

public class Choice extends Component implements ItemSelectable {

    /** The choice popup window. */
    Window win;

    /** A private list containing the choice items. */
    List list = new List (this);
 
    public Choice () {
    }
    

    /** Adds an item to the Choice. */ 

    public void add (String item) {
        list.add (item);
        reselect ();
    }


    /** Adds an item to the Choice. */ 
    
    public void addItem (String item) {
        add (item);
    }


    /** Adds an item listener to the choice. The listener is notified
        when an item is selected or deselected. <b>Attention</b>: kAWT 
        supports only one item listener per choice. */
    
    public void addItemListener (ItemListener l) {
        list.addItemListener (l);
    }
    
    
    /** returns the item at the given index */

    public String getItem (int index) {
        return (String) list.elements.elementAt (index);
    }
    
    
    /** Returns the number of items in the choice */

    public int getItemCount () {
        return list.elements.size ();
    }


    /** Returns the index of the selected item. */

    public int getSelectedIndex () {
        return list.sel;
    }
    

    /** Returns the selected item */ 

    public String getSelectedItem () {
        return (String) list.elements.elementAt (list.sel);
    }
    
    
    /** Returns an object array containing the selected objects */

    public Object [] getSelectedObjects () {
        return list.getSelectedObjects ();
    }


    /** returns the minimum dimensions of the choice */

    public Dimension getMinimumSize () {
        Dimension ms = list.getMinimumSize ();
	ms.height = Laf.laf.getMinimumSelectableSize 
	    (Laf.CHOICE, fontMetrics, "").height;

        ms.width += Laf.laf.getSymbolWidth (Laf.CHOICE);
        return ms;
    }
    
   
    public void insert (String item, int index) {
	list.add (item, index);
    }
  


    /** Private activation method. Opens the choice
        popup window */
    
    void action (InputEvent ie) {
        
        win = new Window (_getWindow ().owner);
           
        win.isMenu = true;
        win.add ("Center", list);
	list.requestFocus ();
	list.focus = list.sel;
        win.pack ();
        
        Point p = getLocationOnScreen ();
        
        Dimension scr = Toolkit.getDefaultToolkit ().getScreenSize ();
        
        win.x = p.x;
        win.y = p.y + (h - win.h) / 2;
        
        if (win.x + win.w > scr.width) 
            win.x = scr.width - win.w;
        if (win.y + win.h > scr.height) 
            win.y = scr.height - win.h;
        if (win.y < 0) win.y = 0;
        if (win.x < 0) win.x = 0;

        win.show ();    
    }
    

    /** Paints the choice. */

    public void paint (Graphics g) {

        String sel = list.getSelectedItem ();
	if (sel == null) sel = "";

	Laf.laf.drawSelectable 
	    (g, background, w, h, Laf.CHOICE, false, sel, hasFocus);

	/*        Laf.laf.drawSymbol (g, background, 0, h/2-3, 
			    Laf.ARROW_DOWN, false);

        if (sel != null) 
            Laf.laf.drawFocusString 
		(g, background, 12, (h - fontMetrics.getHeight ())/2, 
		 sel, hasFocus);
	*/
    }


    public void processItemEvent (ItemEvent ev) {
	list.processItemEvent (ev);
    }




    /** Removes the given item from the choice. */
    
    public void remove (String item) {
        list.remove (item);
        reselect ();
    }


    /** Internal method that makes sure that a
        valid entry is selected e.g. after remove */
    
    void reselect () {
        if ((list.sel < 0 || list.sel >= list.elements.size ())
	    && list.elements.size () != 0) 
            list.select (0);
        
        repaint ();
    }


    /** Removes the item at the given index from the choice. */

    public void remove (int index) {
        list.remove (index);
        reselect ();
    }
    

    /** Removes the item listener from the choice. */
   
    public void removeItemListener (ItemListener l) {
        list.removeItemListener (l);
    }
    

    /** Setlects the item at the given index */

    public void select (int index) {
        list.select (index);
    }

    
    /** Selects the given item */
    
    public void select (String item) {
        select (list.elements.indexOf (item, 0));
    }
}




