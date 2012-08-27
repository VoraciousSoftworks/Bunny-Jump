package com.voracious.ld24.entities;

import com.voracious.graphics.components.Entity;
import com.voracious.graphics.components.Screen;
import com.voracious.ld24.screens.Play;

public class Bunny extends Entity {

	public static final int WIDTH = 20;
	public static final int HEIGHT = 12;
	public static final int[] numFrames = { 4 };
	public static final String imageURI = "/Bunny.png";
	
	private double velY = 0.0;
	private double jumpPower = 10.0;
	private double moveSpeed = 3.0;
	private boolean falling = false;

	public Bunny() {
		super(WIDTH, HEIGHT, numFrames, imageURI);
	}
	
	public void jump() {
		setVelY(jumpPower*-1);
		falling = true;
	}
	
	public void tick() {
		super.tick();
		if(falling){
			if (getVelY() < Play.terminalVelocity) {
				setVelY(getVelY() + Play.gravityPower);
			}
		}
		
		this.setY(this.getY() + getVelY());
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}
	
	public boolean isFalling(){
		return falling;
	}
	
	public void setFalling(boolean falling){
		this.falling = falling;
	}

	public double getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public double getJumpPower() {
		return jumpPower;
	}

	public void setJumpPower(double jumpPower) {
		this.jumpPower = jumpPower;
	}
}
