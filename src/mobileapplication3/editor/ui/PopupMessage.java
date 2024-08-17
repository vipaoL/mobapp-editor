/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

/**
 *
 * @author vipaol
 */
public class PopupMessage extends AbstractPopupWindow {
    
    private String message;
    
    public PopupMessage(String title, String message, final IPopupFeedback feedback) {
        super(title, feedback);
        this.message = message;
    }

    protected Button[] getActionButtons() {
        return new Button[]{
            new Button("OK") {
                public void buttonPressed() {
                    feedback.closePopup();
                }
            }
        };
    }

    protected IUIComponent initAndGetPageContent() {
        return new TextComponent(message);
    }
    
}
