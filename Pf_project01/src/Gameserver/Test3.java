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
		
		Thread a = new Thread();
		a.setDaemon(true);
		a.start();
		
		System.out.println(a.isAlive()+"/"+a.getThreadGroup());
		a.interrupt();
		while(true) {
			System.out.println(a.isAlive()+"/"+a.getThreadGroup());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
