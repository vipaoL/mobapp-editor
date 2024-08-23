package mobileapplication3.editor.ui;

import mobileapplication3.editor.ui.platform.Graphics;
import mobileapplication3.editor.ui.platform.Image;

public class ImageComponent extends UIComponent {
	
	private Image image;
	private Image scaledImage = null;
	
	public ImageComponent(Image image) {
		this.image = image;
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
	
	protected void onSetBounds(int x0, int y0, int w, int h) {
		scaledImage = image.scale(w, h);
	}

	protected void onPaint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
		if (scaledImage != null) {
			g.drawImage(scaledImage, x0 + w/2, y0 + h/2, HCENTER | VCENTER);
		}
	}

}
