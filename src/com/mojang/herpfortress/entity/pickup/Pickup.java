package com.mojang.herpfortress.entity.pickup;

import com.mojang.herpfortress.*;
import com.mojang.herpfortress.entity.Entity;
import com.mojang.herpfortress.unit.*;

public class Pickup extends Entity {
	public int takeTime;
	public int icon = 0;
	public int time = 0;
	public int size;

	public Pickup() {
	}

	public boolean isNeededBy(Unit u) {
		return false;
	}

	public void init() {
		super.init();
		time = level.random.nextInt(62);
	}

	public void tick() {
		if (takeTime > 0) {
			takeTime--;
			time = level.random.nextInt(62);
			return;
		}

		time++;

		double r = 3;
		for (Entity e : level.getEntities(x - r, y - r, z - r, x + r, y + r, z + r)) {
			if (e instanceof Mob && e != this) {
				Mob u = (Mob) e;
				if (u.isAlive()) {
					if (take(u)) {
						takeTime = 10 * 60;
					}
				}
			}
		}

		z = Math.cos(time / 10.0) * 1.5 + 4;
	}

	public boolean blocks(Entity e) {
		return false;
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
		if (isTaken()) return;
		b.fill(xp - 1 - size, yp, xp + 1 + size, yp, 1);
	}

	public void render(Bitmap b, int xp, int yp) {
		if (isTaken()) return;
		b.draw(getBitmap(), xp - 4, yp - 5);
	}

	public Bitmap getBitmap() {
		return Art.i.pickups[icon % 8][icon / 8];
	}

	public boolean take(Mob u) {
		return false;
	}

	public boolean isTaken() {
		return takeTime > 0;
	}
}