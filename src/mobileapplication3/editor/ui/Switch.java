package mobileapplication3.editor.ui;

import javax.microedition.lcdui.Graphics;

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
		padding = h/3;
		switchW = h * 2;
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
	
//	protected void drawSelectionMark(Graphics g, int x0, int y0, int w, int h, boolean isSelected, boolean isFocused, boolean forceInactive) {
//		//g.drawRect(switchX0 - padding, y0, switchW + padding*2, h);
//		super.drawSelectionMark(g, switchX0 - padding, y0, switchW + padding*2, h, isSelected, isFocused, forceInactive);
//	}
	
	public void buttonPressed() {
		value = !value;
		setValue(value);
	}
	
	public abstract boolean getValue();
	public abstract void setValue(boolean value);

}
