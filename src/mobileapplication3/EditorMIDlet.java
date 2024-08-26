/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import mobileapplication3.editor.EditorSettings;
import mobileapplication3.editor.MainScreenUI;
import mobileapplication3.editor.setup.SetupWizard;
import mobileapplication3.platform.Platform;
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
        Platform.init(this);
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
                Platform.setCurrent(new RootContainer(new MainScreenUI(), uiSettings));
            } else {
                Platform.setCurrent(new RootContainer(new SetupWizard(new SetupWizard.FinishSetup() {
                    public void onFinish() {
                        Platform.setCurrent(new RootContainer(new MainScreenUI(), uiSettings));
                    }
                }), uiSettings));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            Platform.setCurrent(new Alert(ex.toString()));
        }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException { }
    
}
