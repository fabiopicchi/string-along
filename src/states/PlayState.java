package states;

import java.awt.event.KeyEvent;

import java.util.Properties;
import java.util.Vector;

import org.jbox2d.common.Vec2;

import main.StringAlong;
import managers.InputManager;
import managers.ResourceManager;

import systems.AISystem;
import systems.AnimationSystem;
import systems.CameraSystem;
import systems.HPSystem;
import systems.InputSystem;
import systems.MovingPlatformSystem;
import systems.PhysicsSystem;
import systems.PlaneSystem;
import systems.PositionSystem;
import systems.RenderSystem;
import systems.ScoreSystem;
import systems.SoundSystem;
import systems.WeaponSystem;
import utils.Digits;
import utils.TextStyles;

import com.artemis.Entity;
import com.artemis.SystemManager;
import com.artemis.World;
import com.artemis.ComponentMapper;

import components.Andar;
import components.Animacao;
import components.HP;
import components.Image;
import components.MovingPlatform;
import components.ObjectState;
import components.PickUp;
import components.Plano;
import components.Posicao;
import components.PowerUp;
import components.Retangulo;
import components.TipoFisica;
import components.VaiVolta;
import components.Weapon;

import constants.GameObject;
import dataStructures.TmxLoader;
import dataStructures.GroupObject;
import factories.EntityFactory;

public class PlayState implements IGameState {

	public static final int LEVEL_TOTAL_TIME = 180000;
	public int gameState = 0;
	private IGameState nextState;
	public int levelTimeLeft = LEVEL_TOTAL_TIME;
	private int perolas;
	public Properties config;

	private StringAlong parent;
	public static TmxLoader stageLoader;

	//controle dos menus
	public float cursorY, selectY;	
	int	x=366;
	int y=416;
	int spacing=45;
	private int pauseMenuItems = 4;
	public int endMenuItems = 3;
	public int endY = 568;
	public int currentSelection;
	public int curtainY=0;

	//controle de carregamento de imagens
	private boolean bHUDLoaded = false;
	private boolean bPauseLoaded = false;
	private boolean bEndLoaded = false;

	private World world;
	public static RenderSystem renderSystem;
	public static PhysicsSystem physicsSystem;
	public static InputSystem inputSystem;
	public static CameraSystem cameraSystem;
	public static PositionSystem positionSystem;
	public static AISystem aiSystem;
	public static AnimationSystem animationSystem;
	public static SoundSystem soundSystem;
	public static PlaneSystem planeSystem;
	public static ScoreSystem scoreSystem;
	public static WeaponSystem weaponSystem;
	public static HPSystem hpSystem;
	public static MovingPlatformSystem movingPlatformSystem;
	public String fileName;
	public static int stage;

	//mappers
	public static ComponentMapper <Andar> andarMapper;
	public static ComponentMapper <Animacao> animacaoMapper;
	public static ComponentMapper <HP> HPMapper;
	public static ComponentMapper <Image> imageMapper;
	public static ComponentMapper <MovingPlatform> movingPlatformMapper;
	public static ComponentMapper <ObjectState> objectStateMapper;
	public static ComponentMapper <PickUp> pickUpMapper;
	public static ComponentMapper <Plano> planoMapper;
	public static ComponentMapper <Posicao> posicaoMapper;
	public static ComponentMapper <PowerUp> powerUpMapper;
	public static ComponentMapper <Retangulo> retanguloMapper;
	public static ComponentMapper <TipoFisica> tipoFisicaMapper;
	public static ComponentMapper <VaiVolta> vaiVoltaMapper;
	public static ComponentMapper <Weapon> weaponMapper;

	public PlayState (String fileName, int stage) {
		this.fileName = fileName;
		this.config = null;
		PlayState.stage = stage;
	}

