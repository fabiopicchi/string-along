package components;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;

import utils.ChangeCoordinates;


import com.artemis.Component;

public class TipoFisica extends Component {
	
	public static int NONE = 0x0000;
	public static int LEFT = 0x0001;
	public static int UP = 0x0002;
	public static int RIGHT = 0x0004;
	public static int DOWN = 0x0008;
	private int timer = 0;
	
	public static float KNOCK_BACK_DECREASE = 200.0f;
	
    private Body body;
    private Filter collisionGroup;
    private BodyType type;
    private boolean sensor;
    private int touching;
    private float knockbackSpeed = 0.0f;
    private Vec2 referentialSpeed = new Vec2 (0.0f, 0.0f);
    private boolean removedReferentialSpeed = false;
    
    public Vec2 getReferentialSpeed() {
		return referentialSpeed;
	}
	public void setReferentialSpeed(Vec2 referentialSpeed) {
		removedReferentialSpeed = false;
		
		float vx = getVx () - this.referentialSpeed.x;
		float vy = getVy () - this.referentialSpeed.y;
		
		this.referentialSpeed = new Vec2 (0.0f, 0.0f);
		
		setVelocidade (new Vec2 (vx + referentialSpeed.x, vy + referentialSpeed.y));
		this.referentialSpeed = referentialSpeed;
	}
	public void removeReferentialSpeed ()
	{
		this.referentialSpeed = new Vec2 (0.0f, 0.0f);
		removedReferentialSpeed = true;
	}
	public boolean isRemovedReferentialSpeed() {
		return removedReferentialSpeed;
	}
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}

	public TipoFisica (BodyType type, boolean sensor)
    {
		this.type = type;
		this.sensor = sensor;
    	collisionGroup = new Filter ();
    	touching = NONE;
    }

	public boolean isSensor() {return sensor;}
	public void setSensor(boolean sensor) {this.sensor = sensor;}
	
	public BodyType getType() {return type;}
	public void setType(BodyType type) {this.type = type;}
	
	public Filter getCollisionGroup() {return collisionGroup;}
	public void setCollisionGroup(Filter collisionGroup) {this.collisionGroup = collisionGroup;}
	
	public Body getBody() {return body;}
	public void setBody(Body body) {this.body = body;}
	
	public void setVelocidade (Vec2 velocidade) {
		if (body.getType().equals(BodyType.DYNAMIC))
		{
			Vec2 impulse = ChangeCoordinates.screenToPhysics(new Vec2 (velocidade.x - this.getVx() + knockbackSpeed + ((velocidade.x * referentialSpeed.x >= 0) ? referentialSpeed.x : 0),
					velocidade.y - this.getVy() + ((velocidade.y * referentialSpeed.y >= 0) ? referentialSpeed.y : 0)).mulLocal(body.getMass()));
			if (impulse.x != 0 || impulse.y != 0)body.applyLinearImpulse(impulse, body.getWorldCenter());
		}
		else
		{
			body.setLinearVelocity(ChangeCoordinates.screenToPhysics
					(new Vec2 (velocidade.x + knockbackSpeed + ((velocidade.x * referentialSpeed.x >= 0) ? referentialSpeed.x : 0),velocidade.y + ((velocidade.y * referentialSpeed.y >= 0) ? referentialSpeed.y : 0))));
		}
	}
	public void setVx (float vx) {		
		if (body.getType().equals(BodyType.DYNAMIC))
		{
			Vec2 impulse = ChangeCoordinates.screenToPhysics(new Vec2 (vx - this.getVx() + knockbackSpeed + ((vx * referentialSpeed.x >= 0) ? referentialSpeed.x : 0), 0).mulLocal(body.getMass()));
			if (impulse.x != 0) body.applyLinearImpulse(impulse, body.getWorldCenter());
		}
		else
		{
			body.setLinearVelocity(
					new Vec2 (ChangeCoordinates.screenToPhysics(vx + knockbackSpeed + ((vx * referentialSpeed.x >= 0) ? referentialSpeed.x : 0)), body.getLinearVelocity().y));
		}
	}
	public void setVy (float vy) {
		if (body.getType().equals(BodyType.DYNAMIC))
		{
			Vec2 impulse = ChangeCoordinates.screenToPhysics(new Vec2 (0, vy - this.getVy() + ((vy * referentialSpeed.y >= 0) ? referentialSpeed.y : 0)).mulLocal(body.getMass()));
			if (impulse.y != 0) body.applyLinearImpulse(impulse, body.getWorldCenter());
		}
		else
		{
			body.setLinearVelocity(
					new Vec2 (body.getLinearVelocity().x, - ChangeCoordinates.screenToPhysics(vy + ((vy * referentialSpeed.y >= 0) ? referentialSpeed.y : 0))));
		}
	}
	public Vec2 getVelocidade() {return ChangeCoordinates.physicsToScreen(body.getLinearVelocity());}
	public float getVx () {return ChangeCoordinates.physicsToScreen(body.getLinearVelocity()).x;}
	public float getVy () {return ChangeCoordinates.physicsToScreen(body.getLinearVelocity()).y;}
	
	public Vec2 getPosition () {return ChangeCoordinates.physicsToScreen(body.getPosition());}
	public float getPosX () {return ChangeCoordinates.physicsToScreen(body.getPosition()).x;}
	public float getPosY () {return ChangeCoordinates.physicsToScreen(body.getPosition()).y;}
	
	public int isTouching () {return touching;}
	public void addTouchingDirection (int direction) {touching |= direction;}
	public void removeTouchingDirection (int direction) {touching &= (~direction);}
	public void resetTouching () {touching = NONE;}
	
	public float getKnockbackSpeed() {return knockbackSpeed;}
	public void setKnockbackSpeed(float knockbackSpeed) {
		this.knockbackSpeed = knockbackSpeed;
	}
	
}
