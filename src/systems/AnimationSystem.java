package systems;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import main.StringAlong;
import managers.ResourceManager;
import states.PlayState;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import components.Animacao;
import components.ObjectState;
import components.Plano;
import components.TipoFisica;
import dataStructures.AnimationFrame;
import factories.EntityFactory;

public class AnimationSystem extends EntityProcessingSystem
{
	private int idleCounter = 0;
	private int beginIdle = 180;
	private Document doc;

	@SuppressWarnings("unchecked")
	public AnimationSystem ()
	{
		super (Animacao.class);
	}

	@Override
	protected void initialize ()
	{
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(getClass().getClassLoader().getResourceAsStream("animationData.xml"));
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void added (Entity e)
	{
		Animacao varAnimacao = PlayState.animacaoMapper.get(e);

		if(varAnimacao.getFileName().indexOf(ResourceManager.PLAYER_ID) != -1){
			addPlayerAnimation(varAnimacao);
		}else if(varAnimacao.getFileName().indexOf(ResourceManager.TRUNK_ID) != -1){
			addTrunkAnimation(varAnimacao);
		}else if(varAnimacao.getFileName().indexOf(ResourceManager.SPIDER_ID) != -1){
			addSpiderAnimation(varAnimacao);
		}else if (varAnimacao.getFileName().equals(ResourceManager.LEVEL1_PART) ||
				varAnimacao.getFileName().equals(ResourceManager.LEVEL2_PART) ||
				varAnimacao.getFileName().equals(ResourceManager.LEVEL3_PART)){
			addPartAnimation(varAnimacao);
		}else if (varAnimacao.getFileName().equals(ResourceManager.AGUA)){
			addWaterAnimation(varAnimacao);
			varAnimacao.setTag("ESQ");
		}
	}

	@Override
	protected void process (Entity e)
	{
		Animacao varAnimacao = PlayState.animacaoMapper.get(e);
		TipoFisica varTipoFisica = PlayState.tipoFisicaMapper.get(e);
		ObjectState varObjectState = PlayState.objectStateMapper.get(e);
		Plano varPlano = PlayState.planoMapper.get(e);
		String direction = ((varObjectState != null) ? (varObjectState.getFacing() == 0 ? "_ESQ" : "_DIR") : "");
		
		if(varAnimacao.getFileName().indexOf(ResourceManager.PLAYER_ID) != -1){
			setPlayerAnimation(e, varAnimacao, varTipoFisica,
						varObjectState, direction);
			if (!varAnimacao.isScalable() && !varAnimacao.getFileName().equals(ResourceManager.PLAYER[PlayState.planoMapper.get(e).getPlano() - 1]))
			{
				varAnimacao.setFileName(ResourceManager.PLAYER[PlayState.planoMapper.get(e).getPlano() - 1]);
				varAnimacao.setSpriteWidth((int)Math.floor((varPlano.getPlaneFactor() * EntityFactory.PLAYER_SPRITE_WIDTH)));
				varAnimacao.setSpriteHeight((int)Math.floor((varPlano.getPlaneFactor() * EntityFactory.PLAYER_SPRITE_HEIGHT)));
			}
		}else if(varAnimacao.getFileName().indexOf(ResourceManager.TRUNK_ID) != -1){
			setTrunkAnimation(e, varAnimacao, varObjectState, direction);
			if (!varAnimacao.isScalable() && !varAnimacao.getFileName().equals(ResourceManager.TRUNK[PlayState.planoMapper.get(e).getPlano() - 1]))
			{
				varAnimacao.setFileName(ResourceManager.TRUNK[PlayState.planoMapper.get(e).getPlano() - 1]);
				varAnimacao.setSpriteWidth((int)Math.floor((varPlano.getPlaneFactor() * EntityFactory.TORA_SPRITE_WIDTH)));
				varAnimacao.setSpriteHeight((int)Math.floor((varPlano.getPlaneFactor() * EntityFactory.TORA_SPRITE_HEIGHT)));
			}
		}else if(varAnimacao.getFileName().indexOf(ResourceManager.SPIDER_ID) != -1){
			setSpiderAnimation(e, varAnimacao, varTipoFisica, varObjectState, direction);
			if (!varAnimacao.isScalable() && !varAnimacao.getFileName().equals(ResourceManager.SPIDER[PlayState.planoMapper.get(e).getPlano() - 1]))
			{
				varAnimacao.setFileName(ResourceManager.SPIDER[PlayState.planoMapper.get(e).getPlano() - 1]);
				varAnimacao.setSpriteWidth((int)Math.floor((varPlano.getPlaneFactor() * EntityFactory.ARANHA_SPRITE_WIDTH)));
				varAnimacao.setSpriteHeight((int)Math.floor((varPlano.getPlaneFactor() * EntityFactory.ARANHA_SPRITE_HEIGHT)));
			}
		}else if (varAnimacao.getFileName().equals(ResourceManager.LEVEL1_PART) ||
				varAnimacao.getFileName().equals(ResourceManager.LEVEL2_PART) ||
				varAnimacao.getFileName().equals(ResourceManager.LEVEL3_PART)){
			setPartAnimation(varAnimacao, direction);
		}
		
		updateAnimation(varAnimacao, world.getDelta());
	}
	
	private void updateAnimation (Animacao varAnimacao, int timeElapsed)
	{
		varAnimacao.setTimer (varAnimacao.getTimer() + timeElapsed);
		if (varAnimacao.getAnimationData().get(varAnimacao.getTag()) != null)
		{
			varAnimacao.setCycle(varAnimacao.getTimer() / varAnimacao.getAnimationData().get(varAnimacao.getTag()).frameDuration);

			if (varAnimacao.getCycle() >= varAnimacao.getAnimationData().get(varAnimacao.getTag()).frames.length) 
			{
				if (varAnimacao.getAnimationData().get(varAnimacao.getTag()).loop)
				{
					varAnimacao.setCycle(0);
					varAnimacao.setTimer(0);
				}
				else
				{
					varAnimacao.setCycle(varAnimacao.getAnimationData().get(varAnimacao.getTag()).frames.length - 1);
					varAnimacao.setAnimationOver(true);
				}
			}
		}
	}
	
	private void setPartAnimation(Animacao varAnimacao, String direction) {
		if (((PlayState)StringAlong.getInstance().state).levelTimeLeft <= 0) varAnimacao.setTag("INVISIBLE");
		else varAnimacao.setTag("VISIBLE");
	}

	private void setSpiderAnimation(Entity e, Animacao varAnimacao,
			TipoFisica varTipoFisica, ObjectState varObjectState, String direction) {
		if (varObjectState.isDying()){
			varAnimacao.setTag("MORRENDO");
			(PlayState.physicsSystem).getpWorld().destroyBody(e.getComponent(TipoFisica.class).getBody());
			if(varAnimacao.isAnimationOver()){
				world.deleteEntity(e);
			}
		}
		else if (varTipoFisica.getVy() < 0) varAnimacao.setTag("SUBINDO");
		else if (varTipoFisica.getVy() >= 0) varAnimacao.setTag("DESCENDO");
		else if (varTipoFisica.getVy() == 0) varAnimacao.setTag("CHAO");
	}

	private void setTrunkAnimation(Entity e, Animacao varAnimacao,
			ObjectState varObjectState, String direction) {
		if (varObjectState.isDying()){
			varAnimacao.setTag("MORRENDO" + direction);
			(PlayState.physicsSystem).getpWorld().destroyBody(e.getComponent(TipoFisica.class).getBody());
			if(varAnimacao.isAnimationOver()){
				world.deleteEntity(e);
			}
		}
		else varAnimacao.setTag("ANDANDO" + direction);
	}

	private void setPlayerAnimation(Entity e, Animacao varAnimacao,
			TipoFisica varTipoFisica, ObjectState varObjectState, String direction) {
		
		if (varObjectState.isDying()){
			varAnimacao.setTag("MORRENDO" + direction);
			idleCounter=0;
			if(varAnimacao.isAnimationOver()){
				(PlayState.physicsSystem).getpWorld().destroyBody(varTipoFisica.getBody());
				world.deleteEntity (e);
				((PlayState)(StringAlong.getInstance().state)).setNextState(new PlayState(((PlayState)(StringAlong.getInstance().state)).fileName, PlayState.stage));
			}
		}
		else if (varObjectState.isAttacking())
		{
			varAnimacao.setTag("ATACANDO" + direction);
			idleCounter=0;
			if (varAnimacao.isAnimationOver())
				varObjectState.setAttacking(false);
		}
		else if (varObjectState.isTakingDamage())
		{
			varAnimacao.setTag("DANO" + direction);
		}
		else if (varTipoFisica.getVy() > 0 && (varTipoFisica.isTouching() & TipoFisica.DOWN) != TipoFisica.DOWN){
			varAnimacao.setTag("CAINDO" + direction);
			idleCounter=0;
		}
		else if (varTipoFisica.getVy() < 0 && (varTipoFisica.isTouching() & TipoFisica.DOWN) != TipoFisica.DOWN){
			varAnimacao.setTag("PULANDO" + direction);
			idleCounter=0;
		}
		else if ((varTipoFisica.getVx() - varTipoFisica.getReferentialSpeed().x) != 0){
			varAnimacao.setTag("ANDANDO" + direction);
			idleCounter=0;
		}
		else{
			if(idleCounter<=beginIdle){
				varAnimacao.setTag("PARADO" + direction);
				idleCounter++;
			}
			else if(idleCounter>beginIdle){
				varAnimacao.setTag("IDLE" + direction);
				if(varAnimacao.isAnimationOver()){
					idleCounter=0;
				}
			}
		}
	}
	
	private void addPartAnimation(Animacao varAnimacao) {
		addAnimationData(varAnimacao, "PARTE");
	}
	
	private void addWaterAnimation(Animacao varAnimacao) {
		addAnimationData(varAnimacao, "AGUA");
	}
	private void addSpiderAnimation(Animacao varAnimacao) {
		addAnimationData(varAnimacao, "ARANHA");
	}

	private void addTrunkAnimation(Animacao varAnimacao) {
		addAnimationData(varAnimacao, "TOCO");
	}
	
//	private int atacandoVel = Integer.parseInt(((PlayState)StringAlong.getInstance().state).config.getProperty("velocidadeAtacandoPlayer"));
//	private int andandoVel = Integer.parseInt(((PlayState)StringAlong.getInstance().state).config.getProperty("velocidadeAndandoPlayer"));
//	private int pulandoVel = Integer.parseInt(((PlayState)StringAlong.getInstance().state).config.getProperty("velocidadePulandoPlayer"));
//	private int caindoVel = Integer.parseInt(((PlayState)StringAlong.getInstance().state).config.getProperty("velocidadeCaindoPlayer"));
	
	private void addPlayerAnimation(Animacao varAnimacao) {
		addAnimationData(varAnimacao, "PLAYER");
	}

	public void addAnimationData(Animacao varAnimacao, String object) {
		NodeList animations = doc.getElementsByTagName("animations");
		int i = 0;
		for (; i < animations.getLength(); i++)
		{
			Element e = (Element) animations.item(i);
			if (e.getAttribute("name").equals(object))
			{
				animations = e.getElementsByTagName("animation");
				break;
			}
		}
		
		for (i = 0; i < animations.getLength(); i++)
		{
			Element e = (Element) animations.item(i);
			NodeList frames = e.getElementsByTagName("frame");
			AnimationFrame [] animationFrames = new AnimationFrame [frames.getLength()];
			for (int j = 0; j < frames.getLength(); j++)
			{
				Element ef = (Element) frames.item(j);
				NodeList points = ef.getElementsByTagName("point");
				AnimationFrame af = new AnimationFrame (Integer.parseInt(ef.getAttribute("id")));
				for (int k = 0; k < points.getLength(); k++)
				{
					Element ep = (Element) points.item(k);
					af.addRefpoint(ep.getAttribute("name"), new Float [] {Float.parseFloat(ep.getAttribute("x")), Float.parseFloat(ep.getAttribute("y"))});
				}
				animationFrames[j] = af;
			}
			varAnimacao.addAnimation(e.getAttribute("name"), animationFrames, Integer.parseInt(e.getAttribute("frameDuration")), Boolean.parseBoolean(e.getAttribute("loop")), Integer.parseInt(e.getAttribute("offsetX")), Integer.parseInt(e.getAttribute("offsetY")));
		}
	}
}