	@Override
	public void create() {
		parent = StringAlong.getInstance();
		parent.textAlign(StringAlong.LEFT);

		loadResources (fileName);

		currentSelection = 0;
		cursorY = -800;
		perolas = 0;

		world = new com.artemis.World();

		SystemManager systemManager = world.getSystemManager();
		renderSystem = (RenderSystem) systemManager.setSystem(new RenderSystem());
		physicsSystem = (PhysicsSystem) systemManager.setSystem(new PhysicsSystem());
		inputSystem = (InputSystem) systemManager.setSystem(new InputSystem());
		cameraSystem = (CameraSystem) systemManager.setSystem(new CameraSystem ());
		positionSystem = (PositionSystem) systemManager.setSystem(new PositionSystem());
		aiSystem = (AISystem) systemManager.setSystem(new AISystem ());
		animationSystem = (AnimationSystem) systemManager.setSystem(new AnimationSystem ());
		soundSystem = (SoundSystem) systemManager.setSystem(new SoundSystem ());
		planeSystem = (PlaneSystem) systemManager.setSystem(new PlaneSystem ());
		scoreSystem = (ScoreSystem) systemManager.setSystem(new ScoreSystem ());
		weaponSystem = (WeaponSystem) systemManager.setSystem(new WeaponSystem ());
		hpSystem = (HPSystem) systemManager.setSystem(new HPSystem());
		movingPlatformSystem = (MovingPlatformSystem) systemManager.setSystem(new MovingPlatformSystem());

		//Mapper declaration
		andarMapper 			= new ComponentMapper <Andar> (Andar.class, world);
		animacaoMapper 			= new ComponentMapper <Animacao> (Animacao.class, world);
		HPMapper 				= new ComponentMapper <HP> (HP.class, world);
		imageMapper 			= new ComponentMapper <Image> (Image.class, world);
		movingPlatformMapper	= new ComponentMapper <MovingPlatform> (MovingPlatform.class, world);
		objectStateMapper 		= new ComponentMapper <ObjectState> (ObjectState.class, world);
		pickUpMapper 			= new ComponentMapper <PickUp> (PickUp.class, world);
		planoMapper 			= new ComponentMapper <Plano> (Plano.class, world);
		posicaoMapper 			= new ComponentMapper <Posicao> (Posicao.class, world);
		powerUpMapper 			= new ComponentMapper <PowerUp> (PowerUp.class, world);
		retanguloMapper 		= new ComponentMapper <Retangulo> (Retangulo.class, world);
		tipoFisicaMapper 		= new ComponentMapper <TipoFisica> (TipoFisica.class, world);
		vaiVoltaMapper 			= new ComponentMapper <VaiVolta> (VaiVolta.class, world);
		weaponMapper 			= new ComponentMapper <Weapon> (Weapon.class, world);

		//Declaração das entidades
		Vector <GroupObject> objects;

		objects = stageLoader.getObject("Cenario Layer 1");
		for (int i = 0; i < objects.size(); i++)
		{
			EntityFactory.createBlock(world, objects.get(i), 1);
		}

		objects = stageLoader.getObject("Cenario Layer 2");
		for (int i = 0; i < objects.size(); i++)
		{
			EntityFactory.createBlock(world, objects.get(i), 2);
		}

		objects = stageLoader.getObject("Cenario Layer 3");
		for (int i = 0; i < objects.size(); i++)
		{
			EntityFactory.createBlock(world, objects.get(i), 3);
		}

		for (GameObject obj : GameObject.values())
		{
			objects = stageLoader.getObject("Object Layer 1", obj.getCode());
			for (int i = 0; i < objects.size(); i++)
			{
				EntityFactory.createObject(world, obj, objects.get(i), 1, stage);
				if (obj.getCode().equals("perola")) perolas++;
			}

			objects = stageLoader.getObject("Object Layer 2", obj.getCode());
			for (int i = 0; i < objects.size(); i++)
			{
				EntityFactory.createObject(world, obj, objects.get(i), 2, stage);
				if (obj.getCode().equals("perola")) perolas++;
			}

			objects = stageLoader.getObject("Object Layer 3", obj.getCode());
			for (int i = 0; i < objects.size(); i++)
			{
				EntityFactory.createObject(world, obj, objects.get(i), 3, stage);
				if (obj.getCode().equals("perola")) perolas++;
			}
		}

		EntityFactory.createPlayer(world);
//		EntityFactory.createAgua(world);
		if (stage >= 0)
		{
			Entity e;
			for (int i = 0; i < ResourceManager.NUM_IMAGENS1; i++)
			{
				System.out.println (ResourceManager.LEVEL_LAYER_1[i]);
				e = world.createEntity();
				e.addComponent(new Image (ResourceManager.LEVEL_LAYER_1[i], false, false));
				e.addComponent(new Posicao (new Vec2 (i*1000,0)));
				e.addComponent(new Retangulo (1000, 768));
				e.addComponent(new Plano (1));
				e.refresh();
			}

			for (int i = 0; i < ResourceManager.NUM_IMAGENS2; i++)
			{
				e = world.createEntity();
				e.addComponent(new Image (ResourceManager.LEVEL_LAYER_2[i], false, false));
				e.addComponent(new Posicao (new Vec2 (i*1000,0)));
				e.addComponent(new Retangulo (1000, 768));
				e.addComponent(new Plano (2));
				e.refresh();
			}

			for (int i = 0; i < ResourceManager.NUM_IMAGENS3; i++)
			{
				e = world.createEntity();
				e.addComponent(new Image (ResourceManager.LEVEL_LAYER_3[i], false, false));
				e.addComponent(new Posicao (new Vec2 (i*1000,0)));
				e.addComponent(new Retangulo (1000, 768));
				e.addComponent(new Plano (3));
				e.refresh();
			}
		}

		//Inicialização do jogo
		systemManager.initializeAll();
		//precisa trocar esse 2 por 4 qdo aumentar a qtidade de fases por mundo
		soundSystem.play("level" + (stage / StringAlong.stagesPerWorld + 1));
	}


