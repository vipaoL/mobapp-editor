/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mobileapplication3.editor.setup;

import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.TextComponent;

/**
 *
 * @author vipaol
 */
public class Page3 extends AbstractSetupWizardPage {
    
    private TextComponent text = new TextComponent();
    
    public Page3(Button[] buttons, SetupWizard.Feedback feedback) {
        super("Let's get started", buttons, feedback);
    }
    
    public void init() {
    	super.init();
    	actionButtons.setSelected(actionButtons.getButtonCount() - 1);
    }

    public void initOnFirstShow() {
        this.text.setText(
                "This app lets you to create structures for mobapp-game."
                        + " You can load them into the game"
                        + " by pressing \"Ext Structs\" in the main menu."
                        + " The game will try to find them in several"
                        + " folders. Let's pick one of them to save structures"
                        + " you'll create into.");
    }

    protected IUIComponent initAndGetPageContent() {
        return text;
    }
    
}