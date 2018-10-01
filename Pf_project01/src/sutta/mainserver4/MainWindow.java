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
 * ���� ȭ�� â(�ӽ�)
 */
public class MainWindow extends JFrame implements Runnable{
	/**
	 * ȭ�� â ������ ��� ����, ����
	 */
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;
//	������Ʈ ��ġ�� ����
	private ArrayList<Room> room_list;
	private Container con = this.getContentPane();
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> room = new JList<String>();
	private JButton join = new JButton("������");
	private JButton addRoom = new JButton("�� �����");
	private JButton quick = new JButton("���� �÷���");
	private JLabel label = new JLabel("�� ���");
	private JScrollPane pane = new JScrollPane(room);
	
	/**
	 * ���������� �� ����� �޾ƿ��� ���ؼ� ������ ó��
	 */
	@Override
//	public void run() {
//		try {
//			while(true) {
//				ArrayList<Room> list = (ArrayList<Room>)in.readObject();
//				if(list!=null && list.size() != 0) {
//					System.out.println("list�� size = "+list.size());
//					room_list = list;
//					model.removeAllElements();
//					int i = 0;
//					for(Room r : room_list) {
//						model.addElement((i+1)+"����"+r.name);
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
	 * �ش� ��ư�� ���� ��ȣ ����
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
				//����Ʈ���� ���� �� ���� �ε����� ���� �� ���� �ε����� �������ش� 
				int index = room.getSelectedIndex();
				if(index == -1) {
					JOptionPane.showMessageDialog(this, "������ ���� ������ �ּ���");
				}
				else if(room_list.get(index).cnt >=4) {
					JOptionPane.showMessageDialog(this, "�̹� �� �� ���Դϴ�");
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
				//�� ���� ����
				
				String name = JOptionPane.showInputDialog("�� ���� �Է�");
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
	 * ���ϰ��� ��θ� �����ϰ� ȭ���� ����
	 * @param socket ���� ����
	 * @param name �г��� ����
	 * @param id ���̵� ����
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
		this.setTitle("KG����");
		this.setSize(1100, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	
	
}

