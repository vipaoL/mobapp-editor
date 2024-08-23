/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor;

import mobileapplication3.utils.Utils;
import mobileapplication3.editor.platform.FileUtils;
import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.ButtonRow;
import mobileapplication3.editor.ui.ButtonCol;
import java.io.IOException;
import java.util.Calendar;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.editor.ui.TextComponent;
import mobileapplication3.editor.ui.platform.Font;
import mobileapplication3.editor.ui.platform.Graphics;
import mobileapplication3.editor.ui.platform.Image;
import mobileapplication3.editor.ui.platform.RootContainer;

/**
 *
 * @author vipaol
 */


// TODO: move list to FileManager


public class PathPicker extends Container {
    
    public static final String QUESTION_REPLACE_WITH_PATH = ".curr_folder.";
    private static final int TARGET_SAVE = 0, TARGET_OPEN = 1;
    
    private Button okBtn, cancelBtn;
    private TextComponent title = null;
    private TextComponent question = null;
    private ButtonCol list;
    private ButtonRow actionButtonPanel;
    private Button[] actionButtons;
    private Feedback feedback;
    
    private int currentTarget = TARGET_SAVE, btnH;
    private String currentFolder = null, pickedPath, fileName = "";
    private String questionTemplate = "";
    
    public PathPicker pickFolder(String question, Feedback onComplete) {
        return pickFolder(null, question, onComplete);
    }
    
    public PathPicker pickFolder(String initialPath, String question, Feedback onComplete) {
        questionTemplate = question;
        currentTarget = TARGET_SAVE;
        currentFolder = initialPath;
        feedback = onComplete;
        initFM();
        return this;
    }
    
    public PathPicker pickFile(String question, Feedback onComplete) {
        return pickFile(null, question, onComplete);
    }
    
    public PathPicker pickFile(String initialPath, String question, Feedback onComplete) {
        questionTemplate = question;
        currentTarget = TARGET_OPEN;
        this.currentFolder = initialPath;
        feedback = onComplete;
        initFM();
        return this;
    }
    
    private void initFM() {
        question.setText("");
        if (currentTarget == TARGET_SAVE) {
            title.setText("Choose a folder");
        } else if (currentTarget == TARGET_OPEN) {
            title.setText("Choose a file");
        }
        
        if (currentTarget == TARGET_SAVE) {
            fileName = getTodaysFileName();
            System.out.println(fileName);
        }
        
        (new Thread(new Runnable() {
            public void run() {
                if (currentFolder == null) {
                    currentFolder = FileUtils.PREFIX;
                    String[] roots = FileUtils.getRoots();
                    setPaths(roots);
                } else {
                    if (currentTarget == TARGET_SAVE) {
                        pickPath(currentFolder + fileName);
                    }
                    getNewList();
                }
            }
        })).start();
    }
    
    private void getNewList() {
        title.setText(currentFolder);
        
        (new Thread(new Runnable() {
            public void run() {
                try {
                    setPaths(FileUtils.list(currentFolder));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        })).start();
    }
    
    private void pickPath(String path) {
        pickedPath = path;
        if (!fileName.equals("")) {
            question.setText(Utils.replace(questionTemplate, QUESTION_REPLACE_WITH_PATH, pickedPath));
        } else {
            question.setText("");
        }
    }
    
    private void setPaths(String[] paths) {
        Button[] listButtons = new Button[paths.length];
        for (int i = 0; i < paths.length; i++) {
            final String name = paths[i];
            listButtons[i] = new Button(name) {
                public void buttonPressed() {
                    // folder or file
                    if (name.endsWith(String.valueOf(FileUtils.SEP))) {
                        if (currentTarget == TARGET_OPEN) {
                            fileName = "";
                        }
                        currentFolder = currentFolder + name;
                        pickPath(currentFolder + fileName);
                        getNewList();
                    } else {
                        if (currentTarget == TARGET_OPEN) {
                            fileName = name;
                        }
                        
                        pickPath(currentFolder + name);
                    }
                }
            };
        }
        setListButtons(listButtons);
    }
    
    private void setListButtons(Button[] buttons) {
        list.setButtons(buttons);
        setSize(w, h);
        refreshFocusedComponents();
        repaint();
    }
    
    private void initUI() {
        setBgColor(COLOR_TRANSPARENT);
        title = new TextComponent();
        title.setBgColor(COLOR_TRANSPARENT);
        
        if (list == null) {
            list = (ButtonCol) new ButtonCol()
                    .enableScrolling(true, true)
                    .setIsSelectionEnabled(true)
                    .setIsSelectionVisible(RootContainer.displayKbHints)
                    .setButtonsBgPadding(3)
                    .setButtonsBgColor(0x555555);
        }
        
        okBtn = new Button("OK") {
            public void buttonPressed() {
                if (currentTarget == TARGET_OPEN && pickedPath.endsWith(String.valueOf(FileUtils.SEP))) {
                    return;
                }
                feedback.onComplete(pickedPath);
            }
        };
        
        cancelBtn = new Button("Cancel") {
            public void buttonPressed() {
                setVisible(false);
                feedback.onCancel();
            }
        };
        
        
        actionButtons = new Button[]{okBtn, cancelBtn};
        actionButtonPanel = (ButtonRow) new ButtonRow(actionButtons) {
        	public boolean canBeFocused() {
        		return false;
        	}
        }
        		.setIsSelectionEnabled(false);
                //.setButtonsBgColor(COLOR_TRANSPARENT);
        actionButtonPanel.bindToSoftButtons(0, actionButtonPanel.getButtonCount() - 1);
        
        question = (TextComponent) new TextComponent()
                .enableHorizontalScrolling(true)
                .setBgColor(COLOR_TRANSPARENT);
    }
    
    public boolean canBeFocused() {
    	return isVisible;
    }
    
    public void init() {
    	initUI();
    	setComponents(new IUIComponent[]{list, question, title, actionButtonPanel});
    }
    
    public Container setBgImage(Image bg) {
        blurImg(bg);
        return super.setBgImage(bg);
    }
    
    public PathPicker setSizes(int w, int h, int btnH) {
        this.btnH = btnH;
        super.setSize(w, h);
        return this;
    }

    protected void onSetBounds(int x0, int y0, int w, int h) {
        title
                .setSize(w, TextComponent.HEIGHT_AUTO)
                .setPos(x0 + w/2, y0 + Font.getDefaultFontHeight(), Graphics.HCENTER | Graphics.TOP);
        actionButtonPanel
                .setSize(w, btnH)
                .setPos(x0, y0 + h, ButtonRow.BOTTOM | ButtonRow.LEFT);
        list
                .setSizes(w, h - btnH*3/2 - btnH*3/2, btnH, false)
                .setPos(x0, y0 + h - btnH*3/2, ButtonCol.LEFT | ButtonCol.BOTTOM);
        question
                .setSize(w, Font.getDefaultFontHeight())
                .setPos(x0 + w/2, y0 + h - btnH*5/4, TextComponent.HCENTER | TextComponent.VCENTER);
    }

    private String getTodaysFileName() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR)
                // it counts months from 0 while everything else from 1.
                + "-" + (calendar.get(Calendar.MONTH) + 1) // why? who knows...
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + "_" + calendar.get(Calendar.HOUR_OF_DAY)
                + "-" + calendar.get(Calendar.MINUTE)
                + "-" + calendar.get(Calendar.SECOND)
                + ".mgstruct";
    }
    
    public interface Feedback {
        void onComplete(final String path);
        void onCancel();
        void needRepaint();
    }
    
}
