/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import mobileapplication3.editor.Utils;

/**
 *
 * @author vipaol
 */
public class Button {

    private String name;
    private boolean isActive = true;
    private ButtonFeedback buttonFeedback;
    private int bgColor = -1;
    private int bgColorInactive = 0x202020;
    private int fontColor = 0xffffff;
    private int fontColorInactive = 0x404040;
    private int selectedColor = 0x002255;
    private int bgPadding = 0;
    
    public Button(String name, ButtonFeedback buttonFeedback) {
        this.name = name;
        this.buttonFeedback = buttonFeedback;
    }
    
    public void invokePressed(boolean isSelected) {
        if (isActive) {
            if (!isSelected) {
                buttonFeedback.buttonPressed();
            } else {
                buttonFeedback.buttonPressedSelected();
            }
        }
    }
    
    public Button setIsActive(boolean b) {
        isActive = b;
        return this;
    }
    
    public boolean isActive() {
        return isActive;
    }

    public Button setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public Button setBgPadding(int bgPadding) {
        this.bgPadding = bgPadding;
        return this;
    }
    
    public void setName(String s) {
        name = s;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }
    
    public abstract static class ButtonFeedback {
        public abstract void buttonPressed();
        public void buttonPressedSelected() {
            buttonPressed();
        }
    }
    
    public void paint(Graphics g, int x0, int y0, int w, int h, boolean isSelected) {
        int prevClipX = g.getClipX();
        int prevClipY = g.getClipY();
        int prevClipW = g.getClipWidth();
        int prevClipH = g.getClipHeight();
        g.setClip(x0, y0, w, h);
        x0 += bgPadding;
        y0 += bgPadding;
        w -= bgPadding*2;
        h -= bgPadding*2;
        Font prevFont = g.getFont();
        
        if (isActive) {
            int bgColor = isSelected ? selectedColor : this.bgColor;
            if (bgColor > 0) {
                g.setColor(bgColor);
                g.fillRect(x0, y0, w, h);
            }
            g.setColor(fontColor);
        } else {
            if (bgColorInactive > 0) {
                g.setColor(bgColorInactive);
                g.fillRect(x0, y0, w, h);
            }
            g.setColor(fontColorInactive);
        }
        
        StringBuffer sb = (new StringBuffer(name));
        String[] splitted = Utils.split(sb, "\n");
        
        if (splitted.length > 1) {
            if (h < g.getFont().getHeight() * 2) {
                g.setFont(Font.getFont(prevFont.getFace(), prevFont.getStyle(), Font.SIZE_SMALL));
            }
            g.drawString(splitted[0], x0 + w/2, y0+h/2, Graphics.HCENTER | Graphics.BOTTOM);
            g.drawString(splitted[1], x0 + w/2, y0+h/2, Graphics.HCENTER | Graphics.TOP);
        } else {
            if (h < g.getFont().getHeight()) {
                g.setFont(Font.getFont(prevFont.getFace(), prevFont.getStyle(), Font.SIZE_SMALL));
            }
            g.drawString(name, x0 + w/2, y0+h/2-g.getFont().getHeight()/2, Graphics.HCENTER | Graphics.TOP);
        }
        g.setClip(prevClipX, prevClipY, prevClipW, prevClipH);
        g.setFont(prevFont);
    }
    
}
