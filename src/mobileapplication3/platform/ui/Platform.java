package mobileapplication3.platform.ui;

import java.io.DataInputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.rms.RecordStoreException;

import mobileapplication3.EditorMIDlet;
import mobileapplication3.platform.RecordStores;

public class Platform {

	public static void showError(String message) {
    	EditorMIDlet.setCurrent(new Alert("Error!", message, null, AlertType.ERROR));
    }

	public static void showError(Exception ex) {
		showError(ex.toString());
	}

	public static void vibrate(int ms) {
		EditorMIDlet.vibrate(ms);
	}
	
	public static void storeShorts(short[] data, String storageName) throws RecordStoreException {
		RecordStores.writeShorts(data, storageName);
	}
	
	public static DataInputStream readStore(String storageName) {
		return RecordStores.openDataInputStream(storageName);
	}
	
	public static void clearStore(String storageName) {
		RecordStores.deleteStore(storageName);
	}
}
