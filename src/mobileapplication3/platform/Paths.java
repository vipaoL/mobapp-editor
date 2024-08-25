/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.platform;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.file.FileSystemRegistry;

/**
 *
 * @author vipaol
 */
public class Paths {
    private static final String PREFIX = "file:///";
    private static final String SEP = "/";
    private static final String[] FOLDERS_ON_EACH_DRIVE = {"", "other" + SEP};
    private static final String[] FOLDERS_FROM_SYSTEM_PROPS = {"fileconn.dir.photos", "fileconn.dir.graphics"};
    public static final String GAME_FOLDER_NAME = "MGStructs";
    
    public static String[] getAllMGStructsFolders() {
        String[] paths;
        Vector pathsVec = new Vector(8);
        Vector roots = new Vector(3);
        Enumeration rootsEnumeration = FileSystemRegistry.listRoots();
        while (rootsEnumeration.hasMoreElements()) {
            roots.addElement(rootsEnumeration.nextElement());
        }
        
        for (int i = 0; i < roots.size(); i++) {
            for (int j = 0; j < FOLDERS_ON_EACH_DRIVE.length; j++) {
                pathsVec.addElement(PREFIX + roots.elementAt(i) + FOLDERS_ON_EACH_DRIVE[j] + GAME_FOLDER_NAME + SEP);
            }
        }
        
        for (int i = 0; i < FOLDERS_FROM_SYSTEM_PROPS.length; i++) {
            String path = System.getProperty(FOLDERS_FROM_SYSTEM_PROPS[i]);
            if (path != null) {
                pathsVec.addElement(path + GAME_FOLDER_NAME + SEP);
            }
        }
        
        paths = new String[pathsVec.size()];
        
        for (int i = 0; i < pathsVec.size(); i++) {
            paths[i] = (String) pathsVec.elementAt(i);
        }
        
        return paths;
    }
}
