package sutta.mainclient6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import sutta.gameserver.Client_Ex;
import sutta.gamewindow.Mainwindow;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

/**
 * 메인 화면 창(임시)
 */
public class MainWindow extends JFrame implements Runnable{
	/**
	 * 화면 창 구현시 통로 생성, 연결
	 */
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;
	private InetAddress g_inet;
	private InetAddress w_inet;
//	컴포넌트 배치용 공간
	private ArrayList<String[]> room_list;
	private Container con = this.getContentPane();
	private String[] names = {"방 번호","방 이름","진행 상태","참가자 수"};
	private DefaultTableModel model1 =new DefaultTableModel(names,0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private DefaultTableModel model2 =new DefaultTableModel(names,0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private DefaultTableModel tg;
	private JTable room = new JTable(model2);
	private JButton join = new JButton("방참가");
	private JButton addRoom = new JButton("방 만들기");
	private JButton quick = new JButton("빠른 플레이");
	private JLabel label = new JLabel("방 목록 ");
	private JScrollPane pane = new JScrollPane(room);
	private JPanel panel = new JPanel();
	private JLabel nickname = new JLabel();
	private JLabel money = new JLabel();
	private Image moon = Toolkit.getDefaultToolkit().getImage("Moon\\moon.jpg");
	private JPanel imagePanel = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(moon, 0, 0, 467, 385, null);
		}
	};
	
	private User user;
	

	
	

	
	private void display() {
		con.setLayout(null);
//		con.add(pane);
		con.add(join);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		con.add(panel);
		con.add(nickname);
		con.add(money);
		con.setBackground(Color.black);
		con.add(imagePanel);
		con.setForeground(Color.WHITE);
		
		
		room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.setLayout(new BorderLayout());
		panel.add(room.getTableHeader(), BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);
		room.setToolTipText("더블 클릭하여 방에 참가");
		
		
		label.setBounds(12, 0, 800, 25);
		panel.setBounds(12, 27, 800, 678);
		join.setBounds(825, 38, 141, 36);
		addRoom.setBounds(825, 84, 141, 36);
		quick.setBounds(825, 130, 141, 36);
		nickname.setBounds(825, 300, 200, 25);
		money.setBounds(825, 330, 200, 25);
		nickname.setText("닉네임 : "+user.getNickname());
		money.setText("가진 돈 : "+user.getMoney()+"전");
		imagePanel.setBounds(517, 366, 467, 385);
		
		

		
	}

	/**
	 * 방 참가 메소드
	 */
	private void joinRoom() {
		try {
			out.writeInt(Signal.JOIN);
			out.flush();
			
			//리스트에서 선택 된 방의 인덱스를 얻어와 그 방의 인덱스를 전송해준다 
			int index = room.getSelectedRow();
			
			if(room_list != null) {
				if(index == -1) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "참여할 방을 선택해 주세요");
				}
				else if(room_list.get(index)[2].equals("진행중")) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "이미 진행중인 방입니다");
					}
				else {
					out.writeInt(room.getSelectedRow());
					out.flush();
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"방에 참가");
					if(r!=null) {
						//방 정보를 받아와 그 서버에 접속시켜준다
						Client_Ex client = new Client_Ex(r.getPort(), user);
						client.setDaemon(true);
						client.start();
						Mainwindow mw = new Mainwindow(client, out, this);
						client.setWindow(mw);
						mw.setVisible(true);
						this.setVisible(false);						
					}
					else {
						JOptionPane.showMessageDialog(this, "게임이 진행중인 방입니다", "", JOptionPane.PLAIN_MESSAGE);
					}
				}										
			}
			else {
				JOptionPane.showMessageDialog(this, "방이 없습니다", "", JOptionPane.PLAIN_MESSAGE);
				out.writeInt(-1);
				out.flush();
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 해당 버튼에 따른 신호 전송
	 */
	private void event() {

		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					out.writeInt(5);
					out.flush();
					socket.close();
					w_socket.close();
				} catch (Exception e1) {
					System.out.println("클라이언트쪽 소켓 에러");
					e1.printStackTrace();
				}
				System.exit(0);
			}
		};
		this.addWindowListener(proc);
		join.addActionListener(e->{
			joinRoom();
		});
		addRoom.addActionListener(e->{
			try {
				out.writeInt(Signal.ADDROOM);
				out.flush();
				//방 정보 전송
				
				String name = JOptionPane.showInputDialog(this, "방 제목 입력(한,영,숫자 1~8)", "방 만들기", JOptionPane.PLAIN_MESSAGE);
				boolean isRoomName = true;
				if(name != null) {
					isRoomName = Pattern.matches("^[\\da-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{1,8}$", name);					
				}
				if(!isRoomName) {
					name = null;
					JOptionPane.showMessageDialog(this, "올바르지 않은 방제목 입니다", "", JOptionPane.PLAIN_MESSAGE);
				}
				out.writeObject(name);
				out.flush();
				
				System.out.println("name = "+name);
				if(name!= null) {
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"방을 생성");
					//방 정보를 받아와 그 서버에 접속시켜준다
					Client_Ex client = new Client_Ex(r.getPort(), user);
					client.setDaemon(true);
					client.start();
					Mainwindow mw = new Mainwindow(client, out, this);
					System.out.println("Mainwindow mw 생성 및 시작");
					client.setWindow(mw);
					mw.setVisible(true);
					this.setVisible(false);
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(Signal.QUICKJOIN);
				out.flush();
				System.out.println(in.readObject());
				Room r = (Room)in.readObject();
				System.out.println(r.getName()+"방에 빠른 참가");
				Client_Ex client = new Client_Ex(r.getPort(), user);
				client.setDaemon(true);
				client.start();
				Mainwindow mw = new Mainwindow(client, out, this);
				client.setWindow(mw);
				mw.setVisible(true);
				this.setVisible(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		MouseListener m = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() ==2) {
					joinRoom();
				}
			}
		};
		room.addMouseListener(m);
		
		KeyListener enter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_ENTER : joinRoom();
				}
			}
		};
		room.addKeyListener(enter);
		
	}

	private void menu() {
		
	}
	
	/**
	 * 소켓과의 통로를 생성하고 화면을 설정
	 * @param socket 소켓 전달
	 * @param name 닉네임 전달
	 * @param id 아이디 전달
	 */
	private ObjectOutputStream w_out;
	private ObjectInputStream w_in;
	private Socket w_socket;
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		try {
			g_inet = InetAddress.getByName("192.168.0.9");
			w_inet = InetAddress.getByName("192.168.0.9");
			w_socket = new Socket(w_inet, 54891);
			w_out = new ObjectOutputStream(w_socket.getOutputStream());
			w_in = new ObjectInputStream(w_socket.getInputStream());
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.socket = socket;
		try {
			this.out = out;
			this.in = in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.user = (User) in.readObject();
			System.out.println("user = "+user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG섯다");
		this.setSize(1000, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t= new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * 지속적으로 방 목록을 받아오기 위해서 스레드 처리
	 */
	public void run() {
		try {
			while(true) {
				ArrayList<String[]> list;
				list = (ArrayList<String[]>)w_in.readObject();
//				System.out.println("receive = "+list.hashCode()+" / "+list);
				if(list!=null && list.size() != 0) {
					room_list = list;
					model1.setNumRows(0);
					for(int i = 0 ; i < list.size(); i++) {
						model1.addRow(list.get(i));
					}
					tg = model2;
					model2 = model1;
					model1 = tg;
					room.repaint();
//					room.setModel(model2);
				}
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}
}

