package Chatserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ServOneThread extends Thread {
	private boolean stop;
	private Socket socket;
	private Format f = new SimpleDateFormat("a hh:mm");
	private Server serv;
	public ServOneThread(Socket socket, Server serv) {
		this.socket=socket; this.serv=serv;
	}
	public void toStop() {
		stop=true;
	}
	public void run() {
		stop = false;
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			while(true) {
				if(stop==true) { return; }
//				if(socket==null) {
//					List<ChatMember> members = Server.getMemList();
//					int index=0;
//					for(ChatMember c : members) {
//						if(c.getThread().equals(this)) {
//							members.remove(index);
//							return;
//						}
//						index++;
//					}
//				}
				else {
					Message msg = (Message)in.readObject();
					String read = msg.getMsg();
					String fromnick = msg.getNickname();
					Date d = new Date();
					List<ChatMember> members = serv.getMemList();
					int index=0;
					for(ChatMember c : members) {
						if(c.getSocket().equals(null)) {
							c.getThread().toStop();
							members.remove(index);
						}
						else {
							ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(c.getSocket().getOutputStream()));
							out.writeObject(new Message(fromnick, read, f.format(d)));
							out.flush();
						}
						index++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class Server extends Thread {
	private List<ChatMember> members = new ArrayList<>();
	private int port = 53892;
	private ServerSocket server;
	
	public Server() {
		start();
	}
	public List<ChatMember> getMemList() {
		return members;
	}
	public void run() {
		try {
			server = new ServerSocket(port);
			while(true) {
				
				Socket receive = server.accept();
				boolean exit = false;
				for(ChatMember c : members) {
					if(c.getSocket().equals(receive)) {
						exit = true;
						break;
					};
				}
				if(exit==false) {		
					ServOneThread thread = new ServOneThread(receive, this);
					thread.setDaemon(true);
					thread.start();
					members.add(new ChatMember(receive, thread));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
