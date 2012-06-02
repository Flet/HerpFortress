package com.mojang.herpfortress.particle;

import java.util.*;

import com.mojang.herpfortress.*;
import com.mojang.herpfortress.entity.Entity;

public class Debris extends Particle {
	public static final Random random = new Random();
	public double xa, ya, za;
	public int life;
	public double drag = 0.998;
	public double bounce = 0.6;
	public int icon = 0;
	public double gravity = 0.08;

	public Debris(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		life = random.nextInt(20) + 40;

		do {
			xa = random.nextDouble() * 2 - 1;
			ya = random.nextDouble() * 2 - 1;
			za = random.nextDouble() * 2 - 1;
		} while (xa * xa + ya * ya + za * za > 1);
		double dd = Math.sqrt(xa * xa + ya * ya + za * za);
		double speed = 1;
		xa = xa / dd * speed;
		ya = (ya / dd * speed);
		za = (za / dd + 1.0) * speed;
	}

	public void tick() {
		super.tick();
		if (--life < 0) {
			remove();
			return;
		}

		boolean onGround = z <= 1;
		if (onGround) {
			xa *= 0.85;
			ya *= 0.85;
		} else {
			xa *= drag;
			ya *= drag;
		}
		za -= gravity;
		attemptMove();
	}

	public void render(Bitmap b, int xp, int yp) {
		b.draw(getBitmap(), xp - 4, yp - 4);
	}

	public Bitmap getBitmap() {
		return Art.i.particles[icon % 8][icon / 8];
	}

	public void attemptMove() {
		int steps = (int) (Math.sqrt(xa * xa + ya * ya + za * za) + 1);

		for (int i = 0; i < steps; i++) {
			_move(xa / steps, 0, 0);
			_move(0, ya / steps, 0);
			_move(0, 0, za / steps);
		}
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
		b.fill(xp - 1, yp, xp, yp, 1);
	}

	private void _move(double xxa, double yya, double zza) {
		if (removed) return;
		double xn = x + xxa;
		double yn = y + yya;
		double zn = z + zza;
		if (xn < 0 || yn < 0 || xn >= level.w*16 || yn >= level.h*16 || zn < 0 || zn > level.maxHeight) {
			if (zn < 0) z = 0;
			collide(null, xxa, yya, zza);
			return;
		}
		if (level.wallBlocks(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh)) {
			collide(null, xxa, yya, zza);
			return;
		}
		/*		List<Entity> entities = level.getEntities(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh);
				for (int i = 0; i < entities.size(); i++) {
					Entity e = entities.get(i);
					if (e.blocksParticles()) {
						collide(e, xxa, yya, zza);
						return;
					}
				}*/
		x = xn;
		y = yn;
		z = zn;
		return;
	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (xxa != 0) xa *= -bounce;
		if (yya != 0) ya *= -bounce;
		if (zza != 0) za *= -bounce;
	}
}