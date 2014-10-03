package menu;

import processing.core.*;

public class PauseMenu extends Menu {
	PImage pauseScreen;
	PImage mao;
	public float cursorY, selectY;
	int	x=280;
	int y=365;
	int spacing=45;
	public PauseMenu(Interface interf){
		super (interf);
		menuItems=4;
		pauseScreen=parent.loadImage("pause.png");
		mao=parent.loadImage("mao.png");
	}
	public void start(){
	      super.start();
	      cursorY=0;
		}
	public void draw(){
		parent.image(pauseScreen, 0, 0);
		parent.image(mao, x,cursorY);
	}
	public void update(){
		selectY=y+interf.currentSelection*spacing;
		cursorY-=(cursorY-selectY)*0.2;
	}
}