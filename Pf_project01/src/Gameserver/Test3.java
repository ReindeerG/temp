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

public class Test3 {
	public static void main(String[] args) {
		
		List<Player> players = new ArrayList<>();
		Player a = new Player(players.size(), null, null);
		Player b = new Player(players.size(), null, null);
		Player c = new Player(players.size(), null, null);
		players.add(a);
		players.add(b);
		players.add(c);
		Object[] cb = players.toArray();
		Player[] arr = (Player[])cb;
		System.out.println(cb);
		
		
	}
}
