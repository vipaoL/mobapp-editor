/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import mobileapplication3.elements.Element;

/**
 *
 * @author vipaol
 */
public class FileUtils {
    
    public static String prefix = "file:///";
    public static String sep = "/";
    
    public static boolean saveShortArrayToFile(short[] arr, String path) {
        try {
            FileConnection fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
            }
            
            ByteArrayOutputStream buf = new ByteArrayOutputStream(arr.length*2);
            DataOutputStream dos = new DataOutputStream(buf);
            for (int i = 0; i < arr.length; i++) {
                dos.writeShort(arr[i]);
            }
            
            dos.flush();
            buf.flush();
            dos.close();
            
            OutputStream fos = fc.openOutputStream();
            fos.write(buf.toByteArray());
            fos.flush();
            fos.close();
            buf.close();
            fc.close();
        } catch (Exception ex) {
            Main.setCurrent(new Alert(ex.toString(), ex.toString(), null, AlertType.ERROR));
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static Element[] readMGStruct(String path) {
        try {
            DataInputStream is = fileToDataInputStream(path);
            short fileVer = is.readShort();
            short elementsCount = is.readShort();
            Element[] elements = new Element[elementsCount];
            for (int i = 0; i < elementsCount; i++) {
                elements[i] = readNextElement(is);
                if (elements[i] == null) {
                    return null;
                }
            }
            return elements;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Element readNextElement(DataInputStream is) {
        try {
            short id = is.readShort();
            System.out.println(id);
            if (id == 0) {
                return null;
            }
            Element element = Element.createTypedInstance(id);
            if (element == null) {
                return null;
            }
            
            int argsCount = element.getArgsCount();
            short[] args = new short[argsCount];
            for (int i = 0; i < argsCount; i++) {
                args[i] = is.readShort();
            }
            element.setArgs(args);
            return element;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static DataInputStream fileToDataInputStream(String path) {
        try {
            FileConnection fc = (FileConnection) Connector.open(path, Connector.READ);
            return fc.openDataInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static String[] getRoots() {
        return enumToArray(FileSystemRegistry.listRoots());
    }
    
    public static String[] list(String path) throws IOException {
        return enumToArray(((FileConnection) Connector.open(path, Connector.READ)).list());
    }
    
    public static String[] enumToArray(Enumeration en) {
        Vector tmp = new Vector(5);
        while (en.hasMoreElements()) {
            tmp.addElement(en.nextElement());
        }
        
        String[] arr = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            arr[i] = (String) tmp.elementAt(i);
        }
        return arr;
    }
    
}
