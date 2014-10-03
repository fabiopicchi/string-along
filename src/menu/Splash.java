package menu;

import processing.core.PConstants;
import managers.ResourceManager;

public class Splash extends Menu {
	public Splash (Interface interf){
		super (interf);
		menuItems=1;
		ResourceManager.loadImage(ResourceManager.SPLASH_LOGO);
	}
	public void draw(){
		parent.background(255);
		parent.imageMode(PConstants.CENTER);
		parent.image(ResourceManager.getImage(ResourceManager.SPLASH_LOGO), parent.width/2, parent.height/2);
		parent.imageMode(PConstants.CORNER);
		parent.fill(0,interf.fade);
		parent.rect(-128,0,1280,768);
		if (interf.transitionOut==true) interf.fade+=20;
	}
}