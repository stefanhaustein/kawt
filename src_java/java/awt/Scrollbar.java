// Scrollbar.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-08-13 Changed Images to gif for MIDP comptibility
// 1999-10-22 Version number 0.1 released
// 1999-11-07 Version number 0.11 released
//            - problem in paint fixed
//  	        (sometimes resizing causes an 
//	        ArtithmeticException)
// 1999-12-14 SH removed double paint problem for List
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

/** <ul>
 *    <li>Only one Listener for each event Type is allowed
 *    <li>AdjustmentEvents are delivered directly to the Listener
 *       without going through ProcessAdjustmentEvent!
 *   </ul>
 */


public class Scrollbar extends Component {  
	
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    //    int minW = 7;
    // int minH = 12;
    
    int minValue;
    int maxValue;
    int currValue;
    int orientation;
    int currVisible;	
    
    int scrPos;
    int scrBarSize;
    int scrSpace;
    
    int unitIncrement = 1;
    int blockIncrement;

    int scrollType = -1;

    /*
      boolean pressingInc = false;
      boolean pressingDec = false;
    */

    boolean dragging = false;
    
    AdjustmentListener adjustmentListener;
    //Component internalOwner;
    boolean repaintParent;

    public Scrollbar () {
	this (VERTICAL);
    }

    public Scrollbar (int orientation) {
	this (orientation, 0, 10, 0, 100);
    } 

    public Scrollbar (int orientation, int value, int visible, 
		      int minimum, int maximum) {
 		
	this.orientation = orientation;
	this.currValue = value;
	this.currVisible = visible;
	this.minValue = minimum;
	this.maxValue = maximum;
	
	blockIncrement = currVisible;
    }
    
    
    public void addAdjustmentListener (AdjustmentListener al) {
	if (adjustmentListener != null) 
	    throw new TooManyListenersException ();
	
	adjustmentListener = al;
    }
    
    
    public int getMaximum() {
   	return maxValue;
    }
    
    
    public int getMinimum() {
  	return minValue;
    }                   
    
    
    public Dimension getMinimumSize () {
	return Laf.laf.getMinimumScrollbarSize (orientation);
    }
    

    public int getBlockIncrement () {
	return blockIncrement;
    }
    

    public int getOrientation() {
      return orientation;
    }

    
    public int getUnitIncrement() {
	return unitIncrement;
    }
                   
    
    public int getValue() {
   	return currValue;
    }

    
    public void paint(Graphics g) {
	
	scrSpace = (orientation == Scrollbar.VERTICAL ? h : w) 
	    - 2 * Laf.laf.scrollArrowSize;

	scrBarSize = (currVisible * scrSpace) / (maxValue - minValue);
	//scrSpace -= scrBarSize;
	scrPos = ((currValue-minValue) * scrSpace) / (maxValue - minValue) 
	    + Laf.laf.scrollArrowSize;

	Laf.laf.drawScrollbar (g, background,  w, h, orientation, 
			       scrPos, scrBarSize, 
			       scrollType, hasFocus);
       /*

	scrSpace = ((orientation == Scrollbar.HORIZONTAL) 
		    ? w : h) - 12;
	
	scrBarSize = (scrSpace * currVisible) 
	    / (maxValue - minValue + currVisible);

	if (scrBarSize < 4) scrBarSize = 4;
	
	scrSpace -= scrBarSize;

	scrPos = 6 + (scrSpace * (currValue - minValue)) 
	    / (maxValue - minValue);


       */

    }
    
    /*
      public void processAdjustmentEvent (AdjustmentEvent e) {
      if (adjustmentListener != null)
      adjustmentListener.adjustmentValueChanged (e);
      }
    */
    

    public void dispatchEvent (AWTEvent event) {
	super.dispatchEvent (event);

	if (event.consumed || !(event instanceof MouseEvent)) return;
	
	MouseEvent e = (MouseEvent) event;

	int mpos = (orientation != HORIZONTAL) ? e.getY () : e.getX ();
	
	/*
	pressingDec = false;
	pressingInc = false;
	*/
	int newVal = currValue;
	scrollType = -1;

	if (event.id == e.MOUSE_PRESSED 
	    && mpos >= scrPos 
	    && mpos <= scrPos + scrBarSize)

	    dragging = true;
	else if (event.id == e.MOUSE_DRAGGED && dragging) {
	    
	    newVal = minValue 
		+ ((mpos - 7 - scrBarSize / 2) 
		   * (maxValue-minValue)) / scrSpace;

	    scrollType = AdjustmentEvent.TRACK;
	}
	else if (event.id == e.MOUSE_DRAGGED || event.id == e.MOUSE_PRESSED) {
	    dragging = false;

	    if (mpos < scrPos) {

		if (mpos <= Laf.laf.scrollArrowSize) {
		    newVal = currValue - unitIncrement;
		    scrollType = AdjustmentEvent.UNIT_DECREMENT;
		}
		else {
		    newVal = currValue - blockIncrement; 
		    scrollType = AdjustmentEvent.BLOCK_DECREMENT;
		}
	    }
	    else if (mpos > scrPos + scrBarSize) {
		if (mpos >= (orientation != HORIZONTAL ? h : w) 
		    - Laf.laf.scrollArrowSize) { 

		    newVal = currValue + unitIncrement; 
		    scrollType = AdjustmentEvent.UNIT_INCREMENT;
		}
		else {
		    newVal = currValue + blockIncrement;
		    scrollType = AdjustmentEvent.BLOCK_INCREMENT;
		}
	    }
	}
	else if (event.id == e.MOUSE_RELEASED) {
	    if (!repaintParent)
		paint (getGraphics ());
	    else parent.repaint (x, y, w, h);
	}

	if (newVal > maxValue-currVisible) newVal = maxValue-currVisible;
	if (newVal < minValue) newVal = minValue;

	if (currValue != newVal) {
	    currValue = newVal;

	    if (!repaintParent) paint (getGraphics ());
	    else parent.repaint ();
	    
	    if (adjustmentListener != null)
		adjustmentListener.adjustmentValueChanged 
		    (new AdjustmentEvent 
			(this, AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED,
			 scrollType, currValue));
	}
    }


    public void setBlockIncrement (int inc) {
	blockIncrement = inc;
    }
    
    
    public void setMaximum (int max) {
	maxValue = max;
	repaint ();
    }
    
    public void setMinimum (int min) {
	minValue = min;
	repaint ();
    }
    
    public void setOrientation (int orientation) {
	this.orientation = orientation;
	valid = false;
    }
    
    
    public void setUnitIncrement (int inc) {
	unitIncrement = inc;
    }
    
    
    public void setValue (int v) {
	currValue = v;
	repaint ();
    }
    

    public void setValues (int val, int visible, int min, int max) {
	maxValue = max;
	minValue = min;
	currVisible = visible;
	currValue = val;
	repaint ();
    }
}
