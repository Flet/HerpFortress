package com.mojang.herpfortress;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art {
	public static Art i;

	public static void init() {
		i = new Art();
	}

	public Bitmap[][] red = loadAndCut("/chars/red.png", 16, 16);
	public Bitmap[][] blu = loadAndCut("/chars/blu.png", 16, 16);
	public Bitmap[][] cursors = loadAndCut("/cursors.png", 16, 16);
	public Bitmap[][] particles = loadAndCut("/particles.png", 8, 8);
	public Bitmap[][] pickups = loadAndCut("/pickups.png", 8, 8);
	public Bitmap[][] missiles = loadAndCut("/missiles.png", 8, 8);
	public Bitmap[][] mapTiles = loadAndCut("/tiles.png", 24, 24);
	public Bitmap panel = load("/panel.png");

	public static Bitmap[][] loadAndCut(String name, int sw, int sh) {
		BufferedImage img;
		try {
			img = ImageIO.read(Art.class.getResource(name));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load " + name);
		}

		int xSlices = img.getWidth() / sw;
		int ySlices = img.getHeight() / sh;

		Bitmap[][] result = new Bitmap[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				result[x][y] = new Bitmap(sw, sh);
				img.getRGB(x * sw, y * sh, sw, sh, result[x][y].pixels, 0, sw);
			}
		}
		return result;
	}

	public static Bitmap load(String name) {
		BufferedImage img;
		try {
			img = ImageIO.read(Art.class.getResource(name));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load " + name);
		}

		int sw = img.getWidth();
		int sh = img.getHeight();

		Bitmap result = new Bitmap(sw, sh);
		img.getRGB(0, 0, sw, sh, result.pixels, 0, sw);

		return result;
	}

	public static Bitmap[][] recolor(Bitmap[][] bitmaps, int a0, int b0, int a1, int b1) {
		for (int x = 0; x < bitmaps.length; x++) {
			for (int y = 0; y < bitmaps[x].length; y++) {
				Bitmap bm = bitmaps[x][y];
				for (int i = 0; i < bm.pixels.length; i++) {
					if (bm.pixels[i] == a0) bm.pixels[i] = b0;
					if (bm.pixels[i] == a1) bm.pixels[i] = b1;
				}
			}
		}
		return bitmaps;
	}
}
