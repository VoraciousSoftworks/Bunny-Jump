package com.voracious.graphics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.voracious.graphics.components.Screen;

public class MouseHandler extends MouseAdapter {
	private static ArrayList<Screen> screens = new ArrayList<Screen>();

	public void mouseClicked(MouseEvent e){
		super.mouseClicked(e);
		for(Screen screen : screens){
			screen.mouseClicked(e);
		}
	}
	
	public static void register(Screen screen) {
		screens.add(screen);
	}

	public static void unregister(Screen screen) {
		screens.remove(screen);
	}
}
