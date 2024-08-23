/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.utils;

import mobileapplication3.editor.platform.Settings;

/**
 *
 * @author vipaol
 */
public class EditorSettings {
    public static final String
    		RECORD_STORE_SETTINGS = "settings",

            IS_SETUP_WIZARD_COMPLETED = "wizardCompleted",
            MGSTRUCTS_FOLDER_PATH = "mgPath",
            ANIMS = "anims",
            LISTS_KEY_REPEATS = "listKRepeats",
            AUTO_SAVE = "autoSave";
    
    private static String mgstructsFolderPath = null;
    
    private static Settings settingsInst = null;
    
    public static void resetSettings() {
    	getSettingsInst().resetSettings();
    }
    
    private static Settings getSettingsInst() {
    	if (settingsInst == null) {
    		settingsInst = new Settings(new String[]{
    	            IS_SETUP_WIZARD_COMPLETED,
    	            MGSTRUCTS_FOLDER_PATH,
    	            ANIMS,
    	            LISTS_KEY_REPEATS,
    	            AUTO_SAVE
    	        }, RECORD_STORE_SETTINGS);
    	}
    	return settingsInst;
    }
    
    public static boolean getAutoSaveEnabled() {
        return getSettingsInst().getBool(AUTO_SAVE);
    }
    
    public static boolean getAutoSaveEnabled(boolean defaultValue) {
        return getSettingsInst().getBool(AUTO_SAVE, defaultValue);
    }
    
    public static void setAutoSaveEnabled(boolean b) {
    	getSettingsInst().set(AUTO_SAVE, b);
    }
    
    public static boolean toggleAutoSaveEnabled() {
    	return getSettingsInst().toggleBool(AUTO_SAVE);
    }
    
    ///
    
    public static boolean getKeyRepeatedInListsEnabled() {
        return getSettingsInst().getBool(LISTS_KEY_REPEATS);
    }
    
    public static boolean getKeyRepeatedInListsEnabled(boolean defaultValue) {
        return getSettingsInst().getBool(LISTS_KEY_REPEATS, defaultValue);
    }
    
    public static void setKeyRepeatedInListsEnabled(boolean b) {
    	getSettingsInst().set(LISTS_KEY_REPEATS, b);
    }
    
    public static boolean toggleKeyRepeatedInListsEnabled() {
    	return getSettingsInst().toggleBool(LISTS_KEY_REPEATS);
    }
    
    ///
    
    public static boolean getAnimsEnabled() {
        return getSettingsInst().getBool(ANIMS);
    }
    
    public static boolean getAnimsEnabled(boolean defaultValue) {
        return getSettingsInst().getBool(ANIMS, defaultValue);
    }
    
    public static void setAnimsEnabled(boolean b) {
    	getSettingsInst().set(ANIMS, b);
    }
    
    public static boolean toggleAnims() {
    	return getSettingsInst().toggleBool(ANIMS);
    }
    
    ///
    
    public static String getMgstructsFolderPath() {
        if (mgstructsFolderPath == null) {
            mgstructsFolderPath = getSettingsInst().getStr(MGSTRUCTS_FOLDER_PATH);
        }
        
        return mgstructsFolderPath;
    }

    public static void setMgstructsFolderPath(String path) {
    	getSettingsInst().set(MGSTRUCTS_FOLDER_PATH, path);
    	mgstructsFolderPath = path;
    }
    
    ///
    
    public static boolean isSetupWizardCompleted() {
        return getSettingsInst().getBool(IS_SETUP_WIZARD_COMPLETED);
    }

    public static void setIsSetupWizardCompleted(boolean b) {
    	getSettingsInst().set(IS_SETUP_WIZARD_COMPLETED, b);
    }
}
