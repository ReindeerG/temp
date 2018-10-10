package sutta.gameserver2;

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
import java.util.List;

import sutta.gamelogic.Logic;
import sutta.mainserver6.MainServer;
import sutta.useall.User;

/**
 * 어떤 플레이어의 턴에 돌아가게 될 타이머쓰레드
 */
class Timer extends Thread {
	/**
	 * serv: 메인 서버 쓰레드
	 * p: 어느 플레이어의 타이머인지
	 * kill: 외부에서 타이머를 확실히 죽이기 위해. kill=true하면 클라이언트들에게 타이머 신호를 안보내고 스스로 정지하기위해 노력함.
	 */
	Server serv=null;
	Player p=null;
	int turn=0;
	private boolean kill=false;
	public boolean isKill() { return kill; }
	public void setKill(boolean kill) { this.kill = kill; }
	public Timer() {};		// 오픈 타이머, 최종패 타이머가 상속받을거라서
	public Timer(Server serv, Player p, int turn) { this.serv=serv; this.p=p; this.turn=turn; }
	public void run() {
		for(int i=0;i<100;i++) {
			if(p.getBetbool()!=0 || isKill()==true) {
				serv.IncreaseTurn();				// 타이머 종료되면 턴 증가하면서 끝나게 됨.
				this.interrupt();
				return;
			}
			else {
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(serv.getWhosturn(), i, turn));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		Player p = serv.getPlayers().get(serv.getWhosturn());
		serv.IncreaseTurn();
		p.getUth().toDie();
		this.setKill(true);
		this.interrupt();
		return;
	}
}
/**
 * 처음 패 오픈시 돌아가게 될 타이머쓰레드
 */
class OpenTimer extends Timer {
	/**
	 * serv: 메인 서버 쓰레드
	 * kill: 외부에서 타이머를 확실히 죽이기 위해. kill=true하면 클라이언트들에게 타이머 신호를 안보내고 스스로 정지하기위해 노력함.
	 */
	Server serv=null;
	int turn=1;
	private boolean kill=false;
	public boolean isKill() { return kill; }
	public void setKill(boolean kill) { this.kill = kill; }
	public OpenTimer(Server serv) { this.serv=serv; }
	public void run() {
		for(int i=0;i<100;i++) {
			if(isKill()==true) {
				this.interrupt();
				return;
			}
			else {
				boolean allsel = true;
				for(Player p : serv.getPlayers()) {
					if(p.getTrash()==0 && p.getBetbool()!=1) {
						allsel = false;
						break;
					}
				}
				if (allsel==true) {
					serv.setWhosturn(serv.getWhosturn()-1);
					serv.setTurn(serv.getTurn()-1);
					serv.nextTurn();
					this.setKill(true);
					this.interrupt();
					return;
				}
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(4, i, turn));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		for(Player p : serv.getPlayers()) {
			if(p.getTrash()==0 && p.getBetbool()!=1) {
				p.setBetbool(1);
				p.setTrash(1);
			}
		}
		serv.setWhosturn(serv.getWhosturn()-1);
		serv.setTurn(serv.getTurn()-1);
		serv.nextTurn();
		this.setKill(true);
		this.interrupt();
		return;
	}
}
/**
 * 생존자끼리 패결정시 돌아가게 될 타이머쓰레드
 */
class SelSetTimer extends Timer {
	/**
	 * serv: 메인 서버 쓰레드
	 * kill: 외부에서 타이머를 확실히 죽이기 위해. kill=true하면 클라이언트들에게 타이머 신호를 안보내고 스스로 정지하기위해 노력함.
	 */
	Server serv=null;
	ArrayList<Player> players=null;
	int turn=1;
	private boolean kill=false;
	public boolean isKill() { return kill; }
	public void setKill(boolean kill) { this.kill = kill; }
	public SelSetTimer(Server serv, ArrayList<Player> players) { this.serv=serv; this.players=players; }
	public void run() {
		for(int i=0;i<100;i++) {
			if(isKill()==true) {
				this.interrupt();
				return;
			}
			else {
				boolean allsel = true;
				for(Player p : players) {
					if(p.getSelCardset()==0 && p.getBetbool()!=1) {
						allsel = false;
						break;
					}
				}
				if (allsel==true) {
					serv.whosWin(players);
					this.setKill(true);
					this.interrupt();
					return;
				}
				try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
				for(Player p2 : serv.getPlayers()) {
					try {
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
						out.writeObject(Gaming.Timer(5, i, turn));
						out.flush();
					} catch(Exception e) {e.printStackTrace();}
				}
			}
		}
		for(Player p : players) {
			if(p.getSelCardset()==0 && p.getBetbool()!=1) {
				p.SelectSet(1);
			}
		}
		serv.whosWin(players);
		this.setKill(true);
		this.interrupt();
		return;
	}
}

/**
 * 유저가 스스로 나갔을 때 해당 유저쓰레드에서 서버쓰레드로 요청시 실행될 쓰레드.
 * 유저쓰레드에 해당 유저의 나간다는 신호가 들어왔어도, 유저쓰레드를 종료시키고 절차를 밟을 수 없으니(해당절차의 return이 될 때까지 쓰레드가 대기상태가 되서)
 * 절차 호출을 쓰레드로 호출하고 유저쓰레드가 먼저 종료될 수 있게 하기 위함.
 */
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
/**
 * 유저가 튕겼을 때 해당 유저쓰레드에서 서버쓰레드로 요청시 실행될 쓰레드.
 * 유저쓰레드에 해당 유저의 튕겼다는 예외가 들어왔어도, 유저쓰레드를 종료시키고 절차를 밟을 수 없으니(해당절차의 return이 될 때까지 쓰레드가 대기상태가 되서)
 * 절차 호출을 쓰레드로 호출하고 유저쓰레드가 먼저 종료될 수 있게 하기 위함.
 */
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
/**
 * 강퇴를 서버쓰레드로 요청시 실행될 쓰레드.
 * 강퇴신호를 받아도 해당 유저쓰레드는 여전히 in 대기 중이고, 유저쓰레드를 종료시키고 절차를 밟을 수 없으니(해당절차의 return이 될 때까지 쓰레드가 대기상태가 되서)
 * 절차 호출을 쓰레드로 호출하고 유저쓰레드가 먼저 종료될 수 있게 하기 위함.
 */
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
class UserThread extends Thread {
	/**
	 * 서버에 연결된 유저 1명당 UserThread가 생성됨.
	 * socket: 서버에 연결된 유저의 Socket이 담김(생성자 인자)
	 * serv: 유저의 명령신호를 받아 서버에 공동처리를 요청하기 위해 서버정보를 담음(생성자 인자)
	 * 
	 * stop: 본 쓰레드를 종료하기 위한 신호. toStop()을 통해  true로 바꾸면 run의 while문이 더 이상 명령을 받지않고 스스로 종료하기 위해서만 노력함.
	 * f: 채팅, 입실 등 시간정보와 알림말이 표시될 때 시간정보의 포맷
	 * in: 연결된 유저 1명과 계속 통신할 InputStream. 이 쓰레드가 start(run)할 때 처음 생성(new)하여 계속 사용.
	 * 
	 * 해당 유저에게서 나가는 신호를 받으면, 서버쓰레드에 요청하여 서버쓰레드에서는 players 리스트에 해당유저를 제거하고 order를 0부터 빈틈없이 수정한 후, 클라이언트들에게 Refresh 시켜 줌.
	 * 
	 * 방장이 자신과 연결된 쓰레드에 누군가의 강제퇴장을 요청하면, 서버쓰레드로 정보를 주고 서버쓰레드에서는 위와 마찬가지의 행동을 함.
	 * 이 때 가끔 방장 자신의 쓰레드도 종료되기도 하는데, 방장의 Socket과 InputStream은 살아있기 때문에 바로 InputStream을 그대로 이어받은 쓰레드를 채워넣는데, 이것이 2번째 생성자임.(InputStream이 포함된 생성자)
	 */
	private Socket socket;
	private boolean stop;
	private Server serv;
	private Format f = new SimpleDateFormat("a hh:mm");
	private ObjectInputStream in;
	
