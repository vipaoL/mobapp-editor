/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.editor.elements.Element;
import mobileapplication3.editor.elements.StartPoint;
import mobileapplication3.platform.ui.Font;
import mobileapplication3.platform.ui.Graphics;
import mobileapplication3.platform.ui.Platform;
import mobileapplication3.platform.ui.RootContainer;
import mobileapplication3.ui.AbstractButtonSet;
import mobileapplication3.ui.Button;
import mobileapplication3.ui.ButtonCol;
import mobileapplication3.ui.ButtonComponent;
import mobileapplication3.ui.ButtonPanelHorizontal;
import mobileapplication3.ui.ButtonRow;
import mobileapplication3.ui.Container;
import mobileapplication3.ui.IUIComponent;
import mobileapplication3.ui.Keys;
import mobileapplication3.ui.TextComponent;

/**
 *
 * @author vipaol
 */
public class MainScreenUI extends Container {
    
    private final static int BTNS_IN_ROW = 4;
    public final static int FONT_H = Font.getDefaultFontHeight();
    public final static int BTN_H = FONT_H*2;

    private Button btnLoad, btnSave, btnPlace, btnList, zoomIn, zoomOut;;
    private EditorCanvas editorCanvas = null;
    private ButtonRow bottomButtonPanel = null, zoomPanel = null;
    private ButtonPanelHorizontal placementButtonPanel = null;
    private ButtonCol placedElementsList = null;
    private ButtonComponent settingsButton = null;
    private PathPicker pathPicker = null;
    private StartPointWarning startPointWarning = null;
    private StructureBuilder elementsBuffer;
    private boolean postInitDone = false;
    private boolean isAutoSaveEnabled = true;
    
    public MainScreenUI() {
    	elementsBuffer = new StructureBuilder(new StructureBuilder.Feedback() {
            public void onUpdate() {
                initListPanel();
                saveToRMS();
            }
        });
    	initEditorCanvas();
	}
    
    public void init() {
    	try {
            initBottomPanel();
            
            initStartPointWarning();

            initZoomPanel();

            initPlacementPanel();

            initSettingsButton();
            
            initListPanel();

            initPathPicker();
            
        } catch(Exception ex) {
            ex.printStackTrace();
            Platform.showError(ex);
        }
    	isAutoSaveEnabled = EditorSettings.getAutoSaveEnabled(true);
    	setComponents();
    }
    
    private void checkAutoSaveStorage() {
    	if (!isAutoSaveEnabled) {
    		return;
    	}

        try {
        	final Element[] elements = AutoSave.autoSaveRead();
            if (elements != null && elements.length > 2) {
                showPopup(new AutoSave(this, elements) {
        			public void onRestore() {
        				elementsBuffer.setElements(elements);
        				close();
        			};
        			
        			public void onDelete() {
                        AutoSave.deleteAutoSave();
        				close();
        			};
        		});
        	}
        } catch (Exception ignored) { }
    }
    
    private void saveToRMS() {
    	if (isAutoSaveEnabled && elementsBuffer != null) {
    		new Thread(new Runnable() {
				public void run() {
					try {
						AutoSave.autoSaveWrite(elementsBuffer);
					} catch (Exception ex) {
						ex.printStackTrace();
						Platform.showError(ex);
					}
				}
			}).start();
    	}
	}
    
