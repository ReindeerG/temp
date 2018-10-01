package sutta.mainserver4;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.border.*;

class SignUp extends JDialog{
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
//	������Ʈ ��ġ�� ����
	private Container con = this.getContentPane();
	private User user;
	private JLabel label = new JLabel("ȸ������");
	private JLabel ID = new JLabel("���̵� :");
	private JLabel PW = new JLabel("��й�ȣ :");
	private JLabel NickName = new JLabel("�г��� :");
	private JTextField id = new JTextField();
	private JTextField nickname = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JButton ok = new JButton("Ȯ��");
	private JButton cancel = new JButton("���");

	private void display() {
		con.setLayout(null);
		con.add(label);
		con.add(ID);
		con.add(PW);
		con.add(NickName);
		con.add(id);
		con.add(nickname);
		con.add(pw);
		con.add(ok);
		con.add(cancel);
		
		label.setBounds(77, 10, 176, 35);
		ID.setBounds(12, 61, 97, 20);
		PW.setBounds(12, 144, 97, 20);
		NickName.setBounds(12, 104, 97, 20);
		id.setBounds(103, 58, 234, 28);
		pw.setBounds(103, 144, 234, 28);
		nickname.setBounds(103, 104, 234, 28);
		ok.setBounds(66, 198, 97, 52);
		cancel.setBounds(207, 198, 97, 52);
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
		ok.addActionListener(e->{
			try {
				out.writeInt(0);
				out.flush();
				
				User u = new User();
				u.id = id.getText();
				u.pw = String.valueOf(pw.getPassword());
				u.nickname = nickname.getText();
				out.writeObject(u);
				out.flush();
				
				System.out.println("test");
				
				try {
					while(true) {
						in.readObject();
					}
				}catch(Exception err) {}
				
				Boolean isSignUp = in.readBoolean();
				System.out.println("isSignUp = "+isSignUp);
				if(isSignUp) {
					JOptionPane.showMessageDialog(this, "ȸ�� ������ �Ϸ�Ǿ����ϴ�", "", JOptionPane.PLAIN_MESSAGE);
					this.dispose();
				}
				else {
					JOptionPane.showMessageDialog(this, "�̹� �����ϴ� ���̵� �Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
					id.setText("");
					nickname.setText("");
					pw.setText("");
				}
			}catch(Exception err) {
				err.getMessage();
			}
		});
		cancel.addActionListener(e->{
			dispose();
		});
	}

	private void menu() {
		
	}
	
	private void setDialogLocation() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int w = d.width/2 - getWidth()/2;
		int h = d.height/2 - getHeight()/2;
		this.setLocation(w,h);
	}
	
	public SignUp(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
		this.display();
		this.event();
		this.menu();
		this.setTitle("ȸ�� ����");
		this.setSize(390, 330);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setDialogLocation();
		this.setModal(true);
	}
}


