package model;



public class Ship {

	public int size;
	public boolean vertical;
	public int hp;

	public Ship(int type, boolean vertical) {
		this.size = type;
		this.vertical = vertical;
		hp = size;
	}
	
	public void hit() {
		hp--;
	}
	
	public boolean isAlive() {
		return hp > 0;
	}
	
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}


}
