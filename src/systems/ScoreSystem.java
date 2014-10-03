package systems;

import main.StringAlong;
import states.PlayState;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.HP;
import components.PickUp;
import components.TipoFisica;
import constants.GameObject;
import factories.EntityFactory;

public class ScoreSystem extends EntityProcessingSystem {
	private int perolasCounter;

	@SuppressWarnings("unchecked")
	public ScoreSystem ()
	{
		super (PickUp.class);
	}
	
	@Override
	public void initialize ()
	{
	}
	
	@Override
	protected void process(Entity e) {		
		PickUp pickUp = PlayState.pickUpMapper.get(e);
		if (pickUp.isPegou()){
			String code = pickUp.getCode();
			if (code.equals(GameObject.PEROLA.getCode())){
				perolasCounter++;
				PlayState.soundSystem.play("pickup");
			}
			if (code.equals(GameObject.CARRETEL.getCode())){
				Entity player = world.getTagManager().getEntity("PLAYER");
				HP playerHP = player.getComponent(HP.class);
				if (playerHP.getHP() < playerHP.getHPmax())
				{
					EntityFactory.createString(world, player.getId(), playerHP, playerHP.getHP());
					playerHP.addLife(1);
//					PlayState.soundSystem.play("carretel");
				}
			}
			if (code.equals(GameObject.PARTE1.getCode()) ||
					code.equals(GameObject.PARTE2.getCode()) ||
					code.equals(GameObject.PARTE3.getCode()))
			{
				((PlayState)StringAlong.getInstance().state).gameState = 2;
			}
			(PlayState.physicsSystem).getpWorld().destroyBody(e.getComponent(TipoFisica.class).getBody());
			world.deleteEntity(e);
		}
	}

	public int getPerolasCounter() {
		return perolasCounter;
	}
	

}
