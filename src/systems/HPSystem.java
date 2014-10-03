package systems;

import managers.ResourceManager;

import org.jbox2d.common.Vec2;

import states.PlayState;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.Animacao;
import components.HP;
import components.ObjectState;
import components.Plano;
import components.Posicao;
import components.Retangulo;

public class HPSystem extends EntityProcessingSystem
{
	@SuppressWarnings("unchecked")
	public HPSystem ()
	{
		super (HP.class,ObjectState.class);
	}

	@Override
	protected void process(Entity e)
	{
		HP varHP = PlayState.HPMapper.get(e);
		ObjectState varObjectState = PlayState.objectStateMapper.get(e);
		Posicao varPosicao = PlayState.posicaoMapper.get(e);
		Plano varPlano = PlayState.planoMapper.get(e);
		Retangulo varRetangulo = PlayState.retanguloMapper.get(e);
		Animacao varAnimacao = PlayState.animacaoMapper.get(e);
		
		if(varHP.getHP()<=0){
			if(varAnimacao.getFileName().indexOf(ResourceManager.TRUNK_ID) != -1 && varObjectState.isDying()==false){
				PlayState.soundSystem.play("tocomorrendo");
			}
			varObjectState.setDying(true);
		}
		
		if (varHP.isInvincible())
		{
			varHP.setTimer(varHP.getTimer() + world.getDelta());
			if (varHP.getTimer() >= varHP.getInvincibilityDuration())
			{
				varHP.endInvincibility();
				varHP.setTimer(0);
			}
		}
		
		//Remove strings to the lost HP
		for (int i = varHP.getLostHP(); i > 0; i--)
		{
			world.deleteEntity(varHP.getTag(varHP.getHP() - 1 + i));
			varHP.removeTag(varHP.getTag(varHP.getHP() - 1 + i));
			if(varAnimacao.getFileName().indexOf(ResourceManager.PLAYER_ID) != -1){
				PlayState.soundSystem.play("cordaquebra");
			}
		}
		varHP.resetLostHP();
		
		Entity string;
		for (int i = varHP.getHP() - 1; i >= 0; i--)
		{
			string = varHP.getTag(i);
			if (string != null)
			{ //Nao estamos contabilizando o offsetY aqui
				PlayState.posicaoMapper.get(string).setPosicao(new Vec2 ((varPosicao.getPosicao().x  - varAnimacao.getSpriteWidth() / 2 + varAnimacao.getOffsetX() + (varAnimacao.getSpriteWidth()*varAnimacao.getPoint(String.valueOf(i))[0]))*varPlano.getPlaneFactor(),
						(varPosicao.getPosicao().y  - varAnimacao.getSpriteHeight() + varRetangulo.getHeight() / 2 + (varAnimacao.getSpriteHeight()*varAnimacao.getPoint(String.valueOf(i))[1]))*varPlano.getPlaneFactor()));
				PlayState.planoMapper.get(string).setPlano(varPlano.getPlano());
			}
		}
	}
}