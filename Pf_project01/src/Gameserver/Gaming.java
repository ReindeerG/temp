package Gameserver;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gaming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int GAME_UNREADY = 1;
	public static final int GAME_READY = 2;
	public static final int GAME_START = 3;
	public static final int GAME_DIE = 10;
	public static final int GAME_GO = 11;
	public static final int GETCARD = 500;
	public static final int CHAT = 330;
	public static final int CHAT_JOIN = 331;
	public static final int CHAT_LEAVE = 332;
	public static final int CHAT_NICKCHANGE = 333;
	
	
	private int who;
	private String userid;
	private int what;
	private ArrayList<Player> players;
	
	private String msg;
	private String date;
	
	private int card1;
	private int card2;
	private int card3;
	
	public Gaming(int what) {
		setWhat(what);
	}

//	public Gaming(Socket socket, int what, String msg) {
//		setSocket(socket); setWhat(what); setMsg(msg);
//	}
	// 일반 채팅 송출
	public Gaming(String userid, int what, String msg, String date) {
		setUserid(userid); setWhat(what); setMsg(msg); setDate(date);
	}
	// 채팅 알림(입장, 퇴장) - 서버용 
	public Gaming(String userid, int what, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setDate(date); setPlayers(players);
	}
	// 채팅 알림(닉변경) - 서버용 
	public Gaming(String userid, int what, String toNick, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setMsg(toNick); setDate(date); setPlayers(players);
	}
	// 채팅 알림(닉변경) - 클라용 
	public Gaming(String userid, int what, String toNick) {
		setUserid(userid); setWhat(what); setMsg(toNick);
	}
	// 일반 게임 명령, 서버용
	public Gaming(String userid, int what, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setPlayers(players);
	}
	// 일반 게임 명령, 채팅 알림(입장, 퇴장) - 클라용
	public Gaming(String userid, int what) {
		setUserid(userid); setWhat(what);
	}
	
	
	// 카드 나눠받을 때
	public Gaming(int what, int card1, int card2, int card3) {
		setWhat(what); this.setCard1(card1); this.setCard2(card2); this.setCard3(card3);
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getCard1() {
		return card1;
	}

	public void setCard1(int card1) {
		this.card1 = card1;
	}

	public int getCard2() {
		return card2;
	}

	public void setCard2(int card2) {
		this.card2 = card2;
	}

	public int getCard3() {
		return card3;
	}

	public void setCard3(int card3) {
		this.card3 = card3;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

}
