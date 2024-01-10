/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import javax.microedition.lcdui.Graphics;
import mobileapplication3.Mathh;
import mobileapplication3.editor.Utils;

/**
 *
 * @author vipaol
 */
public class Sine extends Element {
    
    short x0, y0, l, halfperiods = 1, offset = 90, amp;
    short anchorX, anchorY;
    short[][] pointsCache = null;
    
    public Sine() {
        id = Element.SINE;
    }
    
    public void placePoint(int i, short pointX, short pointY) throws IllegalArgumentException {
        switch (i) {
            case 0:
                setAnchorPoint(pointX, pointY);
                break;
            case 1:
                setLength((short) (pointX - x0));
                setAmplitude((short) (-1000*(pointY - anchorY)/(1000+Mathh.sin(offset))));
                calcStartPoint();
                break;
            case 2:
                setHalfperiodsNumber((short) Math.max(1, l/(pointX - anchorX)));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public void setAnchorPoint(short x, short y) {
        anchorX = x;
        anchorY = y;
    }
    
    public void calcStartPoint() {
        setStartPoint(anchorX, (short) (anchorY - amp*Mathh.sin(offset)/1000));
    }
    
    public void setStartPoint(short x, short y) {
        x0 = x;
        y0 = y;
    }
    
    public void setLength(short l) {
        this.l = l;
    }
    
    public void setHalfperiodsNumber(short n) {
        halfperiods = n;
    }

    public void setOffset(short offset) throws IllegalArgumentException {
        if (offset > 360 || offset < 0) {
            throw new IllegalArgumentException("Offset can't be < 0 or be > 360");
        }
        this.offset = offset;
    }
    
    public void setAmplitude(short a) {
        amp = a;
    }

    public void paint(Graphics g, int zoomOut, int offsetX, int offsetY) {
        if (amp == 0) {
            Utils.drawLine(g, xToPX(x0, zoomOut, offsetX), yToPX(y0, zoomOut, offsetY), xToPX(x0 + l, zoomOut, offsetX), yToPX(y0, zoomOut, offsetY), 24, zoomOut);
        } else {
            int sl = 90;
            int prevPointX = x0;
            int nextPointX;
            int prevPointY = y0 + amp * Mathh.sin(offset) / 1000;
            int nextPointY;
            for(int i = sl; i < l - sl/2; i+=sl) {
                nextPointX = x0 + i;
                nextPointY = y0 + amp*Mathh.sin(180*i*halfperiods/l+offset)/1000;
                Utils.drawLine(g, xToPX(prevPointX, zoomOut, offsetX), yToPX(prevPointY, zoomOut, offsetY), xToPX(nextPointX, zoomOut, offsetX), yToPX(nextPointY, zoomOut, offsetY), 24, zoomOut);
                prevPointX = nextPointX;
                prevPointY = nextPointY;
            }
            
            nextPointX = x0 + l;
            nextPointY = y0 + amp*Mathh.sin(180*halfperiods+offset)/1000;
            Utils.drawLine(g, xToPX(prevPointX, zoomOut, offsetX), yToPX(prevPointY, zoomOut, offsetY), xToPX(nextPointX, zoomOut, offsetX), yToPX(nextPointY, zoomOut, offsetY), 24, zoomOut);
        }
    }

    public Element setArgs(short[] args) {
        x0 = args[0];
        y0 = args[1];
        l = args[2];
        halfperiods = args[3];
        offset = args[4];
        amp = args[5];
        return this;
    }
    
    public short[] getArgs() {
        return new short[]{x0, y0, l, halfperiods, offset, amp};
    }

    public String getInfoStr() {
        return "periods/2=" + halfperiods + " amp=" + amp + " off=" + offset;
    }

    public String getName() {
        return "Sine";
    }

    public short[] getEndPoint() throws Exception {
        return new short[]{(short) (x0 + l), (short) (y0 + amp*Mathh.sin(offset+180*halfperiods)/1000)};
    }
    
}
