package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class CheckMem extends Thread {
	Server serv;
	public CheckMem(Server serv) {
		this.serv=serv;
	}
	public void run() {
		while(true) {
			List<Player> players = serv.getPlayers();
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
					int num = gm.getWhat();
					List<Player> players = serv.getPlayers();
					for(Player p : players) {
						if(p.getSocket().equals(socket)) {
							switch(num) {
								case Gaming.GAME_UNREADY: p.setReady(0); System.out.println("준비안함"); break;
								case Gaming.GAME_READY: p.setReady(1); System.out.println("준비함"); break;
								case Gaming.GAME_DIE: p.setBetbool(1); break;
								case Gaming.GAME_GO: p.setBetbool(2); break;
								default: break;
							}
							break;
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
	
	private List<Player> players = new ArrayList<>();
	private List<Integer> cards = new ArrayList<>();
	/**
	 * 이번 시작 멤버 번호(0-3)
	 */
	private int start;
	
	public Server() {
		Integer[] temp = new Integer[] {11,12,21,22,31,32,41,42,51,52,61,62,71,72,81,82,91,92,101,102};
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
					uth.start();
					Player p = new Player(players.size(), receive, uth);
					players.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Player> getPlayers() {
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
//		List<Player> templist = new ArrayList<>();
//		for(int i=0;i<4;i++) {
//			templist.add(players.get((start+i)%4));
//		}
//		players = templist;
		for(Player p : players) {
			p.setReady(2);
			p.setBetbool(0);
			p.setCard1(0);
			p.setCard2(0);
			p.setCard3(0);
			p.setCardset(0);
		}
		Collections.shuffle(cards);
		int index=0;
		for(int i=0;i<4;i++) {
			Player now = players.get((start+i)%4);
			now.setCard1(cards.get(index++));
		}
		for(int i=0;i<4;i++) {
			Player now = players.get((start+i)%4);
			now.setCard2(cards.get(index++));
		}
		
		
	}
}
