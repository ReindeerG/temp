package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

public class Process {
	private ObjectInputStream in;
	private ArrayList<Room> roomList;
	private User user;
	
	public Process(ObjectInputStream in, ArrayList<Room> roomList, User user) {
		this.in = in;
		this.roomList = roomList;
		this.user = user;
	}
	
	public void removeRoom() {
//		게임서버로부터 방의 접속자 수를 받아서 접속자 수가 0이면 방 삭제
	}
	
	public void joinRoom(int index) {
		if(roomList.get(index).getCnt()>=4) {
			System.out.println("만원");
		}
		else {
			System.out.println((index+1)+"번방 참가 완료");
			roomList.get(index).plusCnt();
			roomList.get(index).addUser(user);
			System.out.println(roomList.get(index).getCnt()+"명");
			if(roomList.get(index).getCnt() == 4) {
				roomList.get(index).setIng(true);		
			}
//		inet = InetAddress.getByName("192.168.0.?");
//		g_socket = new Socket(inet,g_port);
//		c_socket = new Socket(inet,c_port);
//		연결하고 정보 전달(참가자 정보 전달)
		}
	}
	
	public void newRoom(String name) throws ClassNotFoundException, IOException {
		Room r = new Room(name);
		//방 정보 받아서 list에 저장
		roomList.add(r);
		//새 방 접속
		joinRoom(roomList.indexOf(r));
	}
	
	public void process() throws Exception{
		while(true) {
			int choose = in.readInt();
//			System.out.println("choose = "+choose);
			switch(choose) {
			case Signal.JOIN:
				//해당 방 게임 서버, 채팅 서버에 접속
				int index = in.readInt();
				if(index != -1) {
					joinRoom(index);
				}
				break;
				
			case Signal.ADDROOM:
				//새로운 방 만들기
				String roomName = (String)in.readObject();
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
			}
			
		}
	}
	
}
