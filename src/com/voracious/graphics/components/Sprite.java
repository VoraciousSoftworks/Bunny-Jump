package com.voracious.graphics.components;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * An object that can be drawn to a Screen in a given position.
 * 
 * @author Andrew Kallmeyer
 */
public class Sprite {
    private int width, height;
    private int[] pixels;

    /**
     * Initialize the Sprite with an image file as its data
     * 
     * @param width
     * @param height
     * @param image File path for the image to pull data from
     */
    public Sprite(int width, int height, String image) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        try {
            BufferedImage file = ImageIO.read(Sprite.class.getResourceAsStream(image));

            for (int i = 0; i < width * height; i++) {
                int col = file.getRGB(i % width, i / width);
                if ((col & 0xff000000) == 0) {
                    pixels[i] = -1;
                } else {
                    pixels[i] = col & 0x00ffffff;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Initialize the Sprite with a BufferedImage as its data
     * 
     * @param width
     * @param height
     * @param image Image to be saved for drawing
     */
    public Sprite(int width, int height, BufferedImage image) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            int col = image.getRGB(i % width, i / width);
            if ((col & 0xff000000) == 0) {
                pixels[i] = -1;
            } else {
                pixels[i] = col & 0x00ffffff;
            }
        }
    }

    /**
     * Initialize the Sprite with an array of color data as its data
     * 
     * @param width
     * @param height
     * @param pixels array of colors to be used for pixels starting from the top left and going to the bottom right. Colors taken the in format of 0xRRGGBB.
     */
    public Sprite(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    /**
     * Draw the Sprite to screen at the coordinates x, y
     * 
     * @param screen screen to draw to
     * @param x
     * @param y
     */
    public void draw(Screen screen, int x, int y) {
        draw(screen, x, y, false, false, false);
    }

    /**
     * Draw the sprite with various modifiers applied to its data
     * 
     * Sprite will be rotated before flipping
     * 
     * @param screen screen to draw to
     * @param x
     * @param y
     * @param flipX whether it should be flipped over x-axis or not
     * @param flipY whether it should be flipped over the y-axis or not
     * @param rotate90 whether it should be flipped over the line y=x
     */
    public void draw(Screen screen, int x, int y, boolean flipX, boolean flipY, boolean rotate90) {
        for (int i = 0; i < pixels.length; i++) {
            int iterator = i;
            int screenX = x + (i % width);
            int screenY = y + (i / width);

            if (rotate90) {
                int tempX = screenX;
                screenX = screenY - y + x;
                screenY = tempX - x + y;

                int tempWidth = getWidth();
                this.width = this.height;
                this.height = tempWidth;
            }

            if (flipX && flipY) {
                iterator = (height - 1 - (i / width)) * width + (width - 1 - (i % width));
            } else if (flipX) {
                iterator = (i / width) * width + (width - 1 - (i % width));
            } else if (flipY) {
                iterator = (height - 1 - (i / width)) * width + (i % width);
            }

            screen.setPixel(pixels[iterator], screenX, screenY);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Returns the array used for drawing so other classes can modify the data directly
     * 
     * @return the array of colors used to draw. Does not return a copy, modifying the returned object will change the image.
     */
    public int[] getPixels() {
        return pixels;
    }
}