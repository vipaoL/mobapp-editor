/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.utils.Settings;
import mobileapplication3.editor.ui.AbstractButtonSet;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonPanelHorizontal;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.Container;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import mobileapplication3.elements.Element;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import mobileapplication3.editor.ui.ButtonComponent;
import mobileapplication3.editor.ui.IUIComponent;

/**
 *
 * @author vipaol
 */
public class MainScreenUI extends Container {
    
    private final static int BTNS_IN_ROW = 4;
    public final static int FONT_H = Font.getDefaultFont().getHeight();
    public final static int BTN_H = FONT_H*2;
    private Button btnLoad, btnSave, btnPlace, btnList, zoomIn, zoomOut;;
    private EditorCanvas editorCanvas = null;
    private ButtonRow bottomButtonPanel = null, zoomPanel = null;
    private ButtonPanelHorizontal placementButtonPanel = null;
    private ButtonCol placedElementsList = null;
    private ButtonComponent settingsButton = null;
    private PathPicker pathPicker = null;
    private StructureBuilder elementsBuffer;
    
    public MainScreenUI() {
        try {
            elementsBuffer = new StructureBuilder(new StructureBuilder.Feedback() {
                public void onUpdate() {
                    initListPanel();
                }
            });
            
            settingsButton = new ButtonComponent(new Button("Settings" + (Main.hasPointerEvents ? "" : " (0)"), new Button.ButtonFeedback() {
                public void buttonPressed() {
                    showPopup(getSettingsUIObject());
                }
            })) {
            public boolean handleKeyPressed(int keyCode, int count) {
                switch (keyCode) {
                    case Canvas.KEY_NUM0:
                        buttons[0].invokePressed(false, false);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        };

            initBottomPanel();

            initZoomPanel();

            initEditorCanvas();

            initPlacementPanel();

            initListPanel();

            initPathPicker();
            
            setComponents(new IUIComponent[]{editorCanvas, bottomButtonPanel, zoomPanel, placementButtonPanel, placedElementsList, settingsButton, pathPicker});
        } catch(Exception ex) {
            ex.printStackTrace();
            Main.setCurrent(new Alert(ex.toString()));
        }
    }
    
    private SettingsUI getSettingsUIObject() {
        return new SettingsUI(this);
    }
    
    private ElementEditUI getElementEditScreenObject(Element element, StructureBuilder sb) {
        return new ElementEditUI(element, sb, this);
    }
    
    private void initEditorCanvas() {
        editorCanvas = (EditorCanvas) new EditorCanvas(elementsBuffer);
    }
    
    private void initBottomPanel() {
        btnPlace = new Button("Place", new Button.ButtonFeedback() {
            public void buttonPressed() {
                placedElementsList.setVisible(false);
                placementButtonPanel.toggleIsVisible();
            }
        });
        
        btnLoad = new Button("Open" + (Main.hasPointerEvents ? "" : " (8)"), new Button.ButtonFeedback() {
            public void buttonPressed() {
                pathPicker.pickFile(Settings.getMgstructsFolderPath(), "Open " + PathPicker.QUESTION_REPLACE_WITH_PATH + " ?", new PathPicker.Feedback() {
                    public void onComplete(final String path) {
                        (new Thread(new Runnable() {
                            public void run() {
                                System.out.println("Open: " + path);
                                elementsBuffer.loadFile(path);
                                System.out.println("Loaded!");
                                setIsPathPickerVisible(false);
                                repaint();
                            }
                        })).start();
                    }

                    public void onCancel() {
                        setIsPathPickerVisible(false);
                    }

                    public void needRepaint() {
                        repaint();
                    }
                });
                setIsPathPickerVisible(true);
            }
        });
        
        btnSave = new Button("Save" + (Main.hasPointerEvents ? "" : " (9)"), new Button.ButtonFeedback() {
            public void buttonPressed() {
                pathPicker.pickFolder(Settings.getMgstructsFolderPath(), "Save as " + PathPicker.QUESTION_REPLACE_WITH_PATH + " ?", new PathPicker.Feedback() {
                    public void onComplete(final String path) {
                        (new Thread(new Runnable() {
                            public void run() {
                                try {
                                    System.out.println("Save: " + path);
                                    elementsBuffer.saveToFile(path);
                                    System.out.println("Saved!");
                                    setIsPathPickerVisible(false);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Main.setCurrent(new Alert(ex.toString(), ex.toString(), null, AlertType.ERROR));
                                }
                                repaint();
                            }
                        })).start();
                    }

                    public void needRepaint() {
                        repaint();
                    }

                    public void onCancel() {
                        setIsPathPickerVisible(false);
                    }
                });
                setIsPathPickerVisible(true);
            }
        });
        
        btnList = new Button("List", new Button.ButtonFeedback() {
            public void buttonPressed() {
                placementButtonPanel.setVisible(false);
                placedElementsList.toggleIsVisible();
            }
        });
        
        Button[] bottomButtons = {btnPlace, btnLoad, btnSave, btnList};
        bottomButtonPanel = (ButtonRow) new ButtonRow(){
            public boolean handleKeyPressed(int keyCode, int count) {
                switch (keyCode) {
                    case -6:
                        btnPlace.invokePressed(false, false);
                        break;
                    case -7:
                        btnList.invokePressed(false, false);
                        break;
                    case Canvas.KEY_NUM8:
                        btnLoad.invokePressed(false, false);
                        break;
                    case Canvas.KEY_NUM9:
                        btnSave.invokePressed(false, false);
                        break;
                    case Canvas.KEY_NUM0:
                        // new file
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }.setButtons(bottomButtons).setButtonsBgColor(0x303030);
    }
    
    private void initZoomPanel() {
        zoomIn = new Button("+" + (Main.hasPointerEvents ? "" : " (*)"), new Button.ButtonFeedback() {
            public void buttonPressed() {
                editorCanvas.zoomIn();
            }
        });
        
        zoomOut = new Button("-" + (Main.hasPointerEvents ? "" : " (#)"), new Button.ButtonFeedback() {
            public void buttonPressed() {
                editorCanvas.zoomOut();
            }
        });
        
        Button[] zoomPanelButtons = {zoomIn, zoomOut};
        zoomPanel = (ButtonRow) new ButtonRow(zoomPanelButtons){
            public boolean handleKeyPressed(int keyCode, int count) {
                switch (keyCode) {
                    case Canvas.KEY_STAR:
                        zoomIn.invokePressed(false, false);
                        break;
                    case Canvas.KEY_POUND:
                        zoomOut.invokePressed(false, false);
                        break;
                    default:
                        return false;
                }
                return true;
            }
            
        }.setButtonsBgColor(0x000020);
    }
    
    private void initPlacementPanel() {
        Button btnLine = new Button("Line", new Button.ButtonFeedback() {
            public void buttonPressed() {
                elementsBuffer.place(Element.LINE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        });
        
        Button btnCircle = new Button("Circle", new Button.ButtonFeedback() {
            public void buttonPressed() {
                elementsBuffer.place(Element.CIRCLE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        });
        
        Button btnSine = new Button("Sine", new Button.ButtonFeedback() {
            public void buttonPressed() {
                elementsBuffer.place(Element.SINE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        });
        
        Button btnBrLine = new Button("Broken\nline", new Button.ButtonFeedback() {
            public void buttonPressed() {
                elementsBuffer.place(Element.BROKEN_LINE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        });
        
        Button btnBrCircle = new Button("Broken\ncircle", new Button.ButtonFeedback() {
            public void buttonPressed() {
                elementsBuffer.place(Element.BROKEN_CIRCLE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        });
        
        Button btnAccel = new Button("Accele-\nrator", new Button.ButtonFeedback() {
            public void buttonPressed() {
                elementsBuffer.place(Element.ACCELERATOR, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        });
        
        Button[] placementButtons = {btnLine, btnCircle, btnSine, btnBrLine, btnBrCircle.setIsActive(false), btnAccel.setIsActive(false)};
        placementButtonPanel = new ButtonPanelHorizontal(placementButtons)
                .setBtnsInRowCount(BTNS_IN_ROW);
        placementButtonPanel.setIsSelectionEnabled(true);
        placementButtonPanel.setVisible(false);
        placementButtonPanel.setIsSelectionVisible(!Main.hasPointerEvents);
    }

    private void initListPanel() {
        Element[] elements = elementsBuffer.getElementsAsArray();
        System.out.println("updating, " + elements.length);
        Button[] listButtons = new Button[elements.length];
        for (int i = 0; i < elements.length; i++) {
            final int o = i;
            final Element element = elements[i];
            listButtons[i] = new Button(elements[i].getName(), new Button.ButtonFeedback() {
                public void buttonPressed() {
                    //System.out.println(o + "selected");
                }
                public void buttonPressedSelected() {
                    showPopup(getElementEditScreenObject(element, elementsBuffer));
                    //elementsBuffer.remove(o);
                }
            });
        }
        
        if (placedElementsList == null) {
            placedElementsList = (ButtonCol) new ButtonCol() {
            	public AbstractButtonSet setSelected(int selected) {
            		editorCanvas.selectedElement = selected;
            		return super.setSelected(selected);
            	}
            }
                    .enableScrolling(true, true)
                    .setIsSelectionEnabled(true)
                    .setIsSelectionVisible(true)
                    .setVisible(false);
        }
        
        placedElementsList.setButtons(listButtons);
    }
    
    private void initPathPicker() {
        pathPicker = (PathPicker) new PathPicker()
                .setVisible(false);
    }
    
    private void setIsPathPickerVisible(boolean b) {
        if (b) {
            pathPicker.setBgImage(getCapture());
        }
        pathPicker.setVisible(b);
        editorCanvas.setVisible(!b);
        bottomButtonPanel.setVisible(!b);
        zoomPanel.setVisible(!b);
        settingsButton.setVisible(!b);
    }

    public void onSetBounds(int x0, int y0, int w, int h) {
        bottomButtonPanel
                .setSize(w, BTN_H)
                .setPos(x0, y0 + h, BOTTOM | LEFT);
        editorCanvas
                .setSize(w, h - bottomButtonPanel.h)
                .setPos(x0, y0, TOP | LEFT);
        zoomPanel
                .setSize(w/2, BTN_H)
                .setPos(x0 + w/2, y0 + h - bottomButtonPanel.h, BOTTOM | LEFT);
        placementButtonPanel
                .setSizes(w, ButtonPanelHorizontal.H_AUTO, BTN_H)
                .setPos(x0, y0 + h - bottomButtonPanel.h, BOTTOM | LEFT);
        settingsButton
                .setSize(ButtonComponent.W_AUTO, ButtonComponent.H_AUTO)
                .setPos(x0 + w, y0, TOP | RIGHT)
                .setVisible(true);
        placedElementsList
                .setSizes(w/5, bottomButtonPanel.getTopY() - settingsButton.getBottomY() - BTN_H / 4, FONT_H * 3)
                .setPos(x0 + w, y0 + h - bottomButtonPanel.h, RIGHT | BOTTOM);
        pathPicker
                .setSizes(w, h, BTN_H)
                .setPos(x0, y0);
    }
}