	public UserThread(Socket socket, Server serv) { stop=false; this.socket=socket; this.serv=serv; this.in=null; }

	public Socket getSocket() { return socket; }
	public boolean isStop() { return stop; }
	public void toStop() { stop=true; return; }
	public void reserStop() { stop=false; return; }
	public ObjectInputStream getIn() { return in; }
	/**
	 * MatchId()
	 * 현재 독립된 서버로 만들어져 쓰레드가 만들어질 때, userid정보를 클라이언트에서 받아오게됨.. 메인서버와 연결하게되면 사라질 녀석
	 */
	public void MatchId() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(new Gaming(Gaming.IDMATCH));
			out.flush();
		}catch(Exception e) {e.printStackTrace();}
	}
	/**
	 * 유저가 타이머 시간을 모두 소진했을 때, 타이머는 해당 유저의 쓰레드에 다이베팅 명령을 보내는데, 이 메서드가 이용됨.
	 */
	public void toDie() {
		Player p = serv.getPlayers().get(serv.getWhosturn());
		p.setBetbool(1);
		serv.nextTurn();
		return;
	}
	public void run() {
		// in이 없다는건 유저와 처음 연결된다는 것. userid를 요구하고 InputStream을 new함.
		// in이 있다는건 유저가 실제로 접속해있지만 유저쓰레드가 종료되어 다시 복구시킨 것이라 new할 필요 없음. 오히려 new하면 예외발생.
		if(in==null) MatchId();
		try {
			if(in==null) in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true) {
				if(stop==true) { in.close(); this.interrupt(); break; }
				else {
					serv.checkUth();
					try {
							Gaming gm = (Gaming)in.readObject();
							Player q=null;
							for(Player p : serv.getPlayers()) {
								if(p.getSocket().equals(socket)) {
									ArrayList<Player> players = serv.getPlayers();
									q = p;
									switch(gm.getWhat()) {
										case Gaming.IDMATCH: {
											System.out.println("게임서버에서 유저데이터받음");
											q.setUser(gm.getUser());
											System.out.println("서버가 받은 유저:"+gm.getUser());
											serv.Refresh();
											System.out.println("리프레시끝남");
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
											p.getUser().setMoney(p.getUser().getMoney()+serv.getPandon());
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()+serv.getPandon());
											serv.setMoneythisgame(serv.getMoneythisgame()-serv.getPandon());
											serv.MoneyRefresh();
											serv.Refresh();
											break;
										}
										case Gaming.GAME_READY: {
											p.setReady(1);
											p.getUser().setMoney(p.getUser().getMoney()-serv.getPandon());
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()-serv.getPandon());
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getPandon());
											serv.MoneyRefresh();
											serv.Refresh();
											break;
										}
										case Gaming.GAME_START: {
											serv.GameStartBool();
											break;
										}
										case Gaming.GAME_DIE: {
											toDie();
											break;
										}
										case Gaming.GAME_OPEN: {
											p.setTrash(gm.getCard3());
											break;
										}
										case Gaming.GAME_SELECTSET: {
											p.SelectSet(gm.getCardset());
											break;
										}
										case Gaming.GAME_CALL: {
											p.setBetbool(2);
											p.getUser().setMoney(p.getUser().getMoney()-serv.getMinforbet());
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()-serv.getMinforbet());
											p.setThisbet(serv.getMinforbet());
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMinforbet());
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.GAME_HALF: {
											p.setBetbool(3);
											p.getUser().setMoney(p.getUser().getMoney()-serv.getMoneythisgame()/2);
											serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()-serv.getMoneythisgame()/2);
											p.setThisbet(serv.getMoneythisgame()/2);
											serv.setMinforbet(serv.getMoneythisgame()/2);
											serv.setMoneythisgame(serv.getMoneythisgame()+serv.getMoneythisgame()/2);
											serv.MoneyRefresh();
											serv.nextTurn();
											break;
										}
										case Gaming.GAME_CHECK: {
											p.setBetbool(4);
											serv.setMinforbet(0);
											serv.MoneyRefresh();
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
											String msg = gm.getMsg();
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.Chat(q.getUser().getNickname(), msg, f.format(d)));
												out.flush();
											}
											break;
										} 
										case Gaming.CHAT_JOIN: {
											Date d = new Date();
											for(Player p2 : players) {
												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
												out.writeObject(Gaming.HesIn(q.getUser().getNickname(), f.format(d), players));
												out.flush();
											}
											serv.Pandon(serv.getPandon());
											break;
										}
										case Gaming.CHAT_LEAVE: {
											String strtemp = q.getUser().getNickname();
											toStop();
											while(!isStop()) {
											}
											SelfOutTh tmpt = new SelfOutTh(q, strtemp, serv);
											tmpt.start();
											break;
										}
										case Gaming.BAN: {
											String target=gm.getUserid();
											for(Player pl : players) {
												if(pl.getUser().getId().equals(target)) {
													if(pl.getReady()==1) {
														pl.getUser().setMoney(q.getUser().getMoney()+serv.getPandon());
														serv.getMain().setMoney(q.getUser().getId(), q.getUser().getMoney()+serv.getPandon());
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
//										case Gaming.CHAT_NICKCHANGE: {
//											String temp = q.getUser().getNickname();
//											p.getUser().setNickname(gm.getMsg());
//											Date d = new Date();
//											for(Player p2 : players) {
//												ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
//												out.writeObject(Gaming.NickChange(temp, gm.getMsg(), f.format(d), players));
//												out.flush();
//											}
//											break;
//										}
										case Gaming.GAME_RESULT_OK: {
											serv.setResultbacksig(serv.getResultbacksig()-1);
											break;
										}
										default: break;
									}
								}
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
							return;
						}
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		return;
	}
}

