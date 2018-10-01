package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import sutta.mainserver.MainServer;

public class Process {
	private ObjectInputStream in;
	private ArrayList<Room> roomList;
	
	public Process(ObjectInputStream in, ArrayList<Room> roomList) {
		this.in = in;
		this.roomList = roomList;
	}
	
	public void joinRoom(int index) {
//		System.out.println("방 참가 완료");
		roomList.get(index).cnt++;
		if(roomList.get(index).cnt == 4) {
			roomList.get(index).ing = true;
		}
//		inet = InetAddress.getByName("192.168.0.?");
//		g_socket = new Socket(inet,g_port);
//		c_socket = new Socket(inet,c_port);
	}
	
	public void newRoom() throws ClassNotFoundException, IOException {
		Room r = new Room();
		r.name = (String)in.readObject();
		r.cnt = 1;
		r.ing = false;
		//방 정보 받아서 list에 저장
		roomList.add(r);
		//새 방 접속
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		while(true) {
			int choose = in.readInt();
			switch(choose) {
			case MainServer.JOIN:
				//해당 방 게임 서버, 채팅 서버에 접속
				int index = in.readInt();
				if(index != -1) {
					joinRoom(index);
				}
				break;
				
			case MainServer.ADDROOM:
				//새로운 방 만들기
				newRoom();
				break;
				
			case MainServer.QUICKJOIN:
//				System.out.println("빠른 시작할 수 있는 방 참가");
				Room target2 = null;
				for(Room r2 : roomList) {
					if(r2.cnt < 4) {
						target2 = r2;
						break;
					}
				}
				if(target2 != null) {
					joinRoom(roomList.indexOf(target2));
				}
				else {
					newRoom();
				}
				break;
			}
		}
	}
	
}
