package Gameserver;

import java.util.ArrayList;

import javax.swing.JOptionPane;

class si extends Thread {
	private Server s;
	public si(Server a) {
		this.s=a;
	}
	public void run() {
		while(true) {
			JOptionPane.showMessageDialog(null, null, null, 0);
			System.out.println("----------");
			for(Player p : s.getPlayers()) {
				System.out.println(p.getOrder()+"("+p.getUserid()+")-"+p.getUth().isAlive());
			}
		}
	}
}

public class Test01 {
	public static void main(String[] args) {
		Server a = new Server();
		si t = new si(a);
		t.setDaemon(true);
		t.start();
//		a.run();
	}
}