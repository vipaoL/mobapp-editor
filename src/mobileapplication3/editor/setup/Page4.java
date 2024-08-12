/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.setup;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Graphics;
import mobileapplication3.editor.Main;
import mobileapplication3.utils.Paths;
import mobileapplication3.utils.Settings;
import mobileapplication3.editor.ui.AbstractPopupWindow;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.IPopupFeedback;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.UIComponent;
import mobileapplication3.utils.FileUtils;

/**
 *
 * @author vipaol
 */
public class Page4 extends AbstractSetupWizardPage {
    
    //PathPicker folderPicker;
    private ButtonCol list;
    private Button[] listButtons;
    private AbstractPopupWindow popupLoadingScreen;

    public Page4(Button[] buttons, SetupWizard.Feedback feedback) {
        super("Let's pick a folder", buttons, feedback);
        list = new ButtonCol() {
            public boolean canBeFocused() {
                return false;
            }
        };
//        this.folderPicker = new PathPicker();
//        folderPicker.pickMGStructFolder("Press \"OK\" if you want your structures to be saved here.", new PathPicker.Feedback() {
//            public void onComplete(String path) {
//            }
//
//            public void onCancel() {
//            }
//
//            public void needRepaint() {
//                feedback.needRepaint();
//            }
//        });
        initPage();
    }
    
    public void initOnFirstShow() {
        fillList();
        
        actionButtons.buttons[1].setFeedback(new Button.ButtonFeedback() {
            public void buttonPressed() {
                String path = list.buttons[list.getSelected()].getTitle();
                saveFolderChoise(path);
            }
        });
        
        list.enableScrolling(true, true)
                .setIsSelectionVisible(true)
                .setButtonsBgPadding(margin/4)
                .setBgColor(0x111111);
    }
    
    private void fillList() {
        (new Thread(new Runnable() {
            public void run() {
                String[] folders = Paths.getAllMGStructsFolders();
                listButtons = new Button[folders.length];
                for (int i = 0; i < folders.length; i++) {
                    listButtons[i] = new Button(folders[i], null);
                }
                list.setButtons(listButtons);
                list.setSelected(list.buttons.length - 1);
                onSetBounds(x0, y0, w, h);
                feedback.needRepaint();
            }
        })).start();
    }
    
    private void saveFolderChoise(final String path) {
        showPopup(new LoadingPopup("Checking folder...", this));
        (new Thread(new Runnable() {
            public void run() {
                try {
                    FileUtils.createFolder(path);
                } catch (Exception ex) {
                    closePopup();
                    Main.setCurrent(new Alert("Error!", "Can't create folder: " + ex.toString(), null, AlertType.ERROR));
                    ex.printStackTrace();
                }
                
                try {
                    FileUtils.checkFolder(path);
                    Settings.setMgstructsFolderPath(path);
                    feedback.nextPage();
                } catch (Exception ex) {
                    closePopup();
                    Main.setCurrent(new Alert("Error!", "Can't create file in this folder: " + ex, null, AlertType.ERROR));
                    ex.printStackTrace();
                }
            }
        })).start();
    }

    public void setPageContentBounds(int x0, int y0, int w, int h) {
        if (pageContent != null) {
            ((ButtonCol) pageContent)
                    .setSizes(w - margin*2, h - margin*2, ButtonCol.H_AUTO, true)
                    .setPos(x0 + w/2, y0 + h - margin, BOTTOM | HCENTER);
        }
    }

    protected IUIComponent initAndGetPageContent() {
        return list;
    }
    
    private class LoadingPopup extends AbstractPopupWindow {
        int animOffset = 100;

        public LoadingPopup(String title, IPopupFeedback parent) {
            super(title, parent);
            initPage();
            new Thread(new Runnable() {
                public void run() {
                    while (hasParent()) {                        
                        animOffset += 10;
                        repaint();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }, "loading anim").start();
        }
        
        protected boolean handlePointerReleased(int x, int y) {
            return true;
        }

        protected boolean handleKeyPressed(int keyCode, int count) {
            return true;
        }

        public boolean canBeFocused() {
            return true;
        }

        protected Button[] getActionButtons() {
            return new Button[] {
                new Button("Cancel", null).setIsActive(false)
            };
        }

        protected IUIComponent initAndGetPageContent() {
            return new UIComponent() {
                protected boolean handlePointerReleased(int x, int y) {
                    return false;
                }

                protected boolean handleKeyPressed(int keyCode, int count) {
                    return false;
                }

                public void onPaint(Graphics g, int x0, int y0, int w, int h) {
                    System.out.println("drawing");
                    g.setColor(0xffffff);
                    int side = Math.min(w, h);
                    g.drawArc(x0 + (w - side)/2, y0 + (h - side)/2, side, side, animOffset % 360, (animOffset + 250) % 360);
                    g.drawString("Please", x0 + w/2, y0 + h/2, Graphics.BOTTOM | Graphics.HCENTER);
                    g.drawString("wait....", x0 + w/2, y0 + h/2, Graphics.TOP | Graphics.HCENTER);
                }

                public boolean canBeFocused() {
                    return false;
                }
            };
        }
    }
    
}
