package Chatserver;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nickname;
	private String msg;
	private String time;
	public String getNickname() {
		return nickname;
	}
	public String getMsg() {
		return msg;
	}
	public Message(String nickname, String msg) {
		this.nickname=nickname; this.msg=msg;
	}
	public Message(String nickname, String msg, String time) {
		this.nickname=nickname; this.msg=msg; this.time=time;
	}
	public String getTime() {
		return time;
	}
}
