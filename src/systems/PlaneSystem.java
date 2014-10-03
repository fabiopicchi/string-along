package systems;

import org.jbox2d.common.Vec2;

import states.PlayState;
import utils.ChangeCoordinates;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.Plano;
import components.Posicao;
import components.Retangulo;
import components.TipoFisica;

public class PlaneSystem extends EntityProcessingSystem {
	
	private float velTrocaPlano = 5000.0f;
	
	@SuppressWarnings("unchecked")
	public PlaneSystem() 
	{
		super(Plano.class);
	}
	
	@Override
	protected void initialize ()
	{
	}
	
	@Override
	protected void added (Entity e) {
		Plano plano = PlayState.planoMapper.get(e);
		String group = world.getGroupManager().getGroupOf(e);
		
		if (group != null && group.equals("PLAYER"))
			plano.setChanging(Plano.ARRIVING);
	}

	@Override
	protected void process(Entity e) {
		Plano plano = PlayState.planoMapper.get(e);
		TipoFisica tipoFisica = PlayState.tipoFisicaMapper.get(e);
		Posicao posicao = PlayState.posicaoMapper.get(e);
		Retangulo retangulo = PlayState.retanguloMapper.get(e);
		
		if ((plano.getChanging() & Plano.CHANGING_BACKWARD) == Plano.CHANGING_BACKWARD &&
				(plano.getChanging() & Plano.ARRIVING) != Plano.ARRIVING)
		{
			if (plano.getPlano() < 3)
			{
				if (!PlayState.renderSystem.isOnScreen(plano, posicao, retangulo, null))
				{
					float previousPlaneFactor = plano.getPlaneFactor();
					plano.setPlano(plano.getPlano() + 1);
					Vec2 posicaoAtual = tipoFisica.getPosition();
					tipoFisica.getBody().setTransform(ChangeCoordinates.screenToPhysics(new Vec2 (posicaoAtual.x,
							posicaoAtual.y * previousPlaneFactor/ plano.getPlaneFactor())), tipoFisica.getBody().getAngle());
					tipoFisica.setVy(velTrocaPlano);
					tipoFisica.resetTouching();
					plano.setChanging(Plano.ARRIVING);
				}
				else
				{
					tipoFisica.setVy(-velTrocaPlano);
					if ((tipoFisica.isTouching() & TipoFisica.UP) == TipoFisica.UP)
					{
						tipoFisica.setVy(velTrocaPlano);
						plano.setChanging(Plano.ARRIVING);
						PlayState.soundSystem.play("baternoteto");
						world.getSystemManager().getSystem(RenderSystem.class).setShakePlane(plano.getPlano());
					}
				}
			}
			else
				plano.reset();
		}
		
		else if ((plano.getChanging() & Plano.CHANGING_FOWARD) == Plano.CHANGING_FOWARD &&
				(plano.getChanging() & Plano.ARRIVING) != Plano.ARRIVING)
		{
			if (plano.getPlano() > 1)
			{
				if (!PlayState.renderSystem.isOnScreen(plano, posicao, retangulo, null))
				{
					float previousPlaneFactor = plano.getPlaneFactor();
					plano.setPlano(plano.getPlano() - 1);
					Vec2 posicaoAtual = tipoFisica.getPosition();
					tipoFisica.getBody().setTransform(ChangeCoordinates.screenToPhysics(new Vec2 (posicaoAtual.x,
							posicaoAtual.y * previousPlaneFactor/ plano.getPlaneFactor())), tipoFisica.getBody().getAngle());
					tipoFisica.setVy(velTrocaPlano);
					tipoFisica.resetTouching();
					plano.setChanging(Plano.ARRIVING);
				}
				else
				{
					tipoFisica.setVy(-velTrocaPlano);
					if ((tipoFisica.isTouching() & TipoFisica.UP) == TipoFisica.UP)
					{
						tipoFisica.setVy(velTrocaPlano);
						plano.setChanging(Plano.ARRIVING);
						PlayState.soundSystem.play("baternoteto");
						world.getSystemManager().getSystem(RenderSystem.class).setShakePlane(plano.getPlano());
					}
				}
			}
			else
				plano.reset();
		}
		
		if ((plano.getChanging() & Plano.ARRIVING) == Plano.ARRIVING)
		{
			if ((tipoFisica.isTouching() & TipoFisica.DOWN) == TipoFisica.DOWN)
			{
				if (!world.getSystemManager().getSystem(RenderSystem.class).isRetanguloOnScreen(plano, posicao, retangulo))
				{
					if ((plano.getChanging() & Plano.CHANGING_FOWARD) == Plano.CHANGING_FOWARD)
					{
						plano.reset();
						plano.setChanging(Plano.CHANGING_BACKWARD);
						world.getSystemManager().getSystem(RenderSystem.class).setShakePlane(plano.getPlano());
						PlayState.soundSystem.play("baternoteto");
					}
					else if ((plano.getChanging() & Plano.CHANGING_BACKWARD) == Plano.CHANGING_BACKWARD)
					{
						plano.reset();
						plano.setChanging(Plano.CHANGING_FOWARD);
						world.getSystemManager().getSystem(RenderSystem.class).setShakePlane(plano.getPlano());
						PlayState.soundSystem.play("baternoteto");
					}
				}
				else
				{
					plano.reset();
				}
			}
		}
		
//		if (plano.getChanging() == Plano.CHANGING_BACKWARD && plano.getPlano() < 3)
//		{
//			plano.setPlano(plano.getPlano() + 1);
//		}
//		else if (plano.getChanging() == Plano.CHANGING_FOWARD && plano.getPlano() > 1)
//		{
//			plano.setPlano(plano.getPlano() - 1);
//		}
//
//		plano.setChanging(Plano.NOT_CHANGING);
	}

}
