package managers;

import java.util.HashMap;
import java.util.Iterator;

import main.StringAlong;

import processing.core.PFont;
import processing.core.PImage;

public class ResourceManager {

	public static final String LEVEL1_PART = "lady1.png";
	public static final String LEVEL2_PART = "lady2.png";
	public static final String LEVEL3_PART = "lady3.png";
	public static final String [] PEARL = {"pickup1.png", "pickup2.png", "pickup3.png"};
	public static final String PEARLHUD = "pickupHUD.png";
	public static final String [] REEL = {"carretel1.png", "carretel2.png", "carretel3.png"};
//	public static final String TRUNK = "toco_spritesheet_grande.png";
	public static final String [] TRUNK = {"spriteSheet-tora_1.png", "spriteSheet-tora_2.png", "spriteSheet-tora_3.png"};
	public static final String TRUNK_ID = "tora";
//	public static final String SPIDER = "aranha1.png";
	public static final String [] SPIDER = {"aranha1.png", "aranha2.png", "aranha3.png"};
	public static final String SPIDER_ID = "aranha";
	public static final String [] PLAYER = {"boneco1.png", "boneco2.png", "boneco3.png"};
	public static final String PLAYER_ID = "boneco";
	public static final String PAUSE_SCREEN = "pause.png";
	public static final String END_SCREEN = "endgame.png";
	public static final String END_SCREEN2 = "endgame2.png";
	public static final String DEMO_END = "demo-end.jpg";
	public static final String BACKGROUND_MOON = "fundo.jpg";
	public static final String AGUA = "agua.png";

	public static final String HAND = "mao.png";
	public static final String SPOTLIGHT = "spotlight2.png";
	public static final String CURTAIN = "cortina.png";
	public static final String BGCURTAIN = "cortina_fundo.png";
	public static final String SPLASH_LOGO = "catavento.jpg";
	public static final String TITLE_LOGO = "title_logo.png";
	public static final String TITLE_ITEMS = "title_items.png";
	public static final String OPTIONS_IMG = "options_ticket.png";
	public static final String OPTIONS_DOT = "options_dot.png";
	public static final String OPTIONS_ARROW = "options_arrow.png";
	public static final String CREDITS_IMG = "creditos.png";
	public static final String INTRO_SKIP = "intro_skip.png";
	public static final String LS_PAGE = "levelselect_booklet.png";
	public static final String LS_TICKET = "levelselect_ticket_spritesheet.png";
	public static final String LS_PROLOGUE = "levelselect_prologue_spritesheet.png";
	public static final String LS_ACT = "levelselect_act_spritesheet.png";
	public static final String LS_PARTS = "levelselect_parts_spritesheet.png";
	public static final String LS_ARROW = "levelselect_arrow_spritesheet.png";
	public static final String LS_STAR = "levelselect_star_spritesheet.png";
	public static final String LS_COUPLE = "levelselect_couple.png";
	public static final String LS_BROKENHEART = "levelselect_brokenheart.png";
	public static final String LS_PEARL = "levelselect_pearl.png";
	public static final int NUM_CENAS = 14;
	public static final String[] INTRO_SCENE = new String [NUM_CENAS];

	//		public static final String STRING = "sprite_fios.png";
	public static final String AACHEN34 = "StringAlong-Regular-34.vlw";
	public static final String AACHEN39 = "StringAlong-Regular-39.vlw";
	public static final String FONT18 = "StringAlong-Regular-18.vlw";
	public static final String FONT24 = "StringAlong-Regular-24.vlw";

	public static final String PNG = ".png";
	public static final String CAMADA_1 = "_1_";
	public static final String CAMADA_2 = "_2_";
	public static final String CAMADA_3 = "_3_";
	public static final String LEVEL_LAYER = "fase_";
	public static final int NUM_IMAGENS1 = 11;
	public static final int NUM_IMAGENS2 = 8;
	public static final int NUM_IMAGENS3 = 6;
	public static final String[] LEVEL_LAYER_1 = new String [NUM_IMAGENS1];
	public static final String[] LEVEL_LAYER_2 = new String [NUM_IMAGENS2];
	public static final String[] LEVEL_LAYER_3 = new String [NUM_IMAGENS3];

	private static final HashMap <String, PFont> FONTS = new HashMap <String, PFont> ();
	private static final HashMap <String, PImage> STATIC_IMAGES = new HashMap <String, PImage> ();
	private static final HashMap <String, PImage[]> ANIMATIONS = new HashMap <String, PImage[]> ();

