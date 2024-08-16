package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

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
		scaledImage = scale(image, w, h);
	}

	protected void onPaint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
		if (scaledImage != null) {
			g.drawImage(scaledImage, x0 + w/2, y0 + h/2, HCENTER | VCENTER);
		}
	}
	
	public static Image scale(Image original, int newWidth, int newHeight) {
        int[] rawInput = new int[original.getHeight() * original.getWidth()];
        original.getRGB(rawInput, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());

        int[] rawOutput = new int[newWidth * newHeight];

        // YD compensates for the x loop by subtracting the width back out
        int YD = (original.getHeight() / newHeight) * original.getWidth() - original.getWidth();
        int YR = original.getHeight() % newHeight;
        int XD = original.getWidth() / newWidth;
        int XR = original.getWidth() % newWidth;
        int outOffset = 0;
        int inOffset = 0;

        for (int y = newHeight, YE = 0; y > 0; y--) {
            for (int x = newWidth, XE = 0; x > 0; x--) {
                rawOutput[outOffset++] = rawInput[inOffset];
                inOffset += XD;
                XE += XR;
                if (XE >= newWidth) {
                    XE -= newWidth;
                    inOffset++;
                }
            }
            inOffset += YD;
            YE += YR;
            if (YE >= newHeight) {
                YE -= newHeight;
                inOffset += original.getWidth();
            }
        }
        rawInput = null;
        return Image.createRGBImage(rawOutput, newWidth, newHeight, true);

    }

}
