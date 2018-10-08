package sutta.gameserver;

import java.io.Serializable;
import java.net.Socket;

public class Player implements Serializable {
	
	@Override
	public String toString() {
		return "Player [userid=" + userid + ", nickname=" + nickname + ", money=" + money + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * order: 게임방 별 플레이어 순서(0-3)
	 * socket: 플레이어별 고유 소켓
	 * betbool: 0_초기화 / 1_다이 / 2_콜 / 3_하프 / 4_체크
	 * ready: 0_안함 / 1_준비 / 2_이미게임중
	 * gameresult: 0_안띄움 / 1_승리 / 2_재경기
	 */
	private int order;
	private transient Socket socket;
	private String userid;
	private String nickname;
	private int money = 10000;
	private int card1;
	private int card2;
	private int card3;
	private int trash;
	private int[] cardset;
	private int betbool;
	private int ready;
	private int gameresult;
	private transient UserThread uth;
	
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Socket getSocket() {
		return socket;
	}
//	public void setSocket(Socket socket) {
//		this.socket = socket;
//	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public int[] getCardset() {
		return cardset;
	}
	public void setCardset(int[] cardset) {
		this.cardset = cardset;
	}
	public int getSelCardset() {
		return cardset[3];
	}
	public void SelectSet(int num) {
		int[] tmp = getCardset();
		switch(num) {
			case 1: {
				tmp[3]=tmp[0];
				break;
			}
			case 2: {
				tmp[3]=tmp[1];
				break;
			}
			case 3: {
				tmp[3]=tmp[2];
				break;
			}
		}
		setCardset(tmp);
		return;
	}
	public int getBetbool() {
		return betbool;
	}
	public void setBetbool(int betbool) {
		this.betbool = betbool;
	}
	public Player(int order, Socket socket, UserThread uth) {
		this.order=order; this.socket=socket; this.setUth(uth); setReady(0);
	}
//	public Socket getSocket() {
//		return socket;
//	}
//	public String getNickname() {
//		return nickname;
//	}
	public int getReady() {
		return ready;
	}
	public void setReady(int ready) {
		this.ready = ready;
	}
	public UserThread getUth() {
		return uth;
	}
	public void setUth(UserThread uth) {
		this.uth = uth;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getGameresult() {
		return gameresult;
	}
	public void setGameresult(int gameresult) {
		this.gameresult = gameresult;
	}
	public int getTrash() {
		return trash;
	}
	public void setTrash(int trash) {
		this.trash = trash;
	}
	
	
	
	
}
