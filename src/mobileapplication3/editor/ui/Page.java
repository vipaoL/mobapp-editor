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
    private boolean isInited = false;

    public Page(String title) {
        System.out.println("new TextComponent: " + title);
        this.title = new TextComponent(title);
        this.actionButtons = (ButtonRow) new ButtonRow();
                //.setButtonsBgColor(0x3333aa)
                //.setSelectedColor(0x9999ff);
    }

    public boolean keyPressed(int keyCode, int count) {
//        String title = "<null>";
//        if (this.title != null) {
//            title = this.title.getText();
//        }
//        System.out.println(title + ": keyPressed: " + keyCode);
        
        return super.keyPressed(keyCode, count);
    }
    
    protected void initPage() {
        if (!isInited) {
            isInited = true;
            pageContent = initAndGetPageContent();
            actionButtons.setButtons(getActionButtons());
            actionButtons.bindToSoftButtons(0, actionButtons.getButtonCount() - 1);
            actionButtons.setSelected(actionButtons.getButtonCount() - 1);
            setComponents(new IUIComponent[]{title, actionButtons, pageContent});
        }
    }

    public final void onSetBounds(int x0, int y0, int w, int h) {
        if (!isInited) {
            try {
                throw new IllegalStateException("Error: call initPage() first!");
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        
        margin = w / 16;
        
        title
                .setSize(w, TextComponent.HEIGHT_AUTO)
                .setPos(x0, y0, TOP | LEFT);
        actionButtons.setIsSelectionVisible(true)
                .setButtonsBgPadding(margin/4)
                .setSize(w, AbstractButtonSet.H_AUTO)
                .setPos(x0 + w/2, y0 + h, BOTTOM | HCENTER);
        setPageContentBounds(x0, title.getBottomY(), w, actionButtons.getTopY() - title.getBottomY());
    }
    
    public void setPageContentBounds(int x0, int y0, int w, int h) {
        if (pageContent != null) {
            pageContent
                    .setSize(w - margin*2, h - margin*2)
                    .setPos(x0 + w/2, y0 + h - margin, BOTTOM | HCENTER);
        }
    }
    
    protected abstract Button[] getActionButtons();
    protected abstract IUIComponent initAndGetPageContent();
    
}