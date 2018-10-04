package sutta.mainclient5;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import javafx.scene.control.skin.TableColumnHeader;
import sutta.useall.Signal;

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
//	ÄÄÆ÷³ÍÆ® ¹èÄ¡¿ë °ø°£
	private ArrayList<String[]> room_list;
	private Container con = this.getContentPane();
	private String[] names = {"¹æ ¹øÈ£","¹æ ÀÌ¸§","Âü°¡ÀÚ¼ö"};
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
	

	
	

	
	private void display() {
		con.setLayout(null);
//		con.add(pane);
		con.add(join);
		con.add(addRoom);
		con.add(quick);
		con.add(label);
		con.add(panel);
		room.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.setLayout(new BorderLayout());
		panel.add(room.getTableHeader(), BorderLayout.NORTH);
		panel.add(room, BorderLayout.CENTER);
		
		label.setBounds(12, 0, 800, 25);
		panel.setBounds(12, 27, 800, 678);
		join.setBounds(825, 38, 141, 36);
		addRoom.setBounds(825, 84, 141, 36);
		quick.setBounds(825, 130, 141, 36);
		

		
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
				else if(room_list.get(index)[2].equals("4/4")) {
					System.out.println("²ËÂù ¹æ");
					out.writeInt(-1);
					out.flush();
					JOptionPane.showMessageDialog(this, "ÀÌ¹Ì ²Ë Âù ¹æÀÔ´Ï´Ù");					}
				else {
					out.writeInt(room.getSelectedRow());
					out.flush();					
				}										
			}
			else {
				JOptionPane.showMessageDialog(this, "¹æÀÌ ¾ø½À´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
				out.writeInt(-1);
				out.flush();
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * ÇØ´ç ¹öÆ°¿¡ µû¸¥ ½ÅÈ£ Àü¼Û
	 */
	private void event() {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				room.repaint();
//			}
//		});
		WindowListener proc = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//socket.close();
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
					isRoomName = Pattern.matches("^[\\d¤¡-¤¾¤¿-¤Ó°¡-ÆR]{1,8}$", name);					
				}
				if(!isRoomName) {
					name = null;
					JOptionPane.showMessageDialog(this, "¿Ã¹Ù¸£Áö ¾ÊÀº ¹æÁ¦¸ñ ÀÔ´Ï´Ù", "", JOptionPane.PLAIN_MESSAGE);
				}
				out.writeObject(name);
				out.flush();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		quick.addActionListener(e->{
			try {
				out.writeInt(Signal.QUICKJOIN);
				out.flush();
			} catch (IOException e1) {
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
	 * ¼ÒÄÏ°úÀÇ Åë·Î¸¦ »ý¼ºÇÏ°í È­¸éÀ» ¼³Á¤
	 * @param socket ¼ÒÄÏ Àü´Þ
	 * @param name ´Ð³×ÀÓ Àü´Þ
	 * @param id ¾ÆÀÌµð Àü´Þ
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
				try {
					list = (ArrayList<String[]>)in.readObject();
//					System.out.println("receive = "+list.hashCode()+" / "+list);
					if(list!=null && list.size() != 0) {
						room_list = list;
						model1.setNumRows(0);
						for(int i = 0 ; i < list.size(); i++) {
							model1.addRow(list.get(i));
							System.out.println("RowCount = "+model1.getRowCount());
						}
						tg = model2;
						model2 = model1;
						model1 = tg;
						room.repaint();
//						room.setModel(model2);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

