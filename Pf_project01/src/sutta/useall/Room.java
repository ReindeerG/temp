package sutta.useall;

import java.io.Serializable;
import java.util.Arrays;

public class Room implements Serializable{
	@Override
	public String toString() {
		return "Room [name=" + name + ", cnt=" + cnt + ", ing=" + ing + ", user=" + Arrays.toString(user) + "]";
	}
	private String name;
	private int cnt;
	private boolean ing;
	private User[] user = new User[4];
	
	public Room(String name) {
		this.name = name;
		this.cnt = 0;
		this.ing = false;
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
		if(cnt<4 || cnt >0) {
			for(int i = 0; i< this.user.length; i++) {
				if(this.user[i] == null) {
					this.user[i] = user;
					break;
				}
			}
		}
	}
	
	
}
