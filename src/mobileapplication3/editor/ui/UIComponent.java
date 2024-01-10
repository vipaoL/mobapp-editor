/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public abstract class UIComponent {
    
    public static final int TOP = Graphics.TOP;
    public static final int BOTTOM = Graphics.BOTTOM;
    
    protected boolean isVisible = true;
    public int x0, y0, w, h;
    
    public void repaint(Graphics g) {
        if (isVisible) {
            paint(g);
        }
    }
    
    protected boolean checkTouchEvent(int x, int y) {
        if (!isVisible) {
            return false;
        }
        
        if (x < x0 || y < y0) {
            return false;
        }
        
        if (x - x0 > w || y - y0 > h) {
            return false;
        }
        
        return true;
    }
    
    public UIComponent setVisible(boolean b) {
        isVisible = b;
        return this;
    }
    
    public boolean getIsVisible() {
        return isVisible;
    }
    
    public boolean toggleIsVisible() {
        setVisible(!isVisible);
        return isVisible;
    }
    
    public abstract void paint(Graphics g);
    public abstract boolean handlePointerReleased(int x, int y);
    public abstract boolean handleKeyPressed(int keyCode);

    public boolean handleKeyRepeated(int keyCode) {
        return false;
    }
    
    public boolean handlePointerDragged(int x, int y) {
        return false;
    }
    
    public boolean handlePointerPressed(int x, int y) {
        return false;
    }
    
}
