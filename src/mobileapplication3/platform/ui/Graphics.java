package mobileapplication3.platform.ui;

public class Graphics {
	public static final int HCENTER = 1;
	public static final int VCENTER = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	public static final int TOP = 16;
	public static final int BOTTOM = 32;
	public static final int BASELINE = 64;

	private javax.microedition.lcdui.Graphics g;
	
	public Graphics(javax.microedition.lcdui.Graphics g) {
		this.g = g;
	}

	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	public void drawImage(Image img, int x, int y, int anchor) {
		g.drawImage(img.getImage(), x, y, anchor);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(x, y, width, height);
	}

	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	public void drawString(String str, int x, int y, int anchor) {
		g.drawString(str, x, y, anchor);
	}

	public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
		g.drawSubstring(str, offset, len, x, y, anchor);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}

	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		g.fillTriangle(x1, y1, x2, y2, x3, y3);
	}

	public void setClip(int x, int y, int width, int height) {
		g.setClip(x, y, width, height);
	}

	public void setColor(int RGB) {
		g.setColor(RGB);
	}

	public void setColor(int red, int green, int blue) {
		g.setColor(red, green, blue);
	}
	
	public void setFontSize(int size) {
		g.setFont(javax.microedition.lcdui.Font.getFont(getFontFace(), getFontStyle(), size));
	}

	public void setFont(int face, int style, int size) {
		g.setFont(javax.microedition.lcdui.Font.getFont(face, style, size));
	}
	
	public void setFont(Font font) {
		g.setFont(font.getFont());
	}
	
	public Font getFont() {
		return new Font(g.getFont());
	}

	public int getFontFace() {
		return g.getFont().getFace();
	}

	public int getFontStyle() {
		return g.getFont().getStyle();
	}

	public int getFontSize() {
		return g.getFont().getSize();
	}

	public int getFontHeight() {
		return g.getFont().getHeight();
	}

	public int stringWidth(String str) {
		return g.getFont().stringWidth(str);
	}

	public int substringWidth(String str, int offset, int len) {
		return g.getFont().substringWidth(str, offset, len);
	}

	public int getFontHeight(int face, int style, int size) {
		return javax.microedition.lcdui.Font.getFont(face, style, size).getHeight();
	}

	public int getClipWidth() {
		return g.getClipWidth();
	}

	public int getClipHeight() {
		return g.getClipHeight();
	}

	public int getClipX() {
		return g.getClipX();
	}

	public int getClipY() {
		return g.getClipY();
	}

	public int getColor() {
		return g.getColor();
	}
}
