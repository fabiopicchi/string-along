package components;

import java.util.Vector;

import com.artemis.*;

public class HP extends Component {
	private int HP;
	private int HPmax;
	private boolean invincible = false;
	private int timer = 0;
	private int invincibilityDuration = 0;
	private Vector <Entity> tags = new Vector <Entity> ();
	//Reference to the HP lost on this frame
	private int lostHP = 0;
	
	public HP(int hp) {
		HP = hp;
		HPmax = hp;
	}
	public void addTag (Entity s)
	{
		tags.add(s);
	}
	public void removeTag (Entity s)
	{
		tags.remove(s);
	}
	public Entity getTag (int index)
	{
		if (index < tags.size())
			return tags.get(index);
		else
			return null;
	}
	public int getHPmax() 
	{
		return HPmax;
	}
	public int getTimer() 
	{
		return timer;
	}
	public void setTimer(int timer) 
	{
		this.timer = timer;
	}
	public int getHP() 
	{
		return HP;
	}
	public void addLife(int n)
	{
		if(HP<HPmax)
			HP=HP+n;
		else
			HP = HPmax;
	}
	public void subtractLife(int n)
	{
		if (HP - n >= 0)
		{
			lostHP = n;
			HP -= n;
		}
		else
		{
			lostHP = HP;
			HP = 0;
		}
	}
	public int getLostHP ()
	{
		return lostHP;
	}
	public void resetLostHP () 
	{
		lostHP = 0;
	}
	public boolean isInvincible() 
	{
		return invincible;
	}
	public void setInvincible(int duration) 
	{
		this.invincibilityDuration = duration;
		invincible = true;
	}
	public void endInvincibility () 
	{
		invincible = false;
	}
	public int getInvincibilityDuration() 
	{
		return invincibilityDuration;
	}
}