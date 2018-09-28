package sutta.mainserver2;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class Login extends JDialog {
//	컴포넌트 배치용 공간
	private Container con = this.getContentPane();
	private JTextField id = new JTextField();
	private JTextField nickname = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JButton ok = new JButton("확인");
	private JButton cancel = new JButton("취소");
	
	private void display() {
		con.setLayout(null);
		con.add(id);
		con.add(nickname);
		con.add(pw);
		con.add(ok);
		con.add(cancel);
		
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}

	private void menu() {
		
	}
	
	public Login() {
		this.display();
		this.event();
		this.menu();
		this.setTitle("스윙 예제");
		this.setSize(500, 400);
		this.setLocationByPlatform(true);
	}
}
