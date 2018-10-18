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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import sutta.gameserver2.Client_Ex;
import sutta.gamewindow2.Mainwindow;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

/**
 * ¸ÞÀÎ È­¸é Ã¢(ÀÓ½Ã)
 */
public class MainWindow extends JFrame implements Runnable, Signal{
	/**
	 * È­¸é Ã¢ ±¸Çö½Ã Åë·Î »ý¼º, ¿¬°á
	 */
	ObjectOutputStream out;
	ObjectInputStream in;
	Socket socket;
	private InetAddress g_inet;
	private InetAddress w_inet;
//	ÄÄÆ÷³ÍÆ® ¹èÄ¡¿ë °ø°£
	private ArrayList<String[]> room_list;
	private Container con = this.getContentPane();
	private String[] names = {"¹æ ¹øÈ£","¹æ ÀÌ¸§","ÁøÇà »óÅÂ","Âü°¡ÀÚ ¼ö"};
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
	private JButton addRoom = new JButton("¹æ ¸¸µé±â");
	private JButton quick = new JButton("ºü¸¥ ÇÃ·¹ÀÌ");
	private JLabel label = new JLabel("¹æ ¸ñ·Ï ");
	private JScrollPane pane = new JScrollPane(room);
	private JPanel panel = new JPanel();
	private JLabel nickname = new JLabel();
	private JLabel money = new JLabel();
	
	private User user;
	
	
	//°ÔÀÓ ³¡³ª°í Å¬¶óÀÌ¾ðÆ®ÀÇ ¹Ù²ï µ·À» Àç¼³Á¤
	public void moneyst(int num) {
		System.out.println("µ·¹Ù²Þ½ÇÇàµÊ : "+num);
		money.setText("°¡Áø µ· : "+num+"Àü");
		return;
	}
	

	
	private void display() {
		con.setLayout(null);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		con.add(panel);
		con.add(nickname);
		con.add(money);
		
		
		room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.setLayout(new BorderLayout());
		panel.add(room.getTableHeader(), BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);
		room.setToolTipText("´õºí Å¬¸¯ÇÏ¿© ¹æ¿¡ Âü°¡");
		
		label.setBounds(12, 0, 800, 25);
		panel.setBounds(12, 27, 800, 678);
		addRoom.setBounds(825, 38, 141, 36);
		quick.setBounds(825, 84, 141, 36);
		nickname.setBounds(825, 583, 200, 25);
		money.setBounds(825, 611, 200, 25);
		nickname.setText("´Ð³×ÀÓ : "+user.getNickname());
		money.setText("°¡Áø µ· : "+user.getMoney()+"Àü");

	}

	private void showGameRoom(Room r){
		Client_Ex client = new Client_Ex(r.getPort(), user);
		client.setDaemon(true);
		client.start();
		Mainwindow mw = new Mainwindow(client, out, this);
		client.setWindow(mw);
		mw.setVisible(true);
		this.setVisible(false);
	}
	
