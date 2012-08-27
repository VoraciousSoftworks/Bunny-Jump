package com.voracious.ld24.entities;

import java.util.ArrayList;
import java.util.Random;

import com.voracious.graphics.Game;
import com.voracious.ld24.screens.Play;

public class Jet extends Bird{
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 15;
	public static final int[] numFrames = { 4 };
	public static final String imageURI = "/jet.png";
	private ArrayList<Bomb> bombs;
	private Random rand;
	
	public Jet(Play playScreen, ArrayList<Bomb> bombList){
		super(WIDTH, HEIGHT, numFrames, imageURI, playScreen);
		this.play();
		bombs = bombList;
		rand = new Random();
	}
	
	public void tick(){
		super.tick();
		
		if(rand.nextInt(Game.FPS * 40) == 0){
			Bomb temp = new Bomb();
			temp.setX(this.getX() + this.getWidth()/2);
			bombs.add(temp);
		}
	}

}
