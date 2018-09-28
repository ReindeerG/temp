package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import Chatserver.Message;

public class Client extends Thread {
	private String userid = "aa";
	private ArrayList<Player> players;
	private int port = 53891;
	private Socket socket = null;
	
	public Socket getSocket() {
		return socket;
	}
	
	public Client() {
		try {
			InetAddress inet = InetAddress.getByName("192.168.0.14");
			socket = new Socket(inet, port);
		} catch(Exception e) {}
	}
	
	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(new Gaming(userid, Gaming.CHAT_JOIN));
		} catch(Exception e) {}
		
		while(true) {
			try {
				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				
				Gaming gm = (Gaming)in.readObject();				
				switch(gm.getWhat()) {
				case Gaming.CHAT: {
					System.out.println("["+gm.getDate()+"] <"+gm.getUserid()+"> "+gm.getMsg());
					break;
				} 
				case Gaming.CHAT_JOIN: {
					players=gm.getPlayers();
					System.out.println("["+gm.getDate()+"] ("+gm.getUserid()+"님이 채팅방에 입장하셨습니다.)");
					break;
				} 
				case Gaming.CHAT_LEAVE: {
					players=gm.getPlayers();
					System.out.println("["+gm.getDate()+"] ("+gm.getUserid()+"님이 채팅방에서 퇴장하셨습니다.)");
					break;
				}
				case Gaming.CHAT_NICKCHANGE: {
					System.out.println("["+gm.getDate()+"] ("+gm.getUserid()+"님이 "+gm.getMsg()+"로 닉네임을 변경하셨습니다.)");
					players=gm.getPlayers();
					break;
				}
				case Gaming.GETCARD: {
					players=gm.getPlayers();
					int card1 = gm.getCard1(); int card2 = gm.getCard2(); int card3 = gm.getCard3();
					System.out.println(card1+" "+card2+" "+card3);
					break;
				}
				case Gaming.GAME_GO:
				case Gaming.GAME_DIE:
				{
					players=gm.getPlayers();
					break;
				}
				
				
				default: break;
			}
				
			} catch(Exception e) {}
		}
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
