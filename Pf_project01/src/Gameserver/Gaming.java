package Gameserver;

import java.io.Serializable;

public class Gaming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_UNREADY = 1;
	public static final int GAME_READY = 2;
	public static final int GAME_DIE = 10;
	public static final int GAME_GO = 11;
	private int what;
	public Gaming(int n) {
		setWhat(n);
	}
	public int getWhat() {
		return what;
	}
	public void setWhat(int what) {
		this.what = what;
	}
}
