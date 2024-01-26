/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.editor.ui.UIComponent;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonPanelHorizontal;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.ButtonCol;
import mobileapplication3.editor.ui.WindowManager;
import javax.microedition.lcdui.Alert;
import mobileapplication3.elements.Element;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 *
 * @author vipaol
 */
public class UI extends WindowManager {
    
    private final static int BTNS_IN_ROW = 4;
    public final static int FONT_H = Font.getDefaultFont().getHeight();
    public final static int BTN_H = FONT_H*2;
    private Button btnLoad, btnSave, btnPlace, btnList, zoomIn, zoomOut;;
    private EditorCanvas editorCanvas;
    private ButtonRow bottomButtonPanel, zoomPanel;
    private ButtonPanelHorizontal placementButtonPanel;
    private ButtonCol placedElementsList;
    private PathPicker pathPicker;
    private StructureBuilder elementsBuffer;
    public static Image capture = null; //TODO
    public static boolean isCapturing = false;
    
    public UI() {
        try {
            setFullScreenMode(true);
            w = getWidth();
            h = getHeight();
            elementsBuffer = new StructureBuilder(new StructureBuilder.Feedback() {
                public void onUpdate() {
                    initListPanel();
                }
            });

            initBottomPanel();

            initZoomPanel();

            initEditorCanvas();

            initPlacementPanel();

            initListPanel();

            initPathPicker();

            updateUI();
        } catch(Exception ex) {
            Main.setCurrent(new Alert(ex.toString()));
            ex.printStackTrace();
        }
    }
    
    protected void showNotify() {
        setFullScreenMode(true);
    }

    private void updateUI() {
        updateUI(new UIComponent[]{editorCanvas, bottomButtonPanel, zoomPanel, placementButtonPanel, placedElementsList, pathPicker});
    }
    
    long prevPaintTime = System.currentTimeMillis();
    public void paint(Graphics g) {
        //int frameTime = (int) (System.currentTimeMillis() - prevPaintTime);
        //prevPaintTime = System.currentTimeMillis();
        super.paint(g);
        if (isCapturing) {
            return;
        }
        g.setColor(0xffffff);
        //g.drawString("Frame time: " + frameTime, 0, 0, Graphics.TOP | Graphics.LEFT);
    }
    
