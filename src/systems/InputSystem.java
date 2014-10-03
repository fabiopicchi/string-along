package systems;

import java.awt.event.KeyEvent;

import main.StringAlong;
import processing.core.PApplet;
import states.PlayState;
import utils.ChangeCoordinates;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

import components.Plano;
import components.ObjectState;
import components.TipoFisica;

public class InputSystem extends EntitySystem {
	
//	private static final float CHANGE_PLANE_TOLERANCE = 200.0f;
	private Entity 		player;
	private TipoFisica 	tipoFisicaPlayer;
	private Plano 		planoPlayer;
	private ObjectState playerState;
	private int alturaDoPulo = 215;
	private int velPlayer = 500;
	
	protected void initialize ()
	{
		player = world.getTagManager().getEntity("PLAYER");
		tipoFisicaPlayer = player.getComponent(TipoFisica.class);
		planoPlayer = player.getComponent(Plano.class);
		playerState = player.getComponent(ObjectState.class);
	}
	
	@Override
	protected void processEntities (ImmutableBag<Entity> entities) {
		
		if (!(playerState.isJumping() && playerState.isAttacking()))
		{	
			tipoFisicaPlayer.setVx(0.0f);
		}
		
		if (planoPlayer.getChanging() == Plano.NOT_CHANGING && !playerState.isDying())
		{
//			if (!playerState.isAttacking())
//			{
				if (StringAlong.isKeyPressed(PApplet.LEFT) && planoPlayer.getChanging()==0)
				{
					if (!playerState.isTakingDamage()) playerState.setFacing(0);
					tipoFisicaPlayer.setVx (-velPlayer);
				}
				
				if (StringAlong.isKeyPressed(PApplet.RIGHT) && planoPlayer.getChanging()==0)
				{
					if (!playerState.isTakingDamage()) playerState.setFacing(1);
					tipoFisicaPlayer.setVx(velPlayer);	
				}
//			}
			
			if (!playerState.isTakingDamage())
			{	
				if ((tipoFisicaPlayer.isTouching() & TipoFisica.DOWN) == TipoFisica.DOWN)
				{
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_SPACE))
					{
						tipoFisicaPlayer.removeReferentialSpeed();
						tipoFisicaPlayer.setVy((float) - Math.sqrt(2 * ChangeCoordinates.physicsToScreen(PhysicsSystem.GRAVITY).y * alturaDoPulo));
						playerState.setJumping(true);
					}
					
					if (tipoFisicaPlayer.getVelocidade().y < 0 && StringAlong.isKeyJustReleased(KeyEvent.VK_SPACE))
						tipoFisicaPlayer.setVy(tipoFisicaPlayer.getVy()/2);
				}
				
//				if (!playerState.isAttacking())
//				{
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP)) //&& ((tipoFisicaPlayer.getVy() <= CHANGE_PLANE_TOLERANCE) || (tipoFisicaPlayer.isTouching() & TipoFisica.DOWN) == TipoFisica.DOWN))
						planoPlayer.setChanging(Plano.CHANGING_BACKWARD);
					
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN)) //&& ((tipoFisicaPlayer.getVy() <= CHANGE_PLANE_TOLERANCE) || (tipoFisicaPlayer.isTouching() & TipoFisica.DOWN) == TipoFisica.DOWN))
						planoPlayer.setChanging(Plano.CHANGING_FOWARD);
					
//					if (StringAlong.isKeyJustPressed(KeyEvent.VK_E)||StringAlong.isKeyJustPressed(KeyEvent.VK_D)||StringAlong.isKeyJustPressed(KeyEvent.VK_F))
//						playerState.setAttacking(true);
//				}
			}
		}		
		
		if (StringAlong.isKeyJustPressed(KeyEvent.VK_ESCAPE))
			((PlayState)StringAlong.getInstance().state).gameState = 1;
	}
	
	@Override
	public boolean checkProcessing () {return true;}

}
