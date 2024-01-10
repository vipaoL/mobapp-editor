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
public class ButtonRow extends UIComponent {
    Button[] buttons;
    int bgColor = 0x101020;

    public ButtonRow(int x0, int y0, int w, int h, Button[] buttons, int anchor) {
        this.x0 = x0;
        this.y0 = y0;
        this.w = w;
        this.h = h;
        this.buttons = buttons;
        if (anchor == BOTTOM) {
            this.y0 -= h;
        }
    }

    public ButtonRow setBgColor(int color) {
        bgColor = color;
        return this;
    }

    public boolean handlePointerReleased(int x, int y) {
        if (buttons.length == 0) {
            return false;
        }
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        x -= x0;
        y -= y0;
        
        int i = x * buttons.length / w;
        buttons[i].invokePressed(false);
        return true;
    }

    public void paint(Graphics g) {
        if (buttons.length == 0) {
            return;
        }
        
        if (bgColor > 0) {
            g.setColor(bgColor);
            g.fillRect(x0, y0, w, h);
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].paint(g, x0 + i*w/buttons.length, y0, w/buttons.length, h, false);
        }
    }

    public boolean handleKeyPressed(int keyCode) {
        return false;
    }

}