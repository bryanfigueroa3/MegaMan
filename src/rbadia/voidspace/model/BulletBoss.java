package rbadia.voidspace.model;
import java.awt.Rectangle;

/**
 * Represents a bullet fired by a ship.
 */
public class BulletBoss extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	private int bulletBossWidth = 8;
	private int bulletBossHeight = 8;
	private int speed = 12;

	
	/**
	 * Creates a new bullet above the ship, centered on it
	 * @param ship
	 */	
	public BulletBoss(EnemyShip enemy){
		this.setLocation(enemy.x + enemy.width - bulletBossWidth/2,
				enemy.y + enemy.width/2 - bulletBossHeight +2);
		this.setSize(bulletBossWidth, bulletBossHeight);
	}
	

	/**
	 * Return the bullet's speed.
	 * @return the bullet's speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Set the bullet's speed
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
