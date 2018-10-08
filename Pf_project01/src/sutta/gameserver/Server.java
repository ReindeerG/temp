package sutta.gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import sutta.gamelogic.Logic;

class Timer extends Thread {
	Server serv=null;
	Player p=null;
	int turn;
	private boolean kill=false;
	public boolean isKill() {
		return kill;
	}
	public void setKill(boolean kill) {
		this.kill = kill;
	}
	ArrayList<Player> players=null;
	public Timer(Server serv, Player p, int turn) {
		this.serv=serv; this.p=p; this.turn=turn;
	}
	public void run() {
		for(int i=0;i<100;i++) {
			if(p.getBetbool()!=0 || isKill()==true) {
				p.getUth().IncreaseTurn();
				this.interrupt();
				return;
			}
			else {
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
//				players = serv.getPlayers();
//				System.out.println("턴:"+serv.getWhosturn()+"/"+i);
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(serv.getWhosturn(), i, turn));
//						out.writeObject(new Gaming(Gaming.GAME_TIMER, serv.getWhosturn(), i));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
//				System.out.println("타이머 계속 보내고 있단다");
			}
			i--;
		}
		Player p = serv.getPlayers().get(serv.getWhosturn());
		p.getUth().IncreaseTurn();
		p.getUth().toDie();
//		for(Player p2 : players) {
//			try {
//				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
//				out.writeObject(new Gaming(p.getUserid(), Gaming.GAME_DIE, players));
//				out.flush();
//			} catch(Exception e) {e.printStackTrace();}
//		}
		this.interrupt();
		return;
	}
	
	
}

class SelfOutTh extends Thread {
	private Player p;
	private String nick;
	private Server serv;
	public SelfOutTh(Player p, String nick, Server serv) {
		this.p=p; this.nick=nick; this.serv=serv;
	}
	public void run() {
		serv.SelfOut(p, nick);
		return;
	}
}

class HesClosedTh extends Thread {
	private Server serv;
	public HesClosedTh(Server serv) {
		this.serv=serv;
	}
	public void run() {
		serv.HesClosed();
		return;
	}
}

class BanTh extends Thread {
	private Player p;
	private Server serv;
	public BanTh(Player p, Server serv) {
		this.p=p; this.serv=serv;
	}
	public void run() {
		serv.Ban(p);
		return;
	}
}

/*
class CheckMem extends Thread {
	Server serv;
	private boolean stop=false;
	public boolean isStop() {
		return stop;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public CheckMem(Server serv) {
		this.serv=serv;
	}
	public void run() {
		while(true) {
			if(stop==true) { break; }
			else {
//				System.out.println("돌긴하니");
				boolean del = false;
				
				ArrayList<Player> tmpplayer = (ArrayList<Player>)serv.getPlayers().clone();
				for(Player pl : tmpplayer) {
					System.out.println(pl.getOrder()+"번: "+pl.getUth().isAlive());
					if(pl.getUth().isAlive()==false || pl.getUth().isStop()==true) {
						tmpplayer.remove(pl);
						
						del = true;
//						break;
					}
				}
				if(del==true) {
					
					
//					ArrayList<Player> tmpplayer1 = (ArrayList<Player>)serv.getPlayers().clone();
//					for(Player p : tmpplayer1) {
//						if(p.getUth().isAlive()==false) {
//							tmpplayer1.remove(p);
//						}
//					}
					int index=0;
					for(Player p : tmpplayer) {
						p.setOrder(index++);
					}
					serv.setPlayers(tmpplayer);
					serv.setCm(new CheckMem(serv));
					serv.getCm().setDaemon(true);
					serv.getCm().start();
					return;
					
					
					
					
					
					
					
//					setStop(true); serv.remakecheck(); interrupt(); return;
				}
				
			}
		}
		return;
	}
}
*/

