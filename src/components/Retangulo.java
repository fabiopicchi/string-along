package components;


import com.artemis.Component;

public class Retangulo extends Component {
	
	private float width;
	private float height;
	
	public Retangulo (float width, float height)
	{
		this.width = width;
		this.height = height;
	}
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
