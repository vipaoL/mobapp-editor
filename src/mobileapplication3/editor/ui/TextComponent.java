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
public class TextComponent extends UIComponent {
    
    public static final int HEIGHT_AUTO = -1;
    private String text = null;
    private int[][] lineBounds = null;
    private int prevW, prevH;
    public Font font;
    public int padding;
    
    public TextComponent() {
        font = Font.getDefaultFont();
        padding = font.getHeight()/6;
        bgColor = 0x000055;
    }
    
    public TextComponent(String text) {
        this();
        this.text = text;
    }
    
    private int getOptimalHeight() {
        return font.getHeight() * (getLineBounds(text, font, w, padding).length) + font.getHeight() / 2;
    }

    public void onPaint(Graphics g) {
        int[][] lineBounds = getLineBounds(text, font, w, padding);
        
        int offset = 0;
        int step = font.getHeight() * 3 / 2;
        if (step * lineBounds.length > h - padding * 2) {
            //step = (h - padding * 2) / (lineBounds.length+1);
            if (step * lineBounds.length > h - padding * 2) {
                step = h / (lineBounds.length);
            }
        }
        
        g.setColor(0xffffff);
        for (int i = 0; i < lineBounds.length; i++) {
            int[] bounds = lineBounds[i];
            g.drawSubstring(text, bounds[0], bounds[1], x0 + padding, y0 + padding + offset, 0);
            offset += step;
        }
        
        //g.drawString(text, x0 + padding, y0 + padding, Graphics.TOP | Graphics.LEFT);
    }
    
    private int[][] getLineBounds(String text, Font font, int w, int padding) {
        if (lineBounds != null && w == prevW && h == prevH) {
            return lineBounds;
        }
        
        prevW = w;
        prevH = h;
        
        return Utils.getLineBounds(text, font, w, padding);
    }
    
    public boolean canBeFocused() {
        return false;
    }
    
    public boolean handlePointerReleased(int x, int y) {
        return false;
    }

    public boolean handleKeyPressed(int keyCode, int count) {
        return false;
    }

    public String getText() {
        return text;
    }

    public TextComponent setText(String text) {
        this.text = text;
        return this;
    }

    public void onSetBounds(int x0, int y0, int w, int h) {
        this.w = w;
        if (h == HEIGHT_AUTO) {
            this.h = getOptimalHeight();
        }
    }
    
}
