// Menu.java
//
// 2000-10-15 SH Focus handling added for RIM support
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

public class Menu extends MenuItem {

    List list;
    Window win;
    Menu parent;

    public Menu (String label) {
        super (label);
        list = new List (this);
    }

    
    public MenuItem add (MenuItem item) {
        
        list.addObject (item, -1);
        return item;
    }


    /** internal method that actually shows the menu by creating a
        window */


    void show (int x, int y, boolean byKey) {
        if (win == null) { 
            win = new Window (null);
            
            win.isMenu = true;
            win.add ("Center", list);
            win.pack ();
            list.y = -2;
            win.h -= 2;
        }
	if (byKey) {
	    list.requestFocus ();
	    list.focus = 0;
	}
        
        Dimension scr = Toolkit.defaultToolkit.getScreenSize ();
        
        win.x = x-1;
        win.y = y-1;
        
        if (win.x + win.w > scr.width) 
            win.x = scr.width - win.w;
        if (win.y + win.h > scr.height) 
            win.y = scr.height - win.h;
        if (win.y < 0) win.y = 0;
        if (win.x < 0) win.x = 0;

        win.show ();    
    }
}

