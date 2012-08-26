package com.voracious.ld24.entities;

import com.voracious.graphics.components.*;

public class Snake extends Entity{
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 18;
	public static final int[] numFrames = { 2 };
	public static final String imageURI = "/snake.png";
	
	public Snake(){
		super(WIDTH, HEIGHT, numFrames, imageURI);
	}

}
