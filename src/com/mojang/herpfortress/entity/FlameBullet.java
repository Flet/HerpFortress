package com.mojang.herpfortress.entity;

import com.mojang.herpfortress.Bitmap;
import com.mojang.herpfortress.particle.FlameDebris;
import com.mojang.herpfortress.unit.*;
import com.mojang.herpfortress.weapon.Weapon;

public class FlameBullet extends Bullet {
	private int life = 0;

	public FlameBullet(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg) {
		super(owner, weapon, xa, ya, za, dmg);
		this.xa = xa * 0.5;
		this.ya = ya * 0.5;
		this.za = za * 0.5;
		life = 45;
	}

	public boolean blocks(Entity e) {
		if (e == owner) return false;
		if (e instanceof Bullet) return false;
		return true;
	}

	public void tick() {
		xo = x;
		yo = y;
		zo = z;

		if (life-- <= 0) {
			remove();
			return;
		}

		FlameDebris flame = new FlameDebris(x - xa * 2, y - ya * 2, z - za * 2);
		flame.xa *= 0.5;
		flame.ya *= 0.5;
		flame.za *= 0.5;
		flame.xa += xa * 1.5;
		flame.ya += ya * 1.5;
		flame.za += za * 1.5;
		flame.life = flame.maxLife / 2;
		flame.noSmoke = true;
		level.add(flame);

		super.tick();
		attemptMove();
	}

	public void render(Bitmap b, int xp, int yp) {
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (e != null) {
			e.hitBy(this);
		}
		remove();
	}

	public void applyHitEffect(Unit unit) {
		unit.burnTime = 60 * 10;
	}
}
