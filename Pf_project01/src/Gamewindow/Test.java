package Gamewindow;

import javax.swing.UIManager;

public class Test {
	public static void main(String[] args) {
		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {}
		MainWindow frame = new MainWindow();
		frame.setVisible(true);
	}
}