    private void setComponents() {
    	setComponents(new IUIComponent[]{editorCanvas, startPointWarning, zoomPanel, placementButtonPanel, settingsButton, placedElementsList, bottomButtonPanel, pathPicker});
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
        btnPlace = new Button("Place") {
            public void buttonPressed() {
                placedElementsList.setVisible(false);
                placementButtonPanel.toggleIsVisible();
                placementButtonPanel.setFocused(placementButtonPanel.getIsVisible());
            }
        };
        
        btnLoad = new Button("Open" + (RootContainer.displayKbHints ? " (8)" : "")) {
            public void buttonPressed() {
                pathPicker.pickFile(EditorSettings.getMgstructsFolderPath(), "Open " + PathPicker.QUESTION_REPLACE_WITH_PATH + " ?", new PathPicker.Feedback() {
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
        };
        
        btnSave = new Button("Save" + (RootContainer.displayKbHints ? " (9)" : "")) {
            public void buttonPressed() {
                pathPicker.pickFolder(EditorSettings.getMgstructsFolderPath(), "Save as " + PathPicker.QUESTION_REPLACE_WITH_PATH + " ?", new PathPicker.Feedback() {
                    public void onComplete(final String path) {
                        (new Thread(new Runnable() {
                            public void run() {
                                try {
                                    System.out.println("Save: " + path);
                                    elementsBuffer.saveToFile(path);
                                    System.out.println("Saved!");
                                    setIsPathPickerVisible(false);
                                    AutoSave.deleteAutoSave();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Platform.showError(ex);
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
        };
        
        btnList = new Button("Edit") {
            public void buttonPressed() {
                placementButtonPanel.setVisible(false);
                placedElementsList.toggleIsVisible();
                placedElementsList.setFocused(placedElementsList.getIsVisible());
            }
        };
        
        Button[] bottomButtons = {btnPlace, btnLoad, btnSave, btnList};
        bottomButtonPanel = (ButtonRow) new ButtonRow(){
        	public boolean canBeFocused() {
        		return false;
        	}
        	
            public boolean handleKeyPressed(int keyCode, int count) {
                switch (keyCode) {
                    case -6:
                        btnPlace.invokePressed(false, false);
                        break;
                    case -7:
                        btnList.invokePressed(false, false);
                        break;
                    case Keys.KEY_NUM8:
                        btnLoad.invokePressed(false, false);
                        break;
                    case Keys.KEY_NUM9:
                        btnSave.invokePressed(false, false);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }.setButtons(bottomButtons).setButtonsBgColor(0x303030);
    }
    
    private void initStartPointWarning() {
    	startPointWarning = (StartPointWarning) new StartPointWarning().setVisible(false);
    }
    
    private void initZoomPanel() {
        zoomIn = new Button("+" + (RootContainer.displayKbHints ? " (*)" : "")) {
            public void buttonPressed() {
                editorCanvas.zoomIn();
            }
        };
        
        zoomOut = new Button("-" + (RootContainer.displayKbHints ? " (#)" : "")) {
            public void buttonPressed() {
                editorCanvas.zoomOut();
            }
        };
        
        Button[] zoomPanelButtons = {zoomIn, zoomOut};
        zoomPanel = (ButtonRow) new ButtonRow(zoomPanelButtons){
            public boolean handleKeyPressed(int keyCode, int count) {
                switch (keyCode) {
                    case Keys.KEY_STAR:
                        zoomIn.invokePressed(false, false);
                        break;
                    case Keys.KEY_POUND:
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
        Button btnLine = new Button("Line") {
            public void buttonPressed() {
                elementsBuffer.place(Element.LINE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        };
        
        Button btnCircle = new Button("Circle") {
            public void buttonPressed() {
                elementsBuffer.place(Element.CIRCLE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        };
        
        Button btnSine = new Button("Sine") {
            public void buttonPressed() {
                elementsBuffer.place(Element.SINE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        };
        
        Button btnBrLine = new Button("Broken\nline") {
            public void buttonPressed() {
                elementsBuffer.place(Element.BROKEN_LINE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        };
        
        Button btnBrCircle = new Button("Broken\ncircle") {
            public void buttonPressed() {
                elementsBuffer.place(Element.BROKEN_CIRCLE, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        };
        
        Button btnAccel = new Button("Accele-\nrator") {
            public void buttonPressed() {
                elementsBuffer.place(Element.ACCELERATOR, (short) editorCanvas.getCursorX(), (short) editorCanvas.getCursorY());
                placementButtonPanel.setVisible(false);
            }
        };
        
        Button[] placementButtons = {btnLine, btnCircle, btnSine, btnBrLine, btnBrCircle.setIsActive(false), btnAccel};
        placementButtonPanel = new ButtonPanelHorizontal(placementButtons)
                .setBtnsInRowCount(BTNS_IN_ROW);
        placementButtonPanel.setIsSelectionEnabled(true);
        placementButtonPanel.setVisible(false);
        placementButtonPanel.setIsSelectionVisible(RootContainer.displayKbHints);
    }
    
    private void initSettingsButton() {
    	settingsButton = new ButtonComponent(new Button("Settings" + (RootContainer.displayKbHints ? " (0)" : "")) {
            public void buttonPressed() {
                showPopup(getSettingsUIObject());
            }
        }) {
            public boolean handleKeyPressed(int keyCode, int count) {
                switch (keyCode) {
                    case Keys.KEY_NUM0:
                        buttons[0].invokePressed(false, false);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    private void initListPanel() {
        Element[] elements = elementsBuffer.getElementsAsArray();
        System.out.println("updating, " + elements.length);
        Button[] listButtons = new Button[elements.length];
        for (int i = 0; i < elements.length; i++) {
            //final int o = i;
            final Element element = elements[i];
            listButtons[i] = new Button(elements[i].getName()) {
                public void buttonPressed() {
                    //System.out.println(o + "selected");
                }
                public void buttonPressedSelected() {
                	placedElementsList.setVisible(false);
                	placementButtonPanel.setVisible(false);
                    showPopup(getElementEditScreenObject(element, elementsBuffer));
                    //elementsBuffer.remove(o);
                }
            };
        }
        
        if (placedElementsList == null) {
            placedElementsList = (ButtonCol) new ButtonCol() {
            	public AbstractButtonSet setSelected(int selected) {
            		editorCanvas.selectedElement = selected;
            		return super.setSelected(selected);
            	}
            };
        }
        
        placedElementsList
		        .enableScrolling(true, true)
		        .setIsSelectionEnabled(true)
		        .setIsSelectionVisible(true)
		        .setVisible(false)
		        .setBgColor(COLOR_TRANSPARENT);
        placedElementsList.setButtons(listButtons);
    }
    
    private void initPathPicker() {
        pathPicker = new PathPicker();
        pathPicker.setVisible(false);
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
        refreshFocusedComponents();
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
                .setPos(x0 + w, y0, TOP | RIGHT);
        placedElementsList
                .setSizes(w/3, bottomButtonPanel.getTopY() - y0 - BTN_H / 4, FONT_H * 3)
                .setPos(x0 + w, y0 + h - bottomButtonPanel.h, RIGHT | BOTTOM);
        pathPicker
                .setSizes(w, h, BTN_H)
                .setPos(x0, y0);
        startPointWarning
        		.setSize(startPointWarning.getOptimalW(zoomPanel.getLeftX() - x0), startPointWarning.getOptimalH(bottomButtonPanel.getTopY() - y0))
        		.setPos(bottomButtonPanel.getLeftX(), bottomButtonPanel.getTopY(), LEFT | BOTTOM);
        
        if (!postInitDone) {
        	onPostInit();
        }
    }
    
    private void onPostInit() {
    	checkAutoSaveStorage();
    	postInitDone = true;
    }
    
    public void paint(Graphics g, int x0, int y0, int w, int h, boolean forceInactive) {
    	startPointWarning.setVisible(!StartPoint.checkStartPoint(elementsBuffer.getElementsAsArray()));
    	super.paint(g, x0, y0, w, h, forceInactive);
    }
    
    public boolean keyPressed(int keyCode, int count) {
    	if (!RootContainer.displayKbHints && !isPopupShown()) {
    		RootContainer.displayKbHints = true;
	        
            initBottomPanel();
            
            initStartPointWarning();

            initZoomPanel();

            initSettingsButton();

            initPathPicker();
            
            setComponents();
    	}
    	repaint();
    	return super.keyPressed(keyCode, count);
    }
    
    public void moveToZeros() {
    	StartPoint.moveToZeros(elementsBuffer.getElementsAsArray());
    }
    
    class StartPointWarning extends Container {
    	TextComponent message;
    	ButtonComponent button;
    	
    	public StartPointWarning() {
    		setBgColor(COLOR_TRANSPARENT);
    		message = new TextComponent("Warn: start point of the structure should be on (x,y) 0 0");
    		message.setBgColor(COLOR_TRANSPARENT);
    		message.setFontColor(0xffff00);
    		Button button = new Button("Move to 0 0" + (RootContainer.displayKbHints ? " (7)" : "")) {
				public void buttonPressed() {
					moveToZeros();
				}
			}.setBgColor(0x002200);
    		this.button = new ButtonComponent(button) {
    			public boolean handleKeyPressed(int keyCode, int count) {
    	            switch (keyCode) {
    	                case Keys.KEY_NUM7:
    	                    buttons[0].invokePressed(false, false);
    	                    return true;
    	                default:
    	                    return false;
    	            }
    	        }
    		};
		}
    	
    	public void init() {
    		setComponents(new IUIComponent[] {message, this.button});
    	}
    	
		protected void onSetBounds(int x0, int y0, int w, int h) {
			button.setSize(w, ButtonComponent.H_AUTO).setPos(x0, y0 + h, LEFT | BOTTOM);
			message.setSize(w, h - button.getHeight()).setPos(x0, y0, LEFT | TOP);
		}
		
		public int getOptimalW(int freeSpace) {
			return Math.min(freeSpace, Font.defaultFontStringWidth(message.getText()) / 2);
		}
		
		public int getOptimalH(int freeSpace) {
			return Math.min(freeSpace, FONT_H * 10);
		}
    }
}
