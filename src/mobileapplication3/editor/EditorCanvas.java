/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.editor.ui.UIComponent;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import mobileapplication3.elements.Element;

/**
 *
 * @author vipaol
 */
public class EditorCanvas extends UIComponent {
    
    public static final int ZOOMOUT_MACROVIEW_THRESHOLD = 200;
    private StructureBuilder structurePlacer;
    private Car car = new Car();
    private int bgColor = 0x000000;
    private int cursorX, cursorY = 0;
    private int offsetX, offsetY;
    private int zoomOut = 8192;
    private int keyRepeats = 0;
    
    PointerHandler pointerHandler = new PointerHandler();

    public EditorCanvas(StructureBuilder structure) {
        this.structurePlacer = structure;
    }
    
    public void onPaint(Graphics g) {
        g.setColor(bgColor);
        drawBG(g);
        drawElements(g);
        drawStartPoint(g);
        drawCursor(g);
        car.drawCar(g);
        if (structurePlacer.placingNow != null) {
            g.setColor(0xaaffaa);
            g.drawString(structurePlacer.placingNow.getInfoStr(), 0, 0, 0);
        }
    }
    
    private void drawBG(Graphics g) {
        g.fillRect(x0, y0, w, h);
        if (zoomOut < ZOOMOUT_MACROVIEW_THRESHOLD) {
            g.setColor(0x000077);
            int step = 1000/zoomOut;
            int gridOffsetY = x0 + (h/2) % step;
            int gridOffsetX = y0 + (w/2) % step;
            for (int y = gridOffsetY; y < h; y+=step) {
                g.drawLine(0, y, w, y);
            }
            for (int x = gridOffsetX; x < w; x+=step) {
                g.drawLine(x, 0, x, h);
            }
        }
    }
    
    private void drawCursor(Graphics g) {
        int x = xToPX(cursorX);
        int y = yToPX(cursorY);
        int r = 2;
        g.setColor(0x22aa22);
        g.drawArc(x - r, y - r, r*2, r*2, 0, 360);
        g.drawString(cursorX + " " + cursorY, x, y + r, Graphics.TOP | Graphics.LEFT);
    }
    
    private void drawElements(Graphics g) {
        if (structurePlacer.getElementsCount() == 0) {
            return;
        }
        for (int i = 0; i < structurePlacer.buffer.size(); i++) {
            if (((Element) structurePlacer.buffer.elementAt(i)) == structurePlacer.placingNow) {
                g.setColor(0xaaffff);
            } else {
                g.setColor(0xffffff);
            }
            ((Element) structurePlacer.buffer.elementAt(i)).paint(g, zoomOut, offsetX, offsetY);
        }
    }
    
    private void drawStartPoint(Graphics g) {
        int d = 2;
        g.setColor(0x00ff00);
        g.fillRect(xToPX(0) - d, yToPX(0) - d, d*2, d*2);
    }

    public void onSetBounds(int x0, int y0, int w, int h) {
        recalcOffset();
    }
    
    public boolean canBeFocused() {
        return true;
    }

    public boolean handlePointerPressed(int x, int y) {
        pointerHandler.handlePointerPressed(x, y);
        return true;
    }

    public boolean handlePointerReleased(int x, int y) {
        pointerHandler.dragged = false;
        pointerHandler.handlePointerReleased(x, y);
        return true;
    }

    public boolean handlePointerDragged(int x, int y) {
        pointerHandler.handlePointerDragged(x, y);
        return true;
    }

    public boolean handleKeyPressed(int keyCode, int count) {
        System.out.println("count=" + count);
        if (count > 10) {
            count = 10;
        }
        
        int minStep = count * count;
        switch (Main.util.getGameAction(keyCode)) {
            case Canvas.UP:
                cursorY -= Math.max(minStep, zoomOut / 1000);
                break;
            case Canvas.DOWN:
                cursorY += Math.max(minStep, zoomOut / 1000);
                break;
            case Canvas.LEFT:
                cursorX -= Math.max(minStep, zoomOut / 1000);
                break;
            case Canvas.RIGHT:
                cursorX += Math.max(minStep, zoomOut / 1000);
                break;
            case Canvas.FIRE:
                structurePlacer.handleNextPoint((short) cursorX, (short) cursorY, false);
                break;
            default:
                switch (keyCode) {
                    /*case Canvas.KEY_NUM1:
                        cursorX--;
                        cursorY--;
                        break;
                    case Canvas.KEY_NUM3:
                        cursorX++;
                        cursorY--;
                        break;
                    case Canvas.KEY_NUM7:
                        cursorX--;
                        cursorY++;
                        break;
                    case Canvas.KEY_NUM9:
                        cursorX++;
                        cursorY++;
                        break;*/
                    default:
                        return false;
                }
        }
        pointerHandler.onCursorMove();
        keyRepeats = 0;
        return true;
    }
    
