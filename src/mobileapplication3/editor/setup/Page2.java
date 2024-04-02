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
public class Page2 extends AbstractSetupWizardPage {
    
    private TextComponent text = new TextComponent();
    
    public Page2(Button[] buttons, SetupWizard.Feedback feedback) {
        super("Let's get started", buttons, feedback);
        initPage();
    }

    public void initOnFirstShow() {
        this.text.setText(
                "This app lets you to create structures for mobapp-game."
                        + " You can load structures you created into the game"
                        + " by pressing \"Ext Structs\" in the main menu"
                        + " of the game. The game will try to find them in several"
                        + " folders. Let's pick one of them to save structures"
                        + " you'll create into.");
    }

    protected IUIComponent initAndGetPageContent() {
        return text;
    }
    
}