public class Server extends Thread {
	/**
	 * port: 이 방의 포트 번호를 담고 있음. 메인서버에서 방을 개설할 때 받아질 것임. (생성자 인자)
	 * server: 이 방이 서버로서 열어질 Socket임.
	 * players: 이 방에 참여한 유저들을 담고 있음.
	 * cards: 1월부터 10월까지, 각 월별 2장씩하여 1~20의 Integer가 담겨있는 ArrayList. 이것을 Shuffle하여 나눠줌.
	 * inggame: 현재 게임이 진행 중인지 아닌지 알 수 있는 boolean. 방의 참여 가능여부에 사용될 듯. 클라이언트에서는 다른 inggame이 자체적으로 게임중/종료를 판별하여 버튼의 비활성화나 강퇴불가를 결정함.
	 * whosturn: 지금 누구의 턴인지 담길 정보(0~3)
	 * turn:
	 * moneythisgame: 현재 총 베팅금액.
	 * minforbet: 전 사람이 베팅한, 콜하기 위한 금액(베팅 최소금액)
	 * phase2: 현재 이 경기에서 한바퀴 돌고 3장째 새 카드를 받았는지의 여부. 첫바퀴에서는 false / 3장째를 받으면 true.
	 * thisplaynum: 현재 경기시작시 참여한 플레이어 수.
	 * f: 채팅창에 알림 등에 사용될 시간의 포맷양식.
	 * nowtimer: 지금 돌아가고 있는 타이머가 담기는 곳.
	 * pandon: 게임에 참여하기 위해 유저별로 내야하는 초기 기본 베팅금.
	 */
	private MainServer main;
	public MainServer getMain() {
		return main;
	}
	public void setMain(MainServer main) {
		this.main = main;
	}
	private int port;
	private ServerSocket server;
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Integer> cards = new ArrayList<>();
	
	private boolean inggame=false;
	private int whosturn=0;
	private int turn;
	private boolean phase2;
	private int thisplaynum;
	
	private int pandon;
	private int moneythisgame=0;
	private int minforbet=0;

