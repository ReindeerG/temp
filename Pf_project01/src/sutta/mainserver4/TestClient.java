package sutta.mainserver4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


import javax.swing.JOptionPane;

/**
 * 클라이언트 창
 * 
 */

public class TestClient {

	public static void main(String[] args) {
		try {

			InetAddress inet = InetAddress.getByName("localhost");
			Socket socket = null;
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			
			socket = new Socket(inet, 53890);
				
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			Login login  = new Login(out,in);
			login.setVisible(true);
			
			while(!login.isLogin()) {}
			MainWindow m = new MainWindow(socket, out, in);
			m.setVisible(true);
				
			
//			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
