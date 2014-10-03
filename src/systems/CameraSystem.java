package systems;

import main.StringAlong;

import org.jbox2d.common.Vec2;

import states.PlayState;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

import components.Plano;
import components.Retangulo;
import components.TipoFisica;

public class CameraSystem extends EntitySystem {

	public Vec2 cameraPosition;
	
	private Vec2 playerPosition;
	private Vec2 boundaries;
	private Entity focus;
	
//	private PlayState playState;
	
	public CameraSystem ()
	{
//		playState = (PlayState) StringAlong.getInstance().state;
	}
	
	@Override
	protected void initialize ()
	{
		boundaries = new Vec2 ();
		boundaries.x = PlayState.stageLoader.getMapWidth() * PlayState.stageLoader.getTileWidth() * 86.0f / 32.0f + StringAlong.getInstance().width / 2;
		boundaries.y = PlayState.stageLoader.getMapHeight() * PlayState.stageLoader.getTileHeight() * 86.0f / 32.0f;
		focus = world.getTagManager().getEntity("PLAYER");
	}
	
	@Override
	protected void processEntities (ImmutableBag <Entity> entities) {
		playerPosition = focus.getComponent(TipoFisica.class).getPosition();
		
		cameraPosition = new Vec2 (-playerPosition.x, 0);
		
		if (boundaries.x - StringAlong.getInstance().width < 0)
			cameraPosition.x = -(boundaries.x - StringAlong.getInstance().width);
		
		else
		{
			if (cameraPosition.x > 0) cameraPosition.x = 0;
			else if (cameraPosition.x < - (boundaries.x - StringAlong.getInstance().width)) cameraPosition.x = - (boundaries.x - StringAlong.getInstance().width);
		}
	}
	
	@Override
	protected boolean checkProcessing ()
	{
		return true;
	}
	
	public boolean isPlayerOnScreen ()
	{
		if (cameraPosition == null)
			return true;
		
		Retangulo playerBox = focus.getComponent(Retangulo.class);
		Plano playerPlane = focus.getComponent(Plano.class);
		
		return ((playerPosition.x + cameraPosition.x) * playerPlane.getPlaneFactor() <= StringAlong.getInstance().width &&
				(playerPosition.x + playerBox.getWidth() + cameraPosition.x) * playerPlane.getPlaneFactor() >= 0  &&
				(playerPosition.y * playerPlane.getPlaneFactor() + cameraPosition.y) <= StringAlong.getInstance().height &&
				((playerPosition.y + playerBox.getHeight()) * playerPlane.getPlaneFactor() + cameraPosition.y) >= 0);
	}

}
