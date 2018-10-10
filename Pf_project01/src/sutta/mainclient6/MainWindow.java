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

import sutta.gameserver2.Client_Ex;
import sutta.gamewindow2.Mainwindow;
import sutta.useall.Room;
import sutta.useall.Signal;
import sutta.useall.User;

/**
 * ¸ÞÀÎ È­¸é Ã¢(ÀÓ½Ã)
 */
public class MainWindow extends JFrame implements Runnable{
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
	private JButton join = new JButton("¹æÂü°¡");
	private JButton addRoom = new JButton("¹æ ¸¸µé±â");
	private JButton quick = new JButton("ºü¸¥ ÇÃ·¹ÀÌ");
	private JLabel label = new JLabel("¹æ ¸ñ·Ï ");
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
//		con.setBackground(Color.black);
//		con.add(imagePanel);
//		con.setForeground(Color.WHITE);
		
		
		room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.setLayout(new BorderLayout());
		panel.add(room.getTableHeader(), BorderLayout.NORTH);
		panel.add(pane, BorderLayout.CENTER);
		room.setToolTipText("´õºí Å¬¸¯ÇÏ¿© ¹æ¿¡ Âü°¡");
		
		
		label.setBounds(12, 0, 800, 25);
		panel.setBounds(12, 27, 800, 678);
		join.setBounds(825, 38, 141, 36);
		addRoom.setBounds(825, 84, 141, 36);
		quick.setBounds(825, 130, 141, 36);
		nickname.setBounds(825, 300, 200, 25);
		money.setBounds(825, 330, 200, 25);
		nickname.setText("´Ð³×ÀÓ : "+user.getNickname());
		money.setText("°¡Áø µ· : "+user.getMoney()+"Àü");
		imagePanel.setBounds(517, 366, 467, 385);
		
		

		
	}

	/**
	 * ¹æ Âü°¡ ¸Þ¼Òµå
	 */
	private void joinRoom() {
		try {
			out.writeInt(Signal.JOIN);
			out.flush();
			
			//¸®½ºÆ®¿¡¼­ ¼±ÅÃ µÈ ¹æÀÇ ÀÎµ¦½º¸¦ ¾ò¾î¿Í ±× ¹æÀÇ ÀÎµ¦½º¸¦ Àü¼ÛÇØÁØ´Ù 
			int index = room.getSelectedRow();
			
			if(room_list != null) {
				if(index == -1) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "Âü¿©ÇÒ ¹æÀ» ¼±ÅÃÇØ ÁÖ¼¼¿ä");
				}
				else if(room_list.get(index)[2].equals("ÁøÇàÁß")) {
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "ÀÌ¹Ì ÁøÇàÁßÀÎ ¹æÀÔ´Ï´Ù");
					}
				else {
					out.writeInt(room.getSelectedRow());
					out.flush();
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"¹æ¿¡ Âü°¡");
					if(r!=null) {
						//¹æ Á¤º¸¸¦ ¹Þ¾Æ¿Í ±× ¼­¹ö¿¡ Á¢¼Ó½ÃÄÑÁØ´Ù
						Client_Ex client = new Client_Ex(r.getPort(), user);
						client.setDaemon(true);
						client.start();
						Mainwindow mw = new Mainwindow(client, out, this);
						client.setWindow(mw);
						mw.setVisible(true);
						this.setVisible(false);						
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

		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					out.writeInt(6);
					out.flush();
					socket.close();
					w_socket.close();
				} catch (Exception e1) {
					System.out.println("Å¬¶óÀÌ¾ðÆ®ÂÊ ¼ÒÄÏ ¿¡·¯");
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
				
				System.out.println("name = "+name);
				if(name!= null) {
					Room r = (Room)in.readObject();
					System.out.println(r.getName()+"¹æÀ» »ý¼º");
					//¹æ Á¤º¸¸¦ ¹Þ¾Æ¿Í ±× ¼­¹ö¿¡ Á¢¼Ó½ÃÄÑÁØ´Ù
					Client_Ex client = new Client_Ex(r.getPort(), user);
					client.setDaemon(true);
					client.start();
					System.out.println("Client_Ex »ý¼º");
					Mainwindow mw = new Mainwindow(client, out, this);
					System.out.println("Mainwindow mw »ý¼º ¹× ½ÃÀÛ");
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
				System.out.println(r.getName()+"¹æ¿¡ ºü¸¥ Âü°¡");
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
			g_inet = InetAddress.getByName("localhost");
			w_inet = InetAddress.getByName("localhost");
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
//				System.out.println("receive = "+list.hashCode()+" / "+list);
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
//					room.setModel(model2);
				}
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}
}

