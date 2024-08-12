/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author vipaol
 */
public abstract class Container implements IContainer, IUIComponent, IPopupFeedback {
    
    private IUIComponent[] components;
    protected int x0, y0, w, h, prevX0, prevY0, prevW, prevH,
            anchorX0, anchorY0,
            anchor = IUIComponent.LEFT | IUIComponent.TOP;
    protected boolean isVisible = true;
    protected boolean isFocused = true;
    private boolean dragged = false;
    protected int pressedX, pressedY;
    private int bgColor = 0x000000;
    private boolean roundBg = false;
    private int padding;
    protected Image bg = null;
    private AbstractPopupWindow popupWindow = null;
    protected IContainer parent = null;
    
    public Container() {
        components = new IUIComponent[0];
        init();
    }
    
    public void init() {
        
    }

    public IUIComponent setBgColor(int bgColor) {
        this.bgColor = bgColor;
        return this;
    }
    
    public IUIComponent roundBg(boolean b) {
    	roundBg = b;
    	return this;
    }
    
    public IUIComponent setPadding(int padding) {
        this.padding = padding;
        return this;
    }

    public Container setBgImage(Image bg) {
        this.bg = bg;
        return this;
    }
    
    public Image getCapture() {
        Image capture = Image.createImage(w, h);
        onPaint(capture.getGraphics());
        return capture;
    }

