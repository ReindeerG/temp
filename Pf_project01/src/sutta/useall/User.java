package sutta.useall;

import java.io.Serializable;


public class User implements Serializable{
	private final String nickname;
	private final String id;
	private String pw;
	private int money;
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getNickname() {
		return nickname;
	}
	public String getId() {
		return id;
	}
	public User(String id, String nickname) {
		super();
		this.nickname = nickname;
		this.id = id;
	}
	public User(String id) {
		this(id,null);
	}
	
	
	
}
