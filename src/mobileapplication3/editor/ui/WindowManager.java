/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public class WindowManager extends Canvas {
    
    public int w, h;
    boolean dragged = false;
    int pressedX, pressedY;
    int bgColor = 0x000000;
    UIComponent[] uiComponents = new UIComponent[]{};
    
    public void updateUI(UIComponent[] uIComponents) {
        this.uiComponents = uIComponents;
        repaint();
    }
    
    public void showDialog(String question, Button[] answers) {
        
    }
    
    protected void paint(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, w, h);
        for (int i = 0; i < uiComponents.length; i++) {
            uiComponents[i].repaint(g);
        }
    }
    
    protected void keyPressed(int keyCode) {
        for (int i = uiComponents.length - 1; i >= 0; i--) {
            if (uiComponents[i].handleKeyPressed(keyCode)) {
                break;
            }
        }
        
        repaint();
    }
    
    protected void keyRepeated(int keyCode) {
        for (int i = uiComponents.length - 1; i >= 0; i--) {
            if (uiComponents[i].handleKeyRepeated(keyCode)) {
                break;
            }
        }
        
        repaint();
    }
    
    protected void pointerPressed(int x, int y) {
        pressedX = x;
        pressedY = y;
        for (int i = uiComponents.length - 1; i >= 0; i--) {
            if (uiComponents[i].handlePointerPressed(x, y)) {
                break;
            }
        }
        repaint();
    }
    
    protected void pointerDragged(int x, int y) {
        if (Math.abs(x - pressedX) + Math.abs(y - pressedY) < 5) {
            return;
        }
        
        dragged = true;
        for (int i = uiComponents.length - 1; i >= 0; i--) {
            if (uiComponents[i].handlePointerDragged(x, y)) {
                break;
            }
        }
        repaint();
    }
    
    protected void pointerReleased(int x, int y) {
        if (dragged) {
            dragged = false;
            return;
        }
        
        for (int i = uiComponents.length - 1; i >= 0; i--) {
            if (uiComponents[i].handlePointerReleased(x, y)) {
                break;
            }
        }
        
        repaint();
    }
    
}
