package sutta.mainserver6;

import java.net.ServerSocket;
import java.net.Socket;

public class Gameserver {
	
	private ServerSocket g_server;
	private Socket[] socket = new Socket[4];
	private int cnt = 0;
	
	public void waitPlayer() throws Exception{
		if(cnt<4) {
			socket[cnt] = g_server.accept();
			cnt++;			
		}
		System.out.println(cnt+"명 접속 중");
	}
	
	
	public Gameserver(int port){
		try {
			g_server = new ServerSocket(port);
			System.out.println(port+"포트의 게임서버 시작");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
