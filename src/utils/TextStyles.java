package utils;

import main.StringAlong;

public class TextStyles {
	public static void shadedText (String text, float x, float y, int shadowOffsetX, int shadowOffsetY){
		StringAlong.getInstance().fill(0);
		StringAlong.getInstance().text(text,x+shadowOffsetX,y+shadowOffsetY);
		StringAlong.getInstance().fill(255);
		StringAlong.getInstance().text(text,x,y);
	}
}