// Container.java
//
// 2000-11-03 SH Please consult cvs log for newer changes!
//
// 2000-09-23 SH background/foreground fixes
// 2000-09-08 MK Added new Licensetext
// 2000-08-25 removeComponent fixed
// 2000-08-14 SH added several methods on request
// 2000-08-06 SH getComponentCount, and getInsets added
// 2000-05-20 SH isAncestorOf added
// 2000-05-01 SH foreground/background support completed
// 2000-03-06 SH add (Component, Object) fixed
// 2000-01-30 SH add (Component, Object) and isVisible () added
// 1999-11-26 SH added invalidate to add method 
//           (bug rep. by Morten Green Hermansen)
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


/** A special component that can contain other components. */
    

public class Container extends Component {

    int childCount;
    Component [] children;

    //    Vector constraints;
    LayoutManager layoutManager = FlowLayout.defaultLayout;

    /** Creates a new container with a default flow layout. */

    public Container() {
    }


    /** Creates a new container with the given layoutManager.  If the
	parameter is set to null, no layoutmanager is associated with
	this container and the child components must be positioned
	manually using e.g. setBounds. */

    public Container (LayoutManager layoutManager) {
        this ();
        this.layoutManager = layoutManager;
    }


    /** Adds the given component to this container. */

    public Component add (Component c) {
        addImpl (c, null, -1);
        return c;
    }

    
    /** Adds the givn component at the given position. */

    public Component add (String where, Component c) {
        addImpl (c, where, -1);
        return c;
    }


    /** Adds the given component with the given constraints to
	this container */

    public void add (Component c, Object where) {
        addImpl (c, where, -1);
    }


    /** This add method encapsulates the real implementation and is
        called by all other add methods. */

    public synchronized void addImpl (Component c, 
				      Object constraints, int index) {

	if (index == -1) index = childCount;

	if (children == null) 
	    children = new Component [5];
	    
	if (childCount >= children.length) {
	    Component [] old = children;
	    children = new Component [childCount + 5];
	    System.arraycopy (old, 0, children, 0, childCount);
	}

	for (int i = childCount; i > index; i--) 
	    children [i] = children [i-1];
	
	children [index] = c;
	childCount++;

	c.parent = this;
	c.propagateProperties ();

	if (c.w <= 0 && c.h <= 0) 
	    c.setSize (c.getMinimumSize ());

	if (layoutManager != null) 
	    layoutManager.addLayoutComponent ((String) constraints, c);

	/*
	if (c instanceof TextComponent) {
	    Component pp = this;
	    while (pp.parent != null) 
		parent = pp.parent;
	    if (pp instanceof Window && !pp.visible) {
		Window w = (Window) pp;
		if (w.focus == w) {
		    w.focus = this;
		    w.hasFocus = false;
		    hasFocus = true;
		}
	    } 
	    }
	*/

        invalidate ();
    }


    public Component getComponentAt (int px, int py) {
	//System.out.println ("gca "+px+"/"+py+" "+ getClass ());
	if (!contains (px, py)) return null;
	for (int i = childCount; --i >= 0;) {
            Component c = children [i];
	    System.out.println ("-try "+c.getClass() + "v "+ visible);
            if (c.visible && c.contains (px - c.x, py - c.y))
		return c;
        }
        return this;
    }


    /* deprecated 
	returns the component at the given position. If there
	is no subcomponent at the given position, this is returned. 

    public Component findComponentAt (int px, int py) { 
        
        if ((!visible) || !contains (px, py)) return null;

        for (int i = childCount; --i >= 0;) { 
            Component c = children [i]; 
            c = c.findComponentAt (px - c.x, py - c.y);
            if (c != null) return c; 
        } 
        return  this;
    }
*/
    /** Returns the component at the given index. */
    
    public Component getComponent (int i) {
        return children [i];
    }

    /** Returns the number of child components in this container. */

    public int getComponentCount () {
        return childCount;
    }
    

    /** Returns a Component array containing all child components of
	this container. */

    public Component [] getComponents () {
        Component [] result = new Component [childCount];
	if (childCount > 0) 
	    System.arraycopy (children, 0, result, 0, childCount); 
        return result;
    }


    /** Returns the index of the given component or -1 if not contained. */
   
    int indexOf (Component c) {
	for (int i = 0; i < childCount; i++) 
	    if (children [i] == c) return i;

	return -1;
    }


    public Insets getInsets () {
        return new Insets (0, 0, 0, 0);
    }

