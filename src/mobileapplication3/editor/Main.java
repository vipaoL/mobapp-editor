/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.utils.Settings;
import mobileapplication3.editor.setup.SetupWizard;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import mobileapplication3.editor.ui.RootContainer;

/**
 *
 * @author vipaol
 */
public class Main extends MIDlet {
    
    private static Display display;
    public static Canvas util = new Canvas() {protected void paint(Graphics g) {}};
    public static boolean hasPointerEvents = true;
    private static boolean isStarted = false;

    protected void startApp() throws MIDletStateChangeException {
        if (isStarted) {
            return;
        }
        isStarted = true;
        
        hasPointerEvents = util.hasPointerEvents();
        display = Display.getDisplay(this);
        try {
            if (Settings.isSetupWizardCompleted()) {
                setCurrent(new RootContainer(new EditorScreenUI()));
            } else {
                setCurrent(new RootContainer(new SetupWizard(new SetupWizard.FinishSetup() {
                    public void startEditor() {
                        setCurrent(new RootContainer(new EditorScreenUI()));
                    }
                })));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            setCurrent(new Alert(ex.toString()));
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
            try {
                throw new Exception();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            display.setCurrent((Alert) d, display.getCurrent());
        }
        display.setCurrent(d);
    }
    
    public static void vibrate(int duration) {
        display.vibrate(duration);
    }
    
}
