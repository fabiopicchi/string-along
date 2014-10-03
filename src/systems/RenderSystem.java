package systems;

import java.util.Vector;

import main.StringAlong;
import managers.ResourceManager;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.artemis.utils.Bag;

import components.Animacao;
import components.PickUp;
import components.Plano;
import components.Posicao;
import components.Retangulo;
import components.Image;

import processing.core.*;
import states.PlayState;

public class RenderSystem extends EntityProcessingSystem {
	
	private static final int SHAKE_MAX = 30;

	private PApplet parent;
	
	private CameraSystem cameraSystem;
	
	private Bag <Entity> front;
	private Bag <Entity> middle;
	private Bag <Entity> back;
	private Bag <Entity> frontHP;
	private Bag <Entity> middleHP;
	private Bag <Entity> backHP;
	private Bag <Entity> frontCenario;
	private Bag <Entity> middleCenario;
	private Bag <Entity> backCenario;
	Vector <String> usedResources;
	Vector <String> removedImages;
	Vector <String> removedAnimations;
	private int shakePlane = 0;

	public void setShakePlane(int i) {
		shakePlane = i;
	}

	private float shakePhase = 0;
	
	@SuppressWarnings("unchecked")
	public RenderSystem ()
	{
		super (Posicao.class);
		
		this.parent = StringAlong.getInstance();
	}
	
	@Override
	public void initialize ()
	{
		cameraSystem = (PlayState.cameraSystem);
	}
	
	@Override
	public void begin ()
	{
		front = new Bag <Entity> ();
		middle = new Bag <Entity> ();
		back = new Bag <Entity> ();
		frontHP = new Bag <Entity> ();
		middleHP = new Bag <Entity> ();
		backHP = new Bag <Entity> ();
		frontCenario = new Bag <Entity> ();
		middleCenario = new Bag <Entity> ();
		backCenario = new Bag <Entity> ();
		usedResources = new Vector <String> ();
		removedImages = new Vector <String> ();
		removedAnimations = new Vector <String> ();
		parent.noStroke();
	}
	
	@Override	
	protected void process(Entity e) {
		Plano plano = PlayState.planoMapper.get(e);
		Posicao posicao = PlayState.posicaoMapper.get(e);
		Retangulo retangulo = PlayState.retanguloMapper.get(e);
		Image image = PlayState.imageMapper.get(e);
		Animacao animacao = PlayState.animacaoMapper.get(e);
		String group = world.getGroupManager().getGroupOf(e);
		
		if (plano != null || group.equals("BLOCK") || group.equals("FALLINGBLOCK") || group.equals("PASSABLEBLOCK") || group.equals("MOVINGPLATFORM"))
		{
			if (isOnScreen (plano, posicao, retangulo, image))
			{
				if (image != null)
				{
					if (!usedResources.contains(image.getFileName())) usedResources.add(image.getFileName());
					ResourceManager.loadImage(image.getFileName());
				} 
				else if (animacao != null)
				{
					if (!usedResources.contains(animacao.getFileName())) usedResources.add(animacao.getFileName());
					ResourceManager.loadImage(animacao.getFileName());
				}
				if ((image != null && !image.isScalable()))
				{
					if (plano.getPlano() == 1)
						frontCenario.add(e);
					if (plano.getPlano() == 2)
						middleCenario.add(e);
					if (plano.getPlano() == 3)
						backCenario.add(e);
				}
				else if (retangulo != null)
				{
					if (plano.getPlano() == 1)
						front.add(e);
					if (plano.getPlano() == 2)
						middle.add(e);
					if (plano.getPlano() == 3)
						back.add(e);
				}
				else
				{
					if (plano.getPlano() == 1)
						frontHP.add(e);
					if (plano.getPlano() == 2)
						middleHP.add(e);
					if (plano.getPlano() == 3)
						backHP.add(e);
				}
			}
			else
			{
				if (image != null)
				{
					if (!removedImages.contains(image.getFileName())) removedImages.add(image.getFileName());
				} 
				else if (animacao != null)
				{
					if (!removedAnimations.contains(animacao.getFileName())) removedAnimations.add(animacao.getFileName());
				}
			}
		}
	}
	
