package com.voracious.graphics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import com.voracious.graphics.components.Screen;

/**
 * Allows Screens to register and unregister themselves to receive keyboard events
 * 
 * @author Andrew Kallmeyer
 */
public class InputHandler extends KeyAdapter {
    
    private static ArrayList<Screen> screens = new ArrayList<Screen>();
    
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        Iterator<Screen> it = screens.iterator();
        while(it.hasNext()){
            it.next().keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        Iterator<Screen> it = screens.iterator();
        while(it.hasNext()){
            it.next().keyReleased(e);
        }
    }
    
    public static void register(Screen screen){
        screens.add(screen);
    }
    
    public static void unregister(Screen screen){
        screens.remove(screen);
    }
}