	private Format f = new SimpleDateFormat("a hh:mm");
	private Timer nowtimer;

	public ArrayList<Player> getPlayers() { return players; }
	public void setPlayers(ArrayList<Player> players) { this.players=players; }
	
	public boolean isInggame() { return inggame; }
	public void setInggame(boolean inggame) { this.inggame = inggame; }
	public int getWhosturn() { return whosturn; }
	public void setWhosturn(int whosturn) { this.whosturn = whosturn; }
	public int getTurn() { return turn; }
	public void setTurn(int turn) { this.turn = turn; }
	public void increaseTurn() { this.turn++; }
	public boolean isPhase2() { return phase2; }
	public void setPhase2(boolean phase2) { this.phase2 = phase2; }
	public int getThisplaynum() { return thisplaynum; }
	public void setThisplaynum(int thisplaynum) { this.thisplaynum=thisplaynum; }

	public int getPandon() { return pandon; }
	public void setPandon(int pandon) { this.pandon = pandon; }
	public int getMoneythisgame() { return moneythisgame; }
	public void setMoneythisgame(int moneythisgame) { this.moneythisgame = moneythisgame; }
	public int getMinforbet() { return minforbet; }
	public void setMinforbet(int minforbet) { this.minforbet = minforbet; }
	
	
	public Timer getNowtimer() { return nowtimer; }
	public void setNowtimer(Timer nowtimer) { this.nowtimer = nowtimer; }
	
	private List<User> userList;
	private User user;
	
	public void serverClose() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(User user) {
		this.user = user;
		userList.add(user);
	}
	public void removeUser(User user) {
		userList.remove(user);
	}
	public List<User> getUserList(){
		return userList;
	}
	
