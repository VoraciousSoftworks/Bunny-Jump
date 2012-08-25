package com.voracious.ld24.screens;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.voracious.graphics.Game;
import com.voracious.graphics.components.Button;
import com.voracious.graphics.components.Screen;
import com.voracious.graphics.components.Text;
import com.voracious.ld24.entities.Bunny;

public class Evolution extends Screen {
	
	public static final int buttonPadding = 2;
	public static final int buttonColor = 0xAAAAAA;
	private Bunny bunny;
	private Text plusText = new Text("+");
	private Text minusText = new Text("-");
	private Text titleText = new Text("Evolve");
	private Text moveText = new Text("Move speed");
	private Text jumpText = new Text("Jump power");
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	public Evolution(int width, int height, Bunny bunny) {
		super(width, height);
		this.bunny = bunny;
		
		int buttonWidth = plusText.getWidth()+buttonPadding*2;
		int buttonHeight = plusText.getHeight()+buttonPadding*2;
		
		buttons.add(new Button(15, 30, buttonWidth, buttonHeight, "+move", true));
		buttons.add(new Button(43, 30, buttonWidth, buttonHeight, "-move", false));
		
		buttons.add(new Button(15, 55, buttonWidth, buttonHeight, "+jump", true));
		buttons.add(new Button(43, 55, buttonWidth, buttonHeight, "-jump", false));
	}
	
	public void render(){
		clear(0xffffff);
		titleText.draw(this, 5, 5);
		
		moveText.draw(this, 15, 20);
		drawButton(buttons.get(0));
		new Text(Integer.toString((int)bunny.getMoveSpeed())).draw(this, 27, 33);
		drawButton(buttons.get(1));
		
		jumpText.draw(this, 15, 45);
		drawButton(buttons.get(2));
		new Text(Integer.toString((int)bunny.getJumpPower())).draw(this, 27, 58);
		drawButton(buttons.get(3));
	}
	
	public void drawButton(Button button){
		Text label;
		if(button.isPlus()){
			label = plusText;
		}else{
			label = minusText;
		}
		for(int i = 0; i < label.getHeight() + 2*buttonPadding; i++){
			for(int j = 0; j < label.getWidth() + 2*buttonPadding; j++){
				this.setPixel(buttonColor, button.getX()+j, button.getY()+i);
			}
		}
		label.draw(this, button.getX()+buttonPadding, button.getY()+buttonPadding);
	}
	
	public void mouseClicked(MouseEvent e){
		for(Button button : buttons){
			if(button.contains(e.getX()/Game.SCALE, e.getY()/Game.SCALE)){
				String action = button.getAction();
				if(action.equals("+move")){
					bunny.setMoveSpeed(bunny.getMoveSpeed() + 1);
				}else if(action.equals("-move")){
					bunny.setMoveSpeed(bunny.getMoveSpeed() - 1);
				}else if(action.equals("+jump")){
					bunny.setJumpPower(bunny.getJumpPower() + 1);
				}else if(action.equals("-jump")){
					bunny.setJumpPower(bunny.getJumpPower() - 1);
				}
			}
		}
	}
}
