package com.mojang.herpfortress.level;

import java.util.Random;

public class NoiseMap {
	private Random random;

	public NoiseMap() {
		this(new Random());
	}

	public NoiseMap(Random random) {
		this.random = random;
	}

	public int[] getNoise(long seed, int levels, int detailLevels) {
		random.setSeed(seed);
		int[] map = new int[1 * 1];

		for (int level = 0; level < levels; level++) {
			int size = (1 << level);
			int nextSize = 2 << level;
			int[] pixels = new int[nextSize * nextSize];

			int noise = 256 >> level;
			if (level < detailLevels) noise /= 20;
			int diagonalNoise = noise * 14 / 10;

			for (int y = 0; y < size; y++)
				for (int x = 0; x < size; x++) {
					int h0 = map[((x) + (y) * size) & (size * size - 1)];
					int h1 = map[((x + 1) + (y) * size) & (size * size - 1)];
					int h2 = map[((x) + (y + 1) * size) & (size * size - 1)];
					int h3 = map[((x + 1) + (y + 1) * size) & (size * size - 1)];
					pixels[(x * 2) + (y * 2) * nextSize] = h0;
					pixels[(x * 2 + 1) + (y * 2) * nextSize] = ((h1 + h0) >> 1) + random.nextInt(noise) - noise / 2;
					pixels[(x * 2) + (y * 2 + 1) * nextSize] = ((h2 + h0) >> 1) + random.nextInt(noise) - noise / 2;
					pixels[(x * 2 + 1) + (y * 2 + 1) * nextSize] = ((h0 + h1 + h2 + h3) >> 2) + random.nextInt(diagonalNoise) - diagonalNoise / 2;
				}
			map = pixels;
		}

		return map;
	}
}