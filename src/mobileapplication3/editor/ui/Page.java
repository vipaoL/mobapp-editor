/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mobileapplication3.editor.ui;

/**
 *
 * @author vipaol
 */
public abstract class Page extends Container {
    
    protected TextComponent title = null;
    protected IUIComponent pageContent = null;
    protected ButtonRow actionButtons = null;
    protected int margin;
    protected boolean isInited = false;

    public Page(String title) {
        this.title = new TextComponent(title);
        actionButtons = (ButtonRow) new ButtonRow() {
        	public boolean canBeFocused() {
        		return false;
        	}
        };
                //.setButtonsBgColor(0x3333aa)
                //.setSelectedColor(0x9999ff);
    }
    
    public void init() {
    	if (!isInited) {
            isInited = true;
            pageContent = initAndGetPageContent();
            actionButtons.setButtons(getActionButtons());
            actionButtons.bindToSoftButtons(0, actionButtons.getButtonCount() - 1);
            actionButtons.setFocused(false);
            // TODO call onSetBounds if it was called before and failed because initPage had't done
        }
    	setComponents(new IUIComponent[]{title, pageContent, actionButtons});
    }

    public boolean keyPressed(int keyCode, int count) {
        return super.keyPressed(keyCode, count);
    }

    public final void onSetBounds(int x0, int y0, int w, int h) {
        if (!isInited) {
            try {
                throw new IllegalStateException("Error: init() hadn't done! " + getClass().getName());
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        
        margin = w / 32;
        
        title
                .setSize(w, TextComponent.HEIGHT_AUTO)
                .setPos(x0, y0, TOP | LEFT);
        actionButtons
                .setButtonsBgPadding(margin/4)
                .setSize(w, AbstractButtonSet.H_AUTO)
                .setPos(x0 + w/2, y0 + h, BOTTOM | HCENTER);
        setPageContentBounds(pageContent, x0, title.getBottomY(), w, actionButtons.getTopY() - title.getBottomY());
    }
    
    protected void setPageContentBounds(IUIComponent pageContent, int x0, int y0, int w, int h) {
        if (pageContent != null) {
            pageContent
                    .setSize(w - margin*2, h - margin*2)
                    .setPos(x0 + w/2, y0 + h - margin, BOTTOM | HCENTER);
        }
    }
    
    public Page setTitle(String title) {
    	this.title.setText(title);
    	return this;
    }
    
    protected abstract Button[] getActionButtons();
    protected abstract IUIComponent initAndGetPageContent();
    
}