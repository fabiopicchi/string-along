package menu;

import main.StringAlong;
import managers.InputManager;
import managers.ResourceManager;
import states.MenuState;

public class MainMenu extends Menu {

	final int x=290;
	final int y=430;
	final int spacing=61;
	int logoY, logoVY, logoBounces, g;
	float titleItemsY;
	int spotAlpha;

	public MainMenu(Interface interf){
		super (interf);
		menuItems=4;
		ResourceManager.loadImage(ResourceManager.BGCURTAIN);
		ResourceManager.loadImage(ResourceManager.CURTAIN);
		ResourceManager.loadImage(ResourceManager.SPOTLIGHT);
		ResourceManager.loadImage(ResourceManager.HAND);
		ResourceManager.loadImage(ResourceManager.TITLE_LOGO);
		ResourceManager.loadImage(ResourceManager.TITLE_ITEMS);
		ResourceManager.loadImage(ResourceManager.CREDITS_IMG);
	}
	
	public void start(){
		super.start();
		interf.cursorY=-150000;
		logoY=-550;
		logoVY=18;
		logoBounces=0;
		titleItemsY=8000;
		g=2;
		spotAlpha=0;
	}

	
	public void draw(){
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN),0,0);
		parent.tint(255,spotAlpha);
		parent.image(ResourceManager.getImage(ResourceManager.SPOTLIGHT),0,0);
		parent.tint(255);
		parent.image(ResourceManager.getImage(ResourceManager.HAND), interf.cursorX,interf.cursorY-ResourceManager.getImage(ResourceManager.HAND).height+54);
		parent.image(ResourceManager.getImage(ResourceManager.TITLE_LOGO),
				parent.width/2-ResourceManager.getImage(ResourceManager.TITLE_LOGO).width/2+6*(float)Math.sin(StringAlong.getInstance().millis()*0.0015f),
				logoY);
		parent.image(ResourceManager.getImage(ResourceManager.TITLE_ITEMS),parent.width/2-ResourceManager.getImage(ResourceManager.TITLE_ITEMS).width/2+10,titleItemsY);
		parent.image(ResourceManager.getImage(ResourceManager.CURTAIN), -128, 0);
		if (interf.fade>0){
			parent.fill(0,interf.fade);
			parent.rect(-128,0,1280,768);
			interf.fade-=20;
		}
	}
	
	public void update(){
		if (interf.transitionIn==true) transitionIn();
		if (interf.transitionOut==true) transitionOut();
		interf.cursorAimX= x;
		interf.cursorAimY=y+interf.currentSelection*spacing;
		interf.cursorX-=(interf.cursorX-interf.cursorAimX)*0.2;
		if (interf.transitionOut==false)interf.cursorY-=(interf.cursorY-interf.cursorAimY)*0.2;
		if(interf.transitionIn==true && titleItemsY<442) {
			interf.transitionIn=false;
		}
		for (int i =0; i<menuItems;i++){
			if (InputManager.mouseX>x && InputManager.mouseX<x+400 && InputManager.mouseY>y+i*spacing && InputManager.mouseY<y+(i+1)*spacing){
				if (InputManager.mouseMoved==true) interf.currentSelection=i;
				if (InputManager.mouseClicked==true) ((MenuState)parent.state).interf.menuAction();
			}
		}
	}

	public void transitionIn(){
		spotAlpha+=30;
		if (spotAlpha>255)spotAlpha=255;
		titleItemsY-=(titleItemsY-440)*0.2;
		logoY+=logoVY;
		if (logoBounces==0 && logoY>0){
			logoY=0;
			logoBounces=1;
			logoVY= -logoVY/3;
		}
		else if (logoBounces==1){
			logoVY+=g;
			if (logoY>0){
				logoY=0;
				logoBounces=2;
				logoVY= -logoVY/3;
			}
		}
		else if (logoBounces==2){
			logoVY+=g;
			if (logoY>0){
				logoY=0;
				logoBounces=3;
				logoVY=0;
			}
		}
	}
	public void transitionOut(){
		spotAlpha-=30;
		logoY+=logoVY;
		logoVY-=g;
		interf.cursorY-=18;
		titleItemsY+=20;
		if (titleItemsY>1000)((MenuState)parent.state).interf.gotoTela(interf.nextMenu);
	}
}