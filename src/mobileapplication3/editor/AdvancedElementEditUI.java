package mobileapplication3.editor;

import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonComponent;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.Keys;
import mobileapplication3.editor.ui.List;
import mobileapplication3.editor.ui.TextComponent;
import mobileapplication3.editor.ui.platform.Graphics;
import mobileapplication3.editor.ui.platform.RootContainer;
import mobileapplication3.elements.Element;
import mobileapplication3.elements.Element.Argument;
import mobileapplication3.utils.Mathh;

public class AdvancedElementEditUI extends AbstractPopupWindow {
	
	private Element element;
	private IUIComponent[] rows;
	private List list;
	private StructureBuilder sb;

	public AdvancedElementEditUI(Element element, StructureBuilder sb, IPopupFeedback parent) {
		super("Advanced edit: " + element.getName(), parent);
		this.element = element;
		this.sb = sb;
	}

	protected Button[] getActionButtons() {
		final short[] argsUnmodified = element.getArgsValues();
        return new Button[] {
            new Button("OK") {
                public void buttonPressed() {
                	element.recalcCalculatedArgs();
                	if (element.getID() != Element.END_POINT) {
                		sb.recalcEndPoint();
                	}
                    close();
                }
            },
            new Button("Cancel") {
                public void buttonPressed() {
                	element.setArgs(argsUnmodified);
                    close();
                }
            }
        };
    }

	protected IUIComponent initAndGetPageContent() {
		list = new List() {
			public final void onSetBounds(int x0, int y0, int w, int h) {
				setElementsPadding(getElemH()/16);
				super.onSetBounds(x0, y0, w, h);
			}
		};
		
		refreshList();
		
		list.enableScrolling(true, false)
            .trimHeight(true)
            .setIsSelectionEnabled(true)
            .setIsSelectionVisible(true);
		return list;
	}
	
	private void refreshList() {
		Argument[] args = element.getArgs();
		rows = new IUIComponent[args.length + 1];
		for (int i = 0; i < args.length; i++) {
			rows[i] = new ListRow(args[i]);
		}
		rows[args.length] = new ButtonComponent(new Button ("Refresh this list") {
			public void buttonPressed() {
				element.recalcCalculatedArgs();
				refreshList();
			}
		});
		list.setElements(rows);
	}
	
	class ListRow extends Container {

		private TextComponent title;
		private TextComponent valueIndicator;
		private ButtonRow buttonRow;
		private Argument arg;
		
		private short value;
		private short minValue;
		private short maxValue;
		private int prevDraggedX;
		private long prevDraggedTime = 0;
		
		public ListRow(Argument arg) {
			minValue = arg.getMinValue();
			maxValue = arg.getMaxValue();
			
			this.arg = arg;
			
			title = new TextComponent(arg.getName());
			title.setBgColor(COLOR_TRANSPARENT);
			
			value = arg.getValue();
			valueIndicator = new TextComponent(String.valueOf(value));
			valueIndicator.setBgColor(COLOR_TRANSPARENT);
			
			buttonRow = new ButtonRow();
			buttonRow.setBgColor(COLOR_TRANSPARENT);
			buttonRow.setButtonsBgColor(COLOR_TRANSPARENT);
			buttonRow.setButtonsBgColorInactive(COLOR_TRANSPARENT);
			buttonRow.setButtons(new Button[] {
					new Button("-") {
						public void buttonPressed() {
							setValue((short) (getValue() - 1));
						}
					},
					new Button("+") {
						public void buttonPressed() {
							setValue((short) (getValue() + 1));
						}
					}
			});
			
			setBgColor(COLOR_ACCENT);
			setActive(!arg.isCalculated());
			roundBg(true);
		}
		
		public void init() {
			setComponents(new IUIComponent[] {title, valueIndicator, buttonRow});
		}
		
