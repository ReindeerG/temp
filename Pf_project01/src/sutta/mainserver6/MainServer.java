package sutta.mainserver6;


import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sutta.gameserver2.Server;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;


/**
 *메인 서버 클래스
 */
public class MainServer extends Thread{
	public static final int JOIN = 0;
	public static final int ADDROOM = 1;
	public static final int QUICKJOIN = 2;
	
	/**
	 * 클라이언트 저장소
	 * 서버소켓 저장소
	 */
	private ArrayList<User> user_list = new ArrayList<>();
	private ArrayList<Client> list = new ArrayList<>();
	private List<Paint> paint = new ArrayList<>();
	private List<Server> serverList = new ArrayList<>();
	private ServerSocket m_server;
	private ServerSocket w_server;
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<String[]> roomList2 = new ArrayList<>();
	private List<Integer> roomPort = new ArrayList<>();	
	private File f;
	
	
	/**
	 * 접속자(Client)를 무한대로 받아
	 * 저장소(list)에 저장하는 일을 한다
	 */
	private MainServer() {
		for(int i = 53001 ; i <= 54000; i++) {
			roomPort.add(i);
		}
		f = new File("UserFile","user.txt");
		if(f.exists()) {
			user_list = BackUpManager.getUserInfo(f);
			for(User u : user_list) {
				//예기치 않은 서버 다운이 일어났을 때 로그인 상태 모두 false처리
				u.setLogin(false);
			}
			System.out.println("user_list = "+user_list);
		}
		else {
			user_list = new ArrayList<>();
			System.out.println("user_list = "+user_list);
		}
		
		try {
			m_server = new ServerSocket(54890);
//			계속해서 방 목록을 뿌려주는 서버 스레드 시작 
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
	 *
	 */
	public void broadCast(){
		try {
			roomList2.clear();
			for(int i = 0; i<roomList.size(); i++) {
				Room t = roomList.get(i);
				String[] str = {(i+1)+"번방",t.getName(),t.getIng(),t.getCnt()+"/4"};
				roomList2.add(str);
			}
			for(int i=0; i<paint.size();i++) {
				paint.get(i).send();
			}				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	class Paint extends Thread{
		Socket w_socket;
		ObjectOutputStream w_out;
		ObjectInputStream w_in;
		
		public void send() {
			try {
				ArrayList<String[]> target = (ArrayList<String[]>) roomList2.clone();
				w_out.writeObject(target);
				w_out.flush();					
			}catch(Exception e) {
				e.getMessage();
			}
		}
		
		public void run() {
			while(true) {
				try {
					broadCast();
					//1초에 한 번 씩 - 추후 변경 가능 
					Thread.sleep(1500);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 계속해서 방 목록을 뿌려주는 일을 하는 메소드(스레드)
	 * MainServer run()
	 */
	public void run() {
		try {
			w_server = new ServerSocket(54891);
			while(true) {
				Paint pa = new Paint();
				pa.w_socket = w_server.accept();
				pa.w_out = new ObjectOutputStream(pa.w_socket.getOutputStream());
				pa.w_in = new ObjectInputStream(pa.w_socket.getInputStream());
				pa.setDaemon(true);
				pa.start();
				paint.add(pa);
			}			
		} catch (IOException e) {
		}
	}

	
	
	/**
	 * 접속자 정보 관리 클래스로 스레드를 상속
	 * 접속자의 아이디와 비밀번호를 확인하고 회원 가입을 진행해준다 
	 */
	class Client extends Thread{
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		boolean on = false;			//접속자의 로그인 상태
		
		/**
		 * model(방목록)을 ArrayList<Room>의 형태로 클라이언트에 전송하는 메소드
		 * @throws IOException
		 */
		public void send() {
			try {
				if(on == true) {
					ArrayList<String[]> target = (ArrayList<String[]>) roomList2.clone();
					out.writeObject(target);
					out.flush();	
				}				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 회원가입 및 아이디 비밀번호가 일치하는지 확인하는 메소드 
		 */
		public void loginProc(){
			try {
				boolean isMember = false;
				boolean isIng = true;
				//로그인이 완료될 때 까지 실행 
				while(!(isMember&&!isIng)) {
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
								BackUpManager.backUpUserInfo(f, user_list);

							}
							out.writeBoolean(!isUser);
							out.flush();
						}
						else {
							//회원이 없을때 가입
							u1.setMoney(10000);
							user_list.add(u1);
							BackUpManager.backUpUserInfo(f, user_list);
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
								if(!user.isLogin()) {
									u2 = user_list.get(i);
									isIng = false;
								}
								else {
									isIng = true;
								}
								break;
							}
						}
						
						//회원이면서 지금 실행중이지 않은 회원만 로그인 가능
						if(isMember && !isIng) {
							user = u2;
							user.setLogin(true);
						}
						
						if(isMember == false) {
							//회원이 아닐 때
							out.writeInt(0);
							out.flush();
						}
						else if(isIng) {
							//회원인데 로그인 중인 아이디 일때
							out.writeInt(1);
							out.flush();
						}
						else {
							//회원이며 로그인이 완료되었을 때
							out.writeInt(2);
							out.flush();
						}
						break;
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
				list.remove(this);
//				System.out.println("로그인 창 종료해서 클라이언트 삭제");
			}
			
		}
		
		//Client run() : 클라이언트로부터 이벤트를 받아와 상황에 맞는 처리를 해준다
		public void run() {
			try {
				loginProc();
				on = true;
				//로그인 하면 저장된 정보를 사용자에게 보내준다 
				out.writeObject(user);
				out.flush();
				Process p  = new Process(this, roomList,roomPort, serverList, user_list);
				p.process();
				BackUpManager.backUpUserInfo(f, user_list);
				System.out.println("user_list = "+ user_list);
				System.out.println("정상 로그아웃");
				list.remove(this);
				System.out.println("list.size = "+list.size());
				this.interrupt();
			}catch(Exception e) {
				System.out.println("run에러");
				System.out.println("로그아웃");
				list.remove(this);
				System.out.println("list.size = "+list.size());
				this.interrupt();
			}
		}
	}
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
