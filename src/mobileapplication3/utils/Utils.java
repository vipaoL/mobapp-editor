/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.utils;

import mobileapplication3.editor.EditorCanvas;
import mobileapplication3.editor.ui.platform.Graphics;

/**
 *
 * @author vipaol
 */
public class Utils {
    
    public static String shortArrayToString(short [] arr) {
        try {
            if (arr == null) {
                return "null";
            }

            if (arr.length == 0) {
                return "[]";
            }

            StringBuffer sb = new StringBuffer(arr.length*6);
            sb.append("[");
            for (int i = 0; i < arr.length - 1; i++) {
                sb.append(arr[i]);
                sb.append(", ");
            }
            sb.append(arr[arr.length-1]);
            sb.append("]");
            return sb.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }
    
    public static String[] split(String sb, String splitter){
        String[] strs = new String[sb.length()];
        int splitterLength = splitter.length();
        int initialIndex = 0;
        int indexOfSplitter = indexOf(sb, splitter, initialIndex);
        int count = 0;
        if(-1==indexOfSplitter) return new String[]{sb};
        while(-1!=indexOfSplitter){
            char[] chars = new char[indexOfSplitter-initialIndex];
            sb.getChars(initialIndex, indexOfSplitter, chars, 0);
            initialIndex = indexOfSplitter+splitterLength;
            indexOfSplitter = indexOf(sb, splitter, indexOfSplitter+1);
            strs[count] = new String(chars);
            count++;
        }
        // get the remaining chars.
        if(initialIndex+splitterLength<=sb.length()){
            char[] chars = new char[sb.length()-initialIndex];
            sb.getChars(initialIndex, sb.length(), chars, 0);
            strs[count] = new String(chars);
            count++;
        }
        String[] result = new String[count];
        for(int i = 0; i<count; i++){
            result[i] = strs[i];
        }
        return result;
    }

    public static int indexOf(String sb, String str, int start){
        int index = -1;
        if((start>=sb.length() || start<-1) || str.length()<=0) return index;
        char[] tofind = str.toCharArray();
        outer: for(;start<sb.length(); start++){
            char c = sb.charAt(start);
            if(c==tofind[0]){
                if(1==tofind.length) return start;
                inner: for(int i = 1; i<tofind.length;i++){ // start on the 2nd character
                    char find = tofind[i];
                    int currentSourceIndex = start+i;
                    if(currentSourceIndex<sb.length()){
                        char source = sb.charAt(start+i);
                        if(find==source){
                            if(i==tofind.length-1){
                                return start;
                            }
                            continue inner;
                        } else {
                            start++;
                            continue outer;
                        }
                    } else {
                        return -1;
                    }

                }
            }
        }
        return index;
    }

    public static String replace(String _text, String _searchStr, String _replacementStr) {
        // String buffer to store str
        StringBuffer sb = new StringBuffer();

        // Search for search
        int searchStringPos = _text.indexOf(_searchStr);
        int startPos = 0;
        int searchStringLength = _searchStr.length();

        // Iterate to add string
        while (searchStringPos != -1) {
            sb.append(_text.substring(startPos, searchStringPos)).append(_replacementStr);
            startPos = searchStringPos + searchStringLength;
            searchStringPos = _text.indexOf(_searchStr, startPos);
        }

        // Create string
        sb.append(_text.substring(startPos,_text.length()));

        return sb.toString();
    }
    
    // TODO move to Graphics
    public static void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int thickness, int zoomOut) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int arrowX = (x2*5 + x1) / 6;
        int arrowY = (y2*5 + y1) / 6;
        int arrowSideVecX = dy / 8;
        int arrowSideVecY = -dx / 8;
        drawLine(g, x1, y1, arrowX, arrowY, thickness, zoomOut, false, false);
        drawTriangle(g, x2, y2, arrowX + arrowSideVecX, arrowY + arrowSideVecY, arrowX - arrowSideVecX, arrowY - arrowSideVecY, zoomOut);
    }
    
    public static void drawTriangle(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int zoomOut) {
    	if (zoomOut < EditorCanvas.zoomoutThresholdMacroMode) {
            g.drawLine(x1, y1, x2, y2);
            g.drawLine(x2, y2, x3, y3);
            g.drawLine(x1, y1, x3, y3);
        } else {
        	g.fillTriangle(x1, y1, x2, y2, x3, y3);
        }
    }
    
    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, int zoomOut) {
        drawLine(g, x1, y1, x2, y2, thickness, zoomOut, true, true);
    }
    
    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, int zoomOut, boolean rounding, boolean markSkeleton) {
        if (thickness > 2) {
            int t2 = thickness/2;
            int dx = x2 - x1;
            int dy = y2 - y1;
            int l = (int) Math.sqrt(dx*dx+dy*dy);
            
            if (l == 0 || zoomOut < EditorCanvas.zoomoutThresholdMacroMode) {
                g.drawLine(x1, y1, x2, y2);
                return;
            }
            
            // normal vector
            int nx = dy*t2 * 1000 / zoomOut / l;
            int ny = dx*t2 * 1000 / zoomOut / l;
            
            if (nx == 0 && ny == 0) {
                g.drawLine(x1, y1, x2, y2);
                return;
            }
            
            // draw bold line with two triangles (splitting by diagonal)
            g.fillTriangle(x1-nx, y1+ny, x2-nx, y2+ny, x1+nx, y1-ny);
            g.fillTriangle(x2-nx, y2+ny, x2+nx, y2-ny, x1+nx, y1-ny);
            if (rounding) {
                int r = t2 * 1000 / zoomOut;
                int d = r * 2;
                g.fillArc(x1-r, y1-r, d, d, 0, 360);
                g.fillArc(x2-r, y2-r, d, d, 0, 360);
            }
            if (markSkeleton && thickness * 1000 / zoomOut > 8) {
                int prevCol = g.getColor();
                g.setColor(0xff0000);
                g.drawLine(x1, y1, x2, y2);
                g.setColor(prevCol);
            }
        } else {
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public static int count(String s, char c) {
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                ret++;
            }
        }
        return ret;
    }

}
