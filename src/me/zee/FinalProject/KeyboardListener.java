package me.zee.FinalProject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
	private PauseMenu pMenu;
	public KeyboardListener(PauseMenu pMenu) {
		this.pMenu = pMenu;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		//No need for this one
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE) pMenu.resume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//No need for this one
	}
}