	public boolean isOnScreen (Plano plano, Posicao posicao, Retangulo retangulo, Image image)
	{
		if (image != null && !image.isScalable() && !image.isCenterTile())
		{
			return this.isImageOnScreen(plano, posicao, retangulo);
		}
		else
			return this.isRetanguloOnScreen(plano, posicao, retangulo);
	}
	
	public boolean isRetanguloOnScreen (Plano plano, Posicao posicao, Retangulo retangulo)
	{
		if (retangulo == null || posicao == null || plano == null) return true;
		
		return ((posicao.getPosicao().x - retangulo.getWidth() /2 + cameraSystem.cameraPosition.x) * plano.getPlaneFactor() <= StringAlong.getInstance().width &&
				(posicao.getPosicao().x + retangulo.getWidth() / 2 + cameraSystem.cameraPosition.x) * plano.getPlaneFactor() >= - StringAlong.getInstance().width &&
				((posicao.getPosicao().y - retangulo.getHeight() / 2) * plano.getPlaneFactor() + cameraSystem.cameraPosition.y) <= StringAlong.getInstance().height &&
				((posicao.getPosicao().y + retangulo.getHeight() / 2) * plano.getPlaneFactor() + cameraSystem.cameraPosition.y) >= 0);
	}
	
	public boolean isImageOnScreen (Plano plano, Posicao posicao, Retangulo retangulo)
	{
		return ((posicao.getPosicao().x + cameraSystem.cameraPosition.x * plano.getPlaneFactor()) <= StringAlong.getInstance().width &&
				(posicao.getPosicao().x + retangulo.getWidth() + cameraSystem.cameraPosition.x * plano.getPlaneFactor()) >= - StringAlong.getInstance().width &&
				((posicao.getPosicao().y) + cameraSystem.cameraPosition.y * plano.getPlaneFactor()) <= StringAlong.getInstance().height &&
				((posicao.getPosicao().y + retangulo.getHeight()) + cameraSystem.cameraPosition.y * plano.getPlaneFactor()) >= 0);
	}
	
	@Override
	protected void end ()
	{	
		for (int i = 0; i < removedImages.size(); i++)
		{
			if (!usedResources.contains(removedImages.get(i)))
			{
				ResourceManager.unloadImage(removedImages.get(i));
			}
		}
		
		//Carregar as spriteSheets é custoso
//		for (int i = 0; i < removedAnimations.size(); i++)
//		{
//			if (!usedResources.contains(removedAnimations.get(i)))
//			{
//				ResourceManager.unloadAnimation(removedAnimations.get(i));
//			}
//		}
		
		if (shakePlane != 0)
		{
			shakePhase += 1;
		}
		if (shakePhase >= SHAKE_MAX)
		{
			shakePlane = 0;
			shakePhase = 0;
		}

		if (shakePlane == 3) renderPlane (backCenario, true);
		else renderPlane (backCenario, false);
		if (shakePlane == 3) renderPlane (backHP, true);
		else renderPlane (backHP, false);
		if (shakePlane == 3) renderPlane (back, true);
		else renderPlane (back, false);
		//parent.image(ResourceManager.levelLayer3, 0, 0);

		if (shakePlane == 2) renderPlane (middleCenario, true);
		else renderPlane (middleCenario, false);
		if (shakePlane == 2) renderPlane (middleHP, true);
		else renderPlane (middleHP, false);
		if (shakePlane == 2) renderPlane (middle, true);
		else renderPlane (middle, false);
		//parent.image(ResourceManager.levelLayer2, 0, 0);

		if (shakePlane == 1) renderPlane (frontCenario, true);
		else renderPlane (frontCenario, false);
		if (shakePlane == 1) renderPlane (frontHP, true);
		else renderPlane (frontHP, false);
		if (shakePlane == 1) renderPlane (front, true);
		else renderPlane (front, false);
		//parent.image(ResourceManager.levelLayer1, 0, 0);
	}
	
