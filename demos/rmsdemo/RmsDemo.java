// kAWT - Kilobyte Abstract Window Toolkit
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

import java.awt.*;
import java.awt.event.*;

import javax.microedition.rms.*;

import de.kawt.*;

public class RmsDemo extends Frame implements ActionListener {
    
    RecordStore recordStore;
    
    String recStoreName;
    
    List stringEntries = new List ();
    TextField stringData = new TextField ();
    TextField rmsName = new TextField ();

    public RmsDemo () {
        setTitle ("kAWT RMS-Sample");
        
        Panel btnPanel = new Panel (new FlowLayout ());
        
        Button open = new Button ("open");
        open.setActionCommand ("open");
        open.addActionListener (this);

        Button add = new Button ("add");
        add.setActionCommand ("add");
        add.addActionListener (this);

        Button del = new Button ("del");
        del.setActionCommand ("del");
        del.addActionListener (this);

        Button exit = new Button ("Exit");
        exit.setActionCommand ("exit");
        exit.addActionListener (this);

        Panel storeName = new Panel (new BorderLayout ());
        storeName.add ("West", new Label ("RS Name:"));
        storeName.add ("Center", rmsName);
        
        btnPanel.add (open);
        btnPanel.add (add);
        btnPanel.add (del);
        btnPanel.add (exit);


        Panel centerPanel = new Panel (new BorderLayout ());
        Panel northPanel = new Panel (new BorderLayout ());

        northPanel.add ("West", new Label ("String to store:")); 
        northPanel.add ("Center", stringData);

        centerPanel.add ("North", northPanel);

        centerPanel.add ("Center", stringEntries);

        add ("North", storeName);

        add ("Center", centerPanel);
        add ("South", btnPanel);

        pack ();
        show ();
    }
    
    public void actionPerformed (ActionEvent ae) {
        try {
            String cmd = ae.getActionCommand ();
            if (cmd.equals ("add")) {
                if (recStoreName != null  && recordStore != null) {
                    stringEntries.add (stringData.getText ());
                    recordStore.addRecord (stringData.getText ().getBytes (), 0, stringData.getText ().length ());
                }
            } else if (cmd.equals ("exit")) {
                if (recordStore != null)
                    recordStore.closeRecordStore ();
                    System.exit (0);
            } else if (cmd.equals ("del")) {
                int sel = stringEntries.getSelectedIndex ();
                if (sel > -1) {
                    stringEntries.remove (sel);
                    recordStore.deleteRecord (sel+1); 
                }
            } 
            else if (cmd.equals ("open")) {
                recStoreName = rmsName.getText ();
                if (recStoreName.length () > 0) {
                    try {
                        if (recordStore != null) {
                          recordStore.closeRecordStore ();
                          recordStore = null;
                        }
                        recordStore = RecordStore.openRecordStore (recStoreName, true);
                        if (recordStore != null) {
                            for (int i = 1; i <= recordStore.getNumRecords (); i++) {
                                byte[] data = recordStore.getRecord (i);
                                stringEntries.add (new String (data));
                            }
                        } else {
                            stringEntries.add ("Error opening RecordStore.");
                        }
                        
                    } catch (Exception e) {
                        System.out.println (e.toString ());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println (e.toString ());
        }
    }

    
    public static void main (String [] args) {
        new RmsDemo ();
    }
} // RmsDemo
