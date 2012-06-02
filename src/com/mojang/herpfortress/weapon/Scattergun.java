package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.entity.Bullet;
import com.mojang.herpfortress.unit.Mob;

public class Scattergun extends Weapon {
	public Scattergun(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 6;
		ammoCarried = maxAmmoCarried = 32;
		shootDelayTime = 0.64;
		startReloadDelayTime = 0.76;
		reloadDelayTime = 0.56;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		double spread = 0.05;
		for (int i = 0; i < 10; i++) {
			double xxa = xa + (random.nextDouble() - 0.5) * spread;
			double yya = ya + (random.nextDouble() - 0.5) * spread;
			double zza = za + (random.nextDouble() - 0.5) * spread * 0.5;
			owner.level.add(new Bullet(owner, this, xxa, yya, zza, 6));
			spread = 0.4;
		}
		shootDelay = shootDelayTime;
	}
}