// Dialog.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-06-25 SH/MK drawRect changes (see Graphics)
// 2000-05-20 SH Modal dialogs enabled
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

public class Dialog extends Window {

    String title;
    boolean modal;
    //   Object mutex;


    public Dialog (Frame w) {
	this (w, "", false);
    }

    
    public Dialog (Frame w, String t) {
	this (w, t, false);
    }

    public Dialog (Frame w, String t, boolean modal) {
	super (w);
	this.modal = modal;
	setTitle (t);
    }


    public Dimension getMinimumSize () {
	int minH = super.getMinimumSize ().height;
	if (minH > (2 * owner.h) / 3) 
	    minH = owner.h; 
	return new Dimension (owner.w, minH);
    }
    

    public Insets getInsets () {
	return new Insets (16, 4, 4, 4);
    }

    public void paint (Graphics g) {
	
	String s = title;
	if (s == null) s = "No Title";
	
	g.setFont (g.getFont ().deriveFont (Font.BOLD));
      
	g.setColor (SystemColor.activeCaption);
	g.drawRoundRect (0, 0, w-1, h-1, 3, 3);
	g.drawRoundRect (1, 1, w-3, h-3, 2, 2);
	g.fillRect (2,2,w-4,12);
	
	g.setColor (Color.white);
	g.drawString (s, (w - g.getFontMetrics ().stringWidth (s)) / 2, 12);
    }	
    

    public void setVisible (boolean vis) {
	if (vis == visible) return;

	super.setVisible (vis);

	if (modal) {
	    if (vis) {
		//		if (Thread.currentThread () instanceof KawtThread) {
		//    mutex = Toolkit.kawtLock;

		    if (Thread.currentThread () == Toolkit.kawtThread) {
			Toolkit.kawtThread.leave = true; 

			KawtThread nkt = new KawtThread ();
			Toolkit.kawtThread = nkt;
			nkt.start ();
		    }
		    //		}
		    //else mutex = new Object ();

		synchronized (Toolkit.kawtLock) {
		    try {
			Toolkit.kawtLock.wait ();
		    }
		    catch (InterruptedException e) {
			throw new RuntimeException ("Error: "+e);
		    }
		}
	    }
	    else { 
		// if mutex is the dispatchLock, we are already
		// synced, and another sync does not harm. in that
		// case, the notification will be delayed until
		// eventQueue.dispatch has finished... :-)
		synchronized (Toolkit.kawtLock) {
		    Toolkit.kawtLock.notify ();
		}
	    }


	    /*
	    KawtThread kawtThread = Toolkit.kawtThread;

	    if (vis) {
		// show modal dialog: 
		//   1. if current thread is event dispatcher,
		//      start a new event dispatcher
		//   2. save current thread in "blocking"
		//   3. put it in "wait" mode

		blocking = Thread.currentThread ();
		
		synchronized (blocking) {
		
		    if (blocking instanceof KawtThread) {
			
			KawtThread current = (KawtThread) blocking;
			
			if (kawtThread == current) {
			    KawtThread nkt = new KawtThread ();
			    Toolkit.kawtThread = nkt;
			    nkt.start ();
			    			
			    // allow thread waiting in processEvent to cont.
			
			    kawtThread.type = 0;
			}
			else  // ok, simulate boiling out but don't
			    current.type = 'y';
		    
			current.notify ();
		    
			while (current.type != 'x') {
			    try {
				current.wait ();
			    }
			    catch (InterruptedException e) {}
			}
		    }
		    else {
			try {
			    blocking.wait ();
			}
			catch (InterruptedException e) {
			    throw new RuntimeException (e.toString ());
			}
		    }
		}
	    }
	    else {
		synchronized (blocking) {
		    if (blocking instanceof KawtThread) {
			KawtThread kBlocking = (KawtThread) blocking;

			kBlocking.type = 'x';
			kBlocking.notify ();
			while (kBlocking.type != 'y') {
			    try {
				kBlocking.wait ();
			    }
			    catch (InterruptedException e) {}
			}
		    }
		    else 
			blocking.notify ();

		    blocking = null;
		}
	    }
	    */

	} 
    }

    
    public void setTitle (String t) {
	title = t;
	repaint ();
    }
}
