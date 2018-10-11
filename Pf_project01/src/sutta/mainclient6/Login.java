package sutta.mainclient6;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sutta.useall.Signal;
import sutta.useall.User;



public class Login extends JDialog  implements Signal{
//	컴포넌트 배치용 공간
	private Container con = this.getContentPane();
	private JTextField id = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JLabel ID = new JLabel("아이디 :");
	private JLabel PW = new JLabel("비밀번호 :");
	private JLabel KG = new JLabel("KG섯다 로그인");
	private JButton ok = new JButton("확인");
	private JButton cancel = new JButton("취소");
	private JButton member = new JButton("회원가입");
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int login;
	private User user;
	
	public int isLogin() throws IOException {
		return login;
	}
	public String getId() {
		return id.getText();
	}
	public char[] getPw() {
		return pw.getPassword();
	}
	
	private void display() {
		con.setLayout(null);
		con.add(id);
		con.add(pw);
		con.add(ok);
		con.add(cancel);
		con.add(member);
		con.add(ID);
		con.add(PW);
		con.add(KG);
		
		KG.setBounds(57, 10, 93, 23);
		id.setBounds(80, 54, 175, 23);
		pw.setBounds(80, 87, 175, 23);
		ID.setBounds(12,54,93,23);
		PW.setBounds(12,87,93,23);
		ok.setBounds(57, 131, 67, 34);
		member.setBounds(156, 10, 99, 23);
		cancel.setBounds(156, 131, 67, 34);
		
	}

	private void event() {
		WindowListener exit = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(exit);
		ok.addActionListener(e->{
			try {
				out.writeInt(LOGINPROC);
				out.flush();
				
				User u = new User(id.getText());
				u.setPw(String.valueOf(pw.getPassword()));

				out.writeObject(u);
				out.flush();
				
//				System.out.println(in.readBoolean());
				login = in.readInt();
				
				//로그인 되었을 때
				if(login == SUCCESSLOGIN) {
					this.dispose();
				}
				//회원정보가 일치하지 않을 때
				else if(login == NOTMEMBER) {
					JOptionPane.showMessageDialog(this, "올바르지 않은 아이디 혹은 비밀번호 입니다", "", JOptionPane.PLAIN_MESSAGE);
					id.setText("");
					pw.setText("");
				}
				else if(login == PLAYINGMEMBER) {
					JOptionPane.showMessageDialog(this, "이미 접속중입니다", "", JOptionPane.PLAIN_MESSAGE);
				}
				
			}catch (Exception err) {
				err.printStackTrace();
			}
		});
		cancel.addActionListener(e->{
			System.exit(0);
		});
		member.addActionListener(e->{
			SignUp signup = new SignUp(out, in);
			signup.setVisible(true);
		});
	}
	
	private void setDialogLocation() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int w = d.width/2 - getWidth()/2;
		int h = d.height/2 - getHeight()/2;
		this.setLocation(w,h);
	}
	
	public Login(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
		this.display();
		this.event();
		this.setTitle("로그인");
		this.setSize(300, 230);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setDialogLocation();

	}
}
