/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import javax.microedition.lcdui.Graphics;
import mobileapplication3.utils.Utils;

/**
 *
 * @author vipaol
 */
public abstract class AbstractCurve extends Element {
    
    PointsCache pointsCache;

    public AbstractCurve() {
        pointsCache = null;
    }
    
    abstract void genPoints();
    
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
    
    protected class PointsCache {
        
        short[][] pointsCache;
        int cacheCarriage = 0;

        PointsCache(int length) {
            pointsCache = new short[length][2];
        }

        public void writePointToCache(int x, int y) {
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
