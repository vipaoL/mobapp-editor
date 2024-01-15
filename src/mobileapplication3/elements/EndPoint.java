/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public class EndPoint extends Element {
    
    short x, y;
    
    public void placePoint(int i, short pointX, short pointY) throws IllegalArgumentException {
        switch (i) {
            case 0:
                x = pointX;
                y = pointY;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public void paint(Graphics g, int zoomOut, int offsetX, int offsetY) {
        int r = 3;
        int prevColor = g.getColor();
        g.setColor(0xff0000);
        g.fillArc(xToPX(x, zoomOut, offsetX) - r, yToPX(y, zoomOut, offsetY) - r, r*2, r*2, 0, 360);
        g.setColor(prevColor);
    }

    public Element setArgs(short[] args) {
        x = args[0];
        y = args[1];
        return this;
    }
    
    public short[] getArgs() {
        short[] args = {x, y};
        return args;
    }

    public short getID() {
        return Element.END_POINT;
    }

    public int getStepsToPlace() {
        return 1;
    }
    
    public String getName() {
        return "End point";
    }
    
    public short[] getEndPoint() throws Exception {
        throw new Exception("Never ask end point its end point");
    }
    
    public static boolean compare(short[] oldEndPoint, short[] newEndPoint) {
        short oldX = oldEndPoint[0];
        short oldY = oldEndPoint[1];
        short newX = newEndPoint[0];
        short newY = newEndPoint[1];
        if (newX >= oldX) {
            if (newX > oldX || (newY > oldY)) {
                return true;
            }
        }
        return false;
    }
    
}
