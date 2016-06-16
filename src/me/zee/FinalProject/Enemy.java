package me.zee.FinalProject;

import java.awt.Point;
import java.util.Random;
import java.util.UUID;
import com.ethanzeigler.jgamegui.element.CollidableImageElement;

public class Enemy {
	public enum MOVE_DIRECTION { UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT };
	public enum ENEMY_TYPE { RED, BLUE };
	private MOVE_DIRECTION moveDirection;
	private ENEMY_TYPE enemyType;
	private UUID UniqueID;
	private boolean isAlive;
	private CollidableImageElement imageElement;
	private Game game;
	
	public Enemy(CollidableImageElement imgElement, Game game) {
		this.moveDirection = Enemy.getRandomDirection();
		this.UniqueID = UUID.randomUUID();
		this.isAlive = true;
		this.imageElement = imgElement;
		this.game = game;
	}
	
	public Enemy(CollidableImageElement imgElement, boolean isAlive) {
		this.UniqueID = UUID.randomUUID();
		this.isAlive = isAlive;
		this.imageElement = imgElement;
	}
	
	public UUID getUniqueID() {
		return this.UniqueID;
	}
	public void setUniqueID(UUID uuid) {
		this.UniqueID = uuid;
	}
	
	public boolean isAlive() {
		return this.isAlive;
	}
	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public CollidableImageElement getImageElement() {
		return this.imageElement;
	}
	public void setImageElement(CollidableImageElement imgElement) {
		this.imageElement = imgElement;
	}
	
	public MOVE_DIRECTION getMoveDirection() {
		return this.moveDirection;
	}
	public void setMoveDirection(MOVE_DIRECTION moveD) {
		this.moveDirection = moveD;
	}
	
	public ENEMY_TYPE getEnemyType() {
		return this.enemyType;
	}
	public void setEnemyType(ENEMY_TYPE enemyType) {
		this.enemyType = enemyType;
	}
	
	public Point getPosition() {
		return new Point((int) imageElement.getOriginX(), (int) imageElement.getOriginY());
	}
	
	public void kill() {
		setIsAlive(false);
		game.gameScreen.removeElement(imageElement);
	}
	
	/**
	 * <p>Returns a random MOVE_DIRECTION</p>
	 * 
	 * @return MOVE_DIRECTION A random direction
	 */
	public static MOVE_DIRECTION getRandomDirection() {
		Random gen = new Random();
		int dir = gen.nextInt(8);
		switch (dir) {
		case 0:
			return MOVE_DIRECTION.DOWN;
		case 1:
			return MOVE_DIRECTION.DOWN_LEFT;
		case 2:
			return MOVE_DIRECTION.DOWN_RIGHT;
		case 3:
			return MOVE_DIRECTION.LEFT;
		case 4:
			return MOVE_DIRECTION.RIGHT;
		case 5:
			return MOVE_DIRECTION.UP;
		case 6:
			return MOVE_DIRECTION.UP_LEFT;
		case 7:
			return MOVE_DIRECTION.UP_RIGHT;
		default:
			return MOVE_DIRECTION.DOWN; //Don't think this can happen, but just in case
		}
	}
	
	public int getMoveSpeed() {
		switch (enemyType) {
		case BLUE:
			return 15;
		case RED:
			return 5;
		default: //This shouldn't ever happen but I need a default
			return 5;
		}
	}
}