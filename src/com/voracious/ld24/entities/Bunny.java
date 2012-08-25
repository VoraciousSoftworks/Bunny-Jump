package com.voracious.ld24.entities;

import com.voracious.graphics.components.Entity;
import com.voracious.graphics.components.Screen;

public class Bunny extends Entity {

	public static final int WIDTH = 20;
	public static final int HEIGHT = 12;
	public static final int[] numFrames = { 1 };
	public static final String imageURI = "/Bunny.png";

	public Bunny() {
		super(WIDTH, HEIGHT, numFrames, imageURI);
	}

	public void draw(Screen screen) {
		draw(screen, false, false, false);
	}
}
