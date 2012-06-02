package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.entity.Bullet;
import com.mojang.herpfortress.unit.Mob;

public class SniperRifle extends Weapon {
	public SniperRifle(Mob owner) {
		super(owner);

		ammoLoaded = maxAmmoLoaded = 25;
		ammoCarried = maxAmmoCarried = 0;
		maxRange = 250;

		shootDelayTime = 1.5;
		startReloadDelayTime = 1.0;
		reloadDelayTime = 0.5;

		highRamp = 100;
		lowRamp = 100;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		double spread = 0.001;
		for (int i = 0; i < 1; i++) {
			double xxa = xa + (random.nextDouble() - 0.5) * spread;
			double yya = ya + (random.nextDouble() - 0.5) * spread;
			double zza = za + (random.nextDouble() - 0.5) * spread * 0.5;
			owner.level.add(new Bullet(owner, this, xxa, yya, zza, 50, 16));
		}
		shootDelay = shootDelayTime;
	}
}