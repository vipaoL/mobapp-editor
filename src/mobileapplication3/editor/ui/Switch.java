package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Graphics;

public abstract class Switch extends Button {
	
	private boolean value;

	public Switch(String title) {
		super(title);
		value = getValue();
	}
	
	public void paint(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused, boolean drawAsInactive) {
		int padding = h/10;
		int switchW = Math.min(h * 3 / 2, w / 8);
		super.paint(g, x0, y0, w - switchW, h, isSelected, isFocused, drawAsInactive);
		int switchX0 = x0 + w - switchW + padding;
		switchW -= padding * 2;
		int roundingR = (h - padding * 4)/2;
		int switchH = h - padding * 2;
		int switchY0 = y0 + (h - switchH) / 2;
		
		if (isActive()) {
			if (value) {
				g.setColor(IUIComponent.COLOR_ACCENT);
			} else {
				g.setColor(IUIComponent.COLOR_ACCENT_MUTED);
			}
		} else {
			g.setColor(bgColorInactive);
		}
		
		g.fillRoundRect(switchX0, switchY0, switchW, switchH, roundingR, roundingR);
		
		if (isActive()) {
			g.setColor(fontColor);
		} else {
			g.setColor(fontColorInactive);
		}
		
		int d = switchH - padding * 2;
		int x = switchX0;
		if (value) {
			x += switchW - d;
		}
		g.fillArc(x, switchY0 + padding, d, d, 0, 360);
	}
	
	public void buttonPressed() {
		value = !value;
		setValue(value);
	}
	
	public abstract boolean getValue();
	public abstract void setValue(boolean value);

}
