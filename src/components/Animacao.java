package components;

import java.util.HashMap;

import managers.ResourceManager;

import processing.core.PImage;
import com.artemis.Component;

import dataStructures.AnimationData;
import dataStructures.AnimationFrame;

public class Animacao extends Component {
	private int spriteWidth;
	private int spriteHeight;
	private int cycle;
	private int timer;
	private String tag;
	private HashMap <String, AnimationData> animationData;
	private String fileName;
	private String[] files = null;
	private boolean animationOver = false;
	private boolean scroll = false;

	public boolean isScroll() {
		return scroll ;
	}

	public boolean isAnimationOver() {
		return animationOver;
	}

	public String getFileName() {
		return fileName;
	}

	public Animacao (String fileName, int spriteWidth, int spriteHeight, boolean scroll)
	{
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.fileName = fileName;
		this.scroll = scroll;
		cycle = 0;
		timer = 0;
		animationData = new HashMap <String, AnimationData> ();
	}
	
	public Animacao (String [] fileNames, int spriteWidth, int spriteHeight, boolean scroll)
	{
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.files = fileNames;
		this.fileName = fileNames[0];
		this.scroll = scroll;
		cycle = 0;
		timer = 0;
		animationData = new HashMap <String, AnimationData> ();
	}

	public float getFrameDuration() {
		return animationData.get(tag).frameDuration;
	}

	public int getOffsetX () {
		if (animationData.get(tag) != null)
			return animationData.get(tag).offsetX;
		else return 0;
	}
	
	public int getOffsetY () {
		if (animationData.get(tag) != null)
			return animationData.get(tag).offsetY;
		else return 0;
	}
	
	public void addAnimation (String label, AnimationFrame [] indices, int frameDuration, boolean loop, int offsetX, int offsetY)
	{
		animationData.put(label, new AnimationData (indices, frameDuration, loop, offsetX, offsetY));
	}

	public PImage getFrame ()
	{
		if (isAnimationLoaded())
		{
			ResourceManager.loadAnimation(ResourceManager.getImage(fileName), fileName, spriteWidth, spriteHeight);
			return ResourceManager.getAnimation(fileName)[animationData.get(tag).frames[cycle].frame];
		}
		return null;
	}

	public boolean isAnimationLoaded() {
		return (ResourceManager.getImage(fileName) != null && ResourceManager.getImage(fileName).width > 0) || ResourceManager.getAnimation(fileName) != null;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		if (this.tag != null && !this.tag.equals(tag)) 
		{
			if (!this.tag.substring(0, this.tag.length() - 5).equals(this.tag.substring(0, tag.length() - 5)))
			{
				timer = 0;
				animationOver = false;
			}
		}
		this.tag = tag;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isScalable ()
	{
		return (files == null);
	}

	public int getTimer() {
		return timer;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}

	public HashMap<String, AnimationData> getAnimationData() {
		return animationData;
	}

	public void setAnimationData(HashMap<String, AnimationData> animationData) {
		this.animationData = animationData;
	}

	public void setAnimationOver(boolean animationOver) {
		this.animationOver = animationOver;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public int getCycle() {
		return cycle;
	}
	
	public Float[] getPoint (String name)
	{
		return animationData.get(tag).frames[cycle].refPoints.get(name);
	}
}
