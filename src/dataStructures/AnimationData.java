package dataStructures;

public class AnimationData {
	public AnimationFrame [] frames;
	public int frameDuration;
	public boolean loop;
	public int offsetX;
	public int offsetY;
	
	public AnimationData (AnimationFrame [] frames, int frameDuration, boolean loop, int offsetX, int offsetY)
	{
		this.frames = frames;
		this.frameDuration = frameDuration;
		this.loop = loop;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
}
