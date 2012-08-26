package com.voracious.ld24.entities;

import java.util.Random;

import com.voracious.graphics.components.Entity;
import com.voracious.ld24.screens.Play;

public class Bird extends Entity {
	private double flySpeed = 1.0;
	private Play playScreen;
	private Random rand;
	
	public Bird(int width, int height, int[] numFrames, String image, Play playScreen) {
		super(width, height, numFrames, image);
		this.playScreen = playScreen;
		rand = new Random();
		
		this.setX(playScreen.getWidth());
		this.setY(rand.nextDouble()*playScreen.getHeight()*9/10 + 50);
		
		this.setFlySpeed(this.getFlySpeed()*((rand.nextDouble()+4)/2));
		this.setFacingLeft(true);
	}
	
	public void tick(){
		super.tick();
		
		this.setX(this.getX() - getFlySpeed());
	}
	
	public double getFlySpeed() {
		return flySpeed;
	}
	
	public void setFlySpeed(double speed) {
		flySpeed = speed;
	}
}
