package sutta.useall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room implements Serializable{
	
	private String name;
	private int cnt;
	private boolean ing;
	private List<User> user = new ArrayList<>();
	private int port;
	
	public List<User> getUserList(){
		return user;
	}
	
	public String getIng(){
		return ing? "진행중":"대기중";
	}
	
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Room(String name, int port) {
		this.name = name;
		this.cnt = 0;
		this.ing = false;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public boolean isIng() {
		return ing;
	}
	public void setIng(boolean ing) {
		this.ing = ing;
	}
	public void plusCnt() {
		if(cnt<4) {
			cnt++;			
		}
	}
	public void minusCnt() {
		if(cnt >0) {
			cnt--;			
		}
	}
	public void addUser(User user) {
		this.user.add(user);
	}
	public void removeUser(User user) {
		this.user.remove(user);
	}
	
	
}
