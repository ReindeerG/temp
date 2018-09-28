package master;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GameWindow extends JFrame {
	private Container con=null;
	private Image img = null;
	private BufferedImage bkimg = null;
	private class Background extends Container {
		private Image temp;
		Background() {
			try {temp = ImageIO.read(new File("Cardimages\\Background.png"));} catch(Exception e) {}
		}
		public void paintComponents(Graphics g) {
			g.drawImage(temp, 0,0,10,10, this);
		}
	}
	
	private void display() {
		this.setContentPane(new Background());
		con = this.getContentPane();
		try {
			bkimg = ImageIO.read(new File("Cardimages\\Background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		img=bkimg;
		con.repaint();
	}
	
	public GameWindow() {
		display();
		this.setTitle("Å×½ºÆ®");
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(this);
	}
}
