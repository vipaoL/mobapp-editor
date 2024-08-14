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
import mobileapplication3.elements.Element;
import mobileapplication3.elements.Element.PlacementStep;
import mobileapplication3.elements.EndPoint;
import mobileapplication3.utils.Mathh;

public class ElementEditUI extends AbstractPopupWindow {
	
	private Element element;
	private IUIComponent[] rows;
	private List list;
	private StructureBuilder sb;

	public ElementEditUI(Element element, StructureBuilder sb, IPopupFeedback parent) {
		super("Edit " + element.getName(), parent);
		this.element = element;
		this.sb = sb;
		initPage();
	}

	protected Button[] getActionButtons() {
        return new Button[] {
            new Button("OK", new Button.ButtonFeedback() {
                public void buttonPressed() {
                    close();
                }
            })
        };
    }

	protected IUIComponent initAndGetPageContent() {
		PlacementStep[] editSteps = element.getPlacementSteps();
		PlacementStep[] extraEditSteps = element.getExtraEditingSteps();
		String[] argsNames = element.getArgsNames();
		rows = new IUIComponent[editSteps.length + extraEditSteps.length + 2 /*advances edit and delete*/];
		for (int i = 0; i < editSteps.length; i++) {
			final int o = i;
			Button editStepButton = new Button(editSteps[i].getName(), new ButtonFeedback() {
				public void buttonPressed() {
					sb.edit(element, o);
					close();
				}
			});
			rows[o] = new ButtonComponent(editStepButton);
		}
		
		for (int i = 0; i < extraEditSteps.length; i++) {
			final int o = i + editSteps.length;
			Button editStepButton = new Button(extraEditSteps[i].getName(), new ButtonFeedback() {
				public void buttonPressed() {
					sb.edit(element, o);
					close();
				}
			}).setBgColor(0x201010);
			rows[o] = new ButtonComponent(editStepButton);
		}
		
		final IPopupFeedback fb = this;
		Button advancedEditButton = new Button("AdvancedEdit", new ButtonFeedback() {
			public void buttonPressed() {
				showPopup(new AdvancedElementEditUI(element, sb, fb));
			}
		});
		
		Button deleteButton = new Button("Delete element", new ButtonFeedback() {
			public void buttonPressed() {
				sb.remove(element);
				close();
			}
		});
		
		advancedEditButton.setBgColor(0x222200);
		deleteButton.setBgColor(0x550000);
		
		if (element instanceof EndPoint) {
			deleteButton.setIsActive(false);
		}
		
		rows[rows.length - 2] = new ButtonComponent(advancedEditButton);
		rows[rows.length - 1] = new ButtonComponent(deleteButton);
		
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
}
