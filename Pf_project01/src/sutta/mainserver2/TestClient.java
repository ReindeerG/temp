package sutta.mainserver2;

import java.net.InetAddress;
import java.net.Socket;


import javax.swing.JOptionPane;

/**
 * 클라이언트 창
 * 임시적으로 JOptionPane.showInputMessage 창으로 닉네임을 입력 받는다
 */

public class TestClient {

	public static void main(String[] args) {
		try {
			InetAddress inet = InetAddress.getByName("localhost");
			Socket socket = new Socket(inet, 53890);
			String name = JOptionPane.showInputDialog(null, "닉네임 입력", "로그인 창", JOptionPane.PLAIN_MESSAGE);
			String id = JOptionPane.showInputDialog("아이디 입력");
			MainWindow m = new MainWindow(socket, name, id);
			m.setVisible(true);
			
//			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
