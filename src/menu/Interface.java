package menu;

import java.awt.event.KeyEvent;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;

import main.StringAlong;
import managers.ResourceManager;
import managers.InputManager;
import menu.Menu;
import states.PlayState;

public class Interface {
	public int currentMenu=0;
	public int nextMenu=0;
	public int stageToBeLoaded=0;
	public int currentSubMenu=0;
	public int currentSelection=0;
	public float cursorX, cursorY, cursorAimX, cursorAimY;
	public boolean transitionIn=false;
	public boolean transitionOut=false;
	public int timer;
	public int fade;
	private Menu splash;
	private Menu mainMenu;
	private Menu optionsMenu;
	private Menu credits;
	private Menu levelSelectMenu;
	private Intro intro;

	AudioPlayer menusong;
	AudioPlayer introsong;
	Minim minim;
	AudioSample menuPageFlip;
	AudioSample introQuebrado;
	AudioSample menuCortina;

	public Interface() {
		init();
	}

	private void init() {
		mainMenu = new MainMenu(this);
		optionsMenu = new OptionsMenu(this);
		credits = new Credits(this);
		intro = new Intro(this);
		splash = new Splash(this);
		levelSelectMenu = new LevelSelectMenu(this);
		mainMenu.start();
		levelSelectMenu.start();
		minim = new Minim(StringAlong.getInstance());

		if (StringAlong.watchedSplash==true) {
			gotoTela(3);
			loadMenuSounds();
			menusong.play();
			menusong.loop();		
		}
	}

	private void loadMenuSounds(){
		menuPageFlip=minim.loadSample("sound/pagina.wav");
		menuCortina=minim.loadSample("sound/cortina.wav");
		introQuebrado=minim.loadSample("sound/quebrado.wav");
		menusong = minim.loadFile("sound/menu.mp3");
	}

