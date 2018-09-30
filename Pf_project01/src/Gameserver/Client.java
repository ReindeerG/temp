package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import Chatserver.Message;
import Gamewindow.Mainwindow;

public class Client extends Thread {
	private String userid;
	private Player me;
	private ArrayList<Player> players;
	private int port = 53891;
	private Socket socket = null;
	private Mainwindow window=null;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	private int whosturn;
	public int getWhosturn() {
		return whosturn;
	}
	public void setWhosturn(int whosturn) {
		this.whosturn = whosturn;
	}
	private boolean inggame=false;
	public boolean isInggame() {
		return inggame;
	}
	public void setInggame(boolean inggame) {
		this.inggame = inggame;
	}
	private int moneythisgame=0;
	private int minforbet=0;
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
	private int turn;
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public void increaseTurn() {
		this.turn++;
	}
	private int card1;
	private int card2;
	private int card3;
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
	
	
	
	public void setWindow(Mainwindow window) {
		this.window = window;
	}
	public Mainwindow getWindow() {
		return window;
	}
	public Player getMe() {
		return me;
	}
	public void setMe(Player me) {
		this.me = me;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public Socket getSocket() {
		return socket;
	}
	public void callRefresh() {
		try {
			out.writeObject(Gaming.PlzRefresh());
//			out.writeObject(new Gaming(Gaming.REFRESH));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void callWhosturn() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_WHOSTURN));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
	}
	public void StartMyJoin() {
		callRefresh();
		while(true) {
			if(getMe()!=null) break;
		}
		try {
			out.writeObject(Gaming.ImIn(getMe().getUserid()));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT_JOIN));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
	}
	public void ChangeNick(String nick) {
		try {
			out.writeObject(Gaming.NickChange(getMe().getUserid(), nick));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT_NICKCHANGE, nick));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
	}
	public void MakeChat(String msg) {
		try {
			out.writeObject(Gaming.Chat(getMe().getUserid(), msg, null));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT, msg, ""));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void Leave() {
		try {
			out.writeObject(Gaming.ImOut(getMe().getUserid()));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT_LEAVE));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void GameReady() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_READY));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void GameUnready() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_UNREADY));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void GameStart() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_START));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void Bet_Call() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_CALL));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void Bet_Die() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_DIE));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void Bet_Half() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_HALF));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void Bet_Check() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_CHECK));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void Money_Refresh() {
		try {
			out.writeObject(new Gaming(Gaming.MONEY_REFRESH));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public Client() {
		try {
			InetAddress inet = InetAddress.getByName("localhost");
			socket = new Socket(inet, port);
		} catch(Exception e) {e.printStackTrace();}
	}
	public Client(String id) {
		setUserid(id);
		try {
			InetAddress inet = InetAddress.getByName("localhost");
			socket = new Socket(inet, port);
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public void run() {
//		try { in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream())); }
//		catch(Exception e) { e.printStackTrace(); }
		while(true) {
			try {
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				Gaming gm = (Gaming)in.readObject();
				switch(gm.getWhat()) {
				case Gaming.IDMATCH: {
					out.writeObject(Gaming.SendID(getUserid()));
//					out.writeObject(new Gaming(Gaming.IDMATCH, getUserid()));
					out.flush();
					break;
				}
				case Gaming.REFRESH: {
					players=gm.getPlayers();
					for(Player p : players) {
						if(p.getUserid().equals(getUserid())) {
							me=p;
							break;
						}
					}
					while(true) {
						if(getWindow()!=null) break;
					}
					getWindow().Refresh();
					break;
				}
				case Gaming.MONEY_REFRESH: {
					setMoneythisgame(gm.getMoneythisgame());
					setMinforbet(gm.getMinforbet());
					getWindow().MoneyRefresh();
//					getWindow().Refresh();
					break;
				}
				case Gaming.TURN_REFRESH: {
					setTurn(gm.getWho());
//					getWindow().Refresh();
					break;
				}
				case Gaming.CHAT: {
					getWindow().ReceiveMsg(gm.getUserid(), gm.getMsg(), gm.getDate());
					break;
				}
				case Gaming.CHAT_JOIN: {
					players=gm.getPlayers();
					for(Player p : players) {
//---------------------플레이어 많아지면 해제해서 테스트
						if(p.getUserid().equals(userid)) {
							me=p;
							break;
						}
					}
					getWindow().Refresh();
					getWindow().ChatJoin(gm.getUserid(), gm.getDate());
					break;
				} 
				case Gaming.CHAT_LEAVE: {
					players=gm.getPlayers();
					getWindow().ChatLeave(gm.getUserid(), gm.getDate());
					callRefresh();
					break;
				}
				case Gaming.CHAT_NICKCHANGE: {
					getWindow().NickChange(gm.getUserid(), gm.getMsg(), gm.getDate());
					players=gm.getPlayers();
					getWindow().Refresh();
					break;
				}
				case Gaming.GAME_WHOSTURN: {
					setWhosturn(gm.getWho());
//					getWindow().Clockicons();
					break;
				}
				case Gaming.GETCARD: {
					card1 = gm.getCard1(); card2 = gm.getCard2(); card3 = gm.getCard3();
//					System.out.println(card1);
					getWindow().DrawCards();
//					System.out.println(card1+" "+card2+" "+card3);
//					getWindow().DrawCards();
//					getWindow().MycardOpen(card1, card2);
					callRefresh();
					break;
				}
				case Gaming.GAME_TIMER: {
					getWindow().Timer(gm.getWho(), gm.getTime());
//					getWindow().Clockicons();
					break;
				}
				case Gaming.GAME_START: {
					inggame=true;
					turn=1;
					getWindow().StartToButton();
					break;
				}
				case Gaming.GAME_CALL:
				case Gaming.GAME_DIE:
				{
					players=gm.getPlayers();
					break;
				}
				
				
				default: break;
			}
				
			} catch(Exception e) {e.printStackTrace();}
		}
	}
	

}
