package com.mojang.herpfortress.level;

import java.util.Random;

import com.mojang.herpfortress.Bitmap;

public class ShadowMask {
	private static final Random random = new Random();
	public Bitmap[] masks = new Bitmap[16 * 2];
	private int[] hm = new NoiseMap().getNoise(1019, 4, 1);

	public ShadowMask() {
		for (int i = 0; i < 16; i++) {
			masks[i] = generateMask(i);
			masks[i + 16] = generateMask2(i);
		}
	}

	private Bitmap generateMask(int corners) {
		int w = 24;
		Bitmap bm = new Bitmap(w, w);
		int a = (corners >> 0) & 1;
		int b = (corners >> 1) & 1;
		int c = (corners >> 2) & 1;
		int d = (corners >> 3) & 1;

		int rr = 0;
		int gg = 0;
		int bb = 0;

		for (int y = 0; y < w; y++) {
			for (int x = 0; x < w; x++) {
				double xx = ((x + 0.5) / 2.0 + y - 16) / 12.0;
				double yy = (y - (x + 0.5) / 2.0 - 4) / 12.0;
				rr = gg = bb = random.nextInt(200) / 199 * 10 + 10;

				if (xx >= 0 && yy >= 0 && xx < 1 && yy < 1) {
					double ab = a + (b - a) * xx;
					double cd = c + (d - c) * xx;
					double val = ab + (cd - ab) * yy;
					val = val * 4 - 0.4 + hm[(int) (xx * 8) + (int) (yy * 8) * 16] * 0.005;
					if (val < 0) val = 0;
					if (val > 1) val = 1;
					int aa = 255 - (int) (val * 255);
					if (corners == 0) aa = 255;
					bm.pixels[x + y * w] = aa << 24 | rr << 16 | gg << 8 | bb;
				}
			}
		}
		return bm;
	}

	private Bitmap generateMask2(int corners) {
		int w = 24;
		Bitmap bm = new Bitmap(w, w);
		int a = (corners >> 0) & 1;
		int b = (corners >> 1) & 1;
		int c = (corners >> 2) & 1;
		int d = (corners >> 3) & 1;
		for (int y = 0; y < w; y++) {
			for (int x = 0; x < w; x++) {
				double xx = ((x + 0.5) / 2.0 + y - 16) / 12.0;
				double yy = (y - (x + 0.5) / 2.0 - 4) / 12.0;

				if (xx >= 0 && yy >= 0 && xx < 1 && yy < 1) {
					double ab = a + (b - a) * xx;
					double cd = c + (d - c) * xx;
					double val = ab + (cd - ab) * yy;
					val = val * 4 - 0.5 + hm[(int) (xx * 8) + (int) (yy * 8) * 16] * 0.005;
					if (val < 0) val = 0;
					if (val > 1) val = 1;
					int col = (int) (val * 255);
					bm.pixels[x + y * w] = 0xff000000 | (col);
				}
			}
		}
		return bm;
	}
}