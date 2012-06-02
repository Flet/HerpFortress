package com.mojang.herpfortress.particle;

import java.util.Random;

import com.mojang.herpfortress.Bitmap;

public class Explosion extends Particle {
	public static final Random random = new Random();
	public int life, maxLife;

	public Explosion(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		maxLife = life = 5;
	}

	public void tick() {
		super.tick();
		if (--life < 0) {
			remove();
			return;
		}

		int ps = (life) * 40 / maxLife + 1;
		double dd = (maxLife - life) / (double) maxLife + 0.2;
		for (int i = 0; i < ps; i++) {
			double dir = random.nextDouble() * Math.PI * 2;
			double dist = random.nextDouble() * 6 * dd;
			double xx = x + Math.cos(dir) * dist;
			double yy = y + Math.sin(dir) * dist;
			double zz = z + random.nextDouble() * 10;

			FlameDebris fd = new FlameDebris(xx, yy, zz);
			if (random.nextInt(2) == 0) fd.life = fd.maxLife / 2;
			fd.xa *= 0.1;
			fd.ya *= 0.1;
			fd.za *= 0.1;
			fd.xa += (xx - x) * 0.5;
			fd.ya += (yy - y) * 0.5;
			fd.gravity = 0.10;
			level.add(fd);
		}
	}

	public void render(Bitmap b, int xp, int yp) {
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
	}
}