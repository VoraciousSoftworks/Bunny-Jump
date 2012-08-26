package com.voracious.ld24.entities;

import com.voracious.ld24.screens.Play;

public class Jet extends Bird{
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 15;
	public static final int[] numFrames = { 4 };
	public static final String imageURI = "/jet.png";
	
	public Jet(Play playScreen){
		super(WIDTH, HEIGHT, numFrames, imageURI, playScreen);
		this.play();
	}

}
