package de.kawt.date;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class DateDialog extends Dialog implements ActionListener {

    CalendarComponent calendarComponent = new CalendarComponent ();
    Button okButton = new Button ("Ok");
    Button cancelButton = new Button ("Cancel");
    boolean ok;


    public DateDialog (Frame owner) {
	super (owner, "Set Date", true);
	Panel buttons = new Panel ();
	buttons.add (okButton);
	buttons.add (cancelButton);
	okButton.addActionListener (this);
	cancelButton.addActionListener (this);
	add ("Center", calendarComponent);
	add ("South", buttons);

	addWindowListener (new WindowAdapter () {
		public void windowClosing (WindowEvent e) {
		    setVisible (false);
		}
	    });

	pack ();
    }


    public Date getDate (Date d) {
	show ();
	return ok ? calendarComponent.getDate () : d;
    }


    public void actionPerformed (ActionEvent e) {
        ok = e.getSource () == okButton;
	setVisible (false);
    }
}

