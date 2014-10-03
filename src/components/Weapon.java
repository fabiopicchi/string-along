package components;

import java.util.ArrayList;

import com.artemis.Component;
import com.artemis.Entity;

public class Weapon extends Component {
	private boolean active = false;
	private boolean awatingCoolDown = false;
	private Entity wielder;
	private ArrayList <Entity> targetList = new ArrayList <Entity> ();
	
	public Weapon (Entity wielder)
	{
		this.wielder = wielder;
	}
	
	public Entity getWielder ()
	{
		return wielder;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public ArrayList <Entity> getTargetList ()
	{
		return targetList;
	}
	
	public void addTarget (Entity e)
	{
		targetList.add(e);
	}
	
	public void removeTarget (Entity e)
	{
		targetList.remove(e);
	}
	
	public boolean isAwatingCoolDown() {
		return awatingCoolDown;
	}

	public void setAwatingCoolDown(boolean awatingCoolDown) {
		this.awatingCoolDown = awatingCoolDown;
	}

}
