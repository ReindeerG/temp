package sutta.mainserver2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class User{
	String nickname;
	String id;
}

/**
 *메인 서버 클래스
 */
public class MainServer  extends Thread{
	public static final int JOIN = 0;
	public static final int ADDROOM = 1;
	public static final int QUICKJOIN = 2;
	
	/**
	 * 클라이언트 저장소
	 * 서버소켓 저장소
	 */
	private List<Client> list = new ArrayList<>();
	private ServerSocket m_server;
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<String> model = new ArrayList();
	
	/**
	 * 접속자를 무한대로 받아 저장소에 저장하고 화면을 실행시키는 생성자
	 */
	private MainServer() {
		
		try {
			m_server = new ServerSocket(53890);
			this.setDaemon(true);
			this.start();
			while(true) {
				Socket socket = m_server.accept();
				
				//접속자 정보를 저장
				Client c = new Client();
				User u = new User();
				c.socket = socket;
				c.out = new ObjectOutputStream(socket.getOutputStream());
				c.in = new ObjectInputStream(socket.getInputStream());
				u.nickname = (String)c.in.readObject();
				u.id = (String)c.in.readObject();
				c.user = u;

				c.setDaemon(true);
				c.start();
				c.send();
				list.add(c);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * 방 목록을 전체에게 뿌려주는 메소드
	 * @throws IOException
	 */
	public void broadCast() throws IOException {
		for(Client c : list) {
			c.send();			
		}
	}
	
	/**
	 * 계속해서 뿌려주는 상황(실시간)
	 */
	public void run() {
		while(true) {
			try {
				broadCast();
//				Thread.sleep(1000/60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 접속자 관리 클래스로 스레드를 상속
	 * 스레드는 계속해서 클라이언트가 보내는 신호를 대기하고 
	 * 신호에 따라 적절한 처리를 해준다
	 */
	class Client extends Thread{
		Socket socket;
		Socket g_socket = null;
		Socket c_socket = null;
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		int money;
		private InetAddress inet;
		private int g_port = 53891;
		private int c_port = 53892;
		
		/**
		 * model(방목록)을 List<String>의 형태로 클라이언트에 전송하는 메소드
		 * @throws IOException
		 */
		public void send() throws IOException {
			out.writeObject(roomList);
			out.flush();
		}
		
		//클라이언트로부터 이벤트를 받아와 상황에 맞는 처리를 해준다
		public void run() {
			try {
				Process p  = new Process(in, roomList);
				p.process();
			}catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
