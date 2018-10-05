package sutta.mainserver6;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import sutta.useall.User;

public class BackUpManager {
	public static void backUpUserInfo(File f,ArrayList<User> user) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
				ArrayList<User> userInfo = user;
				out.writeObject(userInfo);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static ArrayList<User> getUserInfo(File f) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
			ArrayList<User> userInfo = (ArrayList<User>)in.readObject();
			in.close();
			return userInfo;
		} catch (Exception e) {
		}
		return null;
	}
}
