package components;

import com.artemis.Component;

public class PickUp extends Component {
	private String code;
	private boolean pegou;
	
	public boolean isPegou() {
		return pegou;
	}

	public void setPegou(boolean pegou) {
		this.pegou = pegou;
	}

	public PickUp (String code)
	{
		this.code = code;
	}
	
	public String getCode ()
	{
		return code;
	}
}
