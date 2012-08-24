package com.voracious.project.screens;

import java.awt.event.KeyEvent;

import com.voracious.graphics.Game;
import com.voracious.graphics.InputHandler;
import com.voracious.graphics.components.Screen;

/**
 * Temporary class used to demonstrate pixel color writing
 * 
 * @author Andrew Kallmeyer
 */

public class Temp extends Screen {
    
    public Temp(int width, int height){
        super(width, height);
    }
    
    public void start(){
        InputHandler.register(this);
    }
    
    public void stop() {
        InputHandler.unregister(this);
    }
    
    public void render(){
        for(int x = 0; x < this.getWidth(); x++){
            for(int y = 0; y < this.getHeight(); y++){
                this.setPixel(x*y + tickCount, x, y);
            }
        }
    }
    
    private int tickCount = 0;
    public void tick(){
        tickCount++;
        if(tickCount > 0xff){
            tickCount = 0;
        }
    }
    
    public void keyReleased(KeyEvent e){
        if(e.getKeyChar() == 'p') {
            if(Game.isPaused()){
                Game.setPaused(false);
            }else{
                Game.setPaused(true);
            }
        }
    }
}
