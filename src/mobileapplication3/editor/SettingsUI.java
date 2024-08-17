/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.utils.EditorSettings;
import mobileapplication3.editor.setup.SetupWizard;
import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.Switch;

/**
 *
 * @author vipaol
 */
public class SettingsUI extends AbstractPopupWindow {

    public SettingsUI(IPopupFeedback parent) {
        super("Settings", parent);
    }

//    public void onSetBounds(int x0, int y0, int w, int h) {
//        super.onSetBounds(x0, y0, w, h);
//        settingsList
//                .setButtonsBgPadding(h/64)
//                .setButtonsBgColor(0x333388);
//        settingsList
//                .setSizes(w, actionButtons.y0 - title.getBottomY(), ButtonCol.H_AUTO, true)
//                .setPos(x0 + w/2, actionButtons.y0, ButtonCol.BOTTOM | ButtonCol.HCENTER);
//    }

    protected Button[] getActionButtons() {
        return new Button[] {
            new Button("OK") {
                public void buttonPressed() {
                    close();
                }
            }
        };
    }

    protected IUIComponent initAndGetPageContent() {
        Button[] settingsButtons = new Button[]{
            new Switch("Animations") {
				public boolean getValue() {
					return EditorSettings.getAnimsEnabled();
				}

				public void setValue(boolean value) {
					EditorSettings.setAnimsEnabled(value);
					getUISettings().onChange();
				}
            },
            new Switch("Enable keyRepeated events in lists") {
				public boolean getValue() {
					return EditorSettings.getKeyRepeatedInListsEnabled(false);
				}

				public void setValue(boolean value) {
					EditorSettings.setKeyRepeatedInListsEnabled(value);
					getUISettings().onChange();
				}
            },
            new Button("Show guide and setup wizard again. Current MGStructs folder: " + EditorSettings.getMgstructsFolderPath()) {
                public void buttonPressed() {
                    showPopup(new SetupWizard(new SetupWizard.FinishSetup() {
						public void onFinish() {
							closePopup();
						}
					}));
                }
            },
            new Button("Reset settings (will reset MGStructs folder as well)") {
                public void buttonPressed() {
                    EditorSettings.resetSettings();
                }
            }.setBgColor(0x550000)
        };
        
        ButtonCol settingsList = (ButtonCol) new ButtonCol()
                .enableScrolling(true, false)
                .enableAnimations(false)
                .trimHeight(true)
                .setButtons(settingsButtons);
        
        return settingsList;
    }
    
    public void setPageContentBounds(IUIComponent pageContent, int x0, int y0, int w, int h) {
        if (pageContent != null) {
            ((ButtonCol) pageContent)
                    .setButtonsBgPadding(margin/8)
                    .setSize(w - margin*2, h - margin*2)
                    .setPos(x0 + w/2, y0 + h - margin, BOTTOM | HCENTER);
        }
    }
    
}
