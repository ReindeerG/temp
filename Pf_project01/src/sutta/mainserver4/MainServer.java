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

	//회원 정보 관리 
	private ArrayList<User> user_list = new ArrayList<>();
	
	
	
	
	/**
	 * 접속자를 무한대로 받아 저장소에 저장하고 화면을 실행시키는 생성자
	 */
	private MainServer() {
		
		try {
			m_server = new ServerSocket(53890);
//			this.setDaemon(true);
//			this.start();
			while(true) {
				Socket socket = m_server.accept();
				
				//접속자 정보를 저장
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
		private InetAddress inet;
		private int g_port = 53891;
		private int c_port = 53892;
		
		/**
		 * model(방목록)을 List<String>의 형태로 클라이언트에 전송하는 메소드
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
				//로그인이 완료될 때 까지 실행 
				while(!isMember) {
					//어떤 버튼을 누르느냐에 따라 다르다
					//0  회원 가입
					//1 로그인
					int num = in.readInt();
					switch(num) {
					case 0:
						//회원가입
						User u1 = (User)in.readObject();
						boolean isUser = false;
						if(user_list.size()>0) {
							//회원이 있을 때 회원 가입
							for(int i = 0; i < user_list.size(); i++) {
								User target = user_list.get(i);
								//같은 아이디 혹은 닉네임이 존재하면 회원가입 진행 중단
								if(target.id.equals(u1.id)||target.nickname.equals(u1.nickname)) {
									isUser = true;
									break;
								}
							}
							//회원목록에 없을 경우 회원 가입 진행
							if(!isUser) {
								u1.money = 300000;//초기 돈 설정 (임의로 설정 추후 변경)
								user_list.add(u1);
							}
							out.writeBoolean(!isUser);
							out.flush();
							user = u1;
							System.out.println(!isUser+"전송");
						}
						else {
							//회원이 없을때 가입
							user_list.add(u1);
							out.writeBoolean(true);
							out.flush();
							System.out.println("true 전송");
						}
						break;
					case 1:
						//로그인
						User u2 = (User)in.readObject();
						
						//회원 목록에서 일치하는 것을 받아와 로그인 진행
						for(int i = 0; i<user_list.size(); i++) {
							User user = user_list.get(i);
							if(user.id.equals(u2.id) && user.pw.equals(u2.pw)) {
								isMember = true;
								u2 = user_list.get(i);
								break;
							}
						}
						//회원일 때 실행 시킨다  
						if(isMember) {
							user = u2;
							out.writeBoolean(isMember);
							out.flush();
						}
						//아이디 혹은 비밀번호가 일치하지 않을 때 
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
		
		
		//클라이언트로부터 이벤트를 받아와 상황에 맞는 처리를 해준다
		public void run() {
			try {
				loginProc();
				//방 목록을 뿌려준다
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
