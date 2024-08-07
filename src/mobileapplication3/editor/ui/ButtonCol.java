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
import mobileapplication3.utils.Utils;

/**
 *
 * @author vipaol
 */
public class ButtonCol extends AbstractButtonSet {
    
    private AnimationThread animationThread = null;
    private int btnH = H_AUTO;
    
    private boolean isScrollable = false;
    private boolean trimHeight = false;
    private int hUntilTrim, prevTotalBtnsH;
    private int scrollOffset = 0;
    private int pointerPressedX, pointerPressedY, scrollOffsetWhenPressed;
    private boolean startFromBottom;
    private boolean enableAnimations = true;
    
    public ButtonCol() { }

    public ButtonCol(Button[] buttons) {
        this.buttons = buttons;
    }

    public void recalcSize() {
        setSizes(w, hUntilTrim, btnH, trimHeight);
    }

    public IUIComponent setSize(int w, int h) {
        return setSizes(w, h, btnH);
    }
    
    public IUIComponent setSizes(int w, int h, int btnH) {
        return setSizes(w, h, btnH, trimHeight);
    }

    public IUIComponent setSizes(int w, int h, int btnH, boolean trimHeight) {
        if (w == 0 || h == 0 || btnH == 0) {
            try {
                throw new Exception("Setting zero as a dimension");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return this;
        }
        
        int prevH = this.h;
        this.w = w;
        this.h = h;
        this.btnH = btnH;
        this.hUntilTrim = this.h;
        this.trimHeight = trimHeight;
        
        if (buttons == null) {
            return this;
        }
        
        if (this.w == W_AUTO) {
            this.w = getMinPossibleWidth();
        }
        
        if (this.btnH == H_AUTO) {
            if (this.h == H_AUTO || this.trimHeight) {
                this.btnH = Font.getDefaultFont().getHeight() * 5 / 2 + buttonsBgPadding*2;
            } else {
                this.btnH = this.h / buttons.length;
            }
        } else {
            this.h = Math.min(this.h, this.btnH * buttons.length);
        }
        
        if (this.h == H_AUTO) {
            this.h = buttons.length * this.btnH;
        }
        
        if (this.trimHeight) {
            this.h = Math.min(this.h, this.btnH * buttons.length);
        }
        
        if (startFromBottom) {
            int dtbh = getTotalBtnsH() - prevTotalBtnsH;
            int dh = this.h - prevH;
            prevTotalBtnsH = getTotalBtnsH();
            
            scrollOffset += dtbh - dh;
            
            setSelected(buttons.length - 1);
        }
        
        scrollOffsetWhenPressed = scrollOffset = Math.max(0, Math.min(scrollOffset, getTotalBtnsH() - this.h));
        
        recalcPos();
        
        return super.setSize(this.w, this.h);
    }
    
    public int getMinPossibleWidth() { /////// need fix
        int res = 0;
        for (int i = 0; i < buttons.length; i++) {
            String[] btnTextLines = Utils.split(buttons[i].getTitle(), "\n");
            for (int j = 0; j < btnTextLines.length; j++) {
                res = Math.max(res, Font.getDefaultFont().stringWidth(btnTextLines[j] + "  ") + buttons[i].getBgPagging()*4);
            }
        }
        return res;
    }

    public int getBtnH() {
        return btnH;
    }
    
    public int getTotalBtnsH() {
        if (buttons == null) {
            return 0;
        }
        
        return buttons.length * getBtnH();
    }
    
    public boolean handlePointerReleased(int x, int y) {
        if (!isVisible) {
            return false;
        }
        
        if (buttons == null || buttons.length == 0) {
            return false;
        }
        
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        boolean wasSelected = (selected == prevSelected && isSelectionEnabled);
        prevSelected = selected;
        
        return buttons[selected].invokePressed(wasSelected, isFocused);
    }
    
    public boolean handlePointerPressed(int x, int y) {
        if (!isVisible) {
            return false;
        }
        
        if (buttons == null || buttons.length == 0) {
            return false;
        }
        
        if (!isScrollable) {
            return false;
        }
        
        if (btnH*buttons.length <= h) {
            //return false;
        }
        
        if (!checkTouchEvent(x, y)) {
            pointerPressedX = pointerPressedY = -1;
            return false;
        }
        
        pointerPressedX = x;
        pointerPressedY = y;
        scrollOffsetWhenPressed = scrollOffset;
        
        x -= x0;
        y -= y0 - scrollOffset;
        setSelected(y / btnH);
        
        if (isSelectionEnabled) {
            isSelectionVisible = true;
        }
        
        return true;
    }
    
    public boolean handlePointerDragged(int x, int y) {
        if (!isVisible) {
            return false;
        }
        
        if (buttons == null || buttons.length == 0) {
            return false;
        }
        
        if (!isScrollable) {
            return false;
        }
        
        if (btnH*buttons.length <= h) {
            //return false;
        }
        
        scrollOffset = scrollOffsetWhenPressed - (y - pointerPressedY);
        
        scrollOffset = Math.max(0, Math.min(scrollOffset, getTotalBtnsH() - h));
        
        return true;
    }

    public boolean handleKeyPressed(int keyCode, int count) {
        if (!isVisible) {
            return false;
        }
        
        if (!isSelectionEnabled) {
            return false;
        }
        
        if (buttons == null || buttons.length == 0) {
            return false;
        }
        
        switch (keyCode) {
            default:
                switch (Main.util.getGameAction(keyCode)) {
                    case Canvas.UP:
                        do {
                            if (selected > 0) {
                                setSelected(selected-1);
                            } else {
                            	setSelected(buttons.length - 1);
                            }
                        } while(false && !buttons[selected].isActive());
                        break;
                    case Canvas.DOWN:
                        do {
                            if (selected < buttons.length - 1) {
                            	setSelected(selected+1);
                            } else {
                            	setSelected(0);
                            }
                        } while(false && !buttons[selected].isActive());
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
        
        int selectedH = btnH * selected;
        int startY = scrollOffset;
        int targetY = scrollOffset;
        if (selectedH - btnH < scrollOffset) {
            targetY = Math.max(0, selectedH - btnH * 3 / 4);
        }
        
        if (selectedH + btnH > scrollOffset + h) {
            targetY = Math.min(btnH*buttons.length - h, selectedH - h + btnH + btnH * 3 / 4);
        }
        
        if (enableAnimations && targetY != startY) {
            initAnimationThread();
            animationThread.animate(0, startY, 0, targetY, 200);
        } else {
            scrollOffset = targetY;
        }
        
        if (isSelectionEnabled) {
            isSelectionVisible = true;
        }
        
        return true;
    }

    public boolean handleKeyRepeated(int keyCode, int pressedCount) {
        return handleKeyPressed(keyCode, 1);
    }
    
    public ButtonCol enableScrolling(boolean isScrollable, boolean startFromBottom) {
        this.startFromBottom = startFromBottom;
        
        if (isScrollable) {
            setIsSelectionEnabled(true);
        }
        
        this.isScrollable = isScrollable;
        return this;
    }

    public ButtonCol enableAnimations(boolean b) {
        enableAnimations = b;
        return this;
    }

    public ButtonCol trimHeight(boolean b) {
        trimHeight = b;
        return this;
    }
    
    public void onPaint(Graphics g) {
        if (buttons == null || buttons.length == 0) {
            return;
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
            
            if (y + btnH/2 > y0 + h) {
                break;
            }
            
            if (y < y0) {
                x += (y0 - y) / 2;
                w -= (y0 - y);
                btnH = btnH - (y0 - y);
                y = y0;
            }
            
            if (y + btnH > y1) {
                x += (y + btnH - y1) / 2;
                w -= (y + btnH - y1);
                btnH = y1 - y;
                y = y1 - btnH;
            }
            
            boolean drawAsSelected = (i == selected && isSelectionVisible);
            buttons[i].paint(g, x, y, w, btnH, drawAsSelected, isFocused);
            g.setFont(prevFont);
        }
        
        if (isSelectionEnabled && h < getTotalBtnsH()) {
            g.setColor(0xffffff);
            int selectionMarkY0 = h * scrollOffset / getTotalBtnsH();
            int selectionMarkY1 = h * (scrollOffset + h) / getTotalBtnsH();
            g.drawLine(x0 + w - 1, y0 + selectionMarkY0, x0 + w - 1, y0 + selectionMarkY1);
        }
    }
    
    private void initAnimationThread() {
        if (animationThread == null) {
            animationThread = new AnimationThread(new AnimationThread.AnimationWorker() {
                public void onStep(int newX, int newY) {
                    scrollOffset = newY;
                    repaint();
                }
            });
        }
    }
}