class UserThread extends Thread {
	private Socket socket;
	public Socket getSocket() {
		return socket;
	}
	private boolean stop;
	private Server serv;
	private Format f = new SimpleDateFormat("a hh:mm");
	private ObjectInputStream in;
	public ObjectInputStream getIn() {
		return in;
	}
	public UserThread(Socket socket, Server serv) {
		stop=false; this.socket=socket; this.serv=serv; this.in=null;
	}
	public UserThread(Socket socket, Server serv, ObjectInputStream in) {
		stop=false; this.socket=socket; this.serv=serv; this.in=in;
	}
	public boolean isStop() {
		return stop;
	}
	public void toStop() {
		stop=true;
		return;
	}	
	
	public void IncreaseTurn() {
		serv.increaseTurn();
		ArrayList<Player> players = serv.getPlayers();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.TurnRefresh(serv.getTurn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void MatchId() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(new Gaming(Gaming.IDMATCH));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	public void toDie() {
		ArrayList<Player> players = serv.getPlayers();
		Player p = players.get(serv.getWhosturn());
		p.setBetbool(1);
		serv.nextTurn();
	}
	
	public void run() {
		if(in==null) MatchId();
		try {
			if(in==null) in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true) {
				if(stop==true) { in.close(); this.interrupt(); break; }
				else {
					for(Player tmpp : serv.getPlayers()) {
						if(tmpp.getUth().isStop()==false && tmpp.getUth().getThreadGroup()==null) {
							ObjectInputStream tmpin = tmpp.getUth().getIn();
							tmpp.getUth().toStop();
							UserThread uth = new UserThread(tmpp.getSocket(), serv, tmpin);
							uth.setDaemon(true);
							tmpp.setUth(uth);
							uth.start();
						}
					}
					try {
							Gaming gm = (Gaming)in.readObject();
							Player q=null;
							int index=0;
							for(Player p : serv.getPlayers()) {
								if(p.getSocket().equals(socket)) {
									ArrayList<Player> players = serv.getPlayers();
									q = p;
									switch(gm.getWhat()) {
										case Gaming.IDMATCH: {
											q.setUserid(gm.getUserid());
											break;
										}
										case Gaming.REFRESH: {
											serv.Refresh();
											break;
										}
										case Gaming.MONEY_REFRESH: {
											serv.MoneyRefresh();
											break;
										}
										case Gaming.GAME_WHOSTURN: {
											serv.WhosTurn();
											break;
										}
										case Gaming.GAME_UNREADY: {
											p.setReady(0);
											p.setMoney(p.getMoney()+serv.getPandon());
											serv.setMoneythisgame(serv.getMoneythisgame()-serv.getPandon());
											serv.MoneyRefresh();
											serv.Refresh();
											break;
										}
										case Gaming.GAME_READY: {
											p.setReady(1);
											p.setMoney(p.getMoney()-serv.getPandon());
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getPandon());
											serv.MoneyRefresh();
											serv.Refresh();
											break;
										}
										case Gaming.GAME_START: {
		//									serv.GameStartBool();
											for(Player p2 : players) {
												p2.setReady(0);
											}
											serv.WhosTurn();
											serv.GameStart();
											break;
										}
										case Gaming.GAME_DIE: {
		//									p.setBetbool(1);
		//									nextTurn();
		//									if(serv.isInggame()==true) {
		//										players.get(serv.getWhosturn()).setBetbool(0);
		//										Refresh();
		//										
		//										Timer t = new Timer(serv, players.get(serv.getWhosturn()));
		//										t.setDaemon(true);
		//										t.start();
		//									}
											toDie();
											break;
										}
										case Gaming.GAME_CALL: {
											if(gm.getCard3()!=0) {
												p.setTrash(gm.getCard3());
											} else if (gm.getCardset()!=0) {
												p.SelectSet(gm.getCardset());
											}
											p.setBetbool(2);
											p.setMoney(p.getMoney()-serv.getMinforbet());
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMinforbet());
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.GAME_HALF: {
											if(gm.getCard3()!=0) {
												p.setTrash(gm.getCard3());
											} else if (gm.getCardset()!=0) {
												p.SelectSet(gm.getCardset());
											}
											p.setBetbool(3);
											p.setMoney(p.getMoney()-serv.getMoneythisgame()/2);
											serv.setMinforbet(serv.getMoneythisgame()/2);
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMoneythisgame()/2);
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.GAME_CHECK: {
											if(gm.getCard3()!=0) {
												p.setTrash(gm.getCard3());
											} else if (gm.getCardset()!=0) {
												p.SelectSet(gm.getCardset());
											}
											p.setBetbool(4);
											serv.nextTurn();
											break;
										}
										case Gaming.MUCHPANDON: {
											serv.Pandon(serv.getPandon());
											break;
										}
										case Gaming.PANDON: {
											serv.Pandon(gm.getPandon());
											break;
										}
										case Gaming.CHAT: {
//												System.out.println(serv.getCm().isAlive());
											String msg = gm.getMsg();
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.Chat(q.getNickname(), msg, f.format(d)));
		//										out.writeObject(new Gaming(q.getNickname(), gm.getWhat(), msg, f.format(d)));
												out.flush();
											}
											break;
										} 
										case Gaming.CHAT_JOIN: {
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.HesIn(q.getNickname(), f.format(d), players));
		//										out.writeObject(new Gaming(q.getNickname(), gm.getWhat(), f.format(d), players));
												out.flush();
											}
											serv.Pandon(serv.getPandon());
											break;
										}
										case Gaming.CHAT_LEAVE: {
											/*
												String strtemp = q.getNickname();
												UserThread tmpUth = q.getUth();
			//									q.getUth().getIn().close();
			//									q.getUth().getSocket().close();
												tmpUth.toStop();
												while(!tmpUth.isStop()) {
//												while(tmpUth.isAlive()) {
//													System.out.println("아직 살아있음");
												}
												p.getSocket().close();
												while(!p.getSocket().isClosed()) {
												}
//												serv.getCm().setStop(true);
												serv.getPlayers().remove(q);
												index=0;
												for(Player pl : players) {
													pl.setOrder(index++);
												}
//												serv.setCm(new CheckMem(serv));
//												serv.getCm().setDaemon(true);
//												serv.getCm().start();
												Date d = new Date();
												for(Player p2 : players) {
													if(p2.getUth().isStop()==false && p2.getUth().getThreadGroup()==null) {
														ObjectInputStream tmpin = p2.getUth().getIn();
														p2.getUth().toStop();
														UserThread uth = new UserThread(p2.getSocket(), serv, tmpin);
														uth.setDaemon(true);
														p2.setUth(uth);
														uth.start();
													}
													ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
													out.writeObject(Gaming.HesOut(strtemp, f.format(d), players));
			//										out.writeObject(new Gaming(strtemp, gm.getWhat(), f.format(d), players));
													out.flush();
												}
												*/
											
											String strtemp = q.getNickname();
											toStop();
											while(!isStop()) {
											}
											SelfOutTh tmpt = new SelfOutTh(q, strtemp, serv);
											tmpt.start();
											break;
										}
										case Gaming.BAN: {
											String target=gm.getUserid();
											String strtemp=null;
											for(Player pl : players) {
												if(pl.getUserid().equals(target)) {
													strtemp = pl.getNickname();
													if(pl.getReady()==1) {
														pl.setMoney(q.getMoney()+serv.getPandon());
														serv.setMoneythisgame(serv.getMoneythisgame()-serv.getPandon());
														pl.setReady(0);
														serv.MoneyRefresh();
														serv.Refresh();
													}
													try {
														ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(pl.getSocket().getOutputStream()));
														out.writeObject(Gaming.UrBanned());
														out.flush();
													} catch(Exception e) {e.printStackTrace();}
													pl.getUth().toStop();
													while(!pl.getUth().isStop()) {
													}
													pl.getUth().getIn().close();
													pl.getUth().interrupt();
													BanTh tmpt = new BanTh(pl, serv);
													tmpt.setDaemon(true);
													tmpt.start();
													break;
												}
											}
											break;
										}
										case Gaming.CHAT_NICKCHANGE: {
											String temp = q.getNickname();
											p.setNickname(gm.getMsg());
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.NickChange(temp, gm.getMsg(), f.format(d), players));
		//										out.writeObject(new Gaming(temp, gm.getWhat(), gm.getMsg(), f.format(d), players));
												out.flush();
											}
											break;
										}
										case Gaming.GAME_RESULT_OK: {
											serv.setResultbacksig(serv.getResultbacksig()-1);
											break;
										}
										default: break;
									}
								}
								else { index++; }
							}
						} catch(Exception e) {
							e.printStackTrace();
							if(isStop()==false) {
								System.out.println("튕김");
								toStop();
								in.close();
								HesClosedTh tmpt = new HesClosedTh(serv);
								tmpt.start();
								interrupt();
							} else {
								
							}
//								try { in.close(); } catch (Exception e1) {}
//								this.interrupt();
//								serv.HesClosed();
							return;
						}
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		return;
		
		
		
		
	}
	