	private void loadResources(String fileName) {
		ResourceManager.prepareStageSpecificFileNames(stage);

		//Load HUD
		loadHUD();

		stageLoader = new TmxLoader (fileName);
	}

	@Override
	public void update() {
		if (ResourceManager.getImage(ResourceManager.BGCURTAIN).width > 0)
		{
			if (nextState == null)
			{
				// TODO : I buringu shemu to famiri, no raisu
				if (curtainY > -ResourceManager.getImage(ResourceManager.BGCURTAIN).width)
				{
					if (ResourceManager.getImage(ResourceManager.LEVEL_LAYER_1[0]) != null
							&& ResourceManager.getImage(ResourceManager.LEVEL_LAYER_2[0]) != null
							&& ResourceManager.getImage(ResourceManager.LEVEL_LAYER_3[0]) != null
							&& ResourceManager.getImage(ResourceManager.LEVEL_LAYER_1[0]).width > 0
							&& ResourceManager.getImage(ResourceManager.LEVEL_LAYER_2[0]).width > 0 
							&& ResourceManager.getImage(ResourceManager.LEVEL_LAYER_3[0]).width > 0 || StringAlong.debug)
					{
						curtainY-=40;
					}
				}
				
				if (gameState == 0)
				{
					if (curtainY <= -ResourceManager.getImage(ResourceManager.BGCURTAIN).width)
					{
						if (levelTimeLeft != 0) levelTimeLeft -= parent.timeElapsed;
						if (levelTimeLeft < 0) levelTimeLeft = 0;
					}
					world.loopStart();
					world.setDelta(33);
					inputSystem.process();
					planeSystem.process();
					aiSystem.process();
					movingPlatformSystem.process();
					weaponSystem.process();
					physicsSystem.process();
					positionSystem.process();
					soundSystem.process();
					scoreSystem.process();
					cameraSystem.process();
					animationSystem.process();
					hpSystem.process();
				}
				else if (gameState == 1)
				{
					for (int i =0; i<pauseMenuItems;i++){
						if (InputManager.mouseX>x && InputManager.mouseX<x+400 && InputManager.mouseY>371+i*spacing && InputManager.mouseY<371+(i+1)*spacing){
							if (InputManager.mouseMoved==true) currentSelection=i;
							if (InputManager.mouseClicked==true) menuAction();
						}
					}
		
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_ENTER))
					{
						menuAction();
					}
		
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP))
					{
						currentSelection--;
						if (currentSelection<0)currentSelection=pauseMenuItems-1;
					}
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN))
					{
						currentSelection++;
						if (currentSelection>=pauseMenuItems)currentSelection=0;
					}
					//TODO: THIS!
					selectY = y - ResourceManager.getImage(ResourceManager.HAND).height + currentSelection*spacing;
					cursorY -= (cursorY-selectY)*0.2;
				}
				else if (gameState == 2)
				{
					for (int i =0; i<endMenuItems;i++){
						if (InputManager.mouseX>x && InputManager.mouseX<x+400 && InputManager.mouseY>506+i*spacing && InputManager.mouseY<506+(i+1)*spacing){
							if (InputManager.mouseMoved==true) currentSelection=i;
							if (InputManager.mouseClicked==true) menuAction();
						}
					}
		
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_ENTER))
					{
						menuAction();
					}
		
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP))
					{
						currentSelection--;
						if (currentSelection<0)currentSelection=endMenuItems-1;
					}
		
					if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN))
					{
						currentSelection++;
						if (currentSelection>=endMenuItems)currentSelection=0;
					}
		
					//TODO: THIS!
					selectY = endY - ResourceManager.getImage(ResourceManager.HAND).height +currentSelection*spacing;
					cursorY -= (cursorY-selectY)*0.2;
				}
			}
			//mudando de estado
			else
			{
				curtainY += 40;
				if (curtainY >= 0)
					StringAlong.getInstance().switchState(nextState);
			}
		}
	}

	public void menuAction(){

		if (gameState == 1){
			switch (currentSelection)
			{
			case 0:
				gameState = 0;
				cursorY=-800; //(pra que ele venha descendo de novo na próxima vez q der pause)
				break;
			case 1:
				gameState = 0;
				this.setNextState(new PlayState (fileName, stage));
				break;
			case 2:
				gameState = 0;
				this.setNextState(new MenuState ());
				break;
			case 3:
				parent.exit();
				break;
			}
		}

		if (gameState == 2){
			if (StringAlong.bestTime[stage] < 0) {
				if (stage<3){
					if (stage<StringAlong.stageNumber-1)StringAlong.isStageVisible[stage+1]=true;
				}
				else if (stage==3)StringAlong.isStageVisible[6]=true;
				else if (stage==6)StringAlong.isStageVisible[9]=true;
//				else if (stage==9){
//					TODO: colocar tela de fim da demo, eu presumo?
//				}
				
			}
			
			if (StringAlong.bestTime[stage] < 0  || (LEVEL_TOTAL_TIME - this.levelTimeLeft < StringAlong.bestTime[stage])) StringAlong.bestTime[stage] = LEVEL_TOTAL_TIME - this.levelTimeLeft;
			if (!StringAlong.partCollected[stage]) StringAlong.partCollected[stage] = (this.levelTimeLeft > 0);
			if (perolas > StringAlong.pearlsCollected[stage]) StringAlong.pearlsCollected[stage] = scoreSystem.getPerolasCounter();
			if (stage < 9) //porque a última fase do demo é a 9 (4-1)
			{

				switch (currentSelection)
				{

				case 1:
					gameState = 0;
					this.setNextState(new PlayState (fileName, stage));
					break;
				case 0:
					gameState = 0;		
					//mundo 1 simplesmente avança (demo)
					if (stage / StringAlong.stagesPerWorld == 0) stage++;
					//outros mundos - roda somente a primeira fase
					else stage += 3;
					this.setNextState(new PlayState ("fase_" + (stage / StringAlong.stagesPerWorld + 1) + "_" + (stage % StringAlong.stagesPerWorld + 1) + ".tmx", stage));
					break;
				case 2:
					gameState = 0;
					this.setNextState(new MenuState ());
					break;
				}

			}
			else
			{
				this.setNextState(new MenuState ());
			}
		}
	}

	@Override
	public void draw() {
		if (gameState == 0)
		{
			if (!bHUDLoaded)
			{
				unloadPause();
				unloadEnd();
				loadHUD();
			}
			drawBackground ();
			renderSystem.process();
			drawHUD ();
		}
		else if (gameState == 1)
		{
			if (!bPauseLoaded)
			{
				unloadEnd();
				unloadHUD();
				loadPause();
			}
			drawBackground ();
			renderSystem.process();
			drawPauseScreen();
		}
		else if (gameState == 2)
		{
			if (!bEndLoaded)
			{
				unloadHUD();
				unloadPause();
				loadEnd();
			}
			//			if (stage < 2)
			//			{
			drawBackground ();
			renderSystem.process();
			drawEndScreen ();
			//			}
			//			else
			//			{
			//				ResourceManager.loadImage(ResourceManager.DEMO_END);
			//				parent.image(ResourceManager.getImage(ResourceManager.DEMO_END), 0, 0);
			//			}
		}
	}

	private void drawBackground () 
	{
		parent.image(ResourceManager.getImage(ResourceManager.BACKGROUND_MOON), (StringAlong.getInstance().width == 1024) ? 0 : 128, 0);
	}

	private void drawHUD ()
	{
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN), 0, curtainY);
		parent.image(ResourceManager.getImage(ResourceManager.CURTAIN), -128, 0);
		parent.fill(255);
		parent.textFont(ResourceManager.getFont (ResourceManager.AACHEN39));
		TextStyles.shadedText(Digits.setDigits((levelTimeLeft / 1000 / 60), 2) + ":" + Digits.setDigits((levelTimeLeft / 1000 % 60), 2) + ":" + Digits.setDigits((levelTimeLeft % 1000 / 10), 2), (StringAlong.getInstance().width == 1024) ? 832 : 1088, 65, 3, 4);
		parent.image(ResourceManager.getImage(ResourceManager.PEARLHUD), (StringAlong.getInstance().width == 1024) ? 832 : 1088, 70);
		parent.textFont(ResourceManager.getFont (ResourceManager.AACHEN34));
		TextStyles.shadedText(Digits.setDigits(((ScoreSystem)scoreSystem).getPerolasCounter(), 2) + "/" + Digits.setDigits(perolas, 2), (StringAlong.getInstance().width == 1024) ? 890 : 1146, 103, 3, 4);
	}


	private void drawPauseScreen ()
	{
		parent.image(ResourceManager.getImage(ResourceManager.PAUSE_SCREEN), (StringAlong.getInstance().width == 1024) ? 0 : 128, 0);
		//TODO: THIS!
		parent.image(ResourceManager.getImage(ResourceManager.HAND), (StringAlong.getInstance().width == 1024) ? x - ResourceManager.getImage(ResourceManager.HAND).width : x - ResourceManager.getImage(ResourceManager.HAND).width + 128,cursorY+5);
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN), 0, curtainY);
		parent.image(ResourceManager.getImage(ResourceManager.CURTAIN), (StringAlong.getInstance().width == 1024) ? -128 : 0, 0);
	}

	private void drawEndScreen ()
	{
		if (levelTimeLeft > 0){
			parent.image(ResourceManager.getImage (ResourceManager.END_SCREEN), (StringAlong.getInstance().width == 1024) ? 0 : 128, 0);
		}
		else{
			parent.image(ResourceManager.getImage (ResourceManager.END_SCREEN2), (StringAlong.getInstance().width == 1024) ? 0 : 128, 0);
		}
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN), 0, curtainY);
		parent.image(ResourceManager.getImage (ResourceManager.CURTAIN), (StringAlong.getInstance().width == 1024) ? -128 : 0, 0);
		//TODO: THIS!
		parent.image(ResourceManager.getImage (ResourceManager.HAND), (StringAlong.getInstance().width == 1024) ? x - ResourceManager.getImage (ResourceManager.HAND).width : x - ResourceManager.getImage (ResourceManager.HAND).width + 128,cursorY-5);
		parent.fill(255);
		parent.textFont(ResourceManager.getFont (ResourceManager.AACHEN34));
		parent.text(String.valueOf(levelTimeLeft / 1000 / 60) + ":" + String.valueOf(levelTimeLeft / 1000 % 60) + ":" + String.valueOf(levelTimeLeft % 1000 / 10), (StringAlong.getInstance().width == 1024) ? 481 : 609, 430);
		parent.text(world.getSystemManager().getSystem(ScoreSystem.class).getPerolasCounter(), (StringAlong.getInstance().width == 1024) ? 366 : 494, 478);
	}

	@Override
	public void delete() {
		((SoundSystem)soundSystem).close();
		ResourceManager.unloadResources();
	}

	private void loadHUD() 
	{
		ResourceManager.loadImage(ResourceManager.BACKGROUND_MOON);
		ResourceManager.loadImage(ResourceManager.CURTAIN);
		ResourceManager.loadImage(ResourceManager.BGCURTAIN);
		ResourceManager.loadImage(ResourceManager.PEARLHUD);
		ResourceManager.loadFont(ResourceManager.AACHEN34);
		ResourceManager.loadFont(ResourceManager.AACHEN39);
		bHUDLoaded = true;
	}

	private void unloadHUD() 
	{
		ResourceManager.unloadImage(ResourceManager.PEARLHUD);
		ResourceManager.unloadFont(ResourceManager.AACHEN34);
		ResourceManager.unloadFont(ResourceManager.AACHEN39);
		bHUDLoaded = false;
	}

	private void loadPause() 
	{
		ResourceManager.loadImage(ResourceManager.PAUSE_SCREEN);
		ResourceManager.loadImage(ResourceManager.HAND);
		bPauseLoaded = true;
	}

	private void unloadPause() 
	{
		ResourceManager.unloadImage(ResourceManager.PAUSE_SCREEN);
		ResourceManager.unloadImage(ResourceManager.HAND);
		bPauseLoaded = false;
	}

	private void loadEnd() 
	{
		ResourceManager.loadImage(ResourceManager.END_SCREEN);
		ResourceManager.loadImage(ResourceManager.END_SCREEN2);
		ResourceManager.loadImage(ResourceManager.HAND);
		ResourceManager.loadFont(ResourceManager.AACHEN34);
		bEndLoaded = true;
	}

	private void unloadEnd() 
	{
		ResourceManager.unloadImage(ResourceManager.END_SCREEN);
		ResourceManager.unloadImage(ResourceManager.END_SCREEN2);
		ResourceManager.unloadImage(ResourceManager.HAND);
		ResourceManager.unloadFont(ResourceManager.AACHEN34);
		bEndLoaded = false;
	}
	
	public void setNextState (IGameState state)
	{
		this.nextState = state;
	}
}
