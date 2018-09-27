package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import Chatserver.Message;

public class Client {
	private static List<Player> players;
	
	public static void main(String[] args) throws Exception {
		int port = 53891;
		InetAddress inet = InetAddress.getByName("192.168.0.14");
		Socket socket = new Socket(inet, port);
		
		
		
		Thread t1 = new Thread() {
			public void run() {
				while(true) {
					try {
							ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
							Gaming gm = (Gaming)in.readObject();
							players = gm.getPlayers();
							System.out.println(gm.getWho()+"가 "+gm.getWhat()+"했음.");							
					} catch(Exception e ) {}
				}
			}
		};
		t1.setDaemon(true);
		t1.start();
		
		
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		
		while(true) {
			String msg = JOptionPane.showInputDialog("준비안함1, 준비2, 다이3, 고4");
			if(msg.equals("1")) {
				out.writeObject(new Gaming(Gaming.GAME_UNREADY));
				out.flush();
			}
			if(msg.equals("2")) {
				out.writeObject(new Gaming(Gaming.GAME_READY));
				out.flush();
			}
			if(msg.equals("3")) {
				out.writeObject(new Gaming(Gaming.GAME_DIE));
				out.flush();
			}
			if(msg.equals("4")) {
				out.writeObject(new Gaming(Gaming.GAME_GO));
				out.flush();
			}
		}
	}
}
