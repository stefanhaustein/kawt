// CardLayout.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-09-06 SH Changed indexOf for J9 compatibility
// 2000-06-27 SH Bug in getMinimumLayoutSize fixed
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

/** A card layout displays the content of the container as
    a stack of cards, where only one card (Componet) is visible,
    the other cards are hidden. The show method moves a Component
    to the top of the stack hiding the other Components.  */ 

public class CardLayout implements LayoutManager {

    Vector cards=new Vector();
    Vector names=new Vector();
    int selected;
    
    public void addLayoutComponent (String name, Component card) {
  	card.visible = cards.size() == selected;
	names.addElement (name);
	cards.addElement (card);
    }

    
    /** Shows the first child component. */
    
    public void first (Container parent) {
	show (0);
    }


    /** Shows the last child component */

    public void last (Container parent) {
	show (cards.size () - 1);
    }

    public void layoutContainer (Container parent) {
	for (int i = 0; i < cards.size(); i++){
	    Component c = (Component) cards.elementAt(i);
	    
	    Insets insets = parent.getInsets ();

	    c.setBounds (insets.left, insets.top,
			 parent.w - c.x - insets.right,
			 parent.h - c.y - insets.bottom);
	}
    }
    

    /** Returns the minimum layout size. The minimum layout size of a
	card layout is the maximum width and height of the minimum
	layout sizes of all child components */

    public Dimension minimumLayoutSize (Container parent) {
	
	int w=0;
	int h=0;
	
	for (int i=0;i<cards.size();i++){
	    Component c = (Component) cards.elementAt(i);
	    Dimension d = c.getMinimumSize();

	    if(w<d.width) w=d.width;
	    if(h<d.height) h=d.height;
	}
	return new Dimension (w,h);
    }

    
    /** Shows the next card */

    public void next (Container parent) {
	if (selected < cards.size ()-1)
	    show (selected + 1);
	else 
	    show (0);
    }


    /** Shows the previous card */

    public void previous (Container parent) {
	if (selected > 0)
	    show (selected - 1);
	else 
	    last (parent);
    }

    
    /** Shows the card with the given name (assigned in Container.add) */
    
    public void show (Container parent, String name){
	// changes for J9 compatibility
	show (names.indexOf (name, 0));
	// end of J9 changes
	
	// before J9 changes
	//show (names.indexOf (name));
    }


    void show (int i) {
	if (i == -1 || i == selected || i >= cards.size ()) return;
	((Component) cards.elementAt(selected)).visible = false;
	selected=i;
	((Component) cards.elementAt(selected)).setVisible (true);
    }    

    public void removeLayoutComponent (Component card) {
	// changes for J9 compatibility
	int i=cards.indexOf (card, 0);
	// end of J9 changes
	
	// before J9 changes
	//int i=cards.indexOf (card);

	if (i==selected) show (i > 0 ? i : 0);
  	cards.removeElementAt(i);
	names.removeElementAt(i);
	if (selected > i) selected--;
    }
}
