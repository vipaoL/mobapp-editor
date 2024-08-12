/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import mobileapplication3.utils.Mathh;

/**
 *
 * @author vipaol
 */
public class Sine extends AbstractCurve {
    
	private static final String[] ARGS_NAMES = {"X0", "Y0", "Length", "Halfperiods", "Phase shift", "Amplitude"};
    private short x0, y0, l, halfperiods = 1, offset = 90, amp;
    private short anchorX, anchorY;
    
    public PlacementStep[] getPlacementSteps() {
        return new PlacementStep[] {
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setAnchorPoint(pointX, pointY);
                    calcStartPoint();
                }

                public String getName() {
                    return "Move start point";
                }
                
                public String getCurrentStepInfo() {
					return "x0=" + x0 + " y0=" + y0;
				}
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setLength((short) (pointX - x0));
                    setAmplitude((short) (-1000*(pointY - anchorY)/(1000+Mathh.sin(offset))));
                    calcStartPoint();
                }

                public String getName() {
                    return "Change horizontal length";
                }
                
                public String getCurrentStepInfo() {
					return "l=" + l;
				}
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                	int dx = pointX - anchorX;
                	if (dx * l > 0) {
                		setHalfperiodsNumber((short) Math.max(1, l/dx));
                	}
                }

                public String getName() {
                    return "Change number of halfperiods";
                }
                
                public String getCurrentStepInfo() {
					return "halfperiods=" + halfperiods;
				}
            }
        };
    }

    public PlacementStep[] getExtraEditingSteps() {
        return new PlacementStep[0];
    }
    
    public void setAnchorPoint(short x, short y) {
        if (anchorX == x && anchorY == y) {
            return;
        }
        pointsCache = null;
        anchorX = x;
        anchorY = y;
    }
    
    public void calcStartPoint() {
        setStartPoint(anchorX, (short) (anchorY - amp*Mathh.sin(offset)/1000));
    }
    
    public void setStartPoint(short x, short y) {
        if (x0 == x && y0 == y) {
            return;
        }
        if (pointsCache != null) {
        	pointsCache.movePoints((short) (x - x0), (short) (y - y0));
        }
        x0 = x;
        y0 = y;
    }
    
    public void setLength(short l) {
        if (this.l == l) {
            return;
        }
        pointsCache = null;
        this.l = l;
    }
    
    public void setHalfperiodsNumber(short n) {
        if (halfperiods == n) {
            return;
        }
        pointsCache = null;
        halfperiods = n;
    }

    public void setOffset(short offset) throws IllegalArgumentException {
        if (this.offset == offset) {
            return;
        }
        pointsCache = null;
        if (offset > 360 || offset < 0) {
            throw new IllegalArgumentException("Offset can't be < 0 or be > 360");
        }
        this.offset = offset;
    }
    
    public void setAmplitude(short a) {
        if (amp == a) {
            return;
        }
        pointsCache = null;
        amp = a;
    }

    public Element setArgs(short[] args) {
        x0 = args[0];
        y0 = args[1];
        l = args[2];
        halfperiods = args[3];
        offset = args[4];
        amp = args[5];
        pointsCache = null;
        return this;
    }
    
    public short[] getArgs() {
        return new short[]{x0, y0, l, halfperiods, offset, amp};
    }
    
    public String[] getArgsNames() {
    	return ARGS_NAMES;
    }
    
    public short getID() {
        return Element.SINE;
    }

    public int getStepsToPlace() {
        return 3;
    }

    public String getName() {
        return "Sine";
    }
    
    public void move(short dx, short dy) {
    	x0 += dx;
    	y0 += dy;
    	pointsCache.movePoints(dx, dy);
    }
    
    public short[] getStartPoint() {
        return new short[]{x0, y0};
    }

    public short[] getEndPoint() throws Exception {
        return new short[]{(short) (x0 + l), (short) (y0 + amp*Mathh.sin(offset+180*halfperiods)/1000)};
    }
    
    protected void genPoints() {
        if (amp == 0) {
            pointsCache = new PointsCache(2);
            pointsCache.writePointToCache(x0, y0);
            pointsCache.writePointToCache(x0 + l, y0);
        } else {
            int step = 30;
            int startA = offset;
            int endA = offset + halfperiods * 180;
            int a = endA - startA;

            int nextPointX;
            int nextPointY;
            pointsCache = new PointsCache(1 + halfperiods*6);
            for (int i = startA; i <= endA; i+=step) {
                nextPointX = x0 + (i - startA)*l/a;
                nextPointY = y0 + amp*Mathh.sin(i)/1000;
                pointsCache.writePointToCache(nextPointX, nextPointY);
            }
            
            if (a % step != 0) {
                nextPointX = x0 + l;
                nextPointY = y0 + amp*Mathh.sin(endA)/1000;
                pointsCache.writePointToCache(nextPointX, nextPointY);
            }
        }
    }
    
    public void recalcCalculatedArgs() { }
    
}
