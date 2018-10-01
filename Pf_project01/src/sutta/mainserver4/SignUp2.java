package sutta.mainserver4;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

class SignUp2 extends JDialog{
//	컴포넌트 배치용 공간
	private Container con = this.getContentPane();
	private User user;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private void display() {
		con.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(77, 10, 176, 35);
		getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(66, 198, 97, 52);
		getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(12, 61, 97, 20);
		getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(103, 58, 234, 28);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("New label");
		label.setBounds(12, 104, 97, 20);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("New label");
		label_1.setBounds(12, 144, 97, 20);
		getContentPane().add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(103, 104, 234, 28);
		getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(103, 144, 234, 28);
		getContentPane().add(textField_2);
		
		JButton button = new JButton("New button");
		button.setBounds(207, 198, 97, 52);
		getContentPane().add(button);
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}

	private void menu() {
		
	}
	
	public SignUp2() {
		this.display();
		this.event();
		this.menu();
		this.setTitle("스윙 예제");
		this.setSize(382, 322);
		this.setLocationByPlatform(true);
	}
}


