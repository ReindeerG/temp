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
 *���� ���� Ŭ����
 */
public class MainServer  extends Thread{
	public static final int JOIN = 0;
	public static final int ADDROOM = 1;
	public static final int QUICKJOIN = 2;
	
	/**
	 * Ŭ���̾�Ʈ �����
	 * �������� �����
	 */
	private List<Client> list = new ArrayList<>();
	private ServerSocket m_server;
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<String> model = new ArrayList();
	
	/**
	 * �����ڸ� ���Ѵ�� �޾� ����ҿ� �����ϰ� ȭ���� �����Ű�� ������
	 */
	private MainServer() {
		
		try {
			m_server = new ServerSocket(53890);
			this.setDaemon(true);
			this.start();
			while(true) {
				Socket socket = m_server.accept();
				
				//������ ������ ����
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
	 * �� ����� ��ü���� �ѷ��ִ� �޼ҵ�
	 * @throws IOException
	 */
	public void broadCast() throws IOException {
		for(Client c : list) {
			c.send();			
		}
	}
	
	/**
	 * ����ؼ� �ѷ��ִ� ��Ȳ(�ǽð�)
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
	 * ������ ���� Ŭ������ �����带 ���
	 * ������� ����ؼ� Ŭ���̾�Ʈ�� ������ ��ȣ�� ����ϰ� 
	 * ��ȣ�� ���� ������ ó���� ���ش�
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
		 * model(����)�� List<String>�� ���·� Ŭ���̾�Ʈ�� �����ϴ� �޼ҵ�
		 * @throws IOException
		 */
		public void send() throws IOException {
			out.writeObject(roomList);
			out.flush();
		}
		
		//Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �޾ƿ� ��Ȳ�� �´� ó���� ���ش�
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
