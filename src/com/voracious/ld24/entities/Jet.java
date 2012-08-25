package com.voracious.ld24.entities;

import com.voracious.graphics.components.Entity;

public class Jet extends Entity{
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 15;
	public static final int[] numFrames = { 4 };
	public static final String imageURI = "/Jet.png";
	
	
	
	public Jet(){
		super(WIDTH, HEIGHT, numFrames, imageURI);
	}

}
