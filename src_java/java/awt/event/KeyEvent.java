// KeyEvent.java 
//
// 2000-09-08 MK Added new Licensetext
//	
//#include ..\..\..\license.txt
//
// kAWT 
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

import java.awt.*;

public class KeyEvent extends InputEvent {

    public static final char CHAR_UNDEFINED          = 0x0ffff; 

    public static final int KEY_FIRST                = 400; 
    public static final int KEY_LAST                 = 402; 
    public static final int KEY_TYPED                = 400; 
    public static final int KEY_PRESSED              = 401; 
    public static final int KEY_RELEASED             = 402; 

    public static final int VK_UNDEFINED             = 0; 

    public static final int VK_0                     = 0x30; 
    public static final int VK_1                     = 0x31; 
    public static final int VK_2                     = 0x32; 
    public static final int VK_3                     = 0x33; 
    public static final int VK_4                     = 0x34; 
    public static final int VK_5                     = 0x35; 
    public static final int VK_6                     = 0x36; 
    public static final int VK_7                     = 0x37; 
    public static final int VK_8                     = 0x38; 
    public static final int VK_9                     = 0x39; 

    public static final int VK_A                     = 0x41; 
    public static final int VK_ADD                   = 0x6B; 
    public static final int VK_ACCEPT                = 0x001E; 
    public static final int VK_ALT                   = 0x12; 

    public static final int VK_B                     = 0x42; 
    public static final int VK_BACK_SLASH            = 0x5C; 
    public static final int VK_BACK_QUOTE            = 0xC0; 
    public static final int VK_BACK_SPACE            = '\b'; 
    public static final int VK_BRACELEFT             = 0xa1; 
    public static final int VK_BRACERIGHT            = 0xa2; 

    public static final int VK_C                     = 0x43; 
    public static final int VK_CAPS_LOCK             = 0x14; 
    public static final int VK_CANCEL                = 0x03; 
    public static final int VK_CIRCUMFLEX            = 0x0202; 
    public static final int VK_CLEAR                 = 0x0C; 
    public static final int VK_COMMA                 = 0x2C; 
    public static final int VK_COMPOSE               = 0xFF20; 
    public static final int VK_CONTROL               = 0x11; 
    public static final int VK_CLOSE_BRACKET         = 0x5D; 
    public static final int VK_CONVERT               = 0x001C; 
    public static final int VK_COPY                  = 0xFFCD; 
    public static final int VK_CUT                   = 0xFFD1; 
    
    public static final int VK_CODE_INPUT            = 0x0102; 
    public static final int VK_COLON                 = 0x0201; 

    public static final int VK_D                     = 0x44; 
    public static final int VK_DECIMAL               = 0x6E; 
    public static final int VK_DIVIDE                = 0x6F; 
    public static final int VK_DELETE                = 0x7F; 
    public static final int VK_DEAD_ACUTE            = 0x81; 
    public static final int VK_DEAD_ABOVEDOT         = 0x86; 
    public static final int VK_DEAD_ABOVERING        = 0x88; 
    public static final int VK_DEAD_BREVE            = 0x85; 
    public static final int VK_DEAD_CIRCUMFLEX       = 0x82; 
    public static final int VK_DEAD_CARON            = 0x8a; 
    public static final int VK_DEAD_CEDILLA          = 0x8b;
    public static final int VK_DEAD_DIAERESIS        = 0x87; 
    public static final int VK_DEAD_DOUBLEACUTE      = 0x89; 
    public static final int VK_DEAD_GRAVE            = 0x80; 
    public static final int VK_DEAD_IOTA             = 0x8d; 
    public static final int VK_DEAD_MACRON           = 0x84; 
    public static final int VK_DEAD_OGONEK           = 0x8c; 
    public static final int VK_DEAD_SEMIVOICED_SOUND = 0x8f; 
    public static final int VK_DEAD_TILDE            = 0x83; 
    public static final int VK_DEAD_VOICED_SOUND     = 0x8e; 
    public static final int VK_DOLLAR                = 0x0203; 
    public static final int VK_DOWN                  = 0x28; 

    public static final int VK_E                     = 0x45; 
    public static final int VK_ENTER                 = '\n'; 
    public static final int VK_ESCAPE                = 0x1B; 
    public static final int VK_END                   = 0x23; 
    public static final int VK_EQUALS                = 0x3D; 
    public static final int VK_EURO_SIGN             = 0x0204; 
    public static final int VK_EXCLAMATION_MARK      = 0x0205; 


