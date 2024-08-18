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
public abstract class AbstractPopupWindow extends Page {
    protected IPopupFeedback feedback;

    public AbstractPopupWindow(String title, IPopupFeedback parent) {
        super(title);
        try {
        	setBgImage(((Container) parent).getBlurredCapture());
        } catch (Exception ex) {
			ex.printStackTrace();
		}
        this.feedback = parent;
    }
    
    protected void close() {
        feedback.closePopup();
    }
    
}
