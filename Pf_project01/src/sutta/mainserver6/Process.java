package sutta.mainserver6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sutta.gameserver2.Server;
import sutta.mainserver6.MainServer.Client;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

public class Process {
	private Client c;
	private ArrayList<Room> roomList;
	private List<Integer> roomPort;
	private List<Server> serverList;
	private Room r;
	private Server server;
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
	}
	
	public void exitRoom() throws Exception {
//		Room target = r;
//		c.user = (User) c.in.readObject();
//		System.out.println("c.user = "+c.user);
//		target.removeUser(c.user);
//		target.minusCnt();
//		System.out.println("현재 방 인원 = "+target.getCnt());
//		if(target.getCnt() == 0) {
//			roomPort.add(target.getPort());
//			int index = roomList.indexOf(target);
//			roomList.remove(target);
//			serverList.get(index).serverClose();
//			serverList.remove(index);
//		}
		r.minusCnt();
		System.out.println("참가자 수  = "+r.getCnt());
		System.out.println(c.user.getNickname()+"의 남은 돈 = "+c.user.getMoney());
		server.removeUser(c.user);
		if(r.getCnt() == 0) {
			System.out.println("참가자 수 0일때 실행");
			roomPort.add(r.getPort());
			System.out.println("포트 리스트에 반환");
			System.out.println("방 목록에서 방 제거전"+roomList);
			for(Room r : roomList) {
				System.out.println(r);
			}
			roomList.remove(r);
			System.out.println("방 목록에서 방 제거후"+roomList);
			for(Room r : roomList) {
				System.out.println(r);
			}
			System.out.println("서버 목록에서 서버 제거전"+serverList);
			for(Server s : serverList) {
				System.out.println(s);
			}
			serverList.remove(server);
			System.out.println("서버 목록에서 서버 제거후"+serverList);
			for(Server s : serverList) {
				System.out.println(s);
			}
			server.serverClose();
			System.out.println("서버 소켓 종료");
		}
		server = null;
		r = null;
	}
	
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
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		r = new Room(name, roomPort.get(0));
		roomPort.remove(0);
		//방 정보 받아서 port부여
		int port = r.getPort();
		Server sv = new Server(port, r.getUserList());
		server =sv;
		sv.setDaemon(true);
		sv.start();
		serverList.add(sv);
		r.setPort(port);
		roomList.add(r);
		//새 방 접속
		joinRoom(roomList.indexOf(r));
	}
	
	public void process(){
		boolean isPlay = true;
		try {
			while(isPlay) {
				int choose = c.in.readInt();
//			System.out.println("choose = "+choose);
				switch(choose) {
				case Signal.JOIN:
					//해당 방 게임 서버, 채팅 서버에 접속
					int index = c.in.readInt();
					if(index != -1) {
						joinRoom(index);
					}
					break;
					
				case Signal.ADDROOM:
					//새로운 방 만들기
					String roomName = (String)c.in.readObject();
					if(roomName!=null) {
						newRoom(roomName);					
					}
					break;
					
				case Signal.QUICKJOIN:
//				System.out.println("빠른 시작할 수 있는 방 참가");
					Room target2 = null;
					for(Room r2 : roomList) {
						if(r2.getCnt() < 4) {
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
				case 3:
					//방 접속 종료 포트를 받아서 그 포트에 해당하는 서버 설정(cnt를)을 해준다
					//포트를 다시 roomPort리스트에 저장해준다 
					System.out.println("방 나가기");
					exitRoom();
					break;
				case 4:
					//방 시작
					r.setIng(true);
					server.setInggame(true);
					break;
				case 5:
					//게임 종료
					r.setIng(false);
					server.setInggame(false);
					break;
				case 6:
					//로그아웃
					c.user.setLogin(false);
					c.interrupt();
					isPlay = false;
					break;
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			try {
				exitRoom();
				c.user.setLogin(false);
				c.interrupt();
				isPlay = false;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
