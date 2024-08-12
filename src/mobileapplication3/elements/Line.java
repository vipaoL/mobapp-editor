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
public class Line extends Element {
    
	private static final String[] ARGS_NAMES = {"X1", "Y1", "X2", "Y2"};
    protected short x1, y1, x2, y2;
    
    public PlacementStep[] getPlacementSteps() {
        return new PlacementStep[] {
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setStartPoint(pointX, pointY);
                }

                public String getName() {
                    return "Start Point";
                }
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setEndPoint(pointX, pointY);
                }

                public String getName() {
                    return "End Point";
                }
            }
        };
    }

    public PlacementStep[] getExtraEditingSteps() {
        return new PlacementStep[0];
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
    
    public Element setArgs(short[] args) {
        x1 = args[0];
        y1 = args[1];
        x2 = args[2];
        y2 = args[3];
        return this;
    }

    public short[] getArgs() {
        short[] args = {x1, y1, x2, y2};
        return args;
    }
    
    public String[] getArgsNames() {
    	return ARGS_NAMES;
    }
    
    public short getID() {
        return Element.LINE;
    }
    
    public int getStepsToPlace() {
        return 2;
    }

    public String getName() {
        return "Line";
    }
    
    public String getInfoStr() {
        return x1 + " " + y1 + " " + x2 + " " + y2;
    }
    
    public short[] getEndPoint() {
        return new short[]{x2, y2};
    }

	public boolean isBody() {
		return false;
	}
	
	public void recalcCalculatedArgs() { }
    
}
