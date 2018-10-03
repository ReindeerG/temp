package Gamewindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import Gameserver.Client;
import Gameserver.Gaming;
import Gameserver.Player;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

import Logic.Logic;

class BG extends JPanel {
	BufferedImage image;
	
	public BG() {
		this.setLayout(null);
		try {
			image = ImageIO.read(new File("Cardimages\\Background.png"));
		} catch(Exception e) {e.printStackTrace();}
	}
	public void paint(Graphics g) {
		super.paintComponent(g);
		for(int i=0; i<1200; i+=8) {
			for(int j=0; j<760; j+=8) {
				g.drawImage(image, i, j, 8, 8, this);
			}
		}
	}
}

class Card extends JLabel {
	public Card() {
		this.setLayout(null);
		try {
			ImageIcon i = new ImageIcon(ImageIO.read(new File("Cardimages\\Red\\bk.png")));
			this.setIcon(i);
		} catch(Exception e) {e.printStackTrace();}
	}
}

class Titlebar extends JLabel {
	public Titlebar() {
		this.setLayout(null);
		try {
			ImageIcon i = new ImageIcon(ImageIO.read(new File("Images\\title.png")));
			this.setIcon(i);
		} catch(Exception e) {e.printStackTrace();}
	}
}

class Movewin implements MouseMotionListener {
	private Mainwindow frame;
	private int initx = 0;
	private int inity = 0;
	public Movewin(Mainwindow frame) {
		super();
		this.frame=frame;
	}
	public void mouseDragged(MouseEvent e) {
		frame.setLocation(frame.getX()+(e.getX()-initx), frame.getY()+(e.getY()-inity));
	}
	public void mouseMoved(MouseEvent e) {
		initx = e.getX();
		inity = e.getY();
	}
}

public class Mainwindow extends JFrame {
	private Client client=null;
	private ArrayList<Player> players;
	public void setClient(Client client) {
		this.client=client;
	}
	private boolean ban=false;
	public boolean isBan() {
		return ban;
	}
	public void setBan(boolean ban) {
		this.ban = ban;
	}
	
	private Card[] cards = null;	
	public Card[] getCards() {
		return cards;
	}
	
	private Card mycard1=null;
	private Card mycard2=null;
	private Card mycard3=null;
	private Card p2card1=null;
	private Card p2card2=null;
	private Card p2card3=null;
	private Card p3card1=null;
	private Card p3card2=null;
	private Card p3card3=null;
	private Card p4card1=null;
	private Card p4card2=null;
	private Card p4card3=null;
	
