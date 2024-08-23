/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mobileapplication3.editor.ui;

import mobileapplication3.editor.ui.platform.Graphics;

/**
 *
 * @author vipaol
 */
public class ButtonPanelVertical extends UIComponent {
    public static final int TOP = ButtonRow.TOP;
    public static final int BOTTOM = ButtonRow.BOTTOM;
    private int cols;
    private ButtonCol[] buttonCols;

    public ButtonPanelVertical(Button[] buttons, int anchor, int btnH, int btnsInCol) {
        int buttonsNumber = buttons.length;

        if (buttonsNumber % btnsInCol > 0) {
            cols = buttonsNumber / btnsInCol + 1;
        } else {
            cols = buttonsNumber / btnsInCol;
        }

        if (anchor == BOTTOM) {
        }

        buttonCols = new ButtonCol[cols];

        for (int i = 0; i < cols; i++) {
            Button[] row = new Button[Math.min(buttonsNumber - i*btnsInCol, btnsInCol)];
            System.arraycopy(buttons, i*btnsInCol, row, 0, row.length);
            //buttonCols[i] = new ButtonCol(this.x0, this.y0 + this.btnH*i, this.w, row, ButtonRow.TOP, this.btnH).setVisible(false);
        }
    }

    public void onPaint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
    	int btnW = w / cols;
        for (int i = 0; i < buttonCols.length; i++) {
            buttonCols[i].paint(g, x0 + btnW * i, y0, btnW, h, forceInactive);
        }
    }

    public boolean handlePointerReleased(int x, int y) {
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        for (int i = 0; i < buttonCols.length; i++) {
            if (buttonCols[i].handlePointerReleased(x, y)) {
                return true;
            }
        }
        return false;
    }

    public IUIComponent setVisible(boolean b) {
        for (int i = 0; i < buttonCols.length; i++) {
            buttonCols[i].setVisible(b);
        }
        isVisible = b;
        return this;
    }
    
    public boolean canBeFocused() {
        return true;
    }

    public boolean handleKeyPressed(int keyCode, int count) {
        return false;
    }

    protected void onSetBounds(int x0, int y0, int w, int h) {
    }

}
