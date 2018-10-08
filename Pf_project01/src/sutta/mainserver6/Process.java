package sutta.mainserver6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sutta.gameserver.Server;
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
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
	}
	
	public void exitRoom() throws Exception {
		Room target = r;
		c.user = (User) c.in.readObject();
		System.out.println("c.user = "+c.user);
		target.removeUser(c.user);
		target.minusCnt();
		System.out.println("현재 방 인원 = "+target.getCnt());
		if(target.getCnt() == 0) {
			roomPort.add(target.getPort());
			int index = roomList.indexOf(target);
			roomList.remove(target);
			serverList.get(index).serverClose();
			serverList.remove(index);
		}
	}
	
	public void joinRoom(int index) {
		r = roomList.get(index);
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
		Server sv = new Server(port);
		sv.setDaemon(true);
		sv.start();
		serverList.add(sv);
		r.setPort(port);
		roomList.add(r);
		//새 방 접속
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		boolean isPlay = true;
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
				boolean isPlaying = c.in.readBoolean();
				r.setIng(isPlaying);
				break;
				//게임이 시작되면 방 상태를 진행중으로 변경
				//게임이 끝나면 방 상태를 대기중으로 변경 
				//room.isIng();
				//room.setIng(상태);
			case 5:
				//로그아웃
				c.user.setLogin(false);
				c.interrupt();
				isPlay = false;
			}
			
		}
	}
	
}
