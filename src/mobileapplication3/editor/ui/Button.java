/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import mobileapplication3.utils.Utils;

/**
 *
 * @author vipaol
 */
public class Button {

    private String text;
    private boolean isActive = true;
    private ButtonFeedback buttonFeedback;
    private int bgColor;
    private int bgColorInactive;
    private int fontColor;
    private int fontColorInactive;
    private int selectedBgColor;
    private int bgPadding;
    
    private int[][] lineBounds = null;
    private int prevW, prevH;
    public Font font;
    
    public Button(String title, ButtonFeedback buttonFeedback) {
        font = Font.getDefaultFont();
        this.bgPadding = 0;
        this.selectedBgColor = 0x002255;
        this.fontColorInactive = 0x404040;
        this.fontColor = 0xffffff;
        this.bgColorInactive = 0x202020;
        this.bgColor = IUIComponent.COLOR_ACCENT_MUTED;
        
        this.text = title;
        setTitle(getTitle());
        setFeedback(buttonFeedback);
    }
    
    public boolean invokePressed(boolean isSelected, boolean isFocused) {
        if (buttonFeedback == null) {
            System.out.println(text + isFocused);
            return isFocused;
        }
        
        if (isActive) {
            if (!isSelected) {
                buttonFeedback.buttonPressed();
            } else {
                buttonFeedback.buttonPressedSelected();
            }
            setTitle(getTitle());
            return true;
        }
        
        return false;
    }
    
    public Button setIsActive(boolean b) {
        isActive = b;
        return this;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public int getBgColor() {
        return bgColor;
    }

    public Button setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }
    
    public int getSelectedColor() {
        return selectedBgColor;
    }

    public Button setSelectedColor(int selectedColor) {
        this.selectedBgColor = selectedColor;
        return this;
    }
    
    public int getBgPagging() {
        return bgPadding;
    }

    public Button setBgPadding(int bgPadding) {
        this.bgPadding = bgPadding;
        return this;
    }
    
    public void setTitle(String s) {
        if (s == null) {
            s = "<null>";
        }
        text = s;
    }

    public void setFeedback(ButtonFeedback buttonFeedback) {
        this.buttonFeedback = buttonFeedback;
    }
    
    public String getTitle() {
        return text;
    }
    
    public String toString() {
        return text;
    }
    
    public abstract static class ButtonFeedback {
        public abstract void buttonPressed();
        public void buttonPressedSelected() {
            buttonPressed();
        }
    }
    
    public void paint(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused) {
        int prevClipX = g.getClipX();
        int prevClipY = g.getClipY();
        int prevClipW = g.getClipWidth();
        int prevClipH = g.getClipHeight();
        
        x0 += bgPadding;
        y0 += bgPadding;
        w -= bgPadding*2;
        h -= bgPadding*2;
        if (w <= 0 || h <= 0) {
        	return;
        }
        g.setClip(x0, y0, w, h);
        
        Font prevFont = g.getFont();
        g.setFont(font);
        
        int r = Math.min(w/5, h/5);
        if (isActive) {
            int bgColor = isSelected ? selectedBgColor : this.bgColor;
            if (bgColor > 0) {
                g.setColor(bgColor);
                //g.fillRect(x0, y0, w, h); // TODO add feature to disable rouding
                g.fillRoundRect(x0, y0, w, h, r, r);
            }
            g.setColor(fontColor);
        } else {
            if (bgColorInactive > 0) {
                g.setColor(bgColorInactive);
                //g.fillRect(x0, y0, w, h);
                g.fillRoundRect(x0, y0, w, h, r, r);
            }
            g.setColor(fontColorInactive);
        }
        
        int[][] lineBounds = getLineBounds(text, font, w, h, bgPadding);
        
        int offset = 0;
        int step = font.getHeight() * 3 / 2;
        if (step * lineBounds.length > h - bgPadding * 2) {
            //step = (h - bgPadding * 2) / (lineBounds.length + 1);
            if (step * lineBounds.length > h - bgPadding * 2) {
                step = h / (lineBounds.length);
            }
        }
        
        offset += (h-step*(lineBounds.length - 1) - font.getHeight())/2;
        for (int i = 0; i < lineBounds.length; i++) {
            int[] bounds = lineBounds[i];
            g.drawSubstring(text, bounds[0], bounds[1], x0 + w/2, y0 + offset, Graphics.HCENTER | Graphics.TOP);
            offset += step;
        }
        
        if (isFocused && isSelected) {
            //g.setColor(0xffffff);
            int markY0 = h / 3;
            int markY1 = h - markY0;
            int markCenterY = (markY0 + markY1) / 2;
            int markw = (markY1 - markY0) / 2;
            g.fillTriangle(x0 + 1, y0 + markY0, x0 + 1, y0 + markY1, x0 + markw, y0 + markCenterY);
            g.fillTriangle(x0 + w - 1, y0 + markY0, x0 + w - 1, y0 + markY1, x0 + w - markw, y0 + markCenterY);
        }
        
        g.setClip(prevClipX, prevClipY, prevClipW, prevClipH);
        g.setFont(prevFont);
    }
    
    private int[][] getLineBounds(String text, Font font, int w, int h, int padding) {
        if (lineBounds != null && w == prevW && h == prevH) {
            return lineBounds;
        }
        
        prevW = w;
        prevH = h;
        
        return Utils.getLineBounds(text, font, w, padding);
    }
    
    public int getMinPossibleWidth() {
        int w = 0;
        String[] words = Utils.split(text, " ");
        for (int i = 0; i < words.length; i++) {
            w = Math.max(w, font.stringWidth(words[i]));
        }
        return w;
    }
    
}
