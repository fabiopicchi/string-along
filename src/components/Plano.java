package components;

import com.artemis.Component;

public class Plano extends Component {
	private int plano;
	private char changing = NOT_CHANGING;
	
	public static final char NOT_CHANGING = 0;
	public static final char CHANGING_FOWARD = 1;
	public static final char CHANGING_BACKWARD = 2;
	public static final char ARRIVING = 4;
	
	public Plano (int plano)
	{
		this.plano = plano;
	}
	
	public int getPlano() {return plano;}
	public void setPlano(int plano) {this.plano = plano;}
	public char getChanging() {return changing;}
	public void setChanging(char changing) {this.changing |= changing;}
	public void reset () {this.changing = NOT_CHANGING;}
	public float getPlaneFactor ()
	{
		switch (plano)
		{
		case 1:
			return 1.0f;
		case 2:
			return 64.0f / 86.0f;
		case 3:
			return 48.0f / 86.0f;
		default:
			return 1.0f;	
		}
	}
	
}
