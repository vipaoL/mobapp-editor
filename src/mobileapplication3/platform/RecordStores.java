/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.platform;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

/**
 *
 * @author vipaol
 */
public class RecordStores {

    public static void writeBytesToStore(byte[] data, String recordStoreName) throws RecordStoreException {
        try {
            RecordStore.deleteRecordStore(recordStoreName);
        } catch (Exception e) { }

        RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
        rs.addRecord(data, 0, data.length);
        
        try {
            rs.closeRecordStore();
        } catch(Exception e) { }
    }
    
    public static void writeStringToStore(String settings, String recordStoreName) throws UnsupportedEncodingException, RecordStoreException {
		writeBytesToStore(settings.getBytes("UTF-8"), recordStoreName);
    }
    
    public static String readStringFromStore(String recordStoreName) {
        RecordStore rs = null;
        String ret = null;
        
        try {
            rs = RecordStore.openRecordStore(recordStoreName, false);
            byte[] data = rs.getRecord(1);
            if (data != null && data.length != 0) {
                ret = new String(data, "UTF-8");
            }
        } catch (RecordStoreNotFoundException ex) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            rs.closeRecordStore();
        } catch(Exception e) {
            
        }
        
        return ret;
    }
    
    public static DataInputStream openDataInputStream(String recordStoreName) {
    	RecordStore rs = null;
    	DataInputStream ret = null;
    	try {
            rs = RecordStore.openRecordStore(recordStoreName, false);
            byte[] data = rs.getRecord(1);
            if (data != null && data.length != 0) {
                ret = new DataInputStream(new ByteArrayInputStream(data));
            }
    	} catch (RecordStoreNotFoundException ex) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            rs.closeRecordStore();
        } catch(Exception e) {
            
        }
        
        return ret;
    }
    
    public static void writeShorts(short[] data, String recordStoreName) throws RecordStoreException {
    	writeBytesToStore(shortsToBytes(data), recordStoreName);
	}
    
    private static byte[] shortsToBytes(short[] data) {
        byte[] resultData = new byte[data.length * 2];
        int c = 0;
        for (int i = 0; i < data.length; i++) {
        	resultData[c++] = (byte) (((data[i]) >>> 8) & 0xFF);
            resultData[c++] = (byte) (((data[i])) & 0xFF);
        }
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
