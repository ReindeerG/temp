package Gameserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {
	public static void main(String[] args) throws Exception {
		int port = 53891;
		InetAddress inet = InetAddress.getByName("192.168.0.14");
		Socket socket = new Socket(inet, port);
		
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		out.writeObject(new Gaming(0));
		out.flush();
		while(true) {
			out.writeObject(new Gaming(1));
			out.flush();
			Thread.sleep(1000);
		}
	}
}
