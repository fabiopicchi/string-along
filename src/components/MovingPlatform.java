package components;

import java.util.Vector;

import com.artemis.Component;

public class MovingPlatform extends Component {
	private Vector <TipoFisica> target;
	
	public MovingPlatform ()
	{
		target = new Vector <TipoFisica> ();
	}
	
	public void addTarget (TipoFisica tf)
	{
		if (target.indexOf(tf) == -1)
			target.add(tf);
	}
	
	public void removeTarget (TipoFisica tf)
	{
		if (target.indexOf(tf) != -1)
			target.remove(tf);
	}
	
	public Vector<TipoFisica> getTargets ()
	{
		return target;
	}
}
