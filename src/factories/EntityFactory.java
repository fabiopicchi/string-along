package factories;

import main.StringAlong;
import managers.ResourceManager;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import states.PlayState;

import com.artemis.Entity;
import com.artemis.World;

import components.Andar;
import components.Animacao;
import components.Image;
import components.MovingPlatform;
import components.ObjectState;
import components.PickUp;
import components.Plano;
import components.Pontos;
import components.Posicao;
import components.PowerUp;
import components.PulaPula;
import components.Retangulo;
import components.TipoFisica;
import components.VaiVolta;
import components.HP;
//import components.Weapon;
import constants.GameObject;
import dataStructures.GroupObject;

public class EntityFactory {
	public static final int REEL_HEIGHT = 85;
	public static final int REEL_WIDTH = 80;
	public static final int PEARL_WIDTH = 24;
	public static final int PEARL_HEIGHT = 23;
	public static final int PLAYER_HP = 3;
	public static final int ARANHA_HP = 4;
	public static final int TOCO_HP = 1;
	public static final int PARTE1_SPRITE_HEIGHT = 137;
	public static final int PARTE1_SPRITE_WIDTH = 41;
	public static final int PARTE2_SPRITE_HEIGHT = 104;
	public static final int PARTE2_SPRITE_WIDTH = 135;
	public static final int PARTE3_SPRITE_HEIGHT = 137;
	public static final int PARTE3_SPRITE_WIDTH = 37;
	public static final int ARANHA_SPRITE_HEIGHT = 96;
	public static final int ARANHA_SPRITE_WIDTH = 173;
	public static final int TORA_SPRITE_HEIGHT = 94;
	public static final int TORA_SPRITE_WIDTH = 92;
	public static final int PLAYER_SPRITE_HEIGHT = 239;
	public static final int PLAYER_SPRITE_WIDTH = 194;
	public static final int AGUA_SPRITE_HEIGHT = 89;
	public static final int AGUA_SPRITE_WIDTH = 55;
	public static final float RANGE_TOLERANCE = 1.5f;
	
	public static final float TILE_SIZE = 32.0f;
	public static final float TILEMAP_FACTOR = 86.0f / 32.0f;
	
	public static void createTile (World world, Vec2 position, String group, int plano)
	{
		Entity tile = world.createEntity();
		
		tile.addComponent (new Retangulo (TILE_SIZE, TILE_SIZE));
		tile.addComponent (new Plano (plano));
		tile.addComponent (new Posicao (position));
		tile.addComponent (new TipoFisica(BodyType.STATIC, false));	
		tile.refresh ();
	}
	
