// MenuBarComponent.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-06-29 SH  Version number 0.70 initial release 
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


/** internal class!!! */

class MenuBarComponent extends Component {
    
    MenuBar bar;
    Menu open;
    int selected = -1;
    StringBuffer offsets;

    MenuBarComponent (MenuBar bar) {
        this.bar = bar;
    }


    public Dimension getMinimumSize () {
        return new Dimension 
            (Toolkit.defaultToolkit.getScreenSize ().width, 
             fontMetrics.getHeight () + 3);
    }


    public void paint (Graphics g) {

        g.setColor (Color.black);
        g.drawLine (1, 0, w-3, 0);
        g.drawLine (1, h-2, w-2, h-2);
        g.drawLine (2, h-1, w-3, h-1);
        
        g.drawLine (0, 1, 0, h-3);
        g.drawLine (w-2, 1, w-2, h-2);
        g.drawLine (w-1, 2, w-1, h-3);
        
        g.setFont (g.getFont ().deriveFont (Font.BOLD));
        int x = 5;
        int lh = fontMetrics.getHeight ();

        offsets = new StringBuffer ();
        offsets.append ((char) x);

        for (int i = 0; i < bar.menus.size (); i++) {
            Menu m = (Menu) bar.menus.elementAt (i);

            String label = m.label;
            int w = g.getFontMetrics ().stringWidth (label)+10;     
            
            g.setColor (i == selected 
                        ? SystemColor.activeCaption 
                        : Color.white);
           
            g.fillRect (x, 1, w, h-3);  
            g.setColor (i == selected ? Color.white : Color.black);

            g.drawString (label, x+5, lh);
            
            x += w;
            offsets.append ((char) x);
        }
        g.setColor (Color.black);
    }


    void setSelected (int i, boolean byKey) {
        
        if (selected != i) {
            selected = i;
            Graphics g = getGraphics ();
            if (g != null) paint (g); else repaint ();
        }
 
        Menu toOpen = i == -1 ? null : (Menu) bar.menus.elementAt (i);

        if (open != toOpen && open != null) open.win.setVisible (false);

        open = toOpen;

        if (open != null) open.show ((int) offsets.charAt (i), h, byKey);
    }
                             

    public void dispatchEvent (AWTEvent event) {

        super.dispatchEvent (event);
	if (event.consumed) return;
        
        if (event.id == KeyEvent.KEY_PRESSED) {
	    KeyEvent ke = (KeyEvent) event;

	    switch (ke.getKeyCode ()) {
	    case KeyEvent.VK_UP:
	    case KeyEvent.VK_LEFT:
		selected--;
		if (selected < 0) selected = bar.menus.size ()-1;
		ke.consume ();
		repaint ();
		break;
	    case KeyEvent.VK_DOWN:
	    case KeyEvent.VK_RIGHT:
		selected++;
		if (selected >= bar.menus.size ()) selected = 0;
		ke.consume ();
		repaint ();
		break;
		
	    case KeyEvent.VK_ACCEPT:
	    case KeyEvent.VK_ENTER:
		setSelected (selected, true);
		ke.consume ();
	    }
	}
	else if (event.id == MouseEvent.MOUSE_DRAGGED || 
		 event.id == MouseEvent.MOUSE_PRESSED) {

	    MouseEvent me = (MouseEvent) event;

	    /*	    if (me.getID () != MouseEvent.MOUSE_DRAGGED 
		&& me.getID () != MouseEvent.MOUSE_PRESSED) return;
	    */

	    int xofs = 5;
	    int mx = me.getX ();
	    
	    Graphics g = getGraphics();
	    if (g == null) return;
	    
	    g.setFont (g.getFont ().deriveFont (Font.BOLD));
	    FontMetrics fm = g.getFontMetrics();
                
	    for (int i = 0; i < bar.menus.size(); i++) {
		
		int nextOfs = (int) offsets.charAt (i+1);
		
		if (mx >= xofs && mx < nextOfs) { 
		    setSelected (i, false);
		    return;
		}
		xofs = nextOfs;
	    }
	    setSelected (-1, false);
	}
    }
}