//	private Player player;
//	
//	public UserThread(Player player) {
//		this.player=player;
//	}
}

public class Server extends Thread {
	private int port;
	private ServerSocket server;
	
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Integer> cards = new ArrayList<>();
	/**
	 * 이번 시작 멤버 번호(0-3)
	 */
	private int whosturn=0;
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
	private int pandon;
	public int getPandon() {
		return pandon;
	}
	public void setPandon(int pandon) {
		this.pandon = pandon;
	}
	private boolean phase2;
	public boolean isPhase2() {
		return phase2;
	}
	public void setPhase2(boolean phase2) {
		this.phase2 = phase2;
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
	private int thisplaynum;
	public int getThisplaynum() {
		return thisplaynum;
	}
	public void setThisplaynum(int thisplaynum) {
		this.thisplaynum=thisplaynum;
	}
	private Format f = new SimpleDateFormat("a hh:mm");
	
	private Timer nowtimer;
	public Timer getNowtimer() {
		return nowtimer;
	}
	public void setNowtimer(Timer nowtimer) {
		this.nowtimer = nowtimer;
	}
//	private CheckMem cm;
//	public CheckMem getCm() {
//		return cm;
//	}
//	public void setCm(CheckMem cm) {
//		this.cm = cm;
//	}
	public Server(int port) {
		this.port= port;
		Integer[] temp = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		cards = new ArrayList<>(Arrays.asList(temp));
		setPandon(10);
	}
	
	public void serverClose() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void Add() {
//		players.add(new Player(players.size(), null));
//	}
	
	
	public void run() {
		try {
//			cm = new CheckMem(this);
//			cm.setDaemon(true);
//			cm.start();
			server = new ServerSocket(port);
			while(true) {				
				Socket receive = server.accept();
				boolean exit = false;
				for(Player p : players) {
					if(p.getSocket().equals(receive)) {
						exit = true;
						break;
					};
				}
				if(exit==false) {
					UserThread uth = new UserThread(receive, this);
					uth.setDaemon(true);
					Player p = new Player(players.size(), receive, uth);
//					cm.setStop(true);
					players.add(p);
					uth.start();
//					cm = new CheckMem(this);
//					cm.setDaemon(true);
//					cm.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players=players;
	}
	
	public void GameStartBool() {
		if(players.size()==4) {
			boolean allok = true;
			for(Player p : players) {
				if(p.getReady()!=1) {
					allok = false;
					System.out.println("아직 모든 플레이어가 준비되지 않았습니다!");
				}
			}
			if(allok==true) {
				GameStart();
			}
		}
		else {
			System.out.println("플레이어 수가 부족합니다! (현재 "+players.size()+"명)");
		}
	}
	public void Refresh() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Refresh(players));
//				out.writeObject(new Gaming(Gaming.REFRESH, players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void Ban(Player q) {
		while(q.getUth().isAlive()) {
			q.getUth().interrupt();
		}
		try{
			if(!q.getSocket().isClosed()) q.getSocket().close();
		}catch(Exception e) {e.printStackTrace();}
		while(!q.getSocket().isClosed()) {
		}
		String strtemp = q.getNickname();
//		cm.setStop(true);
		players.remove(q);
		int index=0;
		for(Player p : players) {
			p.setOrder(index++);
		}
//		cm = new CheckMem(this);
//		cm.setDaemon(true);
//		cm.start();
		for(Player tmpp : getPlayers()) {
			if(tmpp.getUth().isStop()==false && tmpp.getUth().getThreadGroup()==null) {
				ObjectInputStream tmpin = tmpp.getUth().getIn();
				tmpp.getUth().toStop();
				UserThread uth = new UserThread(tmpp.getSocket(), this, tmpin);
				uth.setDaemon(true);
				tmpp.setUth(uth);
				uth.start();
			}
//			System.out.println(tmpp.getOrder()+"번-"+tmpp.getUserid()+"/"+tmpp.getUth()+"//"+!tmpp.getSocket().isClosed());
			MoneyRefresh();
			Date d = new Date();
			try {
				for(Player p : players) {
					ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
					out.writeObject(Gaming.HesBanned(strtemp, f.format(d), players));
					out.flush();
				}
			} catch(Exception e) {e.printStackTrace();}
			break;
		}
		Refresh();
		return;
	}
	public void SelfOut(Player q, String strtemp) {
		while(q.getUth().isAlive()) {
		}
		try{q.getSocket().close();}catch(Exception e) {e.printStackTrace();}
		while(!q.getSocket().isClosed()) {
		}
//		serv.getCm().setStop(true);
		getPlayers().remove(q);
		int index=0;
		for(Player pl : players) {
			pl.setOrder(index++);
		}
//		serv.setCm(new CheckMem(serv));
//		serv.getCm().setDaemon(true);
//		serv.getCm().start();
		Date d = new Date();
		for(Player p2 : players) {
			if(p2.getUth().isStop()==false && p2.getUth().getThreadGroup()==null) {
				ObjectInputStream tmpin = p2.getUth().getIn();
				p2.getUth().toStop();
				UserThread uth = new UserThread(p2.getSocket(), this, tmpin);
				uth.setDaemon(true);
				p2.setUth(uth);
				uth.start();
			}
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
				out.writeObject(Gaming.HesOut(strtemp, f.format(d), players));
				out.flush();
			} catch(Exception e) {e.printStackTrace();}
		}
	}
	public void HesClosed() {
//		cm.setStop(true);
//		cm.interrupt();
//		
//		while(cm.isAlive()) {
//			System.out.println(cm+"/"+cm.getThreadGroup());
//		}
		ArrayList<Player> tmp = new ArrayList<>();
		ArrayList<String> tmpstr = new ArrayList<>();
		Date d = new Date();
		for(Player p : players) {
			if(p.getUth().isStop()==true) {
				tmpstr.add(p.getNickname());
				while(p.getUth().getThreadGroup()!=null) {
					try { p.getUth().getIn().close(); } catch(Exception e) { e.printStackTrace(); System.out.println("in닫으려다"); }
					p.getUth().interrupt();
				}
				if(!p.getSocket().isClosed()) {
					try { p.getSocket().close(); } catch(Exception e) { e.printStackTrace(); }
				}
			} else {
				tmp.add(p);
			}
		}
		int index=0;
		for(Player p : tmp) {
			p.setOrder(index++);
		}
		setPlayers(tmp);
		Refresh();
		for(Player p : getPlayers()) {
			try {
				for(String s : tmpstr) {
					ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
					out.writeObject(Gaming.HesOut(s, f.format(d), players));
					out.flush();
				}
			} catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	public void WhosTurn() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Turn(getWhosturn()));
//				out.writeObject(new Gaming(Gaming.GAME_WHOSTURN, serv.getWhosturn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	public void nextTurn() {
		getNowtimer().setKill(true);
		int alive=0;
		int sumcall=0;
		int ischecked=0;
		int sumnone=0;
		for(Player p : players) {
			if(p.getBetbool()!=1) {
				alive++;
			}
			if(p.getBetbool()==2) {
				sumcall++;
			}
			if(p.getBetbool()==4) {
				ischecked++;
			}
			if(p.getBetbool()==0) {
				sumnone++;
			}
		}
		
		
		if(alive==1) {
			// 살아있는사람이이김
			for(Player p : players) {
				if (p.getBetbool()!=1) {
					// 얘가 우승자임
					ArrayList<String> tmplist = new ArrayList<>();
					for(Player q : players) {
						q.setGameresult(0);
					}
					p.setGameresult(1); calc(p);
					tmplist.add(p.getUserid());
					toresult();
					Message_Win(p.getNickname());
					try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
					// 버튼활성화
					resetcards();
					justrebatch(tmplist);
					toButtonOk();
					for(Player q : players) {
						q.setBetbool(0);
					}
					Refresh();
					setInggame(false);
					break;
				}
			}
		}
		else {
			if(sumnone>0) {
				for(int i=1;i<players.size();i++) {
					Player tempp = players.get((getWhosturn()+i)%players.size());
					if(tempp.getBetbool()!=1) {
						setWhosturn(tempp.getOrder());
						break;
					}
				}
				WhosTurn();
				if(isInggame()==true) {
					players.get(getWhosturn()).setBetbool(0);
					Refresh();
					
					setTurn(getTurn()+1);
					Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
					setNowtimer(t);
					t.setDaemon(true);
					t.start();
				}
			} else {
				if(isPhase2()==false) {
					Draw2Phase();
				} else {
					if (alive==sumcall+ischecked) {
						// 결판
						ArrayList<Player> tmplist = new ArrayList<>();
						for(Player p : players) {
							if(p.getBetbool()!=1) tmplist.add(p);
						}
						whosWin(tmplist);
					} else {
						for(int i=1;i<players.size();i++) {
							Player tempp = players.get((getWhosturn()+i)%players.size());
							if(tempp.getBetbool()!=1) {
								setWhosturn(tempp.getOrder());
								break;
							}
						}
						WhosTurn();
						if(isInggame()==true) {
							players.get(getWhosturn()).setBetbool(0);
							Refresh();
							
							setTurn(getTurn()+1);
							Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
							setNowtimer(t);
							t.setDaemon(true);
							t.start();
						}
					}
				}
			}
		}
			

		return;
	}
	public void whosWin(ArrayList<Player> players) {
		int num = players.size();
		String userid1=players.get(0).getUserid();
		String userid2=players.get(1).getUserid();
		int cardresult1=players.get(0).getSelCardset();
		int cardresult2=players.get(1).getSelCardset();
		String userid3=null;
		int cardresult3=0;
		String userid4=null;
		int cardresult4=0;
		if(players.size()>2) {
			userid3=players.get(2).getUserid();
			cardresult3=players.get(2).getSelCardset();
		}
		if(players.size()>3) {
			userid4=players.get(3).getUserid();
			cardresult4=players.get(3).getSelCardset();
		}
		ArrayList<String> result = Logic.GameResult(num, userid1, cardresult1, userid2, cardresult2, userid3, cardresult3, userid4, cardresult4);
		for(Player p : players) {
			p.setGameresult(0);
		}
		if(result.size()==1) {
			ArrayList<String> tmplist = new ArrayList<>();
			for(Player p : players) {
				if(p.getUserid().equals(result.get(0))) { p.setGameresult(1); calc(p); tmplist.add(p.getUserid()); Message_Win(p.getNickname()); break; }
			}
			// result.get(0) 가 우승
			toresult();
			try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
			// 버튼활성화
			resetcards();
			justrebatch(tmplist);
			
			
			
			toButtonOk();
			for(Player q : players) {
				q.setBetbool(0);
			}
			Refresh();
			setInggame(false);
		} else {
			for(String s : result) {
				for(Player p : players) {
					if(p.getUserid().equals(s)) { p.setGameresult(2); break; }
				}
			}
			// 남은사람끼리 재대결
			toresult();
			Message_Re();
			try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			resetcards();
			rebatch(result);
			for(Player q : players) {
				q.setBetbool(0);
			}
			Refresh();
			rematch(result.size());
		}
	}
	private int resultbacksig;
	public int getResultbacksig() {
		return resultbacksig;
	}
	public void setResultbacksig(int resultbacksig) {
		this.resultbacksig = resultbacksig;
	}
	public void toresult() {
//		resultbacksig = players.size();
//		while(true) {
			for(Player p : players) {
				try {
					ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
					out.writeObject(Gaming.Gameresult(players));
					out.flush();
				}catch(Exception e) {e.printStackTrace();}
			}
//			if(resultbacksig==0) break;
//		}
		
		
		
//		for(Player p : players) {
//			try {
//				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
//				out.writeObject(Gaming.Gameresult(players));
//				out.flush();
//				System.out.println(p.getOrder()+1+"놈에게 보냄"+p.getSocket());
//			}catch(Exception e) {e.printStackTrace();}
//		}
		return;
	}
	public void toButtonOk() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ButtonOk());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void MoneyRefresh() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.MoneyRefresh(getMoneythisgame(), getMinforbet()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	public void resetcards() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ResetCard());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void timeToCardset() {
		for(Player p : players) {
			if(p.getBetbool()!=1) {
//				switch(p.getTrash()) {
//					case 1: {
//						p.setCardset(Logic.Result(p.getCard2(), p.getCard3()));
//						break;
//					}
//					case 2: {
//						p.setCardset(Logic.Result(p.getCard1(), p.getCard3()));
//						break;
//					}
//					default: break;
//				}
				p.setCardset(Logic.ResultSet(p.getCard1(), p.getCard2(), p.getCard3()));
			}
		}
		return;
	}
	public void justrebatch(ArrayList<String> replayers) {
		ArrayList<Player> init=getPlayers();
		ArrayList<Player> temp1=new ArrayList<>();
		ArrayList<Player> temp2=new ArrayList<>();
		for(Player p : init) {
			if (replayers.contains(p.getUserid())) {
				temp1.add(p);
			} else {
				temp2.add(p);
			}
		}
		ArrayList<Player> newpl = new ArrayList<>();
		int index=0;
		for(Player p : temp1) {
			p.setOrder(index++);
			newpl.add(p);
		}
		for(Player p : temp2) {
			p.setOrder(index++);
			newpl.add(p);
		}
		setPlayers(newpl);
		return;
	}
	public void rebatch(ArrayList<String> replayers) {
		ArrayList<Player> init=getPlayers();
		ArrayList<Player> temp1=new ArrayList<>();
		ArrayList<Player> temp2=new ArrayList<>();
		for(Player p : init) {
			if (replayers.contains(p.getUserid())) {
				temp1.add(p);
				p.setGameresult(2);
			} else {
				temp2.add(p);
				p.setGameresult(0);
			}
		}
		ArrayList<Player> newpl = new ArrayList<>();
		int index=0;
		for(Player p : temp1) {
			p.setOrder(index++);
			newpl.add(p);
		}
		for(Player p : temp2) {
			p.setOrder(index++);
			newpl.add(p);
		}
		setPlayers(newpl);
		return;
	}
	public void calc(Player p) {
		p.setMoney(p.getMoney()+moneythisgame);
		setMoneythisgame(0);
		for(Player q : players) {
			q.setGameresult(0);
		}
		p.setGameresult(1);
		Refresh();
	}
	public void Message_Win(String nick) {
		Date d = new Date();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Message_Win(nick, f.format(d)));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void Pandon(int pandon) {
		setPandon(pandon);
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Pandon(pandon));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void Message_Re() {
		Date d = new Date();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Message_Re(f.format(d)));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	public void Draw2Phase() {
		getNowtimer().setKill(true);
		setPhase2(true);
		for(Player p : players) {
			if(p.getTrash()!=0) {
				p.setBetbool(0);
//				switch(p.getTrash()) {
//					case 1: {
//						p.setCardset(Logic.Result(p.getCard2(), p.getCard3()));
//						break;
//					}
//					case 2: {
//						p.setCardset(Logic.Result(p.getCard1(), p.getCard3()));
//						break;
//					}
//					default: break;
//				}
				p.setCardset(Logic.ResultSet(p.getCard1(), p.getCard2(), p.getCard3()));
			}
		}
		Refresh();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Draw2Phase());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		for(int i=1;i<players.size();i++) {
			Player tempp = players.get((getWhosturn()+i)%players.size());
			if(tempp.getBetbool()!=1) {
				setWhosturn(tempp.getOrder());
				break;
			}
		}
		WhosTurn();
		setTurn(1);
		if(isInggame()==true) {
			players.get(getWhosturn()).setBetbool(0);
			Refresh();
			
			Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
			setNowtimer(t);
			t.setDaemon(true);
			t.start();
		}
		
		return;
	}
	public void rematch(int num) {
		setPhase2(false);
		for(Player p : players) {
			p.setTrash(0);
			p.setBetbool(1);
			p.setGameresult(0);
			p.setCardset(new int[4]);
		}
		for(int i=0;i<num;i++) {
			players.get(i).setBetbool(0);
		}
		setInggame(true);
		setPhase2(false);
		setTurn(1);
		setWhosturn(0);
		WhosTurn();
		setMinforbet(0);
		MoneyRefresh();
		Collections.shuffle(cards);
		int index=0;
		for(Player p : players) {
			p.setBetbool(0);
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard3(cards.get(index++));
		}
		for(int i=num;i<players.size();i++) {
			players.get(i).setBetbool(1);
		}
		for(Player p : players) {
			System.out.println(p.getUserid());
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gamestart(num));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		System.out.println("스타트보냄");
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.GiveCard(players));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		System.out.println("재경기시작하긴하니");
		while(true) {
			if(1==2) break;
		}
		Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
	}
	public void GameStart() {
		setPhase2(false);
		for(Player p : players) {
			p.setTrash(0);
			p.setBetbool(0);
			p.setGameresult(0);
			p.setCardset(new int[4]);
		}
		setInggame(true);
		setTurn(1);
		setWhosturn(0);
		WhosTurn();
		setMinforbet(0);
		MoneyRefresh();
//		for(Player p : players) {
//			p.setReady(2);
//			p.setBetbool(0);
//			p.setCard1(0);
//			p.setCard2(0);
//			p.setCard3(0);
//			p.setCardset(0);
//		}
//		Collections.shuffle(cards);
//		int index=0;
//		for(int i=0;i<4;i++) {
//			Player now = players.get((start+i)%4);
//			now.setCard1(cards.get(index++));
//		}
//		for(int i=0;i<4;i++) {
//			Player now = players.get((start+i)%4);
//			now.setCard2(cards.get(index++));
//		}
		Collections.shuffle(cards);
		int index=0;
		for(Player p : players) {
			p.setBetbool(0);
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard3(cards.get(index++));
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gamestart(players.size()));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.GiveCard(players));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		
		Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		
	}
	
	
	
	
	
}
