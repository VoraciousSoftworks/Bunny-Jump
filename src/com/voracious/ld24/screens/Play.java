package com.voracious.ld24.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.voracious.graphics.Game;
import com.voracious.graphics.InputHandler;
import com.voracious.graphics.MouseHandler;
import com.voracious.graphics.components.Screen;
import com.voracious.graphics.components.Sprite;
import com.voracious.ld24.entities.Bomb;
import com.voracious.ld24.entities.Bunny;
import com.voracious.ld24.entities.EvilHawk;
import com.voracious.ld24.entities.Jet;
import com.voracious.ld24.entities.Collectable;

public class Play extends Screen {

	public static final double gravityPower = 0.4;
	public static final double terminalVelocity = 10.0;
	private static ArrayList<Integer> heightMap = new ArrayList<Integer>();
	private static int offsetX = 0;
	private static boolean[] keysDown = { false, false, false, false }; // w, a,
																		// s, d
	private Random rand = new Random();
	private Evolution evolution;
	private Bunny bunny = new Bunny();
	private Bunny femBunny = new Bunny();
	private Sprite spikes = new Sprite(5, 5, "/spikes.png");
	private boolean selectingStats = false;
	private ArrayList<EvilHawk> hawks = new ArrayList<EvilHawk>();
	private ArrayList<Jet> jets = new ArrayList<Jet>();
	private ArrayList<Collectable> collectables = new ArrayList<Collectable>();
	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();

	private int collectableSpawnRate = 100;
	private int EP = 0;

	public Play(int width, int height) {
		super(width, height);
		evolution = new Evolution(width, Game.HEIGHT, bunny, this);
	}

	public void start() {
		InputHandler.register(this);
		MouseHandler.register(evolution);
		generateLevel(1.0f);
		offsetX = 0;
		int highest = 0;
		for (int i = (int) bunny.getX() + offsetX; i < bunny.getWidth()
				+ (int) bunny.getX() + offsetX; i++) {
			if (highest < heightMap.get(i)) {
				highest = heightMap.get(i);
			}
		}
		bunny.setY(this.getHeight() - highest - bunny.getHeight());
		setOffsetY(this.getHeight() - Game.HEIGHT);
		// Game.getMusic("loop").play(true);
	}

	public void stop() {
		InputHandler.unregister(this);
	}

	public void generateLevel(float difficulty) {
		int size = (int) (difficulty * 0xfff);

		int pitfall = rand.nextInt(size / 10) + 50;
		int collect = rand.nextInt(size / collectableSpawnRate) + 20;
		int collectLevel = rand.nextInt(10);
		float[] map = generatePerlinNoise(generateWhiteNoise(size), 9);
		for (int i = 0; i < size; i++) {
			if (i == pitfall) {
				int pitfallSize = 25 + rand.nextInt(50 - 25);
				for (int j = 0; j < pitfallSize; j++) {
					heightMap.add(0);
				}
				i += pitfallSize - 1;
				pitfall = rand.nextInt(size / 10) + i - 50;
				continue;
			} else {
				heightMap.add(Integer
						.valueOf((int) (map[i] * Game.HEIGHT / 2) + 1));
			}
			
			// This block generates collectables psuedorandomly and randomly
			// picks their value
			// on a weighted scale.
			if (i >= collect) {
				if (collectLevel <= 5) {
					Collectable temp = new Collectable(1);
					temp.setX(i);
					temp.setY(rand.nextDouble()
							* (this.getHeight() - heightMap.get(i) + 3));
					collectables.add(temp);
					collect = rand.nextInt(size / collectableSpawnRate) + i
							- 20;
					collectLevel = rand.nextInt(10);
				} else if (collectLevel > 5 && collectLevel <= 8) {
					Collectable temp = new Collectable(2);
					temp.setX(i);
					temp.setY(rand.nextDouble()
							* (this.getHeight() - heightMap.get(i) + 3));
					collectables.add(temp);
					collect = rand.nextInt(size / collectableSpawnRate) + i
							- 20;
					collectLevel = rand.nextInt(10);
				} else if (collectLevel == 9) {
					Collectable temp = new Collectable(3);
					temp.setX(i);
					temp.setY(rand.nextDouble()
							* (this.getHeight() - heightMap.get(i) + 3));
					collectables.add(temp);
					collect = rand.nextInt(size / collectableSpawnRate) + i
							- 20;
					collectLevel = rand.nextInt(10);
				}
			}

		}

		femBunny.setX(this.getWidth() - femBunny.getWidth());
		femBunny.setFacingLeft(true);
		int highest = 0;
		for (int l = heightMap.size() - femBunny.getWidth(); l < heightMap
				.size(); l++) {
			if (highest < heightMap.get(l)) {
				highest = heightMap.get(l);
			}
		}
		femBunny.setY(this.getHeight() - highest - femBunny.getHeight());
	}

