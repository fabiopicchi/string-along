package systems;

import managers.ResourceManager;
import states.PlayState;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.Animacao;
import components.ObjectState;
import components.TipoFisica;
import components.Andar;
import components.VaiVolta;

public class AISystem extends EntityProcessingSystem
{
	
	@SuppressWarnings("unchecked")
	public AISystem ()
	{
		super (TipoFisica.class);
	}

	@Override
	protected void initialize ()
	{
	}

	@Override
	public void added (Entity e)
	{	
		TipoFisica varTipoFisica = PlayState.tipoFisicaMapper.get(e);
		Andar varAndar = PlayState.andarMapper.get(e);
		VaiVolta varVaiVolta= PlayState.vaiVoltaMapper.get(e);
		
		if (varAndar != null)
		{
			varTipoFisica.setVx(200.0f);
		}
		else if (varVaiVolta != null)
		{
			varVaiVolta.setPosicaoInicial(varTipoFisica.getPosition());
		}
	}

	@Override
	protected void process(Entity e)
	{
		TipoFisica varTipoFisica = PlayState.tipoFisicaMapper.get(e);
		Andar varAndar = PlayState.andarMapper.get(e);
		VaiVolta varVaiVolta= PlayState.vaiVoltaMapper.get(e);
		ObjectState varObjectState = PlayState.objectStateMapper.get(e);
		Animacao varAnimacao = PlayState.animacaoMapper.get(e);
				
		if (varAndar != null)
		{
			if ((varTipoFisica.isTouching() & TipoFisica.LEFT) == TipoFisica.LEFT){
				varObjectState.setFacing(1);
				varTipoFisica.setVx(200.0f);
			}
			else if ((varTipoFisica.isTouching() & TipoFisica.RIGHT) == TipoFisica.RIGHT){
				varObjectState.setFacing(0);
				varTipoFisica.setVx(-200.0f);
			}
		}
		
		//TODO : Tirar lógica do VaiVolta de dentro do componente
		
		else if (varVaiVolta != null)
		{
			varVaiVolta.updateTimer(world.getDelta());
			if (varVaiVolta.isStarted())
			{
				if (varVaiVolta.getDirecao().equals("ver")){
					if (varVaiVolta.isVai())
						varTipoFisica.setVy(((varVaiVolta.getPosicaoInicial() - varVaiVolta.getvVai() * varVaiVolta.getTimer()) - varTipoFisica.getPosY()) / (world.getDelta() / 1000.0f));
					else
						varTipoFisica.setVy(((varVaiVolta.getPosicaoInicial() - varVaiVolta.getAltura() + varVaiVolta.getvVolta() * (varVaiVolta.getTimer() - varVaiVolta.getProporcao() * varVaiVolta.getIntervalo())) - varTipoFisica.getPosY()) / (world.getDelta() / 1000.0f));
						if(varAnimacao != null && varAnimacao.getFileName().indexOf(ResourceManager.SPIDER_ID) != -1 && varVaiVolta.getTimer()==varVaiVolta.getIntervalo()*varVaiVolta.getProporcao()){
							PlayState.soundSystem.play("aranha");
						}
				}else if (varVaiVolta.getDirecao().equals("hor")){
					if (varVaiVolta.isVai())
						varTipoFisica.setVx(((varVaiVolta.getPosicaoInicial() + varVaiVolta.getvVai() * varVaiVolta.getTimer()) - varTipoFisica.getPosX()) / (world.getDelta() / 1000.0f));
					else
						varTipoFisica.setVx(((varVaiVolta.getPosicaoInicial() + varVaiVolta.getAltura() - varVaiVolta.getvVolta() * (varVaiVolta.getTimer() - varVaiVolta.getProporcao() * varVaiVolta.getIntervalo())) - varTipoFisica.getPosX()) / (world.getDelta() / 1000.0f));
				}
			}
		}
	}
}