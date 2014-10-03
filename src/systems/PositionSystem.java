package systems;

import states.PlayState;
import utils.ChangeCoordinates;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import components.Posicao;
import components.TipoFisica;

public class PositionSystem extends EntityProcessingSystem {
	
	@SuppressWarnings("unchecked")
	public PositionSystem ()
	{
		super (Posicao.class, TipoFisica.class);
	}
	
	
	protected void initialize ()
	{
	}
	
	
	@Override
	protected void process(Entity e) {
		Posicao posicao = PlayState.posicaoMapper.get (e);
		TipoFisica tipoFisica = PlayState.tipoFisicaMapper.get (e);

		posicao.setPosicao (ChangeCoordinates.physicsToScreen (tipoFisica.getBody().getPosition()));
	}

}
