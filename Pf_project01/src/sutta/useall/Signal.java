package sutta.useall;

public interface Signal {
	public static final int JOIN = 0;
	public static final int ADDROOM = 1;
	public static final int QUICKJOIN = 2;
	public static final int EXITROOM = 3;
	public static final int GAMESTART = 4;
	public static final int GAMEEND = 5;
	public static final int LOGOUT = 6;
	
	public static final int NEWMEMBERPROC = 0;
	public static final int LOGINPROC = 1;
	
	public static final int NOTMEMBER = 0;
	public static final int PLAYINGMEMBER = 1;
	public static final int SUCCESSLOGIN = 2;
}
