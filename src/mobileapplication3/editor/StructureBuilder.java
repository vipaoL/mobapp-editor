/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import java.io.IOException;
import mobileapplication3.utils.FileUtils;
import java.util.Vector;
import mobileapplication3.elements.Element;
import mobileapplication3.elements.EndPoint;

/**
 *
 * @author vipaol
 */

public class StructureBuilder {
    
    public static final int PLACE_NOTHING = -1;
    Vector buffer;
    Element placingNow;
    NextPointHandler nextPointHandler;
    Feedback feedback;

    public StructureBuilder(Feedback feedback) {
        this.feedback = feedback;
        buffer = new Vector();
        buffer.addElement(new EndPoint().setArgs(new short[]{0, 0}));
    }
    
    void place(short id, short x, short y) throws IllegalArgumentException {
        placingNow = Element.createTypedInstance(id);
        System.out.println("Placing " + id);
        nextPointHandler = new NextPointHandler();
        handleNextPoint(x, y, false);
        add(placingNow);
        handleNextPoint(x, y, true);
    }
    
    public void handleNextPoint(short x, short y, boolean isPreview) {
        if (placingNow == null) {
            return;
        }
        
        nextPointHandler.showingPreview = isPreview;
        
        nextPointHandler.handleNextPoint(x, y);
        
        if (nextPointHandler.step == placingNow.getStepsToPlace() || nextPointHandler.isEditing) {
            if (!isPreview) {
                recalcEndPoint();
                placingNow = null;
                nextPointHandler = null;
            }
        }
    }
    
    public void add(Element element) {
        if (element == null) {
            return;
        }
        
        if (element instanceof EndPoint) {
            return;
        }
        
        buffer.addElement(element);
        feedback.onUpdate();
    }
    
    public void saveToFile(String path) throws IOException, SecurityException {
        int carriage = 0;
        // {file format version, count of elements, ...data..., eof mark}
        short[] data = new short[1 + 1 + getDataLengthInShorts() + 1];
        
        data[carriage] = 1;
        carriage++;
        data[carriage] = (short) getElementsCount();
        carriage++;
        
        for (int i = 0; i < getElementsCount(); i++) {
            Element element = (Element) buffer.elementAt(i);
            
            short[] elementArgs = element.getAsShortArray();
            for (int j = 0; j < elementArgs.length; j++) {
                data[carriage] = elementArgs[j];
                carriage++;
            }
        }
        
        data[carriage] = 0;
        
        System.out.println(carriage + " " + data.length);
        FileUtils.saveShortArrayToFile(data, path);
    }
    
    public void loadFile(String path) {
        try {
            buffer = new Vector();
            Element[] elements = FileUtils.readMGStruct(path);
            if (elements == null) {
                System.out.println("error: elements array is null");
                return;
            }
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] != null) {
                    buffer.addElement(elements[i]);
                } else {
                    System.out.println("elements["+i+"] is null. skipping");
                }
            }
            feedback.onUpdate();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void remove(int i) {
        if (buffer.elementAt(i) instanceof EndPoint) {
            return;
        }
        
        boolean needToRecalcEndPoint = true;
//        try {
//            (EndPoint) buffer.elementAt(0)).getArgs()
//            needToRecalcEndPoint = ( == ((Element) buffer.elementAt(i)).getEndPoint();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("needToRecalcEndPoint=" + needToRecalcEndPoint);
        buffer.removeElementAt(i);
        if (needToRecalcEndPoint) {
            recalcEndPoint();
        }
        feedback.onUpdate();
    }
    
    public void recalcEndPoint() {
        EndPoint endPointObj = (EndPoint) buffer.elementAt(0);
        short[] endPoint = {0, 0};
        short[] newEndPoint = endPoint;
        for (int i = 1; i < buffer.size(); i++) {
            try {
                newEndPoint = ((Element) buffer.elementAt(i)).getEndPoint();
                if (EndPoint.compare(endPoint, newEndPoint)) {
                    endPoint = newEndPoint;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        endPointObj.setArgs(endPoint);
    }
    
    public Vector getElements() {
        return buffer;
    }
    
    public int getElementsCount() {
        return buffer.size();
    }
    
    public Element[] getElementsAsArray() {
        Element[] elements = new Element[getElementsCount()];
        for (int i = 0; i < getElementsCount(); i++) {
            elements[i] = (Element) buffer.elementAt(i);
        }
        return elements;
    }
    
    public int getDataLengthInShorts() {
        int l = 0;
        for (int i = 0; i < getElementsCount(); i++) {
            l += 1/*id*/ + ((Element) buffer.elementAt(i)).getArgsCount()/*args*/;
        }
        return l;
    }
    
    private class NextPointHandler {
        public int step = 0;
        public boolean showingPreview = false;
        public boolean isEditing = false;
        
        void handleNextPoint(short x, short y) {
            try {
                placingNow.getPlacementSteps()[step].place(x, y);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!showingPreview) {
                step++;
            }
        }
        
    }
    
    public interface Feedback {
        void onUpdate();
    }
}
