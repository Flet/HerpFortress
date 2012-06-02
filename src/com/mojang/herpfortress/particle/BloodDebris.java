package com.mojang.herpfortress.particle;

import com.mojang.herpfortress.Bitmap;

public class BloodDebris extends Debris {
	public BloodDebris(double x, double y, double z) {
		super(x, y, z);

		drag = 0.96;
		bounce = 0.1;
		icon = 1;
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
		b.setPixel(xp, yp, 1);
	}

	public void render(Bitmap b, int xp, int yp) {
		b.setPixel(xp, yp, 0xffa00000);
	}
}