package rbadia.voidspace.main;


import java.awt.event.ActionEvent;



import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.BulletEnemy;
import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.model.EnemyShip;
import rbadia.voidspace.sounds.SoundManager;


/**
 * Handles general game logic and status.
 */
public class GameLogic {
	private GameScreen gameScreen;
	private GameScreen2 gameScreen2;
	private GameStatus status;
	private SoundManager soundMan;

	private MegaMan megaMan;
	private Boss boss;

	private EnemyShip enemy;
	private EnemyShip boss2;
	private Asteroid asteroid;
	private Asteroid asteroid2;
	private BigAsteroid bigAsteroid;
	private List<Bullet> bullets;
	private List<BulletEnemy> bulletsBoss;
	private List<BulletBoss2> bulletsBoss2;
	private List<BigBullet> bigBullets;
	private List<BulletEnemy> enemyBullet;

	private Platform[] numPlatforms;
	private Floor[] floor;

	private long lastBulletTime;

	/**
	 * Create a new game logic handler
	 * @param gameScreen the game screen
	 */
	public GameLogic(GameScreen gameScreen){
		this.gameScreen = gameScreen;

		// initialize game status information
		status = new GameStatus();
		// initialize the sound manager
		soundMan = new SoundManager();

		// init some variables
		bullets = new ArrayList<Bullet>();
		enemyBullet = new ArrayList<BulletEnemy>();
		bulletsBoss2 = new ArrayList<BulletBoss2>();
		bigBullets = new ArrayList<BigBullet>();
		
	//	asteroid = new ArrayList<Asteroid>();
	}

	/**
	 * Returns the game status
	 * @return the game status 
	 */
	public GameStatus getStatus() {
		return status;
	}

