package menu;

import main.StringAlong;

public class Menu {
	
	public int menuItems;
	protected StringAlong parent;
	protected Interface interf;
	
	public Menu (Interface interf){
		parent = StringAlong.getInstance();
		this.interf = interf;
	}
	public void start(){
	}
	public void draw(){

	}
	public void update(){

	}
	public void menuUp(){
		interf.currentSelection--;
		if (interf.currentSelection<0)interf.currentSelection=menuItems-1;
	}
	public void menuDown(){
		interf.currentSelection++;
		if (interf.currentSelection>=menuItems)interf.currentSelection=0;	
	}
	public void changeSubMenu(int newSubMenu){
		interf.currentSubMenu=newSubMenu;
		interf.currentSelection=0;
	}
}