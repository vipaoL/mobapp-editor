package mobileapplication3.editor;

import javax.microedition.lcdui.Graphics;

import mobileapplication3.editor.ui.UIComponent;
import mobileapplication3.elements.Element;
import mobileapplication3.elements.EndPoint;
import mobileapplication3.utils.Mathh;

public class StructureViewerComponent extends UIComponent { // TODO: merge with EditorCanvas
	
	private static final int MIN_ZOOM_OUT = 8;
	private static final int MAX_ZOOM_OUT = 200000;
	
	 private int colLandscape = 0x4444ff;
    private int colBody = 0xffffff;
    private int offsetX, offsetY;
	private int zoomOut = 8192;
	
	private Element[] elements;
	
	public StructureViewerComponent(Element[] elements) {
		this.elements = elements;
		setBgColor(COLOR_ACCENT_MUTED);
		System.out.println("structure viewer: got " + elements.length + "elements");
	}

	public boolean canBeFocused() {
		return false;
	}

	protected boolean handlePointerReleased(int x, int y) {
		return false;
	}

	protected boolean handleKeyPressed(int keyCode, int count) {
		return false;
	}

	protected void onPaint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
		try {
			drawElements(g, x0, y0, w, h, elements);
		} catch (Exception ex) {
			g.drawString(ex.toString(), x0, y0, TOP | LEFT);
		}
	}
	
	private void drawElements(Graphics g, int x0, int y0, int w, int h, Element[] elements) {
        for (int i = 0; i < elements.length; i++) {
        	try {
	        	Element element = elements[i];
	        	System.out.println("drawing " + element.getName());
            	if (!element.isBody()) {
            		g.setColor(colLandscape);
            	} else {
            		g.setColor(colBody);
            	}
	            element.paint(g, zoomOut, x0 + offsetX, y0 + offsetY);
        	} catch (Exception ex) { }
        }
    }
	
	protected void onSetBounds(int x0, int y0, int w, int h) {
		zoomOut = Mathh.constrain(MIN_ZOOM_OUT, 4000000 / Math.min(w, h), MAX_ZOOM_OUT);
		offsetX = w/2;
		try {
			offsetX -= elements[0].getArgsValues()[0] / 2 * 1000 / zoomOut;
		} catch (Exception e) {
			e.printStackTrace();
		}
		offsetY = h/2;
	}

}