    protected final Container setComponents(IUIComponent[] components) {
    	prevW = prevH = 0; // next setBounds call after the components have changed shouldn't be ignored
        if (this.components != null) {
            for (int i = 0; i < this.components.length; i++) {
                if (this.components[i] != null) {
                    //this.components[i].setParent(null);
                }
            }
        }
        
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (components[i] != null) {
                	System.out.println(getClass().getName() + " set as parent for " + components[i].getClass().getName());
                    components[i].setParent(this);
                }
            }
        }
        
        this.components = components;
        refreshFocusedComponents();
        return this;
    }
    
    protected final IUIComponent[] getComponents() {
    	if (components.length == 0) {
    		try {
    			throw new IllegalStateException("No components in the container. Did you forgot to call setComponents()?");
    		} catch (IllegalStateException ex) {
    			ex.printStackTrace();
    		}
    	}
        return components;
    }
    
    public void showPopup(AbstractPopupWindow w) {
        popupWindow = w;
        popupWindow.setParent(this);
        refreshSizes();
        repaint();
    }
    
    public void closePopup() {
        this.popupWindow = null;
        refreshSizes();
        repaint();
    }
    
    public void paint(Graphics g) {
    	paint(g, x0, y0, w, h);
    }
    
    public void paint(Graphics g, int x0, int y0, int w, int h) {
        if (!isVisible) {
            return;
        }
        
        onPaint(g, x0, y0, w, h);
    }
    
    private void onPaint(Graphics g) {
    	onPaint(g, x0, y0, w, h);
    }
    
    private void onPaint(Graphics g, int x0, int y0, int w, int h) {
    	if (w == 0 || h == 0) {
    		try {
    			throw new Exception("Can't paint: w=" + w + " h=" + h);
    		} catch (Exception ex) {
				ex.printStackTrace();
			}
    		return;
    	}
    	
    	x0 += padding;
        y0 += padding;
        w -= padding*2;
        h -= padding*2;
        
        if (w <= 0 || h <= 0) {
        	return;
        }
    	
        IUIComponent[] uiComponents = getComponents();
        
        int prevClipX = g.getClipX();
        int prevClipY = g.getClipY();
        int prevClipW = g.getClipWidth();
        int prevClipH = g.getClipHeight();
        g.setClip(x0, y0, w, h);
        drawBg(g, x0, y0, w, h);
        setBounds(x0, y0, w, h);
        for (int i = 0; i < uiComponents.length; i++) {
            if (uiComponents[i] != null) {
                uiComponents[i].paint(g);
            }
        }
        
        if (popupWindow != null) {
            popupWindow.paint(g, x0, y0, w, h);
        }
        
        g.setClip(prevClipX, prevClipY, prevClipW, prevClipH);
    }
    
    protected void drawBg(Graphics g, int x0, int y0, int w, int h) {
    	if (bgColor >= 0) {
            g.setColor(bgColor);
            if (roundBg) {
	            int r = Math.min(w/5, h/5);
	            g.fillRoundRect(x0, y0, w, h, r, r);
            } else {
            	g.fillRect(x0, y0, w, h);
            }
        }
        
        if (bg != null) {
            g.drawImage(bg, x0 + w/2, y0 + h/2, Graphics.VCENTER | Graphics.HCENTER);
        }
    }
    
    public IUIComponent refreshSizes() {
        setSize(w, h);
        return this;
    }

    public boolean canBeFocused() {
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] != null) {
                if (components[i].canBeFocused()) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void refreshFocusedComponents() {
        for (int i = 0; i < components.length; i++) {
            if (components[i] != null) {
                components[i].setFocused(false);
            }
        }
        
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] != null) {
                if (components[i].canBeFocused()) {
                    components[i].setFocused(true);
                    break;
                }
            }
        }
    }

    public IUIComponent setFocused(boolean b) {
        isFocused = b;
        refreshFocusedComponents();
        return this;
    }

    public IUIComponent setVisible(boolean b) {
        isVisible = b;
        return this;
    }
    
    public boolean toggleIsVisible() {
        setVisible(!isVisible);
        return isVisible;
    }
    
    public boolean getIsVisible() {
        return isVisible;
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

    public boolean pointerReleased(int x, int y) {
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        if (dragged) {
            dragged = false;
            return false;
        }
        
        if (popupWindow != null) {
            boolean isTarget = popupWindow.checkTouchEvent(x, y);
            popupWindow.pointerReleased(x, y);
            repaint();
            if (isTarget) {
                return true;
            } else {
                System.out.println("oj");
            }
        }
        
        IUIComponent[] uiComponents = getComponents();
        
        try {
            for (int i = uiComponents.length - 1; i >= 0; i--) {
                if (uiComponents[i] == null) {
                    continue;
                }
                if (uiComponents[i].pointerReleased(x, y)) {
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return false;
    }

    public boolean pointerDragged(int x, int y) {
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        if (Math.abs(x - pressedX) + Math.abs(y - pressedY) < 5) {
            return false;
        }
        
        dragged = true;
        
        if (popupWindow != null) {
            boolean isTarget = popupWindow.checkTouchEvent(x, y);
            popupWindow.pointerDragged(x, y);
            if (isTarget) {
                repaint();
                return true;
            }
        }
        
        IUIComponent[] uiComponents = getComponents();
        
        try {
            for (int i = uiComponents.length - 1; i >= 0; i--) {
                if (uiComponents[i] == null) {
                    continue;
                }
                if (uiComponents[i].pointerDragged(x, y)) {
                    repaint();
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean pointerPressed(int x, int y) {
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        pressedX = x;
        pressedY = y;
        
        if (popupWindow != null) {
            boolean isTarget = popupWindow.checkTouchEvent(x, y);
            popupWindow.pointerPressed(x, y);
            if (isTarget) {
                repaint();
                return true;
            }
        }
        
        IUIComponent[] uiComponents = getComponents();
        
        try {
            for (int i = uiComponents.length - 1; i >= 0; i--) {
                if (uiComponents[i] == null) {
                    continue;
                }
                if (uiComponents[i].pointerPressed(x, y)) {
                    repaint();
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean keyPressed(int keyCode, int count) {
        if (!isVisible) {
            return false;
        }
        
        if (popupWindow != null) {
            popupWindow.keyPressed(keyCode, count);
            repaint();
            return true;
        }
        
        IUIComponent[] uiComponents = getComponents();
        try {
            for (int i = uiComponents.length - 1; i >= 0; i--) {
                if (uiComponents[i] == null) {
                    continue;
                }
                if (uiComponents[i].keyPressed(keyCode, count)) {
                    repaint();
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return false;
    }

    public boolean keyRepeated(int keyCode, int pressedCount) {
        if (!isVisible) {
            return false;
        }
        
        if (popupWindow != null) {
            popupWindow.keyRepeated(keyCode, pressedCount);
            repaint();
            return true;
        }
        
        IUIComponent[] uiComponents = getComponents();
        try {
            for (int i = uiComponents.length - 1; i >= 0; i--) {
                if (uiComponents[i] == null) {
                    continue;
                }
                if (uiComponents[i].keyRepeated(keyCode, pressedCount)) {
                    repaint();
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public IUIComponent setPos(int x0, int y0) {
        this.x0 = x0;
        this.y0 = y0;
        return this;
    }
    
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
        
        setPos(x0, y0);
        setBounds(x0, y0, w, h);
        
        return this;
    }

    public IUIComponent setSize(int w, int h) {
        this.w = w;
        this.h = h;
        
        setPos(anchorX0, anchorY0, anchor);
        
        return this;
    }
    
    private final void setBounds(int x0, int y0, int w, int h) {
    	if (w == 0 || h == 0) {
            try {
                throw new Exception("Setting zero as a dimension");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
    	if (x0 == prevX0 && y0 == prevY0 && w == prevW && h == prevH) {
    		return;
    	}
    	
    	prevW = this.w = w;
    	prevH = this.h = h;
        prevX0 = this.x0 = x0;
        prevY0 = this.y0 = y0;
        
        if (popupWindow != null) {
            popupWindow.setSize(w, h).setPos(x0, y0, LEFT | TOP);
        }
        
        onSetBounds(x0, y0, w, h);
    }

    public IUIComponent setParent(IContainer parent) {
        this.parent = parent;
        return this;
    }
    
    public boolean hasParent() {
        return parent != null;
    }
    
    public void repaint() {
        if (parent != null) {
            parent.repaint();
        } else {
            try {
                throw new NullPointerException("Can't call parent's repaint: parent component is not set! " + getClass().getName());
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    protected abstract void onSetBounds(int x0, int y0, int w, int h);
    
}