    public static final int VK_F                     = 0x46; 
    public static final int VK_FINAL                 = 0x0018;    
    public static final int VK_F1                    = 0x70; 
    public static final int VK_F2                    = 0x71; 
    public static final int VK_F3                    = 0x72; 
    public static final int VK_F4                    = 0x73; 
    public static final int VK_F5                    = 0x74; 
    public static final int VK_F6                    = 0x75; 
    public static final int VK_F7                    = 0x76; 
    public static final int VK_F8                    = 0x77; 
    public static final int VK_F9                    = 0x78; 
    public static final int VK_F10                   = 0x79; 
    public static final int VK_F11                   = 0x7A; 
    public static final int VK_F12                   = 0x7B; 
    public static final int VK_F13                   = 0xF000; 
    public static final int VK_F14                   = 0xF001;  
    public static final int VK_F15                   = 0xF002;  
    public static final int VK_F16                   = 0xF003;  
    public static final int VK_F17                   = 0xF004;  
    public static final int VK_F18                   = 0xF005;  
    public static final int VK_F19                   = 0xF006;   
    public static final int VK_F20                   = 0xF007;   
    public static final int VK_F21                   = 0xF008;   
    public static final int VK_F22                   = 0xF009;   
    public static final int VK_F23                   = 0xF00A;   
    public static final int VK_F24                   = 0xF00B;  

    public static final int VK_G                     = 0x47; 
    public static final int VK_GREATER               = 0xa0; 

    public static final int VK_H                     = 0x48; 
    public static final int VK_HELP                  = 0x9C; 
    public static final int VK_HOME                  = 0x24; 
    
    public static final int VK_I                     = 0x49; 
    public static final int VK_INSERT                = 0x9B; 

    public static final int VK_J                     = 0x4A;

    public static final int VK_K                     = 0x4B; 
    public static final int VK_KANA                  = 0x0015; 
    public static final int VK_KANJI                 = 0x0019;

    public static final int VK_L                     = 0x4C; 
    public static final int VK_LEFT                  = 0x25;

    public static final int VK_M                     = 0x4D; 
    public static final int VK_META                  = 0x9D;
    public static final int VK_MINUS                 = 0x2D;
    public static final int VK_MULTIPLY              = 0x6A; 
    public static final int VK_MODECHANGE            = 0x001F; 

    public static final int VK_N                     = 0x4E; 
    public static final int VK_NONCONVERT            = 0x001D; 
    public static final int VK_NUM_LOCK              = 0x90; 
    public static final int VK_NUMPAD0               = 0x60; 
    public static final int VK_NUMPAD1               = 0x61;
    public static final int VK_NUMPAD2               = 0x62;
    public static final int VK_NUMPAD3               = 0x63;
    public static final int VK_NUMPAD4               = 0x64;
    public static final int VK_NUMPAD5               = 0x65;
    public static final int VK_NUMPAD6               = 0x66;
    public static final int VK_NUMPAD7               = 0x67;
    public static final int VK_NUMPAD8               = 0x68;
    public static final int VK_NUMPAD9               = 0x69;

    public static final int VK_O                     = 0x4F;
    public static final int VK_OPEN_BRACKET          = 0x5B;

    public static final int VK_P                     = 0x50; 
    public static final int VK_PASTE                 = 0xFFCF; 
    public static final int VK_PAGE_DOWN             = 0x22;
    public static final int VK_PAGE_UP               = 0x21;
    public static final int VK_PAUSE                 = 0x13;
    public static final int VK_PERIOD                = 0x2E;
    public static final int VK_PRINTSCREEN           = 0x9A;
    public static final int VK_PROPS                 = 0xFFCA; 
    
    public static final int VK_Q                     = 0x51;
    public static final int VK_QUOTE                 = 0xDE;

    public static final int VK_R                     = 0x52;
    public static final int VK_RIGHT                 = 0x27;

    public static final int VK_S                     = 0x53; 
    public static final int VK_SEMICOLON             = 0x3B; 
    public static final int VK_SLASH                 = 0x2F; 
    public static final int VK_SHIFT                 = 0x10; 
    public static final int VK_SPACE                 = 0x20; 
    public static final int VK_SEPARATER             = 0x6C; 
    public static final int VK_SUBTRACT              = 0x6D; 
    public static final int VK_SCROLL_LOCK           = 0x91; 

    public static final int VK_T                     = 0x54; 
    public static final int VK_TAB                   = '\t'; 

    public static final int VK_U                     = 0x55; 
    public static final int VK_UP                    = 0x26; 

