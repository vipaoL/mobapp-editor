/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.editor.ui.UIComponent;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.ButtonCol;
import java.io.IOException;
import java.util.Calendar;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public class PathPicker extends UIComponent {
    
    private static final int TARGET_FOLDER = 0;
    private static final int TARGET_FILE = 1;
    public static final String QUESTION_TEMPLATE_FILE_PATH = ".filePath.";
    
    Button okBtn, cancelBtn;
    ButtonCol list;
    ButtonRow actionButtonPanel;
    Button[] actionButtons;
    Feedback feedback;
    int btnH;
    String currentPath;
    String pickedPath;
    String fileName = "";
    private int currentTarget = TARGET_FOLDER;
    String title = "";
    String questionTemplate = "";
    String question = "";
    int questionOffset = 0;
    int questionStrW4;
    
    boolean dragged = false;
    
    Thread questionAnim;
    boolean questionAnimIsRunnung = false;

    public PathPicker(int w, int screenW, int screenH, int btnH) {
        this.w = w;
        this.h = screenH;
        this.btnH = btnH;
        x0 = (screenW - w) / 2;
        y0 = 0;
        list = (ButtonCol) new ButtonCol(x0, h, 1, 1, new Button[0], ButtonCol.LEFT | ButtonCol.BOTTOM, -1);
        
        initActionPanel();
    }
    
    public void pickMGStructFolder(Feedback onComplete) {
        
    }
    
    public void pickFolder(String question, Feedback onComplete) {
        questionTemplate = question;
        this.question = "";
        currentTarget = TARGET_FOLDER;
        setVisible(true);
        feedback = onComplete;
        (new Thread(new Runnable() {
            public void run() {
                initFM();
            }
        })).start();
    }
    
    public void pickFile(String question, Feedback onComplete) {
        questionTemplate = question;
        this.question = "";
        currentTarget = TARGET_FILE;
        setVisible(true);
        feedback = onComplete;
        (new Thread(new Runnable() {
            public void run() {
                initFM();
            }
        })).start();
    }
    
    private void initFM() {
        if (currentTarget == TARGET_FOLDER) {
            title = "Choose a folder";
        } else if (currentTarget == TARGET_FILE) {
            title = "Choose a file";
        }
        
        currentPath = FileUtils.prefix;
        if (currentTarget == TARGET_FOLDER) {
            Calendar calendar = Calendar.getInstance();
            fileName = calendar.get(Calendar.YEAR)
                    + "-" + calendar.get(Calendar.MONTH)
                    + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                    + "_" + calendar.get(Calendar.HOUR_OF_DAY)
                    + "-" + calendar.get(Calendar.MINUTE)
                    + "-" + calendar.get(Calendar.SECOND)
                    + ".mgstruct";
        }
        (new Thread(new Runnable() {
            public void run() {
                String[] roots = FileUtils.getRoots();
                setButtons(roots);
            }
        })).start();
    }
    
    private void getNewList() {
        title = currentPath;
        
        (new Thread(new Runnable() {
            public void run() {
                try {
                    setButtons(FileUtils.list(currentPath));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        })).start();
    }
    
    private void setButtons(String[] buttons) {
        Button[] listButtons = new Button[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            final String name = buttons[i];
            listButtons[i] = new Button(name, new Button.ButtonFeedback() {
                public void buttonPressed() {
                    // folder or file
                    if (name.endsWith(FileUtils.sep)) {
                        if (currentTarget == TARGET_FILE) {
                            fileName = "";
                        }
                        currentPath = currentPath + name;
                        pickedPath = currentPath + fileName;
                        getNewList();
                    } else {
                        if (currentTarget == TARGET_FILE) {
                            fileName = name;
                        }
                        
                        pickedPath = currentPath + name;
                    }
                    
                    if (!fileName.equals("")) {
                        question = Utils.replace(questionTemplate, QUESTION_TEMPLATE_FILE_PATH, pickedPath);
                    } else {
                        question = "";
                    }
                    if (!questionAnimIsRunnung) {
                        questionAnim = new Thread(new Runnable() {
                            public void run() {
                                questionAnimIsRunnung = true;
                                while (isVisible) {
                                    long start = System.currentTimeMillis();
                                    if (questionOffset < questionStrW4) {
                                        questionOffset += questionStrW4 / 64;
                                    } else {
                                        questionOffset = -questionStrW4;
                                    }
                                    feedback.needRepaint();
                                    try {
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
            });
        }
        list = new ButtonCol(x0, h - btnH*3/2, w, h - btnH*3/2 - btnH*3/2, listButtons, ButtonCol.LEFT | ButtonCol.BOTTOM, btnH)
                .setIsSelectionEnabled(!Main.hasPointerEvents)
                .setButtonsBg(0x555555).setBg(-1)
                .setButtonsBgPadding(3)
                .enableScrolling(true, false);
        feedback.needRepaint();
    }
    
    public void paint(Graphics g) {
        if (UI.isCapturing) {
            drawBG(g);
            return;
        }
        drawCapturedBG(g);
        list.paint(g);
        actionButtonPanel.paint(g);
        questionStrW4 = g.getFont().stringWidth(question) / 4;
        g.setColor(0xffffff);
        g.drawString(question, x0 + w/2 - questionOffset, y0 + h - btnH*5/4 - g.getFont().getHeight()/2, Graphics.HCENTER | Graphics.TOP);
        g.drawString(title, x0 + w/2, y0 + g.getFont().getHeight(), Graphics.HCENTER | Graphics.TOP);
    }
    
    void drawCapturedBG(Graphics g) {
        g.drawImage(UI.capture, 0, 0, Graphics.TOP | Graphics.LEFT);
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

    public boolean handlePointerReleased(int x, int y) {
        if (dragged) {
            dragged = false;
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
    
    public boolean handlePointerDragged(int x, int y) {
        dragged = true;
        if (!isVisible) {
            return false;
        }
        return list.handlePointerDragged(x, y);
    }
    
    public boolean handlePointerPressed(int x, int y) {
        if (!isVisible) {
            return false;
        }
        return list.handlePointerPressed(x, y);
    }

    public boolean handleKeyRepeated(int keyCode) {
        return handleKeyPressed(keyCode);
    }

    public boolean handleKeyPressed(int keyCode) {
        if (!isVisible) {
            return false;
        }
        
        switch (keyCode) {
            case -6:
                okBtn.invokePressed(false);
                break;
            case -7:
                cancelBtn.invokePressed(false);
                break;
            default:
                return list.handleKeyPressed(keyCode);
        }
        
        return true;
    }
    
    public interface Feedback {
        void onComplete(final String path);
        void onCancel();
        void needRepaint();
    }
    
    private void initActionPanel() {
        okBtn = new Button("OK", new Button.ButtonFeedback() {
            public void buttonPressed() {
                if (currentTarget == TARGET_FILE && pickedPath.endsWith(FileUtils.sep)) {
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
        
        actionButtonPanel = (ButtonRow) new ButtonRow(x0, y0 + h, w, btnH, actionButtons, ButtonRow.BOTTOM).setBgColor(-1);
    }
    
}
