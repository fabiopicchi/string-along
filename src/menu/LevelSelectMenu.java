package menu;

import main.StringAlong;
import managers.InputManager;
import managers.ResourceManager;

import processing.core.*;
import states.MenuState;
import utils.Digits;
import utils.ImageTransform;

public class LevelSelectMenu extends Menu {

	private final int xLeftPageCenter;
	private final int xRightPageCenter;
	private final int yintro;
	private final int yact;
	private final int ystage1;
	private final int yspacing;
	private final int yseta;
	private int pageY;
	private int starsToDraw;
	
	String actNames [];
	String chapterNames [];
	int pearlsTotal [];

	public LevelSelectMenu(Interface interf){
		super (interf);
		menuItems=11;
		actNames = parent.loadStrings("act_names.txt");
		chapterNames = parent.loadStrings("stage_names.txt");
		start();
		pearlsTotal= new int [StringAlong.stageNumber];
		pearlsTotal[0]=100;
		pearlsTotal[1]=99;
		pearlsTotal[2]=84;
		
		yintro=590;
		yact=280;//original:300
		ystage1=395;//original:415
		yspacing=75;
		yseta=680;
		xLeftPageCenter = parent.width/2-492/2;
		xRightPageCenter = parent.width/2+492/2;

	}
	public void start() {
		ResourceManager.loadImage(ResourceManager.LS_PAGE);
		ResourceManager.loadImage(ResourceManager.LS_COUPLE);
		ResourceManager.loadImage(ResourceManager.LS_BROKENHEART);
		ResourceManager.loadImage(ResourceManager.LS_PEARL);
		ResourceManager.loadAnimation(ResourceManager.LS_PROLOGUE, 269, 45);
		ResourceManager.loadAnimation(ResourceManager.LS_TICKET, 301, 124);
		ResourceManager.loadAnimation(ResourceManager.LS_ACT, 139, 47);
		ResourceManager.loadAnimation(ResourceManager.LS_PARTS, 162, 265);//w=162? //188?
		ResourceManager.loadAnimation(ResourceManager.LS_ARROW, 60, 50);
		ResourceManager.loadAnimation(ResourceManager.LS_STAR, 37, 35);
		ResourceManager.loadFont(ResourceManager.FONT24);
		ResourceManager.loadFont(ResourceManager.FONT18);
		interf.currentSubMenu=0;
		pageY=800;
		super.start();

	}
	public void draw() {
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN),0,0);
		parent.image(ResourceManager.getImage(ResourceManager.CURTAIN), -128, 0);
		parent.image(ResourceManager.getImage(ResourceManager.LS_PAGE), parent.width/2,20+pageY);
		if (interf.currentSubMenu>0) parent.image(ResourceManager.getImage(ResourceManager.LS_PAGE), parent.width/2-ResourceManager.getImage(ResourceManager.LS_PAGE).width,20+pageY);
		parent.imageMode(PApplet.CENTER);
		if (interf.currentSubMenu==0){
			parent.image(ResourceManager.getImage(ResourceManager.LS_COUPLE),xRightPageCenter+20,330+pageY);
			parent.image(ResourceManager.getImage(ResourceManager.LS_BROKENHEART),xRightPageCenter+12,106+pageY);
			
			for (int i=0; i<StringAlong.partCollected.length;i++){
				if (StringAlong.partCollected[i]==true)parent.image(ResourceManager.getAnimation(ResourceManager.LS_PARTS)[i],xRightPageCenter-48,220+pageY);
			}
			
			//drawButton(9,"Watch prologue", xRightPageCenter, yintro+pageY);
			drawWatchPrologue(7, xRightPageCenter, yintro+pageY);
			drawArrow(8,true,xRightPageCenter,yseta+pageY);

		}
		else if (interf.currentSubMenu==1){
			drawActTitle(0,xLeftPageCenter,yact+pageY);
			drawStageTitle(1,0,xLeftPageCenter,ystage1+yspacing*0+pageY);
			drawStageTitle(2,1,xLeftPageCenter,ystage1+yspacing*1+pageY);
			drawStageTitle(3,2,xLeftPageCenter,ystage1+yspacing*2+pageY);
			drawArrow(4,false,xLeftPageCenter,yseta+pageY);

			drawActTitle(1,xRightPageCenter,yact+pageY);
			drawStageTitle(5,3,xRightPageCenter,ystage1+yspacing*0+pageY);
			drawStageTitle(6,4,xRightPageCenter,ystage1+yspacing*1+pageY);
			drawStageTitle(7,5,xRightPageCenter,ystage1+yspacing*2+pageY);
			drawArrow(8,true,xRightPageCenter,yseta+pageY);
		}
		else if (interf.currentSubMenu==2){
			drawActTitle(2,xLeftPageCenter,yact+pageY);
			drawStageTitle(1,6,xLeftPageCenter,ystage1+yspacing*0+pageY);
			drawStageTitle(2,7,xLeftPageCenter,ystage1+yspacing*1+pageY);
			drawStageTitle(3,8,xLeftPageCenter,ystage1+yspacing*2+pageY);

			drawArrow(4,false,xLeftPageCenter,yseta+pageY);

			drawActTitle(3,xRightPageCenter,yact+pageY);
			drawStageTitle(5,9,xRightPageCenter,ystage1+yspacing*0+pageY);
			drawStageTitle(6,10,xRightPageCenter,ystage1+yspacing*1+pageY);
			drawStageTitle(7,11,xRightPageCenter,ystage1+yspacing*2+pageY);

		}
		drawTicket();

	}
	public void update(){
		if (interf.transitionIn==true) transitionIn();
		if (interf.transitionOut==true) transitionOut();
	}
	public void transitionIn(){
		pageY-=pageY*0.2;
		if (pageY<=0){
			pageY=0;
			interf.transitionIn=false;
		}
	}
	public void transitionOut(){
		pageY+=60;
		if (pageY>=800){
			if(interf.nextMenu<4)((MenuState)parent.state).interf.gotoTela(interf.nextMenu);
			if(interf.nextMenu==4) {
				if (interf.menusong.isPlaying())
					interf.menusong.close();
				Interface.startStage(interf.stageToBeLoaded);
			}
		}
	}
	public void select(){

	}
	public int currentStage(){
		int current=0;
		if (interf.currentSelection==1) current=1;
		if (interf.currentSelection==2) current=2;
		if (interf.currentSelection==4) current=3;
		if (interf.currentSelection==5) current=4;
		if (interf.currentSubMenu==2)current+=4;
		return (current);
	}

	public void drawActTitle(int actNumber, int x, int y){
		parent.fill(138,91,60);
		parent.image(ResourceManager.getAnimation(ResourceManager.LS_ACT)[actNumber],x,y);
		parent.textFont(ResourceManager.getFont(ResourceManager.FONT24));
		parent.textAlign(PApplet.CENTER);		
		if (StringAlong.isStageVisible[actNumber*3]==true) parent.text(actNames[actNumber],x,y+55);
		else parent.text("???????",x,y+55);
	}

	public void drawStageTitle(int menuValue, int stageNumber, int x, int y){

		if (InputManager.mouseX>x-200 && InputManager.mouseX<x+200 && InputManager.mouseY>y && InputManager.mouseY<y+40){
			if (InputManager.mouseMoved==true) {
				//isUsingMouse=true;
				interf.currentSelection=menuValue;
			}
			if (InputManager.mouseClicked==true){
				//isUsingMouse=true;
				interf.currentSelection=menuValue;
				((MenuState)parent.state).interf.menuAction();
			}
		}

		starsToDraw=0;
		if(StringAlong.bestTime[stageNumber]>=0){
			starsToDraw=1;
			if(StringAlong.partCollected[stageNumber]==true){
				starsToDraw=2;
				if(StringAlong.everythingCollected[stageNumber]==true){
					starsToDraw=3;
				}
			}
		}
		
		if (interf.currentSelection==menuValue) parent.fill(255,205,105); //TODO: laranja fill(255,205,105)? marrom claro fill (204,125,73);
		else parent.fill(138,91,60);
		parent.textFont(ResourceManager.getFont(ResourceManager.FONT24));
		parent.textAlign(PApplet.LEFT);
		
		if (StringAlong.isStageVisible[stageNumber]==true){
			
			parent.text(chapterNames[stageNumber],x-202,y);
			
			for (int i = 0; i<3; i++){
				if (interf.currentSelection==menuValue) {
					if (i>=starsToDraw) parent.image(ResourceManager.getAnimation(ResourceManager.LS_STAR)[0],x+100+40*i,y);
					else parent.image(ResourceManager.getAnimation(ResourceManager.LS_STAR)[1],x+100+40*i,y);
				}
				else {
					if (i>=starsToDraw) parent.image(ResourceManager.getAnimation(ResourceManager.LS_STAR)[2],x+100+40*i,y);
					else parent.image(ResourceManager.getAnimation(ResourceManager.LS_STAR)[2+starsToDraw],x+100+40*i,y);
				}
			}
			
//			if (StringAlong.partsCollected[stageNumber]==false){
//				if (interf.currentSelection==menuValue) parent.image(ResourceManager.getAnimation(ResourceManager.LS_STAR)[2],x+20,y-10);
//				else parent.image(ResourceManager.getAnimation(ResourceManager.LS_STAR)[0],x+20,y-10);
//			}
			
		}
		else {
			parent.text("????????",x-202,y);
		}
		parent.textFont(ResourceManager.getFont(ResourceManager.FONT18));
		
		if (StringAlong.isStageVisible[stageNumber]==true) {
			if (StringAlong.bestTime[stageNumber]>=0)parent.text("Best time: " + Digits.setDigits((StringAlong.bestTime[stageNumber] / 1000 / 60), 2) + "'" + Digits.setDigits((StringAlong.bestTime[stageNumber] / 1000 % 60), 2) + "''",x-202,y+25);
			else parent.text("(no best time yet!)",x-202,y+25);
		}
		else parent.text("???????",x-202,y+25);

	}

	public void drawTicket(){
		
		if (InputManager.mouseX<200 && InputManager.mouseY>parent.height-120){
			if (InputManager.mouseMoved==true) {
				//isUsingMouse=true;
				interf.currentSelection=0;
			}
			if (InputManager.mouseClicked==true){
				//isUsingMouse=true;
				interf.currentSelection=0;
				((MenuState)parent.state).interf.menuAction();
			}
		}
		
		parent.imageMode(PApplet.CORNER);
		if (interf.currentSelection==0) parent.image(ResourceManager.getAnimation(ResourceManager.LS_TICKET)[1], -65,parent.height-ResourceManager.getAnimation(ResourceManager.LS_TICKET)[0].height+pageY/4);
		else parent.image(ResourceManager.getAnimation(ResourceManager.LS_TICKET)[0], -65,parent.height-ResourceManager.getAnimation(ResourceManager.LS_TICKET)[0].height+pageY/4);
	}
	
	public void drawArrow(int menuValue, boolean isPointingRight, int x, int y){

		if (InputManager.mouseX>x-ResourceManager.getAnimation(ResourceManager.LS_ARROW)[0].width/2 && InputManager.mouseX<x+ResourceManager.getAnimation(ResourceManager.LS_ARROW)[0].width/2 && InputManager.mouseY>y-ResourceManager.getAnimation(ResourceManager.LS_ARROW)[0].height/2 && InputManager.mouseY<y+ResourceManager.getAnimation(ResourceManager.LS_ARROW)[0].height/2){
			if (InputManager.mouseMoved==true) {
				//isUsingMouse=true;
				interf.currentSelection=menuValue;
			}
			if (InputManager.mouseClicked==true){
				//isUsingMouse=true;
				interf.currentSelection=menuValue;
				((MenuState)parent.state).interf.menuAction();
			}
		}
		
		if (isPointingRight==true){
			if (interf.currentSelection==menuValue) parent.image(ResourceManager.getAnimation(ResourceManager.LS_ARROW)[1], x,y);
			else parent.image(ResourceManager.getAnimation(ResourceManager.LS_ARROW)[0], x,y);
		}
		else{
			if (interf.currentSelection==menuValue) ImageTransform.imageMirror(ResourceManager.getAnimation(ResourceManager.LS_ARROW)[1], x,y);
			else ImageTransform.imageMirror(ResourceManager.getAnimation(ResourceManager.LS_ARROW)[0], x,y);
		}
	}

	public void drawButton(int menuValue, String buttonName, int x, int y){
		
		if (InputManager.mouseX>x-200 && InputManager.mouseX<x+200 && InputManager.mouseY>y-30 && InputManager.mouseY<y+30){
			if (InputManager.mouseMoved==true) {
				interf.currentSelection=menuValue;
			}
			if (InputManager.mouseClicked==true){
				interf.currentSelection=menuValue;
				((MenuState)parent.state).interf.menuAction();
			}
		}
		
		parent.textAlign(PApplet.CENTER);
		if(interf.currentSelection==menuValue) parent.fill(255);
		else parent.fill(138,91,60);
		parent.textFont(ResourceManager.getFont(ResourceManager.FONT24));
		parent.text(buttonName, x, y);
	}
	
	public void drawWatchPrologue(int menuValue, int x, int y){
		
		if (InputManager.mouseX>x-200 && InputManager.mouseX<x+200 && InputManager.mouseY>y-30 && InputManager.mouseY<y+30){
			if (InputManager.mouseMoved==true) {
				interf.currentSelection=menuValue;
			}
			if (InputManager.mouseClicked==true){
				interf.currentSelection=menuValue;
				((MenuState)parent.state).interf.menuAction();
			}
		}
		
		if(interf.currentSelection==menuValue) parent.image(ResourceManager.getAnimation(ResourceManager.LS_PROLOGUE)[1], x, y);
		else parent.image(ResourceManager.getAnimation(ResourceManager.LS_PROLOGUE)[0], x, y);
	}
	
	public void changeSubMenu(int newPage){
		
		interf.menuPageFlip.trigger();
		
		interf.currentSubMenu=newPage;
		if (interf.currentSubMenu==0)menuItems=3;
		if (interf.currentSubMenu==1)menuItems=11;
		if (interf.currentSubMenu==2)menuItems=10;

		if (interf.currentSubMenu==0)interf.currentSelection=8;
		if (interf.currentSubMenu==1)interf.currentSelection=1;
		if (interf.currentSubMenu==2)interf.currentSelection=1;
	}
}