/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.elements;

import mobileapplication3.Mathh;

/**
 *
 * @author vipaol
 */
public class Sine extends AbstractCurve {
    
    short x0, y0, l, halfperiods = 1, offset = 90, amp;
    short anchorX, anchorY;
    
    public PlacementStep[] getPlacementSteps() {
        return new PlacementStep[] {
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setAnchorPoint(pointX, pointY);
                }

                public String getName() {
                    return "Move";
                }
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setLength((short) (pointX - x0));
                    setAmplitude((short) (-1000*(pointY - anchorY)/(1000+Mathh.sin(offset))));
                    calcStartPoint();
                }

                public String getName() {
                    return "2nd Point";
                }
            },
            new PlacementStep() {
                public void place(short pointX, short pointY) {
                    setHalfperiodsNumber((short) Math.max(1, l/(pointX - anchorX)));
                }

                public String getName() {
                    return "Periods";
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
        pointsCache = null;
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
    
    public short getID() {
        return Element.SINE;
    }

    public int getStepsToPlace() {
        return 3;
    }

    public String getName() {
        return "Sine";
    }
    
    public String getInfoStr() {
        return "periods/2=" + halfperiods + " amp=" + amp + " off=" + offset;
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
            int sl = 90;
            pointsCache = new PointsCache(1 + (l-sl/2)/sl + 1);
            int nextPointX;
            int nextPointY;
            for(int i = 0; i < l - sl/2; i+=sl) {
                nextPointX = x0 + i;
                nextPointY = y0 + amp*Mathh.sin(180*i*halfperiods/l+offset)/1000;
                pointsCache.writePointToCache(nextPointX, nextPointY);
            }
            
            pointsCache.writePointToCache(x0 + l, y0 + amp*Mathh.sin(180*halfperiods+offset)/1000);
        }
    }
    
}
