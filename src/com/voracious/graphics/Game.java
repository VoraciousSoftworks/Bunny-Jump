package com.voracious.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;

import javax.swing.JFrame;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import com.voracious.graphics.components.Screen;
import com.voracious.ld24.screens.Play;
import com.voracious.ld24.screens.Temp;

/**
 * Basic boiler plate code to create a window and allow rendering pixel data to it.
 * Handles screen and sound management.
 * 
 * @author Andrew Kallmeyer
 */
public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    public static final int FPS = 60; //FPS to limit the game to
    public static final String NAME = "Ludum Dare 24"; //Title of the game window
    public static final int WIDTH = 200; //Width of the canvas that will be scaled by the scale factor
    public static final int HEIGHT = 150; //Height of the canvas that will be scaled by the scale factor
    public static final int SCALE = 3; //Scale everything up by this factor, 3 means each drawn pixel is draw 3x3 
    private static final boolean showFps = true; //Whether the game should print the fps and ticks per second or not

    private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
    private static HashMap<String, Music> songs = new HashMap<String, Music>();
    private static HashMap<String, Screen> screens = new HashMap<String, Screen>();
    private static Screen currentScreen;

    public static BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private static boolean running = false;
    private static boolean paused = false;
    
    public Game() {
        init();
    }

    /**
     * Startup!
     * 
     * @params args Does nothing, ignore.
     */
    public static void main(String[] args) {
        Game game = new Game();
        Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        game.setMinimumSize(size);
        game.setMaximumSize(size);
        game.setPreferredSize(size);

        JFrame window = new JFrame();
        window.setTitle(NAME);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(game, BorderLayout.CENTER);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        game.start();
    }

    public void init() {
        TinySound.init();
        this.addKeyListener(new InputHandler());

        //TODO: Register screens and set the initial screen
        Game.registerScreen("play", new Play(Game.WIDTH, Game.HEIGHT));
        Game.switchScreen("play");

        //TODO: Register sounds
        //Game.registerSound("name", TinySound.loadSound("sound.wav"));
        //Play later with Game.getSound("name").play();
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public void tick() {
        if(!paused){
            currentScreen.tick();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        currentScreen.render();
        currentScreen.draw(pixels);

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        long last = System.nanoTime();
        long lastMillis = System.currentTimeMillis();
        double unprocessedTicks = 0;
        double nsPerTick = 1000000000.0 / FPS;
        int framesShown = 0;
        int ticksProcessed = 0;
        boolean needsRender = false;

        while (running) {
            long now = System.nanoTime();
            unprocessedTicks += (now - last) / nsPerTick;
            last = now;

            if (unprocessedTicks >= 1.0) {
                needsRender = true;
                ticksProcessed++;
                tick();
                unprocessedTicks -= 1.0;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (needsRender) {
                render();
                framesShown++;
                needsRender = false;
            }

            if (System.currentTimeMillis() - lastMillis > 1000) {
                lastMillis = System.currentTimeMillis();
                if (showFps) {
                    System.out.println(ticksProcessed + " tps    " + framesShown + " fps");
                }
                framesShown = 0;
                ticksProcessed = 0;
            }
        }
    }

    public static void registerScreen(String name, Screen screen) {
        screens.put(name, screen);
    }

    public static void switchScreen(String name) {
        if(currentScreen != null){
            currentScreen.stop();
        }
        currentScreen = screens.get(name);
        currentScreen.start();
    }

    public static void registerSound(String name, Sound sound) {
        sounds.put(name, sound);
    }

    public static void registerMusic(String name, Music music) {
        songs.put(name, music);
    }

    public static Sound getSound(String name) {
        return sounds.get(name);
    }

    public static Music getMusic(String name) {
        return songs.get(name);
    }
    
    public static void setPaused(boolean paused){
        Game.paused = paused;
    }
    
    public static boolean isPaused(){
        return Game.paused;
    }
}
