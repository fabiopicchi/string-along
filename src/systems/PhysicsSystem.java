package systems;

import java.awt.event.KeyEvent;

import main.StringAlong;
import states.PlayState;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import utils.ChangeCoordinates;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.HP;
import components.Image;
import components.MovingPlatform;
import components.ObjectState;
import components.Plano;
import components.Posicao;
import components.Retangulo;
import components.TipoFisica;

public class PhysicsSystem extends EntityProcessingSystem implements ContactListener{
	
	private World pWorld;
	public World getpWorld() {return pWorld;}

//	public static Vec2 GRAVITY = ChangeCoordinates.screenToPhysics(new Vec2 (0.0f, Float.parseFloat(((PlayState)StringAlong.getInstance().state).config.getProperty("gravidade"))));
	public static Vec2 GRAVITY = ChangeCoordinates.screenToPhysics(new Vec2 (0.0f, 2000.0f));
	public static float GRAVITY_Y = 2000.0f;
	
	@SuppressWarnings("unchecked")
	public PhysicsSystem ()
	{
		super (Retangulo.class, Posicao.class, TipoFisica.class);
		
		this.pWorld = new World (GRAVITY, true);
		pWorld.setContactListener(this);
	}
	@Override
	public void initialize ()
	{
	}	
	
	@Override
	public void added (Entity e)
	{
		Posicao posicao = PlayState.posicaoMapper.get(e);
		TipoFisica tipoFisica = PlayState.tipoFisicaMapper.get(e);
		Retangulo retangulo = PlayState.retanguloMapper.get(e);
		Plano plano = PlayState.planoMapper.get(e);
		Image image = PlayState.imageMapper.get(e);
		
		String group = world.getGroupManager().getGroupOf(e);
		
		BodyDef bd = new BodyDef ();
		bd.type = tipoFisica.getType();
		if (image != null && image.isCenterTile())
		{
			bd.position.set (ChangeCoordinates.screenToPhysics ( 
					new Vec2 (posicao.getPosicao().x + 86 / 2,
							posicao.getPosicao().y + 86 / 2)));
		}
		else
		{
			bd.position.set (ChangeCoordinates.screenToPhysics ( 
					new Vec2 (posicao.getPosicao().x + retangulo.getWidth() / 2,
							posicao.getPosicao().y + retangulo.getHeight() / 2)));
		}
		if (group != null && group.equals("PLAYER")) bd.bullet = true;
		bd.userData = e;
		
		PolygonShape shape = new PolygonShape ();
		shape.setAsBox(ChangeCoordinates.screenToPhysics(retangulo.getWidth()/2), ChangeCoordinates.screenToPhysics(retangulo.getHeight()/2));

		FixtureDef fd = new FixtureDef ();
		fd.friction = 0.0f;
		fd.filter.groupIndex = plano.getPlano();
		fd.shape = shape;
		fd.isSensor = tipoFisica.isSensor();
		
		tipoFisica.setBody(pWorld.createBody (bd));
		tipoFisica.getBody().createFixture(fd);
	}
	
	@Override
	protected void process(Entity e) 
	{	
		TipoFisica tipoFisica = PlayState.tipoFisicaMapper.get(e);
		Plano plano = PlayState.planoMapper.get(e);
		HP hp = PlayState.HPMapper.get(e);
		ObjectState objState = PlayState.objectStateMapper.get(e);
		
		String group = world.getGroupManager().getGroupOf(e);
		
		if (tipoFisica.getCollisionGroup().groupIndex != plano.getPlano())
		{
			tipoFisica.getCollisionGroup().groupIndex = plano.getPlano();
			tipoFisica.getBody().getFixtureList().setFilterData(tipoFisica.getCollisionGroup());
		}
		
		if (tipoFisica.getKnockbackSpeed() > 0)
		{
			tipoFisica.setKnockbackSpeed(tipoFisica.getKnockbackSpeed() - TipoFisica.KNOCK_BACK_DECREASE);
		}
		else if (tipoFisica.getKnockbackSpeed() < 0)
		{
			tipoFisica.setKnockbackSpeed(tipoFisica.getKnockbackSpeed() + TipoFisica.KNOCK_BACK_DECREASE);
		}
		
		if ((tipoFisica.getVelocidade().y - tipoFisica.getReferentialSpeed().y) == 0 && (tipoFisica.isTouching() & TipoFisica.DOWN) == TipoFisica.DOWN)
		{
			if (objState != null)
			{
				objState.setTakingDamage(false);
				objState.setJumping(false);
			}
			
		}
		
		if(tipoFisica.getPosY()>StringAlong.getInstance().height / plano.getPlaneFactor()){
			if (group != null && group.equals("PLAYER")) {
				hp.subtractLife(3);
			}
			else if (group != null && group.equals("FALLINGBLOCK")){
				pWorld.destroyBody(tipoFisica.getBody());
				world.deleteEntity(e);
			}
		}
		
		//Update Timer
		if (tipoFisica.getTimer() != 0) tipoFisica.setTimer(tipoFisica.getTimer() + world.getDelta());
		if (tipoFisica.getTimer() >= 500)
		{
			tipoFisica.setVy(1500);
			tipoFisica.setTimer(0);
		}
		
		if(group != null && group.equals("FALLINGBLOCK")){
			if ((tipoFisica.isTouching()&TipoFisica.UP) == TipoFisica.UP){
				//Start Timer
				if (tipoFisica.getTimer() == 0) tipoFisica.setTimer(tipoFisica.getTimer() + world.getDelta());
			}
		}
	}
	
