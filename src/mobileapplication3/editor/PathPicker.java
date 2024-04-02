/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.utils.Utils;
import mobileapplication3.utils.FileUtils;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.ButtonCol;
import java.io.IOException;
import java.util.Calendar;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.IUIComponent;

/**
 *
 * @author vipaol
 */







// SCARY CODE! DO NOT LOOK, I WARNED YOU.
// (I'll fix it later)







public class PathPicker extends Container {
    
    public static final String QUESTION_REPLACE_WITH_PATH = ".picked_path.";
    private static final int TARGET_SAVE = 0, TARGET_OPEN = 1;
    
    private Button okBtn, cancelBtn;
    private ButtonCol list;
    private ButtonRow actionButtonPanel;
    private Button[] actionButtons;
    private Feedback feedback;
    private Thread questionAnim;
    
    private int currentTarget = TARGET_SAVE, btnH, questionOffset, questionStrW4;
    private String currentPath = null, pickedPath, fileName = "";
    private String title = "", questionTemplate = "", question = "";
    
    private boolean pointerDragged = false, questionAnimIsRunnung = false;
    private boolean isFMInited = false;
    
    public PathPicker() {
        initActionPanel();
        
        initList();
    }
    
    public PathPicker pickMGStructFolder(String question, Feedback onComplete) {
        questionTemplate = question;
        this.question = question;
        currentTarget = TARGET_SAVE;
        setVisible(true);
        feedback = onComplete;
        (new Thread(new Runnable() {
            public void run() {
                initFM();
            }
        })).start();
        return this;
    }
    
    public PathPicker pickFolder(String question, Feedback onComplete) {
        return pickFolder(null, question, onComplete);
    }
    
    public PathPicker pickFolder(String initialPath, String question, Feedback onComplete) {
        questionTemplate = question;
        this.question = "";
        currentTarget = TARGET_SAVE;
        this.currentPath = initialPath;
        setVisible(true);
        feedback = onComplete;
        (new Thread(new Runnable() {
            public void run() {
                initFM();
            }
        })).start();
        return this;
    }
    
    public PathPicker pickFile(String question, Feedback onComplete) {
        return pickFile(null, question, onComplete);
    }
    
    public PathPicker pickFile(String initialPath, String question, Feedback onComplete) {
        questionTemplate = question;
        this.question = "";
        currentTarget = TARGET_OPEN;
        this.currentPath = initialPath;
        setVisible(true);
        feedback = onComplete;
        (new Thread(new Runnable() {
            public void run() {
                initFM();
            }
        })).start();
        return this;
    }
    
    private void initFM() {
        isFMInited = true;
        if (currentTarget == TARGET_SAVE) {
            title = "Choose a folder";
        } else if (currentTarget == TARGET_OPEN) {
            title = "Choose a file";
        }
        
        if (currentTarget == TARGET_SAVE) {
            Calendar calendar = Calendar.getInstance();
            fileName = calendar.get(Calendar.YEAR)
                    // it counts months from 0 while everything else from 1.
                    + "-" + (calendar.get(Calendar.MONTH) + 1) // why? who knows...
                    + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                    + "_" + calendar.get(Calendar.HOUR_OF_DAY)
                    + "-" + calendar.get(Calendar.MINUTE)
                    + "-" + calendar.get(Calendar.SECOND)
                    + ".mgstruct";
            System.out.println(fileName);
        }
        
        (new Thread(new Runnable() {
            public void run() {
                if (currentPath == null) {
                    currentPath = FileUtils.PREFIX;
                    String[] roots = FileUtils.getRoots();
                    initList(roots);
                } else {
                    if (currentTarget == TARGET_SAVE) {
                        pickPath(currentPath + fileName);
                    }
                    getNewList();
                }
            }
        })).start();
    }
    
