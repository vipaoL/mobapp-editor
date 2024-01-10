/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public class ConfirmDialog extends Dialog {

    public ConfirmDialog(Button[] buttons) {
        super(buttons);
    }

    public void paint(Graphics g) {
    }

    public boolean handlePointerReleased(int x, int y) {
        return false;
    }

    public boolean handleKeyPressed(int keyCode) {
        return false;
    }
    
}
