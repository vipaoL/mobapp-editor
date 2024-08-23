package mobileapplication3.editor.ui;

import mobileapplication3.editor.ui.platform.Graphics;

public abstract class Switch extends Button {
	
	private boolean value;
	int padding;
	private int switchW;
	int switchX0;

	public Switch(String title) {
		super(title);
		value = getValue();
	}
	
	protected void drawText(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused, boolean drawAsInactive) {
		switchW = Math.min(h * 2, w / 4);
		padding = switchW / 8;
		super.drawText(g, x0, y0, w - switchW, h, isSelected, isFocused, drawAsInactive);
		switchX0 = x0 + w - switchW + padding;
		switchW -= padding * 2;
		int switchH = switchW / 2;
		int roundingD = switchH;
		int switchY0 = y0 + (h - switchH) / 2;
		
		if (isActive()) {
			if (value) {
				g.setColor(IUIComponent.COLOR_ACCENT);
			} else {
				g.setColor(IUIComponent.BG_COLOR_INACTIVE);
			}
		} else {
			g.setColor(bgColorInactive);
		}
		
		g.fillRoundRect(switchX0, switchY0, switchW, switchH, roundingD, roundingD);
		
		if (isActive()) {
			g.setColor(fontColor);
		} else {
			g.setColor(fontColorInactive);
		}
		
		int d = switchH * 4 / 5;
		int x = switchX0;
		if (value) {
			x += switchW - d;
		}
		g.fillArc(x, switchY0 + (switchH - d) / 2, d, d, 0, 360);
	}
	
	public void buttonPressed() {
		value = !value;
		setValue(value);
	}
	
	public abstract boolean getValue();
	public abstract void setValue(boolean value);

}
