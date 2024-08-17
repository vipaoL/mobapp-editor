package mobileapplication3.editor;

import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.Button.ButtonFeedback;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.elements.Element;

public class AutoSaveRestorer extends AbstractPopupWindow {
	
	private ButtonFeedback onRestore;
	private ButtonFeedback onDelete;
	private Element[] elements;
	
	public AutoSaveRestorer(IPopupFeedback parent, Element[] elements, ButtonFeedback onRestore, ButtonFeedback onDelete) {
		super("Some unsaved data can be recovered", parent);
		this.onRestore = onRestore;
		this.onDelete = onDelete;
		this.elements = elements;
		initPage();
	}

	protected Button[] getActionButtons() {
		return new Button[] {
			new Button("Restore", new Button.ButtonFeedback() {
				public void buttonPressed() {
					onRestore.buttonPressed();
				}
			}),
			new Button("Delete", new Button.ButtonFeedback() {
				public void buttonPressed() {
					onDelete.buttonPressed();
				}
			})
		};
	}

	protected IUIComponent initAndGetPageContent() {
		return new StructureViewerComponent(elements);
	}

}
