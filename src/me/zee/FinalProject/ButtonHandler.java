package me.zee.FinalProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ButtonHandler implements ActionListener {
	private JButton start, help, quit;
	private String title;
	private double wide, tall;
	public ButtonHandler(JButton start, JButton help, JButton quit, String title, double wide, double tall) {
		this.start = start;
		this.help = help;
		this.quit = quit;
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
		
		else if (e.getSource()==help) { //Didn't feel like setting up a help menu/window yet, so we'll have this for now
			JOptionPane.showMessageDialog(null, "Help? Help is for women. We are men!", "Help", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if (e.getSource()==quit) {
			int dialogButton = JOptionPane.showConfirmDialog(null, "Are you sure you want to end the program?", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (dialogButton == JOptionPane.YES_OPTION) System.exit(0);
		}
	}
}