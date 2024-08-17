/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.utils;

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
import mobileapplication3.editor.Main;
import mobileapplication3.elements.Element;

/**
 *
 * @author vipaol
 */
public class FileUtils {
    
    public static final String PREFIX = "file:///";
    public static final char SEP = '/';
    private static final short[] TESTDATA = new short[]{0, 1, 2, 3};
    
    public static void saveShortArrayToFile(short[] arr, String path) throws IOException, SecurityException {
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
        byte[] data = buf.toByteArray();
        dos.close();
        buf.close();

        OutputStream fos = fc.openOutputStream();
        fos.write(data);
        fos.close();
        fc.close();
    }
    
    public static Element[] readMGStruct(String path) {
        return readMGStruct(fileToDataInputStream(path));
    }
    
    public static Element[] readMGStruct(DataInputStream dis) {
        try {
            short fileVer = dis.readShort();
            System.out.println("mgstruct v" + fileVer);
            short elementsCount = dis.readShort();
            System.out.println("elements count: " + elementsCount);
            Element[] elements = new Element[elementsCount];
            for (int i = 0; i < elementsCount; i++) {
                elements[i] = readNextElement(dis);
                if (elements[i] == null) {
                    System.out.println("got null. stopping read");
                    return elements;
                }
            }
            return elements;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Element readNextElement(DataInputStream is) {
        System.out.println("reading next element...");
        try {
            short id = is.readShort();
            System.out.print("id" + id + " ");
            if (id == 0) {
                System.out.println("id0 is EOF mark. stopping");
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
            
            System.out.println(Utils.shortArrayToString(args));
            
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

    public static void createFolder(String path) throws IOException {
        FileConnection fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
        if (!fc.exists()) {
            // "file:///root/other/MGStructs/" - 6 '/'. we need to check if the parent folder doesn't exist if MGStruct is a subfolder (not in root)
            if (Utils.count(path, SEP) >= 6) {
                String parentFolderPath = path.substring(0, path.length() - Paths.GAME_FOLDER_NAME.length() - 1);
                System.out.println("checking parent folder: " + parentFolderPath);
                FileConnection parentFc = (FileConnection) Connector.open(parentFolderPath, Connector.READ_WRITE);
                if (!parentFc.exists()) {
                    parentFc.mkdir();
                }
                parentFc.close();
            }
            
            fc.mkdir();
        }
        fc.close();
    }
    
    public static void checkFolder(String path) throws IOException {
        path = path + "test.mgstruct";
        
        saveShortArrayToFile(TESTDATA, path);
        
        FileConnection fc = (FileConnection) Connector.open(path, Connector.WRITE);
        fc.delete();
        fc.close();
    }
    
}
