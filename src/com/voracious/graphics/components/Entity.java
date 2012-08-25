package com.voracious.graphics.components;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.voracious.graphics.Game;

/**
 * Similar to Sprite, Entity allows an image to be stored and drawn to a Screen.
 * Entities differ from Sprites in that they keep track of their own positions, are capable of animation, 
 * and can do hit tests.
 * 
 * @author Andrew Kallmeyer
 */
public class Entity {

    public static final int FPS = 10;
    private int width, height;
    private double x, y = 0;
    private int currentFrame = 0;
    private int currentAnimation = 0;
    private int currentFps = FPS;
    private int tickCount = 0;
    private boolean play = false;
    private Sprite[][] frames;
    private boolean facingLeft = false;
    
    /**
     * Initializes an Entity from an image file
     * 
     * Image file should be formatted in such a way that its width and height are multiples of the frame width
     * and height respectively. Frames should be arranged starting at the top-left and wrapping line by line to the
     * bottom-right with no spacing in-between individual frames.
     * 
     * @param width Frame width
     * @param height Frame height
     * @param numFrames number of frames in each animation.
     * @param image image file location
     */
    public Entity(int width, int height, int numFrames[], String image) {
        this.width = width;
        this.height = height;

        int largestNumFrames = 0;
        for (int i = 0; i < numFrames.length; i++) {
            if (numFrames[i] > largestNumFrames) {
                largestNumFrames = numFrames[i];
            }
        }

        try {
            BufferedImage file = ImageIO.read(Entity.class.getResourceAsStream(image));
            frames = new Sprite[numFrames.length][largestNumFrames];
            int framesProcessed = 0;
            for (int j = 0; j < frames.length; j++) {
                for (int i = 0; i < numFrames[j]; i++) {
                    BufferedImage frame = file.getSubimage(((framesProcessed) * width) % file.getWidth(), height * (((framesProcessed) * width) / file.getWidth()), width, height);
                    frames[j][i] = new Sprite(width, height, frame);
                    framesProcessed++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @see Sprite
     * @param screen
     */
    public void draw(Screen screen) {
        draw(screen, facingLeft, false, false);
    }
    
    /**
     * @see Sprite
     * 
     * @param screen
     * @param flipX
     * @param flipY
     * @param rotate90
     */
    public void draw(Screen screen, boolean flipX, boolean flipY, boolean rotate90) {
        frames[currentAnimation][currentFrame].draw(screen, (int) x, (int) y, flipX, flipY, rotate90);
    }
    
    /**
     * This should be called each tick in the tick method of the screen this entity is displayed on
     * 
     * @see Screen
     */
    public void tick() {
        if (play) {
            nextFrame(currentFps);
        }

        tickCount++;
        if (tickCount > Game.FPS * 2) {
            tickCount = 0;
        }
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int w) {
        width = w;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int h) {
        height = h;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame < frames[currentAnimation].length && currentFrame >= 0 && frames[currentAnimation][currentFrame] != null) {
            this.currentFrame = currentFrame;
        } else {
            throw new IllegalArgumentException("The frame specified is out of bounds: " + currentFrame);
        }
    }

    public void setCurrentAnimation(int currentAnimation) {
        if (currentAnimation < frames.length && currentAnimation >= 0) {
            this.currentAnimation = currentAnimation;
            currentFrame = 0;
            tickCount = -1;
        } else {
            throw new IllegalArgumentException("The frame specified is out of bounds: " + currentAnimation);
        }
    }

    public int getCurrentAnimation() {
        return currentAnimation;
    }

    public void nextFrame() {
        nextFrame(Entity.FPS);
    }

    public void nextFrame(int fps) {
        if (tickCount > Game.FPS / fps) {
            currentFrame++;
            if (currentFrame >= frames[currentAnimation].length || frames[currentAnimation][currentFrame] == null) {
                currentFrame = 0;
            }
            tickCount = -1;
        }
    }

    public boolean hitTest(Sprite test, int x, int y) {
        boolean result = false;

        if (this.getX() + this.getWidth() > x && this.getX() < test.getWidth() + x) {
            if (this.getY() + this.getHeight() > y && this.getY() < y + test.getHeight()) {
                result = true;
            }
        }

        return result;
    }

    public boolean hitTest(ArrayList<Entity> eList) {
        boolean result = false;

        for (int i = 0; i < eList.size(); i++) {
            Entity e = eList.get(i);
            if (!this.equals(e)) {
                if (this.hitTest(e)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public boolean hitTest(Entity e) {
        boolean result = false;

        if (this.getX() + this.getWidth() > e.getX() && this.getX() < e.getWidth() + e.getX()) {
            if (this.getY() + this.getHeight() > e.getY() && this.getY() < e.getY() + e.getHeight()) {
                result = true;
            }
        }

        return result;
    }

    public void play(int fps) {
        currentFps = fps;
        tickCount = 0;
        play = true;
    }

    public void play() {
        play(Entity.FPS);
    }

    public void stop() {
        play = false;
    }
    
    public void setFacingLeft(boolean facingLeft){
    	this.facingLeft = facingLeft;
    }
}
