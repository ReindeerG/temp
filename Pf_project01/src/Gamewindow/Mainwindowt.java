package Gamewindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

class BG extends JPanel {
	BufferedImage image;
	
	public BG() {
		this.setLayout(null);
		try {
			image = ImageIO.read(new File("Cardimages\\Background.png"));
		} catch(Exception e) {}
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
		} catch(Exception e) {}
	}
}

class MainWindow extends JFrame {
	private Card[] cards = null;	
	public Card[] getCards() {
		return cards;
	}
	private ImageIcon[] cardimages;
	public ImageIcon[] getCardimages() {
		return cardimages;
	}
	
	// 컴포넌트 배치용 공간
	private Container con = this.getContentPane();
	
	private void display() {
		con.setLayout(null);
	}
	private void event() {
		this.setDefaultCloseOperation(/*JFrame.*/DISPOSE_ON_CLOSE);
	}
	private void menu() {
	}
	private void CreateCards() {
		cards = new Card[20];
		for(int i=0;i<20;i++) {
			cards[i] = new Card();
			cards[i].setBounds(1200/2-100/2, 760/2-156/2+40+(i*2)-40, 100, 156);
			con.add(cards[i]);
		}
	}
	private void LoadImages() {
		try {
			cardimages = new ImageIcon[20];
			for(int i=0;i<cardimages.length;i++) {
				cardimages[i] = new ImageIcon(ImageIO.read(new File("Cardimages\\Red\\"+(i+1)+".png")));
			}
		} catch(Exception e) {}
	}
	public void DrawCards() {
		for(int i=11;i>=1;i--) {
			cards[0].setBounds(1200/2-100/2 -(50/i) , 760/2-156/2-40 +(382)/i, 100, 156);
			cards[1].setBounds(1200/2-100/2 +(50/i) , 760/2-156/2+2-40 +(382-2)/i, 100, 156);
			try { Thread.sleep(100); } catch (InterruptedException e) {}
		}
	}
	
	public MainWindow() {
		this.CreateCards();
		this.LoadImages();
		this.display();
		this.event();
		this.menu();
		this.setTitle("섯다방");
		this.setSize(1200, 800);
		this.setLocationRelativeTo(this);
//		this.setLocationByPlatform(true);
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
	}
}

public class Mainwindowt {
	private static JTextField textField;
	public static void main(String[] args) {
		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {}
		MainWindow frame = new MainWindow();
		
		BG panel = new BG();
		panel.setBounds(0, 40, 1200, 760);
//		frame.getContentPane().add(panel);
		
		Card[] cards = frame.getCards();
		
//		lblNewLabel.setComponentZOrder(panel, 0);
		
//		try {
//			ImageIcon i = new ImageIcon(ImageIO.read(new File("Cardimages\\Red\\011.png")));
//			lblNewLabel.setIcon(i);
//		}catch(Exception e) {}
		
//		cards[0].setBounds(1200/2-100/2-50, 760-156+40, 100, 156);
//		cards[1].setBounds(1200/2-100/2+50, 760-156+40, 100, 156);
//		
//		cards[2].setBounds(0, 760/2-156/2+40, 100, 156);
//		cards[3].setBounds(100, 760/2-156/2+40, 100, 156);
//		
//		cards[4].setBounds(1200-100-100, 760/2-156/2+40, 100, 156);
//		cards[5].setBounds(1200-100, 760/2-156/2+40, 100, 156);
//		
//		cards[6].setBounds(1200/2-100/2-50, 40, 100, 156);
//		cards[7].setBounds(1200/2-100/2+50, 40, 100, 156);
//		
//		ImageIcon[] arr = frame.getCardimages();
//		cards[0].setIcon(arr[0]);
		
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 760-200+20, 300, 200);
		frame.getContentPane().add(textArea);
		