    /** Returns the minimum size of this container, depending 
	on the current layout manager. */ 

  
    public Dimension getMinimumSize () {
        return layoutManager != null 
            ? layoutManager.minimumLayoutSize (this)
            : getSize ();
    }


    /** Returns false */

    public boolean isFocusTraversable () {
        return false;
    }

    /** returns if this container contains the given component or any
        container containing the given component. */

    public boolean isAncestorOf (Component c) {
        do {
            if (c.parent == this) return true;
            c = c.parent;
        }
        while (c != null);
        return false;
    }
    

    /** Returns the LayoutManger assigned to this Container. */
        
    public LayoutManager getLayout () {
        return layoutManager;
    }


    /** Paints this component and all child components. */

    public void paintAll (Graphics g) {
        if (visible && g != null) {
            
            if (!valid) doLayout ();

	    //g.setColor (foreground);
            paint (g.create ()); // g may be changed in paint

            //    paintComponents (g);
            //  save some stack space (uahrg!)

            for (int i = 0; i < childCount; i++) {
                Component child = children [i];
                if (child.visible && g.hitClip 
                    (child.x, child.y, child.w, child.h)) {
                    
                    Graphics g2 = g.create 
                        (child.x, child.y, child.w, child.h);
		    
		    if (fontMetrics != child.fontMetrics) 
			setFont (child.fontMetrics.font);

                    if (child.background != background) 
                        child.update (g2);
                    else {
			if (child.foreground != foreground) 
			    g2.setColor (child.foreground);
                        child.paintAll (g2);
		    }
		}
	    }
	}
    }


    /** Paints all child components. May be removed from kAWT, so
        please do not use this function. */
             
    public void paintComponents (Graphics g) {
        
	for (int i = 0; i < childCount; i++) {
	    Component child = children [i];
            child.paintAll 
                (g.create (child.x, child.y, child.w, child.h));
        }
    }


    /** propagates changed properties to children */

    void propagateProperties () {
	super.propagateProperties ();
	for (int i = 0; i < childCount; i++) {
	    children [i].propagateProperties ();
	}
    }
    

    /** Removes all components from this container. */

    public void removeAll () {
        for (int i = childCount-1; i >= 0; i--) 
            remove (i);
    }


    /** Removes the component at the given index from this container. */
 
    public void remove (int i) {
	Component c = children [i];
	childCount--;
	while (i < childCount) {
	    children [i] = children [i+1];
	    i++;
	}
	children[childCount] = null;
	if (layoutManager != null) 
            layoutManager.removeLayoutComponent (c);
	invalidate ();
    }


    /** Removes the given component from this container. */

    public void remove (Component c) {
	int i = indexOf (c);
	if (i != -1) remove (i);

    }


    /** Sets the given layout manager. */

    public void setLayout (LayoutManager mgr) {
	layoutManager = mgr;
        invalidate ();
    }


    /** This method clears the background using the current background
	color and calls paintAll.  Attention: If only one child
	component hits the current clip are, this child is repaint
	without the paintAll indirection. */
	

    public void update (Graphics g) {
        
        // avoid flickering if possible by checking if the whole clipping
        // area is covered by a single child. if so, call update of this
        // child instead of super.update....
	
        Rectangle clip = g.getClipBounds ();

        for (int i = childCount-1; i >= 0; i--) {
            Component child = children [i];
            if (!child.visible) continue;
            
            if (clip.x >= child.x 
                && clip.y >= child.y 
                && clip.x + clip.width <= child.x + child.w 
                && clip.y + clip.height <= child.y + child.h) {
                
                g.translate (child.x, child.y);

		if (child.fontMetrics != fontMetrics) 
		    g.setFont (child.fontMetrics.font);

		child.update (g);
                return;
            }
        }
	
        g.setColor (background);
        g.fillRect (0, 0, w, h);
	g.setColor (foreground);
        paintAll (g);
    }


    /** Forces new layout of this container. Call validate in order to
	perform a new layout only when really neede. */

    public void doLayout () {

        if (layoutManager != null) 
	    layoutManager.layoutContainer (this);
        
        for (int i = 0; i < childCount; i++) 
            children [i].doLayout ();
        
        valid = true;
    }

    
    /** Calls doLayout if the current layout is not valid. (inValidate
        has been called before). */


    public void validate () {
        if (!valid) {
            doLayout (); 
            repaint ();
        }
    }
}
