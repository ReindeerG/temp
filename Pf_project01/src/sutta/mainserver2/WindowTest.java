package sutta.mainserver2;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;

public class WindowTest extends JFrame{
	public WindowTest() {
		getContentPane().setLayout(null);
		
		JList list = new JList();
		list.setBounds(12, 27, 900, 678);
		getContentPane().add(list);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(925, 38, 141, 36);
		getContentPane().add(btnNewButton);
		
		JButton button = new JButton("New button");
		button.setBounds(925, 84, 141, 36);
		getContentPane().add(button);
		
		JButton button_1 = new JButton("New button");
		button_1.setBounds(925, 130, 141, 36);
		getContentPane().add(button_1);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(12, 0, 130, 25);
		getContentPane().add(lblNewLabel);
	}
}
