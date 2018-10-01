package sutta.mainserver4;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class Login extends JDialog {
//	������Ʈ ��ġ�� ����
	private Container con = this.getContentPane();
	private JTextField id = new JTextField();
	private JPasswordField pw = new JPasswordField();
	private JLabel ID = new JLabel("���̵� :");
	private JLabel PW = new JLabel("��й�ȣ :");
	private JButton ok = new JButton("Ȯ��");
	private JButton cancel = new JButton("���");
	private JButton member = new JButton("ȸ������");
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean login = false;
	
	public boolean isLogin() throws IOException {
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
		
		id.setBounds(80, 54, 175, 23);
		pw.setBounds(80, 87, 175, 23);
		ID.setBounds(12,54,93,23);
		PW.setBounds(12,87,93,23);
		ok.setBounds(30, 131, 67, 34);
		member.setBounds(109, 131, 67, 34);
		cancel.setBounds(188, 131, 67, 34);
		
	}

	private void event() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ok.addActionListener(e->{
			try {
				out.writeInt(1);
				out.flush();
				
				User u = new User();
				u.id = id.getText();
				u.pw = String.valueOf(pw.getPassword());

				out.writeObject(u);
				out.flush();
				
				try {
					while(true) {
						in.readObject();
					}
				}catch(Exception err) {}
				
				Boolean read = in.readBoolean();
//				System.out.println("read = "+read);
				if(read!=null) {
					login = read;
//					System.out.println("login = "+login);
				}
				
				if(login) {
					System.out.println("�α��� ����");
					this.dispose();
				}
				else {
					JOptionPane.showMessageDialog(this, "�ùٸ��� ���� ���̵� Ȥ�� ��й�ȣ �Դϴ�", "", JOptionPane.PLAIN_MESSAGE);
					id.setText("");
					pw.setText("");
				}
				
			}catch (Exception err) {
				err.printStackTrace();
			}
		});
		cancel.addActionListener(e->{
			dispose();
		});
		member.addActionListener(e->{
			SignUp signup = new SignUp(out, in);
			signup.setVisible(true);
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
	
	public Login(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
		this.display();
		this.event();
		this.menu();
		this.setTitle("�α���");
		this.setSize(300, 230);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setDialogLocation();

	}
}
