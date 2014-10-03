package dataStructures;

import java.util.HashMap;

public class AnimationFrame {
	public int frame;
	public HashMap <String, Float []> refPoints; 
	
	public AnimationFrame (int frame){
		this.frame = frame;
		refPoints = new HashMap<String, Float[]>();
	}
	
	public void addRefpoint (String key, Float[] value)
	{
		refPoints.put(key, value);
	}
}
