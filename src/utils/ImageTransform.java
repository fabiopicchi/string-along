package utils;

import main.StringAlong;
import processing.core.PImage;

public class ImageTransform {
	
	public static void imageMirror(PImage I, float x, float y){
		StringAlong.getInstance().pushMatrix();
		StringAlong.getInstance().translate(x,y);
		StringAlong.getInstance().scale(-1,1);
		StringAlong.getInstance().image (I,0,0);
		StringAlong.getInstance().popMatrix();
	}
}