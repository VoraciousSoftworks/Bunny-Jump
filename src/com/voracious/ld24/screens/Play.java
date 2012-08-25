package com.voracious.ld24.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.voracious.graphics.Game;
import com.voracious.graphics.InputHandler;
import com.voracious.graphics.components.Screen;

public class Play extends Screen {
	
	private static ArrayList<Integer> heightMap = new ArrayList<Integer>();
	private static int offsetX = 0;
	private static boolean[] keysDown = {false, false, false, false}; //w, a, s, d

	public Play(int width, int height) {
		super(width, height);
	}
	
	public void start(){
        InputHandler.register(this);
        generateLevel(1.0f);
    }
    
    public void stop() {
        InputHandler.unregister(this);
    }
    
    public void generateLevel(float difficulty){
    	int size = (int) (difficulty * 0xfff);
    	
    	for(int i=0; i<size; i++){
    		heightMap.add(Integer.valueOf(Math.abs((int) (Math.sin(i/100.0)*75))));
    	}
    }
    
    public void render(){
    	clear(0xffffff);
    	for(int i=0; i<this.getWidth(); i++){
    		int height = heightMap.get(i+offsetX);
    		for(int j=0; j<height; j++){
    			this.setPixel(0xff00ff, i, 149-j);
    			//System.out.println(j);
    		}
    	}
    }
    
    public void tick() {
    	if(keysDown[1]){ //a
    		if(offsetX > 5){
    			offsetX -= 5;
    		}
    	}else if(keysDown[3]){ //d
    		if(offsetX < heightMap.size() - this.getWidth() - 5){
    			offsetX += 5;
    		}
    	}
    }
    
    public void keyPressed(KeyEvent e){
    	if(e.getKeyChar() == 'w'){
    		keysDown[0] = true;
    	}else if(e.getKeyChar() == 'a') {
            keysDown[1] = true;
        }else if(e.getKeyChar() == 's'){
        	keysDown[2] = true;
        }else if(e.getKeyChar() == 'd') {
            keysDown[3] = true;
        }
    }
    
    public void keyReleased(KeyEvent e){
    	if(e.getKeyChar() == 'w'){
    		keysDown[0] = false;
    	}else if(e.getKeyChar() == 'a') {
            keysDown[1] = false;
        }else if(e.getKeyChar() == 's'){
        	keysDown[2] = false;
        }else if(e.getKeyChar() == 'd') {
            keysDown[3] = false;
        }
    }
}
