package menu;

import managers.ResourceManager;

public class Credits extends Menu {
	
	public Credits (Interface interf){
		super (interf);
		menuItems=1;
	}
	public void draw(){
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN),0,0);
		parent.image(ResourceManager.getImage(ResourceManager.SPOTLIGHT),0,0);
		parent.image(ResourceManager.getImage(ResourceManager.HAND), interf.cursorX,interf.cursorY-ResourceManager.getImage(ResourceManager.HAND).height+54);
		parent.image(ResourceManager.getImage(ResourceManager.CREDITS_IMG), 140, 80);
		parent.image(ResourceManager.getImage(ResourceManager.CURTAIN), -128, 0);
	}
	public void update(){
		interf.cursorAimX= 28;
		interf.cursorAimY= parent.height-168;
		interf.cursorX-=(interf.cursorX-interf.cursorAimX)*0.2;
		interf.cursorY-=(interf.cursorY-interf.cursorAimY)*0.2;
	}
}