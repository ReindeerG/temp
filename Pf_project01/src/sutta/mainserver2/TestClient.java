package sutta.mainserver2;

import java.net.InetAddress;
import java.net.Socket;


import javax.swing.JOptionPane;

/**
 * Ŭ���̾�Ʈ â
 * �ӽ������� JOptionPane.showInputMessage â���� �г����� �Է� �޴´�
 */

public class TestClient {

	public static void main(String[] args) {
		try {
			InetAddress inet = InetAddress.getByName("localhost");
			Socket socket = new Socket(inet, 53890);
			String name = JOptionPane.showInputDialog(null, "�г��� �Է�", "�α��� â", JOptionPane.PLAIN_MESSAGE);
			String id = JOptionPane.showInputDialog("���̵� �Է�");
			MainWindow m = new MainWindow(socket, name, id);
			m.setVisible(true);
			
//			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
