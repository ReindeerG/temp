package Gamewindow;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import Gameserver.Client;

public class SetWindow extends JDialog {
	private Client client;
	private Mainwindow parent;
	private ImageIcon card1;
	private ImageIcon card2;
	private ImageIcon card3;
	private String[] set;
	private JLabel[] setlbl;
	private JButton[] bt;
	private JLabel text;
	private JLabel cardlbl11;
	private JLabel cardlbl12;
	private JLabel cardlbl21;
	private JLabel cardlbl23;
	private JLabel cardlbl32;
	private JLabel cardlbl33;
	
	public SetWindow(Client client, Mainwindow parent, ImageIcon card1, ImageIcon card2, ImageIcon card3, String set1, String set2, String set3) {
		set = new String[3];
		this.client=client; this.parent=parent; this.card1=card1; this.card2=card2; this.card3=card3; this.set[0]=set1; this.set[1]=set2; this.set[2]=set3;
		
		getContentPane().setLayout(null);
		Container con = getContentPane();
		
		cardlbl11 = new JLabel(card1);
		cardlbl12 = new JLabel(card2);
		cardlbl21 = new JLabel(card1);
		cardlbl23 = new JLabel(card3);
		cardlbl32 = new JLabel(card2);
		cardlbl33 = new JLabel(card1);
		
		cardlbl11.setBounds(10, 10, 100, 156);
		cardlbl12.setBounds(10+100, 10, 100, 156);
		cardlbl21.setBounds(10+100+100+20, 10, 100, 156);
		cardlbl23.setBounds(10+100+100+20+100, 10, 100, 156);
		cardlbl32.setBounds(10+100+100+20+100+100+20, 10, 100, 156);
		cardlbl33.setBounds(10+100+100+20+100+100+20+100, 10, 100, 156);
		
		con.add(cardlbl11);
		con.add(cardlbl12);
		con.add(cardlbl21);
		con.add(cardlbl23);
		con.add(cardlbl32);
		con.add(cardlbl33);
		

		
		bt = new JButton[3];
		for(int i=0;i<3;i++) {
			bt[i] = new JButton();
			bt[i].setBorderPainted(false);
			bt[i].setFocusPainted(false);
			bt[i].setContentAreaFilled(false);
			bt[i].setOpaque(false);
			bt[i].setBounds(10+(100+100+20)*i, 10, 200, 156);
			
			con.add(bt[i]);
		}
		bt[0].addActionListener(e->{
			client.SelectSet(1, set[0]);
			client.getWindow().toemptysw();
			dispose();
		});
		bt[1].addActionListener(e->{
			client.SelectSet(2, set[1]);
			client.getWindow().toemptysw();
			dispose();
		});
		bt[2].addActionListener(e->{
			client.SelectSet(3, set[2]);
			client.getWindow().toemptysw();
			dispose();
		});
		
		Font f = new Font(null, Font.BOLD, 20);
		setlbl = new JLabel[3];
		for(int i=0;i<3;i++) {
			setlbl[i] = new JLabel(set[i]);
			setlbl[i].setFont(f);
			setlbl[i].setHorizontalAlignment(JLabel.CENTER);
			setlbl[i].setBounds(10+(100+100+20)*i, 10+156, 200, 20);
			con.add(setlbl[i]);
		}
		
		text = new JLabel("弥辆菩甫 急琶秦林技夸.");
		text.setFont(f);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setBounds(0, 10+156+20+20, 660, 20);
		con.add(text);
		
		this.setTitle("弥辆菩 急琶");
		this.setSize(660, 230);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLocationRelativeTo(parent);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		SetWindow s = new SetWindow(null, null, null, null, null, "伙迫堡动动动动动", "伙迫堡动动动动动", "伙迫堡动动动动动");
		s.setVisible(true);
	}
}
