package com.voracious.ld24.entities;

import com.voracious.ld24.screens.Play;

public class EvilHawk extends Bird{
	
	public static final int WIDTH = 15;
	public static final int HEIGHT = 10;
	public static final int[] numFrames = { 4 };
	public static final String imageURI = "/evilHawk.png";
	
	public EvilHawk(Play playScreen){
		super(WIDTH, HEIGHT, numFrames, imageURI, playScreen);
		this.play();
	}

}