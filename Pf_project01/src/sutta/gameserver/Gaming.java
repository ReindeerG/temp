package sutta.gameserver;

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
	public static final int PANDON = 4;
	public static final int MUCHPANDON = 5;
	public static final int GAME_DIE = 10;
	public static final int GAME_CALL = 11;
	public static final int GAME_HALF = 12;
	public static final int GAME_CHECK = 13;
	public static final int MONEY_REFRESH = 89;
	public static final int TURN_REFRESH = 87;
	public static final int RESETCARDS = 501;
	public static final int GETCARD = 500;
	public static final int DRAW2 = 505;
	public static final int GAME_WHOSTURN = 600;
	public static final int GAME_TIMER = 606;
	public static final int GAME_RESULT = 608;
	public static final int GAME_RESULT_OK = 688;
	public static final int BUTTON_OK = 609;
	public static final int CHAT = 330;
	public static final int CHAT_JOIN = 331;
	public static final int CHAT_LEAVE = 332;
	public static final int CHAT_NICKCHANGE = 333;
	public static final int CHAT_WIN = 335;
	public static final int CHAT_RE = 336;
	public static final int BAN = 444;
	public static final int IMBANNED = 445;
	public static final int REFRESH = 88;
	public static final int IDMATCH = 77;
	
	
	private int who;
	private String userid;
	private int what;
	private ArrayList<Player> players;
	
	private String msg;
	private String date;
	
	private int card1;
	private int card2;
	private int card3;
	private int cardset;
	private int thisplaynum;
	private int turn;
	
	private int time;
	
	private int moneythisgame;
	private int minforbet;
	private int pandon;
	
	
	public Gaming() {}
	
	// 클라이언트에서 서버에 어떤 명령 요청시.
	public Gaming(int what) {
		setWhat(what);
	}
	
	
	// 서버용 다음턴 누군지 뿌릴때ddddddddd
	public Gaming(int what, int who) {
		setWhat(what); setWho(who);
	}
	
	// 서버용 타이머 뿌릴때, 돈 갱신ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
	public Gaming(int what, int who, int time) {
		setWhat(what); setWho(who); setTime(time);
	}
	
	// 서버에서 클라이언트 새로고침요구ㅇㅇㅇㅇㅇ
	public Gaming(int what, ArrayList<Player> players) {
		setWhat(what); setPlayers(players);
	}
	
	// 첫연결시 아이디 매칭때ㅇㅇㅇㅇㅇㅇ
	public Gaming(int what, String id) {
		setWhat(what); setMsg(id);
	}
	

	
	
	
	
	// 서버에서 클라이언트들에게 다음턴이 누군지 뿌려줄 때
	public static Gaming Gamestart(int thisplaynum) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_START); g.setThisplaynum(thisplaynum);
		return g;
	}
	// 서버에서 클라이언트들에게 다음턴이 누군지 뿌려줄 때
	public static Gaming Turn(int who) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_WHOSTURN); g.setWho(who);
		return g;
	}
	// 방장이 서버에게, 서버에서 클라이언트들에게 판돈이 얼마로 바뀌는지 뿌려줄 때
	public static Gaming Pandon(int pandon) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.PANDON); g.setPandon(pandon);
		return g;
	}
	// 클라이언트들이 처음에 판돈 서버로부터 얼마인지 받아올 때
	public static Gaming MuchPandon() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.MUCHPANDON);
		return g;
	}
	// 서버에서 게임시작시 카드 나눠줄 때
	public static Gaming GiveCard(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GETCARD); g.setPlayers(players);
		return g;
	}
	// 서버에서 2번째 카드 뽑으라고 할 때
	public static Gaming Draw2Phase() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.DRAW2);
		return g;
	}
	// 클라이언트에서 오픈하면서 콜할때
	public static Gaming Call(int trash) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CALL); g.setCard3(trash);
		return g;
	}
	// 클라이언트에서 오픈하면서 체크할때
	public static Gaming Check(int trash) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CHECK); g.setCard3(trash);
		return g;
	}
	// 클라이언트에서 오픈하면서 하프할때
	public static Gaming Half(int trash) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_HALF); g.setCard3(trash);
		return g;
	}
	// 클라이언트에서 마지막 정하면서 콜할때
	public static Gaming SetNCall(int set) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CALL); g.setCardset(set);
		return g;
	}
	// 클라이언트에서 마지막 정하면서 체크할때
	public static Gaming SetNCheck(int set) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_CHECK); g.setCardset(set);
		return g;
	}
	// 클라이언트에서 마지막 정하면서 하프할때
	public static Gaming SetNHalf(int set) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_HALF); g.setCardset(set);
		return g;
	}
	// 서버에서 게임시작시 카드 제자리위치하게 요구
		public static Gaming ResetCard() {
			Gaming g = new Gaming();
			g.setWhat(Gaming.RESETCARDS);
			return g;
		}
	// 서버에서 클라이언트들에게 게임 정보를 갱신시켜 줌.
	public static Gaming GameInfo(String userid, int what, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(what); g.setUserid(userid); g.setPlayers(players);
		return g;
	}
	// 클라이언트에서 일반 채팅 송출
	public static Gaming Chat(String userid, String msg, String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT); g.setUserid(userid); g.setMsg(msg); g.setDate(date);
		return g;
	}
	// 클라이언트에서 자기 닉네임 변경 요청
	public static Gaming NickChange(String userid, String toNick) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_NICKCHANGE); g.setUserid(userid); g.setMsg(toNick);
		return g;
	}
	// 서버에서 클라이언트들에게 변경한 닉네임과 시간을 뿌려줌
	public static Gaming NickChange(String userid, String toNick, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_NICKCHANGE); g.setUserid(userid); g.setMsg(toNick); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 서버에서 클라이언트들에게 누가 승리했는지 채팅알림으로도 뿌려줌
	public static Gaming Message_Win(String userid, String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_WIN); g.setUserid(userid); g.setDate(date);
		return g;
	}
	// 서버에서 클라이언트들에게 재경기한다고 채팅알림으로도 뿌려줌
	public static Gaming Message_Re(String date) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_RE); g.setDate(date);
		return g;
	}
	// 클라이언트에서 자신의 입장을 알려달라고 부탁함.
	public static Gaming ImIn(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_JOIN); g.setUserid(userid);
		return g;
	}
	// 클라이언트에서 자신의 퇴장을 알려달라고 부탁함.
	public static Gaming ImOut(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_LEAVE); g.setUserid(userid);
		return g;
	}
	// 서버에서 클라이언트들에게 누군가의 입장을 알리고 갱신하기 위해 뿌려줌.
	public static Gaming HesIn(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_JOIN); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 서버에서 클라이언트들에게 누군가의 퇴장을 알리고 갱신하기 위해 뿌려줌.
	public static Gaming HesOut(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.CHAT_LEAVE); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 클라이언트에서 서버로 방장이 누군가의 강퇴를 요청함
	public static Gaming callBan(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.BAN); g.setUserid(userid);
		return g;
	}
	// 서버에서 클라이언트들에게 누가 퇴장당했는지 알려줌
	public static Gaming HesBanned(String userid, String date, ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.BAN); g.setUserid(userid); g.setDate(date); g.setPlayers(players);
		return g;
	}
	// 클라이언트가 퇴장요구를 받게됨.
	public static Gaming UrBanned() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.IMBANNED);
		return g;
	}
	// 클라이언트에서 서버로 자신의 ID 알려줄 때
	public static Gaming SendID(String userid) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.IDMATCH); g.setUserid(userid);
		return g;
	}
	// 클라이언트에서 서버로 플레이어 전체 갱신을 요구할 때
	public static Gaming PlzRefresh() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.REFRESH);
		return g;
	}
	// 서버에서 클라이언트에 플레이어 갱신 정보를 뿌려줄 때
	public static Gaming Refresh(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.REFRESH); g.setPlayers(players);
		return g;
	}
	// 서버에서 클라이언트들에게 타이머 뿌릴 때
	public static Gaming Timer(int who, int time, int turn) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_TIMER); g.setWho(who); g.setTime(time); g.setTurn(turn);
		return g;
	}
	// 서버-클라이언트 양방향 판돈/베팅최소금액 갱신
	public static Gaming MoneyRefresh(int moneythisgame, int minforbet) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.MONEY_REFRESH); g.setMoneythisgame(moneythisgame); g.setMinforbet(minforbet);
		return g;
	}
	// 서버에서 클라이언트들에게 다음턴이 누군지 갱신시켜줌
	public static Gaming TurnRefresh(int turn) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.TURN_REFRESH); g.setWho(turn);
		return g;
	}
	// 서버에서 클라이언트들에게 게임 결과화면 띄우게 만듦
	public static Gaming Gameresult(ArrayList<Player> players) {
		Gaming g = new Gaming();
		g.setWhat(Gaming.GAME_RESULT); g.setPlayers(players);
		return g;
	}
	// 서버에서 클라이언트들에게 게임 끝나서 버튼활성화되게 만듦
	public static Gaming ButtonOk() {
		Gaming g = new Gaming();
		g.setWhat(Gaming.BUTTON_OK);
		return g;
	}
	
	
	

