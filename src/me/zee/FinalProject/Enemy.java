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
	
	public Enemy(CollidableImageElement imgElement, Game game) {
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
	
	public void kill() {
		setIsAlive(false);
		game.gameScreen.removeElement(imageElement);
	}
}