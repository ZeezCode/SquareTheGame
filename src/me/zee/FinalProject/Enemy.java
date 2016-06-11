package me.zee.FinalProject;

import java.awt.Point;
import java.util.Random;
import java.util.UUID;

import com.ethanzeigler.jgamegui.element.CollidableImageElement;

public class Enemy {
	public enum MOVE_DIRECTION { UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT };
	private MOVE_DIRECTION moveDirection;
	private UUID UniqueID;
	private boolean isAlive;
	private CollidableImageElement imageElement;
	private Game game;
	
	/**
	 * <p>Regular constructor</p>
	 * <p>NOTE: Creating this enemy does not add it to the Window. That is not handled by this class. (It may in the future though?)</p>
	 * 
	 * @param imgElement The initial CollidableImageElement that represents this enemy
	 * @param isAlive Whether or not the enemy should be considered alive
	 */
	public Enemy(CollidableImageElement imgElement, Game game) {
		this.moveDirection = Enemy.getRandomDirection();
		this.UniqueID = UUID.randomUUID();
		this.isAlive = true;
		this.imageElement = imgElement;
		this.game = game;
	}
	
	/**
	 * <p>Regular constructor, don't know if I'll ever use this but who knows</p>
	 * <p>NOTE: Creating this enemy does not add it to the Window. That is not handled by this class. (It may in the future though?)</p>
	 * 
	 * @param imgElement The initial CollidableImageElement that represents this enemy
	 * @param isAlive Whether or not the enemy should be considered alive
	 */
	public Enemy(CollidableImageElement imgElement, boolean isAlive) {
		this.UniqueID = UUID.randomUUID();
		this.isAlive = isAlive;
		this.imageElement = imgElement;
	}
	
	/**
	 * <p>Returns the UUID of this enemy</p>
	 * <p>This may end up being removed, doubt I'll be needing it.</p>
	 * 
	 * @return UUID The UUID of this enemy
	 */
	public UUID getUniqueID() {
		return this.UniqueID;
	}
	
	/**
	 * <p>Sets the UUID of the enemy</p>
	 * <p>This may end up being removed, doubt I'll be needing it.</p>
	 * 
	 * @param uuid The new UUID of the enemy
	 */
	public void setUniqueID(UUID uuid) {
		this.UniqueID = uuid;
	}
	
	/**
	 * <p>Returns whether or not this enemy is currently alive</p>
	 * @return boolean Whether or not the enemy is currently alive
	 */
	public boolean isAlive() {
		return this.isAlive;
	}
	
	/**
	 * <p>Returns the CollidableImageElement that this enemy is currently represented by</p>
	 * 
	 * @return CollidableImageElement The CollidableImageElement that the enemy is currently represented by
	 */
	public CollidableImageElement getImageElement() {
		return this.imageElement;
	}
	
	/**
	 * <p>Sets the CollidableImageElement that the enemy will be visually represented by</p>
	 * 
	 * @param imgElement The CollidableImageElement that the enemy will be visually represented by
	 */
	public void setImageElement(CollidableImageElement imgElement) {
		this.imageElement = imgElement;
	}
	
	/**
	 * <p>Returns the direction that the enemy is moving in</p>
	 * 
	 * @return MOVE_DIRECTION The direction that the player is moving in
	 */
	public MOVE_DIRECTION getMoveDirection() {
		return this.moveDirection;
	}
	
	/**
	 * <p>Sets the direction that the enemy is moving in</p>
	 * 
	 * @param moveD The direction the enemy is being set to move in
	 */
	public void setMoveDirection(MOVE_DIRECTION moveD) {
		this.moveDirection = moveD;
	}
	
	/**
	 * <p>Returns the coordinates of the enemy in Point format</p>
	 * 
	 * @return Point The position of the enemy
	 */
	public Point getPosition() {
		return new Point((int) imageElement.getOriginX(), (int) imageElement.getOriginY());
	}
	
	/**
	 * <p>Sets the isAlive status to false and removes image from game screen</p>
	 */
	public void kill() {
		this.isAlive = false;
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
}