	public static void prepareStageSpecificFileNames (int stage)
	{
		System.out.println ("STAGE: " + stage);
		for (int i = 0; i < NUM_IMAGENS1; i++)
		{
			LEVEL_LAYER_1[i] = LEVEL_LAYER + (stage / StringAlong.stagesPerWorld + 1) + "_" + (stage % StringAlong.stagesPerWorld + 1) + CAMADA_1 + (i + 1) + PNG;
		}
		for (int i = 0; i < NUM_IMAGENS2; i++)
		{
			LEVEL_LAYER_2[i] = LEVEL_LAYER + (stage / StringAlong.stagesPerWorld + 1) + "_" + (stage % StringAlong.stagesPerWorld + 1) + CAMADA_2 + (i + 1) + PNG;
		}
		for (int i = 0; i < NUM_IMAGENS3; i++)
		{
			LEVEL_LAYER_3[i] = LEVEL_LAYER + (stage / StringAlong.stagesPerWorld + 1) + "_" + (stage % StringAlong.stagesPerWorld + 1) + CAMADA_3 + (i + 1) + PNG;
		}
	}
	public static void prepareIntroSceneFileNames() { //TODO: Perguntar pro picchi se é assim mesmo.
		for (int i = 0; i < NUM_CENAS; i++){
			INTRO_SCENE[i] = "intro/"+i+".jpg";
		}
	}

	public static String getPlatformFileName (String tipo, int plano, int largura)
	{
		return "p_" + tipo + "_" + plano + "_" + largura + PNG;
	}
	
	public static void loadFont (String fileName)
	{
		if (!FONTS.containsKey(fileName))
		{
			FONTS.put(fileName, StringAlong.getInstance().loadFont(fileName));
		}
	}

	public static void unloadFont (String fileName)
	{
		if (FONTS.containsKey(fileName))
		{
			FONTS.get(fileName).removeCache(StringAlong.getInstance().g);
			FONTS.remove(fileName);
		}
	}

	private static void unloadFontIt (String fileName)
	{
		if (FONTS.containsKey(fileName))
		{
			FONTS.get(fileName).removeCache(StringAlong.getInstance().g);
		}
	}
	
	public static PFont getFont (String fileName)
	{
		return FONTS.get(fileName);
	}

	public static void loadImage (String fileName)
	{
		if (!STATIC_IMAGES.containsKey(fileName))
		{
			STATIC_IMAGES.put(fileName, StringAlong.getInstance().requestImage(fileName));
		}
	}

	public static void unloadImage (String fileName)
	{
		if (STATIC_IMAGES.containsKey(fileName))
		{
			removeImage(STATIC_IMAGES.get(fileName));
			STATIC_IMAGES.remove(fileName);
		}
	}

	private static void unloadImageIt (String fileName)
	{
		if (STATIC_IMAGES.containsKey(fileName))
		{
			removeImage(STATIC_IMAGES.get(fileName));
		}
	}
	
	public static PImage getImage (String fileName)
	{
		return STATIC_IMAGES.get(fileName);
	}

	public static void loadAnimation (PImage spriteSheet, String fileName, int spriteWidth, int spriteHeight)
	{
		if (!ANIMATIONS.containsKey(fileName))
		{
			int cols = spriteSheet.width / spriteWidth;
			int rows = spriteSheet.height / spriteHeight;
			PImage [] animation = new PImage [cols*rows];

			for (int i = 0; i < cols; i++) 
				for (int j = 0; j < rows; j++) 
					animation[i + j * cols] = spriteSheet.get(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);

			unloadImage (fileName);
			ANIMATIONS.put(fileName, animation);
		}
	}
	
	public static void loadAnimation (String fileName, int spriteWidth, int spriteHeight)
	{
		if (!ANIMATIONS.containsKey(fileName))
		{
			PImage spriteSheet = StringAlong.getInstance().loadImage(fileName); 
			int cols = spriteSheet.width / spriteWidth;
			int rows = spriteSheet.height / spriteHeight;
			PImage [] animation = new PImage [cols*rows];

			for (int i = 0; i < cols; i++) 
				for (int j = 0; j < rows; j++) 
					animation[i + j * cols] = spriteSheet.get(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);

			removeImage (spriteSheet);
			ANIMATIONS.put(fileName, animation);
		}
	}

	public static void unloadAnimation (String fileName)
	{
		if (ANIMATIONS.containsKey(fileName))
		{
			PImage [] animation = ANIMATIONS.get(fileName);
			for (int i = 0; i < animation.length; i++)
			{
				removeImage(animation[i]);
			}
			ANIMATIONS.remove(fileName);
		}
	}

	private static void unloadAnimationIt (String fileName)
	{
		if (ANIMATIONS.containsKey(fileName))
		{
			PImage [] animation = ANIMATIONS.get(fileName);
			for (int i = 0; i < animation.length; i++)
			{
				removeImage(animation[i]);
			}
		}
	}
	
	public static PImage[] getAnimation (String fileName)
	{
		if (ANIMATIONS.containsKey(fileName))
		{
			return ANIMATIONS.get(fileName);
		}
		return null;
	}

	public static void unloadResources ()
	{
		Iterator <String> it = STATIC_IMAGES.keySet().iterator();

		while (it.hasNext())
		{
			unloadImageIt (it.next());
			it.remove();
		}

		it = ANIMATIONS.keySet().iterator();

		while (it.hasNext())
		{
			unloadAnimationIt (it.next());
			it.remove();
		}
		
		it = FONTS.keySet().iterator();
		
		while (it.hasNext())
		{
			unloadFontIt (it.next());
			it.remove();
		}
	}

	private static void removeImage(PImage image) 
	{
		image.removeCache (StringAlong.getInstance().g);
		image.removeParams (StringAlong.getInstance().g);
		image.delete();
	}
}
