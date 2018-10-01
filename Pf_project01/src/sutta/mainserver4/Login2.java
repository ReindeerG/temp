package sutta.mainserver4;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;



public class Login2 extends JDialog {
//	컴포넌트 배치용 공간
	private Container con = this.getContentPane();
	private JTextField id = new JTextField();
	private JTextField nickname = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JButton ok = new JButton("확인");
	private JButton cancel = new JButton("취소");
	private JTextField textField;
	private JTextField textField_2;
	
	private void display() {
		getContentPane().setLayout(null);
		id.setBounds(0, 0, 0, 0);
		con.add(id);
		nickname.setBounds(0, 0, 0, 0);
		con.add(nickname);
		pw.setBounds(0, 0, 0, 0);
		con.add(pw);
		ok.setBounds(0, 0, 0, 0);
		con.add(ok);
		cancel.setBounds(0, 0, 0, 0);
		con.add(cancel);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(96, 10, 93, 23);
		getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("New label");
		label.setBounds(12, 54, 93, 23);
		getContentPane().add(label);
		
		JLabel label_2 = new JLabel("New label");
		label_2.setBounds(12, 87, 93, 23);
		getContentPane().add(label_2);
		
		textField = new JTextField();
		textField.setBounds(80, 54, 175, 23);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setText("87, 175, 23");
		textField_2.setColumns(10);
		textField_2.setBounds(80, 87, 175, 23);
		getContentPane().add(textField_2);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(30, 131, 67, 34);
		getContentPane().add(btnNewButton);
		
		JButton button = new JButton("New button");
		button.setBounds(109, 131, 67, 34);
		getContentPane().add(button);
		
		JButton button_1 = new JButton("New button");
		button_1.setBounds(188, 131, 67, 34);
		getContentPane().add(button_1);
		
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}

	private void menu() {
		
	}
	
	public Login2() {
		this.display();
		this.event();
		this.menu();
		this.setTitle("스윙 예제");
		this.setSize(300, 228);
		this.setLocationByPlatform(true);
	}
}
