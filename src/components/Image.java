package components;

import processing.core.PImage;
import managers.ResourceManager;
import com.artemis.Component;

public class Image extends Component {
	private String fileName;
	private boolean scalable;
	private boolean centerTile;
	private boolean imageLoaded = false;
	private int offsetX;
	private int offsetY;

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public boolean isCenterTile() {
		return centerTile;
	}

	public void setCenterTile(boolean centerTile) {
		this.centerTile = centerTile;
	}
	
	public Image (String fileName, boolean scalable, boolean centerTile)
	{
		this.fileName = fileName;
		this.scalable = scalable;
		this.centerTile = centerTile;
		this.offsetX = 0;
		this.offsetY = 0;
	}
	
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public PImage getImage ()
	{
		return ResourceManager.getImage(fileName);
	}
	
	public String getFileName () {
		return fileName;
	}
	
	public boolean isScalable() {
		return scalable;
	}

	public boolean isImageLoaded() {
		return imageLoaded;
	}

	public void setImageLoaded(boolean imageLoaded) {
		this.imageLoaded = imageLoaded;
	}
}
