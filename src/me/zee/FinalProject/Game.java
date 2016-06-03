package me.zee.FinalProject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import com.ethanzeigler.jgamegui.JGameGUI;
import com.ethanzeigler.jgamegui.element.CollidableImageElement;
import com.ethanzeigler.jgamegui.element.TextElement;
import com.ethanzeigler.jgamegui.window.Window;

@SuppressWarnings("serial")
public class Game extends JGameGUI {
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private String title;
	private int wide, tall, killCounter = 0;
	private boolean gameIsPaused=false, gameHasStarted=false;
	public Window gameScreen;
	private CollidableImageElement player;
	private TextElement killCounterDisplay;
	private boolean isWDown=false, isADown=false, isSDown=false, isDDown=false;
	private final int PLAYER_MOVE_SPEED = 10, STARTING_ENEMY_COUNT = 4;

	public Game(String title, double wide, double tall) {
		super((int) wide, (int) tall);

		this.title = title;
		this.wide = (int) wide;
		this.tall = (int) tall;
	}
	
	public boolean getGameIsOver() {
		if (!(gameHasStarted)) return false;
		
		int counter = 0;
		for (Enemy enemy : enemies) {
			if (enemy.isAlive()) counter++;
		}
		return counter == 0;
	}

	@SuppressWarnings("unused")
	public void spawnNewEnemy() {
		Dimension pos = getRandomPosition();
		CollidableImageElement enemy = new CollidableImageElement(new ImageIcon("resources/redcircle.png"), pos.getWidth(), pos.getHeight(), pos.getWidth(), pos.getHeight(), 2);
		if (enemy==null) {
			System.out.println("Enemy is null");
			return;
		}
		gameScreen.addElement(enemy);
		enemies.add(new Enemy(enemy, this));
	}

	public Dimension getRandomPosition() {
		Random generator = new Random();
		int x = generator.nextInt((int) Main.getDimensions().getWidth()), y = generator.nextInt((int) Main.getDimensions().getHeight());
		if (x<100) x=100;
		else if (x>(wide-100)) x = wide-100;
		if (y<100) y = 100;
		else if (y>(tall-100)) y = tall-100;

		return new Dimension(x, y);
	}

	public String getTitle() {
		return this.title;
	}

	public int getWide() {
		return this.wide;
	}

	public int getTall() {
		return this.tall;
	}

	public void resumeGame() {
		if (gameIsPaused) {
			gameIsPaused=false;
		}
	}

	public void pauseGame() {
		gameIsPaused = true;
	}

	public void endGame() {
		onStop();
	}

