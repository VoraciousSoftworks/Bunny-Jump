package com.voracious.ld24.screens;

import java.awt.Color;
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
	private Play play;
	private Text plusText = new Text("+", Color.BLACK);
	private Text minusText = new Text("-", Color.BLACK);
	private Text titleText = new Text("Evolve");
	private Text moveText = new Text("Move speed");
	private Text jumpText = new Text("Jump power");
	private int evePoints = 0;
	private ArrayList<Button> buttons = new ArrayList<Button>();

	public Evolution(int width, int height, Bunny bunny, Play play) {
		super(width, height);
		this.bunny = bunny;
		this.play = play;
		

		int buttonWidth = plusText.getWidth() + buttonPadding * 2;
		int buttonHeight = plusText.getHeight() + buttonPadding * 2;

		buttons.add(new Button(15, 30, buttonWidth, buttonHeight, "+move", "+"));
		buttons.add(new Button(43, 30, buttonWidth, buttonHeight, "-move", "-"));

		buttons.add(new Button(15, 55, buttonWidth, buttonHeight, "+jump", "+"));
		buttons.add(new Button(43, 55, buttonWidth, buttonHeight, "-jump", "-"));

		int doneWidth = Text.FONT_WIDTH * 4 + buttonPadding * 2;
		buttons.add(new Button(this.getWidth() - doneWidth - 10, this
				.getHeight() - buttonHeight - 10, doneWidth, buttonHeight,
				"done", "done"));
	}

	public void render() {
		play.setOffsetY(0);
		clear(-1);
		titleText.draw(this, 5, 5);

		moveText.draw(this, 15, 20);
		drawButton(buttons.get(0));
		new Text(Integer.toString((int) (bunny.getMoveSpeed()*10))).draw(this, 27, 33);
		drawButton(buttons.get(1));
		new Text("Cost: " + (getCost(bunny.getMoveSpeed()*8) + "EP")).draw(this, 60, 33);

		jumpText.draw(this, 15, 45);
		drawButton(buttons.get(2));
		new Text(Integer.toString(((int) (bunny.getJumpPower()*10))/4)).draw(this, 27, 58);
		drawButton(buttons.get(3));
		new Text("Cost: " + (getCost(bunny.getJumpPower()*2.4) + "EP")).draw(this, 60, 58);

		drawButton(buttons.get(4));
		
		evePoints = play.getEP();
		Text evolvePoints = new Text("Evolution Points: " + evePoints);
		evolvePoints.draw(this, 5, this.getHeight() - Text.FONT_HEIGHT);
	}

	public void drawButton(Button button) {
		Text label;
		if (button.getLabel() == "+") {
			label = plusText;
		} else if (button.getLabel() == "-") {
			label = minusText;
		} else {
			label = new Text(button.getLabel(), Color.BLACK);
		}

		for (int i = 0; i < label.getHeight() + 2 * buttonPadding; i++) {
			for (int j = 0; j < label.getWidth() + 2 * buttonPadding; j++) {
				this.setPixel(buttonColor, button.getX() + j, button.getY() + i);
			}
		}
		label.draw(this, button.getX() + buttonPadding, button.getY()
				+ buttonPadding);
	}
	
	public int getCost(double currentStrength){
		return (int)(Math.pow(1.2, currentStrength)/500*currentStrength);
	}

	public void mouseClicked(MouseEvent e) {
		if (play.isSelectingStats()) {
			for (Button button : buttons) {
				if (button.contains(e.getX() / Game.SCALE, e.getY()
						/ Game.SCALE)) {
					String action = button.getAction();
					if (action.equals("+move")) {
						if(play.getEP() >= getCost(bunny.getMoveSpeed()*8)){
							play.setEP(play.getEP() - getCost(bunny.getMoveSpeed()*8));
							bunny.setMoveSpeed(bunny.getMoveSpeed() + 0.1);
						}
					} else if (action.equals("-move")) {
						if(bunny.getMoveSpeed() > 2){
							bunny.setMoveSpeed(bunny.getMoveSpeed() - 0.1);
							play.setEP(play.getEP() + getCost(bunny.getMoveSpeed()*8));
						}
					} else if (action.equals("+jump")) {
						if(play.getEP() >= getCost(bunny.getJumpPower()*2.4)){
							play.setEP(play.getEP() - getCost(bunny.getJumpPower()*2.4));
							bunny.setJumpPower(bunny.getJumpPower() + 1);
						}
					} else if (action.equals("-jump")) {
						if(bunny.getJumpPower() > 8){
							bunny.setJumpPower(bunny.getJumpPower() - 1);
							play.setEP(play.getEP() + getCost(bunny.getJumpPower()*2.4));
						}
					} else if (action.equals("done")) {
						play.setOffsetY(play.getHeight() - Game.HEIGHT);
						play.setSelectingStats(false);
					}
				}
			}
		}
	}
}
