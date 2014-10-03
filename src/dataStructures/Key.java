package dataStructures;

public class Key {
	
	public boolean pressed = false;
	public boolean justPressed = false;
	public boolean justReleased = false;
	public boolean bufferJustPressed = false;
	public boolean bufferJustReleased = false;
	public boolean dirty = false;
	public int timePressed = 0;
}