    private void getNewList() {
        title = currentPath;
        
        (new Thread(new Runnable() {
            public void run() {
                try {
                    initList(FileUtils.list(currentPath));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        })).start();
    }
    
    private void pickPath(String path) {
        pickedPath = path;
        if (!fileName.equals("")) {
            question = Utils.replace(questionTemplate, QUESTION_REPLACE_WITH_PATH, pickedPath);
        } else {
            question = "";
        }
        
        questionStrW4 = Font.getDefaultFont().stringWidth(question) / 4;
        questionOffset = questionStrW4;
        if (!questionAnimIsRunnung) {
            questionAnim = new Thread(new Runnable() {
                public void run() {
                    questionAnimIsRunnung = true;
                    boolean reverse = false;
                    while (isVisible) {
                        try {
                            long start = System.currentTimeMillis();
                            if (!reverse) {
                                if (questionOffset < questionStrW4) {
                                    questionOffset += questionStrW4 / 64;
                                } else {
                                    repaint();
                                    Thread.sleep(500);
                                    reverse = true;
                                }
                            } else {
                                if (questionOffset > -questionStrW4) {
                                    questionOffset -= questionStrW4 / 8;
                                } else {
                                    repaint();
                                    Thread.sleep(500);
                                    reverse = false;
                                }
                            }
                            repaint();
                            Thread.sleep(Math.max(0, 20 - (System.currentTimeMillis() - start)));
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    questionAnimIsRunnung = false;
                }
            });
            questionAnim.start();
        }
    }
    
    private void initList(String[] paths) {
        Button[] listButtons = new Button[paths.length];
        for (int i = 0; i < paths.length; i++) {
            final String name = paths[i];
            listButtons[i] = new Button(name, new Button.ButtonFeedback() {
                public void buttonPressed() {
                    // folder or file
                    if (name.endsWith(String.valueOf(FileUtils.SEP))) {
                        if (currentTarget == TARGET_OPEN) {
                            fileName = "";
                        }
                        currentPath = currentPath + name;
                        pickPath(currentPath + fileName);
                        getNewList();
                    } else {
                        if (currentTarget == TARGET_OPEN) {
                            fileName = name;
                        }
                        
                        pickPath(currentPath + name);
                    }
                }
            });
        }
        initList(listButtons);
    }
    
    private void initList(Button[] buttons) {
//        list = (ButtonCol) new ButtonCol(x0, y0 + h - btnH*3/2, w, h - btnH*3/2 - btnH*3/2, buttons, ButtonCol.LEFT | ButtonCol.BOTTOM, btnH)
//                .enableScrolling(true, false)
//                .setIsSelectionEnabled(true)
//                .setIsSelectionVisible(!Main.hasPointerEvents)
//                .setButtonsBgPadding(3)
//                .setButtonsBgColor(0x555555);
        
        list.setButtons(buttons).setParent(this);
        setSize(w, h);
        repaint();
    }
    
    private void initList() {
        if (list == null) {
            list = (ButtonCol) new ButtonCol()
                    .enableScrolling(true, true)
                    .setIsSelectionEnabled(true)
                    .setIsSelectionVisible(!Main.hasPointerEvents)
                    .setButtonsBgPadding(3)
                    .setButtonsBgColor(0x555555);
        }
    }
    
    public void paint(Graphics g) {
        if (!isVisible) {
            return;
        }
        
        if (EditorScreenUI.isCapturing) { // fix hardcode to EditorScreenUI
            drawBG(g);
            return;
        }
        super.paint(g);
        drawCapturedBG(g);
        if (list != null) {
            list.onPaint(g);
        }
        actionButtonPanel.onPaint(g);
        g.setColor(0xffffff);
        g.drawString(question, x0 + w/2 - questionOffset, y0 + h - btnH*5/4 - g.getFont().getHeight()/2, Graphics.HCENTER | Graphics.TOP);
        g.drawString(title, x0 + w/2, y0 + g.getFont().getHeight(), Graphics.HCENTER | Graphics.TOP);
    }
    
    void drawCapturedBG(Graphics g) {
        if (EditorScreenUI.capture != null) {
            g.drawImage(EditorScreenUI.capture, 0, 0, Graphics.TOP | Graphics.LEFT);
        }
    }
    
    void drawBG(Graphics g) {
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        int a = 3;
        for (int i = 0; i < (w + h) / a; i++) {
            g.setColor(0x220011);
            g.drawLine(x1 + x0, y1 + y0, x2 + x0, y2 + y0);
            g.drawLine(x1 + x0, h - (y1 + y0), x2 + x0, h - (y2 + y0));
            
            if (y1 < h) {
                y1 += a;
            } else {
                x1 += a;
            }
            
            if (x2 < w) {
                x2 += a;
            } else {
                y2 += a;
            }
        }
    }

    public IUIComponent setPos(int x0, int y0) {
        return super.setPos(x0, y0);
    }

    public IUIComponent setSize(int w, int h) {
        return super.setSize(w, h);
    }
    
    public IUIComponent setSizes(int w, int h, int btnH) {
        this.btnH = btnH;
        return super.setSize(w, h);
    }

    public boolean pointerReleased(int x, int y) {
        if (pointerDragged) {
            pointerDragged = false;
            return false;
        }
        
        if (!isVisible) {
            return false;
        }
        
        if (!checkTouchEvent(x, y)) {
            return false;
        }
        
        if (actionButtonPanel.handlePointerReleased(x, y)) {
            return true;
        }
        
        if (list.handlePointerReleased(x, y)) {
            return true;
        }
        
        return false;
    }
    
    public boolean pointerDragged(int x, int y) {
        pointerDragged = true;
        if (!isVisible) {
            return false;
        }
        return list.handlePointerDragged(x, y);
    }
    
    public boolean pointerPressed(int x, int y) {
        if (!isVisible) {
            return false;
        }
        return list.handlePointerPressed(x, y);
    }

    public boolean keyPressed(int keyCode, int count) {
        if (!isVisible) {
            return false;
        }
        
        switch (keyCode) {
            case -6:
                okBtn.invokePressed(false, false);
                break;
            case -7:
                cancelBtn.invokePressed(false, false);
                break;
            default:
                return list.handleKeyPressed(keyCode, count);
        }
        
        return true;
    }

    protected void onSetBounds(int x0, int y0, int w, int h) {
        actionButtonPanel
                .setSize(w, btnH)
                .setPos(x0, y0 + h, ButtonRow.BOTTOM | ButtonRow.LEFT);
        list
                .setSizes(w, h - btnH*3/2 - btnH*3/2, btnH, false)
                .setPos(x0, y0 + h - btnH*3/2, ButtonCol.LEFT | ButtonCol.BOTTOM);
    }
    
    private void initActionPanel() {
        okBtn = new Button("OK", new Button.ButtonFeedback() {
            public void buttonPressed() {
                if (currentTarget == TARGET_OPEN && pickedPath.endsWith(String.valueOf(FileUtils.SEP))) {
                    return;
                }
                feedback.onComplete(pickedPath);
            }
        });
        
        cancelBtn = new Button("Cancel", new Button.ButtonFeedback() {
            public void buttonPressed() {
                setVisible(false);
                feedback.onCancel();
            }
        });
        
        
        actionButtons = new Button[]{okBtn, cancelBtn};
        actionButtonPanel = (ButtonRow) new ButtonRow(actionButtons).setButtonsBgColor(-1);
    }
    
    public interface Feedback {
        void onComplete(final String path);
        void onCancel();
        void needRepaint();
    }
    
}
