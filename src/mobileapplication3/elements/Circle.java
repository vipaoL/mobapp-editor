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
public class Circle extends Element {
    
    private final int SEGMENTS_IN_CIRCLE = 36;       // how many lines will draw up a circle
    private final int CIRCLE_SEGMENT_LEN = 360 / SEGMENTS_IN_CIRCLE;
    
    short x, y, r, arcAngle, startAngle, kx, ky;
    
    PointsCache pointsCache;
    
    public void placePoint(int i, short pointX, short pointY) throws IllegalArgumentException {
        switch (i) {
            case 0:
                setCenter(pointX, pointY);
                break;
            case 1:
                short dx = (short) (pointX - x);
                short dy = (short) (pointY - y);
                setRadius(calcDistance(dx, dy));
                setArc((short) 360, (short) 0);
                setScale((short) 100, (short) 100);
                break;
            case 2:
                throw new IllegalArgumentException("setting start angle is not supported yet");
            case 3:
                throw new IllegalArgumentException("setting arc angle is not supported yet");
            default:
                throw new IllegalArgumentException();
        }
        pointsCache = null;
    }
    
    public Element setCenter(short x, short y) {
        this.x = x;
        this.y = y;
        pointsCache = null;
        return this;
    }
    
    public Element setRadius(short r) {
        this.r = r;
        pointsCache = null;
        return this;
    }
    
    public Element setArc(short arcAngle, short startAngle) {
        this.arcAngle = arcAngle;
        this.startAngle = startAngle;
        pointsCache = null;
        return this;
    }
    
    public Element setScale(short scaleX, short scaleY) {
        this.kx = scaleX;
        this.ky = scaleY;
        pointsCache = null;
        return this;
    }
    
    public void paint(Graphics g, int zoomOut, int offsetX, int offsetY) {
        if (pointsCache == null) {
            genPoints();
        }
        
        short[] startPoint = pointsCache.getPoint(0);
        for (int i = 0; i < pointsCache.getSize() - 1; i++) {
            short[] endPoint = pointsCache.getPoint(i+1);
            Utils.drawLine(g, xToPX(startPoint[0], zoomOut, offsetX), yToPX(startPoint[1], zoomOut, offsetY), xToPX(endPoint[0], zoomOut, offsetX), yToPX(endPoint[1], zoomOut, offsetY), 24, zoomOut);
            startPoint = endPoint;
        }
    }
    
    public Element setArgs(short[] args) {
        x = args[0];
        y = args[1];
        r = args[2];
        arcAngle = args[3];
        startAngle = args[4];
        kx = args[5];
        ky = args[6];
        pointsCache = null;
        return this;
    }
    
    public short[] getArgs() {
        short[] args = {x, y, r, arcAngle, startAngle, kx, ky};
        return args;
    }
    
    public short getID() {
        return Element.LINE;
    }
    
    public int getStepsToPlace() {
        return 2;
    }

    public String getName() {
        return "Circle";
    }

    public String getInfoStr() {
        return "r=" + r;
    }
    
    public short[] getEndPoint() {
        return new short[]{(short) (x + r), y};
    }
    
    private void genPoints() { //k: 10 = 1.0
        int pointsNumber = arcAngle/CIRCLE_SEGMENT_LEN + 1;
        if (arcAngle % CIRCLE_SEGMENT_LEN != 0) {
            pointsNumber += 1;
        }
        pointsCache = new PointsCache(pointsNumber);
        
        while (startAngle < 0) {
            startAngle += 360;
        }
        
        for(int i = 0; i <= arcAngle; i+=CIRCLE_SEGMENT_LEN) {
            pointsCache.writePointToCache(x+Mathh.cos(i+startAngle)*kx*r/1000/100, y+Mathh.sin(i+startAngle)*ky*r/1000/100);
        }
        
        if (arcAngle % CIRCLE_SEGMENT_LEN != 0) {
            pointsCache.writePointToCache(Mathh.cos(arcAngle+startAngle)*kx*r/1000/100,
                    y+Mathh.sin(arcAngle+startAngle)*ky*r/1000/100);
        }
    }

    private class PointsCache {
        
        short[][] pointsCache;
        int cacheCarriage = 0;

        public PointsCache(int length) {
            pointsCache = new short[length][2];
        }

        private void writePointToCache(int x, int y) {
            pointsCache[cacheCarriage][0] = (short) x;
            pointsCache[cacheCarriage][1] = (short) y;
            cacheCarriage += 1;
        }

        public int getSize() {
            return cacheCarriage;
        }
        
        public short[] getPoint(int i) {
            return pointsCache[i];
        }
    
    }
    
}
