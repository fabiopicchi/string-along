package managers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Set;

import main.StringAlong;

import dataStructures.Key;

import processing.core.PApplet;


public class InputManager implements KeyListener {
	
	private HashMap <Integer, Key> keys;
	
	public static float mouseX, mouseY;
	public static boolean mouseMoved, mouseClicked, mouseDown;

	public InputManager (HashMap <Integer, Key> keys)
	{
		super ();
		this.keys = keys;
		addKeys ();
	}
	
	public void updateMouse(){
		//TODO: se widescreen == true, adicionar a diferença de tamanho das telas a mouseX.
		mouseMoved=false;
		mouseClicked=false;
		if (StringAlong.getInstance().mousePressed==true){
			if (mouseDown==false) mouseClicked=true;
			mouseDown=true;
		}
		else mouseDown=false;

		if (mouseX!=StringAlong.getInstance().mouseX) mouseMoved=true;
		if (mouseY!=StringAlong.getInstance().mouseY) mouseMoved=true;
		mouseX=StringAlong.getInstance().mouseX;
		mouseY=StringAlong.getInstance().mouseY;
	}
	
	private void addKeys ()
	{
		keys.put(KeyEvent.VK_W, new Key());
		keys.put(KeyEvent.VK_S, new Key());
		keys.put(KeyEvent.VK_D, new Key());
		keys.put(KeyEvent.VK_E, new Key());
		keys.put(KeyEvent.VK_F, new Key());
		keys.put(KeyEvent.VK_ENTER, new Key());
		keys.put(KeyEvent.VK_ESCAPE, new Key());
		keys.put(KeyEvent.VK_SPACE, new Key());
		keys.put(PApplet.LEFT, new Key());
		keys.put(PApplet.UP, new Key());
		keys.put(PApplet.RIGHT, new Key());
		keys.put(PApplet.DOWN, new Key());
		keys.put(KeyEvent.VK_P, new Key());
		keys.put(KeyEvent.VK_L, new Key());
	}
	
	private void refreshKeys ()
	{
		Set <Integer> indices = keys.keySet ();
		Key key;
		for (Integer indice : indices)
		{
			key = keys.get(indice);
			if (key.dirty)
			{
				key.justPressed = false;
				key.justReleased = false;
				key.dirty = false;
			}
		}
	}
	
	
	private void updateKeys ()
	{
		Set <Integer> indices = keys.keySet ();
		Key key;
		for (Integer indice : indices)
		{
			key = keys.get(indice);
			if (key.pressed) key.timePressed += StringAlong.getInstance().timeElapsed;
			key.justPressed = key.bufferJustPressed;
			key.justReleased = key.bufferJustReleased;
			if (key.bufferJustPressed || key.bufferJustReleased) key.dirty = true;
			key.bufferJustPressed = false;
			key.bufferJustReleased = false;
		}
	}
	
    public void keyTyped(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) 
    {
    	Key key = keys.get(e.getKeyCode());
    	
    	if (key != null)
    	{
    		if (!key.pressed) key.bufferJustPressed = true;
    		key.pressed = true;
    	}
    }
    
    public void keyReleased(KeyEvent e) 
    {
    	Key key = keys.get(e.getKeyCode());
    	
    	if (key != null)
    	{
    		key.bufferJustReleased= true;
    		key.pressed = false;
    		key.timePressed = 0;
    	}
    }
    
    public void start ()
    {
    	updateKeys ();
    	updateMouse(); //TODO:ver com o picchi se pode ser assim
    	
    }
    
    public void end ()
    {
    	refreshKeys ();
    }

}
