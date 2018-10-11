package sutta.mainclient6;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.UIManager;

import sutta.useall.Signal;

/**
 * 클라이언트 창
 * 
 */
//방 목록 업데이트 상황 변경 사항 적용되는지 확인하기 

public class TestClient {
	

	public static void main(String[] args) {
		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {}
		try {
			InetAddress inet = InetAddress.getByName("localhost");
			ObjectOutputStream out = null;
			ObjectInputStream in = null;

			Socket socket = new Socket(inet, 54890);
				
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			Login login  = new Login(out,in);
		 	login.setVisible(true);
			
			while(login.isLogin() != Signal.SUCCESSLOGIN) {}
			MainWindow m = new MainWindow(socket, out, in);
			m.setVisible(true);
				
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
