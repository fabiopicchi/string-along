package menu;

import processing.core.PImage;

public class ResultsMenu extends Menu {
	PImage resultScreen;
	PImage mao;
	public float cursorY, selectY;
	int	x=280;
	int y=365;
	int spacing=49;
	public ResultsMenu(Interface interf){
		super (interf);
		menuItems=2;
		resultScreen=parent.loadImage("results.png");
		mao=parent.loadImage("mao.png");
	}
	public void start(){
	      super.start();
	      cursorY=0;
		}
	public void draw(){
		parent.image(resultScreen, 0, 0);
		parent.image(mao, x,cursorY);
	}
	public void update(){
		selectY=y+interf.currentSelection*spacing;
		cursorY-=(cursorY-selectY)*0.2;
	}
}