	public void gotoTela(int T){
		if (T==1){
			mainMenu.start();
		}
		if (T==2){
			introsong = minim.loadFile("sound/intro.mp3", 8192);
			intro.start();
		}
		//		if (T==3){
		//			levelSelectMenu.start();
		//		}
		currentMenu=T;
		currentSubMenu=0;
		currentSelection=0;
		if (T==3) currentSelection=8;
		cursorX=290;
		transitionOut=false;
		transitionIn=true;
	}
	public void drawSplash(){
		splash.draw();
		if (StringAlong.getInstance().millis()>4000 || InputManager.mouseClicked==true || StringAlong.isKeyJustPressed(KeyEvent.VK_ENTER) || StringAlong.isKeyJustPressed(KeyEvent.VK_SPACE)) menuAction();
		if (fade>355){
			gotoTela(nextMenu);
			loadMenuSounds();
			menusong.play();
			menusong.loop();
		}
	}
	public void drawMainMenu(){
		if (StringAlong.isKeyJustPressed(KeyEvent.VK_ENTER) || StringAlong.isKeyJustPressed(KeyEvent.VK_SPACE)) menuAction();
		if (currentSubMenu==0){
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP))mainMenu.menuUp();
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN))mainMenu.menuDown();			
			mainMenu.draw();
			mainMenu.update();
		}
		else if (currentSubMenu==1){
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP)) optionsMenu.menuUp();
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN)) optionsMenu.menuDown();
			if (currentSelection<2 && (StringAlong.isKeyJustPressed(KeyEvent.VK_LEFT)||StringAlong.isKeyJustPressed(KeyEvent.VK_RIGHT))) {
				menuAction();
			}
			mainMenu.draw();
			optionsMenu.draw();
			optionsMenu.update();
		}
		else if (currentSubMenu==2){
			if (InputManager.mouseClicked==true)menuAction();
			credits.draw();
			credits.update();
		}
	}

	public void drawIntro(){
		if (StringAlong.isKeyJustPressed(KeyEvent.VK_ESCAPE) || intro.ended()==true){
			menuAction();
		}
		intro.draw();
		intro.update();
	}

	public void drawLevelSelect(){
		if (StringAlong.isKeyJustPressed(KeyEvent.VK_ENTER)||StringAlong.isKeyJustPressed(KeyEvent.VK_SPACE)) menuAction();

		if (currentSubMenu==0){
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP)){
				if (currentSelection==0)currentSelection=8;
				else if (currentSelection==8)currentSelection=7;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN)){
				if (currentSelection==7)currentSelection=8;
				else if (currentSelection==8)currentSelection=0;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_LEFT)){
				currentSelection=0;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_RIGHT)){
				if (currentSelection==0)currentSelection=8;
			}
		}
		else if (currentSubMenu==1){

			if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP)){
				if (currentSelection==0)currentSelection=4;
				else if (currentSelection>1 && currentSelection !=5) currentSelection-=1;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN)){
				if (currentSelection==4 || currentSelection==8)currentSelection=0;
				else if (currentSelection>0)currentSelection+=1;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_LEFT)){
				currentSelection-=4;
				if (currentSelection<0) currentSelection=0;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_RIGHT)){
				if (currentSelection<5)currentSelection+=4;
			}
		}

		else if (currentSubMenu==2){
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_UP)){
				if (currentSelection==0)currentSelection=4;
				else if (currentSelection>1 && currentSelection !=5) currentSelection-=1;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_DOWN)){
				if (currentSelection==4)currentSelection=0;
				if (currentSelection==7)currentSelection=4;
				else if (currentSelection>0 && currentSelection<8)currentSelection+=1;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_LEFT)){
				currentSelection-=4;
				if (currentSelection<0) currentSelection=0;
			}
			if (StringAlong.isKeyJustPressed(KeyEvent.VK_RIGHT)){
				if (currentSelection<5) currentSelection+=4;
			}
		}

		StringAlong.getInstance().image(ResourceManager.getImage(ResourceManager.CURTAIN),-128,0);
		levelSelectMenu.draw();
		levelSelectMenu.update();
	}



	public void menuAction(){

		if(transitionOut==false){

			if (currentMenu==0){
				startTransitionOut(1);
				StringAlong.watchedSplash=true;
			}
			//tela de título
			else if (currentMenu==1){
				if (currentSubMenu==0){
					if (currentSelection==0){
						if (StringAlong.watchedIntro==false){
							startTransitionOut(2);
							StringAlong.watchedIntro=true;
						}
						else startTransitionOut(3);
					}
					else if (currentSelection==1){
						//currentSubMenu=1;
						//currentSelection=0;
					}
					else if (currentSelection==2){
						currentSubMenu=2;
						currentSelection=0;
					}
					else if (currentSelection==3)exitGame();
				}
				else if (currentSubMenu==1){
					if (currentSelection==0)toggleSound();
					else if (currentSelection==1)toggleFullScreen();
					else if (currentSelection==2){
						currentSubMenu=0;
						currentSelection=1;
					}
				}
				else if (currentSubMenu==2){
					currentSubMenu=0;
					currentSelection=2;
				}
			}

			//intro
			else if (currentMenu==2){
				intro.end();
				currentSubMenu=0;
				currentSelection=6;
				startTransitionOut(3);
			}

			//levelselect
			else if (currentMenu==3){
				if (currentSubMenu==0){
					if (currentSelection==0)startTransitionOut(1);
					else if (currentSelection==7)startTransitionOut(2);
					else if (currentSelection==8)levelSelectMenu.changeSubMenu(1);
				}
				else if (currentSubMenu==1){
					if (currentSelection==0)startTransitionOut(1);
					else if (currentSelection==4)levelSelectMenu.changeSubMenu(0);
					else if (currentSelection==8)levelSelectMenu.changeSubMenu(2);
					else{
						if(StringAlong.isStageVisible[getStageNumber(currentSelection, currentSubMenu)]==true){
							startTransitionOut(4);
							stageToBeLoaded=getStageNumber(currentSelection, currentSubMenu);
						}
					}
				}
				else if (currentSubMenu==2){
					if (currentSelection==0)startTransitionOut(1);
					else if (currentSelection==4)levelSelectMenu.changeSubMenu(1);
					else{
						if(StringAlong.isStageVisible[getStageNumber(currentSelection, currentSubMenu)]==true){
							startTransitionOut(4);
							stageToBeLoaded=getStageNumber(currentSelection, currentSubMenu);
						}
					}
				}
			}
		}
	}
	public void restartLevel(){

	}
	public static void exitGame(){
		StringAlong.getInstance().exit();
	}
	public static int getStageNumber(int selection, int submenu){
		int S=selection;
		S--;
		if (selection>4)S--;
		S+=6*(submenu-1);
		return(S);
	}
	public static void startStage(int A){
		int world= A/StringAlong.stagesPerWorld+1;
		int stage = A%StringAlong.stagesPerWorld+1;
		StringAlong.getInstance().switchState(new PlayState("fase_"+world+"_"+stage+".tmx", A));
	}

	public void toggleSound(){

	}
	public void toggleFullScreen(){

	}

	public void startTransitionOut(int A){
		transitionOut=true;
		transitionIn=false;
		nextMenu=A;
	}
}