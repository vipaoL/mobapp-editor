/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public class ButtonPanelHorizontal extends UIComponent {
    private int rows, btnH;
    private ButtonRow[] buttonRows;
    private Button[] buttons;

    public ButtonPanelHorizontal(int x0, int y0, int w, Button[] buttons, int anchor, int btnH, int btnsInRow) {
        this.x0 = x0;
        this.y0 = y0;
        this.w = w;
        this.btnH = btnH;
        this.buttons = buttons;

        int buttonsNumber = buttons.length;

        if (buttonsNumber % btnsInRow > 0) {
            rows = buttonsNumber / btnsInRow + 1;
        } else {
            rows = buttonsNumber / btnsInRow;
        }
        
        this.h = btnH*rows;

        if (anchor == BOTTOM) {
            this.y0 -= btnH*rows;
        }

        buttonRows = new ButtonRow[rows];

        for (int i = 0; i < rows; i++) {
            Button[] row = new Button[Math.min(buttonsNumber - i*btnsInRow, btnsInRow)];
            System.arraycopy(buttons, i*btnsInRow, row, 0, row.length);
            buttonRows[i] = (ButtonRow) new ButtonRow(this.x0, this.y0 + this.btnH*i, this.w, this.btnH, row, ButtonRow.TOP).setVisible(false);
        }
    }

    public void paint(Graphics g) {
        for (int i = 0; i < buttonRows.length; i++) {
            buttonRows[i].paint(g);
        }
    }

    public boolean handlePointerReleased(int x, int y) {
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        for (int i = 0; i < buttonRows.length; i++) {
            if (buttonRows[i].handlePointerReleased(x, y)) {
                return true;
            }
        }
        return false;
    }

    public UIComponent setVisible(boolean b) {
        for (int i = 0; i < buttonRows.length; i++) {
            buttonRows[i].setVisible(b);
        }
        isVisible = b;
        return this;
    }

    public boolean handleKeyPressed(int keyCode) {
        if (keyCode >= 49 && keyCode <= 57) {
            int i = keyCode - 49;
            
            if (i < buttons.length) {
                buttons[i].invokePressed(false);
                return true;
            }
        }
        
        return false;
    }

}