package mobileapplication3.editor.ui.platform;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

import mobileapplication3.editor.platform.Main;

public class Platform {
	public static void showError(String message) {
    	Main.setCurrent(new Alert("Error!", message, null, AlertType.ERROR));
    }

	public static void showError(Exception ex) {
		showError(ex.toString());
	}
}
