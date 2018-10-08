package sutta.gamewindow;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import sutta.gameserver.Client_Ex;

import javax.swing.ButtonGroup;
import javax.swing.JButton;

public class PandonWindow extends JDialog{
	private Client_Ex client;
	public Client_Ex getClient() {
		return client;
	}
	public PandonWindow(Client_Ex client) {
		this.client=client;
		getContentPane().setLayout(null);
		this.setTitle("판돈수정");
		this.setSize(280, 140);
		this.setResizable(false);
		this.setLocationRelativeTo(this);
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("판돈(시작 전 기본베팅금)을 선택해주세요.");
		lblNewLabel.setBounds(12, 10, 400, 15);
		getContentPane().add(lblNewLabel);
		
		JRadioButton Rd_10 = new JRadioButton("10전");
		Rd_10.setBounds(18, 31, 50, 20);
		getContentPane().add(Rd_10);
		JRadioButton Rd_20 = new JRadioButton("20전");
		Rd_20.setBounds(98, 31, 50, 20);
		getContentPane().add(Rd_20);
		JRadioButton Rd_50 = new JRadioButton("50전");
		Rd_50.setBounds(178, 31, 50, 20);
		getContentPane().add(Rd_50);
		ButtonGroup group = new ButtonGroup();
		group.add(Rd_10);
		group.add(Rd_20);
		group.add(Rd_50);
		switch(client.getPandon()) {
			case 10: Rd_10.setSelected(true); break;
			case 20: Rd_20.setSelected(true); break;
			case 50: Rd_50.setSelected(true); break;
			default: break;
		}
		JButton bt_ok = new JButton("확인");
		bt_ok.setBounds(82, 65, 97, 23);
		getContentPane().add(bt_ok);
		
		bt_ok.addActionListener(e->{
			if(Rd_10.isSelected()) {
				if(client.getPandon()!=10) client.Pandon(10);
			} else if(Rd_20.isSelected()) {
				if(client.getPandon()!=20) client.Pandon(20);
			} else if(Rd_50.isSelected()) {
				if(client.getPandon()!=50) client.Pandon(50);
			}
			dispose();
		});
	}
}
