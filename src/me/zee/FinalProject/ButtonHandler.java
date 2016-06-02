package me.zee.FinalProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ButtonHandler implements ActionListener {
	private JButton start, quit, help;
	private String title;
	private double wide, tall;
	public ButtonHandler(JButton start, JButton quit, JButton help, String title, double wide, double tall) {
		this.start = start;
		this.quit = quit;
		this.help = help;
		this.title = title;
		this.wide = wide;
		this.tall = tall;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==start) {
			Main.hideWindow();
			new Game(title, wide, tall);
		}
		
		else if (e.getSource()==quit) {
			int dialogButton = JOptionPane.showConfirmDialog(null, "Are you sure you want to end the program?","Confirmation", JOptionPane.YES_NO_OPTION);
			if (dialogButton == JOptionPane.YES_OPTION) System.exit(0);
		}
		
		else if (e.getSource()==help) {
			JOptionPane.showMessageDialog(null, "Help? Help is for women. We are men!", "Help", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}