    public static final int VK_V                     = 0x56;
    public static final int VK_W                     = 0x57;
    public static final int VK_X                     = 0x58;
    public static final int VK_Y                     = 0x59;
    public static final int VK_Z                     = 0x5A;


    int keyCode;
    char keyChar;

    public KeyEvent (Component source, int id, long when, 
		     int modifiers, int keyCode) {
	this (source, id, when, modifiers, keyCode, CHAR_UNDEFINED);
    } 
    

    public KeyEvent (Component source, int id, long when, int modifiers, 
		     int keyCode, char keyChar) {
	
	super (source, id, when, modifiers);
	
	this.keyCode = keyCode;
	this.keyChar = keyChar;
    }

    
    public char getKeyChar () {
	return keyChar;
    }

    
    public int getKeyCode () {
	return keyCode;
    }


    public static String getKeyText (int keyCode) {
	if (keyCode <= VK_A && keyCode >= VK_Z)
	    return "" + (char) (keyCode - VK_A + 65);
	if (keyCode <= VK_0 && keyCode >= VK_9)
	    return "" + (char) (keyCode - VK_0 + 48);
	
        if (keyCode >= VK_NUMPAD0 && keyCode <= VK_NUMPAD9) 
            return "NumPad-" 
		+ (char)(keyCode - VK_NUMPAD0 + '0');
	
        return "Unknown keyCode: 0x" + Integer.toString(keyCode, 16);
    }


    public static String getKeyModifiersText (int modifiers) {
	StringBuffer buf = new StringBuffer ();
	if ((modifiers & CTRL_MASK) != 0) 
	    buf.append ("Ctrl-");
	if ((modifiers & SHIFT_MASK) != 0)
	    buf.append ("Shift-");
	if ((modifiers & META_MASK) != 0) 
	    buf.append ("Meta-");
	if ((modifiers & ALT_MASK) != 0) 
	    buf.append ("Alt-");

	if (buf.length () == 0) return "";

	buf.setLength (buf.length ()-1);

	return buf.toString ();
    } 

    

    public boolean isActionKey() {
        switch (keyCode) {
	case VK_HOME:
	case VK_END:
	case VK_PAGE_UP:
	case VK_PAGE_DOWN:
	case VK_UP:
	case VK_DOWN:
	case VK_LEFT:
	case VK_RIGHT:
	    
	case VK_F1:
	case VK_F2:
	case VK_F3:
	case VK_F4:
	case VK_F5:
	case VK_F6:
	case VK_F7:
	case VK_F8:
	case VK_F9:
	case VK_F10:
	case VK_F11:
	case VK_F12:
	case VK_F13:
	case VK_F14:
	case VK_F15:
	case VK_F16:
	case VK_F17:
	case VK_F18:
	case VK_F19:
	case VK_F20:
	case VK_F21:
	case VK_F22:
	case VK_F23:
	case VK_F24:

	case VK_PRINTSCREEN:
	case VK_SCROLL_LOCK:
	case VK_CAPS_LOCK:
	case VK_NUM_LOCK:
	case VK_PAUSE:
	case VK_INSERT:
	    
	case VK_FINAL:
	case VK_CONVERT:
	case VK_NONCONVERT:
	case VK_ACCEPT:
	case VK_MODECHANGE:
	case VK_KANA:
	case VK_KANJI:

	case VK_CODE_INPUT:
	    
	case VK_COPY:
	case VK_CUT:
	case VK_PASTE:

	case VK_PROPS:
	    
	case VK_HELP:
	    return true;
        }
        return false;
    }

  
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
    

    public void setKeyChar(char keyChar) {
        this.keyChar = keyChar;
    }
}


/*
 * $Log: KeyEvent.java,v $
 * Revision 1.7  2001/08/30 15:09:51  mkroll
 * Changed CHAR_UNDEFINED to 0x0ffff.
 *
 * Revision 1.6  2001/08/28 22:06:16  haustein
 * removed two temp. variables
 *
 * Revision 1.5  2001/08/28 21:57:05  mkroll
 * Added additional VK constants and the methods setKeyCode(), setKeyChar()
 * and isActionKey().
 *
 * Revision 1.4  2001/08/27 23:31:17  haustein
 * keyevent constants added, log added
 *
 * Revision 1.3  2001/08/27 22:33:15  haustein
 * ComponentEvent added, ImputEvent upgraded to 996, AWTEvent fixed
 *
 * Revision 1.2  2001/08/23 19:35:06  mkroll
 * Changed KEY_TYPED, KEY_PRESSED and KEY_RELEASED from 0xxx to xxx values.
 *
 */



