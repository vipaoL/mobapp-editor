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
public class Circle extends AbstractCurve {
    
	private static final String[] ARGS_NAMES = {"X", "Y", "R", "Arc angle", "Start angle", "kX", "kY"};
    private short x, y, r, arcAngle = 360, startAngle, kx = 100, ky = 100;
    
    public PlacementStep[] getPlacementSteps() {
        return new PlacementStep[] {
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setCenter(pointX, pointY);
                }

                public String getName() {
                    return "Move";
                }

				public String getCurrentStepInfo() {
					return "x=" + x + " y=" + y;
				}
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    short dx = (short) (pointX - x);
                    short dy = (short) (pointY - y);
                    setRadius(calcDistance(dx, dy));
                }

                public String getName() {
                    return "Change radius";
                }
                
                public String getCurrentStepInfo() {
					return "r=" + r;
				}
            }
        };
    }

    public PlacementStep[] getExtraEditingSteps() {
    	final short centerX = x;
    	final short centerY = y;
    	final short startAngle = this.startAngle;
        return new PlacementStep[] {
            new PlacementStep() {
                public void place(short x, short y) {
                	short ang = (short) (Mathh.arctg(x - centerX, y - centerY) - startAngle);
                	ang %= 360;
                	while (ang < 1) {
                		ang += 360;
                	}
                	setArcAngle((short) ang);
                }

                public String getName() {
                    return "Change angle";
                }
                
                public String getCurrentStepInfo() {
					return "ang=" + arcAngle;
				}
            },
            new PlacementStep() {
                public void place(short x, short y) {
                	short ang = (short) Mathh.arctg(x - centerX, y - centerY);
                	setStartAngle(ang);
                }

                public String getName() {
                    return "Change start angle";
                }
                
                public String getCurrentStepInfo() {
					return "startAng=" + startAngle;
				}
            }
        };
    }
    
    public Element setCenter(short x, short y) {
        if (this.x == x && this.y == y) {
            return this;
        }
        if (pointsCache != null) {
        	pointsCache.movePoints((short) (x - this.x), (short) (y - this.y));
        }
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Element setRadius(short r) {
        if (this.r == r) {
            return this;
        }
        this.r = r;
        pointsCache = null;
        return this;
    }
    
    public Element setArc(short arcAngle, short startAngle) {
        setArcAngle(arcAngle);
        setStartAngle(startAngle);
        return this;
    }
    
    public Element setArcAngle(short arcAngle) {
        if (this.arcAngle == arcAngle) {
            return this;
        }
        this.arcAngle = arcAngle;
        pointsCache = null;
        return this;
    }
    
    public Element setStartAngle(short startAngle) {
        while (startAngle < 0) {
            startAngle += 360;
        }
        startAngle%=360;
        
        if (this.startAngle == startAngle) {
            return this;
        }
        this.startAngle = startAngle;
        pointsCache = null;
        return this;
    }
    
    public Element setScale(short scaleX, short scaleY) {
        if (this.kx == scaleX && this.ky == scaleY) {
            return this;
        }
        this.kx = scaleX;
        this.ky = scaleY;
        pointsCache = null;
        return this;
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
    
    public String[] getArgsNames() {
    	return ARGS_NAMES;
    }
    
    public short getID() {
        return Element.CIRCLE;
    }
    
    public int getStepsToPlace() {
        return 2;
    }

    public String getName() {
        return "Circle";
    }
    
    public void move(short dx, short dy) {
    	x += dx;
    	y += dy;
    }
    
    public short[] getStartPoint() {
        return new short[]{(short) (x - r), y};
    }
    
    public short[] getEndPoint() {
        return new short[]{(short) (x + r), y};
    }
    
    protected void genPoints() { //k: 100 = 1.0
        // calculated formula. r=20: sn=5,sl=72; r=1000: sn=36,sl=10
        int circleSegmentLen=10000/(140+r);
        circleSegmentLen = Math.min(72, Math.max(10, circleSegmentLen));
        int pointsNumber = arcAngle/circleSegmentLen + 1;
        if (arcAngle % circleSegmentLen != 0) {
            pointsNumber += 1;
        }
        pointsCache = new PointsCache(pointsNumber);
        
        int startAngle = this.startAngle;
        
        for(int i = 0; i <= arcAngle; i+=circleSegmentLen) {
            pointsCache.writePointToCache(x+Mathh.cos(i+startAngle)*kx*r/100000, y+Mathh.sin(i+startAngle)*ky*r/100000);
        }
        
        if (arcAngle % circleSegmentLen != 0) {
            pointsCache.writePointToCache(x+Mathh.cos(startAngle+arcAngle)*kx*r/100000, y+Mathh.sin(startAngle+arcAngle)*ky*r/100000);
        }
    }
    
    public void recalcCalculatedArgs() { }
    
}
