package main;

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;

import managers.InputManager;

import dataStructures.Key;

import processing.core.PApplet;

import states.IGameState;
import states.MenuState;
import states.PlayState;

@SuppressWarnings("serial")
public class StringAlong extends PApplet {

	private static StringAlong instance;
	public int timeElapsed;
	private int start;
	public IGameState state;
	private IGameState nextState;
	public static HashMap <Integer, Key> keys = new HashMap <Integer, Key> ();
	private InputManager input;
	private GraphicsEnvironment env;
	private GraphicsDevice device;
	private DisplayMode userDisplayMode;
	private Frame fullScreenFrame = new Frame ();
	

	public static final int stageNumber = 16;
	public static boolean[] isStageVisible = new boolean [stageNumber];
	public static int[] bestTime = new int [stageNumber];
	public static final int stagesPerWorld = 3;
	public static int[] pearlsCollected = new int [stageNumber];
	public static boolean[] partCollected = new boolean [stageNumber];
	public static boolean[] everythingCollected = new boolean [stageNumber];
	public static boolean watchedIntro=false;
	public static boolean watchedSplash=false;
	public final static boolean debug = false;
	
	public static void main (String[] args)
	{
		PApplet.main(new String[] {"main.StringAlong"});
	}

	public StringAlong ()
	{
		instance = this;
	}

	public static StringAlong getInstance ()
	{
		return instance;
	}

	@Override
	public void setup ()
	{	
		size (1024, 768);
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
		userDisplayMode = device.getDisplayMode();
		
		frameRate(30);
		this.cursor(0);
		if (!debug)
		{
			this.state = new MenuState ();
//			this.state = new PlayState ("fase_2_1.tmx", 2);
		}
		else
		{
			String fileName = selectInput();
			this.state = new PlayState (fileName, -1);
		}
		this.nextState = null;
		state.create();

		start = millis();
		timeElapsed = start;
		
		loadGame ();

		this.addKeyListener(input = new InputManager (keys));
	}
	
	public void setFullScreen (boolean set)
	{
		if (set)
		{
			fullScreenFrame.setUndecorated(true);
            frame.remove(this);
            frame.setVisible(false);
            fullScreenFrame.add(this);
            fullScreenFrame.setVisible(true);
            fullScreenFrame.setResizable(false);
            if (device.isFullScreenSupported()) device.setFullScreenWindow(fullScreenFrame);
            if (device.isDisplayChangeSupported()){
            	if ((float)device.getDisplayMode().getWidth() / (float)device.getDisplayMode().getHeight() != 4.0 / 3.0)
            	{
            		setWideScreen (true);
            		device.setDisplayMode(new DisplayMode (1280, 768, 16, DisplayMode.REFRESH_RATE_UNKNOWN));
            	}
            	else
            	{
            		setWideScreen(false);
            		device.setDisplayMode(new DisplayMode (1024, 768, 16, DisplayMode.REFRESH_RATE_UNKNOWN));
            		
//                  boolean GL = false;
//                  if (this.g.getClass().getName().equals("processing.opengl.PGraphicsOpenGL"))
//                  	GL = true;
//            		if (GL)
//            		{
//            			try
//            			{
//		            		PGraphics g = this.g;
//		    				Field context = PGraphicsOpenGL.class.getDeclaredField( "context" );
//		    				context.setAccessible( true ); // make private public
//		    				context.set( g, null );
//		    				
//		    				Method allocate = PGraphicsOpenGL.class.getDeclaredMethod( "allocate" );
//		    				allocate.setAccessible( true );
//		    				allocate.invoke( g );
//		    				
//		    				Field field = PGraphics3D.class.getDeclaredField( "textures" );
//		    				field.setAccessible( true ); // make private variable public! 
//		    				PImage textures[] = (PImage[]) field.get( g );
//		    				
//		    				for( int i = 0; i < textures.length; i++ ){
//		    					if( textures[i] != null ){
//		    						textures[i].updatePixels(); 
//		    					}
//		    				}
//            			}
//            			catch (Exception e)
//            			{
//            				throw new Error (" DAMN MAN ");
//            			}
//            		}
            		
            	}
            }
		}
		else
		{
			fullScreenFrame.remove(this);
			fullScreenFrame.setVisible(false);
			frame.add(this);
			frame.setVisible(true);
			if (!device.getDisplayMode().equals(userDisplayMode)) device.setDisplayMode(userDisplayMode);
		}
		this.requestFocus();
	}
	
	public void setWideScreen (boolean set)
	{
		if (set)
		{
			size (1280, 768);
			frame.setBounds((device.getDisplayMode().getWidth() - width) / 2, (device.getDisplayMode().getHeight() - height) / 2, 1280, 768);
		}
		else
		{
			size (1024, 768);
			frame.setBounds((device.getDisplayMode().getWidth() - width) / 2, (device.getDisplayMode().getHeight() - height) / 2, 1024, 768);
		}
	}
	
	@Override
	public void focusLost()
	{
		if (device.getFullScreenWindow() != null && device.getFullScreenWindow().equals(fullScreenFrame))
			this.requestFocus();
	}
	
	@Override
	public void draw ()
	{	
		if (this.hasNextState())
		{
			state.delete();
			state = nextState;
			nextState = null;

			state.create();
			start = millis();
			timeElapsed = start;
		}

		timeElapsed = millis() - start;
		start = millis();

		input.start();

		state.update();
		state.draw();

		input.end();
	}
	
	@Override
	public void keyPressed() {
	  if (key == ESC) {
	    key = 0;  // Fools! don't let them escape!
	  }
	}

	private boolean hasNextState ()
	{
		return nextState != null;
	}	
	public void switchState (IGameState state)
	{
		nextState = state;
	}
	
	public static boolean isKeyPressed (int keyCode){return keys.get(keyCode).pressed;}	
	public static boolean isKeyJustPressed (int keyCode){return keys.get(keyCode).justPressed;}
	public static boolean isKeyJustReleased (int keyCode){return keys.get(keyCode).justReleased;}
	public static boolean keyPressedFor (int keyCode, int time) {return (keys.get(keyCode).timePressed > 0 && keys.get(keyCode).timePressed <= time);}
	
	public void stop(){
		//TODO:	fechar todas as coisas do minim, tanto efeito sonoro quanto música, por exemplo:
		//		player.close();
		//		song.stop();
		super.stop();
	}
	
	public void loadGame (){
		for (int i = 0; i < stageNumber;i++)
		{
			if (i==0) isStageVisible[i] = true;
			else isStageVisible[i] = false;
			partCollected[i] = false;
			everythingCollected[i] = false;
			bestTime[i] = -1;
		}
	}
}
