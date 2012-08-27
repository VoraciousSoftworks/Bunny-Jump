package com.voracious.ld24.entities;

import com.voracious.graphics.components.Entity;
import com.voracious.ld24.screens.Play;

public class Bomb extends Entity {

	public static final int WIDTH = 7;
	public static final int HEIGHT = 7;
	public static final int[] numFrames = { 1, 5 };
	public static final String imageURI = "/bomb.png";
	public static final int explosionSize = 25;
	private double velY = 0.0;

	public Bomb() {
		super(WIDTH, HEIGHT, numFrames, imageURI);
	}

	public void explode() {
		this.setCurrentAnimation(1);
		this.play();
	}

	public void tick() {
		super.tick();
		if (getVelY() < Play.terminalVelocity) {
			setVelY(getVelY() + Play.gravityPower);
		}
		this.setY(this.getY() + getVelY());
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}
}
