package com.voracious.ld24.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.voracious.graphics.Game;
import com.voracious.graphics.InputHandler;
import com.voracious.graphics.components.Screen;

public class Play extends Screen {

	private static ArrayList<Integer> heightMap = new ArrayList<Integer>();
	private static int offsetX = 0;
	private static boolean[] keysDown = { false, false, false, false }; // w, a,
																		// s, d
	private Random rand = new Random();

	public Play(int width, int height) {
		super(width, height);
	}

	public void start() {
		InputHandler.register(this);
		generateLevel(1.0f);
	}

	public void stop() {
		InputHandler.unregister(this);
	}

	public void generateLevel(float difficulty) {
		int size = (int) (difficulty * 0xfff);

		int pitfall = rand.nextInt(size / 10);
		float[] map = generatePerlinNoise(generateWhiteNoise(size), 9);
		for (int i = 0; i < size; i++) {
			if (i == pitfall) {
				int pitfallSize = rand.nextInt(60);
				for (int j = 0; j < pitfallSize; j++) {
					heightMap.add(0);
				}
				i += pitfallSize;
				pitfall = rand.nextInt(size / 10) + i;
			} else {
				heightMap.add(Integer.valueOf((int) (map[i] * getHeight() / 2)));
			}
		}
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

		// normalisation
		for (int i = 0; i < width; i++) {
			perlinNoise[i] /= totalAmplitude;
		}

		return perlinNoise;
	}

	float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	public void render() {
		clear(0xffffff);
		for (int i = 0; i < this.getWidth(); i++) {
			int height = heightMap.get(i + offsetX);
			for (int j = 0; j < height; j++) {
				this.setPixel(0xff00ff, i, 149 - j);
				// System.out.println(j);
			}
		}
	}

	public void tick() {
		if (keysDown[1]) { // a
			if (offsetX > 5) {
				offsetX -= 5;
			}
		} else if (keysDown[3]) { // d
			if (offsetX < heightMap.size() - this.getWidth() - 5) {
				offsetX += 5;
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'w') {
			keysDown[0] = true;
		} else if (e.getKeyChar() == 'a') {
			keysDown[1] = true;
		} else if (e.getKeyChar() == 's') {
			keysDown[2] = true;
		} else if (e.getKeyChar() == 'd') {
			keysDown[3] = true;
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
}
