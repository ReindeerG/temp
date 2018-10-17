package sutta.mainserver6;


import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import sutta.gameserver2.Server;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;


/**
 *메인 서버 클래스
 */
public class MainServer extends JFrame implements Runnable{
	/**
	 * 유저 정보 저장 리스트
	 * 클라이언트 소켓 저장 리스트
	 * 방목록 전송하기 위한 소켓 저장 리스트
	 * 서버 목록 리스트
	 * 메인 서버 소켓
	 * 방목록 전송용 서버 소켓
	 * 방 목록 저장 리스트
	 * 방 목록 전송을 위한 방 목록 이름 저장 리스트
	 * 게임 서버에 사용되고 있지 않은 포트들 저장소
	 * 백업용 파일 
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
	
	private void setRoomPortList() {
		for(int i = 53001 ; i <= 54000; i++) {
			roomPort.add(i);
		}
	}
	private void setUserList() {
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
	}
	/**
	 * 접속자(Client)를 무한대로 받아
	 * 저장소(list)에 저장하는 일을 한다
	 */
	private MainServer() {
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG섯다 서버");
		this.setSize(500, 400);
		this.setLocationByPlatform(true);
		this.setVisible(true);
		
		setRoomPortList();//게임서버에 사용될 포트들을 세팅한다
		setUserList();	//백업되어 있는 유저 정보를 불러온다
		
		try {
			m_server = new ServerSocket(54890);
//			계속해서 방 목록을 뿌려주는 서버 스레드 시작 
			Thread tt = new Thread(this);
			tt.setDaemon(true);
			tt.start();
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
	
	public void setMoney(String id, int money) {
		for(User user : user_list) {
			if(user.getId().equals(id)) {
				user.setMoney(money);
				BackUpManager.backUpUserInfo(f, user_list);
				break;
			}
		}
		return;
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
	
	/**
	 * 방 목록을 보내주는 클래스
	 */
	class Paint extends Thread{
		Socket w_socket;
		ObjectOutputStream w_out;
		ObjectInputStream w_in;
		
		/**
		 * 방 목록을 클라이언트에게 전송해주는 메소드 
		 */
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
					//2초에 한 번 씩  방 목록을 전체에게 전송- 추후 변경 가능 
					broadCast();
					Thread.sleep(1500);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * MainServer의 run()
	 * 방 목록을 뿌려주는 서버를 실행시키고
	 * 계속해서 접속자를 받는다
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
	 * 접속자 정보 관리 클래스
	 * 접속자의 아이디와 비밀번호를 확인하고 회원 가입을 진행해준다
	 * 그 후 클라이언트가 하는 행동에 따라 처리해준다(Process)
	 */
	class Client extends Thread implements Signal{
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		
		//첫 가입한 사람 정보 설정 및 유저 정보 저장소에 저장 및 백업
		public void newMember(User u1) {
			u1.setMoney(10000);//초기 돈 설정 (임의로 설정 추후 변경)
			user_list.add(u1);
			BackUpManager.backUpUserInfo(f, user_list);
		}
		
		/**
		 * 회원가입 및 아이디 비밀번호가 일치하는지 확인하는 메소드 
		 */
		public void loginProc(){
			try {
				boolean isMember = false;	//이미 회원 가입이 되어있는 아이디인지
				boolean isIng = true;		//지금 로그인 중인지
				//로그인이 완료될 때 까지 실행 
				while(!(isMember&&!isIng)) {
					//어떤 버튼을 누르느냐에 따라
					//0  회원 가입
					//1 로그인
					int num = in.readInt();
					switch(num) {
					case NEWMEMBERPROC:
						//회원가입 기입된 정보를 받아온다(u1)
						User u1 = (User)in.readObject();
						//이미 가입되어있는 아이디 혹은 닉네임인지 확인
						boolean isUser = false;
						//회원수가 0이 아닐 때 회원 가입
						if(user_list.size()>0) {
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
								newMember(u1);
							}
							out.writeBoolean(!isUser);
							out.flush();
						}
						else {
							//회원이 없을때 가입
							newMember(u1);
							out.writeBoolean(true);
							out.flush();
						}
						break;
					case LOGINPROC:
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
							out.writeInt(NOTMEMBER);
							out.flush();
						}
						else if(isIng) {
							//회원인데 로그인 중인 아이디 일때
							out.writeInt(PLAYINGMEMBER);
							out.flush();
						}
						else {
							//회원이며 로그인이 완료되었을 때
							out.writeInt(SUCCESSLOGIN);
							out.flush();
						}
						break;
					}
				}
			} catch (Exception e) {
				list.remove(this);
			}
			
		}
		
		public void logout() {
			try {
				BackUpManager.backUpUserInfo(f, user_list);//사용자 정보 백업
				list.remove(this);//접속 종료하는 사람의 소켓을 저장소에서 제거
				this.socket.close();
				this.interrupt();//스레드 종료
			} catch (Exception e) {
			}//소켓 종료
		}
		
		//Client run() : 클라이언트로부터 이벤트를 받아와 상황에 맞는 처리를 해준다
		public void run() {
			try {
				loginProc();
				//로그인 하면 저장된 정보를 사용자에게 보내준다 
				out.writeObject(user);
				out.flush();
				Process p  = new Process(this, roomList,roomPort, serverList, user_list, MainServer.this);
				p.process();
				//로그 아웃 시 process()가 중단
				logout();
			}catch(Exception e) {
				//예외 발생시 백업 후 
				logout();
			}
		}
	}
	
	private Container con = this.getContentPane();
	private JButton exit = new JButton("서버 종료");
	
	private void display() {
		con.add(exit);
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		exit.addActionListener(e->{
			for(Client cl : list) {
				try {
					cl.socket.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			for(Paint p : paint) {
				try {
					p.w_socket.close();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			try {
				w_server.close();
				m_server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		});
	}

	private void menu() {
		
	}
	
	
	
	
	public static void main(String[] args) {
		MainServer m =new MainServer();
	}
}
