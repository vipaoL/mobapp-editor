package mobileapplication3.editor;

import mobileapplication3.editor.elements.Element;
import mobileapplication3.platform.FileUtils;
import mobileapplication3.platform.ui.Platform;
import mobileapplication3.ui.AbstractPopupWindow;
import mobileapplication3.ui.Button;
import mobileapplication3.ui.IPopupFeedback;
import mobileapplication3.ui.IUIComponent;

public abstract class AutoSave extends AbstractPopupWindow {
	private final static String STORE_NAME_AUTOSAVE = "AutoSave";

	private Element[] elements;

	public AutoSave(IPopupFeedback parent, Element[] elements) {
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
	
	public static void autoSaveWrite(StructureBuilder data) throws Exception {
		Platform.storeShorts(data.asShortArray(), STORE_NAME_AUTOSAVE);
	}

	public static Element[] autoSaveRead() {
		return FileUtils.readMGStruct(Platform.readStore(STORE_NAME_AUTOSAVE));
	}

	public static void deleteAutoSave() {
		Platform.clearStore(STORE_NAME_AUTOSAVE);
	}

}
