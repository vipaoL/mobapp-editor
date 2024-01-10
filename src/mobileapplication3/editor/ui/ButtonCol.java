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
public class ButtonCol extends UIComponent {
    
    public static final int TOP = Graphics.TOP;
    public static final int BOTTOM = Graphics.BOTTOM;
    public static final int LEFT = Graphics.LEFT;
    public static final int RIGHT = Graphics.RIGHT;
    public static final int BTN_H_AUTO = 0;
    int btnH;
    int selected = 0;
    Button[] buttons;
    int bgColor = 0x101020;
    boolean selectionEnabled = false;
    boolean isScrollable = false;
    int scrollOffset = 0;

    public ButtonCol(int x0, int y0, int w, int h, Button[] buttons, int anchor, int btnH) {
        this.x0 = x0;
        this.y0 = y0;
        this.w = w;
        this.h = h;
        
        this.buttons = buttons;
        
        if (btnH == BTN_H_AUTO) {
            this.btnH = h / buttons.length;
        } else {
            this.btnH = btnH;
            this.h = Math.min(h, btnH * buttons.length);
        }
        
        if ((anchor&BOTTOM) != 0) {
            this.y0 -= this.h;
        }
        if ((anchor&RIGHT) != 0) {
            this.x0 -= this.w;
        }
    }

    public ButtonCol setBg(int color) {
        bgColor = color;
        return this;
    }
    
    public ButtonCol setButtonsBg(int color) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBgColor(color);
        }
        return this;
    }
    
    public ButtonCol setButtonsBgPadding(int padding) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBgPadding(padding);
        }
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
        y -= y0 - scrollOffset;
        
        int i = y / btnH;
        boolean wasSelected = (i == selected && selectionEnabled);
        if (selectionEnabled) {
            selected = i;
        }
        buttons[i].invokePressed(wasSelected);
        return true;
    }
    
    int pointerPressedX, pointerPressedY, scrollOffsetWhenPressed;
    
    public boolean handlePointerPressed(int x, int y) {
        if (!isVisible) {
            return false;
        }
        
        if (!isScrollable) {
            return false;
        }
        
        if (btnH*buttons.length <= h) {
            return false;
        }
        
        if (!checkTouchEvent(x, y)) {
            pointerPressedX = pointerPressedY = -1;
            return false;
        }
        
        pointerPressedX = x;
        pointerPressedY = y;
        scrollOffsetWhenPressed = scrollOffset;
        
        return true;
    }
    
    public boolean handlePointerDragged(int x, int y) {
        if (!isVisible) {
            return false;
        }
        
        if (!isScrollable) {
            return false;
        }
        
        if (btnH*buttons.length <= h) {
            return false;
        }
        
        scrollOffset = scrollOffsetWhenPressed - (y - pointerPressedY);
        
        scrollOffset = Math.max(0, Math.min(scrollOffset, buttons.length*btnH - h));
        
        return true;
    }
    
    public ButtonCol enableScrolling(boolean isScrollable, boolean startFromBottom) {
        this.isScrollable = isScrollable;
        if (startFromBottom) {
            scrollOffsetWhenPressed = scrollOffset = Math.max(0, btnH*buttons.length - h);
            selected = buttons.length - 1;
        }
        return this;
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
            Font prevFont = g.getFont();
            int btnH = this.btnH;
            int x = x0;
            int y = y0 - scrollOffset + i*btnH;
            int w = this.w;
            int y1 = y0 + h;
            
            if (y + btnH - prevFont.getHeight() < y0) {
                continue;
            }
            
            if (y < y0) {
                g.setFont(Font.getFont(prevFont.getFace(), prevFont.getStyle(), Font.SIZE_SMALL));
                x += (y0 - y) / 2;
                w -= (y0 - y);
                btnH = btnH - (y0 - y);
                y = y0;
            }
            
            if (y + btnH > y1) {
                g.setFont(Font.getFont(prevFont.getFace(), prevFont.getStyle(), Font.SIZE_SMALL));
                x += (y + btnH - y1) / 2;
                w -= (y + btnH - y1);
                btnH = y1 - y;
                y = y1 - btnH;
            }
            
            if (y + btnH/2 > y0 + h) {
                g.setFont(prevFont);
                break;
            }
            
            boolean wasSelected = (i == selected && selectionEnabled);
            buttons[i].paint(g, x, y, w, btnH, wasSelected);
            g.setFont(prevFont);
        }
    }

    public ButtonCol setIsSelectionEnabled(boolean selectionEnabled) {
        this.selectionEnabled = selectionEnabled;
        return this;
    }

    public boolean handleKeyRepeated(int keyCode) {
        return handleKeyPressed(keyCode);
    }

    public boolean handleKeyPressed(int keyCode) {
        if (!isVisible) {
            return false;
        }
        setIsSelectionEnabled(true);
        System.out.println(keyCode);
        switch (keyCode) {
            default:
                switch (Main.util.getGameAction(keyCode)) {
                    case Canvas.UP:
                        if (selected > 0) {
                            selected--;
                        } else {
                            selected = buttons.length - 1;
                        }
                        break;
                    case Canvas.DOWN:
                        if (selected < buttons.length - 1) {
                            selected++;
                        } else {
                            selected = 0;
                        }
                        break;
                    case Canvas.FIRE:
                        buttons[selected].invokePressed(true);
                        return true;
                    default:
                        return false;
                }
        }
        
        int selectedH = btnH * selected;
        if (selectedH - btnH < scrollOffset) {
            scrollOffset = Math.max(0, selectedH - btnH * 3 / 4);
        }
        
        if (selectedH + btnH > scrollOffset + h) {
            scrollOffset = Math.min(btnH*buttons.length - h, selectedH - h + btnH + btnH * 3 / 4);
        }
        
        return true;
    }
}