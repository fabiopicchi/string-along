package systems;

import org.jbox2d.common.Vec2;

import states.PlayState;
import utils.ChangeCoordinates;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.ObjectState;
import components.Plano;
import components.Retangulo;
import components.Weapon;
import components.TipoFisica;

public class WeaponSystem extends EntityProcessingSystem 
{
	private PhysicsSystem ps;
	
	@SuppressWarnings("unchecked")
	public WeaponSystem ()
	{
		super (Weapon.class);
	}

	@Override
	protected void initialize ()
	{
		ps = (PlayState.physicsSystem);
	}

	@Override
	protected void process(Entity e) 
	{
		TipoFisica varTipoFisica = PlayState.tipoFisicaMapper.get(e);
		Weapon varWeapon = PlayState.weaponMapper.get(e);
		Plano varPlano = PlayState.planoMapper.get(e);
		Retangulo varRetangulo = PlayState.retanguloMapper.get(e);
		
		varTipoFisica.getBody().applyForce(new Vec2 (ps.getpWorld().getGravity().x, - ps.getpWorld().getGravity().y), varTipoFisica.getBody().getPosition());
		
		if (varWeapon.getWielder().getComponent(ObjectState.class).getFacing() == 1)
		{
			varTipoFisica.getBody().setTransform(ChangeCoordinates.screenToPhysics(new Vec2 (varWeapon.getWielder().getComponent(TipoFisica.class).getPosX() + varRetangulo.getWidth() / 2.0f,
														varWeapon.getWielder().getComponent(TipoFisica.class).getPosY() + varWeapon.getWielder().getComponent(Retangulo.class).getHeight()/2.0f - varRetangulo.getHeight()/2.0f)),  0.0f);
			varTipoFisica.setVelocidade(varWeapon.getWielder().getComponent(TipoFisica.class).getVelocidade());
		}
		else if (varWeapon.getWielder().getComponent(ObjectState.class).getFacing() == 0)
		{
			varTipoFisica.getBody().setTransform(ChangeCoordinates.screenToPhysics(new Vec2 (varWeapon.getWielder().getComponent(TipoFisica.class).getPosX() + varRetangulo.getWidth() / 2.0f - varRetangulo.getWidth(),
					varWeapon.getWielder().getComponent(TipoFisica.class).getPosY() + varWeapon.getWielder().getComponent(Retangulo.class).getHeight()/2.0f - varRetangulo.getHeight()/2.0f)),  0.0f);
			varTipoFisica.setVelocidade(varWeapon.getWielder().getComponent(TipoFisica.class).getVelocidade());
		}
		
		varPlano.setPlano(varWeapon.getWielder().getComponent(Plano.class).getPlano());
		
		if (varWeapon.getWielder().getComponent(ObjectState.class).isAttacking())
		{
			if (varWeapon.getTargetList().size() > 0  && !varWeapon.isAwatingCoolDown())
			{
				for (int i = 0; i < varWeapon.getTargetList().size(); i++)
				{
					/*
					 * TODO: Jogar a animacao da corda no AnimationSystem
					 */
					PlayState.HPMapper.get(varWeapon.getTargetList().get(i)).subtractLife(1);
				}
				varWeapon.setAwatingCoolDown(true);
			}
		}
		else
		{
			varWeapon.setActive(false);
			varWeapon.setAwatingCoolDown(false);
		}
	}

}
