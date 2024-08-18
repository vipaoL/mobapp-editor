/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication3.editor.setup;

import mobileapplication3.editor.ui.Button;
import mobileapplication3.editor.ui.Container;
import mobileapplication3.editor.ui.IUIComponent;
import mobileapplication3.utils.EditorSettings;

/**
 *
 * @author vipaol
 */
public class SetupWizard extends Container {
    
    private int currentPageI = 0;
    private FinishSetup finishSetup;
    private Feedback pageSwitcher = new Feedback() {
        public void nextPage() {
            System.out.println(currentPageI + " - current. switching to next page");
            setCurrentPage(currentPageI + 1);
        }

        public void prevPage() {
            System.out.println(currentPageI + " - current. switching to prev page");
            setCurrentPage(currentPageI - 1);
        }
        
        public void needRepaint() {
            repaint();
        }
    };
    
    private AbstractSetupWizardPage[] pages;
    
    public SetupWizard(FinishSetup finishSetup) {
        this.finishSetup = finishSetup;
        
        pages = new AbstractSetupWizardPage[]{
            new Page1(new Button[]{getNewNextButton()}, pageSwitcher),
            new Page2(new Button[]{getNewPrevButton(), getNewNextButton()}, pageSwitcher),
            new Page3(new Button[]{getNewPrevButton(), getNewNextButton()}, pageSwitcher),
            new Page4(new Button[]{getNewPrevButton(), getNewNextButton()}, pageSwitcher)
        };
    }
    
    public void init() {
    	for (int i = 0; i < pages.length; i++) {
        	pages[i].setParent(this);
		}
    	setCurrentPage(currentPageI);
    }
    
    private Button getNewNextButton() {
        return new Button("Next") {
            public void buttonPressed() {
                pageSwitcher.nextPage();
            }
        };
    }
    
    private Button getNewPrevButton() {
        return new Button("Back") {
            public void buttonPressed() {
                pageSwitcher.prevPage();
            }
        };
    }
    
    private void setCurrentPage(int i) {
        System.out.println("setting page i=" + i);
        pages[currentPageI].setParent(this);
        pages[currentPageI].setVisible(false);
        if (i < 0) {
            return;
        } else if (i >= pages.length) {
            finishSetup();
            return;
        }
        
        currentPageI = i;
        pages[currentPageI].onShow();
        pages[currentPageI].setVisible(true);
        setComponents(new IUIComponent[]{pages[currentPageI]});
        if (w != 0 && h != 0) {
            setSize(w, h);
        }
    }
    
    private void finishSetup() {
        pages = null;
        finishSetup.onFinish();
        EditorSettings.setIsSetupWizardCompleted(true);
    }

    public void onSetBounds(int x0, int y0, int w, int h) {
        pages[currentPageI].setSize(w, h);
    }
    
    public interface Feedback {
        public void nextPage();
        public void prevPage();
        public void needRepaint();
    }

//    protected IUIComponent[] getComponents() {
//        return new IUIComponent[]{currentPage};
//    }
    
    public interface FinishSetup {
        void onFinish();
    }
}
