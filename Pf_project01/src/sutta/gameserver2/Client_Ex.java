package sutta.gameserver2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import sutta.gamewindow2.Mainwindow;
import sutta.useall.User;

public class Client_Ex extends Thread {
	private User user;
	public User getUser() {
		return user;
	}
	
//	private String userid;
	private Player me;
	private ArrayList<Player> players;
	private int port;
	private Socket socket = null;
	private Mainwindow window=null;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public ObjectInputStream getIn() {
		return in;
	}
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	private boolean stop=false;
	public void toStop() {
		this.stop=true;
	}
	public boolean isStop() {
		return this.stop;
	}
	
	private int pandon=10;
	public int getPandon() {
		return pandon;
	}
	public void setPandon(int pandon) {
		this.pandon = pandon;
	}
//	public String getUserid() {
//		return userid;
//	}
//	public void setUserid(String userid) {
//		this.userid = userid;
//	}
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
	private int cardset;
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
	private int thisplaynum;
	public int getThisplaynum() {
		return thisplaynum;
	}
	public void setThisplaynum(int thisplaynum) {
		this.thisplaynum=thisplaynum;
	}
	
	private boolean canthalf=false;
	public boolean isCanthalf() {
		return canthalf;
	}
	public void setCanthalf(boolean canthalf) {
		this.canthalf = canthalf;
	}
	private boolean boolTrash;
	public boolean isBoolTrash() {
		return boolTrash;
	}
	public void setBoolTrash(boolean boolTrash) {
		this.boolTrash = boolTrash;
	}
	private int trash;
	public int getTrash() {
		return trash;
	}
	public void setTrash(int trash) {
		this.trash = trash;
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
	private boolean yetresult;
	public boolean isYetresult() {
		return yetresult;
	}
	public void setYetresult(boolean yetresult) {
		this.yetresult = yetresult;
	}
	private boolean phase2;
	public boolean isPhase2() {
		return phase2;
	}
	public void setPhase2(boolean phase2) {
		this.phase2 = phase2;
	}
	private boolean onceneglecttimer;
	public boolean isOnceneglecttimer() {
		return onceneglecttimer;
	}
	public void setOnceneglecttimer(boolean onceneglecttimer) {
		this.onceneglecttimer = onceneglecttimer;
	}

	public void SelectSet(int n, String str) {
		setCardset(n);
		System.out.println(getCardset()+"선택했다야");
		getWindow().nameset(str);
		try {
			out.writeObject(Gaming.SelectSet(n));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
		return;
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
		return;
	}
	public void StartMyJoin() {
		callRefresh();
		System.out.println("callRefresh 실행 완료");
		while(true) {
			if(getMe()!=null) break;
		}
		System.out.println("getMe() != null");
		try {
			out.writeObject(Gaming.ImIn(getMe().getUser().getId()));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT_JOIN));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void ChangeNick(String nick) {
		try {
			out.writeObject(Gaming.NickChange(getMe().getUser().getId(), nick));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT_NICKCHANGE, nick));
			out.flush();
		} catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void MakeChat(String msg) {
		try {
			out.writeObject(Gaming.Chat(getMe().getUser().getId(), msg, null));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT, msg, ""));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Leave() {
		try {
			out.writeObject(Gaming.ImOut(getMe().getUser().getId()));
//			out.writeObject(new Gaming(getMe().getUserid(), Gaming.CHAT_LEAVE));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void GameReady() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_READY));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void GameUnready() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_UNREADY));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void GameStart() {
		try {
			out.writeObject(new Gaming(Gaming.GAME_START));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Open(int trash) {
		try {
			out.writeObject(Gaming.Open(trash));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Bet_Call() {
		setCanthalf(true);
		getWindow().NotMyTurn();
		try {
			out.writeObject(Gaming.Call());
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Bet_Die() {
		getWindow().NotMyTurn();
		if(isBoolTrash()==false) {
			getWindow().surrender();
		}
		try {
			out.writeObject(new Gaming(Gaming.GAME_DIE));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Bet_Half() {
		getWindow().NotMyTurn();
		try {
			out.writeObject(Gaming.Half());
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Bet_Check() {
		setCanthalf(true);
		getWindow().NotMyTurn();
		try {
			out.writeObject(Gaming.Check());
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Money_Refresh() {
		try {
			out.writeObject(new Gaming(Gaming.MONEY_REFRESH));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Ban(String userid) {
		try {
			out.writeObject(Gaming.callBan(userid));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void MuchPandon() {
		try {
			out.writeObject(Gaming.MuchPandon());
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public void Pandon(int pandon) {
		try {
			out.writeObject(Gaming.Pandon(pandon));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
		return;
	}
	public Client_Ex(int port, User user) {
		try {
			this.user = user;
			this.port = port;
			InetAddress inet = InetAddress.getByName("localhost");
			socket = new Socket(inet, port);
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch(Exception e) {e.printStackTrace();}
		return;
	}
	
	public void run() {
//		try { in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream())); }
//		catch(Exception e) { e.printStackTrace(); }
		while(true) {
			if(stop==true) { this.interrupt(); break; }
			else {
				try {
					in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					Gaming gm = (Gaming)in.readObject();
					switch(gm.getWhat()) {
					case Gaming.IDMATCH: {
						System.out.println("처음에 가진 user"+user);
						out.writeObject(Gaming.UserMatch(user));
	//					out.writeObject(new Gaming(Gaming.IDMATCH, getUserid()));
						out.flush();
						break;
					}
					case Gaming.REFRESH: {
						System.out.println("0");
						players=gm.getPlayers();
						System.out.println("players = "+players);
						System.out.println("getUser() = "+getUser());
						System.out.println("받은 players"+players);
						for(Player p : players) {
							if(p.getUser().getId().equals(user.getId())) {
								me=p;
								break;
							}
						}
						System.out.println("서버가 가진 유저:"+me.getUser());
						while(true) {
							if(getWindow()!=null) break;
//							else System.out.println(getWindow());
							else Thread.sleep(10);
						}
//						System.out.println(getWindow());
						System.out.println("여기까진돼1");
						getWindow().Refresh();
						System.out.println("여기까진돼2");
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
					case Gaming.PANDON: {
						setPandon(gm.getPandon());
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
							if(p.getUser().getId().equals(user.getId())) {
								me=p;
								break;
							}
						}
						getWindow().Refresh();
						getWindow().ChatJoin(gm.getUserid(), gm.getDate());
						MuchPandon();
						break;
					} 
					case Gaming.CHAT_LEAVE: {
						players=gm.getPlayers();
						getWindow().ChatLeave(gm.getUserid(), gm.getDate());
						getWindow().Refresh();
	//					callRefresh();
						break;
					}
					case Gaming.BAN: {
						players=gm.getPlayers();
						getWindow().ChatBan(gm.getUserid(), gm.getDate());
	//					getWindow().Refresh();
	//					Thread.sleep(500);
	//					callRefresh();
						break;
					}
					case Gaming.IMBANNED: {
						getWindow().ImBanned();
						break;
					}
					case Gaming.CHAT_NICKCHANGE: {
						getWindow().NickChange(gm.getUserid(), gm.getMsg(), gm.getDate());
						players=gm.getPlayers();
						getWindow().Refresh();
						break;
					}
					case Gaming.CHAT_WIN: {
						getWindow().Winmsg(gm.getUserid(), gm.getDate());
						players=gm.getPlayers();
						getWindow().Resultgame();
						whosturn=0;
						break;
					}
					case Gaming.CHAT_RE: {
						getWindow().Remsg(gm.getDate());
						break;
					}
					case Gaming.GAME_WHOSTURN: {
						setWhosturn(gm.getWho());
	//					getWindow().Clockicons();
						break;
					}
					case Gaming.GETCARD: {
						setPhase2(false);
						getWindow().setReseted(false);
						getWindow().ResetCards();
						while(getWindow().isReseted()==false) {
						}
						setYetresult(false);
						setBoolTrash(false);
						setTrash(0);
						setCardset(0);
						players = gm.getPlayers();
						for(Player p : players) {
							if(p.getUser().getId().equals(user.getId())) {
								me=p;
								break;
							}
						}
						
						card1 = getMe().getCard1(); card2 = getMe().getCard2(); card3 = getMe().getCard3();
	//					card1 = gm.getCard1(); card2 = gm.getCard2(); card3 = gm.getCard3();
	//					System.out.println(card1);
						getWindow().DrawCards();
	//					System.out.println(card1+" "+card2+" "+card3);
	//					getWindow().DrawCards();
	//					getWindow().MycardOpen(card1, card2);
						callRefresh();
						break;
					}
					case Gaming.DRAW2: {
						setOnceneglecttimer(true);
						setPhase2(true);
						setCanthalf(false);
						setTurn(1);
						players = gm.getPlayers();
						for(Player p : players) {
							if(p.getUser().getId().equals(user.getId())) {
								me=p;
								break;
							}
						}
						getWindow().DrawCards2();
						break;
					}
					case Gaming.RESETCARDS: {
						getWindow().ResetCards();
						break;
					}
					case Gaming.GAME_TIMER: {
						if(isOnceneglecttimer()==false) {
							setTurn(gm.getTurn());
							getWindow().Timer(gm.getWho(), gm.getTime());
						} else {
							setOnceneglecttimer(false);
						}
						break;
					}
					case Gaming.GAME_START: {
						setOnceneglecttimer(false);
						thisplaynum=gm.getThisplaynum();
						setMoneythisgame(gm.getMoneythisgame());
						setMinforbet(gm.getMinforbet());
						inggame=true;
						setPhase2(false);
						canthalf=false;
						turn=1;
						getWindow().StartToButton();
						break;
					}
	//				case Gaming.GAME_REMATCH: {
	//					setOnceneglecttimer(false);
	//					thisplaynum=gm.getThisplaynum();
	//					setMoneythisgame(gm.getMoneythisgame());
	//					setMinforbet(0);
	//					inggame=true;
	//					setPhase2(false);
	//					canthalf=false;
	//					turn=1;
	//					getWindow().StartToButton();
	//					break;
	//				}
					case Gaming.GAME_RESULT: {
	//					if(isYetresult()==false) {
	//						setYetresult(true);
							players=gm.getPlayers();
	//						boolean isRe = false;
	//						for(Player p : players) {
	//							if(p.getGameresult()==2) { isRe=true; break; }
	//						}
	//						if(isRe==false) setMinforbet(0);
							
							
							
							getWindow().Resultgame();
							whosturn=0;
	//						out.writeObject(new Gaming(Gaming.GAME_RESULT_OK));
	//						out.flush();
	//					}
						break;
					}
					case Gaming.BUTTON_OK: {
						inggame=false;
						getWindow().fornextgame();
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
				} catch(Exception e) { /*e.printStackTrace();*/ }
			}
		}
		return;
	}
	
	
	
	

}
