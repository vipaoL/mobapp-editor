package mobileapplication3.editor;

import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.elements.Element;
import mobileapplication3.elements.Element.PlacementStep;
import mobileapplication3.elements.EndPoint;

public class ElementEditUI extends AbstractPopupWindow {
	
	private Element element;
	private Button[] rows;
	private ButtonCol list;
	private StructureBuilder sb;

	public ElementEditUI(Element element, StructureBuilder sb, IPopupFeedback parent) {
		super("Edit " + element.getName(), parent);
		this.element = element;
		this.sb = sb;
	}

	protected Button[] getActionButtons() {
        return new Button[] {
            new Button("OK") {
                public void buttonPressed() {
                    close();
                }
            }
        };
    }

	protected IUIComponent initAndGetPageContent() {
		PlacementStep[] editSteps = element.getPlacementSteps();
		PlacementStep[] extraEditSteps = element.getExtraEditingSteps();
		rows = new Button[editSteps.length + extraEditSteps.length + 3 /*clone, advanced edit and delete*/];
		for (int i = 0; i < editSteps.length; i++) {
			final int o = i;
			rows[o] = new Button(editSteps[i].getName()) {
				public void buttonPressed() {
					sb.edit(element, o);
					close();
				}
			};
		}
		
		for (int i = 0; i < extraEditSteps.length; i++) {
			final int o = i + editSteps.length;
			rows[o] = new Button(extraEditSteps[i].getName()) {
				public void buttonPressed() {
					sb.edit(element, o);
					close();
				}
			}.setBgColor(0x201010);
		}
		
		Button cloneButton = new Button("Clone") {
			public void buttonPressed() {
				Element clone = element.clone();
				sb.add(clone);
				sb.edit(clone, 0);
				close();
			}
		};
		
		final IPopupFeedback fb = this;
		Button advancedEditButton = new Button("AdvancedEdit") {
			public void buttonPressed() {
				showPopup(new AdvancedElementEditUI(element, sb, fb));
			}
		};
		
		Button deleteButton = new Button("Delete element") {
			public void buttonPressed() {
				sb.remove(element);
				close();
			}
		};
		
		advancedEditButton.setBgColor(0x222200);
		deleteButton.setBgColor(0x550000);
		
		if (element instanceof EndPoint) {
			cloneButton.setIsActive(false);
			deleteButton.setIsActive(false);
		}
		
		rows[rows.length - 3] = (cloneButton);
		rows[rows.length - 2] = (advancedEditButton);
		rows[rows.length - 1] = (deleteButton);
		
		list = (ButtonCol) new ButtonCol() {
			public final void onSetBounds(int x0, int y0, int w, int h) {
				setButtonsBgPadding(getBtnH()/16);
				super.onSetBounds(x0, y0, w, h);
			}
		}
				.enableScrolling(true, false)
                .trimHeight(true)
                .setButtons(rows)
                .setIsSelectionEnabled(true)
                .setIsSelectionVisible(true);
		return list;
	}
}
