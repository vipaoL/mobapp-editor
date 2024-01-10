/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author vipaol
 */
public class Main extends MIDlet {
    
    private static Display display;
    public static Canvas util = new Canvas() {protected void paint(Graphics g) {}};
    public static boolean hasPointerEvents = true;

    protected void startApp() throws MIDletStateChangeException {
        hasPointerEvents = util.hasPointerEvents();
        display = Display.getDisplay(this);
        try {
            UI ui = new UI();
            setCurrent(ui);
        } catch(Exception ex) {
            setCurrent(new Alert(ex.toString()));
            ex.printStackTrace();
        }
    }

    protected void pauseApp() {
        // TODO: save current work to RMS
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        // TODO: save current work to RMS
    }
    
    public static void setCurrent(Displayable d) {
        if (d instanceof Alert) {
            display.setCurrent((Alert) d, display.getCurrent());
        }
        display.setCurrent(d);
    }
    
    public static void vibrate(int duration) {
        display.vibrate(duration);
    }
    
}