    private void initEditorCanvas() {
        editorCanvas = new EditorCanvas(0, 0, w, h - bottomButtonPanel.h, elementsBuffer);
    }

    
    private void initBottomPanel() {
        btnPlace = new Button("Place", new Button.ButtonFeedback() {
            public void buttonPressed() {
                placementButtonPanel.toggleIsVisible();
            }
        });
        
        btnLoad = new Button("Open" + (Main.hasPointerEvents ? "" : " (8)"), new Button.ButtonFeedback() {
            public void buttonPressed() {
                pathPicker.pickFile("Open " + PathPicker.QUESTION_TEMPLATE_FILE_PATH + " ?", new PathPicker.Feedback() {
                    public void onComplete(final String path) {
                        (new Thread(new Runnable() {
                            public void run() {
                                System.out.println("Open: " + path);
                                elementsBuffer.loadFile(path);
                                System.out.println("Loaded!");
                                pathPicker.setVisible(false);
                                editorCanvas.setVisible(true);
                                bottomButtonPanel.setVisible(true);
                                zoomPanel.setVisible(true);
                                repaint();
                            }
                        })).start();
                    }

                    public void onCancel() {
                        pathPicker.setVisible(false);
                        editorCanvas.setVisible(true);
                        bottomButtonPanel.setVisible(true);
                        zoomPanel.setVisible(true);
                    }

                    public void needRepaint() {
                        repaint();
                    }
                });
                isCapturing = true;
                capture = Image.createImage(w, h);
                paint(capture.getGraphics());
                isCapturing = false;
                editorCanvas.setVisible(false);
                bottomButtonPanel.setVisible(false);
                zoomPanel.setVisible(false);
            }
        });
        
        btnSave = new Button("Save" + (Main.hasPointerEvents ? "" : " (9)"), new Button.ButtonFeedback() {
            public void buttonPressed() {
                pathPicker.pickFolder("Save as " + PathPicker.QUESTION_TEMPLATE_FILE_PATH + " ?", new PathPicker.Feedback() {
                    public void onComplete(final String path) {
                        (new Thread(new Runnable() {
                            public void run() {
                                elementsBuffer.saveToFile(path);
                                System.out.println("Save: " + path);
                                pathPicker.setVisible(false);
                                editorCanvas.setVisible(true);
                                bottomButtonPanel.setVisible(true);
                                zoomPanel.setVisible(true);
                                repaint();
                            }
                        })).start();
                    }

                    public void needRepaint() {
                        repaint();
                    }

                    public void onCancel() {
                        pathPicker.setVisible(false);
                        editorCanvas.setVisible(true);
                        bottomButtonPanel.setVisible(true);
                        zoomPanel.setVisible(true);
                    }
                });
                isCapturing = true;
                capture = Image.createImage(w, h);
                paint(capture.getGraphics());
                isCapturing = false;
                editorCanvas.setVisible(false);
                bottomButtonPanel.setVisible(false);
                zoomPanel.setVisible(false);
            }
        });
        
        btnList = new Button("List", new Button.ButtonFeedback() {
            public void buttonPressed() {
                placedElementsList.toggleIsVisible();
            }
        });
        
        Button[] bottomButtons = {btnPlace, btnLoad, btnSave, btnList};
        bottomButtonPanel = new ButtonRow(0, h, w, BTN_H, bottomButtons, ButtonRow.BOTTOM){
            public boolean handleKeyPressed(int keyCode) {
                switch (keyCode) {
                    case -6:
                        btnPlace.invokePressed(false);
                        break;
                    case -7:
                        btnList.invokePressed(false);
                        break;
                    case Canvas.KEY_NUM8:
                        btnLoad.invokePressed(false);
                        break;
                    case Canvas.KEY_NUM9:
                        btnSave.invokePressed(false);
                        break;
                    case Canvas.KEY_NUM0:
                        // new file
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }.setBgColor(0x303030);
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
        zoomPanel = new ButtonRow(w/2, h - bottomButtonPanel.h, w/2, BTN_H, zoomPanelButtons, ButtonRow.BOTTOM){
            public boolean handleKeyPressed(int keyCode) {
                switch (keyCode) {
                    case Canvas.KEY_STAR:
                        zoomIn.invokePressed(false);
                        break;
                    case Canvas.KEY_POUND:
                        zoomOut.invokePressed(false);
                        break;
                    default:
                        return false;
                }
                return true;
            }
            
        }.setBgColor(0x000020);
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
        placementButtonPanel = (ButtonPanelHorizontal) new ButtonPanelHorizontal(0, h - bottomButtonPanel.h, w, placementButtons, ButtonPanelHorizontal.BOTTOM, BTN_H, BTNS_IN_ROW).setVisible(false);
    }

    private void initListPanel() {
        Element[] elements = elementsBuffer.getElementsAsArray();
        System.out.println("updating, " + elements.length);
        Button[] listButtons = new Button[elements.length];
        for (int i = 0; i < elements.length; i++) {
            final int o = i;
            listButtons[i] = new Button(elements[i].getName(), new Button.ButtonFeedback() {
                public void buttonPressed() {
                    //System.out.println(o + "pressed");
                }
                public void buttonPressedSelected() {
                    // there should be edit, but there's deleting for test
                    System.out.println("removing");
                    elementsBuffer.remove(o);
                }
            });
        }
        placedElementsList = ((ButtonCol) new ButtonCol(w, h - bottomButtonPanel.h, w/5, h-bottomButtonPanel.h, listButtons, ButtonCol.RIGHT | ButtonCol.BOTTOM, FONT_H * 3)
                .setVisible(false))
                .setIsSelectionEnabled(true)
                .enableScrolling(true, true);
        updateUI();
    }
    
    private void initPathPicker() {
        pathPicker = (PathPicker) new PathPicker(w - w/20, w, h, BTN_H).setVisible(false);
    }
}
