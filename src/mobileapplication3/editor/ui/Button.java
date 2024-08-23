/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import mobileapplication3.editor.ui.platform.Font;
import mobileapplication3.editor.ui.platform.Graphics;
import mobileapplication3.utils.Utils;

/**
 *
 * @author vipaol
 */
public abstract class Button {

    private String text;
    private boolean isActive = true;
    protected int bgColor;
    protected int bgColorInactive;
    protected int fontColor;
    protected int fontColorInactive;
    protected int selectedBgColor;
    private int bgPadding;
    
    private int[][] lineBounds = null;
    private int prevW;
    public Font font;
    private Font prevGetLineBoundsFont;
    
    public Button(String title) {
        setFont(Font.getDefaultFontSize());
        this.bgPadding = 0;
        this.selectedBgColor = 0x002255;
        this.fontColorInactive = IUIComponent.FONT_COLOR_INACTIVE;
        this.fontColor = IUIComponent.FONT_COLOR;
        this.bgColorInactive = IUIComponent.BG_COLOR_INACTIVE;
        this.bgColor = IUIComponent.COLOR_ACCENT_MUTED;
        
        this.text = title;
        setTitle(getTitle());
    }
    
    public boolean invokePressed(boolean isSelected, boolean isFocused) {
        if (isActive) {
            if (!isSelected) {
                buttonPressed();
            } else {
                buttonPressedSelected();
            }
            setTitle(getTitle());
            return true;
        }
        
        return false;
    }
    
    protected void setFont(int size, Graphics g) {
    	setFont(size);
    	g.setFont(font);
	}
    
    public void setFont(int size) {
		font = new Font(size);
	}
    
    public Button setIsActive(boolean b) {
        isActive = b;
        return this;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public Button setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}
    
    public int getFontColor() {
		return fontColor;
	}
    
    public Button setFontColorInactive(int fontColorInactive) {
		this.fontColorInactive = fontColorInactive;
		return this;
	}
    
    public int getFontColorInactive() {
		return fontColorInactive;
	}
    
    public int getBgColor() {
        return bgColor;
    }

    public Button setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }
    
    public int getBgColorInactive() {
		return bgColorInactive;
	}
    
    public Button setBgColorInactive(int bgColorInactive) {
    	this.bgColorInactive = bgColorInactive;
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
    
    public String getTitle() {
        return text;
    }
    
    public String toString() {
        return text;
    }
    
    public void paint(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused, boolean drawAsInactive) {
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
        
        drawBg(g, x0, y0, w, h, isSelected, drawAsInactive);
        drawText(g, x0, y0, w, h, isSelected, isFocused, drawAsInactive);
        drawSelectionMark(g, x0, y0, w, h, isSelected, isFocused, drawAsInactive);
        
        g.setClip(prevClipX, prevClipY, prevClipW, prevClipH);
    }
    
    protected void drawSelectionMark(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused, boolean forceInactive) {
    	if (isSelected) {
            g.setColor(getCurrentFontColor(forceInactive));
            int markY0 = h / 3;
            int markY1 = h - markY0;
            int markCenterY = (markY0 + markY1) / 2;
            int markw = (markY1 - markY0) / 2;
            g.fillTriangle(x0 + 1, y0 + markY0, x0 + 1, y0 + markY1, x0 + markw, y0 + markCenterY);
            g.fillTriangle(x0 + w - 1, y0 + markY0, x0 + w - 1, y0 + markY1, x0 + w - markw, y0 + markCenterY);
        }
	}
    
    protected void drawText(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused, boolean forceInactive) {
    	Font prevFont = g.getFont();
        setFont(Font.getDefaultFontSize());
        
        int[][] lineBounds = getLineBounds(text, font, w, bgPadding);
        if (h / lineBounds.length < font.getHeight()) {
        	setFont(Font.SIZE_MEDIUM);
        	lineBounds = getLineBounds(text, font, w, bgPadding);
        	if (h / lineBounds.length < font.getHeight()) {
            	setFont(Font.SIZE_SMALL);
            	lineBounds = getLineBounds(text, font, w, bgPadding);
            }
        }
        setFont(font.getSize(), g);

        g.setColor(getCurrentFontColor(forceInactive));
        
        int offset = 0;
        int step = font.getHeight() * 3 / 2;
        if (step * lineBounds.length > h - bgPadding * 2) {
        	step = h / (lineBounds.length);
        }
        
        offset += (h-step*(lineBounds.length - 1) - font.getHeight())/2;
        for (int i = 0; i < lineBounds.length; i++) {
            int[] bounds = lineBounds[i];
            g.drawSubstring(text, bounds[0], bounds[1], x0 + w/2, y0 + offset, Graphics.HCENTER | Graphics.TOP);
            offset += step;
        }
        
        g.setFont(prevFont);
	}
    
    protected void drawBg(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean forceInactive) {
    	int r = Math.min(w/5, h/5);
        
        int bgColor;
        if (isActive && !forceInactive) {
        	if (!isSelected) {
        		bgColor = this.bgColor;
        	} else {
        		bgColor = selectedBgColor;
        	}
        } else {
        	bgColor = bgColorInactive;
        }
        
        if (bgColor > 0) {
            g.setColor(bgColor);
            //g.fillRect(x0, y0, w, h); // TODO add feature to disable rouding
            g.fillRoundRect(x0, y0, w, h, r, r);
        }
    }
    
    protected int getCurrentFontColor(boolean forceInactive) {
        if (isActive && !forceInactive) {
        	return this.fontColor;
        } else {
        	return fontColorInactive;
        }
    }
    
    private int[][] getLineBounds(String text, Font font, int w, int padding) {
        if (lineBounds != null && w == prevW && font.getSize() == prevGetLineBoundsFont.getSize()) {
            return lineBounds;
        }
        
        prevW = w;
        
        lineBounds = font.getLineBounds(text, w, padding);
        prevGetLineBoundsFont = font;
        return lineBounds;
    }
    
    public int getMinPossibleWidth() {
        int w = 0;
        String[] words = Utils.split(text, " ");
        for (int i = 0; i < words.length; i++) {
            w = Math.max(w, font.stringWidth(words[i]));
        }
        return w;
    }
    
    public abstract void buttonPressed();
    public void buttonPressedSelected() {
        buttonPressed();
    }
    
}
