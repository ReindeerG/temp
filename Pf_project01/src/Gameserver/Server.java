package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.util.List;

import Chatserver.Message;

class Timer extends Thread {
	Server serv=null;
	Player p=null;
	ArrayList<Player> players=null;
	public Timer(Server serv, Player p) {
		this.serv=serv; this.p=p;
	}
	public void run() {
		for(int i=0;i<300;i++) {
			if(p.getBetbool()==1) {
				return;
			}
			else {
				try { Thread.sleep(100); } catch (InterruptedException e) {}
				players = serv.getPlayers();
				for(Player p2 : players) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						String str = (300-i)+" 남음";
						out.writeObject(new Gaming(p.getUserid(), Gaming.CHAT, str, ""));
						out.flush();
					} catch(Exception e) {}
				}
			}
		}
		players = serv.getPlayers();
		for(Player p2 : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
				out.writeObject(new Gaming(p.getUserid(), Gaming.GAME_DIE, players));
				out.flush();
			} catch(Exception e) {}
		}
		return;
	}
	
}

class CheckMem extends Thread {
	Server serv;
	public CheckMem(Server serv) {
		this.serv=serv;
	}
	public void run() {
		while(true) {
			ArrayList<Player> players = serv.getPlayers();
			int index=0;
			boolean del=false;
			for(Player pl : players) {
				if(pl.getSocket().equals(null)) {
					pl.getUth().toStop();
					players.remove(index);
					del=true;
				}
				index++;
			}
			if(del==true) {
				index=0;
				for(Player pl : players) {
					pl.setOrder(index);
				}
			}
		}
	}
}

class UserThread extends Thread {
	private Socket socket;
	private boolean stop;
	private Server serv;
	private Format f = new SimpleDateFormat("a hh:mm");
	public UserThread(Socket socket, Server serv) {
		stop=false; this.socket=socket; this.serv=serv;
	}
	public void toStop() {
		stop=true;
	}	
	
	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true) {
				if(stop==true) { return; }
				else {
					Gaming gm = (Gaming)in.readObject();
					ArrayList<Player> players = serv.getPlayers();
					Player q=null;
					for(Player p : players) {
						if(p.getSocket().equals(socket)) {
							q = p;
							switch(gm.getWhat()) {
								case Gaming.GAME_UNREADY: {
									p.setReady(0);
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(q.idToNick(gm.getUserid()), gm.getWhat()));
										out.flush();
									}
									break;
								}
								case Gaming.GAME_READY: {
									p.setReady(1);
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(q.idToNick(gm.getUserid()), gm.getWhat()));
										out.flush();
									}
									break;
								}
								case Gaming.GAME_START: {
//									serv.GameStartBool();
									serv.GameStart();
									break;
								}
								case Gaming.GAME_DIE: {
									p.setBetbool(1);
									boolean allchoice = true;
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(q.idToNick(gm.getUserid()), gm.getWhat(), players));
										out.flush();
									}
									if(allchoice==true) {
										//겜종료 실행
										System.out.println("다쥬금ㅋㅋㅋ");
									}
									break;
								}
								case Gaming.GAME_GO: {
									p.setBetbool(2);
									boolean allchoice = true;
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(q.idToNick(gm.getUserid()), gm.getWhat(), players));
										out.flush();
										if(p2.getBetbool()==0) allchoice=false;
									}
									if(allchoice==true) {
										//겜종료 실행
									}
									
									break;
								}
								case Gaming.CHAT: {
									String msg = gm.getMsg();
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(q.idToNick(gm.getUserid()), gm.getWhat(), msg, f.format(d)));
										out.flush();
									}
									break;
								} 
								case Gaming.CHAT_JOIN:
								case Gaming.CHAT_LEAVE: {
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(q.idToNick(gm.getUserid()), gm.getWhat(), f.format(d), players));
										out.flush();
									}
									break;
								}
								case Gaming.CHAT_NICKCHANGE: {
									String temp = q.idToNick(gm.getUserid());
									p.setNickname(gm.getMsg());
									Date d = new Date();
									for(Player p2 : players) {
										ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
										out.writeObject(new Gaming(temp, gm.getWhat(), gm.getMsg(), f.format(d), players));
										out.flush();
									}
								}
								default: break;
							}
						}
					}
					
				}
			}
		}
		catch(Exception e) {}
		
		
//		while(true) {
//			try {
//				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//					if(stop==true) { return; }
//					else {
//						Gaming gm = (Gaming)in.readObject();
//						int num = gm.getWhat();
//						List<Player> players = serv.getPlayers();
//						for(Player p : players) {
//							if(p.getSocket().equals(socket)) {
//								switch(num) {
//									case Gaming.GAME_UNREADY: p.setReady(0); System.out.println("준비안함"); break;
//									case Gaming.GAME_READY: p.setReady(1); System.out.println("준비함"); break;
//									case Gaming.GAME_DIE: p.setBetbool(1); break;
//									case Gaming.GAME_GO: p.setBetbool(2); break;
//									default: break;
//								}
//								break;
//							}
//						}
//						
//						
//						
//						
//						
//					}
//			}
//			catch(Exception e) {}
//		}
		
		
		
		
	}
	
//	private Player player;
//	
//	public UserThread(Player player) {
//		this.player=player;
//	}
}

public class Server {
	private int port = 53891;
	private ServerSocket server;
	
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Integer> cards = new ArrayList<>();
	/**
	 * 이번 시작 멤버 번호(0-3)
	 */
	private int start;
	
	public Server() {
		Integer[] temp = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		cards = new ArrayList<>(Arrays.asList(temp));
		this.run();
	}
	
//	public void Add() {
//		players.add(new Player(players.size(), null));
//	}
	
	
	public void run() {
		try {
			CheckMem cm = new CheckMem(this);
			cm.setDaemon(true);
			cm.start();
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
					players.add(p);
					uth.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
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
	
	public void GameStart() {
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
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(new Gaming(Gaming.GETCARD, p.getCard1(), p.getCard2(), p.getCard3()));
				out.flush();
			}
			catch (Exception e) {}
		}
		Timer t = new Timer(this, players.get(0));
		t.setDaemon(true);
		t.start();
		
	}
}
