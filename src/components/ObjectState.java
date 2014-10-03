package components;

import com.artemis.Component;

public class ObjectState extends Component {
	private boolean attacking = false;
	private boolean jumping = false;
	private boolean falling = false;
	private boolean moving = false;
	private boolean dying = false;
	private boolean takingDamage = false;
	
	public boolean isTakingDamage() {
		return takingDamage;
	}
	public void setTakingDamage(boolean takingDamage) {
		this.takingDamage = takingDamage;
	}
	private int facing = 1;
	
	public boolean isAttacking() {
		return attacking;
	}
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	public boolean isJumping() {
		return jumping;
	}
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	public boolean isFalling() {
		return falling;
	}
	public void setFalling(boolean falling) {
		this.falling = falling;
	}
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	public boolean isDying() {
		return dying;
	}
	public void setDying(boolean dying) {
		this.dying = dying;
	}
	public int getFacing() {
		return facing;
	}
	public void setFacing(int facing) {
		this.facing = facing;
		//0 = esq; 1 = dir;
	}
	
	
}