	private ActionListener alcard1 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(client.isBoolTrash()==false && client.isInggame()==true) {
				surrender();
				mycard1.setLocation(mycard1.getLocation().x, mycard1.getLocation().y-30);
				bt_inv_card1.setLocation(mycard1.getLocation().x, mycard1.getLocation().y);
				client.setTrash(1);
				Lbl_myset.setText("베팅을 마무리하세요.");
			}
		}
	};
	private ActionListener alcard2 = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(client.isBoolTrash()==false && client.isInggame()==true) {
				surrender();
				mycard2.setLocation(mycard2.getLocation().x, mycard2.getLocation().y-30);
				bt_inv_card2.setLocation(mycard2.getLocation().x, mycard2.getLocation().y);
				client.setTrash(2);
				Lbl_myset.setText("베팅을 마무리하세요.");
			}
		}
	};
	
	private int[][] cardposori=null;
	private int[][] cardpospl=null;
	
	private ImageIcon[] cardimages;
	public ImageIcon[] getCardimages() {
		return cardimages;
	}
	private Titlebar titlebar;
	private ImageIcon img_bt_ready_no;
	private ImageIcon img_bt_ready_yes;
	private ImageIcon img_bt_ready_no_roll;
	private ImageIcon img_bt_ready_yes_roll;
	private ImageIcon img_bt_start_no;
	private ImageIcon img_bt_start_yes;
	private ImageIcon img_bt_start_on;
	private ImageIcon img_bt_invite;
	private ImageIcon img_bt_invite_on;
	private ImageIcon img_bt_leave_ok;
	private ImageIcon img_bt_leave_on;
	private ImageIcon img_alarmclock;
	private ImageIcon img_bet_call;
	private ImageIcon img_bet_die;
	private ImageIcon img_bet_check;
	private ImageIcon img_bet_half;
	private ImageIcon img_bt_call_ok;
	private ImageIcon img_bt_call_no;
	private ImageIcon img_bt_call_on;
	private ImageIcon img_bt_die_ok;
	private ImageIcon img_bt_die_no;
	private ImageIcon img_bt_die_on;
	private ImageIcon img_bt_check_ok;
	private ImageIcon img_bt_check_no;
	private ImageIcon img_bt_check_on;
	private ImageIcon img_bt_half_ok;
	private ImageIcon img_bt_half_no;
	private ImageIcon img_bt_half_on;
	private ImageIcon img_bt_moneymod;
	private ImageIcon img_bt_moneymod_on;
	private ImageIcon img_win;
	private ImageIcon img_re;
	private ImageIcon img_ban;
	private Image icon;
	
	private JTextArea chatlog = new JTextArea();
	private JScrollPane scroll;
	private JTextField makechat = new JTextField();
	private JButton bt_nickch = new JButton("닉네임 변경");
	
	private JButton bt_ready = new JButton();
	private JButton bt_start = new JButton();
	private JButton bt_invite = new JButton();
	private JButton bt_exit = new JButton();
	private JButton bt_moneymod = new JButton();
	private JButton bt_bet_call = new JButton();
	private JButton bt_bet_die = new JButton();
	private JButton bt_bet_check = new JButton();
	private JButton bt_bet_half = new JButton();
	
	private JButton bt_inv_card1 = new JButton();
	private JButton bt_inv_card2 = new JButton();
	
	private JLabel Lbl_mynick = new JLabel("");
	private JLabel Lbl_mymoney = new JLabel("");
	private JLabel Lbl_mytime = new JLabel("");
	private JLabel Lbl_myset = new JLabel("");
	private JLabel Lbl_myclock = new JLabel();
	private JLabel Lbl_mybet = new JLabel();
	private JLabel Lbl_gamemoney = new JLabel("");
	private JLabel Lbl_2p_nick = new JLabel("");
	private JLabel Lbl_2p_money = new JLabel("");
	private JLabel Lbl_2p_time = new JLabel("");
	private JLabel Lbl_2p_set = new JLabel("");
	private JLabel Lbl_2p_ready = new JLabel("");
	private JLabel Lbl_2p_clock = new JLabel();
	private JLabel Lbl_2p_bet = new JLabel();
	private JButton bt_2p_ban = new JButton();
	private JLabel Lbl_3p_nick = new JLabel("");
	private JLabel Lbl_3p_money = new JLabel("");
	private JLabel Lbl_3p_time = new JLabel("");
	private JLabel Lbl_3p_set = new JLabel("");
	private JLabel Lbl_3p_ready = new JLabel("");
	private JLabel Lbl_3p_clock = new JLabel();
	private JLabel Lbl_3p_bet = new JLabel();
	private JButton bt_3p_ban = new JButton();
	private JLabel Lbl_4p_nick = new JLabel("");
	private JLabel Lbl_4p_money = new JLabel("");
	private JLabel Lbl_4p_time = new JLabel("");
	private JLabel Lbl_4p_set = new JLabel("");
	private JLabel Lbl_4p_ready = new JLabel("");
	private JLabel Lbl_4p_clock = new JLabel();
	private JLabel Lbl_4p_bet = new JLabel();
	private JButton bt_4p_ban = new JButton();
	
	private String name_2p=null;
	private String name_3p=null;
	private String name_4p=null;
	private String userid_2p=null;
	private String userid_3p=null;
	private String userid_4p=null;
	
	private Container con = this.getContentPane();
	
	private void display() {
		con.setLayout(null);
		chatlog.setBounds(0, 760-200+20, 450, 200);
		chatlog.setFont(new Font(null, Font.PLAIN, 12));
		chatlog.setLineWrap(true);
		chatlog.setEditable(false);
		scroll = new JScrollPane(chatlog);
		scroll.setBounds(0, 580, 450, 200);
//		con.add(chatlog);
		con.add(scroll);
		makechat.setBounds(0, 780, 450, 20);
		con.add(makechat);
		makechat.setColumns(10);
		bt_nickch.setBounds(0, 560, 100, 20);
		con.add(bt_nickch);
		
		bt_ready.setBounds(1000, 720, 100, 40);
		bt_ready.setIcon(img_bt_ready_no);
		bt_ready.setRolloverIcon(img_bt_ready_no_roll);
		con.add(bt_ready);
		bt_start.setBounds(1100, 720, 100, 40);
		bt_start.setIcon(img_bt_start_no);
		bt_start.setEnabled(false);
		con.add(bt_start);
		bt_invite.setBounds(1000, 760, 100, 40);
		bt_invite.setIcon(img_bt_invite);
		bt_invite.setRolloverIcon(img_bt_invite_on);
		con.add(bt_invite);
		bt_exit.setBounds(1100, 760, 100, 40);
		bt_exit.setIcon(img_bt_leave_ok);
		bt_exit.setRolloverIcon(img_bt_leave_on);
		con.add(bt_exit);
		bt_moneymod.setBounds(1000, 680, 100, 40);
		bt_moneymod.setIcon(img_bt_moneymod);
		bt_moneymod.setRolloverIcon(img_bt_moneymod_on);
		bt_moneymod.setEnabled(false);
		con.add(bt_moneymod);
		
		bt_inv_card1.setBounds(-100, -156, 100, 156);
		bt_inv_card1.setBorderPainted(false);
		bt_inv_card1.setFocusPainted(false);
		bt_inv_card1.setContentAreaFilled(false);
		bt_inv_card1.setOpaque(false);
		bt_inv_card1.addActionListener(alcard1);
		con.add(bt_inv_card1);
		bt_inv_card2.setBounds(-100, -156, 100, 156);
		bt_inv_card2.setBorderPainted(false);
		bt_inv_card2.setFocusPainted(false);
		bt_inv_card2.setContentAreaFilled(false);
		bt_inv_card2.setOpaque(false);
		bt_inv_card2.addActionListener(alcard2);
		con.add(bt_inv_card2);

		bt_bet_call.setBounds(860, 640, 100, 40);
		bt_bet_call.setIcon(img_bt_call_no);
		bt_bet_call.setEnabled(false);
		con.add(bt_bet_call);
		bt_bet_die.setBounds(860, 680, 100, 40);
		bt_bet_die.setIcon(img_bt_die_no);
		bt_bet_die.setEnabled(false);
		con.add(bt_bet_die);
		bt_bet_check.setBounds(860, 720, 100, 40);
		bt_bet_check.setIcon(img_bt_check_no);
		bt_bet_check.setEnabled(false);
		con.add(bt_bet_check);
		bt_bet_half.setBounds(860, 760, 100, 40);
		bt_bet_half.setIcon(img_bt_half_no);
		bt_bet_half.setEnabled(false);
		con.add(bt_bet_half);
		
		Lbl_mynick.setBounds(450, 760-200+20, 250, 16);
		Lbl_mynick.setFont(new Font(null, Font.BOLD, 16));
		Lbl_mynick.setForeground(Color.WHITE);
		con.add(Lbl_mynick);
		Lbl_mymoney.setBounds(450, 760-200+20+20, 150, 14);
		Lbl_mymoney.setFont(new Font(null, Font.BOLD, 14));
		Lbl_mymoney.setForeground(Color.WHITE);
		con.add(Lbl_mymoney);
		Lbl_myset.setBounds(600, 760-200+20+20, 300, 14);
		Lbl_myset.setFont(new Font(null, Font.BOLD, 14));
		Lbl_myset.setForeground(Color.WHITE);
		con.add(Lbl_myset);
		Lbl_mytime.setBounds(860, 620, 150, 14);
		Lbl_mytime.setFont(new Font(null, Font.BOLD, 14));
		Lbl_mytime.setForeground(Color.WHITE);
		con.add(Lbl_mytime);
		Lbl_myclock.setBounds(820, 595, 40, 40);
		con.add(Lbl_myclock);
		Lbl_mybet.setBounds(820-40, 595, 80, 40);
		con.add(Lbl_mybet);
		
		Lbl_gamemoney.setBounds(550, 500, 200, 14);
		Lbl_gamemoney.setFont(new Font(null, Font.BOLD, 14));
		Lbl_gamemoney.setForeground(Color.WHITE);
		con.add(Lbl_gamemoney);
		Lbl_2p_nick.setBounds(16, 280, 250, 16);
		Lbl_2p_nick.setFont(new Font(null, Font.BOLD, 16));
		Lbl_2p_nick.setForeground(Color.WHITE);
		con.add(Lbl_2p_nick);
		Lbl_2p_money.setBounds(0, 300, 150, 14);
		Lbl_2p_money.setFont(new Font(null, Font.BOLD, 14));
		Lbl_2p_money.setForeground(Color.WHITE);
		con.add(Lbl_2p_money);
		Lbl_2p_time.setBounds(0, 320, 150, 14);
		Lbl_2p_time.setFont(new Font(null, Font.BOLD, 14));
		Lbl_2p_time.setForeground(Color.WHITE);
		con.add(Lbl_2p_time);
		Lbl_2p_set.setBounds(0, 502, 150, 14);
		Lbl_2p_set.setFont(new Font(null, Font.BOLD, 14));
		Lbl_2p_set.setForeground(Color.WHITE);
		con.add(Lbl_2p_set);
		Lbl_2p_ready.setBounds(0, 260, 150, 14);
		Lbl_2p_ready.setFont(new Font(null, Font.BOLD, 14));
		Lbl_2p_ready.setForeground(Color.YELLOW);
		con.add(Lbl_2p_ready);
		Lbl_2p_clock.setBounds(105, 295, 40, 40);
		con.add(Lbl_2p_clock);
		Lbl_2p_bet.setBounds(105+40, 295, 80, 40);
		con.add(Lbl_2p_bet);
		Lbl_3p_nick.setBounds(710, 40, 250, 16);
		Lbl_3p_nick.setFont(new Font(null, Font.BOLD, 16));
		Lbl_3p_nick.setForeground(Color.WHITE);
		con.add(Lbl_3p_nick);
		Lbl_3p_money.setBounds(710, 60, 150, 14);
		Lbl_3p_money.setFont(new Font(null, Font.BOLD, 14));
		Lbl_3p_money.setForeground(Color.WHITE);
		con.add(Lbl_3p_money);
		Lbl_3p_time.setBounds(710, 80, 150, 14);
		Lbl_3p_time.setFont(new Font(null, Font.BOLD, 14));
		Lbl_3p_time.setForeground(Color.WHITE);
		con.add(Lbl_3p_time);
		Lbl_3p_set.setBounds(500, 200, 150, 14);
		Lbl_3p_set.setFont(new Font(null, Font.BOLD, 14));
		Lbl_3p_set.setForeground(Color.WHITE);
		con.add(Lbl_3p_set);
		Lbl_3p_ready.setBounds(710, 100, 150, 14);
		Lbl_3p_ready.setFont(new Font(null, Font.BOLD, 14));
		Lbl_3p_ready.setForeground(Color.YELLOW);
		con.add(Lbl_3p_ready);
		Lbl_3p_clock.setBounds(815, 55, 40, 40);
		con.add(Lbl_3p_clock);
		Lbl_3p_bet.setBounds(710, 114, 80, 40);
		con.add(Lbl_3p_bet);
		Lbl_4p_nick.setBounds(1000, 280, 250, 16);
		Lbl_4p_nick.setFont(new Font(null, Font.BOLD, 16));
		Lbl_4p_nick.setForeground(Color.WHITE);
		con.add(Lbl_4p_nick);
		Lbl_4p_money.setBounds(1000, 300, 150, 14);
		Lbl_4p_money.setFont(new Font(null, Font.BOLD, 14));
		Lbl_4p_money.setForeground(Color.WHITE);
		con.add(Lbl_4p_money);
		Lbl_4p_time.setBounds(1000, 320, 150, 14);
		Lbl_4p_time.setFont(new Font(null, Font.BOLD, 14));
		Lbl_4p_time.setForeground(Color.WHITE);
		con.add(Lbl_4p_time);
		Lbl_4p_set.setBounds(1000, 502, 150, 14);
		Lbl_4p_set.setFont(new Font(null, Font.BOLD, 14));
		Lbl_4p_set.setForeground(Color.WHITE);
		con.add(Lbl_4p_set);
		Lbl_4p_ready.setBounds(1000, 260, 150, 14);
		Lbl_4p_ready.setFont(new Font(null, Font.BOLD, 14));
		Lbl_4p_ready.setForeground(Color.YELLOW);
		con.add(Lbl_4p_ready);
		Lbl_4p_clock.setBounds(960, 295, 40, 40);
		con.add(Lbl_4p_clock);
		Lbl_4p_bet.setBounds(880, 295, 80, 40);
		con.add(Lbl_4p_bet);
		bt_2p_ban.setBounds(-16, -16, 16, 16);
		bt_2p_ban.setBorderPainted(false);
		bt_2p_ban.setFocusPainted(false);
		bt_2p_ban.setContentAreaFilled(false);
		bt_2p_ban.setIcon(img_ban);
		con.add(bt_2p_ban);
		bt_3p_ban.setBounds(-16, -16, 16, 16);
		bt_3p_ban.setBorderPainted(false);
		bt_3p_ban.setFocusPainted(false);
		bt_3p_ban.setContentAreaFilled(false);
		bt_3p_ban.setIcon(img_ban);
		con.add(bt_3p_ban);
		bt_4p_ban.setBounds(-16, -16, 16, 16);
		bt_4p_ban.setBorderPainted(false);
		bt_4p_ban.setFocusPainted(false);
		bt_4p_ban.setContentAreaFilled(false);
		bt_4p_ban.setIcon(img_ban);
		con.add(bt_4p_ban);
		
		BG panel = new BG();
		panel.setBounds(0, 40, 1200, 760);
		con.add(panel);
	}
	private void event() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		bt_2p_ban.addActionListener(e->{
			String tmp = userid_2p;
			int banresult = JOptionPane.showConfirmDialog(this, name_2p+"님을 퇴장시키겠습니까?", "강제퇴장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(banresult==0 && tmp.equals(userid_2p)) {
				client.Ban(userid_2p);
			}
		});
		bt_3p_ban.addActionListener(e->{
			String tmp = userid_3p;
			int banresult = JOptionPane.showConfirmDialog(this, name_3p+"님을 퇴장시키겠습니까?", "강제퇴장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(banresult==0 && tmp.equals(userid_3p)) {
				client.Ban(userid_3p);
			}
		});
		bt_4p_ban.addActionListener(e->{
			String tmp = userid_4p;
			int banresult = JOptionPane.showConfirmDialog(this, name_4p+"님을 퇴장시키겠습니까?", "강제퇴장", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(banresult==0 && tmp.equals(userid_4p)) {
				client.Ban(userid_4p);
			}
		});
		bt_bet_call.addActionListener(e->{
			if(Lbl_myset.getText().equals("베팅을 마무리하세요.")) {
				Lbl_myset.setText("");
			}
			client.Bet_Call();
		});
		bt_bet_die.addActionListener(e->{
			if(Lbl_myset.getText().equals("베팅을 마무리하세요.")) {
				Lbl_myset.setText("");
			}
			bt_inv_card1.setLocation(-100, -156);
			bt_inv_card2.setLocation(-100, -156);
			client.Bet_Die();
			//ResetCards();
		});
		bt_bet_check.addActionListener(e->{
			if(Lbl_myset.getText().equals("베팅을 마무리하세요.")) {
				Lbl_myset.setText("");
			}
			client.Bet_Check();
		});
		bt_bet_half.addActionListener(e->{
			if(Lbl_myset.getText().equals("베팅을 마무리하세요.")) {
				Lbl_myset.setText("");
			}
			client.Bet_Half();
		});
		bt_moneymod.addActionListener(e->{
			PandonWindow pw = new PandonWindow(client);
			pw.setVisible(true);
		});
		bt_nickch.addActionListener(e->{
			String tmpstr = JOptionPane.showInputDialog(this, "바꿀 닉네임을 입력하세요.", "닉네임 변경", JOptionPane.PLAIN_MESSAGE);
			if(tmpstr!=null) {
				if(!tmpstr.equals("")) {
					if(tmpstr.length()<9) {
						if(!tmpstr.equals(client.getMe().getNickname())) {
							client.ChangeNick(tmpstr);
						}
					} else {
						JOptionPane.showMessageDialog(this, "닉네임은 8글자 이하로 해주세요!", "글자수 초과", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		bt_ready.addActionListener(e->{
			if(client.getMe().getReady()==0) {
				bt_ready.setIcon(img_bt_ready_yes);
				bt_ready.setRolloverIcon(img_bt_ready_yes_roll);
				client.GameReady();
				bt_exit.setEnabled(false);
			} else {
				bt_ready.setIcon(img_bt_ready_no);
				bt_ready.setRolloverIcon(img_bt_ready_no_roll);
				client.GameUnready();
				bt_exit.setEnabled(true);
			}
		});
		bt_start.addActionListener(e->{
			client.GameStart();
		});
		bt_exit.addActionListener(e->{
			client.Leave();
			this.dispose();
		});
		KeyListener chatenter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER && !makechat.getText().equals("")) {
					if(makechat.getText().equals("/분배")) {
						DrawCards();
					} else if(makechat.getText().equals("/제자리")) {
						ResetCards();
					} else if(makechat.getText().equals("/카드")) {
						allOpen();
					} else if(makechat.getText().equals("/새로고침")) {
						client.callRefresh();
						System.out.println(client.getMe().getOrder());
					} else if(makechat.getText().equals("/판돈")) {
						client.callRefresh();
						System.out.println(client.getPandon());
					} else if(makechat.getText().equals("/뽑아")) {
						DrawCards2();
					}
					else {
						client.MakeChat(makechat.getText());
						makechat.setText("");
					}
				}
			}
		};
		// 채팅 글자수 20자 제한
		KeyListener chatmax = new KeyAdapter() {
			public void keyTyped(KeyEvent e) { 
				if(makechat.getText().length()>20) {
					e.consume();
				}
			}
		};
		makechat.addKeyListener(chatenter);
		makechat.addKeyListener(chatmax);
		Movewin mw = new Movewin(this);
		titlebar.addMouseMotionListener(mw);
	}
	private void menu() {
	}
	private void CreateCards() {
		cardposori = new int[2][20];
		for(int i=0;i<20;i++) {
			cardposori[0][i] = 1200/2-100/2;
			cardposori[1][i] = 40+760/2-156/2-40+(i*2);
		}
		cardpospl = new int[2][12];
		cardpospl[0][0] = 1200/2-100/2 -50;
		cardpospl[1][0] = 800-156;
		cardpospl[0][1] = 1200/2-100/2 +50;
		cardpospl[1][1] = 800-156;
		cardpospl[0][2] = 0;
		cardpospl[1][2] = 40+760/2-156/2;
		cardpospl[0][3] = 100;
		cardpospl[1][3] = 40+760/2-156/2;
		cardpospl[0][4] = 1200/2-100/2 -50;
		cardpospl[1][4] = 40;
		cardpospl[0][5] = 1200/2-100/2 +50;
		cardpospl[1][5] = 40;
		cardpospl[0][6] = 1200-200;
		cardpospl[1][6] = 40+760/2-156/2;
		cardpospl[0][7] = 1200-100;
		cardpospl[1][7] = 40+760/2-156/2;
		
		cardpospl[0][8] = 1200/2-100/2 +50+100;
		cardpospl[1][8] = 800-156;
		cardpospl[0][9] = 200;
		cardpospl[1][9] = 40+760/2-156/2;
		cardpospl[0][10] = 1200/2-100/2 -50-100;
		cardpospl[1][10] = 40;
		cardpospl[0][11] = 1200-200-100;
		cardpospl[1][11] = 40+760/2-156/2;
		
		cards = new Card[20];
		for(int i=0;i<20;i++) {
			cards[i] = new Card();
			cards[i].setBounds(cardposori[0][i], cardposori[1][i], 100, 156);
			con.add(cards[i]);
		}
	}
	private void LoadImages() {
		try {
			cardimages = new ImageIcon[21];
			for(int i=0;i<20;i++) {
				cardimages[i] = new ImageIcon(ImageIO.read(new File("Cardimages\\Red\\"+(i+1)+".png")));
			}
			cardimages[20] = new ImageIcon(ImageIO.read(new File("Cardimages\\Red\\bk.png")));
			
			img_bt_ready_no = new ImageIcon(ImageIO.read(new File("Images\\ready_no.png")));
			img_bt_ready_yes = new ImageIcon(ImageIO.read(new File("Images\\ready_yes.png")));
			img_bt_ready_no_roll = new ImageIcon(ImageIO.read(new File("Images\\ready_no_roll.png")));
			img_bt_ready_yes_roll = new ImageIcon(ImageIO.read(new File("Images\\ready_yes_roll.png")));
			img_bt_start_no = new ImageIcon(ImageIO.read(new File("Images\\start_no.png")));
			img_bt_start_yes = new ImageIcon(ImageIO.read(new File("Images\\start_ok.png")));
			img_bt_start_on = new ImageIcon(ImageIO.read(new File("Images\\start_on.png")));
			img_bt_invite = new ImageIcon(ImageIO.read(new File("Images\\invite.png")));
			img_bt_invite_on = new ImageIcon(ImageIO.read(new File("Images\\invite_on.png")));
			img_bt_leave_ok = new ImageIcon(ImageIO.read(new File("Images\\leave_ok.png")));
			img_bt_leave_on = new ImageIcon(ImageIO.read(new File("Images\\leave_on.png")));
			img_alarmclock = new ImageIcon(ImageIO.read(new File("Images\\alarmclock.png")));
			img_bet_call = new ImageIcon(ImageIO.read(new File("Images\\bet_call.png")));
			img_bet_die = new ImageIcon(ImageIO.read(new File("Images\\bet_die.png")));
			img_bet_check = new ImageIcon(ImageIO.read(new File("Images\\bet_check.png")));
			img_bet_half = new ImageIcon(ImageIO.read(new File("Images\\bet_half.png")));
			img_bt_call_ok = new ImageIcon(ImageIO.read(new File("Images\\bt_call_ok.png")));
			img_bt_call_no = new ImageIcon(ImageIO.read(new File("Images\\bt_call_no.png")));
			img_bt_call_on = new ImageIcon(ImageIO.read(new File("Images\\bt_call_on.png")));
			img_bt_die_ok = new ImageIcon(ImageIO.read(new File("Images\\bt_die_ok.png")));
			img_bt_die_no = new ImageIcon(ImageIO.read(new File("Images\\bt_die_no.png")));
			img_bt_die_on = new ImageIcon(ImageIO.read(new File("Images\\bt_die_on.png")));
			img_bt_check_ok = new ImageIcon(ImageIO.read(new File("Images\\bt_check_ok.png")));
			img_bt_check_no = new ImageIcon(ImageIO.read(new File("Images\\bt_check_no.png")));
			img_bt_check_on = new ImageIcon(ImageIO.read(new File("Images\\bt_check_on.png")));
			img_bt_half_ok = new ImageIcon(ImageIO.read(new File("Images\\bt_half_ok.png")));
			img_bt_half_no = new ImageIcon(ImageIO.read(new File("Images\\bt_half_no.png")));
			img_bt_half_on = new ImageIcon(ImageIO.read(new File("Images\\bt_half_on.png")));
			img_bt_moneymod = new ImageIcon(ImageIO.read(new File("Images\\moneymod.png")));
			img_bt_moneymod_on = new ImageIcon(ImageIO.read(new File("Images\\moneymod_on.png")));
			img_win = new ImageIcon(ImageIO.read(new File("Images\\win.png")));
			img_re = new ImageIcon(ImageIO.read(new File("Images\\re.png")));
			img_ban = new ImageIcon(ImageIO.read(new File("Images\\BAN.png")));
			icon = Toolkit.getDefaultToolkit().getImage("Images\\ico.png");
		} catch(Exception e) {e.printStackTrace();}
	}
	public void ResetCards() {
		for(int i=0;i<cards.length;i++) {
			cards[i].setIcon(cardimages[20]);
			cards[i].setBounds(cardposori[0][i], cardposori[1][i], 100, 156);
		}
	}
	public void ReceiveMsg(String userid, String msg, String time) {
		chatlog.append("["+time+"] <"+userid+"> "+msg+"\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void NickChange(String userid, String msg, String time) {
		chatlog.append("["+time+"] ("+userid+"님이 "+msg+"로 닉네임을 변경하셨습니다.)\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void ChatJoin(String userid, String time) {
		chatlog.append("["+time+"] ("+userid+"님이 입장하셨습니다.)\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void ChatLeave(String userid, String time) {
		chatlog.append("["+time+"] ("+userid+"님이 퇴장하셨습니다.)\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void ChatBan(String userid, String time) {
		chatlog.append("["+time+"] ("+userid+"님이 방장에 의해 강제퇴장 당하셨습니다.)\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void ImBanned() {
		JOptionPane.showMessageDialog(this, "방장에 의해 강제퇴장당하셨습니다.", "강제퇴장", JOptionPane.WARNING_MESSAGE);
		setBan(true);
		dispose();
		return;
	}
	public void Winmsg(String nick, String time) {
		chatlog.append("["+time+"] ("+nick+"님이 이겼습니다.)\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void Remsg(String time) {
		chatlog.append("["+time+"] (3초 후 대상자끼리 재경기가 시작됩니다.)\n");
		chatlog.setCaretPosition(chatlog.getDocument().getLength());
		return;
	}
	public void surrender() {
		mycard1.setLocation(cardpospl[0][0], cardpospl[1][0]);
		mycard2.setLocation(cardpospl[0][1], cardpospl[1][1]);
		Lbl_myset.setText("");
		return;
	}
	public void Refresh() {
		players = client.getPlayers();
		int allready=0;
		for(Player p : players) {
			if(p.getReady()==1) allready++;
			if(p.getUserid().equals(client.getUserid())) {
				client.setMe(p);
			}
		}
		if(client.isInggame()==false) {
			bt_inv_card1.setLocation(-100, -156);
			bt_inv_card2.setLocation(-100, -156);
			if(allready==0) {
				if(client.getMe().getOrder()==0) {
					bt_moneymod.setToolTipText(null);
					bt_moneymod.setEnabled(true);
				} else {
					bt_moneymod.setToolTipText("판돈은 방장만이 수정할 수 있습니다.");
					bt_moneymod.setEnabled(false);
				}
			} else {
				bt_moneymod.setToolTipText("한명이라도 판돈을 낸 상태(준비한 상태)라면 수정할 수 없습니다.");
				bt_moneymod.setEnabled(false);
			}
		} else {
			bt_moneymod.setToolTipText("게임 중에는 판돈을 수정할 수 없습니다.");
			bt_moneymod.setEnabled(false);
		}
		if(client.isInggame()==true && client.getMe().getTrash()==0 && client.getMe().getBetbool()==1 && client.getMe().getOrder()<client.getThisplaynum()) {
			surrender();
		}
		if(client.isInggame()==false && client.getMe().getMoney()<client.getPandon() && client.getMe().getReady()==0) {
			bt_ready.setEnabled(false);
			bt_ready.setToolTipText("판돈("+client.getPandon()+"전)이 부족합니다.");
		} else if (client.isInggame()==false && client.getMe().getMoney()>=client.getPandon() && client.getMe().getReady()==0) {
			bt_ready.setEnabled(true);
			bt_ready.setToolTipText(null);
		}
		if(client.getMe().getOrder()==0 && allready>1 && allready==players.size()) {
			bt_start.setIcon(img_bt_start_yes);
			bt_start.setRolloverIcon(img_bt_start_on);
			bt_start.setEnabled(true);
		} else {
			bt_start.setIcon(img_bt_start_no);
			bt_start.setRolloverIcon(img_bt_start_no);
			bt_start.setEnabled(false);
		}
		if(client.getMe().getOrder()==0) { Lbl_mynick.setText("<"+client.getMe().getNickname()+"> (방장)"); }
		else { Lbl_mynick.setText("<"+client.getMe().getNickname()+">"); }
		Lbl_mymoney.setText("가진 돈: "+client.getMe().getMoney()+"전");
		if(client.isInggame()==true) {
			switch(client.getMe().getBetbool()) {
			case 0:
				Lbl_mybet.setIcon(null); break;
			case 1:
				Lbl_mybet.setIcon(img_bet_die); break;
			case 2:
				Lbl_mybet.setIcon(img_bet_call); break;
			case 3:
				Lbl_mybet.setIcon(img_bet_half); break;
			case 4:
				Lbl_mybet.setIcon(img_bet_check); break;
			}
		} else {
			Lbl_mybet.setIcon(null);
		}
		Lbl_2p_nick.setText("");
		Lbl_2p_money.setText("");
		Lbl_2p_ready.setText("");
		Lbl_2p_bet.setIcon(null);
		Lbl_3p_nick.setText("");
		Lbl_3p_money.setText("");
		Lbl_3p_ready.setText("");
		Lbl_3p_bet.setIcon(null);
		Lbl_4p_nick.setText("");
		Lbl_4p_money.setText("");
		Lbl_4p_ready.setText("");
		Lbl_4p_bet.setIcon(null);
		
		bt_2p_ban.setLocation(-16, -16);
		bt_3p_ban.setLocation(-16, -16);
		bt_4p_ban.setLocation(-16, -16);
		
		int temporder = client.getMe().getOrder();
		for(Player p : players) {
			if((p.getOrder()%4)==((temporder+1)%4)) {
				if(client.getMe().getOrder()==0) {
					bt_2p_ban.setLocation(0, 280);
				} else {
					bt_2p_ban.setLocation(-16, -16);
				}
				if(client.isInggame()==true && p.getTrash()!=0) {
					switch(p.getTrash()) {
					case 1: p2card1.setIcon(cardimages[p.getCard1()-1]); break;
					case 2: p2card2.setIcon(cardimages[p.getCard2()-1]); break;
					default: break;
					}
				}
				if(p.getOrder()==0) { Lbl_2p_nick.setText("<"+p.getNickname()+"> (방장)"); }
				else { name_2p=p.getNickname(); userid_2p=p.getUserid(); Lbl_2p_nick.setText("<"+name_2p+">"); }
				Lbl_2p_money.setText("가진 돈: "+p.getMoney()+"전");
				if(p.getReady()==0) {
					Lbl_2p_ready.setText("");
				} else {
					Lbl_2p_ready.setText("(READY!)");
				}
				if(client.isInggame()==true) {
					switch(p.getBetbool()) {
					case 0:
						Lbl_2p_bet.setIcon(null); break;
					case 1:
						Lbl_2p_bet.setIcon(img_bet_die); break;
					case 2:
						Lbl_2p_bet.setIcon(img_bet_call); break;
					case 3:
						Lbl_2p_bet.setIcon(img_bet_half); break;
					case 4:
						Lbl_2p_bet.setIcon(img_bet_check); break;
					}
				} else {
					Lbl_2p_bet.setIcon(null);
				}
			}
			if((p.getOrder()%4)==((temporder+2)%4)) {
				if(client.getMe().getOrder()==0) {
					bt_3p_ban.setLocation(710-16, 40);
				} else {
					bt_3p_ban.setLocation(-16, -16);
				}
				if(client.isInggame()==true && p.getTrash()!=0) {
					switch(p.getTrash()) {
					case 1: p3card1.setIcon(cardimages[p.getCard1()-1]); break;
					case 2: p3card2.setIcon(cardimages[p.getCard2()-1]); break;
					default: break;
					}
				}
				if(p.getOrder()==0) { Lbl_3p_nick.setText("<"+p.getNickname()+"> (방장)"); }
				else { name_3p=p.getNickname(); userid_3p=p.getUserid(); Lbl_3p_nick.setText("<"+name_3p+">"); }
				Lbl_3p_money.setText("가진 돈: "+p.getMoney()+"전");
				if(p.getReady()==0) {
					Lbl_3p_ready.setText("");
				} else {
					Lbl_3p_ready.setText("(READY!)");
				}
				if(client.isInggame()==true) {
					switch(p.getBetbool()) {
					case 0:
						Lbl_3p_bet.setIcon(null); break;
					case 1:
						Lbl_3p_bet.setIcon(img_bet_die); break;
					case 2:
						Lbl_3p_bet.setIcon(img_bet_call); break;
					case 3:
						Lbl_3p_bet.setIcon(img_bet_half); break;
					case 4:
						Lbl_3p_bet.setIcon(img_bet_check); break;
					}
				} else {
					Lbl_3p_bet.setIcon(null);
				}
			}
			if((p.getOrder()%4)==((temporder+3)%4)) {
				if(client.getMe().getOrder()==0) {
					bt_4p_ban.setLocation(1000-16, 280);
				} else {
					bt_4p_ban.setLocation(-16, -16);
				}
				if(client.isInggame()==true && p.getTrash()!=0) {
					switch(p.getTrash()) {
					case 1: p4card1.setIcon(cardimages[p.getCard1()-1]); break;
					case 2: p4card2.setIcon(cardimages[p.getCard2()-1]); break;
					default: break;
					}
				}
				if(p.getOrder()==0) { Lbl_4p_nick.setText("<"+p.getNickname()+"> (방장)"); }
				else { name_4p=p.getNickname(); userid_4p=p.getUserid(); Lbl_4p_nick.setText("<"+name_4p+">"); }
				Lbl_4p_money.setText("가진 돈: "+p.getMoney()+"전");
				if(p.getReady()==0) {
					Lbl_4p_ready.setText("");
				} else {
					Lbl_4p_ready.setText("(READY!)");
				}
				if(client.isInggame()==true) {
					switch(p.getBetbool()) {
					case 0:
						Lbl_4p_bet.setIcon(null); break;
					case 1:
						Lbl_4p_bet.setIcon(img_bet_die); break;
					case 2:
						Lbl_4p_bet.setIcon(img_bet_call); break;
					case 3:
						Lbl_4p_bet.setIcon(img_bet_half); break;
					case 4:
						Lbl_4p_bet.setIcon(img_bet_check); break;
					}
				} else {
					Lbl_4p_bet.setIcon(null);
				}
			}
		}
	}
	public void MoneyRefresh() {
		Lbl_gamemoney.setText("총 베팅금: "+client.getMoneythisgame()+"전");
		return;
	}
	public void Timer(int who, int time) {
		players = client.getPlayers();
		if(client.isInggame()==true) {
			int me = client.getMe().getOrder();
			if(who-me==0) {
				Lbl_mytime.setText("남은 시간: "+(10-time/10)+"초");
				Lbl_2p_time.setText("");
				Lbl_3p_time.setText("");
				Lbl_4p_time.setText("");
				Lbl_myclock.setIcon(img_alarmclock);
				Lbl_2p_clock.setIcon(null);
				Lbl_3p_clock.setIcon(null);
				Lbl_4p_clock.setIcon(null);
				MyTurn();
			}
			else if(who-me==1 || who-me==-3) {
				Lbl_2p_time.setText("남은 시간: "+(10-time/10)+"초");
				Lbl_mytime.setText("");
				Lbl_3p_time.setText("");
				Lbl_4p_time.setText("");
				Lbl_2p_clock.setIcon(img_alarmclock);
				Lbl_myclock.setIcon(null);
				Lbl_3p_clock.setIcon(null);
				Lbl_4p_clock.setIcon(null);
				NotMyTurn();
			}
			else if(who-me==2 || who-me==-2) {
				Lbl_3p_time.setText("남은 시간: "+(10-time/10)+"초");
				Lbl_mytime.setText("");
				Lbl_2p_time.setText("");
				Lbl_4p_time.setText("");
				Lbl_3p_clock.setIcon(img_alarmclock);
				Lbl_myclock.setIcon(null);
				Lbl_2p_clock.setIcon(null);
				Lbl_4p_clock.setIcon(null);
				NotMyTurn();
			}
			else if(who-me==3 || who-me==-1) {
				Lbl_4p_time.setText("남은 시간: "+(10-time/10)+"초");
				Lbl_mytime.setText("");
				Lbl_2p_time.setText("");
				Lbl_3p_time.setText("");
				Lbl_4p_clock.setIcon(img_alarmclock);
				Lbl_myclock.setIcon(null);
				Lbl_2p_clock.setIcon(null);
				Lbl_3p_clock.setIcon(null);
				NotMyTurn();
			}
			for(Player p : players) {
				if(p.getGameresult()==1||p.getGameresult()==2) {
					Lbl_mytime.setText("");
					Lbl_2p_time.setText("");
					Lbl_3p_time.setText("");
					Lbl_4p_time.setText("");
					Lbl_myclock.setIcon(null);
					Lbl_2p_clock.setIcon(null);
					Lbl_3p_clock.setIcon(null);
					Lbl_4p_clock.setIcon(null);
					bt_bet_call.setIcon(img_bt_call_no);
					bt_bet_die.setIcon(img_bt_die_no);
					bt_bet_check.setIcon(img_bt_check_no);
					bt_bet_half.setIcon(img_bt_half_no);
					bt_bet_call.setEnabled(false);
					bt_bet_die.setEnabled(false);
					bt_bet_check.setEnabled(false);
					bt_bet_half.setEnabled(false);
					break;
				}
			}
		} else {
			Lbl_mytime.setText("");
			Lbl_2p_time.setText("");
			Lbl_3p_time.setText("");
			Lbl_4p_time.setText("");
			Lbl_myclock.setIcon(null);
			Lbl_2p_clock.setIcon(null);
			Lbl_3p_clock.setIcon(null);
			Lbl_4p_clock.setIcon(null);
			bt_bet_call.setIcon(img_bt_call_no);
			bt_bet_die.setIcon(img_bt_die_no);
			bt_bet_check.setIcon(img_bt_check_no);
			bt_bet_half.setIcon(img_bt_half_no);
			bt_bet_call.setEnabled(false);
			bt_bet_die.setEnabled(false);
			bt_bet_check.setEnabled(false);
			bt_bet_half.setEnabled(false);
		}
		return;
//		System.out.println(client.isInggame());
	}
	public void MyTurn() {
		if(client.isBoolTrash()==false) {
			bt_inv_card1.setLocation(mycard1.getLocation().x, mycard1.getLocation().y);
			bt_inv_card2.setLocation(mycard2.getLocation().x, mycard2.getLocation().y);
		} else {
			bt_inv_card1.setLocation(-100, -156);
			bt_inv_card2.setLocation(-100, -156);
		}
		if(client.isBoolTrash()==false) {
			Lbl_myset.setText("오픈할 패를 선택해주세요.");
		}
		bt_bet_die.setIcon(img_bt_die_ok);
		bt_bet_die.setRolloverIcon(img_bt_die_on);
		bt_bet_die.setEnabled(true);
		if(client.getTrash()==0) {
			bt_bet_call.setIcon(img_bt_call_no);
			bt_bet_call.setRolloverIcon(img_bt_call_no);
			bt_bet_call.setEnabled(false);
			bt_bet_half.setIcon(img_bt_half_no);
			bt_bet_half.setRolloverIcon(img_bt_half_no);
			bt_bet_half.setEnabled(false);
			bt_bet_check.setIcon(img_bt_check_no);
			bt_bet_check.setRolloverIcon(img_bt_check_no);
			bt_bet_check.setEnabled(false);
			bt_bet_call.setToolTipText("오픈할 패를 선택해야 베팅할 수 있습니다.");
			bt_bet_half.setToolTipText("오픈할 패를 선택해야 베팅할 수 있습니다.");
			bt_bet_check.setToolTipText("오픈할 패를 선택해야 베팅할 수 있습니다.");
		} else {
			if(client.getTurn()==1) {
				bt_bet_check.setIcon(img_bt_check_ok);
				bt_bet_check.setRolloverIcon(img_bt_check_on);
				bt_bet_check.setToolTipText(null);
				bt_bet_check.setEnabled(true);
			} else {
				bt_bet_check.setIcon(img_bt_check_no);
				bt_bet_check.setRolloverIcon(img_bt_check_no);
				bt_bet_check.setToolTipText("선인 사람의 첫턴에만 가능합니다.");
				bt_bet_check.setEnabled(false);
			}
//			if(client.getMinforbet()==0) {
//				bt_bet_call.setIcon(img_bt_call_no);
//				bt_bet_call.setRolloverIcon(img_bt_call_no);
//				bt_bet_call.setToolTipText("첫 베팅은 하프로 이루어져야 합니다.");
//				bt_bet_call.setEnabled(false);
//			} else
			if(client.getMinforbet()==0) {
				bt_bet_call.setIcon(img_bt_call_no);
				bt_bet_call.setRolloverIcon(img_bt_call_no);
				bt_bet_call.setToolTipText("첫 베팅은 하프여야 합니다.");
				bt_bet_call.setEnabled(false);
			}
			else if(client.getMe().getMoney()<client.getMinforbet()) {
				bt_bet_call.setIcon(img_bt_call_no);
				bt_bet_call.setRolloverIcon(img_bt_call_no);
				bt_bet_call.setToolTipText(client.getMinforbet()+"전의 베팅금이 필요합니다.");
				bt_bet_call.setEnabled(false);
			} else {
				bt_bet_call.setIcon(img_bt_call_ok);
				bt_bet_call.setRolloverIcon(img_bt_call_on);
				bt_bet_call.setToolTipText(client.getMinforbet()+"전 베팅");
				bt_bet_call.setEnabled(true);
			}
			if(client.isCanthalf()==true) {
				bt_bet_half.setIcon(img_bt_half_no);
				bt_bet_half.setRolloverIcon(img_bt_half_no);
				bt_bet_half.setToolTipText("콜이나 체크를 한 유저는 하프 베팅할 수 없습니다.");
				bt_bet_half.setEnabled(false);
			}
			else if(client.getMe().getMoney()<client.getMoneythisgame()/2) {
				bt_bet_half.setIcon(img_bt_half_no);
				bt_bet_half.setRolloverIcon(img_bt_half_no);
				bt_bet_half.setToolTipText(client.getMoneythisgame()/2+"전의 베팅금이 필요합니다.");
				bt_bet_half.setEnabled(false);
			} else {
				bt_bet_half.setIcon(img_bt_half_ok);
				bt_bet_half.setRolloverIcon(img_bt_half_on);
				bt_bet_half.setToolTipText(client.getMoneythisgame()/2+"전 베팅");
				bt_bet_half.setEnabled(true);
			}
		}
		return;
	}
	public void NotMyTurn() {
		bt_inv_card1.setLocation(-100, -156);
		bt_inv_card2.setLocation(-100, -156);
		
		
		if(client.isBoolTrash()==false) {
			Lbl_myset.setText("");
		}
		bt_bet_call.setIcon(img_bt_call_no);
		bt_bet_call.setRolloverIcon(img_bt_call_no);
		bt_bet_die.setIcon(img_bt_die_no);
		bt_bet_die.setRolloverIcon(img_bt_die_no);
		bt_bet_check.setIcon(img_bt_check_no);
		bt_bet_check.setRolloverIcon(img_bt_check_no);
		bt_bet_half.setIcon(img_bt_half_no);
		bt_bet_half.setRolloverIcon(img_bt_half_no);
		bt_bet_call.setEnabled(false);
		bt_bet_die.setEnabled(false);
		bt_bet_check.setEnabled(false);
		bt_bet_half.setEnabled(false);
		bt_bet_call.setToolTipText(null);
		bt_bet_half.setToolTipText(null);
	}
	public void StartToButton() {
		bt_ready.setIcon(img_bt_ready_no);
		bt_ready.setEnabled(false);
		bt_exit.setEnabled(false);
		bt_start.setIcon(img_bt_start_no);
		bt_start.setEnabled(false);
		bt_invite.setEnabled(false);
		return;
	}
	public void EndToButton() {
		bt_ready.setIcon(img_bt_ready_no);
		bt_ready.setRolloverIcon(img_bt_ready_no_roll);
		bt_ready.setEnabled(true);
		bt_exit.setEnabled(true);
		bt_invite.setEnabled(true);
		Lbl_myset.setText("");
		Lbl_2p_set.setText("");
		Lbl_3p_set.setText("");
		Lbl_4p_set.setText("");
		Lbl_mybet.setIcon(null);
		Lbl_2p_bet.setIcon(null);
		Lbl_3p_bet.setIcon(null);
		Lbl_4p_bet.setIcon(null);
		client.callRefresh();
		return;
	}
	public void allOpen() {
		players = client.getPlayers();
		int playnum = client.getThisplaynum();
		switch(playnum) {
			case 2: {
				if(client.getMe().getOrder()==0 && players.get(1).getBetbool()>1) {
					p2card1.setIcon(cardimages[players.get(1).getCard1()-1]);
					p2card2.setIcon(cardimages[players.get(1).getCard2()-1]);
					if(players.get(1).getTrash()!=0) p2card3.setIcon(cardimages[players.get(1).getCard3()-1]);
					Lbl_2p_set.setText(Logic.lastName(players.get(1).getCardset()));
				} else if(client.getMe().getOrder()==1 && players.get(0).getBetbool()>1) {
					p4card1.setIcon(cardimages[players.get(0).getCard1()-1]);
					p4card2.setIcon(cardimages[players.get(0).getCard2()-1]);
					if(players.get(0).getTrash()!=0) p4card3.setIcon(cardimages[players.get(0).getCard3()-1]);
					Lbl_4p_set.setText(Logic.lastName(players.get(0).getCardset()));
				} else if(client.getMe().getOrder()==2) {
					if(players.get(0).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(0).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(0).getCard2()-1]);
						if(players.get(0).getTrash()!=0) p3card3.setIcon(cardimages[players.get(0).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(0).getCardset()));
					}
					if(players.get(1).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p4card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
				} else if(client.getMe().getOrder()==3) {
					if(players.get(0).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(0).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(0).getCard2()-1]);
						if(players.get(0).getTrash()!=0) p2card3.setIcon(cardimages[players.get(0).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(0).getCardset()));
					}
					if(players.get(1).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p3card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
				}
				break;
			}
			case 3: {
				if(client.getMe().getOrder()==0) {
					if(players.get(1).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p2card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
					if(players.get(2).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(2).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(2).getCard2()-1]);
						if(players.get(2).getTrash()!=0) p3card3.setIcon(cardimages[players.get(2).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(2).getCardset()));
					}
				} else if(client.getMe().getOrder()==1) {
					if(players.get(0).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(0).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(0).getCard2()-1]);
						if(players.get(0).getTrash()!=0) p4card3.setIcon(cardimages[players.get(0).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(0).getCardset()));
					}
					if(players.get(2).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(2).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(2).getCard2()-1]);
						if(players.get(2).getTrash()!=0) p2card3.setIcon(cardimages[players.get(2).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(2).getCardset()));
					}
				} else if(client.getMe().getOrder()==2) {
					if(players.get(0).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(0).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(0).getCard2()-1]);
						if(players.get(0).getTrash()!=0) p3card3.setIcon(cardimages[players.get(0).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(0).getCardset()));
					}
					if(players.get(1).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p4card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
				} else if(client.getMe().getOrder()==3) {
					if(players.get(0).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(0).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(0).getCard2()-1]);
						if(players.get(0).getTrash()!=0) p2card3.setIcon(cardimages[players.get(0).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(0).getCardset()));
					}
					if(players.get(1).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p3card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
					if(players.get(2).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(2).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(2).getCard2()-1]);
						if(players.get(2).getTrash()!=0) p4card3.setIcon(cardimages[players.get(2).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(2).getCardset()));
					}
				}
				break;
			}
			case 4: {
				if(client.getMe().getOrder()==0) {
					if(players.get(1).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p2card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
					if(players.get(2).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(2).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(2).getCard2()-1]);
						if(players.get(2).getTrash()!=0) p3card3.setIcon(cardimages[players.get(2).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(2).getCardset()));
					}
					if(players.get(3).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(3).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(3).getCard2()-1]);
						if(players.get(3).getTrash()!=0) p4card3.setIcon(cardimages[players.get(3).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(3).getCardset()));
					}
				} else if(client.getMe().getOrder()==1) {
					if(players.get(0).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(0).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(0).getCard2()-1]);
						if(players.get(0).getTrash()!=0) p4card3.setIcon(cardimages[players.get(0).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(0).getCardset()));
					}
					if(players.get(2).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(2).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(2).getCard2()-1]);
						if(players.get(2).getTrash()!=0) p2card3.setIcon(cardimages[players.get(2).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(2).getCardset()));
					}
					if(players.get(3).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(3).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(3).getCard2()-1]);
						if(players.get(3).getTrash()!=0) p3card3.setIcon(cardimages[players.get(3).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(3).getCardset()));
					}
				} else if(client.getMe().getOrder()==2) {
					if(players.get(1).getBetbool()>1) {
						p4card1.setIcon(cardimages[players.get(1).getCard1()-1]);
						p4card2.setIcon(cardimages[players.get(1).getCard2()-1]);
						if(players.get(1).getTrash()!=0) p4card3.setIcon(cardimages[players.get(1).getCard3()-1]);
						Lbl_4p_set.setText(Logic.lastName(players.get(1).getCardset()));
					}
					if(players.get(3).getBetbool()>1) {
						p2card1.setIcon(cardimages[players.get(3).getCard1()-1]);
						p2card2.setIcon(cardimages[players.get(3).getCard2()-1]);
						if(players.get(3).getTrash()!=0) p2card3.setIcon(cardimages[players.get(3).getCard3()-1]);
						Lbl_2p_set.setText(Logic.lastName(players.get(3).getCardset()));
					}
					if(players.get(4).getBetbool()>1) {
						p3card1.setIcon(cardimages[players.get(4).getCard1()-1]);
						p3card2.setIcon(cardimages[players.get(4).getCard2()-1]);
						if(players.get(4).getTrash()!=0) p3card3.setIcon(cardimages[players.get(4).getCard3()-1]);
						Lbl_3p_set.setText(Logic.lastName(players.get(4).getCardset()));
					}
				}
				break;
			}
		}
		return;
	}
	public void Resultgame() {
		bt_inv_card1.setLocation(-100, -156);
		bt_inv_card2.setLocation(-100, -156);
		Lbl_myclock.setIcon(null);
		Lbl_mytime.setText("");
		Lbl_2p_clock.setIcon(null);
		Lbl_2p_time.setText("");
		Lbl_3p_clock.setIcon(null);
		Lbl_3p_time.setText("");
		Lbl_4p_clock.setIcon(null);
		Lbl_4p_time.setText("");
		bt_bet_call.setEnabled(false);
		bt_bet_die.setEnabled(false);
		bt_bet_check.setEnabled(false);
		bt_bet_half.setEnabled(false);
		client.setCanthalf(false);
		allOpen();
		players = client.getPlayers();
		if(client.getMe().getOrder()==0) {
			switch(players.get(0).getGameresult()) {
				case 0: Lbl_mybet.setIcon(null); break;
				case 1: Lbl_mybet.setIcon(img_win); break;
				case 2: Lbl_mybet.setIcon(img_re); break;
				default: Lbl_mybet.setIcon(null); break;
			}
			switch(players.get(1).getGameresult()) {
				case 0: Lbl_2p_bet.setIcon(null); break;
				case 1: Lbl_2p_bet.setIcon(img_win); break;
				case 2: Lbl_2p_bet.setIcon(img_re); break;
				default: Lbl_2p_bet.setIcon(null); break;
			}
			if(client.getThisplaynum()>2) {
				switch(players.get(2).getGameresult()) {
					case 0: Lbl_3p_bet.setIcon(null); break;
					case 1: Lbl_3p_bet.setIcon(img_win); break;
					case 2: Lbl_3p_bet.setIcon(img_re); break;
					default: Lbl_3p_bet.setIcon(null); break;
				}
			}
			if(client.getThisplaynum()>3) {
				switch(players.get(3).getGameresult()) {
					case 0: Lbl_4p_bet.setIcon(null); break;
					case 1: Lbl_4p_bet.setIcon(img_win); break;
					case 2: Lbl_4p_bet.setIcon(img_re); break;
					default: Lbl_4p_bet.setIcon(null); break;
				}
			}
		}
		else if(client.getMe().getOrder()==1) {
			switch(players.get(1).getGameresult()) {
				case 0: Lbl_mybet.setIcon(null); break;
				case 1: Lbl_mybet.setIcon(img_win); break;
				case 2: Lbl_mybet.setIcon(img_re); break;
				default: Lbl_mybet.setIcon(null); break;
			}
			switch(players.get(0).getGameresult()) {
				case 0: Lbl_4p_bet.setIcon(null); break;
				case 1: Lbl_4p_bet.setIcon(img_win); break;
				case 2: Lbl_4p_bet.setIcon(img_re); break;
				default: Lbl_4p_bet.setIcon(null); break;
			}
			if(client.getThisplaynum()>2) {
				switch(players.get(2).getGameresult()) {
					case 0: Lbl_2p_bet.setIcon(null); break;
					case 1: Lbl_2p_bet.setIcon(img_win); break;
					case 2: Lbl_2p_bet.setIcon(img_re); break;
					default: Lbl_2p_bet.setIcon(null); break;
				}
			}
			if(client.getThisplaynum()>3) {
				switch(players.get(3).getGameresult()) {
					case 0: Lbl_3p_bet.setIcon(null); break;
					case 1: Lbl_3p_bet.setIcon(img_win); break;
					case 2: Lbl_3p_bet.setIcon(img_re); break;
					default: Lbl_3p_bet.setIcon(null); break;
				}
			}
		}
		else if(client.getMe().getOrder()==2) {
			switch(players.get(2).getGameresult()) {
				case 0: Lbl_mybet.setIcon(null); break;
				case 1: Lbl_mybet.setIcon(img_win); break;
				case 2: Lbl_mybet.setIcon(img_re); break;
				default: Lbl_mybet.setIcon(null); break;
			}
			switch(players.get(1).getGameresult()) {
				case 0: Lbl_4p_bet.setIcon(null); break;
				case 1: Lbl_4p_bet.setIcon(img_win); break;
				case 2: Lbl_4p_bet.setIcon(img_re); break;
				default: Lbl_4p_bet.setIcon(null); break;
			}
			switch(players.get(0).getGameresult()) {
				case 0: Lbl_3p_bet.setIcon(null); break;
				case 1: Lbl_3p_bet.setIcon(img_win); break;
				case 2: Lbl_3p_bet.setIcon(img_re); break;
				default: Lbl_3p_bet.setIcon(null); break;
			}
			if(client.getThisplaynum()>3) {
				switch(players.get(3).getGameresult()) {
					case 0: Lbl_2p_bet.setIcon(null); break;
					case 1: Lbl_2p_bet.setIcon(img_win); break;
					case 2: Lbl_2p_bet.setIcon(img_re); break;
					default: Lbl_2p_bet.setIcon(null); break;
				}
			}
		}
		else if(client.getMe().getOrder()==3) {
			switch(players.get(3).getGameresult()) {
				case 0: Lbl_mybet.setIcon(null); break;
				case 1: Lbl_mybet.setIcon(img_win); break;
				case 2: Lbl_mybet.setIcon(img_re); break;
				default: Lbl_mybet.setIcon(null); break;
			}
			switch(players.get(2).getGameresult()) {
				case 0: Lbl_4p_bet.setIcon(null); break;
				case 1: Lbl_4p_bet.setIcon(img_win); break;
				case 2: Lbl_4p_bet.setIcon(img_re); break;
				default: Lbl_4p_bet.setIcon(null); break;
			}
			switch(players.get(1).getGameresult()) {
				case 0: Lbl_3p_bet.setIcon(null); break;
				case 1: Lbl_3p_bet.setIcon(img_win); break;
				case 2: Lbl_3p_bet.setIcon(img_re); break;
				default: Lbl_3p_bet.setIcon(null); break;
			}
			switch(players.get(0).getGameresult()) {
				case 0: Lbl_2p_bet.setIcon(null); break;
				case 1: Lbl_2p_bet.setIcon(img_win); break;
				case 2: Lbl_2p_bet.setIcon(img_re); break;
				default: Lbl_2p_bet.setIcon(null); break;
			}
		}
		boolean isend=false;
		for(Player p : players) {
			if(p.getGameresult()==2) { isend=true; break; }
		}
		if(isend==true) {
			to3secre();
		} else {
			to5sec();
		}
		return;
	}
	public void to5sec() {
		Lbl_gamemoney.setText("게임종료");
		return;
	}
	public void to3secre() {
		Lbl_gamemoney.setText("3초 후 재경기");
//		if(client.getMe().getGameresult()!=2) {
//			bt_exit.setEnabled(true);
//		}
		return;
	}
	public void fornextgame() {
		Lbl_gamemoney.setText("");
		EndToButton();
		return;
	}
	public void Draw_my_1() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(mycard1)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			mycard1.setBounds(cardposori[0][index]+(cardpospl[0][0]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][0]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_my_2() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(mycard2)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			mycard2.setBounds(cardposori[0][index]+(cardpospl[0][1]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][1]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_my_3() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(mycard3)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			mycard3.setBounds(cardposori[0][index]+(cardpospl[0][8]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][8]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_2p_1() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p2card1)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p2card1.setBounds(cardposori[0][index]+(cardpospl[0][2]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][2]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_2p_2() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p2card2)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p2card2.setBounds(cardposori[0][index]+(cardpospl[0][3]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][3]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_2p_3() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p2card3)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p2card3.setBounds(cardposori[0][index]+(cardpospl[0][9]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][9]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_3p_1() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p3card1)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p3card1.setBounds(cardposori[0][index]+(cardpospl[0][4]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][4]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_3p_2() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p3card2)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p3card2.setBounds(cardposori[0][index]+(cardpospl[0][5]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][5]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_3p_3() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p3card3)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p3card3.setBounds(cardposori[0][index]+(cardpospl[0][10]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][10]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_4p_1() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p4card1)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p4card1.setBounds(cardposori[0][index]+(cardpospl[0][6]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][6]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_4p_2() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p4card2)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p4card2.setBounds(cardposori[0][index]+(cardpospl[0][7]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][7]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	public void Draw_4p_3() {
		int index=0;
		for(Card c : cards) {
			if(c.equals(p4card3)) break;
			index++;
		}
		for(int i=1;i<=10;i++) {
			p4card3.setBounds(cardposori[0][index]+(cardpospl[0][11]-cardposori[0][index])*i/10, cardposori[1][index]+(cardpospl[1][11]-cardposori[1][index])*i/10, 100, 156);
			try { Thread.sleep(20); } catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}
	
	public void DrawCards() {
		players = client.getPlayers();
		int temporder = client.getMe().getOrder();
		switch(client.getThisplaynum()) {
			case 2: {
				for(Player p : players) {
					if(p.getOrder()==client.getWhosturn()) {
						if(p.getOrder()==temporder) {
							mycard1=cards[0];
							p2card1=cards[1];
							mycard2=cards[2];
							p2card2=cards[3];
							mycard3=cards[4];
							p2card3=cards[5];
							Draw_my_1();
							Draw_2p_1();
							Draw_my_2();
							Draw_2p_2();
						} else if(p.getOrder()==(temporder-1)) {
							p4card1=cards[0];
							mycard1=cards[1];
							p4card2=cards[2];
							mycard2=cards[3];
							p4card3=cards[4];
							mycard3=cards[5];
							Draw_4p_1();
							Draw_my_1();
							Draw_4p_2();
							Draw_my_2();
						} else if(players.size()==3 && p.getOrder()==(temporder-2)) {
							p3card1=cards[0];
							p4card1=cards[1];
							p3card2=cards[2];
							p4card2=cards[3];
							p3card3=cards[4];
							p4card3=cards[5];
							Draw_3p_1();
							Draw_4p_1();
							Draw_3p_2();
							Draw_4p_2();
						} else if(players.size()==3 && p.getOrder()==(temporder-3)) {
							p2card1=cards[0];
							p3card1=cards[1];
							p2card2=cards[2];
							p3card2=cards[3];
							p2card3=cards[4];
							p3card3=cards[5];
							Draw_2p_1();
							Draw_3p_1();
							Draw_2p_2();
							Draw_3p_2();
						}
						break;
					}
				}
				break;
			}
			case 3: {
				for(Player p : players) {
					if(p.getOrder()==client.getWhosturn()) {
						if(p.getOrder()==temporder) {
							mycard1=cards[0];
							p2card1=cards[1];
							p3card1=cards[2];
							mycard2=cards[3];
							p2card2=cards[4];
							p3card2=cards[5];
							Draw_my_1();
							Draw_2p_1();
							Draw_3p_1();
							Draw_my_2();
							Draw_2p_2();
							Draw_3p_2();
						}
						else if(p.getOrder()==(temporder-1)) {
							p4card1=cards[0];
							mycard1=cards[1];
							p2card1=cards[2];
							p4card2=cards[3];
							mycard2=cards[4];
							p2card2=cards[5];
							Draw_4p_1();
							Draw_my_1();
							Draw_2p_1();
							Draw_4p_2();
							Draw_my_2();
							Draw_2p_2();
						}
						else if(p.getOrder()==(temporder-2)) {
							p3card1=cards[0];
							p4card1=cards[1];
							mycard1=cards[2];
							p3card2=cards[3];
							p4card2=cards[4];
							mycard2=cards[5];
							Draw_3p_1();
							Draw_4p_1();
							Draw_my_1();
							Draw_3p_2();
							Draw_4p_2();
							Draw_my_2();
						}
						else if(players.size()==4 && p.getOrder()==(temporder-3)) {
							p2card1=cards[0];
							p3card1=cards[1];
							p4card1=cards[2];
							p2card2=cards[3];
							p3card2=cards[4];
							p4card2=cards[5];
							Draw_2p_1();
							Draw_3p_1();
							Draw_4p_1();
							Draw_2p_2();
							Draw_3p_2();
							Draw_4p_2();
						}
						break;
					}
				}
				break;
			}
			case 4: {
				for(Player p : players) {
					if(p.getOrder()==client.getWhosturn()) {
						if(p.getOrder()==temporder) {
							mycard1=cards[0];
							p2card1=cards[1];
							p3card1=cards[2];
							p4card1=cards[3];
							mycard2=cards[4];
							p2card2=cards[5];
							p3card2=cards[6];
							p4card2=cards[7];
							Draw_my_1();
							Draw_2p_1();
							Draw_3p_1();
							Draw_4p_1();
							Draw_my_2();
							Draw_2p_2();
							Draw_3p_2();
							Draw_4p_2();
						}
						else if(p.getOrder()==(temporder-1)) {
							p4card1=cards[0];
							mycard1=cards[1];
							p2card1=cards[2];
							p3card1=cards[3];
							p4card2=cards[4];
							mycard2=cards[5];
							p2card2=cards[6];
							p3card2=cards[7];
							Draw_4p_1();
							Draw_my_1();
							Draw_2p_1();
							Draw_3p_1();
							Draw_4p_2();
							Draw_my_2();
							Draw_2p_2();
							Draw_3p_2();
						}
						else if(p.getOrder()==(temporder-2)) {
							p3card1=cards[0];
							p4card1=cards[1];
							mycard1=cards[2];
							p2card1=cards[3];
							p3card2=cards[4];
							p4card2=cards[5];
							mycard2=cards[6];
							p2card2=cards[7];
							Draw_3p_1();
							Draw_4p_1();
							Draw_my_1();
							Draw_2p_1();
							Draw_3p_2();
							Draw_4p_2();
							Draw_my_2();
							Draw_2p_2();
						}
						else if(p.getOrder()==(temporder-3)) {
							p2card1=cards[0];
							p3card1=cards[1];
							p4card1=cards[2];
							mycard1=cards[3];
							p2card2=cards[4];
							p3card2=cards[5];
							p4card2=cards[6];
							mycard2=cards[7];
							Draw_2p_1();
							Draw_3p_1();
							Draw_4p_1();
							Draw_my_1();
							Draw_2p_2();
							Draw_3p_2();
							Draw_4p_2();
							Draw_my_2();
						}
						break;
					}
				}
				break;
			}
		}
		mycard1.setIcon(cardimages[client.getCard1()-1]);
		mycard2.setIcon(cardimages[client.getCard2()-1]);
		client.setBoolTrash(false);
		
	}
	public void DrawCards2() {
		NotMyTurn();
		Lbl_myclock.setIcon(null);
		Lbl_2p_clock.setIcon(null);
		Lbl_3p_clock.setIcon(null);
		Lbl_4p_clock.setIcon(null);
		Lbl_mytime.setText("");
		Lbl_2p_time.setText("");
		Lbl_3p_time.setText("");
		Lbl_4p_time.setText("");
		Lbl_mybet.setIcon(null);
		Lbl_2p_bet.setIcon(null);
		Lbl_3p_bet.setIcon(null);
		Lbl_4p_bet.setIcon(null);
		
		
		
		
		players = client.getPlayers();
		int temporder = client.getMe().getOrder();
		switch(client.getThisplaynum()) {
			case 2: {
				switch(client.getMe().getOrder()) {
					case 0: {
						Draw_my_3();
						Draw_2p_3();
						break;
					}
					case 1: {
						Draw_4p_3();
						Draw_my_3();
						break;
					}
					case 2: {
						Draw_3p_3();
						Draw_4p_3();
						break;
					}
					case 3: {
						Draw_2p_3();
						Draw_3p_3();
						break;
					}
					default: break;
				}
				break;
			}
			case 3: {
				switch(client.getMe().getOrder()) {
					case 0: {
						if(players.get(0).getBetbool()!=1) {
							mycard3=cards[6];
							Draw_my_3();
							if(players.get(1).getBetbool()!=1) {
								p2card3=cards[7];
								Draw_2p_3();
								if(players.get(2).getBetbool()!=1) {
									p3card3=cards[8];
									Draw_3p_3();
								}
							} else {
								p3card3=cards[7];
								Draw_3p_3();
							}
						} else {
							p2card3=cards[6];
							p3card3=cards[7];
							Draw_2p_3();
							Draw_3p_3();
						}
						break;
					}
					case 1: {
						if(players.get(0).getBetbool()!=1) {
							p4card3=cards[6];
							Draw_4p_3();
							if(players.get(1).getBetbool()!=1) {
								mycard3=cards[7];
								Draw_my_3();
								if(players.get(2).getBetbool()!=1) {
									p2card3=cards[8];
									Draw_2p_3();
								}
							} else {
								mycard3=cards[7];
								Draw_2p_3();
							}
						} else {
							mycard3=cards[6];
							p2card3=cards[7];
							Draw_my_3();
							Draw_2p_3();
						}
						break;
					}
					case 2: {
						if(players.get(0).getBetbool()!=1) {
							p3card3=cards[6];
							Draw_3p_3();
							if(players.get(1).getBetbool()!=1) {
								p4card3=cards[7];
								Draw_4p_3();
								if(players.get(2).getBetbool()!=1) {
									mycard3=cards[8];
									Draw_my_3();
								}
							} else {
								mycard3=cards[7];
								Draw_my_3();
							}
						} else {
							p4card3=cards[6];
							mycard3=cards[7];
							Draw_4p_3();
							Draw_my_3();
						}
						break;
					}
					case 3: {
						if(players.get(0).getBetbool()!=1) {
							p2card3=cards[6];
							Draw_2p_3();
							if(players.get(1).getBetbool()!=1) {
								p3card3=cards[7];
								Draw_3p_3();
								if(players.get(2).getBetbool()!=1) {
									p4card3=cards[8];
									Draw_4p_3();
								}
							} else {
								p4card3=cards[7];
								Draw_4p_3();
							}
						} else {
							p3card3=cards[6];
							p4card3=cards[7];
							Draw_3p_3();
							Draw_4p_3();
						}
						break;
					}
					default: break;
				}
				break;
			}
			case 4: {
				switch(client.getMe().getOrder()) {
					case 0: {
						if(players.get(0).getBetbool()!=1) {
							mycard3=cards[8];
							Draw_my_3();
							if(players.get(1).getBetbool()!=1) {
								p2card3=cards[9];
								Draw_2p_3();
								if(players.get(2).getBetbool()!=1) {
									p3card3=cards[10];
									Draw_3p_3();
									if(players.get(3).getBetbool()!=1) {
										p4card3=cards[11];
										Draw_4p_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										p4card3=cards[10];
										Draw_4p_3();
									}
								}
							} else {
								if(players.get(2).getBetbool()!=1) {
									p3card3=cards[9];
									Draw_3p_3();
									if(players.get(3).getBetbool()!=1) {
										p4card3=cards[10];
										Draw_4p_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										p4card3=cards[9];
										Draw_4p_3();
									}
								}
							}
						} else {
							if(players.get(1).getBetbool()!=1) {
								p2card3=cards[8];
								Draw_2p_3();
								if(players.get(2).getBetbool()!=1) {
									p3card3=cards[9];
									Draw_3p_3();
									if(players.get(3).getBetbool()!=1) {
										p4card3=cards[10];
										Draw_4p_3();
									}
								} else {
									p4card3=cards[9];
									Draw_4p_3();
								}
								
							} else {
								p3card3=cards[8];
								p4card3=cards[9];
								Draw_3p_3();
								Draw_4p_3();
							}
						}
						break;
					}
					case 1: {
						if(players.get(0).getBetbool()!=1) {
							p4card3=cards[8];						// 첫
							Draw_4p_3();
							if(players.get(1).getBetbool()!=1) {
								mycard3=cards[9];					// 둘
								Draw_my_3();
								if(players.get(2).getBetbool()!=1) {
									p2card3=cards[10];				// 셋
									Draw_2p_3();
									if(players.get(3).getBetbool()!=1) {
										p3card3=cards[11];			// 넷
										Draw_3p_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										p3card3=cards[10];			// 넷
										Draw_3p_3();
									}
								}
							} else {
								if(players.get(2).getBetbool()!=1) {
									p2card3=cards[9];							// 셋
									Draw_2p_3();
									if(players.get(3).getBetbool()!=1) {
										p3card3=cards[10];						// 넷
										Draw_3p_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										p3card3=cards[9];						// 넷
										Draw_3p_3();
									}
								}
							}
						} else {
							if(players.get(1).getBetbool()!=1) {
								mycard3=cards[8];								// 둘
								Draw_my_3();
								if(players.get(2).getBetbool()!=1) {
									p2card3=cards[9];							// 셋
									Draw_2p_3();
									if(players.get(3).getBetbool()!=1) {
										p3card3=cards[10];						//넷
										Draw_3p_3();
									}
								} else {
									p3card3=cards[9];							//넷
									Draw_3p_3();
								}
								
							} else {
								p2card3=cards[8];								//셋
								p3card3=cards[9];								//넷
								Draw_2p_3();
								Draw_3p_3();
							}
						}
						break;
					}
					case 2: {
						if(players.get(0).getBetbool()!=1) {
							p3card3=cards[8];						// 첫
							Draw_3p_3();
							if(players.get(1).getBetbool()!=1) {
								p4card3=cards[9];					// 둘
								Draw_4p_3();
								if(players.get(2).getBetbool()!=1) {
									mycard3=cards[10];				// 셋
									Draw_my_3();
									if(players.get(3).getBetbool()!=1) {
										p2card3=cards[11];			// 넷
										Draw_2p_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										p2card3=cards[10];			// 넷
										Draw_2p_3();
									}
								}
							} else {
								if(players.get(2).getBetbool()!=1) {
									mycard3=cards[9];							// 셋
									Draw_my_3();
									if(players.get(3).getBetbool()!=1) {
										p2card3=cards[10];						// 넷
										Draw_2p_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										p2card3=cards[9];						// 넷
										Draw_2p_3();
									}
								}
							}
						} else {
							if(players.get(1).getBetbool()!=1) {
								p4card3=cards[8];								// 둘
								Draw_4p_3();
								if(players.get(2).getBetbool()!=1) {
									mycard3=cards[9];							// 셋
									Draw_my_3();
									if(players.get(3).getBetbool()!=1) {
										p2card3=cards[10];						//넷
										Draw_2p_3();
									}
								} else {
									p2card3=cards[9];							//넷
									Draw_2p_3();
								}
								
							} else {
								mycard3=cards[8];								//셋
								p2card3=cards[9];								//넷
								Draw_my_3();
								Draw_2p_3();
							}
						}
						break;
					}
					case 3: {
						
						if(players.get(0).getBetbool()!=1) {
							p2card3=cards[8];						// 첫
							Draw_2p_3();
							if(players.get(1).getBetbool()!=1) {
								p3card3=cards[9];					// 둘
								Draw_3p_3();
								if(players.get(2).getBetbool()!=1) {
									p4card3=cards[10];				// 셋
									Draw_4p_3();
									if(players.get(3).getBetbool()!=1) {
										mycard3=cards[11];			// 넷
										Draw_my_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										mycard3=cards[10];			// 넷
										Draw_my_3();
									}
								}
							} else {
								if(players.get(2).getBetbool()!=1) {
									p4card3=cards[9];							// 셋
									Draw_4p_3();
									if(players.get(3).getBetbool()!=1) {
										mycard3=cards[10];						// 넷
										Draw_my_3();
									}
								} else {
									if(players.get(3).getBetbool()!=1) {
										mycard3=cards[9];						// 넷
										Draw_my_3();
									}
								}
							}
						} else {
							if(players.get(1).getBetbool()!=1) {
								p3card3=cards[8];								// 둘
								Draw_3p_3();
								if(players.get(2).getBetbool()!=1) {
									p4card3=cards[9];							// 셋
									Draw_4p_3();
									if(players.get(3).getBetbool()!=1) {
										mycard3=cards[10];						//넷
										Draw_my_3();
									}
								} else {
									mycard3=cards[9];							//넷
									Draw_my_3();
								}
								
							} else {
								p4card3=cards[8];								//셋
								mycard3=cards[9];								//넷
								Draw_4p_3();
								Draw_my_3();
							}
						}
						break;
					}
					default: break;
				}
				break;
			}
		}
		mycard3.setIcon(cardimages[client.getMe().getCard3()-1]);
		Lbl_myset.setText(Logic.lastName(client.getMe().getCardset()));
	}
	
	
//	public void MycardOpen(int card1, int card2) {
//		cards[0].setIcon(cardimages[card1-1]);
//		cards[4].setIcon(cardimages[card2-1]);
//	}
	public Mainwindow(Client client) {
		this.client=client;
		titlebar = new Titlebar();
		titlebar.setBounds(0, 0, 1200, 40);
		con.add(titlebar);
		this.CreateCards();
		this.LoadImages();
		this.display();
		this.event();
		this.menu();
		this.setTitle("섯다");
		this.setIconImage(icon);
		this.setSize(1200, 800);
		this.setLocationRelativeTo(this);
		this.setUndecorated(true);
		this.setResizable(false);
//		client.callRefresh();
		client.StartMyJoin();
	}
}
