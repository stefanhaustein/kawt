package de.kawt.date;

/**
 * Title:        Pommes
 * Description:  Patientenorientierte mobile medizinische Software
 * Copyright:    Copyright (c) 2001
 * Company:      Universität Dortmund, Lehrstuhl Informatik X
 * @author Jörg Pleumann
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CalendarComponent extends Panel implements ActionListener {

    Calendar calendar =  Calendar.getInstance ();
    //Choice monthChoice = new Choice ();
    Label yearMonthLabel = new Label ("9999");
    Button buttonPlus = new Button (">");
    Button buttonPlusPlus = new Button (">>");
    Button buttonMinus = new Button ("<");
    Button buttonMinusMinus = new Button ("<<");
    CalendarCanvas calendarCanvas = new CalendarCanvas ();

    public CalendarComponent () {
	
	super (new BorderLayout ());

	/*
	monthChoice.add ("Jan");	
	monthChoice.add ("Feb");

	monthChoice.add ("Mar");
	monthChoice.add ("Apr");
	monthChoice.add ("May");
	monthChoice.add ("Jun");
	monthChoice.add ("Jul");
	monthChoice.add ("Aug");
	monthChoice.add ("Sep");
	monthChoice.add ("Oct");
	monthChoice.add ("Nov");
	monthChoice.add ("Dec");
	monthChoice.addItemListener (this);
	*/
	Panel top = new Panel ();
	top.add (buttonMinusMinus);
	top.add (buttonMinus);
	//	top.add (monthChoice);
	top.add (yearMonthLabel);
	top.add (buttonPlus);
	top.add (buttonPlusPlus);
	buttonPlus.addActionListener (this);
	buttonMinus.addActionListener (this);
	buttonPlusPlus.addActionListener (this);
	buttonMinusMinus.addActionListener (this);

	add ("North", top);
	add ("Center", calendarCanvas);
	
	propagate ();
    }


    public void setDate (Date date) {
	calendar.setTime (date);
	propagate ();
    }


    void propagate () {
	calendar.set 
	    (Calendar.DAY_OF_MONTH,
	     calendarCanvas.getCalendar ().get 
	      (Calendar.DAY_OF_MONTH));

	calendarCanvas.setDate (calendar.getTime ());

	yearMonthLabel.setText (""+(calendar.get (Calendar.MONTH)-Calendar.JANUARY+1)+"/"+calendar.get(Calendar.YEAR));/*
	monthChoice.select 
	(calendar.get (Calendar.MONTH)-Calendar.JANUARY);*/
	repaint ();
    }

    
    public Date getDate () {

	calendar.set (Calendar.DAY_OF_MONTH, 
		      calendarCanvas.getCalendar().get 
		      (Calendar.DAY_OF_MONTH));
	
	return calendar.getTime ();
    }


    /*
    public void itemStateChanged (ItemEvent ev) {

	calendar.set (Calendar.MONTH,
		      monthChoice.getSelectedIndex () + Calendar.JANUARY);
	
	propagate ();
    }
    */

    public void actionPerformed (ActionEvent ev) {
	if (ev.getSource () == buttonPlusPlus)
	    calendar.set (Calendar.YEAR, calendar.get (Calendar.YEAR) + 1);
	else if (ev.getSource () == buttonPlus)
	    calendar.set (Calendar.MONTH, calendar.get (Calendar.MONTH) + 1);
	    else if (ev.getSource () == buttonMinusMinus)
	    calendar.set (Calendar.YEAR, calendar.get (Calendar.YEAR) - 1);
	else if (ev.getSource () == buttonMinus)
	    calendar.set (Calendar.MONTH, calendar.get (Calendar.MONTH) - 1);

	propagate ();
    }
}
