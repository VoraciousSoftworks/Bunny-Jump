package com.voracious.graphics;

import java.awt.BorderLayout;

/**
 * Class run when the game is played as a web-applet
 * 
 * @author Andrew Kallmeyer
 */
public class Applet extends java.applet.Applet {
    private static final long serialVersionUID = 1L;
    public static Game game;
    
    @Override
    public void init(){
        game = new Game();
        setLayout(new BorderLayout());
        this.add(game, BorderLayout.CENTER);
    }
    
    @Override
    public void start(){
        game.start();
    }
    
    @Override
    public void stop(){
        game.stop();
    }
}
