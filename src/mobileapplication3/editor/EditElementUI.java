package mobileapplication3.editor;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.Button.ButtonFeedback;
import mobileapplication3.editor.ui.ButtonComponent;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.List;
import mobileapplication3.editor.ui.TextComponent;
import mobileapplication3.editor.ui.UIComponent;
import mobileapplication3.elements.Element;
import mobileapplication3.elements.EndPoint;
import mobileapplication3.utils.Mathh;

public class EditElementUI extends AbstractPopupWindow {
	
	private Element element;
	private IUIComponent[] rows;
	private List list;
	private StructureBuilder sb;

	public EditElementUI(Element element, StructureBuilder sb, IPopupFeedback parent) {
		super("Edit " + element.getName(), parent);
		this.element = element;
		this.sb = sb;
		initPage();
	}

	protected Button[] getActionButtons() {
        return new Button[] {
            new Button("OK", new Button.ButtonFeedback() {
                public void buttonPressed() {
                	short[] args = new short[element.getArgsCount()];
                	for (int i = 0; i < args.length; i++) {
            			args[i] = ((ListRow) rows[i]).getValue();
            		}
                	element.setArgs(args);
                	element.recalcCalculatedArgs();
                	sb.recalcEndPoint();
                    close();
                }
            }),
            new Button("Cancel", new Button.ButtonFeedback() {
                public void buttonPressed() {
                    close();
                }
            })
        };
    }

	protected IUIComponent initAndGetPageContent() {
		short[] args = element.getArgs();
		String[] argsNames = element.getArgsNames();
		rows = new IUIComponent[args.length + 1];
		for (int i = 0; i < args.length; i++) {
			rows[i] = new ListRow(argsNames[i], args[i]);
		}
		
		Button deleteButton = new Button("Delete element", new ButtonFeedback() {
			public void buttonPressed() {
				sb.remove(element);
				close();
			}
		});
		
		deleteButton.setBgColor(0x550000);
		
		if (element instanceof EndPoint) {
			deleteButton.setIsActive(false);
		}
		
		rows[args.length] = new ButtonComponent(deleteButton);
		
		list = new List() {
			public final void onSetBounds(int x0, int y0, int w, int h) {
				setElementsPadding(getElemH()/16);
				super.onSetBounds(x0, y0, w, h);
			}
		}
				.setElements(rows)
				.enableScrolling(true, false)
				.enableAnimations(false)
                .trimHeight(true)
                .setIsSelectionEnabled(true)
                .setIsSelectionVisible(true);
		return list;
	}
	
	class ListRow extends Container {

		private TextComponent title;
		private TextComponent valueIndicator;
		private ButtonRow buttonRow;
		
		private int prevDraggedX;
		private long prevDraggedTime = 0;
		private short minValue = Short.MIN_VALUE;
		private short maxValue = Short.MAX_VALUE;
		private short value;
		private short valueWhenPointerPressed;
		
		public ListRow(String paramName, short value) {
			this.value = value;
			
			title = new TextComponent(paramName);
			title.setBgColor(COLOR_TRANSPARENT);
			
			valueIndicator = new TextComponent(String.valueOf(value));
			valueIndicator.setBgColor(COLOR_TRANSPARENT);
			
			buttonRow = new ButtonRow();
			buttonRow.setBgColor(COLOR_TRANSPARENT);
			buttonRow.setButtonsBgColor(COLOR_TRANSPARENT);
			buttonRow.setButtons(new Button[] {
					new Button("-", new ButtonFeedback() {
						public void buttonPressed() {
							setValue((short) (getValue() - 1));
						}
					}),
					new Button("+", new ButtonFeedback() {
						public void buttonPressed() {
							setValue((short) (getValue() + 1));
						}
					})
			});
			
			setBgColor(COLOR_ACCENT);
			roundBg(true);
			setComponents(new IUIComponent[] {title, valueIndicator, buttonRow});
		}
		
		protected void drawBg(Graphics g, int x0, int y0, int w, int h) {
			int prevClipX = g.getClipX();
	        int prevClipY = g.getClipY();
	        int prevClipW = g.getClipWidth();
	        int prevClipH = g.getClipHeight();
	        
	        int barLeftW = w * (value - minValue) / (maxValue - minValue);
	        
	        g.setColor(COLOR_ACCENT);
	        g.setClip(x0, y0, barLeftW, h);
			g.fillRect(x0, y0, w, h);
			
			g.setColor(COLOR_ACCENT_MUTED);
			g.setClip(x0 + barLeftW, y0, w - barLeftW, h);
			g.fillRect(x0, y0, w, h);
			
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
			return value;
		}
		
		public void setValue(int value) {
			this.value = (short) Mathh.constrain(minValue, value, maxValue);
			valueIndicator.setText(String.valueOf(this.value));
		}
		
		public boolean pointerPressed(int x, int y) {
			valueWhenPointerPressed = value;
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
				setValue((value + dx * dx * dx * 100 / dt / w));
				
				if (value != prevValue) {
					// Do not change prevDraggedX on small movements to allow set more precisely.
					// On the next pointerDragged event, dx will be greater
					prevDraggedX = x;
				}
			}
			
			return true;
		}
		
		public boolean keyPressed(int keyCode, int count) {
			System.out.println("pressed " + count);
			switch (Main.util.getGameAction(keyCode)) {
	            case Canvas.RIGHT:
	            	setValue(value + count * count);
	                return true;
	            case Canvas.LEFT:
	            	setValue(value - count * count);
	                return true;
	            default:
	            	return super.keyPressed(keyCode, count);
	        }
		}
		
		public boolean keyRepeated(int keyCode, int pressedCount) {
			System.out.println("repeated " + pressedCount);
			switch (Main.util.getGameAction(keyCode)) {
	            case Canvas.RIGHT:
	            	setValue(value + pressedCount * pressedCount);
	                return true;
	            case Canvas.LEFT:
	            	setValue(value - pressedCount * pressedCount);
	                return true;
	            default:
	            	return super.keyRepeated(keyCode, pressedCount);
	        }
		}
		
	}

}