	public SoundManager getSoundMan() {
		return soundMan;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * Prepare for a new game.
	 */
	public void newGame(){
		status.setGameStarting(true);

		// init game variables
		bullets = new ArrayList<Bullet>();
		enemyBullet = new ArrayList<BulletEnemy>();
		bulletsBoss2 = new ArrayList<BulletBoss2>();
		bigBullets = new ArrayList<BigBullet>();
	//	asteroid = new ArrayList<Asteroid>();
		//numPlatforms = new Platform[5];

		status.setShipsLeft(3);
		status.setLevel(1);
		status.setGameOver(false);
		status.setAsteroidsDestroyed(0);
		status.setNewAsteroid(false);
		status.setNewAsteroid2(false);
		status.setNewBigAsteroid(false);
		//status.setNewFloor(false);

		// init the ship and the asteroid
		newMegaMan(gameScreen);
		newFloor(gameScreen, 9);

		newNumPlatforms(gameScreen, 8);

		//        newPlatform(gameScreen/*, 1*/);
		//        newPlatform1(gameScreen);
		newEnemy(gameScreen);
		newBoss(gameScreen);
		newBoss2(gameScreen);
		newAsteroid(gameScreen);
		newAsteroid2(gameScreen);
		newBigAsteroid(gameScreen);

		// prepare game screen
		gameScreen.doNewGame();

		// delay to display "Get Ready" message for 1.5 seconds
		Timer timer = new Timer(1500, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameStarting(false);
				status.setGameStarted(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Check game or level ending conditions.
	 */
	public void checkConditions(){
		// check game over conditions
		if(!status.isGameOver() && status.isGameStarted()){
			if(status.getShipsLeft() == 0){
				gameOver();
			}
		}
		if(!status.isGameWon()){
			if(gameScreen.getBoom() == 2)
				gameWon();
		}
		if(!status.isGameWon()){
			if(gameScreen.getBoom() == 8)
				gameWon();
		}
		
	}

	/**
	 * Actions to take when the game is over.
	 */
	public void gameOver(){
		status.setGameStarted(false);
		status.setGameOver(true);
		gameScreen.doGameOver();

		// delay to display "Game Over" message for 3 seconds
		Timer timer = new Timer(5000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameOver(false);
			}
		});
		timer.setRepeats(false);
		timer.start();

		//Change music back to menu screen music
		VoidSpaceMain.audioClip.close();
		VoidSpaceMain.audioFile = new File("audio/menuScreen.wav");
		try {
			VoidSpaceMain.audioStream = AudioSystem.getAudioInputStream(VoidSpaceMain.audioFile);
			VoidSpaceMain.audioClip.open(VoidSpaceMain.audioStream);
			VoidSpaceMain.audioClip.start();
			VoidSpaceMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Actions to take if game is won.
	 */

	//GAME LOOPS ON THE FIRST GAMESCREEN AND RESETS ALL VARIABLE COUNTERS
	public void gameWon(){
		//status.setGameStarted(false);  //SENDS TO MAIN SCREEN/ IF COMMENTED OUT LOOPS THE GAME
		status.setGameWon(true);
		gameScreen.doGameOver();

		// delay to display "Game Won" message for 3 seconds
		Timer timer = new Timer(3000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameWon(false);
			}
		});
		timer.setRepeats(false);
		timer.start();

		//Change music back to menu screen music
		VoidSpaceMain.audioClip.close();
		VoidSpaceMain.audioFile = new File("audio/menuScreen.wav");
		try {
			VoidSpaceMain.audioStream = AudioSystem.getAudioInputStream(VoidSpaceMain.audioFile);
			VoidSpaceMain.audioClip.open(VoidSpaceMain.audioStream);
			VoidSpaceMain.audioClip.start();
			VoidSpaceMain.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Fire a bullet from ship.
	 */
	public void fireBullet(){
		Bullet bullet = new Bullet(megaMan);
		bullets.add(bullet);
		soundMan.playBulletSound();
	}

	/**
	 * Fire the "Power Shot" bullet
	 */
	public void fireBigBullet(){
		BigBullet bigBullet = new BigBullet(megaMan);
		bigBullets.add(bigBullet);
		soundMan.playBulletSound();
	}
	public void fireEnemyBullet(){
		int boom = gameScreen.getBoom();
		if(!status.isNewEnemy()&& boom>2 && boom<8){
		BulletEnemy bullet=new BulletEnemy(enemy);
		enemyBullet.add(bullet);
		soundMan.playBulletSound();
		
		}
	}
	public void fireBossBullet(){
		int boom = gameScreen.getBoom();
		if(!status.isNewBoss() && boom>8){
		BulletBoss2 bullet=new BulletBoss2(enemy);
		bulletsBoss2.add(bullet);
		
		
		}
	}

	/**
	 * Move a bullet once fired from the ship.
	 * @param bullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBullet(Bullet bullet){
		if(bullet.getY()+bullet.width - bullet.getSpeed() < gameScreen.getHeight()){
			bullet.translate(bullet.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Move a bullet once fired from the boss.
	 * @param bulleEnemy the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBulletEnemy(BulletEnemy bulleEnemy){
		if(bulleEnemy.getY()+bulleEnemy.width-bulleEnemy.getSpeed()<gameScreen.getWidth()){
			bulleEnemy.translate(-bulleEnemy.getSpeed(),0);
			return false;
		}
		else{
			return true;
		}
	}

	/** Move a bullet once fired from the second boss.
	 * @param bulletBoss2 the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBulletBoss2(BulletBoss2 bulletBoss2){
		if(bulletBoss2.getY()+bulletBoss2.width + bulletBoss2.getSpeed()< gameScreen.getHeight()){
			bulletBoss2.translate(-bulletBoss2.getSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}

	/** Move a "Power Shot" bullet once fired from the ship.
	 * @param bulletBoss2 the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBigBullet(BigBullet bigBullet){
		if(bigBullet.getY() - bigBullet.getBigSpeed() >= 0){
			bigBullet.translate(bigBullet.getBigSpeed(), 0);
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Create a new ship (and replace current one).
	 */
	public MegaMan newMegaMan(GameScreen screen){
		this.megaMan = new MegaMan(screen);
		return megaMan;
	}
	public Boss newBoss(GameScreen screen){
		this.boss = new Boss(screen);
		return boss;
	}

	public Floor[] newFloor(GameScreen screen, int n){
		floor = new Floor[n];
		for(int i=0; i<n; i++){
			this.floor[i] = new Floor(screen, i);
		}

		return floor;
	}

	public Platform[] newNumPlatforms(GameScreen screen, int n){
		numPlatforms = new Platform[n];
		for(int i=0; i<n; i++){
			this.numPlatforms[i] = new Platform(screen, i);
		}
		return numPlatforms;

	}


	/**
	 * Create the first enemy.
	 */
	public EnemyShip newEnemy(GameScreen screen){
		this.enemy = new EnemyShip(screen);
		return enemy;
	}

	/**
	 * Create the second boss.
	 */
	public EnemyShip newBoss2(GameScreen screen){
		this.boss2 = new EnemyShip(screen);
		return boss2;
	}

	/**
	 * Create a new asteroid.
	 */
	public Asteroid newAsteroid(GameScreen screen){
		this.asteroid = new Asteroid(screen);
		
		return asteroid;
	}

	/**
	 * Create a second asteroid.
	 */
	public Asteroid newAsteroid2(GameScreen screen){
		this.asteroid2 = new Asteroid(screen);
		return asteroid2;
	}

	/**
	 * Create a new big asteroid.
	 */
	public BigAsteroid newBigAsteroid(GameScreen screen){
		this.bigAsteroid = new BigAsteroid(screen);
		return bigAsteroid;
	}

	/**
	 * Returns the ship.
	 * @return the ship
	 */
	public MegaMan getMegaMan() {
		return megaMan;
	}
	public Boss getBoss(){
		return boss;
	}
	public Floor[] getFloor(){
		return floor;	
	}

	public Platform[] getNumPlatforms(){
		return numPlatforms;
	}

	public EnemyShip getEnemy() {
		return enemy;
	}

	public EnemyShip getBoss2() {
		return boss2;
	}

	/**
	 * Returns the asteroid.
	 * @return the asteroid
	 */
	public Asteroid getAsteroid() {
		return asteroid;
	}

	public Asteroid getAsteroid2() {
		return asteroid2;
	}

	public BigAsteroid getBigAsteroid() {
		return bigAsteroid;
	}

	/**
	 * Returns the list of bullets.
	 * @return the list of bullets
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * Returns the list of the boss's bullets.
	 * @return the list of the boss's bullets
	 */
	public List<BulletEnemy> getEnemyBullet() {
		return enemyBullet;
	}

	/**
	 * Returns the list of the second boss's bullets.
	 * @return the list of the second boss's bullets
	 */
	public List<BulletBoss2> getBulletBoss2() {
		return bulletsBoss2;
	}

	/**
	 * Returns the list of "Power Shot" bullets.
	 * @return the list of "Power Shot" bullets
	 */
	public List<BigBullet> getBigBullets(){
		return bigBullets;
	}
}