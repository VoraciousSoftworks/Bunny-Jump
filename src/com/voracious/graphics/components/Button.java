package com.voracious.graphics.components;

public class Button {

	private int x;
	private int y;
	private int width;
	private int height;
	private String action;
	private String label;
	
	public Button(int x, int y, int width, int height, String action, String label) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.action = action;
		this.label = label;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean contains(int x, int y){
		return x > this.getX() && x < this.getX() + this.getWidth() && y > this.getY() && y < this.getHeight() + this.getY();
	}
}
