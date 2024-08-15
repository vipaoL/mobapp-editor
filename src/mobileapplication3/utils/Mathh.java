/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.utils;

/**
 *
 * @author vipaol
 */
public class Mathh {
    private static int sin_t[] = {0, 174, 342, 500, 643, 766, 866, 940, 985, 1000};
    //private static int tg_t[] = {0, 105, 212, 324, 445, 577, 726, 900, 1110, 1376, 1732, 2246, 3077, 4704, 9514};
    private static int tg_t[] = {0, 17, 34, 52, 69, 87, 105, 122, 140, 158, 176,
        194, 212, 230, 249, 267, 286, 305, 324, 344, 363, 383, 404, 424, 445,
        466, 487, 509, 531, 554, 577, 600, 624, 649, 674, 700, 726, 753,
        781, 809, 839, 869, 900, 932, 965, 999, 1035, 1072, 1110, 1150, 1191,
        1234, 1279, 1327, 1376, 1428, 1482, 1539, 1600, 1664, 1732, 1804, 1880,
        1962, 2050, 2144, 2246, 2355, 2475, 2605, 2747, 2904, 3077, 3270, 3487,
        3732, 4010, 4331, 4704, 5144, 5671, 6313, 7115, 8144, 9514, 11430, 14300,
        19081, 28636, 57289, Integer.MAX_VALUE};
    private static int sinus(int t) {
        int k;
        k = (int) (t / 10);
        if (t % 10 == 0) {
            return sin_t[k];
        } else {
            return (int) ((sin_t[k + 1] - sin_t[k]) * (t % 10) / 10 + sin_t[k]);
        }
    }

    public static int sin(int t) {
        int sign = 1;
        t = t % 360;
        if (t < 0)
        {
            t = -t;
            sign = -1;
        }
        if (t <= 90) {
            return sign * sinus(t);
        } else if (t <= 180) {
            return sign * sinus(180 - t);
        } else if (t <= 270) {
            return -sign * sinus(t - 180);
        } else {
            return -sign * sinus(360 - t);
        }
    }

    public static int cos(int t) {
        t = t % 360;
        if (t < 0) {
            t = -t;
        }
        if (t <= 90) {
            return sinus(90 - t);
        } else if (t <= 180) {
            return -sinus(t - 90);
        } else if (t <= 270) {
            return -sinus(270 - t);
        } else {
            return sinus(t - 270);
        }
    }
    
    public static int arctg(int x, int y) {
        if (x == 0) {
            if (y > 0) {
                return 90;
            } else {
                return 270;
            }
        }
        if (y == 0) {
            if (x > 0) {
                return 0;
            } else {
                return 180;
            }
        }
        int tg = Math.abs(1000 * y / x);
        int ang = search(tg_t, tg);
        if (y >= 0) {
            if (x < 0) {
                ang = 180 - ang;
            }
        } else {
            if (x < 0) {
                ang += 180;
            } else {
                ang = 360 - ang;
            }
        }
        return ang;
    }
    
    public static int search(int[] nums, int target) {
        int l = 0;
        int r = nums.length - 1;
        int mid = 0;
        while (l <= r) {
            mid = (l + r) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return mid;
  }
    
    static int distance(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
    static boolean strictIneq(int leftBound, int a, int rightBound) {
        return ((leftBound < a) & (a < rightBound));
    }
    
    public static int constrain(int leftBound, int a, int rightBound) {
        return Math.min(Math.max(leftBound, a), rightBound);
    }
    
    public static boolean isPointOnArc(int a, int startAngle, int arcAngle) {
		if (Math.abs(arcAngle) >= 360) {
			return true;
		}
		
		if (arcAngle < 0) {
			arcAngle = -arcAngle;
			startAngle = -startAngle;
			a = -a;
		}
		
		a = normalizeAngle(a);
		startAngle = normalizeAngle(startAngle);
		if (a < startAngle) {
			a += 360;
		}
		
		return a >= startAngle && a <= startAngle + arcAngle;
	}
	
	public static int normalizeAngle(int a) {
    	a %= 360;
		if (a < 0) {
			a += 360;
		}
        return a;
    }
	
	public static short calcDistance(int dx, int dy) {
        if (dy == 0) {
            return (short) Math.abs(dx);
        } else if (dx == 0) {
            return (short) Math.abs(dy);
        } else {
            return (short) Math.sqrt((double) (dx*dx + dy*dy));
        }
    }
}
