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
public class RootContainer extends Canvas implements IContainer {
    
    private IUIComponent rootUIComponent = null;
    private KeyboardHelper kbHelper;
    public static boolean displayKbHints = false;
    private int bgColor = 0x000000;
    public int w, h;
    private static RootContainer inst = null;
    private UISettings uiSettings;

    public RootContainer(IUIComponent rootUIComponent, UISettings uiSettings) {
        setFullScreenMode(true);
        this.uiSettings = uiSettings;
        inst = this;
        //        for (int i = 0; i < currentlyPressedKeys.length; i++) {
//            currentlyPressedKeys[i] = Integer.MIN_VALUE;
//        }
        kbHelper = new KeyboardHelper();
        displayKbHints = !hasPointerEvents();
        setRootUIComponent(rootUIComponent);
    }

    public void init() {
    	setRootUIComponent(rootUIComponent);
	}

    public RootContainer setRootUIComponent(IUIComponent rootUIComponent) {
        if (this.rootUIComponent != null) {
            this.rootUIComponent.setParent(null);
        }
        
        if (rootUIComponent != null) {
		    this.rootUIComponent = rootUIComponent.setParent(this).setFocused(true);
		    rootUIComponent.init();
        }
        return this;
    }
    
    public UISettings getUISettings() {
		return uiSettings;
	}

    protected void paint(Graphics g) {
    	if (bgColor >= 0) {
    		g.fillRect(0, 0, w, h);
    	}
        if (rootUIComponent != null) {
            rootUIComponent.paint(g);
        }
    }
    
    public int getBgColor() {
		return bgColor;
	}
    
    public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}
    
    public static int getGameActionn(int keyCode) {
    	return inst.getGameAction(keyCode);
    }
    
//    private void startKeyRepeatedThread() {
//        if (isKeyRepeaterRunning) {
//            return;
//        }
//        
//        System.out.println("starting thread");
//        
//        keyRepeater = new Thread(new Runnable() {
//            public void run() {
//                isKeyRepeaterRunning = true;
//                while(isShown()) {
//                    try {
//                        Thread.sleep(keyRepeaterDelay);
//                        long start = System.currentTimeMillis();
//                        keyRepeaterDelay = 0;
//                        for (int i = 0; i < currentlyPressedKeys.length; i++) {
//                            if (currentlyPressedKeys[i] != Integer.MIN_VALUE) {
//                                handleKeyRepeated(currentlyPressedKeys[i]);
//                            }
//                        }
//
//                        long dt = System.currentTimeMillis() - start;
//                        Thread.sleep(Math.max(0, 100 - dt));
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//                isKeyRepeaterRunning = false;
//            }
//        }, "Key repeater");
//        keyRepeater.start();
//    }
    
    protected void keyPressed(int keyCode) {
        kbHelper.keyPressed(keyCode);
        
        //        for (int i = 0; i < currentlyPressedKeys.length; i++) {
//            if (currentlyPressedKeys[i] == keyCode) {
//                return;
//            }
//        }
//        
//        for (int i = 0; i < currentlyPressedKeys.length; i++) {
//            if (currentlyPressedKeys[i] == Integer.MIN_VALUE) {
//                currentlyPressedKeys[i] = keyCode;
//                break;
//            }
//        }
//        
//        keyRepeaterDelay = 100;
//        startKeyRepeatedThread();
    }
    
    private void handleKeyPressed(int keyCode, int count) {
        if (rootUIComponent != null) {
            rootUIComponent.setVisible(true);
            if (rootUIComponent.keyPressed(keyCode, count)) {
                repaint();
            }
        }
    }

    protected void keyReleased(int keyCode) {
        kbHelper.keyReleased(keyCode);
//        for (int i = 0; i < currentlyPressedKeys.length; i++) {
//            if (currentlyPressedKeys[i] == keyCode) {
//                currentlyPressedKeys[i] = Integer.MIN_VALUE;
//            }
//        }
    }
    
    protected void handleKeyRepeated(int keyCode, int pressedCount) {
        if (getGameAction(keyCode) == Canvas.FIRE) {
            return;
        }
        if (rootUIComponent != null) {
            if (rootUIComponent.keyRepeated(keyCode, pressedCount)) {
                repaint();
            }
        }
    }
    
    protected void pointerPressed(int x, int y) {
        if (rootUIComponent != null) {
            rootUIComponent.setVisible(true);
            if (rootUIComponent.pointerPressed(x, y)) {
                repaint();
            }
        }
    }
    
    protected void pointerDragged(int x, int y) {
        if (rootUIComponent != null) {
            if (rootUIComponent.pointerDragged(x, y)) {
                repaint();
            }
        }
    }
    
    protected void pointerReleased(int x, int y) {
        if (rootUIComponent != null) {
            if (rootUIComponent.pointerReleased(x, y)) {
                repaint();
            }
        }
    }
    
    protected void sizeChanged(int w, int h) {
    	this.w = w;
    	this.h = h;
        if (rootUIComponent != null) {
            rootUIComponent.setSize(w, h);
            repaint();
        }
    }

    protected void showNotify() {
        kbHelper.show();
        if (rootUIComponent != null) {
            rootUIComponent.setVisible(true);
            repaint();
        }
        sizeChanged(getWidth(), getHeight());
    }
    
    protected void hideNotify() {
        kbHelper.hide();
        if (rootUIComponent != null) {
            rootUIComponent.setVisible(false);
            repaint();
        }
    }
    
    private class KeyboardHelper {
        private Object tillPressed = new Object();
        private int lastKey, pressCount;
        private boolean pressState;
        private Thread repeatThread;
        private long lastEvent;

        public void show() {
            pressState = false;
            pressCount = 1;
            lastKey = 0;
            repeatThread = new Thread() {
                public void run() {
                    try {
                        while(true) {
                            if(!pressState) {
                                synchronized(tillPressed) {
                                    tillPressed.wait();
                                }
                            }
                            
                            int k = lastKey;
                            Thread.sleep(200);
                            while (!isLastEventOld()) {
                                Thread.sleep(200);
                            }
                            
                            while(pressState && lastKey == k) {
                                handleKeyRepeated(k, pressCount);
                                Thread.sleep(100);
                            }
                            
                            pressCount = 1;
                        }
                    } catch (InterruptedException e) { }
                }
            };
            repeatThread.start();
        }
        
        public void hide() {
            if(repeatThread != null) {
                repeatThread.interrupt();
            }
        }

        public void keyPressed(int k) {
            if (!isLastEventOld() && k == lastKey) {
                pressCount++;
            } else {
                pressCount = 1;
            }
            
            updateLastEventTime();
            lastKey = k;
            pressState = true;
            synchronized(tillPressed) {
                tillPressed.notify();
            }
            handleKeyPressed(k, pressCount);
        }

        public void keyReleased(int k) {
            updateLastEventTime();
            if(lastKey == k) {
                pressState = false;
            } else {
                pressCount = 0;
            }
        }
        
        private boolean isLastEventOld() {
            return System.currentTimeMillis() - lastEvent > 200;
        }
        
        private void updateLastEventTime() {
            lastEvent = System.currentTimeMillis();
        }
    }
}
