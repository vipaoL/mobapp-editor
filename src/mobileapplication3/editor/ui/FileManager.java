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
public class FileManager extends Container {
    /*private String currentFolder = null;
    
    public void openFolder() {
        (new Thread(new Runnable() {
            public void run() {
                currentFolder = FileUtils.PREFIX;
                String[] roots = FileUtils.getRoots();
                setPaths(roots);
            }
        })).start();
    }
    
    public void saveAs(String fileName) {
        
    }
    
    public void saveAs(String initialPath, String fileName) {
        
    }
    
    public void openFile(final String initialPath) {
        (new Thread(new Runnable() {
            public void run() {
                currentFolder = initialPath;
                if (currentTarget == TARGET_SAVE) {
                    pickPath(currentFolder + fileName);
                }
                getNewList();
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
            listButtons[i] = new Button(name, new Button.ButtonFeedback() {
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
            });
        }
        setListButtons(listButtons);
    }
    
    private void setListButtons(Button[] buttons) {
        list.setButtons(buttons).setParent(this);
        setSize(w, h);
        repaint();
    }*/

    protected void onSetBounds(int x0, int y0, int w, int h) {
    }
}
