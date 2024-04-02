/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import mobileapplication3.editor.Main;

/**
 *
 * @author vipaol
 */
public class ButtonPanelHorizontal extends AbstractButtonSet {
    public static final int H_AUTO = -1;
    
    private int rows, btnH;
    private ButtonRow[] buttonRows = new ButtonRow[0];
    private int btnsInRow;
    private int selectedRow, selectedBtnInRow;

    public ButtonPanelHorizontal(Button[] buttons) {
        setButtons(buttons);
    }
    
//    public void setButtons(Button[] buttons) {
//        this.buttons = buttons;
//        initRows();
//    }

    public AbstractButtonSet setButtons(Button[] buttons) {
        this.buttons = buttons;
        initRows();
        setIsSelectionVisible(isSelectionVisible);
        return super.setButtons(buttons);
    }
    
    private void initRows() {
        if (buttons == null) {
            return;
        }
        
        buttonRows = new ButtonRow[rows];

        for (int i = 0; i < rows; i++) {
            Button[] row = new Button[Math.min(buttons.length - i*btnsInRow, btnsInRow)];
            System.arraycopy(buttons, i*btnsInRow, row, 0, row.length);
            buttonRows[i] = (ButtonRow) new ButtonRow(row);
        }
    }
    
    public IUIComponent setSizes(int w, int h, int btnH) {
        return setSizes(w, h, btnH, false);
    }

    public IUIComponent setSizes(int w, int h, int btnH, boolean trimHeight) {
        if (buttons == null) {
            return this;
        }
        
        this.w = w;
        this.h = h;
        this.btnH = btnH;
        
        if (this.btnH == H_AUTO) {
            if (this.h == H_AUTO || trimHeight) {
                this.btnH = Font.getDefaultFont().getHeight() * 5 / 2;// + buttonsBgPadding*2;
            } else {
                this.btnH = this.h / rows;
            }
            if (trimHeight) {
                this.h = Math.min(this.h, this.btnH * rows);
            }
        } else {
            this.h = Math.min(this.h, this.btnH * rows);
        }
        
        if (h == H_AUTO) {
            this.h = rows * this.btnH;
        }
        
        setPos(x0, y0, anchor);
        return super.setSize(this.w, this.h);
    }

    public void onPaint(Graphics g) {
        for (int i = 0; i < rows; i++) {
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

    public boolean handleKeyPressed(int keyCode, int count) {
        if (keyCode >= 49 && keyCode <= 57) {
            int i = keyCode - 49;
            
            if (i < buttons.length) {
                setIsSelectionVisible(true);
                selected = i;
                buttons[selected].invokePressed(false, false);
                return true;
            }
        }
        
        if (!isVisible) {
            return false;
        }
        
        setIsSelectionVisible(true);
        switch (keyCode) {
            default:
                switch (Main.util.getGameAction(keyCode)) {
                    case Canvas.LEFT:
                        if (selected > 0) {
                            selected--;
                        } else {
                            selected = buttons.length - 1;
                        }
                        break;
                    case Canvas.RIGHT:
                        if (selected < buttons.length - 1) {
                            selected++;
                        } else {
                            selected = 0;
                        }
                        break;
                    case Canvas.UP:
                        selected -= btnsInRow;
                        if (selected < 0) {
                            selected += buttons.length;
                        }
                        break;
                    case Canvas.DOWN:
                        selected += btnsInRow;
                        if (selected > buttons.length - 1) {
                            selected += buttons.length;
                        }
                        break;
                    case Canvas.FIRE:
                        if (isSelectionEnabled) {
                            isSelectionVisible = true;
                        }
                        return buttons[selected].invokePressed(true, isFocused);
                    default:
                        return false;
                }
        }
        
        buttonRows[selectedRow].setIsSelectionVisible(false);
        selectedRow = selected / btnsInRow;
        buttonRows[selectedRow].setIsSelectionVisible(true);
        buttonRows[selectedRow].setSelected(selected % btnsInRow);
        

        if (isSelectionEnabled) {
            isSelectionVisible = true;
        }
        return true;
    }

    public AbstractButtonSet setIsSelectionVisible(boolean isSelectionVisible) {
        if (buttonRows != null && buttonRows.length > selectedRow && buttonRows[selectedRow] != null) {
            buttonRows[selectedRow].setIsSelectionVisible(isSelectionVisible);
        }
        return super.setIsSelectionVisible(isSelectionVisible);
    }

    public ButtonPanelHorizontal setBtnsInRowCount(int btnsInRow) {
        this.btnsInRow = btnsInRow;
        int buttonsNumber = buttons.length;

        if (buttonsNumber % btnsInRow > 0) {
            rows = buttonsNumber / btnsInRow + 1;
        } else {
            rows = buttonsNumber / btnsInRow;
        }
        
        if (isSizeSet()) {
            setSize(w, btnH*rows);
        }
        
        return this;
    }
    
    protected void onSetBounds(int x0, int y0, int w, int h) {
        initRows();
        
        for (int i = 0; i < rows; i++) {
            buttonRows[i]
                        .setSize(this.w, this.btnH)
                        .setPos(this.x0, this.y0 + this.btnH*i, ButtonRow.TOP | ButtonRow.LEFT);
        }
    }

    public int getMinPossibleWidth() {
        int ret = 0;
        for (int i = 0; i < buttonRows.length; i++) {
            int rowW = 0;
            for (int j = 0; j < buttonRows[i].buttons.length; j++) {
                rowW += buttonRows[i].buttons[i].getMinPossibleWidth();
            }
            ret = Math.max(ret, rowW);
        }
        return ret;
    }

}