    public boolean handleKeyRepeated(int keyCode, int pressedCount) {
        // TODO: rewrite to cursormove
        int minStep = Math.max(zoomOut / 1000, 1);
        int a = keyRepeats;
        System.out.println(a);
        int step = (minStep + a*8) * pressedCount;
        switch (Main.util.getGameAction(keyCode)) {
            case Canvas.UP:
                cursorY -= step;
                break;
            case Canvas.DOWN:
                cursorY += step;
                break;
            case Canvas.LEFT:
                cursorX -= step;
                break;
            case Canvas.RIGHT:
                cursorX += step;
                break;
            case Canvas.FIRE:
                break;
            default:
                switch (keyCode) {
                    /*case Canvas.KEY_NUM1:
                        cursorX -= a;
                        cursorY -= a;
                        break;
                    case Canvas.KEY_NUM3:
                        cursorX += a;
                        cursorY -= a;
                        break;
                    case Canvas.KEY_NUM7:
                        cursorX -= a;
                        cursorY += a;
                        break;
                    case Canvas.KEY_NUM9:
                        cursorX += a;
                        cursorY += a;
                        break;*/
                    default:
                        return false;
                }
        }
        pointerHandler.onCursorMove();
        keyRepeats = Math.min(15, keyRepeats + 1);
        return true;
    }

    public int getCursorX() {
        return cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }
    
    class Car {
        int carbodyLength = 240;
        int carbodyHeight = 40;
        int wr = 40;
        int carX = 0 - (carbodyLength / 2 - wr);
        int carY = 0 - wr / 2 * 3 - 2;
        int lwX = carX - (carbodyLength / 2 - wr);
        int lwY = carY + wr / 2;
        int rwX = carX + (carbodyLength / 2 - wr);
        int rwY = carY + wr / 2;
    
        void drawCar(Graphics g) {
            g.setColor(0x444444);
            g.drawRect(xToPX(carX - carbodyLength / 2),
                    yToPX(carY - carbodyHeight / 2),
                    carbodyLength*1000/zoomOut,
                    carbodyHeight*1000/zoomOut);
            lwX = xToPX(carX - (carbodyLength / 2 - wr));
            lwY = yToPX(carY + wr / 2);
            rwX = xToPX(carX + (carbodyLength / 2 - wr));
            rwY = yToPX(carY + wr / 2);
            
            int wrScaled = wr * 1000 / zoomOut;
            g.setColor(bgColor);
            g.fillArc(lwX - wrScaled, lwY - wrScaled, wrScaled*2, wrScaled*2, 0, 360);
            g.fillArc(rwX - wrScaled, rwY - wrScaled, wrScaled*2, wrScaled*2, 0, 360);
            g.setColor(0x444444);
            g.drawArc(lwX - wrScaled, lwY - wrScaled, wrScaled*2, wrScaled*2, 0, 360);
            g.drawArc(rwX - wrScaled, rwY - wrScaled, wrScaled*2, wrScaled*2, 0, 360);
            
            int lineEndX = carX - carbodyLength / 2 - wr / 2;
            int lineStartX = lineEndX - wr;
            int lineY = carY + carbodyHeight / 3;
            g.drawLine(xToPX(lineStartX), yToPX(lineY), xToPX(lineEndX), yToPX(lineY));
            lineStartX += carbodyHeight / 3;
            lineEndX += carbodyHeight / 3;
            lineY += carbodyHeight / 3;
            g.drawLine(xToPX(lineStartX), yToPX(lineY), xToPX(lineEndX), yToPX(lineY));
            lineStartX -= carbodyHeight * 2 / 3;
            lineEndX -= carbodyHeight * 2 / 3;
            lineY -= carbodyHeight * 2 / 3;
            g.drawLine(xToPX(lineStartX), yToPX(lineY), xToPX(lineEndX), yToPX(lineY));
        }
    }
    
    class PointerHandler {
        public boolean dragged = false;
        int pressedX, pressedY;
        int prevCursorX, prevCursorY;
        int prevOffsetX, prevOffsetY;
        int lastCursorX = 0;
        int lastCursorY = 0;
        
        void handlePointerPressed(int x, int y) {
            if (!isVisible) {
                return;
            }
            
            pressedX = x;
            pressedY = y;
            prevOffsetX = offsetX;
            prevOffsetY = offsetY;
            prevCursorX = cursorX;
            prevCursorY = cursorY;
        }

        void handlePointerDragged(int x, int y) {
            if (!isVisible) {
                return;
            }
            
            int dx = x - pressedX;
            int dy = y - pressedY;
            
            lastCursorX = cursorX;
            lastCursorY = cursorY;
            cursorX = prevCursorX + dx * zoomOut / 1000;
            cursorY = prevCursorY + dy * zoomOut / 1000;
            
            onCursorMove();
            dragged = (dx != 0 || dy != 0);
        }

        void handlePointerReleased(int x, int y) {
            if (!isVisible) {
                return;
            }
            
            if (!dragged) {
                structurePlacer.handleNextPoint((short) cursorX, (short) cursorY, false);
            }
            dragged = false;
        }
        
        void onCursorMove() {
            if (zoomOut < 500 && (lastCursorX != cursorX || lastCursorY != cursorY)) {
                Main.vibrate(1);
            }

            recalcOffset();

            structurePlacer.handleNextPoint((short) cursorX, (short) cursorY, true);
        }
    }
    
    void zoomIn() {
        int newZoomOut = zoomOut / 2;
        if (newZoomOut > 15) {
            zoomOut = newZoomOut;
        }
        recalcOffset();
    }
    
    void zoomOut() {
        int newZoomOut = zoomOut * 2;
        if (newZoomOut < 200000) {
            zoomOut = newZoomOut;
        }
        recalcOffset();
    }
    
    void recalcOffset() {
        offsetX = w/2 - cursorX * 1000 / zoomOut;
        offsetY = h/2 - cursorY * 1000 / zoomOut;
    }
    
    public int xToPX(int c) {
        return c * 1000 / zoomOut + offsetX;
    }

    public int yToPX(int c) {
        return c * 1000 / zoomOut + offsetY;
    }
}
