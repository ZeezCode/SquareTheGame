package me.zee.FinalProject;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
	private static ButtonHandler btnHandler;
	private static JFrame frame;
	private static JLabel menuText;
	private static JButton startBtn, quitBtn, helpBtn;
	
	private static final String GAME_TITLE = "Square - The Game - v.0.6 Alpha";
	private static double wide, tall;
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		wide = screenSize.getWidth() * .8;
		tall = screenSize.getHeight() * .8;
		
		setupScreen();
	}
	
	public static void setupScreen() {
		frame = new JFrame("Square - The Game - Main Menu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLayout(new GridLayout(4, 0));
		
		menuText = new JLabel("Square - The Game");
		menuText.setHorizontalAlignment(JLabel.CENTER);
		frame.add(menuText);
		
		startBtn = new JButton("Start!");
		frame.add(startBtn);
		
		helpBtn = new JButton("Help");
		frame.add(helpBtn);
		
		quitBtn = new JButton("Quit");
		frame.add(quitBtn);
		
		btnHandler = new ButtonHandler(startBtn, quitBtn, helpBtn, GAME_TITLE, wide, tall);
		
		startBtn.addActionListener(btnHandler);
		helpBtn.addActionListener(btnHandler);
		quitBtn.addActionListener(btnHandler);
		
		frame.getRootPane().setDefaultButton(startBtn);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void hideWindow() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	public static Dimension getDimensions() {
		return new Dimension((int) wide, (int) tall);
	}
}