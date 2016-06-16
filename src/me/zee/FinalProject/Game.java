package me.zee.FinalProject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import me.zee.FinalProject.Enemy.ENEMY_TYPE;
import me.zee.FinalProject.Enemy.MOVE_DIRECTION;
import com.ethanzeigler.jgamegui.JGameGUI;
import com.ethanzeigler.jgamegui.element.CollidableImageElement;
import com.ethanzeigler.jgamegui.element.ImageElement;
import com.ethanzeigler.jgamegui.element.TextElement;
import com.ethanzeigler.jgamegui.window.Window;

@SuppressWarnings("serial")
public class Game extends JGameGUI {
	//private ImageElement[] healthBar = new ImageElement[10];
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private String title;
	private int wide, tall, killCounter = 0, health;
	private boolean gameIsPaused=false, gameHasStarted=false, gameHasEnded=false;
	public Window gameScreen;
	private CollidableImageElement player;
	private TextElement killCounterDisplay, healthDisplay;
	private boolean isWDown=false, isADown=false, isSDown=false, isDDown=false;
	private final int PLAYER_MOVE_SPEED = 10, STARTING_ENEMY_COUNT = 4;
	private long lastTimeHit = 0;

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
			if (enemy.isAlive() && enemy.getEnemyType()==Enemy.ENEMY_TYPE.RED) counter++; //Blue enemies can't be killed, so we only care if all the red ones are dead
		}
		return counter == 0;
	}
	
	public void fixEnemyMoveDirection(Enemy enemy) {
		double x = enemy.getImageElement().getOriginX(), y = enemy.getImageElement().getOriginY();
		MOVE_DIRECTION dir = enemy.getMoveDirection();
		
		if (x<0) {
			if (dir == MOVE_DIRECTION.LEFT)
				enemy.setMoveDirection(MOVE_DIRECTION.RIGHT);
			else if (dir == MOVE_DIRECTION.UP_LEFT)
				enemy.setMoveDirection(MOVE_DIRECTION.UP_RIGHT);
			else if (dir == MOVE_DIRECTION.DOWN_LEFT)
				enemy.setMoveDirection(MOVE_DIRECTION.DOWN_RIGHT);
		}
		else if (y<20) {
			if (dir == MOVE_DIRECTION.UP_LEFT)
				enemy.setMoveDirection(MOVE_DIRECTION.DOWN_LEFT);
			else if (dir == MOVE_DIRECTION.UP_RIGHT)
				enemy.setMoveDirection(MOVE_DIRECTION.DOWN_RIGHT);
			else if (dir == MOVE_DIRECTION.UP)
				enemy.setMoveDirection(MOVE_DIRECTION.DOWN);
		}
		else if (x>(wide-100)) {
			if (dir == MOVE_DIRECTION.RIGHT)
				enemy.setMoveDirection(MOVE_DIRECTION.LEFT);
			if (dir == MOVE_DIRECTION.UP_RIGHT)
				enemy.setMoveDirection(MOVE_DIRECTION.UP_LEFT);
			if (dir == MOVE_DIRECTION.DOWN_RIGHT)
				enemy.setMoveDirection(MOVE_DIRECTION.DOWN_LEFT);
		}
		else if (y>(tall-100)) {
			if (dir == MOVE_DIRECTION.DOWN_LEFT)
				enemy.setMoveDirection(MOVE_DIRECTION.UP_LEFT);
			else if (dir == MOVE_DIRECTION.DOWN_RIGHT)
				enemy.setMoveDirection(MOVE_DIRECTION.UP_RIGHT);
			else if (dir == MOVE_DIRECTION.DOWN)
				enemy.setMoveDirection(MOVE_DIRECTION.UP);
		}
	}
	
	public void playSound(String name) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("resources/"+name+".wav")));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	public void spawnNewEnemy() {
		Dimension pos = getRandomPosition();
		CollidableImageElement image;
		int num = new Random().nextInt(10); //generates a number 0-19, if it's 0 (5% chance) then we spawn a blue circle
		if (num==0)
			image = new CollidableImageElement(new ImageIcon("resources/bluecircle.png"), pos.getWidth(), pos.getHeight(), pos.getWidth(), pos.getHeight(), 2);
		else
			image = new CollidableImageElement(new ImageIcon("resources/redcircle.png"), pos.getWidth(), pos.getHeight(), pos.getWidth(), pos.getHeight(), 2);
		if (image==null) {
			System.out.println("Enemy is null");
			return;
		}
		
		Enemy enemy = new Enemy(image, this);
		if (num==0)
			enemy.setEnemyType(ENEMY_TYPE.BLUE);
		else
			enemy.setEnemyType(ENEMY_TYPE.RED);
		
		gameScreen.addElement(image);
		enemies.add(enemy);
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
		if (gameIsPaused==true && gameHasEnded==false) {
			gameIsPaused=false;
		}
	}

	public void pauseGame() {
		gameIsPaused = true;
	}

	public void endGame() {
		onStop();
	}

	public double calcDistance(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}
		
	/**
	 * <p>Moves all enemies to their next position</p>
	 */
	public void moveAllEnemies() {
		for (Enemy enemy : enemies) {
			if (enemy.isAlive()) {
				switch (enemy.getMoveDirection()) {
				case DOWN:
					enemy.getImageElement().setOriginY(enemy.getImageElement().getOriginY()+enemy.getMoveSpeed());
					break;
				case DOWN_LEFT:
					enemy.getImageElement().setOriginY(enemy.getImageElement().getOriginY()+enemy.getMoveSpeed());
					enemy.getImageElement().setOriginX(enemy.getImageElement().getOriginX()-enemy.getMoveSpeed());
					break;
				case DOWN_RIGHT:
					enemy.getImageElement().setOriginY(enemy.getImageElement().getOriginY()+enemy.getMoveSpeed());
					enemy.getImageElement().setOriginX(enemy.getImageElement().getOriginX()+enemy.getMoveSpeed());
					break;
				case LEFT:
					enemy.getImageElement().setOriginX(enemy.getImageElement().getOriginX()-enemy.getMoveSpeed());
					break;
				case RIGHT:
					enemy.getImageElement().setOriginX(enemy.getImageElement().getOriginX()+enemy.getMoveSpeed());
					break;
				case UP:
					enemy.getImageElement().setOriginY(enemy.getImageElement().getOriginY()-enemy.getMoveSpeed());
					break;
				case UP_LEFT:
					enemy.getImageElement().setOriginY(enemy.getImageElement().getOriginY()-enemy.getMoveSpeed());
					enemy.getImageElement().setOriginX(enemy.getImageElement().getOriginX()-enemy.getMoveSpeed());
					break;
				case UP_RIGHT:
					enemy.getImageElement().setOriginY(enemy.getImageElement().getOriginY()-enemy.getMoveSpeed());
					enemy.getImageElement().setOriginX(enemy.getImageElement().getOriginX()+enemy.getMoveSpeed());
					break;
				default:
					//what in the world has happened
					break;
				}
				fixEnemyMoveDirection(enemy);
			}
		}
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
		killCounterDisplay.setColor(Color.BLUE);
		gameScreen.addElement(killCounterDisplay);

		g.setWindow(gameScreen);
		g.setLocationRelativeTo(null);
		
		//Health bar
		double xPos = dim.getWidth() * .8, yPos = dim.getHeight() * .1;
		health = 100;
		healthDisplay = new TextElement(xPos, yPos, 5, "Health: " + Integer.toString(health));
		healthDisplay.setColor(Color.RED);
		gameScreen.addElement(healthDisplay);
		
		//Health bar

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
		if (gameIsPaused==false && gameHasStarted==true) {
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
					if (enemy.getEnemyType()==Enemy.ENEMY_TYPE.RED) { //Enemy is red, can be killed
						killCounter++;
						killCounterDisplay.setText(Integer.toString(killCounter));
						playSound("bang");

						int newEnemyCount = new Random().nextInt(3);

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
									gameHasEnded = true;

									//saveGameScore();
								}
							}
						};
						timer.schedule(task, 100);
						enemy.kill();
						ImageElement fire = new ImageElement(new ImageIcon("resources/fire.png"), enemy.getPosition().getX(), enemy.getPosition().getY(), 2);
						gameScreen.addElement(fire);

						TimerTask fireRemover = new TimerTask() {
							public void run() {
								gameScreen.removeElement(fire);
							}
						};
						timer.schedule(fireRemover, 1*1000);
					}
					else { //Enemy is blue, can't be killed, do damage to player
						if (!(gameIsPaused)) {
							long curTime = new Date().getTime()/1000;
							if (curTime - lastTimeHit >=2) {
								health -= 10;
								lastTimeHit = curTime;
								healthDisplay.setText("Health: " + Integer.toString(health));
								playSound("ouch");
							}
						}
					}
				}
			}
		}
		
		if (!(gameIsPaused)) moveAllEnemies();
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