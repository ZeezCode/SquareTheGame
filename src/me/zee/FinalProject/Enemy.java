package me.zee.FinalProject;

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
	 * <p>Sets the isAlive status to false and removes image from game screen</p>
	 */
	public void kill() {
		this.isAlive = false;
		game.gameScreen.removeElement(imageElement);
	}
}