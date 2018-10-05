package sutta.mainserver6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Gameserver.Server;
import sutta.mainserver6.MainServer.Client;
import sutta.useall.Room;
import sutta.useall.Signal;

public class Process {
	private Client c;
	private ArrayList<Room> roomList;
	private List<Integer> roomPort;
	private List<Server> serverList;
	
	
	public Process(Client c, ArrayList<Room> roomList,List<Integer> roomPort, List<Server> serverList) {
		this.c = c;
		this.roomList = roomList;
		Collections.sort(roomPort);
		this.roomPort = roomPort;
		this.serverList = serverList;
	}
	
	public void removeRoom() {
//		게임서버로부터 방의 접속자 수를 받아서 접속자 수가 0이면 방 삭제
	}
	
	public void joinRoom(int index) {
		roomList.get(index).plusCnt();
		roomList.get(index).addUser(c.user);
		if(roomList.get(index).getCnt() == 4) {
		roomList.get(index).setIng(true);
		}
		try {
			c.out.writeObject(roomList.get(index));
			c.out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		Room r = new Room(name, roomPort.get(0));
		roomPort.remove(0);
		//방 정보 받아서 port부여
		int port = r.getNumber();
		Server sv = new Server(port);
		serverList.add(sv);
		System.out.println("게임서버 리스트에 저장");
		r.setPort(port);
		roomList.add(r);
		//새 방 접속
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		while(true) {
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
			}
			
		}
	}
	
}
