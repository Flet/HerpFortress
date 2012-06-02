package com.mojang.herpfortress.entity;

import java.util.List;

import com.mojang.herpfortress.*;

public class Entity extends Sprite {
	public int xBlockCell, yBlockCell;
	public double xa, ya, za;
	public boolean isCollideable = true;

	public boolean intersects(double x0, double y0, double z0, double x1, double y1, double z1) {
		if (x1 <= x - xr || x0 > x + xr || y1 <= y - yr || y0 > y + yr || z1 <= z || z0 > z + zh) return false;
		return true;
	}

	public boolean blocks(Entity e) {
		return true;
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
		b.fill(xp - 2, yp, xp + 1, yp, 1);
	}

	private void _move(double xxa, double yya, double zza) {
		if (removed) return;
		double xn = x + xxa;
		double yn = y + yya;
		double zn = z + zza;
		if (xn < 0 || yn < 0 || xn >= level.w * 16 || yn >= level.h * 16 || zn < 0 || zn > level.maxHeight) {
			if (zn < 0) z = 0;
			collide(null, xxa, yya, zza);
			return;
		}
		List<Entity> entities = level.getEntities(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e == this) continue;
			if (e.blocks(this) && blocks(e)) {
				collide(e, xxa, yya, zza);
				return;
			}
		}
		//		List<Wall> walls = level.getWalls(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh);
		if (level.wallBlocks(xn - xr, yn - yr, zn, xn + xr, yn + yr, zn + zh)) {
			collide(null, xxa, yya, zza);
			return;
		}
		//		for (int i = 0; i < walls.size(); i++) {
		//			Wall e = walls.get(i);
		//			collide(e, xxa, yya, zza);
		//			return;
		//		}
		x = xn;
		y = yn;
		z = zn;
		return;
	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (xxa != 0) xa = 0;
		if (yya != 0) ya = 0;
		if (zza != 0) za = 0;
	}

	public boolean blocksParticles() {
		return false;
	}

	public void hitBy(Bullet bullet) {
	}

	public double distanceToSqr(Entity e) {
		double xd = x - e.x;
		double yd = y - e.y;
		double zd = z - e.z;
		return xd * xd + yd * yd + zd * zd;
	}

	public void handleExplosion(Bullet source, int dmg, double xd, double yd, double zd) {
	}

	public void onRemoved() {
	}
}