/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.setup;

import javax.microedition.lcdui.Graphics;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.UIComponent;

/**
 *
 * @author vipaol
 */
public class Page1 extends AbstractSetupWizardPage {
    
    private UIComponent logo;
    
    public Page1(Button[] buttons, SetupWizard.Feedback feedback) {
        super("Welcome to mobapp-game editor", buttons, feedback);
        initPage();
    }
    
    public void initOnFirstShow() {
        
    }

    protected IUIComponent initAndGetPageContent() {
        logo = new UIComponent() {
            public void onPaint(Graphics g) {
                g.setColor(0x0000ff);
                g.drawArc(x0, y0, w-1, h-1, 0, 360);
                g.drawString("Here will be", x0 + w/2, y0 + h/2, Graphics.BOTTOM | Graphics.HCENTER);
                g.drawString("a cool logo.", x0 + w/2, y0 + h/2, Graphics.TOP | Graphics.HCENTER);
            }
            
            public boolean canBeFocused() {
                return false;
            }
            
            public boolean handlePointerReleased(int x, int y) {
                return false;
            }
            
            public boolean handleKeyPressed(int keyCode, int count) {
                return false;
            }
        };
        return logo;
    }

    public void setPageContentBounds(int x0, int y0, int w, int h) {
        int freeSpaceH = h - margin*2;
        int logoSide = Math.min(freeSpaceH, w - margin * 2);

        logo.setSize(logoSide, logoSide)
                .setPos(x0 + w / 2, y0 + h / 2, IUIComponent.VCENTER | IUIComponent.HCENTER);
    }
    
}
