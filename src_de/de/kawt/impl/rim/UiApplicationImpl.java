package de.kawt.impl.rim;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import java.awt.event.KeyEvent;
import java.awt.*;

public class UiApplicationImpl extends UiApplication implements Runnable, ThumbListener, KeyListener  {

    int timer;
    FullScreenImpl screen = new FullScreenImpl ();
   // EventQueue eventQueue;

    public UiApplicationImpl () {
    }

    synchronized public void activate() {
        pushScreen(screen);
        GraphicsImpl.rimGraphics = screen.getGraphics (screen);
      //  eventQueue = Toolkit.getDefaultToolkit ().getSystemEventQueue ();
        addKeyListener (this);
        addThumbListener (this);
        notify ();
    }


    public void run () {
       enterEventDispatcher ();
    }


    public boolean keyDown(int status, int time, char rimKey) {

        int type;
        int keyCode = KeyEvent.VK_UNDEFINED;
        char keyChar = KeyEvent.CHAR_UNDEFINED;

        if (rimKey < 32) {
            keyCode = rimKey;
            type = KeyEvent.KEY_PRESSED;
        }
        else { 
            keyChar = rimKey;
            type = KeyEvent.KEY_TYPED;
        }  
              
                
        Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent (new KeyEvent 
                 (null,
                  type, 0, 0,
                  keyCode, keyChar));
                  
        return true;
    } 

 
    public boolean keyRepeat(int status, int time, char key) {
        return keyDown (status, time, key);
    }
        

    public boolean keyStatus(int status, int time, char key) {
        return false;
    } 


    public boolean keyUp(int status, int time, char key) {
        return true;
    }



    public boolean thumbClick(int status, int time) {
        timer = time;
        /*
       Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent 
           (new KeyEvent 
                 (null,
                  KeyEvent.KEY_PRESSED, 0, 0,
                  KeyEvent.VK_ACCEPT, KeyEvent.CHAR_UNDEFINED));*/
        return true; 
    }
    
    public boolean thumbRollDown(int status, int time, int amount) {
       Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent 
           (new KeyEvent 
                 (null,
                  KeyEvent.KEY_PRESSED, 0, 0,
                  timer == 0 ? KeyEvent.VK_DOWN : KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED));

       if (timer > 0) timer = -1;
       return true;
    } 
    
    public boolean thumbRollUp(int status, int time, int amount) { 
       Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent 
           (new KeyEvent 
                 (null,
                  KeyEvent.KEY_PRESSED, 0, 0,
                  timer == 0 ? KeyEvent.VK_UP : KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));

       if (timer > 0) timer = -1;
       return true;
    }
    

    public boolean thumbUnclick(int status, int time) {
        if (timer > 0) 
          Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent 
           (new KeyEvent 
                 (null,
                  KeyEvent.KEY_PRESSED, 0, 0,
                  time - timer < 50 
                  ? KeyEvent.VK_ACCEPT 
                  : KeyEvent.VK_CANCEL,
                  KeyEvent.CHAR_UNDEFINED));

        timer = 0;
        return true;
    }
}