	public Server(int port, List<User> userList) {
		this.port = port;
		this.userList = userList;
		Integer[] temp = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		cards = new ArrayList<>(Arrays.asList(temp));
		setPandon(10);
	}
	public void run() {
		System.out.println("게임방시작됨");
		try {
			server = new ServerSocket(port);
			while(true) {				
				Socket receive = server.accept();
				System.out.println("누군가 접속함");
				boolean exit = false;
				for(Player p : players) {
					if(p.getSocket().equals(receive)) {
						exit = true;
						break;
					};
				}
				if(exit==false) {	// accept한 유저가 이미 접속한 유저가 아닐 경우에만 유저쓰레드를 생성하고 플레이어에 추가.
					UserThread uth = new UserThread(receive, this);
					uth.setDaemon(true);
					Player p = new Player(players.size(), receive, uth, user);
					players.add(p);
					uth.start();
				}
				while(players.size()>3) {	// 이 방에 접속된 유저의 수가 4명 이상이면 더이상 새 유저를 받지 않음.
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 어차피 모든 플레이어가 READY가 아니라면 방장 클라이언트 자체에서도 시작버튼은 막혀있지만, 그래도 검증하여 모두 READY면 시작, 아니면 무응답. 
	 */
	public void GameStartBool() {
		boolean allready=true;
		for(Player p : players) {
			if(p.getReady()==0) {
				allready=false;
				break;
			}
		}
		if(allready==true) {
			for(Player p : players) {
				p.setReady(0);
			}
			WhosTurn();
			GameStart(players.size(), false);
		}
		return;
	}
	/**
	 * 모든 클라이언트들에게 players 정보를 뿌려주어 정보를 최신화시킴.
	 */
	public void Refresh() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Refresh(players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 방장클라이언트->방장유저쓰레드로 유저q의 밴요청->유저쓰레드로부터 전달받아 밴절차 실행
	 * @param q: 밴 당할 플레이어
	 */
	public void Ban(Player q) {
		while(q.getUth().isAlive()) {	// 밴 당할 유저쓰레드가 아직 살아있다면 우선 죽여줌.
			q.getUth().toStop();
			q.getUth().interrupt();
		}
		while(!q.getSocket().isClosed()) {
			try{ q.getSocket().close();	}catch(Exception e) { e.printStackTrace(); }	// 혹시 유저의 Socket이 안닫혔다면, 닫아줌. 닫힐 때까지 반복.
		}
		String strtemp = q.getUser().getNickname();		// 누가 밴당했는지 알림메세지도 표시해주기 위해, 플레이어 정보를 날리기 전에 닉네임만 백업.
		players.remove(q);
		int index=0;
		for(Player p : players) {		// 밴당한 플레이어가 중간에 빠졌더라도, 다시 순서대로 order를 0부터 순서대로 재정의.
			p.setOrder(index++);
		}
		checkUth();						// 혹시 밴당하면서 다른 유저의 쓰레드가 영향이 있었는지 살펴보고, 있다면 복구해줌.
		MoneyRefresh();					// 밴 당하면서 READY한 플레이어라면 판돈 돌려주므로 총 베팅금 상태 최신화.
		Date d = new Date();			// 누가 밴 당했는지 남아있는 유저들에게 알림메세지 송출.
		try {
			for(Player p : players) {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.HesBanned(strtemp, f.format(d), players));
				out.flush();
			}
		} catch(Exception e) {e.printStackTrace();}
		
		Refresh();						// 클라이언트들에게 플레이어 정보 최신화시켜줌.
		return;
	}
	/**
	 * 클라이언트->유저쓰레드로 자신q의 나가기 요청->유저쓰레드로부터 전달받아 유저q의 삭제 실행. (유저q는 q대로 클라이언트에서 자체 Socket Close 후 창 Dispose.)
	 * @param q: 나가는 유저
	 * @param strtemp: 나가는 유저의 닉네임
	 */
	public void SelfOut(Player q, String strtemp) {
		while(q.getUth().isAlive()) {	// 나가는 신호를 받은 해당 유저쓰레드는 스스로 제대로 종료절차를 밟았겠지만, 혹시 모르니 제대로 종료될 때까지 기다림.
		}
		while(!q.getSocket().isClosed()) {
			try{ q.getSocket().close();	}catch(Exception e) { e.printStackTrace(); }	// 혹시 유저의 Socket이 안닫혔다면, 닫아줌. 닫힐 때까지 반복.
		}
		getPlayers().remove(q);
		int index=0;
		for(Player pl : players) {		// 나간 플레이어가 중간에 빠졌더라도, 다시 순서대로 order를 0부터 순서대로 재정의.
			pl.setOrder(index++);
		}
		checkUth();						// 혹시 나가면서 다른 유저의 쓰레드가 영향이 있었는지 살펴보고, 있다면 복구해줌.
		Date d = new Date();
		for(Player p2 : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p2.getSocket().getOutputStream()));
				out.writeObject(Gaming.HesOut(strtemp, f.format(d), players));
				out.flush();
			} catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 누군가 튕겼을 때. 스스로 나간 것도 아니고, 방장이 강퇴한 것도 아닌데 어떤 유저쓰레드의 InputStream에서 예외가 발생하면 튕긴 것이므로, 아래 메서드가 실행되도록 함.
	 */
	public void HesClosed() {
		ArrayList<Player> tmp = new ArrayList<>();
		ArrayList<String> tmpstr = new ArrayList<>();
		Date d = new Date();
		for(Player p : players) {
			if(p.getUth().isStop()==true) {			// 누군가의 유저쓰레드가 멈춘상태라면(튕긴 예외발생때 제대로 toStop() 신호는 보냈음.)
				tmpstr.add(p.getUser().getNickname());		// 유저의 닉네임은 미리 복사해두고
				while(p.getUth().getThreadGroup()!=null) {			// 유저쓰레드가 아직 실행중이라면 stop=true 됐어도 in.close()가 됐는지도 불안하고, 다른 종료명령도 필요할 것 같아서.
					try { p.getUth().getIn().close(); } catch(Exception e) { e.printStackTrace(); }
					p.getUth().interrupt();
				}
				if(!p.getSocket().isClosed()) {
					try { p.getSocket().close(); } catch(Exception e) { e.printStackTrace(); }
				}
			} else {								// 유저쓰레드가 제대로 돌아가는 애라면 정상리스트(tmp)에 추가.
				tmp.add(p);
			}
		}
		int index=0;
		for(Player p : tmp) {		// tmp에 추가된 Player들은 정상적인 애들이고, 0부터 order 재정렬 후, 이 tmp를 players로 사용.
			p.setOrder(index++);
		}
		setPlayers(tmp);
		Refresh();					// 클라이언트들에게 players 정보 최신화시켜줌.
		for(Player p : getPlayers()) {			// 남은 사람들에게 누가 튕겼는지 알려줌(메세지는 그냥 스스로 나간 퇴장처럼 보임)
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
	/**
	 * 클라이언트들에게 현재 누구(0~3)의 턴인지 뿌려줌.
	 */
	public void WhosTurn() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Turn(getWhosturn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * 현재 턴 수를 셈. 의미없지만 체크가능한지 알 수 있음. 시작시 1턴, 카드 새로받을시 1턴이라, 1턴이면서 3번째카드를 받았다면 체크가 가능함.
	 */
	public void IncreaseTurn() {
		increaseTurn();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.TurnRefresh(getTurn()));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 다음턴 계산을 함.
	 * 1명만 남았다면 그 사람이 무조건 우승하거나,
	 * 아직 아무것도 안한 플레이어가 있다면 계산할 필요없이 턴이 넘어가고,
	 * 아직 3번째 카드를 받지 않은 상태에서 + 2명이상 살아있는데 + 최근 베팅금액까지 모두 같으면 3번째 카드를 받게되고,
	 * 3번째 카드도 받았는데 + 2명이상 살아있고 + 
	 */
	public void nextTurn() {
		getNowtimer().setKill(true);
		while(!getNowtimer().isAlive()) {
		}
		/**
		 * alive: 다이베팅하지 않은 유저의 수를 셈.
		 * sumcall: 콜베팅한 유저의 수를 셈.
		 * ischecked: 선이 체크했는지 셈. 콜와 체크의 수의 합이 생존자의 수와 같다면, 다이할 사람은 빼고 나머지가 모두 판종료를 선언한 것.
		 * sumone: 아직 어떠한 베팅도 하지 않은 유저의 수를 셈. 모두가 다이하지 않은 이상, 이 사람도 뭔가 베팅하긴 해야함.
		 */
		int alive=0;
		int sumcall=0;
		int ischecked=0;
		int sumnone=0;
		for(Player p : players) {
			if(p.getBetbool()!=1) { alive++; }
			if(p.getBetbool()==2) { sumcall++; }
			if(p.getBetbool()==4) { ischecked++; }
			if(p.getBetbool()==0) { sumnone++; }
		}
		
		// 아무도 오픈패 지정 안해서 다 죽음;;
		if(alive==0) {
			for(Player p : players) {
				p.setGameresult(2);	// 모두 재경기 대상 처리.
			}
			toresult();					// 클라이언트들에게 게임종료화면 띄우게 함.
			Message_Re();				// 클라이언트들에게 3초후 대상자끼리 재경기한다고 알림 메세지 송출.
			try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			GameStart(thisplaynum, true);		// 이번판 인원 재경기 시작
			return;
		} else if(alive==1) {
			// 살아있는사람이이김
			for(Player p : players) {
				if (p.getBetbool()!=1) {
					// 얘가 우승자임
					ArrayList<String> tmplist = new ArrayList<>();
					for(Player q : players) {
						q.setGameresult(0);
					}
					p.setGameresult(1); calc(p);
					tmplist.add(p.getUser().getId());
					toresult();
					Message_Win(p.getUser().getNickname());
					try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
					// 버튼활성화
					resetcards();
					rebatch(tmplist, false);
					toButtonOk();
					for(Player q : players) {
						q.setBetbool(0);
					}
					Refresh();
					setInggame(false);
					break;
				}
			}
			return;
		}
		else {
			if(sumnone>0) {
				for(int i=1;i<players.size();i++) {
					Player p = players.get((getWhosturn()+i)%players.size());
					if(p.getBetbool()!=1) {
						setWhosturn(p.getOrder());
						break;
					}
				}
				WhosTurn();
				if(isInggame()==true) {
					players.get(getWhosturn()).setBetbool(0);
					Refresh();
					
					setTurn(getTurn()+1);
					makeTimer();
				}
			} else {
				// 플레이어들의 최근 베팅금액이 동일한지 판별
				boolean allbetequal = true;
				int bet = 0;
				for(Player p : players) {
					if(p.getBetbool()!=1) {
						if(bet==0) {
							bet=p.getThisbet();
						} else {
							if(p.getThisbet()!=bet) {
								allbetequal = false;
								break;
							}
						}
					}
				}
				if(alive==ischecked+sumcall && isPhase2()==true) {
					// 결판
					ArrayList<Player> tmplist = new ArrayList<>();
					for(Player p : players) {
						if(p.getBetbool()!=1) tmplist.add(p);
					}
					makeSelSetTimer(tmplist);
				} else if(allbetequal==true) {
					if(isPhase2()==false) {
						getNowtimer().setKill(true);
						Draw2Phase();
					} else {
						// 결판
						ArrayList<Player> tmplist = new ArrayList<>();
						for(Player p : players) {
							if(p.getBetbool()!=1) tmplist.add(p);
						}
						makeSelSetTimer(tmplist);
					}
				} else {
					// 턴 넘기기
					for(int i=1;i<players.size();i++) {
						Player p = players.get((getWhosturn()+i)%players.size());
						if(p.getBetbool()!=1) {
							setWhosturn(p.getOrder());
							break;
						}
					}
					WhosTurn();
					if(isInggame()==true) {
						players.get(getWhosturn()).setBetbool(0);
						Refresh();
						
						setTurn(getTurn()+1);
						makeTimer();
					}
				}
			}
		}
		return;
	}
	/**
	 * 살아남은 유저가 2명 이상인데 판끝내기가 신청된 경우, 아래의 메서드가 마무리해줌.
	 * @param players: 이 메서드를 호출할 때, 살아남은 유저들만 담긴 List를 인자로 호출할 것임.
	 */
	public void whosWin(ArrayList<Player> players) {
		int num = players.size();
		String userid1=players.get(0).getUser().getId();
		String userid2=players.get(1).getUser().getId();
		int cardresult1=players.get(0).getSelCardset();
		int cardresult2=players.get(1).getSelCardset();
		String userid3=null;
		int cardresult3=0;
		String userid4=null;
		int cardresult4=0;
		if(players.size()>2) {								// 수가 많으면 계속해서 담지만, 수가 적으면 어차피 num에서 플레이어 수를 줬으므로, 판별 Logic에서는 num 초과 유저가 null이라도 상관없음.
			userid3=players.get(2).getUser().getId();
			cardresult3=players.get(2).getSelCardset();
		}
		if(players.size()>3) {
			userid4=players.get(3).getUser().getId();
			cardresult4=players.get(3).getSelCardset();
		}
		// 비교할 플레이어 수와 최종패를 담아 List를 받환받음.
		ArrayList<String> result = Logic.GameResult(num, userid1, cardresult1, userid2, cardresult2, userid3, cardresult3, userid4, cardresult4);
		for(Player p : players) {
			p.setGameresult(0);		// 우선 모든 플레이어의 게임결과는 0(패배)로 해두고,
		}
		if(result.size()==1) {		// 반환받은 List에 플레이어가 1명이라면 혼자 우승한 것이니, 그 1명 우승 처리.
			ArrayList<String> tmplist = new ArrayList<>();
			for(Player p : players) {
				if(p.getUser().getId().equals(result.get(0))) { p.setGameresult(1); calc(p); tmplist.add(p.getUser().getId()); Message_Win(p.getUser().getNickname()); break; }
			}
			toresult();					// 클라이언트들에게 게임종료화면 띄우게 함.
			try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
			resetcards();				// 5초 후 카드들 중간에 배치시키고, 아래 플레이어 정보 최신화과정 실행.
			rebatch(tmplist, false);		// 이긴 사람이 선(방장)이 되기 위해 재배치
			toButtonOk();				// 게임이 끝났으니 클라이언트들에게 걸린 버튼비활성을 풀고 게임 중이 아닌 상태로 돌아가게 함.
			for(Player q : players) {	// 게임 베팅상태도 0으로 모두 초기화하고
				q.setBetbool(0);
			}
			Refresh();					// 클라이언트들에게 플레이어 정보 최신화
			setInggame(false);			// 서버에서도 게임 중 아니라는 표시.
		} else {		// 반환받은 List에 플레이어가 2명 이상이라면 재경기해야하니, 재경기 처리.
			for(String s : result) {
				for(Player p : players) {
					if(p.getUser().getId().equals(s)) { p.setGameresult(2); break; }	// 반환받은 List에 해당하는 플레이어들 게임결과 재경기 대상 처리.
				}
			}
			toresult();					// 클라이언트들에게 게임종료화면 띄우게 함.
			Message_Re();				// 클라이언트들에게 3초후 대상자끼리 재경기한다고 알림 메세지 송출.
			try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			rebatch(result, true);		// 대상자끼리 앞으로 배치
			GameStart(result.size(), true);		// 바로 대상자만 재경기 시작(나머지 플레이어는 진 상태로 패도 받지않은 채 구경만 하게될 것임)
		}
		return;
	}
	private int resultbacksig;
	public int getResultbacksig() { return resultbacksig; }
	public void setResultbacksig(int resultbacksig) { this.resultbacksig = resultbacksig; }
	/**
	 * 클라이언트들에게 게임 결과화면을 띄우라고 신호 보냄
	 */
	public void toresult() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gameresult(players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * 클라이언트들에게 게임이 끝났으므로, 비활성화되었던 버튼들 활성화하라고 알려줌.
	 */
	public void toButtonOk() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ButtonOk());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 클라이언트들에게 총베팅금과 최소베팅금 정보를 최신화시킴.
	 */
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
	/**
	 * 클라이언트들에게 카드들 중간으로 모으라고 말해줌.
	 */
	public void resetcards() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.ResetCard());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 2페이즈로 넘어가게 되면(3번째 카드를 받는 사람이  나오면, 1페이즈에서 다이로 결판나지 않으면) 플레이어별로 정할 수 있는 최종패를 기억해둠(4칸 1차행렬 중 3칸에 각각 담음)
	 */
	public void timeToCardset() {
		for(Player p : players) {
			if(p.getBetbool()!=1) {
				p.setBetbool(0);
				p.setCardset(Logic.ResultSet(p.getCard1(), p.getCard2(), p.getCard3()));
			}
		}
		return;
	}
	/**
	 * players의 순서와 그에따른 order를 재배치함. 게임의 결과(이긴 사람이 방장(선))나 재경기시 재경기 대상자가 앞쪽에 배치하여 경기를 진행할 때. 즉 어떤 판이 끝났을 때 그 결과에 따라 호출됨.
	 * @param replayers: 앞쪽에 배치되어야할 유저의 id가 담긴 리스트를 인자로 받아옴.
	 * @param rematch: 재경기를 위한 배치면 true / 그냥 재배치일 경우 false 로 호출됨.
	 */
	public void rebatch(ArrayList<String> replayers, boolean rematch) {
		ArrayList<Player> init=getPlayers();
		ArrayList<Player> temp1=new ArrayList<>();
		ArrayList<Player> temp2=new ArrayList<>();
		for(Player p : init) {
			if (replayers.contains(p.getUser().getId())) {
				temp1.add(p);
				if(rematch==true) p.setGameresult(2);
			} else {
				temp2.add(p);
				if(rematch==true) p.setGameresult(0);
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
	/**
	 * 우승한 플레이어에게 전체 베팅된 금액을 주게 됨. 즉 전체 베팅금은 0으로, 우승한 플레이어의 돈은 그만큼 증가.
	 * @param p: 우승한 플레이어.
	 */
	public void calc(Player p) {
		p.getUser().setMoney(p.getUser().getMoney()+moneythisgame);
		serv.getMain().setMoney(p.getUser().getId(), p.getUser().getMoney()+moneythisgame);
		setMoneythisgame(0);
		for(Player q : players) {
			q.setGameresult(0);
		}
		p.setGameresult(1);
		Refresh();
	}
	/**
	 * 클라이언트들에게 누가 이겼다고 메세지를 송출.
	 * @param nick: 이긴 플레이어의 닉네임
	 */
	public void Message_Win(String nick) {
		Date d = new Date();
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Message_Win(nick, f.format(d), players));
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
	}
	/**
	 * 클라이언트들에게 방장이 바꾸자고 한 금액의 판돈으로 정보를 최신화.
	 * @param pandon: 판돈
	 */
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
	/**
	 * 클라이언트들에게 3초후 대상자끼리 재경기한다는 메세지 송출.
	 */
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
		getNowtimer().setKill(true);	// 돌고 있던 타이머 멈춤(클라이언트에게 전송되던 타이머 정보 모두 정지.)
		setPhase2(true);				// 3번째 카드 나눠주게 되는 상황이라고 표시
		timeToCardset();				// 생존자 대상으로 카드 3장으로 만들 수 있는 최종패에 대한 3가지 경우의 수를 담아둠.
		Refresh();						// 플레이어들은 최종패 3가지 경우의 수를 받게됨.
		for(Player p : players) {		// 플레이어들은 아래 신호를 받아 GUI상으로 카드를 받는 액션을 보게됨.
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Draw2Phase());
				out.flush();
			}catch(Exception e) {e.printStackTrace();}
		}
		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }		// 카드 액션 기다리고
		// 3번째 카드 받되, 순서는 원래 선으로 돌아감(선이 죽었으면 그 다음부터 살아있는 플레이어로부터)
		for(Player p : players) {
			if(p.getBetbool()!=1) {
				setWhosturn(p.getOrder());
				break;
			}
		}
		setTurn(1);
		if(isInggame()==true) {
			players.get(getWhosturn()).setBetbool(0);
			Refresh();
			makeTimer();
		}
		return;
	}
	/**
	 * 카드를 섞은 후, 위부터 차례로 카드 정보를 가짐. 이후에 GIVE CARD 신호를 보낼 때 이 정보를 나눠줄 것임.
	 */
	public void cardShuffle() {
		Collections.shuffle(cards);
		int index=0;
		for(Player p : players) {
			p.setCard1(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard2(cards.get(index++));
		}
		for(Player p : players) {
			p.setCard3(cards.get(index++));
		}
		return;
	}
	/**
	 * 타이머 스레드를 만듦. 타이머 생성시마다 이 쓰레드를 호출.
	 */
	public void makeTimer() {
		Timer t = new Timer(this, players.get(getWhosturn()), getTurn());
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		return;
	}
	/**
	 * 오픈 패 정할 첫 타이머 스레드를 만듦. 매 경기의 맨 처음에 쓰는 오픈타이머에만 이 쓰레드를 호출.
	 */
	public void makeOpenTimer() {
		Timer t = new OpenTimer(this);
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		return;
	}
	/**
	 * 최종 패 정할 마지막 타이머 스레드를 만듦. 매 경기의 맨 마지막에 쓰는 최종패타이머에만 이 쓰레드를 호출.
	 */
	public void makeSelSetTimer(ArrayList<Player> pl) {
		Timer t = new SelSetTimer(this, pl);
		setNowtimer(t);
		t.setDaemon(true);
		t.start();
		return;
	}
	/**
	 * 클라이언트들에게 게임 시작 신호를 보냄.
	 * @param num: 이번 게임에 시작부터 참여하는 플레이어 수
	 */
	public void sendStartSignal(int num) {
		for(Player p : players) {
			System.out.println(p.getUser().getId());
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.Gamestart(num, getMoneythisgame(), getMinforbet()));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * 클라이언트들에게 카드 2장을 주면서 카드를 받은 GUI 액션을 보게함.
	 */
	public void giveFirstCards() {
		for(Player p : players) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(p.getSocket().getOutputStream()));
				out.writeObject(Gaming.GiveCard(players));
				out.flush();
			}
			catch (Exception e) {e.printStackTrace();}
		}
		return;
	}
	/**
	 * 게임 시작을 위한 메서드
	 * @param num: 게임 시작 인원
	 * @param rematch: 이 게임이 재경기 게임인지, 새로하는 경기인지
	 */
	public void GameStart(int num, boolean rematch) {
		setThisplaynum(num);
		setPhase2(false);
		setInggame(true);
		if(rematch==false) setMinforbet(0);		// 재경기가 아니라 아예 새게임이라면 최소판돈을 초기화 
		for(Player p : players) {
			p.setThisbet(0);
			p.setTrash(0);
			p.setGameresult(0);
			p.setCardset(new int[4]);
			if(rematch==false) {				// 아예 새게임이라면 모두 베팅정보를 없애고,
				p.setBetbool(0);
			} else {							// 재경기라면 모두 베팅정보를 다이베팅으로 한 뒤,
				p.setBetbool(1);
			}
		}
		for(int i=0;i<num;i++) {
			players.get(i).setBetbool(0);		// 재경기 대상자만 베팅정보를 초기화하여 경기에 참여시킴. (나머지는 다이베팅상태라 턴이 오지 않음.)
		}
		Refresh();
		setTurn(1);
		setWhosturn(0);
		WhosTurn();
		cardShuffle();
		try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		if(rematch==true) {
			try { Thread.sleep(900); } catch (InterruptedException e) { e.printStackTrace(); }
			sendStartSignal(num);
		} else {
			sendStartSignal(players.size());
		}
		giveFirstCards();
		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
		makeOpenTimer();
		return;
	}
	/**
	 * 돌아가는 유저쓰레드 중, 정지시킨 쓰레드가 아닌데도 동작을 멈춘 쓰레드가 있는지 검사하고, 있다면 복구시킴.
	 */
	public void checkUth() {
		for(Player p : players) {
			// 유저쓰레드가 종료명령을 받은 것이 아닌데, 유저쓰레드가 실행 중이 아니라면(쓰레드 그룹이 main도 아니고 null)
			if(p.getUth().isStop()==false && p.getUth().getThreadGroup()==null) {
				p.getUth().setDaemon(true);
				p.getUth().start();		// 유저쓰레드를 다시 실행시켜줌.
			}
		}
		return;
	}
}