	private void renderPlane (Bag <Entity> plane, boolean shake)
	{
		for (int i = 0; i < plane.size(); i++)
		{
			Posicao posicao = PlayState.posicaoMapper.get(plane.get(i));
			Retangulo retangulo = PlayState.retanguloMapper.get(plane.get(i));
			Image image = PlayState.imageMapper.get(plane.get(i));
			Animacao animacao = PlayState.animacaoMapper.get(plane.get(i));
			Plano plano = PlayState.planoMapper.get(plane.get(i));
			PickUp pickUp = PlayState.pickUpMapper.get(plane.get(i));
			
			if (image != null && (image.getImage().width == 0 || image.getImage().width == -1)) continue;
			
			if (animacao != null && (!animacao.isAnimationLoaded())) continue;
			
			String group = world.getGroupManager().getGroupOf(plane.get(i));
			
			if (animacao != null && !animacao.isScroll())
			{
				parent.pushMatrix();
				parent.translate(cameraSystem.cameraPosition.x * plano.getPlaneFactor(), 0);
				parent.image(animacao.getFrame(), posicao.getPosicao().x, posicao.getPosicao().y);
				parent.popMatrix();
			}
			
			parent.pushMatrix();
				parent.translate (parent.width/2, cameraSystem.cameraPosition.y);
				if (shake)
				{
					parent.translate (cameraSystem.cameraPosition.x * plano.getPlaneFactor(), (float)((2 * (SHAKE_MAX - shakePhase) / shakePhase) * Math.sin(shakePhase) * plano.getPlaneFactor()));
				}
				else
				{
					parent.translate(cameraSystem.cameraPosition.x * plano.getPlaneFactor(), 0);
				}
				
				if (!(retangulo == null || (image != null && !image.isScalable()) || (animacao != null && !animacao.isScalable()))) {
					parent.scale(plano.getPlaneFactor());
				}
				
				if (image != null && !image.isScalable())
				{
					if (!image.isCenterTile())
						parent.image(image.getImage(), posicao.getPosicao().x, posicao.getPosicao().y);
					else
					{
						parent.image(image.getImage(), (posicao.getPosicao().x - image.getImage().width  / 2.0f) * plano.getPlaneFactor(), (posicao.getPosicao().y - image.getImage().height / 2.0f) * plano.getPlaneFactor());
					}
				}
				
				if (animacao != null && !animacao.isScalable())
				{
					parent.image(animacao.getFrame(), (posicao.getPosicao().x + animacao.getOffsetX()) * plano.getPlaneFactor() - animacao.getSpriteWidth() / 2 , (posicao.getPosicao().y + retangulo.getHeight() / 2 + animacao.getOffsetY()) * plano.getPlaneFactor() - animacao.getSpriteHeight() );
				}
				
				else if (retangulo != null)
				{
					if (animacao != null && animacao.isScroll() && animacao.isScalable())
					{
						parent.image(animacao.getFrame(), posicao.getPosicao().x - animacao.getSpriteWidth() / 2 + animacao.getOffsetX(), posicao.getPosicao().y - animacao.getSpriteHeight() + retangulo.getHeight() / 2 + animacao.getOffsetY());
					}
					//else if (image != null)
						//parent.image(image.getImage(), posicao.getPosicao().x - image.getImage().width / 2, posicao.getPosicao().y - image.getImage().height / 2);
					else if (image != null && image.isScalable())
	                {
						if (pickUp != null)
						{
							parent.image(image.getImage(), posicao.getPosicao().x - image.getImage().width / 2.0f, posicao.getPosicao().y - image.getImage().height / 2.0f);
						}
						else
						{
							parent.image(image.getImage(), posicao.getPosicao().x - image.getImage().width / 2.0f + image.getOffsetX(), posicao.getPosicao().y - image.getImage().height - retangulo.getHeight() / 2 + image.getOffsetY());
						}
	                }
					if (PlayState.stage < 0)
					{
						parent.noStroke();
						parent.fill(128.0f * plano.getPlaneFactor());
						if (group.equals("BLOCK") || group.equals("FALLINGBLOCK") || group.equals("PASSABLEBLOCK") || group.equals("MOVINGPLATFORM"))
							parent.rect(posicao.getPosicao().x - retangulo.getWidth() / 2.0f, posicao.getPosicao().y - retangulo.getHeight() / 2.0f, retangulo.getWidth() , retangulo.getHeight());
						parent.noFill();
					}
				}
				else
				{
					parent.stroke(200, 200, 200);
					parent.line(posicao.getPosicao().x, posicao.getPosicao().y - parent.height, posicao.getPosicao().x, posicao.getPosicao().y);
				}
			parent.popMatrix();
		}
	}

}
