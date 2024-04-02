/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.utils.Settings;
import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.TextComponent;
import mobileapplication3.editor.ui.TextComponent;

/**
 *
 * @author vipaol
 */
public class PageRMSTest extends AbstractPopupWindow {

    private PageContent content;
    private Button[] buttons;

    public PageRMSTest(IPopupFeedback feedback) {
        super("RMS debug", feedback);
        buttons = new Button[] {
            new Button("Back", new Button.ButtonFeedback() {
            public void buttonPressed() {
                close();
            }
        })
        };
        content = new PageContent();
        
        initPage();
    }

    protected IUIComponent initAndGetPageContent() {
        return content;
    }

    protected Button[] getActionButtons() {
        return buttons;
    }
    
    private class PageContent extends Container {
        public TextComponent text;
        public ButtonCol testButtons;
        public void init() {
            text = new TextComponent("aaaaa");
            
            testButtons = new ButtonCol(
                    new Button[] {
                        new Button("write settings to RMS", new Button.ButtonFeedback() {
                        public void buttonPressed() {
                            Settings.saveToRMS();
                        }
                    }),
                    new Button("get settings", new Button.ButtonFeedback() {
                        public void buttonPressed() {
                            text.setText(Settings.getCurrentSettingsAsStr());
                            repaint();
                        }
                    }),
                    new Button("1test test test test test test test test 2test test test testest test test test test test test test 2test test test testest test test test test test test test 2test test test test3", new Button.ButtonFeedback() {
                        public void buttonPressed() {
                        }
                    }).setBgColor(0xff0000)
            }).enableScrolling(true, false);
            
            setComponents(new IUIComponent[]{text, testButtons});
        }

        protected void onSetBounds(int x0, int y0, int w, int h) {
            testButtons.setButtonsBgPadding(margin/4);
            testButtons
                    .setSizes(w, ButtonCol.H_AUTO, ButtonCol.H_AUTO)
                    .setPos(x0 + w/2, y0 + h, ButtonCol.BOTTOM | ButtonCol.HCENTER);

            text.setSize(w, h - testButtons.getHeight())
                    .setPos(x0 + w/2, y0, ButtonCol.TOP | ButtonCol.HCENTER);
        }
    }
    
}
