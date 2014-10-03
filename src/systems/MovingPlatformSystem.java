package systems;

import java.util.Vector;

import org.jbox2d.common.Vec2;

import states.PlayState;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import components.MovingPlatform;
import components.TipoFisica;

public class MovingPlatformSystem extends EntityProcessingSystem {
	@SuppressWarnings("unchecked")
	public MovingPlatformSystem ()
	{
		super (MovingPlatform.class);
	}
	
	@Override
	public void initialize ()
	{
	}
	
	@Override
	protected void process(Entity e) {
		MovingPlatform varMovingPlatform = PlayState.movingPlatformMapper.get(e);
		TipoFisica varTipoFisica = PlayState.tipoFisicaMapper.get(e);
		Vector <TipoFisica> removed = new Vector <TipoFisica> ();
		
		for (TipoFisica tf : varMovingPlatform.getTargets())
		{
			if (!tf.isRemovedReferentialSpeed())
			{
				if (tf.getReferentialSpeed().x != varTipoFisica.getVelocidade().x)
					tf.setReferentialSpeed(new Vec2 (varTipoFisica.getVelocidade().x, tf.getReferentialSpeed().y));
				if (tf.getReferentialSpeed().y != varTipoFisica.getVelocidade().y)
					tf.setReferentialSpeed(new Vec2 (tf.getReferentialSpeed().x, varTipoFisica.getVelocidade().y));
			}
			else
			{
				removed.add(tf);
			}
		}
		
		for (TipoFisica tf : removed)
		{
			varMovingPlatform.removeTarget(tf);
		}
	}

}
