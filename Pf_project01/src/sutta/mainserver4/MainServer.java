package sutta.mainserver4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;


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
	private ArrayList<Client> list = new ArrayList<>();
	private ServerSocket m_server;
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<String> roomList2 = new ArrayList<>();

	/**
	 * 회원 정보 저장소
	 */
	private ArrayList<User> user_list = new ArrayList<>();
	
	
	
	
	/**
	 * 접속자(Client)를 무한대로 받아
	 * 저장소(list)에 저장하는 일을 한다
	 */
	private MainServer() {
		
		try {
			m_server = new ServerSocket(53890);
			//계속해서 방 목록을 뿌려주는 스레드 시작
			this.setDaemon(true);
			this.start();
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
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * 방 목록을 접속자 전체에게 뿌려주는 메소드
	 * @throws IOException
	 */
	public void broadCast(){
		try {
			roomList2.clear();
			for(int i = 0; i<roomList.size(); i++) {
				Room t = roomList.get(i);
				roomList2.add((i+1)+"번방        "+t.getName()+"                "+t.getCnt()+"/4");
			}
			System.out.println("list.size = "+list.size());
			for(int i=0; i<list.size();i++) {
				list.get(i).send();
			}
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	/**
	 * 계속해서 방 목록을 뿌려주는 일을 하는 메소드(스레드)
	 */
	public void run() {
		while(true) {
			try {
				broadCast();
				//1초에 한 번 씩 - 추후 변경 가능 
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for(int i = 0; i<list.size(); i++) {
				if(list.get(i).socket.isClosed()) {
					Client removed = list.get(i);
					list.remove(removed);
				}
			}
		}
	}

	
	/**
	 * 접속자 정보 관리 클래스로 스레드를 상속
	 * 접속자의 아이디와 비밀번호를 확인하고 회원 가입을 진행해준다 
	 */
	class Client extends Thread{
		Socket socket;
		Socket g_socket = null;		//게임서버 소켓 보관
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		boolean on = false;			//접속자의 로그인 상태
		private InetAddress inet;	//게임서버 주소
		private int g_port = 53891;	//게임서버 포트
		
		/**
		 * model(방목록)을 ArrayList<Room>의 형태로 클라이언트에 전송하는 메소드
		 * @throws IOException
		 */
		public void send() {
			try {
				if(on == true) {
					ArrayList<Room> target = (ArrayList<Room>) roomList2.clone();
//					for(int i = 0 ; i < target.size();i++) {
//						System.out.println("target.get("+i+").cnt = "+target.get(i).getCnt());
//					}
//					System.out.println("send = "+target.hashCode()+" / "+target);
					out.writeObject(target);
					out.flush();	
				}				
			}catch(Exception e) {
				e.getMessage();
			}
		}
		
		/**
		 * 회원가입 및 아이디 비밀번호가 일치하는지 확인하는 메소드 
		 */
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
					case Signal.ADDMEMBER:
						//회원가입 기입된 정보를 받아온다(u1)
						User u1 = (User)in.readObject();
						//이미 가입되어있는 아이디 혹은 닉네임인지 확인
						boolean isUser = false;
						if(user_list.size()>0) {
							//회원수가 0이 아닐 때 회원 가입
							for(int i = 0; i < user_list.size(); i++) {
								User target = user_list.get(i);
								//같은 아이디 혹은 닉네임이 존재하면 회원가입 진행 중단
								if(target.getId().equals(u1.getId())||target.getNickname().equals(u1.getNickname())) {
									isUser = true;
									break;
								}
							}
							//회원목록에 없을 경우 회원 가입 진행
							if(!isUser) {
								u1.setMoney(10000);//초기 돈 설정 (임의로 설정 추후 변경)
								user_list.add(u1);
							}
							out.writeBoolean(!isUser);
							out.flush();
							user = u1;
						}
						else {
							//회원이 없을때 가입
							user_list.add(u1);
							out.writeBoolean(true);
							out.flush();
						}
						break;
					case Signal.LOGIN:
						//로그인
						User u2 = (User)in.readObject();
						
						//회원 목록에서 일치하는 것을 받아와 로그인 진행
						for(int i = 0; i<user_list.size(); i++) {
							User user = user_list.get(i);
							if(user.getId().equals(u2.getId()) && user.getPw().equals(u2.getPw())) {
								isMember = true;
								u2 = user_list.get(i);
								break;
							}
						}
						//회원일 때 실행 시킨다  
						if(isMember) {
							user = u2;
						}
						out.writeBoolean(isMember);
						out.flush();
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
				//방 목록을 1회(시작시 뿌려준다)
				on = true;
				send();
				Process p  = new Process(in, roomList, user);
				p.process();
			}catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
