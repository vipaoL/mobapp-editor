/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import javax.microedition.lcdui.Graphics;
import mobileapplication3.editor.Utils;

/**
 *
 * @author vipaol
 */
public class Line extends Element {
    
    short x1, y1, x2, y2;
    
    public Line() {
        id = Element.LINE;
    }
    
    public void placePoint(int i, short x, short y) throws IllegalArgumentException {
        switch (i) {
            case 0:
                setStartPoint(x, y);
                break;
            case 1:
                setEndPoint(x, y);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public void setStartPoint(short x, short y) {
        x1 = x;
        y1 = y;
    }
    
    public void setEndPoint(short x, short y) {
        x2 = x;
        y2 = y;
    }
    
    public void paint(Graphics g, int zoomOut, int offsetX, int offsetY) {
        Utils.drawLine(g, xToPX(x1, zoomOut, offsetX), yToPX(y1, zoomOut, offsetY), xToPX(x2, zoomOut, offsetX), yToPX(y2, zoomOut, offsetY), 24, zoomOut);
    }

    public short[] getArgs() {
        short[] args = {x1, y1, x2, y2};
        return args;
    }

    public Element setArgs(short[] args) {
        x1 = args[0];
        y1 = args[1];
        x2 = args[2];
        y2 = args[3];
        return this;
    }

    public String getInfoStr() {
        return x1 + " " + y1 + " " + x2 + " " + y2;
    }

    public String getName() {
        return "Line";
    }
    
    public short[] getEndPoint() {
        return new short[]{x2, y2};
    }
    
}
