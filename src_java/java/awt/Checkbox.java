// Checkbox.java - R0996
//
// 2000-09-08 MK Added new Licensetext
// 2000-07-17 MK toString() added
// 2000-03-06 SH Empty constructor added 
//            (requested by Martin Ryzl/fourte4java)
// 1999-11-16 Version number 0.15 released
//            - interface ItemSelectable added 
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

import java.util.*;
import java.awt.event.*;
import de.kawt.impl.Laf;

/** The Checkbox class can be used to display checkbox or radio button
    components. Checkboxes can be checked or unchecked. Radio buttons
    are created by assigning a group to checkboxes. Only one of the
    radio buttons of a group can be checked at a time, similar to the
    channel buttons of a radio. If a radio button is selected, the
    other radio buttons of the same group are unchecked
    automatically. */

public class Checkbox extends Component implements ItemSelectable {  
        
    
    String label;
    boolean state = false;
    CheckboxGroup group;
    //ItemListener itemListener;
    Chain itemListeners;


    /** Constructs a checkbox without a label */
    
    public Checkbox () {
        this ("", null, false);
    }
 

    /** Construct a checkbox with the given label */
    
    public Checkbox (String s) {
        this (s, null, false);
    }
    

    /** Constructs a checkbox with the given label and 
	state */

    public Checkbox (String label, boolean checked) {
        this (label, null, checked);  
    }

    
    /** Constructs a radio button with the given label and state 
	belonging to the given checkbox group. */
    
    public Checkbox (String s, boolean state, CheckboxGroup group) {
        this (s, group, state);
    }
    

    /** @deprecated 
	Constructs a radio button with the given label and state
	belonging to the given checkbox group. */

    public Checkbox (String s, CheckboxGroup group, boolean state) {
        setLabel (s);
        setCheckboxGroup (group);               
        setState (state);
    }
    

    /** Adds an item listener to this checkbox. The item listener
	is notified when the checkbox is selected or unselected.
	<b>Attention:</b> kAWT supports only one item listener
	for each checkbox. */

    public void addItemListener (ItemListener il) {
	itemListeners = new Chain (il, itemListeners);
	eventMask |= AWTEvent.ITEM_EVENT_MASK;
    }
    

    public Dimension getMinimumSize () {

	return Laf.laf.getMinimumSelectableSize 
	    (group == null ? Laf.CHECKBOX : Laf.RADIOBOX, fontMetrics, label);
    }
    

    /** returns the an object array containing this checkbox if 
	it is selected, otherwise an empty object array. */
    
    public Object [] getSelectedObjects () {
        return state ? new Object [] {this} : new Object [0]; 
    }
    

    /** Returns the selection state of the checkbox. */

    public boolean getState () {
        return state;
    }


    /** Returns the label of the checkbox */

    public String getLabel () {
	return label;
    }


    /** Paints the checkbox to the given graphics context. */

    public void paint(Graphics g) {

	Laf.laf.drawSelectable 
	    (g, background, w, h, 
	     group == null ? Laf.CHECKBOX : Laf.RADIOBOX, 
	     state, label, hasFocus);
    }


    /** Processes the given item event by calling the
	registered listener. */

    public void processItemEvent (ItemEvent e) {
        Chain c = itemListeners;
	while (c != null) {
	    ((ItemListener) c.element).itemStateChanged (e);
	    c = c.next;
	}      

    }
    

    /** Internal action method */

    void action (InputEvent e) {
        if (group == null) setState (!state);
        else setState (true);
    }
    

    public CheckboxGroup getCheckboxGroup () {
	return group;
    }
    
    /** Removes the giveb item listener if it is registerd for this
	component */

    public void removeItemListener (ItemListener l) {
        if (itemListeners != null) 
	    itemListeners = itemListeners.remove (l);

    }
  
    
    /** @deprecated
	Assigns a checkbox group to this checkbox and turns
	it into a radio button. Only one group can be
	assigned to a checkbox. If the checkbox is already
        assigned to a group, the previous assignment is removed.
        A group assignment can be removed by passing null to this method. */

    public void setCheckboxGroup (CheckboxGroup newGroup) {
        if (group != null) 
            group.checkboxes.removeElement (this);
        
        group = newGroup;
        if (group != null)
            group.checkboxes.addElement (this);
    }
    

    /** Sets the selection state of this checkbox and repaints it (deferred)
	if neccessary. */

    public void setState (boolean sel) {
        if (sel != state) {
            if (group == null) {
                state = !state;
		//		Graphics g = getGraphics ();
		// if (g != null) update (g); //paint (g);
		    repaint (); 
                //repaint (0, 0, , h);
                
                processItemEvent 
                    (new ItemEvent 
                        (this, ItemEvent.ITEM_STATE_CHANGED, this, 
                         sel ? ItemEvent.SELECTED : ItemEvent.DESELECTED)); 
            }
            else group.setSelectedCheckbox (this);
        }
    }

    
    /** Sets the label of the checkbox. */

    public void setLabel (String s) {
        this.label = s;
    }
}
