package com.mojang.herpfortress;

import com.mojang.herpfortress.level.Level;

public class Sprite {
	public static final double SCALE_X = 12.0 / 16.0;
	public static final double SCALE_Y = 6.0 / 16.0;

	public boolean removed;

	public double x, y, z;
	public double xr = 2;
	public double yr = 2;
	public double zh = 5;

	public Level level;

	public void init(Level level) {
		this.level = level;
		init();
	}

	public void init() {
	}

	public void tick() {
	}

	public void remove() {
		removed = true;
	}

	public final void render(Bitmap b) {
		int xp = (int) (Math.floor((x - y) * SCALE_X));
		int yp = (int) (Math.floor((y + x) * SCALE_Y - z));
		render(b, xp, yp);
	}

	public void render(Bitmap b, int xp, int yp) {
	}

	public final void renderShadow(Bitmap b) {
		int xp = (int) (Math.floor((x - y) * SCALE_X));
		int yp = (int) (Math.floor((y + x) * SCALE_Y));
		renderShadow(b, xp, yp);
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
	}
}