	double calcDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}

	/**
	 * Called before the window is displayed
	 *
	 * @param g The JGameGUI instance that is being started
	 */
	@Override
	protected void onStart(JGameGUI g) {
		Dimension dim = Main.getDimensions();
		gameScreen = new Window();
		player = new CollidableImageElement(new ImageIcon("resources/circle.png"), (dim.getWidth()/2)-50, (dim.getHeight()/2)-50, (dim.getWidth()/2)-50, (dim.getHeight()/2)-50, 3);
		gameScreen.addElement(player);
		killCounterDisplay = new TextElement(20, 60, 4, Integer.toString(killCounter));
		killCounterDisplay.setColor(Color.RED);
		gameScreen.addElement(killCounterDisplay);

		g.setWindow(gameScreen);
		g.setLocationRelativeTo(null);

		Timer timer = new Timer();
		TimerTask myTask = new TimerTask() {
			@Override
			public void run() {
				for (int i=0; i<STARTING_ENEMY_COUNT; i++) {
					spawnNewEnemy();
				}
				gameHasStarted=true;
			}
		};
		timer.schedule(myTask, 3*1000);
	}

	/**
	 * <p>Called before the GUI is updated each frame and can be used to update AbstractElement positions.
	 * This is invoked <i><s>before</s></i>
	 * any animations defined in AbstractElement</p>
	 *
	 * @param gui The JGameGUI instance that is updating
	 */
	@Override
	protected void onScreenUpdate(JGameGUI gui) {
		if (!(gameIsPaused)) {
			if (isWDown) {
				if (player.getOriginY()>9) {
					player.setOriginY(player.getOriginY()-PLAYER_MOVE_SPEED);
				} else player.setOriginY(9);
			}
			if (isSDown) {
				if (player.getOriginY()<(tall-87)) {
					player.setOriginY(player.getOriginY()+PLAYER_MOVE_SPEED);
				} else player.setOriginY(tall-87);
			}
			if (isADown) {
				if (player.getOriginX()>-14) {
					player.setOriginX(player.getOriginX()-PLAYER_MOVE_SPEED);
				} else player.setOriginX(-14);
			}
			if (isDDown) {
				if (player.getOriginX()<(wide-88)) {
					player.setOriginX(player.getOriginX()+PLAYER_MOVE_SPEED);
				} else player.setOriginX(wide-88);
			}
		}

		for (Enemy enemy : enemies) {
			if (enemy.isAlive()) {
				if (calcDistance(player.getOriginX(), player.getOriginY(), enemy.getImageElement().getOriginX(), enemy.getImageElement().getOriginY())<=85) {
					killCounter++;
					killCounterDisplay.setText(Integer.toString(killCounter));
					
					int newEnemyCount = new Random().nextInt(1);
					
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						public void run() {
							for (int i=0; i<newEnemyCount; i++) {
								spawnNewEnemy();
							}
							enemies.remove(enemy); //The game tends to freeze when I call this outside of the timer task, not sure why but w/e
							
							if (getGameIsOver()==true) { //Used to have this in another timer, but kept ending the game then spawning new enemies afterwards, dunno why
														 //This fixed it anyway so w/e
								gameIsPaused=true;
								TextElement victoryDisplayMsg = new TextElement((wide/2)-265, (tall/2), 5, "You've won! There are no more enemies!");
								victoryDisplayMsg.setColor(Color.GREEN);
								gameScreen.addElement(victoryDisplayMsg);
							}
						}
					};
					timer.schedule(task, 100);
					enemy.kill();
				}
			}
		}
	}

	/**
	 * Called when the VM is being disabled. This method can be used to dispose of any variables or save data.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		System.exit(0);
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 *
	 * @param e the MouseEvent
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
	}

	/**
	 * Invoked when the mouse enters a component.
	 *
	 * @param e the MouseEvent
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param e the MouseEvent
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
	}

	/**
	 * Invoked when the mouse exits a component.
	 *
	 * @param e the MouseEvent
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		super.mouseExited(e);
	}

	/**
	 * Invoked when a mouse button is pressed on a component and then
	 * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
	 * delivered to the component where the drag originated until the
	 * mouse button is released (regardless of whether the mouse position
	 * is within the bounds of the component).
	 * <p>
	 * Due to platform-dependent Drag&amp;Drop implementations,
	 * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
	 * Drag&amp;Drop operation.
	 *
	 * @param e the MouseEvent
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
	}

	/**
	 * Invoked when the mouse cursor has been moved onto a component
	 * but no buttons have been pushed.
	 *
	 * @param e the MouseEvent
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
	}

	/**
	 * Invoked when a key has been typed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key typed event.
	 *
	 * @param e the KeyEvent
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		super.keyTyped(e);
	}

	/**
	 * Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 *
	 * @param e the KeyEvent
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ESCAPE) { //Pause game
			//Stop movement of all enemies
			isWDown=false;
			isADown=false;
			isSDown=false;
			isDDown=false;

			pauseGame();
			new PauseMenu(this);

			super.keyPressed(e);

			return;
		}
		if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP)
			isWDown=true;
		else if (e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT)
			isADown=true;
		else if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN)
			isSDown=true;
		else if (e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT)
			isDDown=true;
		super.keyPressed(e);
	}

	/**
	 * Invoked when a key has been released.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key released event.
	 *
	 * @param e the KeyEvent
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (gameIsPaused) return;
		if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP)
			isWDown=false;
		else if (e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT)
			isADown=false;
		else if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN)
			isSDown=false;
		else if (e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT)
			isDDown=false;
		super.keyReleased(e);
	}
}