		textField = new JTextField();
		textField.setBounds(0, 760-200+20+200, 300, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton button1 = new JButton("준비");
		button1.setBounds(1000, 800-80, 100, 40);
		frame.getContentPane().add(button1);
		
		JButton button2 = new JButton("시작");
		button2.setBounds(1100, 800-80, 100, 40);
		button2.setEnabled(false);
		frame.getContentPane().add(button2);
		
		JButton button3 = new JButton("초대");
		button3.setBounds(1000, 800-40, 100, 40);
		frame.getContentPane().add(button3);
		
		JButton button4 = new JButton("나가기");
		button4.setBounds(1100, 800-40, 100, 40);
		frame.getContentPane().add(button4);
		
		
		JButton button5 = new JButton("콜");
		button5.setBounds(860, 640, 100, 40);
		frame.getContentPane().add(button5);
		JButton button6 = new JButton("다이");
		button6.setBounds(860, 640+40, 100, 40);
		frame.getContentPane().add(button6);
		JButton button7 = new JButton("체크");
		button7.setBounds(860, 640+80, 100, 40);
		frame.getContentPane().add(button7);
		JButton button8 = new JButton("하프");
		button8.setBounds(860, 640+120, 100, 40);
		frame.getContentPane().add(button8);
		JButton button9 = new JButton("닉네임 변경");
		button9.setBounds(300, 800-20, 100, 20);
		frame.getContentPane().add(button9);
		JLabel Label1 = new JLabel("<닉네임>");
		Label1.setBounds(0, 300, 100, 16);
		Label1.setFont(new Font(null, Font.BOLD, 16));
		Label1.setForeground(Color.WHITE);
		frame.getContentPane().add(Label1);
		JLabel Label2 = new JLabel("가진 돈: 8888만원");
		Label2.setBounds(0, 300+20, 150, 14);
		Label2.setFont(new Font(null, Font.BOLD, 14));
		Label2.setForeground(Color.WHITE);
		frame.getContentPane().add(Label2);
		JLabel Label3 = new JLabel("<내닉네임>");
		Label3.setBounds(300, 760-200+20, 100, 16);
		Label3.setFont(new Font(null, Font.BOLD, 16));
		Label3.setForeground(Color.WHITE);
		frame.getContentPane().add(Label3);
		JLabel Label4 = new JLabel("가진 돈: 8888만원");
		Label4.setBounds(300, 760-200+20+20, 150, 14);
		Label4.setFont(new Font(null, Font.BOLD, 14));
		Label4.setForeground(Color.WHITE);
		frame.getContentPane().add(Label4);
		
		JLabel Label5 = new JLabel("<닉네임>");
		Label5.setBounds(1000, 300, 100, 16);
		Label5.setFont(new Font(null, Font.BOLD, 16));
		Label5.setForeground(Color.WHITE);
		frame.getContentPane().add(Label5);
		JLabel Label6 = new JLabel("가진 돈: 8888만원");
		Label6.setBounds(1000, 300+20, 150, 14);
		Label6.setFont(new Font(null, Font.BOLD, 14));
		Label6.setForeground(Color.WHITE);
		frame.getContentPane().add(Label6);
		
		JLabel Label7 = new JLabel("<닉네임>");
		Label7.setBounds(710, 40, 100, 16);
		Label7.setFont(new Font(null, Font.BOLD, 16));
		Label7.setForeground(Color.WHITE);
		frame.getContentPane().add(Label7);
		JLabel Label8 = new JLabel("가진 돈: 8888만원");
		Label8.setBounds(710, 40+20, 150, 14);
		Label8.setFont(new Font(null, Font.BOLD, 14));
		Label8.setForeground(Color.WHITE);
		frame.getContentPane().add(Label8);
		
		
		
		JLabel Label0 = new JLabel("판돈: 8888만원");
		Label0.setBounds(550, 520, 100, 14);
		Label0.setFont(new Font(null, Font.BOLD, 14));
		Label0.setForeground(Color.WHITE);
		frame.getContentPane().add(Label0);
		
		
		
		
		
		
		frame.getContentPane().add(panel);
		
		
		frame.setVisible(true);
		
		frame.DrawCards();
	}
}


