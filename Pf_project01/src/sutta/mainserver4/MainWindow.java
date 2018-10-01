package sutta.mainserver4;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

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
//	컴포넌트 배치용 공간
	private ArrayList<Room> room_list;
	private Container con = this.getContentPane();
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> room = new JList<String>();
	private JButton join = new JButton("방참가");
	private JButton addRoom = new JButton("방 만들기");
	private JButton quick = new JButton("빠른 플레이");
	private JLabel label = new JLabel("방 목록");
	private JScrollPane pane = new JScrollPane(room);
	
	/**
	 * 지속적으로 방 목록을 받아오기 위해서 스레드 처리
	 */
	@Override
//	public void run() {
//		try {
//			while(true) {
//				ArrayList<Room> list = (ArrayList<Room>)in.readObject();
//				if(list!=null && list.size() != 0) {
//					System.out.println("list의 size = "+list.size());
//					room_list = list;
//					model.removeAllElements();
//					int i = 0;
//					for(Room r : room_list) {
//						model.addElement((i+1)+"번방"+r.name);
//						i++;
//					}
//					System.out.println("model = "+model);
//					room.setModel(model);
//					room.repaint();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void run() {
		
	}
	
	
	private void display() {
		con.setLayout(null);
		con.add(pane);
		con.add(join);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		
		label.setBounds(12, 0, 130, 25);
		pane.setBounds(12, 27, 900, 678);
		join.setBounds(925, 38, 141, 36);
		addRoom.setBounds(925, 84, 141, 36);
		quick.setBounds(925, 130, 141, 36);
		
		
	}

	/**
	 * 해당 버튼에 따른 신호 전송
	 */
	private void event() {
		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					socket.close();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		this.addWindowListener(proc);
		join.addActionListener(e->{
			try {
				out.writeInt(MainServer.JOIN);
				out.flush();
				//리스트에서 선택 된 방의 인덱스를 얻어와 그 방의 인덱스를 전송해준다 
				int index = room.getSelectedIndex();
				if(index == -1) {
					JOptionPane.showMessageDialog(this, "참여할 방을 선택해 주세요");
				}
				else if(room_list.get(index).cnt >=4) {
					JOptionPane.showMessageDialog(this, "이미 꽉 찬 방입니다");
				}
				else {
					out.writeInt(room.getSelectedIndex());
					out.flush();					
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		addRoom.addActionListener(e->{
			try {
				out.writeInt(MainServer.ADDROOM);
				out.flush();
				//방 정보 전송
				
				String name = JOptionPane.showInputDialog("방 제목 입력");
				out.writeObject(name);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(MainServer.QUICKJOIN);
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
	}

	private void menu() {
		
	}
	
	/**
	 * 소켓과의 통로를 생성하고 화면을 설정
	 * @param socket 소켓 전달
	 * @param name 닉네임 전달
	 * @param id 아이디 전달
	 */
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		this.socket = socket;
		try {
			this.out = out;
			this.in = in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.display();
		this.event();
		this.menu();
		this.setTitle("KG섯다");
		this.setSize(1100, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	
	
}

