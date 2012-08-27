package com.voracious.ld24.entities;

import com.voracious.graphics.components.*;
import com.voracious.ld24.screens.Play;

public class Snake extends Entity{
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 18;
	public static final int[] numFrames = { 2 };
	public static final String imageURI = "/snake.png";
	private Play playScreen;
	
	public Snake(Play screen, int y){
		super(WIDTH, HEIGHT, numFrames, imageURI);
		playScreen = screen;
		this.setX(playScreen.getWidth());
		this.setY(y);
		this.play();
	}

}
