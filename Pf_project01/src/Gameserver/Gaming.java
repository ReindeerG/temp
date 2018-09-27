package Gameserver;

import java.io.Serializable;
import java.util.List;

public class Gaming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_UNREADY = 1;
	public static final int GAME_READY = 2;
	public static final int GAME_DIE = 10;
	public static final int GAME_GO = 11;
	
	
	private int who;
	private int what;
	
	private List<Player> players;
	
	public Gaming(int what) {
		setWhat(what);
	}
	public Gaming(int who, int what, List<Player> players) {
		setWho(who); setWhat(what); setPlayers(players);
	}
	
	public int getWhat() {
		return what;
	}
	public void setWhat(int what) {
		this.what = what;
	}
	public int getWho() {
		return who;
	}
	public void setWho(int who) {
		this.who = who;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}
