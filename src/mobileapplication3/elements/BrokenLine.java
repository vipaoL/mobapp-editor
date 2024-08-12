/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import javax.microedition.lcdui.Graphics;
import mobileapplication3.utils.Mathh;
import mobileapplication3.utils.Utils;

/**
 *
 * @author vipaol
 */
public class BrokenLine extends Line {
    
	private static final String[] ARGS_NAMES = {"X1", "Y1", "X2", "Y2", "Thickness", "Platform length (calculated)", "Spacing", "Length (calculated)", "Angle (calculated)"};
    protected short thickness = 20, platformLength, spacing = 10, l, ang;
    
    public PlacementStep[] getPlacementSteps() {
        return new PlacementStep[] {
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setStartPoint(pointX, pointY);
                }

                public String getName() {
                    return "Move start point";
                }
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setEndPoint(pointX, pointY);
                    recalcCalculatedArgs();
                }

                public String getName() {
                    return "Move end point";
                }
            }
        };
    }
    
    public void recalcCalculatedArgs() {
    	short dx = (short) (x2 - x1);
        short dy = (short) (y2 - y1);
        if (dy == 0) {
            l = dx;
        } else if (dx == 0) {
            l = dy;
        } else {
            l = calcDistance(dx, dy);
        }
        if (l <= 0) {
            l = 1;
        }
        short optimalPlatfL = 260;
        platformLength = optimalPlatfL;
        if (platformLength > l) {
            platformLength = l;
        } else {
            short platfL1 = platformLength;
            while ((l + spacing) % (platformLength + spacing) != 0 & platformLength < l & (l + spacing) % (platfL1 + spacing) != 0) {
                platformLength++;
                if (platfL1 > 5)
                    platfL1--;
            }
            if ((l + spacing) % (platformLength + spacing) == 0) {
                platfL1 = platformLength;
            }
            platformLength = platfL1;
        }
        if (platformLength <= 0)
            platformLength = l;
        ang = (short) Mathh.arctg(dx, dy);
    }

    public PlacementStep[] getExtraEditingSteps() {
        return new PlacementStep[0];
    }
    
    public void paint(Graphics g, int zoomOut, int offsetX, int offsetY) {
        int dx = x2 - x1;
        int dy = y2 - y1;
                
        int n = (l + spacing) / (platformLength + spacing);

        int spX = spacing * dx / l;
        int spY = spacing * dy / l;

        int platfDx = (dx+spX) / n;
        int platfDy = (dy+spY) / n;
        
        for (int i = 0; i < n; i++) {
            Utils.drawLine(g, xToPX(x1 + i * platfDx, zoomOut, offsetX),
                    yToPX(y1 + i * platfDy, zoomOut, offsetY),
                    xToPX(x1 + (i + 1) * platfDx - spX, zoomOut, offsetX),
                    yToPX(y1 + (i + 1) * platfDy - spY, zoomOut, offsetY),
                    thickness,
                    zoomOut,
                    false,
                    true);
        }
    }
    
    public Element setArgs(short[] args) {
        x1 = args[0];
        y1 = args[1];
        x2 = args[2];
        y2 = args[3];
        thickness = args[4];
        platformLength = args[5];
        spacing = args[6];
        l = args[7];
        ang = args[8];
        return this;
    }
    
    public short[] getArgs() {
        return new short[]{x1, y1, x2, y2, thickness, platformLength, spacing, l, ang};
    }
    
    public String[] getArgsNames() {
    	return ARGS_NAMES;
    }

    public short getID() {
        return Element.BROKEN_LINE;
    }

    public int getStepsToPlace() {
        return 2;
    }

    public String getName() {
        return "Broken Line";
    }
    
    public String getInfoStr() {
        String info = "plL=" + platformLength + " ang=" + ang;
        return info;
    }
    
    public boolean isBody() {
		return true;
	}
}
