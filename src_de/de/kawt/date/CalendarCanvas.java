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

public class CalendarCanvas extends Canvas implements MouseListener {
    
    Calendar calendar = Calendar.getInstance ();

    private static String[] days
	= {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};

    private static int daysInMonth[] 
	= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};    

    
    int cellWidth;
    int cellHeight;
    int totalWidth;
    int totalHeight;
    int left;
    int top;
    int firstDay;

    public CalendarCanvas () {
	addMouseListener (this);
    }

    public void paint(Graphics g) {

	cellWidth = (getWidth() - 1) / 7;
        cellHeight = (getHeight() - 1) / 7;
	
        totalWidth = 7 * cellWidth + 1;
        totalHeight = 7 * cellHeight + 1;

	left = (getWidth() - totalWidth) / 2;
	top = (getHeight() - totalHeight) / 2;

	g.translate (left, top);
	
	for (int i = 0; i < 8; i++) 
	    g.drawLine 
		(cellWidth * i, cellHeight, 
		 cellWidth * i, 7 * cellHeight - 1);
	
	for (int i = 1; i < 8; i++) 
	    g.drawLine
		(0, cellHeight * i, 
		 totalWidth - 1, cellHeight * i);
	
	FontMetrics font = g.getFontMetrics();
	int textHeight = font.getHeight();
	int ascent = font.getAscent ();

	for (int i = 0; i < 7; i++) {
	    String name = days [i];
	    int textWidth = font.stringWidth(name);
	    g.drawString
		(name,  cellWidth * i + (cellWidth - textWidth) / 2, 
		 cellHeight - 1);
	}
	
	
	int day = calendar.get (Calendar.DAY_OF_MONTH);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
	firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	calendar.set(Calendar.DAY_OF_MONTH, day);

	int dayOfWeek = firstDay;

	int days = daysInMonth (calendar);
	int weekOfMonth = 1;
	
	for (int i = 1; i <= days; i++) {
	    String s = String.valueOf(i);
	    int textWidth = font.stringWidth(s);
	    int cellLeft = cellWidth * dayOfWeek;
	    int cellTop = cellHeight * weekOfMonth;
	    
	    if (i == day) {
		g.fillRect (cellLeft, cellTop, 
			    cellWidth, cellHeight); 
		g.setColor (Color.white);
	    }

	    g.drawString (s, 
			  cellLeft + (cellWidth - textWidth) / 2, 
			  cellTop + ascent + (cellHeight - textHeight) / 2);
	    
	    g.setColor (Color.black);

	    dayOfWeek++;
	    if (dayOfWeek == 7) {
		weekOfMonth++;
		dayOfWeek = 0;
	    }
	}
    }
    
    public static int daysInMonth (Calendar calendar) {
	int year = calendar.get (Calendar.YEAR);
	int month = calendar.get (Calendar.MONTH);
	int days = daysInMonth [month-Calendar.JANUARY];
	if (month == Calendar.FEBRUARY 
	    && (year % 4 == 0 
		&& (!(year % 100 == 0) || (year % 400 == 0))))
	    days++;

	return days;
    }



    public void setDate (Date date) {
	calendar.setTime (date);
	repaint ();
    }


    public Calendar getCalendar () {
	return calendar;
    }


    public Date getDate () {
	return calendar.getTime ();
    }


    public Dimension getPreferredSize () {

	
	FontMetrics fm = Toolkit.getDefaultToolkit ().getFontMetrics 
	    (getFont ());

	return new Dimension ((fm.stringWidth ("88") + 1) * 7 + 1, 
			      (fm.getHeight () + 1) * 7 + 1);

    }

    public void mouseEntered (MouseEvent ev) {}
    public void mouseExited (MouseEvent ev) {}
    public void mousePressed (MouseEvent ev) {}
    public void mouseReleased (MouseEvent ev) {}

    public void mouseClicked (MouseEvent ev) {
	int x = (ev.getX () - left) / cellWidth;
	int y = (ev.getY () - top) / cellHeight - 1;

	int index = x + 7 * y - firstDay + 1;

	if (index > 0 && index <= daysInMonth (calendar))
	    calendar.set (Calendar.DAY_OF_MONTH, index);

	repaint ();
    }

}
