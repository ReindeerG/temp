package sutta.mainserver6;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sutta.gameserver2.Server;
import sutta.mainserver6.MainServer.Client;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

/**
 * 각각 클라이언트의 행동에 대한 신호를 받아와
 * 그에 맞게 처리해주는 클래스
 */
public class Process implements Signal{
	private Client c;
	private ArrayList<Room> roomList;
	private List<Integer> roomPort;
	private List<Server> serverList;
	private Room r;
	private Server server;
	private ArrayList<User> userList;
	private MainServer main;
	private boolean isPlay = true;
	
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList, ArrayList<User> userList, MainServer main) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
		this.userList = userList;
		this.main = main;
	}
	
	//방에서 나올 때 메소드
	public void exitRoom(){
		r.minusCnt();
		System.out.println(c.user.getNickname()+"의 남은 돈 = "+c.user.getMoney());
		server.removeUser(c.user);
		if(r.getCnt() == 0) {
			roomPort.add(r.getPort());
			roomList.remove(r);
			serverList.remove(server);
			server.serverClose();
		}
		server = null;
		r = null;
	}
	
	//방에 참가할 때 메소드
	public void joinRoom(int index) {
		r = roomList.get(index);
		server = serverList.get(index);
		server.addUser(c.user);
		if(r.isIng()) {
			try {
				c.out.writeObject(null);
				c.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			r.plusCnt();
			r.addUser(c.user);
			try {
				c.out.writeObject(r);
				c.out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
	}
	
	//새로운 방을 만들때 메소드
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		if(roomPort.size() > 0) {
			r = new Room(name, roomPort.get(0));
			roomPort.remove(0);
			//방 정보 받아서 port부여
			int port = r.getPort();
			Server sv = new Server(port, r.getUserList(), main);
			server =sv;
			sv.setDaemon(true);
			sv.start();
			serverList.add(sv);
			r.setPort(port);
			roomList.add(r);
			//새 방 접속
			joinRoom(roomList.indexOf(r));			
		}
		else {
			//방이 1000개 다 만들어 졌을 때
			c.out.writeObject(null);
			c.out.flush();
		}
	}
	//게임이 시작했는지 설정
	public void setIng(boolean ing) {
		r.setIng(ing);
	}
	//게임이 끝났을 때 끝났음을 알림
	public void logout() {
		c.user.setLogin(false);
		isPlay = false;
	}
	
	public void process(){
		try {
			while(isPlay) {	//종료 하기 전까지 반복
				int choose = c.in.readInt();
				switch(choose) {
				case JOIN:
					//해당 방 게임 서버, 채팅 서버에 접속
					int index = c.in.readInt();
					if(index != -1) {
						joinRoom(index);
					}
					break;
					
				case ADDROOM:
					//새로운 방 만들기
					String roomName = (String)c.in.readObject();
					if(roomName!=null) {
						newRoom(roomName);					
					}
					break;
					
				case QUICKJOIN:
					//빠른 방 참여 
					Room target2 = null;
					for(Room r2 : roomList) {
						System.out.println(r2.getName()+"방 참가 가능 여부"+(r2.getCnt() < 4 && !r2.isIng()));
						if(r2.getCnt() < 4 && !r2.isIng()) {
							target2 = r2;
							break;
						}
					}
					if(target2 != null) {
						joinRoom(roomList.indexOf(target2));
					}
					else {
						newRoom("빠겜");
					}
					break;
				case EXITROOM:
					//방 접속 종료
					exitRoom();
					break;
				case GAMESTART:
					//게임 시작
					setIng(true);
					break;
				case GAMEEND:
					//게임 종료
					setIng(false);
					break;
				case LOGOUT:
					//로그아웃
					logout();
					break;
				}
				
			}
			
		}catch(Exception e) {
			if(r!=null) {
				exitRoom();
			}
			logout();
		}
	}
	
}
