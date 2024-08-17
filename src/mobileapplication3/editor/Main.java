/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import mobileapplication3.editor.setup.SetupWizard;
import mobileapplication3.editor.ui.RootContainer;
import mobileapplication3.utils.Settings;

/**
 *
 * @author vipaol
 */
public class Main extends MIDlet {
    
    private static Display display;
    private static boolean isStarted = false;

    protected void startApp() throws MIDletStateChangeException {
        if (isStarted) {
            return;
        }
        isStarted = true;
        display = Display.getDisplay(this);
        try {
            if (Settings.isSetupWizardCompleted()) {
                setCurrent(new RootContainer(new MainScreenUI()));
            } else {
                setCurrent(new RootContainer(new SetupWizard(new SetupWizard.FinishSetup() {
                    public void onFinish() {
                        setCurrent(new RootContainer(new MainScreenUI()));
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
            try {
            	display.setCurrent((Alert) d, display.getCurrent());
            } catch (Exception ex) {
				display.setCurrent(d);
			}
        } else {
        	display.setCurrent(d);
        }
    }
    
    public static void vibrate(int duration) {
        display.vibrate(duration);
    }
    
    public static void showTextBox(TextBox textBox) {
    	//TextBoxCommandListener cl = new TextBoxCommandListener();
    	//textBox.setCommandListener(new TextBoxCommandListener());
    }
    
    public class TextBoxCommandListener implements CommandListener {

		public void commandAction(Command command, Displayable displayable) {
			switch (command.getCommandType()) {
				case Command.OK:
				case Command.CANCEL:
					
					break;
			}
		}
    	
    }
    
}
