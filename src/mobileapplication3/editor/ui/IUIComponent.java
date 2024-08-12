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
public interface IUIComponent {
    public static final int 
            TOP = Graphics.TOP,
            BOTTOM = Graphics.BOTTOM,
            LEFT = Graphics.LEFT,
            RIGHT = Graphics.RIGHT,
            HCENTER = Graphics.HCENTER,
            VCENTER = Graphics.VCENTER,
            COLOR_TRANSPARENT = -1,
            NOT_SET = -2,
            KEYCODE_LEFT_SOFT = -6,
            KEYCODE_RIGHT_SOFT = -7,
            COLOR_ACCENT = 0x000055,
            COLOR_ACCENT_MUTED = 0x101020;
    
    
    public IUIComponent setParent(IContainer parent);
    public IUIComponent setPos(int x0, int y0, int anchor);
    public IUIComponent setSize(int w, int h);
    public IUIComponent setVisible(boolean b);
    public IUIComponent setFocused(boolean b);
    public IUIComponent setBgColor(int color);
    public IUIComponent setPadding(int padding);
    public IUIComponent roundBg(boolean b);
    public boolean getIsVisible();
    public void paint(Graphics g);
    public void paint(Graphics g, int x0, int y0, int w, int h);
    public boolean canBeFocused();
    public boolean pointerReleased(int x, int y);
    public boolean pointerDragged(int x, int y);
    public boolean pointerPressed(int x, int y);
    public boolean keyPressed(int keyCode, int count);
    public boolean keyRepeated(int keyCode, int pressedCount);
}
