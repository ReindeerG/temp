package Gamewindow;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class JokboWindow extends JFrame implements ComponentListener{
//	컴포넌트 배치용 공간
	Point mainFrameLocation = new Point(0, 0);
    Point mouseClickedLocation = new Point(0, 0);
    
    private Mainwindow mw;
	private Container con = this.getContentPane();
	private JPanel jp = new JPanel(new GridLayout(33, 1));
	private Font font = new Font("굴림", Font.PLAIN, 17);
	private Font font2 = new Font("궁서", Font.BOLD, 31);
	private Font font3 = new Font("굴림", Font.BOLD, 17);
	private Color LabelFontColor = new Color(41, 214, 140);
	private Color BackGroundColor = new Color(0, 64, 0);
	
	private JLabel a1 = new JLabel(" [1]  3·8광땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a2 = new JLabel(" [2]  1·8광땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a3 = new JLabel(" [3]  1·3광땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	
	private JLabel a4 = new JLabel(" [4]  장땡(10땡) "){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a5 = new JLabel(" [5]  9땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a6 = new JLabel(" [6]  8땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a7 = new JLabel(" [7]  7땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a8 = new JLabel(" [8]  6땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a9 = new JLabel(" [9]  5땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a10 = new JLabel("[10] 4땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a11 = new JLabel("[11] 3땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a12 = new JLabel("[12] 2땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a13 = new JLabel("[13] 1땡"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};

	private JLabel a14 = new JLabel("[14] 알리(1·2)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a15 = new JLabel("[15] 독사(1·4)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a16 = new JLabel("[16] 구삥(9·1)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a17 = new JLabel("[17] 장삥(10·1)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a18 = new JLabel("[18] 장사(10·4)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a19 = new JLabel("[19] 세륙(4·6)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	
	private JLabel a20 = new JLabel("[20] 갑오(9끗)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a21 = new JLabel("[21] 8끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a22 = new JLabel("[22] 7끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a23 = new JLabel("[23] 6끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a24 = new JLabel("[24] 5끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a25 = new JLabel("[25] 4끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a26 = new JLabel("[26] 3끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a27 = new JLabel("[27] 2끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a28 = new JLabel("[28] 1끗"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a29 = new JLabel("[29] 망통(0끗)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	
	private JLabel a30 = new JLabel("[30] 4·7암행어사"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a31 = new JLabel("[31] 3·7땡잡이"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        TooltipColorFont(tip);
	        return tip;
	    }
	};
	private JLabel a32 = new JLabel("[32] 멍텅구리구사"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        tip.setFont(font3);
	        return tip;
	    }
	};
	private JLabel a33 = new JLabel("[33] 구사(9·4)"){
		public JToolTip createToolTip() {
	        JToolTip tip = super.createToolTip();
	        tip.setFont(font3);
	        return tip;
	    }
	};
	private void TooltipColorFont(JToolTip tip) {
		tip.setBackground(Color.CYAN);
        tip.setForeground(Color.BLACK);
        tip.setFont(font3);
	}

	private void display() {
		JLabel[] a = new JLabel[] {a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,a33};
		for(int i=0;i<a.length;i++) {
			a[i].setForeground(LabelFontColor);
		}
		con.add(jp);
		
		Border Border = BorderFactory.createTitledBorder("섯다 족보");
		jp.setBorder(Border);
		
		jp.setBackground(BackGroundColor);
		TitledBorder titledBorder = (TitledBorder)jp.getBorder(); 
		titledBorder.setTitleFont(font2);
	    titledBorder.setTitleColor(LabelFontColor);
	    
	    for(int i=0;i<a.length;i++) {
	    	jp.add(a[i]);
	    }
		
		a1.setToolTipText(" 3월광 + 8월광의 조합 [가장 높은 천하무적 족보] ");
		a2.setToolTipText(" 1월광 + 8월광 조합 [암행어사에게 잡힘] ");
		a3.setToolTipText(" 1월광 + 3월광 조합 [암행어사에게 잡힘] ");
		
		a4.setToolTipText(" 10월 패 2장 조합 [광땡 제외 최고 족보] ");
		a5.setToolTipText(" 9월 패 2장 조합 [땡잡이에게 잡힘] ");
		a6.setToolTipText(" 8월 패 2장 조합 [땡잡이에게 잡힘] ");
		a7.setToolTipText(" 7월 패 2장 조합 [땡잡이에게 잡힘] ");
		a8.setToolTipText(" 6월 패 2장 조합 [땡잡이에게 잡힘] ");
		a9.setToolTipText(" 5월 패 2장 조합 [땡잡이에게 잡힘] ");
		a10.setToolTipText(" 4월 패 2장 조합 [땡잡이에게 잡힘] ");
		a11.setToolTipText(" 3월 패 2장 조합 [땡잡이에게 잡힘] ");
		a12.setToolTipText(" 2월 패 2장 조합 [땡잡이에게 잡힘] ");
		a13.setToolTipText(" 1월 패 2장 조합 [땡잡이에게 잡힘] ");
		
		a14.setToolTipText(" 1월 + 2월 조합 [땡 미만에서 가장 높은 족보] ");
		a15.setToolTipText(" 1월 + 4월 조합 [알리(1·2)보다 낮음] ");
		a16.setToolTipText(" 9월 + 1월 조합 [독사(1·4)보다 낮음] ");
		a17.setToolTipText(" 10월 + 1월 조합 [구삥(9·1)보다 낮음] ");
		a18.setToolTipText(" 10월 + 4월 조합 [장삥(10·1)보다 낮음] ");
		a19.setToolTipText(" 4월 + 6월 조합 [갑오(9끗)보다 높은 족보] ");
		
		a20.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 9 [끗 중에 가장 높은 족보] ");
		a21.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 8 ");
		a22.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 7 ");
		a23.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 6 ");
		a24.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 5 ");
		a25.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 4 ");
		a26.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 3 ");
		a27.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 2 ");
		a28.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 1 ");
		a29.setToolTipText(" 패 두장의 월을 더한 값의 일의자리 끗수가 0 [섯다에서 가장 낮은 족보] ");
		
		a30.setToolTipText(" 4월열끗 + 7월열끗 조합 [1·8광땡, 1·3광땡에게 승리] ");
		a31.setToolTipText(" 3월열끗 + 7월열끗 조합 [1땡~9땡에게 승리] ");
		a32.setToolTipText(" 9월열끗 + 4월열끗 조합 [9땡까지 재경기(장땡불가)] ");
		a33.setToolTipText(" 9월 + 4월의 조합 (멍텅구리구사 제외조합) [알리(1·2)까지 재경기] ");
		
		for(int i=0;i<a.length;i++) {
			a[i].setFont(font);
		}
	}
	private void event() {
		KeyListener key = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				
//				ESC를 누르면 프레임창을 종료
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					mw.toemptyjw();
					dispose();
				}
			}
		};
		this.addKeyListener(key);
	}
	
	public JokboWindow(Mainwindow mw) {
		this.mw=mw;
		this.display();
		this.event();
		this.setSize(170, 660);
		this.setResizable(false);
//		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(mw);
		this.setAlwaysOnTop(true);
		addMouseListener(new FrameMove_mouseAdapter(this));
        addMouseMotionListener(new FrameMove_mouseMotionAdapter(this));
 
        /* root frame */
        setUndecorated(true);
//        setLocationRelativeTo(null);
        addComponentListener(this);
	}
	
//	public static void main(String[] args) {
//		try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e) {e.printStackTrace();}
//		JokboWindow mainFrame = new JokboWindow(null);
//		mainFrame.setVisible(true);
//	}

	public void componentHidden(ComponentEvent arg0) {
    }

    public void componentMoved(ComponentEvent arg0) {
    }
 
    public void componentResized(ComponentEvent arg0) {
    }
 
    public void componentShown(ComponentEvent arg0) {
    }
}	

class FrameMove_mouseAdapter extends MouseAdapter {
    private JokboWindow frame;
 
    FrameMove_mouseAdapter(JokboWindow mainFrame) {
        this.frame = mainFrame;
    }
 
    public void mousePressed(MouseEvent e) {
        frame.mouseClickedLocation.x = e.getX();
        frame.mouseClickedLocation.y = e.getY();
    }
}

class FrameMove_mouseMotionAdapter extends MouseMotionAdapter {
    private JokboWindow frame;
     
    FrameMove_mouseMotionAdapter(JokboWindow mainFrame) {
        this.frame = mainFrame;
    }
 
    public void mouseDragged(MouseEvent e) {
        frame.setLocation(e.getLocationOnScreen().x - frame.mouseClickedLocation.x,
                e.getLocationOnScreen().y - frame.mouseClickedLocation.y);
    }
}