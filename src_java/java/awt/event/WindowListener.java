// WindowListener.java -R0996
//
// 2000-09-08 MK Added new Licensetext
//	
//#include ..\..\..\license.txt
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


package java.awt.event;

import java.util.EventListener;

  /**
   * The listener interface for receiving window events. The class that is 
   * interested in processing a window event either implements this interface 
   * (and all the methods it contains) or extends the abstract WindowAdapter class 
   * (overriding only the methods of interest). The listener object created from that
   * class is then registered with a Window using the winodw's addWindowListener 
   * method. When the window's status changes by virtue of being opened, closed, 
   * activated or deactivated, iconified or deiconified, the relevant method in 
   * the listener object s invoked, and the WindowEvent is passed to it.
   *
   * @author  Stefan Haustein / Michael Kroll
   */


public interface WindowListener extends EventListener {
    
    /**
     * Invoked when the window is set to be the user's active window, 
     * which means the window (or one of its subcomponents) will receive 
     * keyboard events.
     */
    
    public void windowActivated (WindowEvent e); 
    
    /**
     * Invoked when a window has been closed as the result of calling dispose on the window.
     */
    
    public void windowClosed (WindowEvent e); 
    
    /**
     * Invoked when the user attempts to close the window from the window's system menu.
     */
    public void windowClosing (WindowEvent e); 
    
    /**
     * Invoked when a window is no longer the user's active window, which means 
     * that keyboard events will no longer be delivered to the window or 
     * its subcomponents.
     */
    public void windowDeactivated (WindowEvent e); 
    
    /**
     * Invoked when a window is changed from a minimized to a normal state.
     */
    public void windowDeiconified (WindowEvent e);
    
    /**
     * Invoked when a window is changed from a normal to a minimized state.
     */
    public void windowIconified (WindowEvent e); 
    
    /**
     * Invoked the first time a window is made visible.
     */
    public void windowOpened (WindowEvent e); 
}


/*
 * $Log: WindowListener.java,v $
 * Revision 1.2  2001/08/28 23:29:11  mkroll
 * Added EventListener for J2SE compatibility.
 * Changed the Listeners accordingly.
 *
 */

