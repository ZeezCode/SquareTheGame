package me.zee.FinalProject;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PauseMenu {
	private KeyboardListener kbListener;
	private Game game;
	private JFrame frame;
	private JButton resume, quit, help;
	public PauseMenu(Game game) {
		this.game = game;
		this.kbListener = new KeyboardListener(this);
		
		frame = new JFrame("Square - The Game - Pause Menu");
		frame.setSize(400, 400);
		frame.setLayout(new GridLayout(3, 1));
		frame.setUndecorated(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				resume();
			}
		});
		
		resume = new JButton("Resume Game");
		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resume();
			}
		});
		frame.add(resume);
		
		help = new JButton("Help");
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Help? Help is for women. We are men!", "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		frame.add(help);
		
		quit = new JButton("End Game");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.showConfirmDialog(null, "Are you sure you want to end the program?","Confirmation", JOptionPane.YES_NO_OPTION);
				if (dialogButton == JOptionPane.YES_OPTION) {
					frame.setVisible(false);
					frame.dispose();
					game.endGame();
				}
			}
		});
		frame.add(quit);
		
		frame.addKeyListener(kbListener);
		resume.addKeyListener(kbListener);
		quit.addKeyListener(kbListener);
		help.addKeyListener(kbListener);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void resume() {
		frame.setVisible(false);
		frame.dispose();
		game.resumeGame();
	}
}