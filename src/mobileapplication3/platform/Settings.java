/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.platform;

import mobileapplication3.platform.ui.Platform;

/**
 *
 * @author vipaol
 */
public class Settings {
    public static final String
            TRUE = "1",
            FALSE = "0",
            UNDEF = "";
    
    private static final char SEP = '\n';
    
    private String recordStoreName;
    private String[] settingsKeysVals;
    private String[] keys;
    
    public Settings(String[] keys, String recordStoreName) {
    	this.keys = keys;
    	this.recordStoreName = recordStoreName;
    }
    
    public void saveToRMS() {
        try {
        	RecordStores.writeStringToStore(getCurrentSettingsAsStr(), recordStoreName);
        } catch (Exception ex) {
        	Platform.showError("Can't save settings to RMS: " + ex.toString());
        }
    }
    
    public void resetSettings() {
    	RecordStores.deleteStore(recordStoreName);
    	loadDefaults();
    }
    
    public void loadDefaults() {
    	settingsKeysVals = new String[keys.length * 2];
    	for (int i = 0; i < keys.length; i++) {
			settingsKeysVals[i*2] = keys[i];
			settingsKeysVals[i*2 + 1] = UNDEF;
		}
    }
    
    public void loadFromRMS() {
        loadFromString(RecordStores.readStringFromStore(recordStoreName));
    }
    
    public void loadFromString(String str) {
        loadDefaults();
        if (str == null) {
            return;
        }
        
        String[] keyValueCouples = Utils.split(str.substring(0, str.length() - 1), "" + SEP);
        //settingsKeysVals = new String[(keyValueCouples.length) * 2];
        for (int i = 0; i < keyValueCouples.length; i++) {
            int splitterIndex = keyValueCouples[i].indexOf(' ');
            String key = keyValueCouples[i].substring(0, splitterIndex);
            String value = keyValueCouples[i].substring(splitterIndex + 1);
            for (int j = 0; j < settingsKeysVals.length / 2; j++) {
                if (key.equals(settingsKeysVals[j*2])) {
                    settingsKeysVals[i*2 + 1] = value;
                }
            }
        }
    }
    
    public String getCurrentSettingsAsStr() {
        if (settingsKeysVals == null) {
            loadFromRMS();
        }
        
        //assert ((settingsKeysVals.length % 2) == 0);
        StringBuffer sb = new StringBuffer(settingsKeysVals.length*5);
        for (int i = 0; i < settingsKeysVals.length / 2; i++) {
            sb.append(settingsKeysVals[i*2]);
            sb.append(" ");
            sb.append(settingsKeysVals[i*2 + 1]);
            sb.append(SEP);
        }
        return sb.toString();
    }

    public boolean set(String key, String value) {
        if (settingsKeysVals == null) {
            loadFromRMS();
        }
        
        for (int i = 0; i < settingsKeysVals.length / 2; i++) {
            if (settingsKeysVals[i*2].equals(key)) {
                settingsKeysVals[i*2 + 1] = value;
                saveToRMS();
                return true;
            }
        }
        
        return false;
    }
    
    public boolean set(String key, boolean value) {
        return set(key, toStr(value));
    }
    
    public String getStr(String key) {
        if (settingsKeysVals == null) {
            loadFromRMS();
        }
        
        for (int i = 0; i < settingsKeysVals.length / 2; i++) {
            if (settingsKeysVals[i*2].equals(key)) {
            	String value = settingsKeysVals[i*2 + 1];
            	if (value.equals(null)) {
            		value = UNDEF;
            	}
                return value;
            }
        }
        return null;
    }
    
    public boolean toggleBool(String key) {
        boolean newValue = !getBool(key);
        set(key, newValue);
        return newValue;
    }
    
    public boolean getBool(String key) {
    	return TRUE.equals(getStr(key));
    }
    
    public boolean getBool(String key, boolean defaultValue) {
    	String value = getStr(key);
    	if (value == null || "".equals(value)) {
    		set(key, defaultValue);
    		return TRUE.equals(getStr(key));
    	}
        return TRUE.equals(value);
    }
    
    public int getInt(String key) {
    	String value = getStr(key);
    	if (UNDEF.equals(value)) {
    		return 0;
    	}
    	return Integer.parseInt(value);
    }
    
    private String toStr(boolean b) {
        return b ? "1" : "0";
    }
}
