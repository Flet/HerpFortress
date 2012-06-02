package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.entity.Bullet;
import com.mojang.herpfortress.unit.Mob;

public class Minigun extends Weapon {
	public Minigun(Mob owner) {
		super(owner);

		ammoLoaded = maxAmmoLoaded = 200 * 4;
		ammoCarried = maxAmmoCarried = 0;
		shootDelayTime = 0.1 / 4;
		startReloadDelayTime = 0.0;
		reloadDelayTime = 0.0;

		highRamp = 150;
		lowRamp = 50;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		double spread = 0.1;
		for (int i = 0; i < 1; i++) {
			double xxa = xa + (random.nextDouble() - 0.5) * spread;
			double yya = ya + (random.nextDouble() - 0.5) * spread;
			double zza = za + (random.nextDouble() - 0.5) * spread * 0.5;
			owner.level.add(new Bullet(owner, this, xxa, yya, zza, 9));
		}
		shootDelay = shootDelayTime;
	}
}