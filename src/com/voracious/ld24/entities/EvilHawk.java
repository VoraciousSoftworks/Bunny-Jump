package com.voracious.ld24.entities;

import com.voracious.graphics.components.Entity;

public class EvilHawk extends Entity{
	
	public static final int WIDTH = 15;
	public static final int HEIGHT = 10;
	public static final int[] numFrames = { 4 };
	public static final String imageURI = "/evilHawk.png";
	
	public EvilHawk(){
		super(WIDTH, HEIGHT, numFrames, imageURI);
	}

}