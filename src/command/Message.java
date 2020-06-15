package command;

import java.io.Serializable;

public class Message implements Serializable {

	public static final int
			ADD = 0,
			ACCEPTADD = 1,
			OPPONENT = 2,
			ACCEPTOPPONENT = 3,
			FULLOPPONENT = 4,
			SHOOT = 5,
			HIT = 6,
			MISS = 7,
			WIN = 8;
	private String msg;
	private int type;
	private boolean bool;
	private int posX;
	private int posY;

	public Message(int type, String msg) {
		this.setType(type); 
		this.setMsg(msg);
	}

	public Message(int type, Boolean bool) {
		this.setType(type);
		this.setBool(bool);
	}

	public Message(int type, int posX, int posY) {
		this.setType(type);
		this.setPosX(posX);
		this.setPosY(posY);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
}
