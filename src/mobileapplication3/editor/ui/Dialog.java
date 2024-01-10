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
public abstract class Dialog extends UIComponent {

    public Dialog(Button[] buttons) {
        
    }

    public abstract void paint(Graphics g);

    public abstract boolean handlePointerReleased(int x, int y);

    public abstract boolean handleKeyPressed(int keyCode);
    
}
