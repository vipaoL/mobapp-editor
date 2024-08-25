/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import mobileapplication3.editor.EditorSettings;
import mobileapplication3.editor.MainScreenUI;
import mobileapplication3.editor.setup.SetupWizard;
import mobileapplication3.platform.ui.RootContainer;
import mobileapplication3.ui.UISettings;

/**
 *
 * @author vipaol
 */
public class EditorMIDlet extends MIDlet {
    
    private static Display display;
    private static boolean isStarted = false;

    protected void startApp() throws MIDletStateChangeException {
        if (isStarted) {
            return;
        }
        isStarted = true;
        display = Display.getDisplay(this);
        try {
        	final UISettings uiSettings = new UISettings() {
				public boolean getKeyRepeatedInListsEnabled() {
					return EditorSettings.getKeyRepeatedInListsEnabled(false);
				}
				
				public boolean getAnimsEnabled() {
					return EditorSettings.getAnimsEnabled(true);
				}
				
				public void onChange() {
					try {
						((RootContainer) display.getCurrent()).init();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			};
            if (EditorSettings.isSetupWizardCompleted()) {
                setCurrent(new RootContainer(new MainScreenUI(), uiSettings));
            } else {
                setCurrent(new RootContainer(new SetupWizard(new SetupWizard.FinishSetup() {
                    public void onFinish() {
                        setCurrent(new RootContainer(new MainScreenUI(), uiSettings));
                    }
                }), uiSettings));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            setCurrent(new Alert(ex.toString()));
        }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException { }
    
    public static void setCurrent(Displayable d) {
        if (d instanceof Alert) {
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
    
//    public static void showTextBox(TextBox textBox) {
//    	TextBoxCommandListener cl = new TextBoxCommandListener();
//    	textBox.setCommandListener(new TextBoxCommandListener());
//    }
//    
//    public class TextBoxCommandListener implements CommandListener {
//
//		public void commandAction(Command command, Displayable displayable) {
//			switch (command.getCommandType()) {
//				case Command.OK:
//				case Command.CANCEL:
//					
//					break;
//			}
//		}
//    	
//    }
    
}
