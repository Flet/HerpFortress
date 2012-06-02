package com.mojang.herpfortress.particle;

import com.mojang.herpfortress.Bitmap;

public class HealDebris extends Debris {
	public int lifeTime;
	public int color = 0;

	public HealDebris(double x, double y, double z, int color) {
		super(x, y, z);

		lifeTime = life /= 2;

		drag = 0.92;
		icon = 8 * 3 + color;
		gravity = -0.00;
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
	}

	public void render(Bitmap b, int xp, int yp) {
		b.draw(getBitmap(), xp - 4, yp - 4);
	}
}