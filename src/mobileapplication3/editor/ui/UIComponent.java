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
public abstract class UIComponent implements IUIComponent {
    
    protected boolean isVisible = true;
    protected boolean isFocused = true;
    public int x0, y0, w, h,
            anchorX0, anchorY0,
            anchor = IUIComponent.LEFT | IUIComponent.TOP;
    private boolean isSizeSet = false;
    protected int bgColor;
    private IContainer parent = null;
    
    public final void paint(Graphics g) {
        if (isVisible) {
            int prevClipX = g.getClipX();
            int prevClipY = g.getClipY();
            int prevClipW = g.getClipWidth();
            int prevClipH = g.getClipHeight();
            
            g.setClip(x0, y0, w, h);
            
            drawBg(g);
            onPaint(g);
            
            g.setClip(prevClipX, prevClipY, prevClipW, prevClipH);
        }
    }
    
    public void drawBg(Graphics g) {
        if (bgColor >= 0) {
            g.setColor(bgColor);
            g.fillRect(x0, y0, w, h);
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
    
    public IUIComponent setFocused(boolean b) {
        isFocused = b;
        return this;
    }
    
    public IUIComponent setVisible(boolean b) {
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
    
    public void recalcSize() {
        setSize(w, h);
    }
    
    public void recalcPos() {
        setPos(anchorX0, anchorY0, anchor);
    }
    
//    public IUIComponent setPos(int x0, int y0) {
//        this.x0 = x0;
//        this.y0 = y0;
//        anchorX0 = x0;
//        anchorY0 = y0;
//        return this;
//    }
    
    public IUIComponent setPos(int x0, int y0, int anchor) {
        anchorX0 = x0;
        anchorY0 = y0;
        this.anchor = anchor;
        
        if ((anchor & IUIComponent.RIGHT) != 0) {
            x0 -= w;
        } else if ((anchor & IUIComponent.HCENTER) != 0) {
            x0 -= w/2;
        }
        if ((anchor & IUIComponent.BOTTOM) != 0) {
            y0 -= h;
        } else if ((anchor & IUIComponent.VCENTER) != 0) {
            y0 -= h/2;
        }
        
        setBounds(x0, y0, w, h);
        
        return this;
    }
    
    public IUIComponent setSize(int w, int h) {
        if (w != 0 && h != 0) {
            isSizeSet = true;
        } else {
            try {
                throw new Exception("Setting zero as a dimension");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        this.w = w;
        this.h = h;
        
        recalcPos();
        return this;
    }
    
    private final void setBounds(int x0, int y0, int w, int h) {
        this.w = w;
        this.h = h;
        this.x0 = x0;
        this.y0 = y0;
        
        onSetBounds(x0, y0, w, h);
    }
    
    protected void onSetBounds(int x0, int y0, int w, int h) { }
    
    public UIComponent setBgColor(int color) {
        bgColor = color;
        return this;
    }
    
    public int getLeftX() {
        return x0;
    }
    
    public int getRightX() {
        return x0 + w;
    }
    
    public int getTopY() {
        return y0;
    }
    
    public int getBottomY() {
        return y0 + h;
    }
    
    public int getWidth() {
        return w;
    }
    
    public int getHeight() {
        return h;
    }

    public boolean isSizeSet() {
        return isSizeSet;
    }
    
    public IUIComponent setParent(IContainer parent) {
        this.parent = parent;
        return this;
    }
    
    public void repaint() {
        if (parent != null) {
            parent.repaint();
        } else {
            try {
                throw new NullPointerException("Can't call parent's repaint: parent component is not set!");
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public boolean pointerReleased(int x, int y) {
        if (!(isVisible && checkTouchEvent(x, y))) {
            return false;
        }
        return handlePointerReleased(x, y);
    }
    
    public boolean pointerDragged(int x, int y) {
        if (!(isVisible && checkTouchEvent(x, y))) {
            return false;
        }
        return handlePointerDragged(x, y);
    }
    
    public boolean pointerPressed(int x, int y) {
        if (!(isVisible && checkTouchEvent(x, y))) {
            return false;
        }
        return handlePointerPressed(x, y);
    }
    
    public boolean keyPressed(int keyCode, int count) {
        if (!isVisible) {
            return false;
        }
        return handleKeyPressed(keyCode, count);
    }
    
    public boolean keyRepeated(int keyCode, int pressedCount) {
        if (!isVisible) {
            return false;
        }
        return handleKeyRepeated(keyCode, pressedCount);
    }

    protected boolean handleKeyRepeated(int keyCode, int pressedCount) {
        return false;
    }
    
    protected boolean handlePointerDragged(int x, int y) {
        return false;
    }
    
    protected boolean handlePointerPressed(int x, int y) {
        return false;
    }
    
    protected abstract boolean handlePointerReleased(int x, int y);
    protected abstract boolean handleKeyPressed(int keyCode, int count);
    protected abstract void onPaint(Graphics g);
    
}
