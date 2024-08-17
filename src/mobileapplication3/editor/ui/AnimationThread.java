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
public class AnimationThread implements Runnable {
    public static final int FPS = 20;
    public static final int FRAME_MILLIS = 1000 / FPS;
    public static final int FP_MATH_MULTIPLIER = 1000;
    
    private Thread thread = null;
    private AnimationWorker feedback;
    private boolean isRunning = false;
    private int framesCount;
    private int x, y, targetX, targetY;
    private int vX = 0, vY = 0;
    private int aX, aY;
    
    public AnimationThread(AnimationWorker feedback) {
        this.feedback = feedback;
    }
    
    public void animate(int currX, int currY, int targetX, int targetY, int durationMillis) {
        if (currY == targetY && currX == targetX) {
            return;
        }
        framesCount = FPS * durationMillis / 1000;
        
        x = currX * FP_MATH_MULTIPLIER;
        y = currY * FP_MATH_MULTIPLIER;
        this.targetX = targetX * FP_MATH_MULTIPLIER;
        this.targetY = targetY * FP_MATH_MULTIPLIER;
        
        int sX = this.targetX - x;
        int sY = this.targetY - y;
        
        vX = 2*sX/(framesCount);
        vY = 2*sY/(framesCount);
        
        aX = 2*sX/(framesCount*framesCount);
        aY = 2*sY/(framesCount*framesCount);
        
        if (!isRunning) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        isRunning = true;
        for (int i = 0; i < framesCount; i++) {
            long iterationStartMillis = System.currentTimeMillis();
            
            vX -= aX;
            vY -= aY;
            x += vX;
            y += vY;
            
            feedback.onStep(x / FP_MATH_MULTIPLIER, y / FP_MATH_MULTIPLIER);
            
            try {
                Thread.sleep(Math.max(0, FRAME_MILLIS - (System.currentTimeMillis() - iterationStartMillis)));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        feedback.onStep(targetX / FP_MATH_MULTIPLIER, targetY / FP_MATH_MULTIPLIER);
        isRunning = false;
    }

    public interface AnimationWorker {
        public void onStep(int newX, int newY);
    }
}