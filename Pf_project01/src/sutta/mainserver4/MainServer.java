package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class User implements Serializable{
	String nickname;
	String id;
	String pw;
	int money;
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

	//ȸ�� ���� ���� 
	private ArrayList<User> user_list = new ArrayList<>();
	
	
	
	
	/**
	 * �����ڸ� ���Ѵ�� �޾� ����ҿ� �����ϰ� ȭ���� �����Ű�� ������
	 */
	private MainServer() {
		
		try {
			m_server = new ServerSocket(53890);
//			this.setDaemon(true);
//			this.start();
			while(true) {
				Socket socket = m_server.accept();
				
				//������ ������ ����
				Client c = new Client();
				c.socket = socket;
				c.out = new ObjectOutputStream(socket.getOutputStream());
				c.in = new ObjectInputStream(socket.getInputStream());
				c.setDaemon(true);
				c.start();
				list.add(c);
//				this.start();
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
//	public void run() {
//		while(true) {
//			try {
//				broadCast();
////				Thread.sleep(1000/60);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	@Override
	public void run() {
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
		private InetAddress inet;
		private int g_port = 53891;
		private int c_port = 53892;
		
		/**
		 * model(����)�� List<String>�� ���·� Ŭ���̾�Ʈ�� �����ϴ� �޼ҵ�
		 * @throws IOException
		 */
		public void send() throws IOException {
			ArrayList<Room> target = roomList;
			out.writeObject(target);
			out.flush();
		}
		
		public void loginProc(){
			try {
				boolean isMember = false;
				//�α����� �Ϸ�� �� ���� ���� 
				while(!isMember) {
					//� ��ư�� �������Ŀ� ���� �ٸ���
					//0  ȸ�� ����
					//1 �α���
					int num = in.readInt();
					switch(num) {
					case 0:
						//ȸ������
						User u1 = (User)in.readObject();
						boolean isUser = false;
						if(user_list.size()>0) {
							//ȸ���� ���� �� ȸ�� ����
							for(int i = 0; i < user_list.size(); i++) {
								User target = user_list.get(i);
								//���� ���̵� Ȥ�� �г����� �����ϸ� ȸ������ ���� �ߴ�
								if(target.id.equals(u1.id)||target.nickname.equals(u1.nickname)) {
									isUser = true;
									break;
								}
							}
							//ȸ����Ͽ� ���� ��� ȸ�� ���� ����
							if(!isUser) {
								u1.money = 300000;//�ʱ� �� ���� (���Ƿ� ���� ���� ����)
								user_list.add(u1);
							}
							out.writeBoolean(!isUser);
							out.flush();
							user = u1;
							System.out.println(!isUser+"����");
						}
						else {
							//ȸ���� ������ ����
							user_list.add(u1);
							out.writeBoolean(true);
							out.flush();
							System.out.println("true ����");
						}
						break;
					case 1:
						//�α���
						User u2 = (User)in.readObject();
						
						//ȸ�� ��Ͽ��� ��ġ�ϴ� ���� �޾ƿ� �α��� ����
						for(int i = 0; i<user_list.size(); i++) {
							User user = user_list.get(i);
							if(user.id.equals(u2.id) && user.pw.equals(u2.pw)) {
								isMember = true;
								u2 = user_list.get(i);
								break;
							}
						}
						//ȸ���� �� ���� ��Ų��  
						if(isMember) {
							user = u2;
							out.writeBoolean(isMember);
							out.flush();
						}
						//���̵� Ȥ�� ��й�ȣ�� ��ġ���� ���� �� 
						else {
							out.writeBoolean(isMember);
							out.flush();
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		//Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �޾ƿ� ��Ȳ�� �´� ó���� ���ش�
		public void run() {
			try {
				loginProc();
				//�� ����� �ѷ��ش�
				send();
				Process p  = new Process(in, roomList);
				p.process();
			}catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