	@Override 
	protected void end ()
	{
		pWorld.step(world.getDelta() / 1000.0f, 10, 10);
	}

	@Override
	public void beginContact(Contact contact) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		String groupIdA = world.getGroupManager().getGroupOf(entityA);
		String groupIdB = world.getGroupManager().getGroupOf(entityB);
		
		TipoFisica tfA = entityA.getComponent(TipoFisica.class);
		TipoFisica tfB = entityB.getComponent(TipoFisica.class);
		
		if (groupIdA != null && groupIdB  != null)
		{
			disableContact(contact, groupIdA, groupIdB, "WEAPON", "ENEMY");
			disableContact(contact, groupIdA, groupIdB, "PICKUP", "ENEMY");
			disableContact(contact, groupIdA, groupIdB, "PICKUP", "PLAYER");
			
			if (groupIdA.equals("PLAYER") && groupIdB.equals("PICKUP"))
				getPickUp (entityB);
			else if (groupIdB.equals("PLAYER") && groupIdA.equals("PICKUP"))
				getPickUp (entityA);
			
//			if (groupIdA.equals("WEAPON") && groupIdB.indexOf("ENEMY") != -1)
//				setTarget (entityA, entityB);
//			else if (groupIdB.equals("WEAPON") && groupIdA.indexOf("ENEMY") != -1)
//				setTarget (entityB, entityA);		
			
			if (groupIdA.equals("MOVINGPLATFORM") && groupIdB.equals("PLAYER") && contact.getManifold().localNormal.y > 0.0f)
			{
				tfB.setReferentialSpeed(tfA.getVelocidade());
				entityA.getComponent(MovingPlatform.class).addTarget(tfB);
			}
			else if (groupIdB.equals("MOVINGPLATFORM") && groupIdA.equals("PLAYER") && contact.getManifold().localNormal.y < 0.0f)
			{
				tfA.setReferentialSpeed(tfB.getVelocidade());
				entityB.getComponent(MovingPlatform.class).addTarget(tfA);
			}
			
			if (groupIdA.equals("MOVINGPLATFORM") && groupIdB.indexOf("ENEMY") != -1)
				entityA.getComponent(MovingPlatform.class).addTarget(tfB);
			else if (groupIdB.equals("MOVINGPLATFORM") && groupIdA.indexOf("ENEMY") != -1)
				entityB.getComponent(MovingPlatform.class).addTarget(tfA);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		String groupIdA = world.getGroupManager().getGroupOf(entityA);
		String groupIdB = world.getGroupManager().getGroupOf(entityB);
		
		TipoFisica tfA = entityA.getComponent(TipoFisica.class);
		TipoFisica tfB = entityB.getComponent(TipoFisica.class);
		
//		if (world.getGroupManager().getGroupOf(entityA).equals("PLAYER") || world.getGroupManager().getGroupOf(entityB).equals("PLAYER")) System.out.println (contact.getManifold().localNormal.y);
		
		if (contact.isEnabled())
		{	
			removeTouching(tfA, tfB, contact.getManifold().localNormal);
		}
		
		if (groupIdA != null && groupIdB  != null)
		{
//			if (groupIdA.equals("WEAPON") && groupIdB.indexOf("ENEMY") != -1)
//				releaseTarget (entityA, entityB);
//			else if (groupIdB.equals("WEAPON") && groupIdA.indexOf("ENEMY") != -1)
//				releaseTarget (entityB, entityA);

			/*
			 * TODO: Funcao removeReferentialSpeed eh um remendo para setReferentialSpeed (0, 0) que nao estava funcionando devido a sua logica interna
			 * O foco do problema e que o endContact esta sendo chamado atrasado - investigar isso =/
			 */
			
			if (groupIdA.equals("MOVINGPLATFORM") && (groupIdB.equals("PLAYER") || groupIdB.indexOf("ENEMY") != -1))
			{
				entityA.getComponent(MovingPlatform.class).removeTarget(tfB);
				tfB.removeReferentialSpeed();
			}
			else if (groupIdB.equals("MOVINGPLATFORM") && (groupIdA.equals("PLAYER") || groupIdA.indexOf("ENEMY") != -1))
			{
				entityB.getComponent(MovingPlatform.class).removeTarget(tfA);
				tfA.removeReferentialSpeed();
			}
			
			if (groupIdA.equals("MOVINGPLATFORM") && groupIdB.indexOf("ENEMY") != -1)
			{
				entityA.getComponent(MovingPlatform.class).removeTarget(tfB);
				tfB.removeReferentialSpeed();
			}
			else if (groupIdB.equals("MOVINGPLATFORM") && groupIdA.indexOf("ENEMY") != -1)
			{
				entityB.getComponent(MovingPlatform.class).removeTarget(tfA);
				tfA.removeReferentialSpeed();
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {	
		Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
		
		String groupIdA = world.getGroupManager().getGroupOf(entityA);
		String groupIdB = world.getGroupManager().getGroupOf(entityB);

		TipoFisica tfA = entityA.getComponent(TipoFisica.class);
		TipoFisica tfB = entityB.getComponent(TipoFisica.class);
		
		if (groupIdA != null && groupIdB != null)
		{
			disableContact(contact, groupIdA, groupIdB, "PLAYER", "ENEMY");
			disableContact(contact, groupIdA, groupIdB, "PLAYER", "TOCOBLOCK");
			
			if (groupIdA.equals("PLAYER") && groupIdB.indexOf("ENEMY") != -1)
			{
				playerEnemyCollision(contact.getManifold().localNormal.y, groupIdB, entityB, entityA, tfB, tfA);
			}
			
			else if (groupIdB.equals("PLAYER") && groupIdA.indexOf("ENEMY") != -1)
			{
				playerEnemyCollision(-contact.getManifold().localNormal.y, groupIdA, entityA, entityB, tfA, tfB);
			}
			
			if (groupIdA.equals("PLAYER") && groupIdB.equals("MOVINGPLATFORM"))
				testPlayerSmashed (entityA, tfA);
			else if (groupIdB.equals("PLAYER") && groupIdA.equals("MOVINGPLATFORM"))
				testPlayerSmashed (entityB, tfB);
			
			if (groupIdA.equals("PLAYER") && (groupIdB.equals("PASSABLEBLOCK") || groupIdB.equals("FALLINGBLOCK"))){
				if (tfA.getVelocidade().y < 0){
					disableContact(contact, groupIdA, groupIdB, "PLAYER", "PASSABLEBLOCK");
				}
			}else if (groupIdB.equals("PLAYER") && (groupIdA.equals("PASSABLEBLOCK") || groupIdA.equals("FALLINGBLOCK"))){
				if (tfB.getVelocidade().y < 0){
					disableContact(contact, groupIdA, groupIdB, "PLAYER", "PASSABLEBLOCK");
				}
			}
		}
		
		if (contact.isEnabled())
		{	
			removeTouching (tfA, tfB, oldManifold.localNormal);
			addTouching (tfA, tfB, contact.getManifold().localNormal);
		}
	}
	private void playerEnemyCollision(float touchDirection, String enemyGroupId, 
			Entity enemy, Entity player, TipoFisica tfEnemy, TipoFisica tfPlayer) 
	{
		ObjectState osEnemy = enemy.getComponent(ObjectState.class);
		ObjectState osPlayer = player.getComponent(ObjectState.class);
		
		if (!osEnemy.isDying() && !osPlayer.isDying())
		{
			if (enemyGroupId.indexOf("TOCO") != -1 && touchDirection < 0.0f)
			{
				if (!osPlayer.isTakingDamage() && tfPlayer.getVelocidade().y > 0)
				{
					damageEnemy (enemy, tfEnemy);
					tfPlayer.setVy((float) - Math.sqrt(2 * GRAVITY_Y * (StringAlong.keyPressedFor(KeyEvent.VK_SPACE, 200) ? 150 : 75)));
				}
			}
			else
			{
				damagePlayer (player, tfPlayer);
			}
		}
	}
	
	private void addTouching (TipoFisica tfA, TipoFisica tfB, Vec2 localNormal)
	{
		if (localNormal.x < 0.0f)
		{
			tfA.addTouchingDirection(TipoFisica.LEFT);
			tfB.addTouchingDirection(TipoFisica.RIGHT);
		}
		
		else if (localNormal.x > 0.0f)
		{
			tfA.addTouchingDirection(TipoFisica.RIGHT);
			tfB.addTouchingDirection(TipoFisica.LEFT);
		}
		
		if (localNormal.y < 0.0f)
		{
			tfA.addTouchingDirection(TipoFisica.DOWN);
			tfB.addTouchingDirection(TipoFisica.UP);
		}
		
		else if (localNormal.y > 0.0f)
		{
			tfA.addTouchingDirection(TipoFisica.UP);
			tfB.addTouchingDirection(TipoFisica.DOWN);
		}
	}
	
	private void removeTouching (TipoFisica tfA, TipoFisica tfB, Vec2 localNormal)
	{
		if (localNormal.x < 0.0f)
		{
			tfA.removeTouchingDirection(TipoFisica.LEFT);
			tfB.removeTouchingDirection(TipoFisica.RIGHT);
		}
		
		else if (localNormal.x > 0.0f)
		{
			tfA.removeTouchingDirection(TipoFisica.RIGHT);
			tfB.removeTouchingDirection(TipoFisica.LEFT);
		}
		
		if (localNormal.y < 0.0f)
		{
			tfA.removeTouchingDirection(TipoFisica.DOWN);
			tfB.removeTouchingDirection(TipoFisica.UP);
		}
		
		else if (localNormal.y > 0.0f)
		{
			tfA.removeTouchingDirection(TipoFisica.UP);
			tfB.removeTouchingDirection(TipoFisica.DOWN);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
	}
	
	private void getPickUp (Entity e)
	{
		PlayState.pickUpMapper.get(e).setPegou(true);
	}
	
//	private void setTarget (Entity eA, Entity eB)
//	{
//		PlayState.weaponMapper.get(eA).addTarget(eB);
//	}
//	
//	private void releaseTarget (Entity eA, Entity eB)
//	{
//		PlayState.weaponMapper.get(eA).removeTarget(eB);
//	}
	
	private void testPlayerSmashed(Entity e, TipoFisica tf) {
		if ( ((tf.isTouching() & TipoFisica.UP) == TipoFisica.UP &&
				(tf.isTouching() & TipoFisica.DOWN) == TipoFisica.DOWN) ||
				((tf.isTouching() & TipoFisica.LEFT) == TipoFisica.RIGHT &&
				(tf.isTouching() & TipoFisica.RIGHT) == TipoFisica.LEFT) )
		{
			e.getComponent(HP.class).subtractLife(3);
		}
	}
	
	private void damagePlayer (Entity e, TipoFisica tipoFisica)
	{
		HP hp = PlayState.HPMapper.get(e);
		ObjectState objState = PlayState.objectStateMapper.get(e);
		
		if (!hp.isInvincible())
		{
			hp.subtractLife(1);
			hp.setInvincible(1000);
			
			if (hp.getHP() > 0)
			{
				if (tipoFisica.getVx() > 0)
				{
					tipoFisica.setKnockbackSpeed(-5 * TipoFisica.KNOCK_BACK_DECREASE);
				}
				else if (tipoFisica.getVx() < 0)
				{
					tipoFisica.setKnockbackSpeed(5 * TipoFisica.KNOCK_BACK_DECREASE);
				}
				else
				{
					double testYourLuck = Math.random();
					if (testYourLuck <= 0.5)
						tipoFisica.setKnockbackSpeed(-5 * TipoFisica.KNOCK_BACK_DECREASE);
					else
						tipoFisica.setKnockbackSpeed(5 * TipoFisica.KNOCK_BACK_DECREASE);
				}
				
				tipoFisica.setVy((float) - Math.sqrt(2 * GRAVITY_Y * 75));
				objState.setTakingDamage(true);
			}
		}
	}
	
	private void damageEnemy (Entity e, TipoFisica tipoFisica)
	{
		HP hp = PlayState.HPMapper.get(e);
		hp.subtractLife(1);
	}
	
	private void disableContact(Contact contact, String groupIdA,
			String groupIdB, String groupA, String groupB) {
		
		if (groupIdA.indexOf(groupA) != -1 && groupIdB.indexOf(groupB) != -1||
				groupIdA.indexOf(groupB) != -1 && groupIdB.indexOf(groupA) != -1 )
		{
			contact.setEnabled(false);
		}
		
	}
}
