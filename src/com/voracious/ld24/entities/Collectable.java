package com.voracious.ld24.entities;

import com.voracious.graphics.components.Entity;

public class Collectable extends Entity{
	
	public static final int WIDTH = 3;
	public static final int HEIGHT = 3;
	public static final int[] numFrames = { 1, 1, 6};
	public static final String imageURI = "/carrots.png";
	public static int value = 1; //Should only be 1, 2, or 3. Indicating value of carrot in EP
	
	
	//Level should only be 1, 2, or 3
	public Collectable(int level){
		super(WIDTH, HEIGHT, numFrames, imageURI);
		if(level == 1){
			this.setCurrentAnimation(0);
			value = 1;
		}
		if(level == 2){
			this.setCurrentAnimation(1);
			value = 2;
		}
		if(level == 3){
			this.setCurrentAnimation(2);
			value = 3;
			this.play();
		}
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int v){
		value = v;
		if(v == 3)
			this.play();
	}

}
