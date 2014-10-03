package menu;

import processing.core.PApplet;
import main.StringAlong;
import managers.ResourceManager;
import states.MenuState;

public class Intro {
	//int timer=0;
	int currentScene=0;
	int cortinaY;
	int fade;
	int introStart;
	int introTimeElapsed;
	boolean fadeOut;

	private StringAlong parent;
	private Interface interf;

	public Intro (Interface interf){
		parent = StringAlong.getInstance();
		this.interf = interf;
	}

	public void start(){
		cortinaY=0;
		currentScene=-1;
		nextScene();
		ResourceManager.loadImage(ResourceManager.INTRO_SKIP);
		ResourceManager.loadImage(ResourceManager.INTRO_SCENE[0]);  //TODO: fazer o RM descarregar as intros no final
		fadeOut=false;
		//timer=140;
		fade=255;

		introStart= parent.millis();
		introTimeElapsed= introStart;

		interf.menusong.pause();
		interf.introsong.play();
		//interf.menuCortina.trigger();
	}

	public void draw()
	{
		introTimeElapsed= parent.millis()-introStart;
		if (currentScene<ResourceManager.NUM_CENAS) parent.image(ResourceManager.getImage(ResourceManager.INTRO_SCENE[currentScene]),0,0);
		parent.fill(0,fade);
		parent.rect(0,0,parent.width,parent.height);
		parent.image(ResourceManager.getImage(ResourceManager.BGCURTAIN),0,cortinaY);
		parent.image(ResourceManager.getImage(ResourceManager.CURTAIN),-128,0);
		parent.fill(255);
		parent.textFont(ResourceManager.getFont(ResourceManager.FONT24));
		parent.textAlign(PApplet.RIGHT);
		StringAlong.getInstance().image(ResourceManager.getImage(ResourceManager.INTRO_SKIP), ResourceManager.getImage(ResourceManager.BGCURTAIN).width-275, StringAlong.getInstance().height-75);
	}

	public void update(){
		if (interf.transitionIn==true) {
			cortinaY-=20;
			if (cortinaY<-767) {
				interf.transitionIn=false;
			}
		}
		if (interf.transitionOut==true) {
			cortinaY+=20;
			if (cortinaY>0) {
				((MenuState)parent.state).interf.gotoTela(3);
				interf.menusong.play();
				interf.menusong.loop();
			}
		}
		if (interf.transitionIn==false && interf.transitionOut==false){

			if (currentScene==0 && introTimeElapsed*0.001   > 2.98   ) startFadeOut();
			if (currentScene==1 && introTimeElapsed*0.001   > 5.96   ) startFadeOut();
			if (currentScene==2 && introTimeElapsed*0.001   > 8.95   ) startFadeOut();
			if (currentScene==3 && introTimeElapsed*0.001   > 11.93   ) startFadeOut();
			if (currentScene==4 && introTimeElapsed*0.001   > 14.91   ) startFadeOut();
			if (currentScene==5 && introTimeElapsed*0.001   > 17.89   ) startFadeOut();
			if (currentScene==6 && introTimeElapsed*0.001   > 20.88   ) startFadeOut();
			if (currentScene==7 && introTimeElapsed*0.001   > 22.37   ) startFadeOut();
			if (currentScene==8 && introTimeElapsed*0.001   > 23.86   ) startFadeOut();
			if (currentScene==9 && introTimeElapsed*0.001   > 25.35   ) startFadeOut();
			if (currentScene==10 && introTimeElapsed*0.001  > 26.84   ) startFadeOut();
			if (currentScene==11 && introTimeElapsed*0.001  > 28.00   ) startFadeOut();
			if (currentScene==12 && introTimeElapsed*0.001  > 30.80   ) startFadeOut();
			if (currentScene==13 && introTimeElapsed*0.001  > 34.20   ) startFadeOut();

			//timer--;
			//if (timer==0){
			//	startFadeOut();
			//}
			if (fadeOut==true && fade<260){
				fade+=20;
				if (fade>=260){
					nextScene();
					if (currentScene==11)interf.introQuebrado.trigger();
				}
			}
			else if (fadeOut==false && fade>0)fade-=10;
		}
	}

	public void startFadeOut(){
		if (fadeOut==false)fadeOut=true;
	}
	public void nextScene(){
		currentScene++;
		if (currentScene<ResourceManager.NUM_CENAS) {
			ResourceManager.loadImage(ResourceManager.INTRO_SCENE[currentScene]);
		}
		//	timer=checkSceneLength(currentScene);
		fadeOut=false;
	}
	public boolean ended(){
		boolean E=false;
		if (currentScene>=14)E=true;
		return E;
	}
	public void end () {
		if (interf.introsong.isPlaying())
			interf.introsong.close();
	}
}
