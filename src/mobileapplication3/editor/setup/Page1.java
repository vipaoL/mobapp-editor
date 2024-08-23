/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.setup;

import java.io.IOException;

import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.ImageComponent;
import mobileapplication3.editor.ui.TextComponent;
import mobileapplication3.editor.ui.platform.Image;

/**
 *
 * @author vipaol
 */
public class Page1 extends AbstractSetupWizardPage {
    
    public Page1(Button[] buttons, SetupWizard.Feedback feedback) {
        super("Welcome to the structure editor for mobapp-game", buttons, feedback);
    }
    
    public void initOnFirstShow() {
        
    }

    protected IUIComponent initAndGetPageContent() {
    	Image logo = null;
    	String errorMessage = null;
    	
        try {
        	logo = Image.createImage("/logo.png");
		} catch (IOException ex) {
			ex.printStackTrace();
			errorMessage = ex + " ";
			try {
	        	logo = Image.createImage("/icon.png");
			} catch (IOException e) {
				e.printStackTrace();
				errorMessage += e;
			}
		}
        
        if (logo != null) {
        	return new ImageComponent(logo).setBgColor(COLOR_ACCENT_MUTED);
        } else {
			return new TextComponent("Could not load logo image: " + errorMessage)
					.setFontColor(0xff0000)
					.setBgColor(0);
		}
    }

    public void setPageContentBounds(IUIComponent pageContent, int x0, int y0, int w, int h) {
        int freeSpaceH = h - margin*2;
        int logoSide = Math.min(freeSpaceH, w - margin * 2);

        pageContent.setSize(logoSide, logoSide)
                .setPos(x0 + w / 2, y0 + h / 2, IUIComponent.VCENTER | IUIComponent.HCENTER);
    }
    
}
