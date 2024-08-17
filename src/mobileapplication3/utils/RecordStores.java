/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

/**
 *
 * @author vipaol
 */
public class RecordStores {
    public static final String RECORD_STORE_NAME_SETTINGS = "settings";
    
    public static boolean writeBytesToStore(byte[] data, String recordStoreName) {
        RecordStore rs = null;
        boolean ret = true;
        
        System.out.println("writing bytes to store: ");
        for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			System.out.print(b + " ");
		}
        System.out.println();
        
        try {
            try {
                RecordStore.deleteRecordStore(recordStoreName);
            } catch (Exception e) { }

            rs = RecordStore.openRecordStore(recordStoreName, true);
            System.out.println("recordId=" + rs.addRecord(data, 0, data.length));
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        
        try {
            rs.closeRecordStore();
        } catch(Exception e) {
            
        }
        
        return ret;
    }
    
    public static boolean writeStringToStore(String settings, String recordStoreName) {
    	System.out.println("writing to RMS: " + settings);
    	try {
			byte[] data = settings.getBytes("UTF-8");
			return writeBytesToStore(data, recordStoreName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
    public static String readStringFromStore(String recordStoreName) {
        System.out.println("reading store " + recordStoreName);
        RecordStore rs = null;
        String ret = null;
        
        try {
            rs = RecordStore.openRecordStore(recordStoreName, false);
            byte[] data = rs.getRecord(1);
            if (data != null && data.length != 0) {
                ret = new String(data, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            rs.closeRecordStore();
        } catch(Exception e) {
            
        }
        
        System.out.println("read from " + recordStoreName + " store: " + ret);
        return ret;
    }
    
    public static DataInputStream openDataInputStream(String recordStoreName) {
    	RecordStore rs = null;
    	DataInputStream ret = null;
    	try {
            rs = RecordStore.openRecordStore(recordStoreName, false);
            byte[] data = rs.getRecord(1);
            System.out.println("read record store \"" + recordStoreName + "\". data.length=" + data.length);
            if (data != null && data.length != 0) {
                ret = new DataInputStream(new ByteArrayInputStream(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            rs.closeRecordStore();
        } catch(Exception e) {
            
        }
        
        return ret;
    }
    
    public static void WriteShortArray(short[] data, String recordStoreName) {
    	System.out.println("writing shorts to store: ");
        for (int i = 0; i < data.length; i++) {
			short s = data[i];
			System.out.print(s + " ");
		}
        System.out.println();
    	writeBytesToStore(shortsToBytes(data), recordStoreName);
	}
    
    private static byte[] shortsToBytes(short[] data) {
        byte[] resultData = new byte[data.length * 2];
        int c = 0;
        for (int i = 0; i < data.length; i++) {
        	resultData[c++] = (byte) (((data[i]) >>> 8) & 0xFF);
            resultData[c++] = (byte) (((data[i])) & 0xFF);
        }
        System.out.println("shorts to bytes: shorts.length=" + data.length + " bytes.length=" + resultData.length);
        return resultData;
    }
    
    public static void deleteStore(String recordStoreName) {
    	try {
			RecordStore.deleteRecordStore(recordStoreName);
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
    }
    
}
