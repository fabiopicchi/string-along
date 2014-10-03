package states;

import managers.ResourceManager;
import menu.Interface;

public class MenuState implements IGameState {
	public Interface interf;

	@Override
	public void create() {
		interf = new Interface();
		ResourceManager.prepareIntroSceneFileNames();
	}

	@Override
	public void update() {
	}

	@Override
	public void draw() {
		if (interf.currentMenu==0) interf.drawSplash();
		else if (interf.currentMenu==1) interf.drawMainMenu();
		else if (interf.currentMenu==2) interf.drawIntro();
		else if (interf.currentMenu==3) interf.drawLevelSelect();
	}

	@Override
	public void delete() {
		ResourceManager.unloadResources();
	}

}