	public float[] generateWhiteNoise(int width) {
		float[] result = new float[width];

		for (int i = 0; i < width; i++) {
			result[i] = (float) rand.nextDouble() % 1;
		}

		return result;
	}

	float[] generateSmoothNoise(float[] baseNoise, int octave) {
		int width = baseNoise.length;

		float[] smoothNoise = new float[width];

		int samplePeriod = 1 << octave; // calculates 2 ^ k
		float sampleFrequency = 1.0f / samplePeriod;

		for (int j = 0; j < width; j++) {
			// calculate the vertical sampling indices
			int sample_j0 = (j / samplePeriod) * samplePeriod;
			int sample_j1 = (sample_j0 + samplePeriod) % width; // wrap around
			float vertical_blend = (j - sample_j0) * sampleFrequency;

			// blend the top two corners
			float top = Interpolate(baseNoise[sample_j0], baseNoise[sample_j0],
					vertical_blend);

			// blend the bottom two corners
			float bottom = Interpolate(baseNoise[sample_j1],
					baseNoise[sample_j1], vertical_blend);

			// final blend
			smoothNoise[j] = Interpolate(top, bottom, vertical_blend);
		}

		return smoothNoise;
	}

	float[] generatePerlinNoise(float[] baseNoise, int octaveCount) {
		int width = baseNoise.length;

		float[][] smoothNoise = new float[octaveCount][]; // an array of 2D
															// arrays containing

		float persistance = 0.5f;

		// generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		}

		float[] perlinNoise = new float[width];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		// blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				perlinNoise[i] += smoothNoise[octave][i] * amplitude;
			}
		}

		// Normalization
		for (int i = 0; i < width; i++) {
			perlinNoise[i] /= totalAmplitude;
		}

		return perlinNoise;
	}

	float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	public int interpolateColor(int col1, int col2, float alpha) {
		int r1 = (col1 & 0xff0000) / 0xffff;
		int g1 = (col1 & 0xff00) / 0xff;
		int b1 = col1 & 0xff;

		int r2 = (col2 & 0xff0000) / 0xffff;
		int g2 = (col2 & 0xff00) / 0xff;
		int b2 = col2 & 0xff;

		return ((int) Interpolate(r1, r2, alpha) * 0xffff)
				+ ((int) Interpolate(g1, g2, alpha) * 0xff)
				+ ((int) Interpolate(b1, b2, alpha));
	}

	public void render() {
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				int color = interpolateColor(0, 0x2b2baa,
						(float) j / this.getHeight());
				this.setPixel(color, i, j);
			}
		}

		if (selectingStats) {
			evolution.render();
			evolution.draw(this.getPixels());
		} else {
			int counter = 5;
			for (int i = 0; i < this.getWidth(); i++) {
				int height = heightMap.get(i + offsetX);
				for (int j = 0; j < height; j++) {
					this.setPixel(0x2bAA2b, i, this.getHeight() - j);
				}
				if (height == 0) {
					if (counter == 5) {
						spikes.draw(this, i,
								this.getHeight() - spikes.getHeight());
						counter = 0;
					}
					counter++;
				}
			}

			if (offsetX > heightMap.size() - this.getWidth()
					- femBunny.getWidth()) {
				femBunny.setX((this.getWidth() - femBunny.getWidth())
						+ (heightMap.size() - this.getWidth() - offsetX));
				femBunny.draw(this);
			}
			bunny.draw(this);

			for (Jet jet : jets) {
				jet.draw(this);
			}

			for (EvilHawk hawk : hawks) {
				hawk.draw(this);
			}

			for (Collectable collectable : collectables) {
				collectable.draw(this);
			}

			for (Bomb bomb : bombs) {
				bomb.draw(this);
			}
		}
	}

	public void tick() {
		if (selectingStats) {

		} else {
			if (keysDown[1]) { // a
				if (bunny.getX() > 65
						|| (offsetX == 0 && bunny.getX() - bunny.getMoveSpeed() > 0)) {
					bunny.setX(bunny.getX() - bunny.getMoveSpeed());
				} else if (offsetX - bunny.getMoveSpeed() > 0) {
					setOffsetX((int) (getOffsetX() - bunny.getMoveSpeed()));
				} else {
					offsetX = 0;
				}

				bunny.nextFrame();
			} else if (keysDown[3]) { // d
				if (bunny.getX() < this.getWidth() - bunny.getWidth() - 65
						|| (offsetX == heightMap.size() - this.getWidth() && bunny
								.getX() + bunny.getMoveSpeed() < this
								.getWidth() - bunny.getWidth())) {
					bunny.setX(bunny.getX() + bunny.getMoveSpeed());
				} else if (offsetX < heightMap.size() - this.getWidth()
						- bunny.getMoveSpeed()) {
					setOffsetX((int) (getOffsetX() + bunny.getMoveSpeed()));
				} else {
					setOffsetX(heightMap.size() - this.getWidth());
				}
				bunny.nextFrame();
			}

			if (bunny.getY() < getHeight() - Game.HEIGHT + 45) {
				setOffsetY((int) (getOffsetY() + bunny.getVelY()));
			} else if (bunny.getY() > Game.HEIGHT - 45 && bunny.getVelY() > 0) {
				if (getOffsetY() < getHeight() - Game.HEIGHT - bunny.getVelY()) {
					setOffsetY((int) (getOffsetY() + bunny.getVelY()));
				}
			}

			int highest = 0;
			for (int i = (int) bunny.getX() + offsetX; i < bunny.getWidth()
					+ (int) bunny.getX() + offsetX; i++) {
				if (highest < heightMap.get(i)) {
					highest = heightMap.get(i);
				}
			}

			bunny.tick();
			if (bunny.isFalling() == true
					&& bunny.getY() > getHeight() - highest - bunny.getHeight()
							- bunny.getVelY()) {
				if (getHeight() - highest - bunny.getHeight() - bunny.getY() > -5) {
					bunny.setFalling(false);
					bunny.setVelY(0.0);
				} else {
					if (heightMap.get((int) (bunny.getX() + offsetX + bunny
							.getMoveSpeed())) < heightMap
							.get((int) (bunny.getX() + offsetX - bunny
									.getMoveSpeed()) >= 0 ? (int) (bunny.getX()
									+ offsetX - bunny.getMoveSpeed()) : 0)) {
						bunny.setX(bunny.getX() + bunny.getMoveSpeed());
					} else {
						bunny.setX(bunny.getX() - bunny.getMoveSpeed());
					}
				}
			} else if (!bunny.isFalling()) {
				if (getHeight() - highest - bunny.getHeight() - bunny.getY() > 10) {
					bunny.setFalling(true);
				} else if (getHeight() - highest - bunny.getHeight()
						- bunny.getY() > -5) {
					bunny.setY(getHeight() - highest - bunny.getHeight());
				} else {
					if (heightMap.get((int) (bunny.getX() + offsetX + bunny
							.getMoveSpeed())) < heightMap
							.get((int) (bunny.getX() + offsetX - bunny
									.getMoveSpeed()) >= 0 ? (int) (bunny.getX()
									+ offsetX - bunny.getMoveSpeed()) : 0)) {
						bunny.setX(bunny.getX() + bunny.getMoveSpeed());
					} else {
						bunny.setX(bunny.getX() - bunny.getMoveSpeed());
					}
				}
			}

			if (bunny.getY() >= this.getHeight() - bunny.getHeight()) {
				endRun();
			}

			ArrayList<Collectable> collectsToRemove = new ArrayList<Collectable>();
			for (Collectable collect : collectables) {
				if (bunny.hitTest(collect)) {
					EP += collect.getValue();
					collectsToRemove.add(collect);
				}
			}

			for (Collectable collect : collectsToRemove) {
				collectables.remove(collect);
			}

			if (rand.nextInt(heightMap.size()) < Math.sqrt(offsetX) * 2) {
				if (offsetX > heightMap.size() / 2 && rand.nextInt(3) < 2) {
					jets.add(new Jet(this, bombs));
				} else {
					hawks.add(new EvilHawk(this));
				}
			}

			ArrayList<Jet> jetsToRemove = new ArrayList<Jet>();
			for (Jet jet : jets) {
				jet.tick();
				if (jet.getX() < getOffsetX() * -1) {
					jetsToRemove.add(jet);
				}else if(jet.hitTest(bunny)){
					jetsToRemove.add(jet);
					bunny.setVelY(bunny.getVelY() - 15.0);
					this.setEP(this.getEP() + 10);
				}
			}

			for (Jet jet : jetsToRemove) {
				jets.remove(jet);
			}

			ArrayList<EvilHawk> hawksToRemove = new ArrayList<EvilHawk>();
			for (EvilHawk hawk : hawks) {
				hawk.tick();
				if (hawk.getX() < getOffsetX() * -1) {
					hawksToRemove.add(hawk);
				}else if(hawk.hitTest(bunny)){
					hawksToRemove.add(hawk);
					bunny.setVelY(bunny.getVelY() - 10.0);
					this.setEP(this.getEP() + 5);
				}
			}

			for (EvilHawk hawk : hawksToRemove) {
				hawks.remove(hawk);
			}

			for (Collectable collectable : collectables) {
				collectable.tick();
			}
			
			ArrayList<Bomb> bombsToRemove = new ArrayList<Bomb>();
			for (Bomb bomb : bombs) {
				bomb.tick();
				if (bomb.getX() > 0 && bomb.getX() < heightMap.size()) {
					if (bomb.getY() > this.getHeight()
							- heightMap.get((int) (getOffsetX() + bomb.getX()))) {
						bombsToRemove.add(bomb);
						
						int startX = getOffsetX() + (int) bomb.getX()
								- Bomb.explosionSize / 2;
						for (int i = startX; i < startX + Bomb.explosionSize; i++) {
							int ammountToDestroy = (int) Math
									.sqrt((Bomb.explosionSize / 2)
											* (Bomb.explosionSize / 2)
											- ((i - startX) - (Bomb.explosionSize / 2))
											* ((i - startX) - (Bomb.explosionSize / 2)));
							int newHeight = heightMap.get(i) - ammountToDestroy;
							if (newHeight < 0)
								newHeight = 0;
							heightMap.set(i, newHeight);
						}
						
						//if bomb hit bunny, shoot it into the air
						if(bunny.getX() > bomb.getX() - Bomb.explosionSize/2 && bunny.getX() < bomb.getX() + Bomb.explosionSize/2){
							bunny.setVelY(bunny.getVelY() - Bomb.explosionSize*1/(Math.abs(bunny.getX() - bomb.getX())));
							this.setEP(this.getEP() + 25);
						}
					}
				} else {
					bombsToRemove.add(bomb);
				}
			}

			for (Bomb bomb : bombsToRemove) {
				bombs.remove(bomb);
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'w') {
			keysDown[0] = true;
			if (!bunny.isFalling()) {
				bunny.jump();
			}
		} else if (e.getKeyChar() == 'a') {
			keysDown[1] = true;
			bunny.setFacingLeft(true);
		} else if (e.getKeyChar() == 's') {
			keysDown[2] = true;
		} else if (e.getKeyChar() == 'd') {
			keysDown[3] = true;
			bunny.setFacingLeft(false);
		} else if (e.getKeyChar() == 'k') {
			endRun();
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == 'w') {
			keysDown[0] = false;
		} else if (e.getKeyChar() == 'a') {
			keysDown[1] = false;
		} else if (e.getKeyChar() == 's') {
			keysDown[2] = false;
		} else if (e.getKeyChar() == 'd') {
			keysDown[3] = false;
		}
	}

	public void endRun() {
		setSelectingStats(true);
		bunny.setX(0);
		offsetX = 0;
		heightMap.clear();
		hawks.clear();
		jets.clear();
		collectables.clear();
		bombs.clear();
		generateLevel(1.0f);
		int highest = 0;
		for (int i = 0; i < bunny.getWidth(); i++) {
			if (highest < heightMap.get(i)) {
				highest = heightMap.get(i);
			}
		}
		bunny.setY(this.getHeight() - highest - bunny.getHeight() - 10);
	}

	public void setSelectingStats(boolean selecting) {
		selectingStats = selecting;
	}

	public boolean isSelectingStats() {
		return selectingStats;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offset) {
		for (EvilHawk hawk : hawks) {
			hawk.setX(hawk.getX() + offsetX - offset);
		}
		for (Jet jet : jets) {
			jet.setX(jet.getX() + offsetX - offset);
		}
		for (Collectable collect : collectables) {
			collect.setX(collect.getX() + offsetX - offset);
		}

		offsetX = offset;
	}

	public int getEP() {
		return EP;
	}

	public void setEP(int p) {
		EP = p;
	}
}
