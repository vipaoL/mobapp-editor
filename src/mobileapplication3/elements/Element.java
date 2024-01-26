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
public abstract class Element {
    
    public static final short EOF = 0;
    public static final short END_POINT = 1;
    public static final short LINE = 2;
    public static final short CIRCLE = 3;
    public static final short BROKEN_LINE = 4;
    public static final short BROKEN_CIRCLE = 5;
    public static final short SINE = 6;
    public static final short ACCELERATOR = 7;
    public static final short TRAMPOLINE = 8;
    
    public static final int[] ARGS_NUMBER = {0, /*1*/2, /*2*/4, /*3*/7, /*4*/9, /*5*/10, /*6*/5, /*7*/8, /*7         */8};
    int[] stepsToPlace = {0, /*1*/1, /*2*/2, /*3*/2, /*4*/2, /*5*/2, /*6*/3, /*7*/2, /*8*/2};
    
    protected Element() {}

    public Element createTypedInstance(short id, short[] args) throws IllegalArgumentException {
        if (args.length != ARGS_NUMBER[id]) {
            throw new IllegalArgumentException("Element with id=" + id + " can't have " + args.length + " args");
        }
        
        return createTypedInstance(id).setArgs(args);
    }
    
    public static Element createTypedInstance(short id) throws IllegalArgumentException {
        if (id < 1) {
            throw new IllegalArgumentException("Element id can't be < 1");
        }
        
        switch (id) {
            case Element.END_POINT:
                return new EndPoint();
            case Element.LINE:
                return new Line();
            case Element.CIRCLE:
                return new Circle();
            case Element.BROKEN_LINE:
                return new BrokenLine();
            case Element.BROKEN_CIRCLE:
                return new BrokenCircle();
            case Element.SINE:
                return new Sine();
            case Element.ACCELERATOR:
                return null;
            default:
                System.out.println("Unknown id: " + id);
                return null;
        }
    }
    
    public void printDebug() {
        short[] args = getArgs();
        System.out.print("id="+getID());
        for (int i = 0; i < args.length; i++) {
            System.out.print(" " + args[i]);
        }
        System.out.println("");
    }
    
    public static Element readFromData(short[] data) {
        short id = data[0];
        short[] args = new short[data.length - 1];
        System.arraycopy(data, 1, args, 0, args.length);
        
        if (id < 1) {
            throw new IllegalArgumentException("Element id can't be < 1");
        }
        
        if (args.length != ARGS_NUMBER[id]) {
            throw new IllegalArgumentException("Element with id=" + id + " can't have " + args.length + " args");
        }
        
        return createTypedInstance(id).setArgs(args);
    }

    public int xToPX(int c, int zoomOut, int offsetX) {
        return c * 1000 / zoomOut + offsetX;
    }

    public int yToPX(int c, int zoomOut, int offsetY) {
        return c * 1000 / zoomOut + offsetY;
    }
    
    public static short calcDistance(short dx, short dy) {
        if (dy == 0) {
            return dx;
        } else if (dx == 0) {
            return dy;
        } else {
            return (short) Math.sqrt((double) (dx*dx + dy*dy));
        }
    }
    
    public String getInfoStr() {
        return "";
    }
    
    public int getArgsCount() {
        return getArgs().length;
    }
    
    public int getDataLengthInShorts() {
        return 1 + getArgsCount(); // id + args
    }

    public short[] getAsShortArray() {
        short[] args = getArgs();
        short[] arr = new short[args.length + 1];
        arr[0] = getID();
        System.arraycopy(getArgs(), 0, arr, 1, getArgsCount());
        return arr;
    }
    
    public abstract PlacementStep[] getPlacementSteps();
    
    public abstract PlacementStep[] getExtraEditingSteps();
    
    public abstract void paint(Graphics g, int zoomOut, int offsetX, int offsetY);
    
    public abstract Element setArgs(short[] args);
    
    public abstract short[] getArgs();
    
    public abstract short getID();
    
    public abstract int getStepsToPlace();

    public abstract String getName();
    
    public abstract short[] getEndPoint() throws Exception;
    
    public abstract class PlacementStep {
        public abstract void place(short pointX, short pointY);
        public abstract String getName();
    }
    
}
