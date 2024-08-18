package mobileapplication3.editor;

import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.elements.Element;

public abstract class AutoSaveRestorer extends AbstractPopupWindow {
	private Element[] elements;
	
	public AutoSaveRestorer(IPopupFeedback parent, Element[] elements) {
		super("Some unsaved data can be recovered", parent);
		this.elements = elements;
	}
	
	public void init() {
		super.init();
        actionButtons.setIsSelectionEnabled(true);
        actionButtons.setIsSelectionVisible(true);
	}

	protected Button[] getActionButtons() {
		return new Button[] {
			new Button("Restore") {
				public void buttonPressed() {
					onRestore();
				}
			},
			new Button("Delete") {
				public void buttonPressed() {
					onDelete();
				}
			}.setBgColor(0x550000)
		};
	}

	protected IUIComponent initAndGetPageContent() {
		return new StructureViewerComponent(elements);
	}
	
	public abstract void onRestore();
	public abstract void onDelete();

}