	/**
	 * ¹æ Âü°¡ ¸Þ¼Òµå
	 */
	private void joinRoom() {
		try {
			out.writeInt(JOIN);
			out.flush();
			
			//¸®½ºÆ®¿¡¼­ ¼±ÅÃ µÈ ¹æÀÇ ÀÎµ¦½º¸¦ ¾ò¾î¿Í ±× ¹æÀÇ ÀÎµ¦½º¸¦ Àü¼ÛÇØÁØ´Ù 
			int index = room.getSelectedRow();
			
			if(room_list != null) {
				if(index == -1) {	//¼±ÅÃµÈ ¹æÀÌ ¾øÀ» ¶§
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "Âü¿©ÇÒ ¹æÀ» ¼±ÅÃÇØ ÁÖ¼¼¿ä");
				}
				else if(room_list.get(index)[2].equals("ÁøÇàÁß")) {	//¹æÀÌ ÁøÇàÁßÀÏ ¶§
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "ÀÌ¹Ì ÁøÇàÁßÀÎ ¹æÀÔ´Ï´Ù");
					}
				else {	//¹æ¿¡ Âü°¡ 
					out.writeInt(room.getSelectedRow());
					out.flush();
					Room r = (Room)in.readObject();
					if(r!=null) {
						//¹æ Á¤º¸¸¦ ¹Þ¾Æ¿Í ±× ¼­¹ö¿¡ Á¢¼Ó½ÃÄÑÁØ´Ù
						showGameRoom(r);						
					}
					else {
						JOptionPane.showMessageDialog(this, "°ÔÀÓÀÌ ÁøÇàÁßÀÎ ¹æÀÔ´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
					}
				}										
			}
			else {
				JOptionPane.showMessageDialog(this, "¹æÀÌ ¾ø½À´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
				out.writeInt(-1);
				out.flush();
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * ÇØ´ç ¹öÆ°¿¡ µû¸¥ ½ÅÈ£ Àü¼Û
	 */
	private void event() {
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int sign = JOptionPane.showConfirmDialog(MainWindow.this, "Á¤¸»·Î Á¾·á ÇÏ½Ã°Ú½À´Ï±î?","°ÔÀÓ Á¾·á",JOptionPane.YES_NO_OPTION);
				
				if(sign == 0) {
					try {
						out.writeInt(LOGOUT);
						out.flush();
						socket.close();
						w_socket.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				}
			}
		};
		this.addWindowListener(proc);
		
		addRoom.addActionListener(e->{
			try {
				out.writeInt(ADDROOM);
				out.flush();
				//¹æ Á¤º¸ Àü¼Û
				
				String name = JOptionPane.showInputDialog(this, "¹æ Á¦¸ñ ÀÔ·Â(ÇÑ,¿µ,¼ýÀÚ 1~8)", "¹æ ¸¸µé±â", JOptionPane.PLAIN_MESSAGE);
				boolean isRoomName = true;
				if(name != null) {
					isRoomName = Pattern.matches("^[\\da-zA-Z¤¡-¤¾¤¿-¤Ó°¡-ÆR]{1,8}$", name);					
				}
				if(!isRoomName) {
					name = null;
					JOptionPane.showMessageDialog(this, "¿Ã¹Ù¸£Áö ¾ÊÀº ¹æÁ¦¸ñ ÀÔ´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
				}
				out.writeObject(name);
				out.flush();
				
				if(name!= null) {
					Room r = (Room)in.readObject();
					//¹æ Á¤º¸¸¦ ¹Þ¾Æ¿Í ±× ¼­¹ö¿¡ Á¢¼Ó½ÃÄÑÁØ´Ù
					if(r !=  null) {
						showGameRoom(r);						
					}
					else {
						JOptionPane.showMessageDialog(this, "´õÀÌ»ó ¹æÀ» ¸¸µé ¼ö ¾ø½À´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
					}
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(QUICKJOIN);
				out.flush();
				Room r = (Room)in.readObject();
				if(r !=  null) {
					showGameRoom(r);						
				}
				else {
					JOptionPane.showMessageDialog(this, "´õÀÌ»ó ¹æÀ» ¸¸µé ¼ö ¾ø½À´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		MouseListener m = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() ==2) {	//´õºíÅ¬¸¯½Ã ¹æ¿¡ Âü°¡ 
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

	/**
	 * ¼ÒÄÏ°úÀÇ Åë·Î¸¦ »ý¼ºÇÏ°í È­¸éÀ» ¼³Á¤
	 * @param socket ¼ÒÄÏ Àü´Þ
	 * @param name ´Ð³×ÀÓ Àü´Þ
	 * @param id ¾ÆÀÌµð Àü´Þ
	 */
	private ObjectOutputStream w_out;
	private ObjectInputStream w_in;
	private Socket w_socket;
	public MainWindow(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
		try {
			
			File f = new File("Address","gameserveradd.txt");
			String g_ipadd = null;
			if(f.exists()) {
				BufferedReader fin = new BufferedReader(new FileReader(f));
				
				g_ipadd = fin.readLine();
				fin.close();
				if(g_ipadd == null) {
					System.exit(0);
				}
			}
			else {
				System.exit(0);
			}
			
			File f2 = new File("Address","address.txt");
			String w_ipadd = null;
			int port = 0;
			if(f2.exists()) {
				BufferedReader fin = new BufferedReader(new FileReader(f2));
				
				w_ipadd = fin.readLine();
				port = Integer.parseInt(fin.readLine());
				fin.close();
				if(w_ipadd == null || port == 0 || port == -1) {
					System.exit(0);
				}
			}
			else {
				System.exit(0);
			}
			
			
			
			g_inet = InetAddress.getByName(g_ipadd);
			w_inet = InetAddress.getByName(w_ipadd);
			w_socket = new Socket(w_inet, port);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.display();
		this.event();
		this.setTitle("KG¼¸´Ù");
		this.setSize(1000, 800);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		Thread t= new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Áö¼ÓÀûÀ¸·Î ¹æ ¸ñ·ÏÀ» ¹Þ¾Æ¿À±â À§ÇØ¼­ ½º·¹µå Ã³¸®
	 */
	public void run() {
		try {
			while(true) {
				ArrayList<String[]> list;
				list = (ArrayList<String[]>)w_in.readObject();
				if(list!=null ) {
					room_list = list;
					model1.setNumRows(0);
					for(int i = 0 ; i < list.size(); i++) {
						model1.addRow(list.get(i));
					}
					tg = model2;
					model2 = model1;
					model1 = tg;
					room.repaint();
					money.repaint();
				}
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}
}