	public static void createBlock (World world, GroupObject obj, int plano)
	{
		Entity block = world.createEntity();
		
		block.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, obj.height * TILEMAP_FACTOR));
		block.addComponent (new Plano (plano));
		block.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
		block.addComponent (new TipoFisica(BodyType.STATIC, false));	
		block.setGroup("BLOCK");
		block.refresh ();
	}
	
	public static void createPlayer (World world)
	{
		Entity e = world.createEntity();
		e.addComponent (new ObjectState ());
		e.addComponent (new Retangulo (0.50f * TILE_SIZE * TILEMAP_FACTOR, 0.80f * 2.0f * TILE_SIZE * TILEMAP_FACTOR));
		e.addComponent (new Plano (1));
		e.addComponent (new Posicao (new Vec2 (100.0f, -500.0f)));
		e.addComponent (new TipoFisica (BodyType.DYNAMIC, false));
		e.addComponent (new HP (PLAYER_HP));
		e.addComponent (new Pontos ());
		e.addComponent (new PowerUp ());
		e.addComponent (new Animacao (ResourceManager.PLAYER, PLAYER_SPRITE_WIDTH, PLAYER_SPRITE_HEIGHT, true));
		e.setGroup("PLAYER");
		e.setTag("PLAYER");
		e.refresh();
		
		createWeapon(world, e);
		
		for (int i = 0; i < PLAYER_HP; i++)
		{
			createString(world, e.getId(), e.getComponent(HP.class), i);
		}
	}

	public static void createString(World world, int entityId, HP entityHP, int stringId) {
		Entity e;
		e = world.createEntity();
		e.addComponent(new Plano (1));
		e.addComponent(new Posicao (new Vec2 (0, 0)));
		e.setTag(entityId + "_" + stringId);
		e.refresh();
		entityHP.addTag(e);
	}

	public static void createWeapon(World world, Entity wielder) {
//		Entity e;
//		e = world.createEntity();
//		e.addComponent (new TipoFisica (BodyType.DYNAMIC, true));
//		e.addComponent (new Retangulo (194.0f / 2.0f + 52, 239.0f));
//		e.addComponent (new Weapon (wielder));
//		e.addComponent (new Posicao (new Vec2 (100.0f + 0.50f * 86.0f, -500.0f)));
//		e.addComponent (new Plano (1));
//		e.setGroup("WEAPON");
//		e.refresh();
	}
	
	public static void createObject (World world, GameObject code, GroupObject obj, int plano, int stage)
	{
		Entity object;
		switch (code)
		{
		case PEROLA:
			object = world.createEntity ();
			object.setGroup("PICKUP");
			object.addComponent (new PickUp (code.getCode()));
			object.addComponent (new Image (ResourceManager.PEARL[plano - 1], false, true));
			object.addComponent (new Retangulo (PEARL_WIDTH * RANGE_TOLERANCE, PEARL_HEIGHT * RANGE_TOLERANCE));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, true));
			object.refresh();
			break;
		case TOCOBLOCK:
			object = world.createEntity ();
			object.setGroup("TOCOBLOCK");
			object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, obj.height * TILEMAP_FACTOR));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, false));
			object.refresh();
			break;
		case CARRETEL:
			object = world.createEntity ();
			object.setGroup("PICKUP");
			object.addComponent (new PickUp (code.getCode()));
			object.addComponent (new Image (ResourceManager.REEL[plano - 1], false, true));
			object.addComponent (new Retangulo (REEL_WIDTH * RANGE_TOLERANCE, REEL_HEIGHT * RANGE_TOLERANCE));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, true));
			object.refresh();
			break;
		case PLATAFORMAQUECAI:
			object = world.createEntity ();
			object.setGroup("FALLINGBLOCK");
			object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, obj.height * TILEMAP_FACTOR));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.KINEMATIC, false));
			object.addComponent (new ObjectState ());
			object.refresh();
			break;
		case PLATAFORMAMOVEL:
			object = world.createEntity ();
			object.setGroup("MOVINGPLATFORM");
			object.addComponent (new VaiVolta (Float.parseFloat(obj.props.getProperty("altura")),
												Integer.parseInt(obj.props.getProperty("intervalo")), 
												Integer.parseInt(obj.props.getProperty("t_inicial")),
												"plataforma",
												Integer.parseInt(obj.props.getProperty("direcao"))));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.KINEMATIC, false));
			object.addComponent (new ObjectState ());
			if (PlayState.stage >= 0)
			{
				//TODO: SOLUCAO TEMPORARIA PARA A DEMO - BUSCAR OFFSET DAS PLATAFORMAS
				Image img;
				switch (plano)
				{
				case 1:
					img = new Image (ResourceManager.getPlatformFileName(obj.props.getProperty("tipo"), plano, (int) Math.floor(obj.width / TILE_SIZE)), true, false);
					img.setOffsetX(0);
					img.setOffsetY(15);
					object.addComponent (new Image (ResourceManager.getPlatformFileName(obj.props.getProperty("tipo"), plano, (int) Math.floor(obj.width / TILE_SIZE)), true, false));
					object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, 15));
					break;
				case 2:
					img = new Image (ResourceManager.getPlatformFileName(obj.props.getProperty("tipo"), plano, (int) Math.floor(obj.width / TILE_SIZE)), true, false);
					img.setOffsetX(0);
					img.setOffsetY(23);
					object.addComponent (img);
					object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, 23));
					break;
				case 3:
					img = new Image (ResourceManager.getPlatformFileName(obj.props.getProperty("tipo"), plano, (int) Math.floor(obj.width / TILE_SIZE)), true, false);
					img.setOffsetX(0);
					img.setOffsetY(39);
					object.addComponent (img);
					object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, 39));
					break;
				default:
					img = new Image (ResourceManager.getPlatformFileName(obj.props.getProperty("tipo"), plano, (int) Math.floor(obj.width / TILE_SIZE)), true, false);
					img.setOffsetX(0);
					img.setOffsetY(15);
					object.addComponent (img);
					object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, 15));
					break;
				}
			}
			else
			{
				object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, obj.height * TILEMAP_FACTOR));
			}
			object.addComponent (new MovingPlatform ());
			object.refresh();
			break;
		case PLATAFORMATRANSPONIVEL:
			object = world.createEntity ();
			object.setGroup("PASSABLEBLOCK");
			object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, obj.height * TILEMAP_FACTOR));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, false));
			object.addComponent (new ObjectState ());
			object.refresh();
			break;
		case CHAVE:
			break;
		case CAIXA:
			break;
		case PORTA:
			break;
		case ESPINHO:
			break;
		case TOCO:
			object = world.createEntity ();
			object.setGroup("ENEMY_TOCO");
			object.addComponent (new Andar ());
			object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR- 20, obj.height * TILEMAP_FACTOR - 10));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.DYNAMIC, false));
			object.addComponent (new ObjectState ());
			object.addComponent (new Animacao (ResourceManager.TRUNK, TORA_SPRITE_WIDTH, TORA_SPRITE_HEIGHT, true));
			object.addComponent (new HP (TOCO_HP));
			object.refresh();
			
			for (int i = 0; i < TOCO_HP; i++)
			{
				createString (world, object.getId(), object.getComponent(HP.class), i);
			}
			break;
		case TOCOPULA:
			object = world.createEntity ();
			object.setGroup("ENEMY");
			object.addComponent (new PulaPula ());
			object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR- 20, obj.height * TILEMAP_FACTOR - 10));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.DYNAMIC, false));
			object.addComponent (new ObjectState ());
			object.addComponent (new Animacao (ResourceManager.TRUNK, TORA_SPRITE_WIDTH, TORA_SPRITE_HEIGHT, true));
			object.addComponent (new HP (TOCO_HP));
			object.refresh();
			
			for (int i = 0; i < TOCO_HP; i++)
			{
				createString (world, object.getId(), object.getComponent(HP.class), i);
			}
			break;
		case ARANHA:
			object = world.createEntity ();
			object.setGroup("ENEMY_ARANHA");
			object.addComponent (new VaiVolta (Float.parseFloat(obj.props.getProperty("altura")),
												Integer.parseInt(obj.props.getProperty("intervalo")), 
												Integer.parseInt(obj.props.getProperty("t_inicial")),
												"aranha",
												1));
			object.addComponent (new Retangulo (obj.width * TILEMAP_FACTOR, obj.height * TILEMAP_FACTOR));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.KINEMATIC, false));
			object.addComponent (new ObjectState ());
			object.addComponent (new Animacao (ResourceManager.SPIDER, ARANHA_SPRITE_WIDTH, ARANHA_SPRITE_HEIGHT, true));
			object.addComponent (new HP (ARANHA_HP));
			object.refresh();
			
			for (int i = 0; i < ARANHA_HP; i++)
			{
				createString (world, object.getId(), object.getComponent(HP.class), i);
			}
			break;
		case PARTE1:
			object = world.createEntity ();
			object.setGroup("PICKUP");
			object.addComponent (new PickUp (code.getCode()));
			object.addComponent (new Retangulo (PARTE1_SPRITE_WIDTH, PARTE1_SPRITE_HEIGHT));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, true));
			object.addComponent (new Animacao (ResourceManager.LEVEL1_PART, PARTE1_SPRITE_WIDTH, PARTE1_SPRITE_HEIGHT, true));
			object.refresh();
			break;
		case PARTE2:
			object = world.createEntity ();
			object.setGroup("PICKUP");
			object.addComponent (new PickUp (code.getCode()));
			object.addComponent (new Retangulo (PARTE2_SPRITE_WIDTH, PARTE2_SPRITE_HEIGHT));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, true));
			object.addComponent (new Animacao (ResourceManager.LEVEL2_PART, PARTE2_SPRITE_WIDTH, PARTE2_SPRITE_HEIGHT, true));
			object.refresh();
			break;
		case PARTE3:
			object = world.createEntity ();
			object.setGroup("PICKUP");
			object.addComponent (new PickUp (code.getCode()));
			object.addComponent (new Retangulo (PARTE3_SPRITE_WIDTH, PARTE3_SPRITE_HEIGHT));
			object.addComponent (new Plano (plano));
			object.addComponent (new Posicao (new Vec2 (obj.x * TILEMAP_FACTOR, obj.y * TILEMAP_FACTOR)));
			object.addComponent (new TipoFisica (BodyType.STATIC, true));
			object.addComponent (new Animacao (ResourceManager.LEVEL3_PART, PARTE3_SPRITE_WIDTH, PARTE3_SPRITE_HEIGHT, true));
			object.refresh();
			break;
		default:
			break;
		}
	}
	
	public static void createAgua (World world) {
		int numWaterTiles = (int) Math.floor((PlayState.stageLoader.getMapWidth() * 86.0f + StringAlong.getInstance().width / 2)/ AGUA_SPRITE_WIDTH);
		for (int i = 0; i <= numWaterTiles; i++)
		{
			Entity object = world.createEntity();
			object.setGroup("AGUA");
			object.addComponent (new Animacao (ResourceManager.AGUA, AGUA_SPRITE_WIDTH, AGUA_SPRITE_HEIGHT, false));
			object.addComponent (new Posicao (new Vec2 (i * AGUA_SPRITE_WIDTH, StringAlong.getInstance().height - AGUA_SPRITE_HEIGHT - 40)));
			object.addComponent (new Retangulo (AGUA_SPRITE_WIDTH, AGUA_SPRITE_HEIGHT));
			object.addComponent (new Plano (1));
			object.refresh();
		}
		for (int i = 0; i <= numWaterTiles; i++)
		{
			Entity object = world.createEntity();
			object.setGroup("AGUA");
			object.addComponent (new Animacao (ResourceManager.AGUA, AGUA_SPRITE_WIDTH, AGUA_SPRITE_HEIGHT, false));
			object.addComponent (new Posicao (new Vec2 (i * AGUA_SPRITE_WIDTH, StringAlong.getInstance().height - AGUA_SPRITE_HEIGHT - 40)));
			object.addComponent (new Retangulo (AGUA_SPRITE_WIDTH, AGUA_SPRITE_HEIGHT));
			object.addComponent (new Plano (2));
			object.refresh();
		}
	}
}