		protected void drawBg(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
			int prevClipX = g.getClipX();
	        int prevClipY = g.getClipY();
	        int prevClipW = g.getClipWidth();
	        int prevClipH = g.getClipHeight();
	        
	        int barLeftW = w * (value - minValue) / (maxValue - minValue);
	        int d = Math.min(w/5, h/5);
	        
	        boolean isActive = this.isActive && !forceInactive;
	        if (isActive) {
				g.setColor(COLOR_ACCENT_MUTED);
	        } else {
	        	g.setColor(BG_COLOR_INACTIVE);
	        }
			g.setClip(x0, y0, w, h);
            g.fillRoundRect(x0, y0, w, h, d, d);
	        
	        if (isActive) {
	        	g.setColor(COLOR_ACCENT);
	        } else {
	        	g.setColor(COLOR_ACCENT_MUTED);
	        }
	        g.setClip(x0, y0, barLeftW, h);
            g.fillRoundRect(x0, y0, w, h, d, d);
			
			g.setClip(prevClipX, prevClipY, prevClipW, prevClipH);
		}
		
		protected void onSetBounds(int x0, int y0, int w, int h) {
			buttonRow
				.setSize(ButtonRow.W_AUTO, h/2)
				.setPos(x0 + w, y0 + h, BOTTOM | RIGHT);
			valueIndicator
				.setSize(w - buttonRow.w, buttonRow.h)
				.setPos(x0, y0 + h, BOTTOM | LEFT);
			title
				.setSize(w, h - buttonRow.h)
				.setPos(x0, y0, TOP | LEFT);
		}
		
		public short getValue() {
			return arg.getValue();
		}
		
		public void setValue(int value) {
			this.value = (short) Mathh.constrain(minValue, value, maxValue);
			arg.setValue(this.value);
			valueIndicator.setText(String.valueOf(this.value));
		}
		
		public boolean pointerPressed(int x, int y) {
			prevDraggedX = x;
			prevDraggedTime = System.currentTimeMillis();
			return super.pointerPressed(x, y);
		}
		
		public boolean pointerDragged(int x, int y) {
			int dx = x - pressedX;
			int dy = y - pressedY;
			int dt = (int) (System.currentTimeMillis() - prevDraggedTime);
			
			if (Math.abs(dx) < Math.abs(dy * 5)) {
				prevDraggedX = x;
				// list scrolling handles the pointer event if false is returned
				// so scroll the list if the vertical movement is greater than the horizontal movement
				return false;
			}
			
			if (dt != 0) {
				prevDraggedTime = System.currentTimeMillis();
				
				dx = Mathh.constrain(-200, x - prevDraggedX, 200); // prevent int overflow
				int prevValue = value;
				setValue((value + dx * dx * dx / dt / w));
				
				if (value != prevValue) {
					// Do not change prevDraggedX on small movements to allow set more precisely.
					// On the next pointerDragged event, dx will be greater
					prevDraggedX = x;
				}
			}
			
			return true;
		}
		
		public boolean keyPressed(int keyCode, int count) {
			if (!isActive || !isVisible) {
	            return false;
	        }
			
			switch (RootContainer.getGameActionn(keyCode)) {
	            case Keys.RIGHT:
	            	setValue(value + count * count);
	                return true;
	            case Keys.LEFT:
	            	setValue(value - count * count);
	                return true;
	            default:
	            	return super.keyPressed(keyCode, count);
	        }
		}
		
		public boolean keyRepeated(int keyCode, int pressedCount) {
			if (!isActive || !isVisible) {
	            return false;
	        }
			
			switch (RootContainer.getGameActionn(keyCode)) {
	            case Keys.RIGHT:
	            	setValue(value + pressedCount * pressedCount);
	                return true;
	            case Keys.LEFT:
	            	setValue(value - pressedCount * pressedCount);
	                return true;
	            default:
	            	return super.keyRepeated(keyCode, pressedCount);
	        }
		}
		
	}

}
