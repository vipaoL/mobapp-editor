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
public class ButtonRow extends AbstractButtonSet {
    
    private int leftSoftBindIndex = NOT_SET;
    private int rightSoftBindIndex = NOT_SET;
    
    public ButtonRow() { }
    
    public ButtonRow(Button[] buttons) {
        this.buttons = buttons;
    }
    
    public ButtonRow bindToSoftButtons(int leftSoftBindIndex, int rightSoftBindIndex) {
        this.leftSoftBindIndex = leftSoftBindIndex;
        this.rightSoftBindIndex = rightSoftBindIndex;
        return this;
    }
    
    public int getMinPossibleWidth() { // need fix
        int res = 0;
        for (int i = 0; i < buttons.length; i++) {
            String[] btnTextLines = Utils.split(buttons[i].getTitle(), "\n");
            int maxLineW = 0;
            for (int j = 0; j < btnTextLines.length; j++) {
                maxLineW = Math.max(maxLineW, Font.getDefaultFont().stringWidth(btnTextLines[j] + "    ") + buttons[i].getBgPagging()*4);
            }
            res += maxLineW;
        }
        return res;
    }

    public void onSetBounds(int x0, int y0, int w, int h) {
        if (w == W_AUTO) {
            this.w = getMinPossibleWidth();
        }
        if (h == H_AUTO) {
            this.h = Font.getDefaultFont().getHeight() * 5 / 2 + buttonsBgPadding;
        }
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
        boolean wasSelected = (i == selected && isSelectionEnabled);
        if (isSelectionEnabled) {
        	setSelected(i);
        }
        
        return buttons[i].invokePressed(wasSelected, isFocused);
    }

    public boolean handleKeyPressed(int keyCode, int count) {
        if (!isVisible) {
            return false;
        }
        
        //setIsSelectionEnabled(true);
        switch (keyCode) {
            case KEYCODE_LEFT_SOFT:
                if (leftSoftBindIndex != NOT_SET) {
                    return buttons[leftSoftBindIndex].invokePressed(selected == leftSoftBindIndex, isFocused);
                } else {
                	return false;
                }
            case KEYCODE_RIGHT_SOFT:
                if (rightSoftBindIndex != NOT_SET) {
                    return buttons[rightSoftBindIndex].invokePressed(selected == rightSoftBindIndex, isFocused);
                } else {
                	return false;
                }
            default:
            	if (!isSelectionEnabled) {
            		return false;
            	}
                switch (RootContainer.getGameActionn(keyCode)) {
                    case Keys.LEFT:
                    	if (selected > 0) {
                            setSelected(selected-1);
                        } else {
                        	setSelected(buttons.length - 1);
                        }
                        break;
                    case Keys.RIGHT:
                    	if (selected < buttons.length - 1) {
                        	setSelected(selected+1);
                        } else {
                        	setSelected(0);
                        }
                        break;
                    case Keys.FIRE:
                        return buttons[selected].invokePressed(true, isFocused);
                    default:
                        return isFocused;
                }
        }
        
        // scrolling is not implemented for rows (maybe yet)
//        int selectedH = btnH * selected;
//        if (selectedH - btnH < scrollOffset) {
//            initAnimationThread();
//            animationThread.animate(0, scrollOffset, 0, Math.max(0, selectedH - btnH * 3 / 4), 200);
//        }
//        
//        if (selectedH + btnH > scrollOffset + h) {
//            initAnimationThread();
//            animationThread.animate(0, scrollOffset, 0, Math.min(btnH*buttons.length - h, selectedH - h + btnH + btnH * 3 / 4), 200);
//        }

        if (isSelectionEnabled) {
            isSelectionVisible = true;
        }
        
        return true;
    }
    
    public void onPaint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
        if (buttons == null || buttons.length == 0) {
            return;
        }

        for (int i = 0; i < buttons.length; i++) {
            boolean drawAsSelected = (i == selected && isSelectionVisible && isSelectionEnabled);
            buttons[i].paint(g, x0 + i*w/buttons.length, y0, w/buttons.length, h, drawAsSelected, isFocused, forceInactive);
        }
    }

}