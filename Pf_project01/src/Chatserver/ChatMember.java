package Chatserver;

import java.net.Socket;

//import Gameserver.Player;

public class ChatMember {
	private Socket socket;
	private ServOneThread thread;
	public ChatMember(Socket socket, ServOneThread thread) {
		this.socket=socket; this.thread=thread;
	}
	public Socket getSocket() {
		return socket;
	}
	public ServOneThread getThread() {
		return thread;
	}
}
