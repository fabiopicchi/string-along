package menu;

import managers.InputManager;
import managers.ResourceManager;
import states.MenuState;

public class OptionsMenu extends Menu {
	boolean sound=true;
	boolean fullscreen=false;
	public OptionsMenu(Interface interf){
		super (interf);
		menuItems=3;
		ResourceManager.loadImage(ResourceManager.OPTIONS_IMG);
		ResourceManager.loadImage(ResourceManager.OPTIONS_DOT);
		ResourceManager.loadImage(ResourceManager.OPTIONS_ARROW);
	}
	public void draw(){
		
		parent.fill (0,128);
		parent.rect(0,0,parent.width,parent.height);
		parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_IMG),37,-99);
		if (sound==true) parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_DOT),505,400);
		else parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_DOT),659,357);
		if (fullscreen==true) parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_DOT),606,441);
		else parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_DOT),726,406);
		if (interf.currentSelection==0) parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_ARROW),218,463);
		if (interf.currentSelection==1) parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_ARROW),237,524);
		if (interf.currentSelection==2) parent.image(ResourceManager.getImage(ResourceManager.OPTIONS_ARROW),262,605);
	}
	public void update(){
		
		for (int i = 2; i>=0; i--){
			if(InputManager.mouseY>=InputManager.mouseX*-0.28+513+72*i) {
				if (InputManager.mouseMoved==true) {
					//isUsingMouse=true;
					interf.currentSelection=i;
				}
				if (InputManager.mouseClicked==true){
					//isUsingMouse=true;
					interf.currentSelection=i;
					((MenuState)parent.state).interf.menuAction();
				}
				break;
			}
		}
	}
	public void toggleSound(){
		if (sound==true){
			sound=false;
		}
		else{
			sound=true;
		}
	}
	public void toggleFullScreen(){
		if (fullscreen==true){
			fullscreen=false;
		}
		else{
			fullscreen=true;
		}		
	}
}