//	public Gaming(Socket socket, int what, String msg) {
//		setSocket(socket); setWhat(what); setMsg(msg);
//	}
	// 일반 채팅 송출ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
	public Gaming(String userid, int what, String msg, String date) {
		setUserid(userid); setWhat(what); setMsg(msg); setDate(date);
	}
	// 채팅 알림(입장, 퇴장) - 서버용 ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
	public Gaming(String userid, int what, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setDate(date); setPlayers(players);
	}
	// 채팅 알림(닉변경) - 서버용 dddddddddddddddddddddddddddddddddddddddd
	public Gaming(String userid, int what, String toNick, String date, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setMsg(toNick); setDate(date); setPlayers(players);
	}
	// 채팅 알림(닉변경) - 클라용 ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
	public Gaming(String userid, int what, String toNick) {
		setUserid(userid); setWhat(what); setMsg(toNick);
	}
	// 일반 게임 명령, 서버용ddddddddddddddddddddddddd
	public Gaming(String userid, int what, ArrayList<Player> players) {
		setUserid(userid); setWhat(what); setPlayers(players);
	}
	// 일반 게임 명령, 채팅 알림(입장, 퇴장) - 클라용ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
	public Gaming(String userid, int what) {
		setUserid(userid); setWhat(what);
	}
	
	
	// 카드 나눠받을 때ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
	public Gaming(int what, int card1, int card2, int card3) {
		setWhat(what); this.setCard1(card1); this.setCard2(card2); this.setCard3(card3);
	}
	
	public int getPandon() {
		return pandon;
	}
	public void setPandon(int pandon) {
		this.pandon = pandon;
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
	
	public int getCardset() {
		return cardset;
	}

	public void setCardset(int cardset) {
		this.cardset = cardset;
	}
	public int getThisplaynum() {
		return thisplaynum;
	}
	public void setThisplaynum(int thisplaynum) {
		this.thisplaynum=thisplaynum;
	}
	
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	public int getMoneythisgame() {
		return moneythisgame;
	}

	public void setMoneythisgame(int moneythisgame) {
		this.moneythisgame = moneythisgame;
	}

	public int getMinforbet() {
		return minforbet;
	}

	public void setMinforbet(int minforbet) {
		this.minforbet = minforbet;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
