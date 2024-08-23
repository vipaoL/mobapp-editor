/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import mobileapplication3.editor.ui.platform.Font;
import mobileapplication3.editor.ui.platform.Graphics;
import mobileapplication3.editor.ui.platform.RootContainer;
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
    private int pointerPressedY, scrollOffsetWhenPressed;
    private boolean startFromBottom;
    private boolean enableAnimations = true;
    
    public ButtonCol() { }

    public ButtonCol(Button[] buttons) {
        this.buttons = buttons;
    }
    
    public void init() {
    	try {
    		enableAnimations = getUISettings().getAnimsEnabled();
    	} catch (Exception ex) { }
    	super.init();
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
                throw new Exception("Setting zero as a dimension " + getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return this;
        }
        
        int prevH = this.h;
        super.setSize(w, h);
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
        return this;
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
            pointerPressedY = -1;
            return false;
        }
        
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
                switch (RootContainer.getGameActionn(keyCode)) {
                    case Keys.UP:
                        if (selected > 0) {
                            setSelected(selected-1);
                        } else {
                        	setSelected(buttons.length - 1);
                        }
                        break;
                    case Keys.DOWN:
                        if (selected < buttons.length - 1) {
                        	setSelected(selected+1);
                        } else {
                        	setSelected(0);
                        }
                        break;
                    case Keys.FIRE:
                        if (isSelectionEnabled) {
                            isSelectionVisible = true;
                        }
                        return buttons[selected].invokePressed(true, isFocused);
                    default:
                        return isFocused;
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
    
    public void onPaint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
        if (buttons == null || buttons.length == 0) {
            return;
        }

        for (int i = 0; i < buttons.length; i++) {
        	int prevFontFace = g.getFontFace();
			int prevFontStyle = g.getFontStyle();
			int prevFontSize = g.getFontSize();
			int prevFontHeight = g.getFontHeight();
			
            int btnH = this.btnH;
            int btnX = x0;
            int btnY = y0 - scrollOffset + i*btnH;
            int btnBottomY = y0 + h;
            int btnW = w;
            
            if (btnY + btnH - prevFontHeight < y0) {
                continue;
            }
            
            if (btnY + btnH/2 > y0 + h) {
                break;
            }
            
            if (btnY < y0) {
                btnX += (y0 - btnY) / 2;
                btnW -= (y0 - btnY);
                btnH = btnH - (y0 - btnY);
                btnY = y0;
            }
            
            if (btnY + btnH > btnBottomY) {
                btnX += (btnY + btnH - btnBottomY) / 2;
                btnW -= (btnY + btnH - btnBottomY);
                btnH = btnBottomY - btnY;
                btnY = btnBottomY - btnH;
            }
            
            boolean drawAsSelected = (i == selected && isSelectionVisible && isFocused);
            buttons[i].paint(g, btnX, btnY, btnW, btnH, drawAsSelected, isFocused, forceInactive);
            g.setFont(prevFontFace, prevFontStyle, prevFontSize);
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