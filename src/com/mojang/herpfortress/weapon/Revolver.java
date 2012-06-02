package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.entity.Bullet;
import com.mojang.herpfortress.unit.Mob;

public class Revolver extends Weapon {
	public Revolver(Mob owner) {
		super(owner);

		ammoLoaded = maxAmmoLoaded = 6;
		ammoCarried = maxAmmoCarried = 24;

		shootDelayTime = 0.58;
		startReloadDelayTime = 1.16;
		reloadDelayTime = 1.16;

		highRamp = 150;
		lowRamp = 52;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		double spread = 0.001;
		for (int i = 0; i < 1; i++) {
			double xxa = xa + (random.nextDouble() - 0.5) * spread;
			double yya = ya + (random.nextDouble() - 0.5) * spread;
			double zza = za + (random.nextDouble() - 0.5) * spread * 0.5;
			owner.level.add(new Bullet(owner, this, xxa, yya, zza, 40, 6));
		}
		shootDelay = shootDelayTime;
	}

	public void reload() {
		while (ammoLoaded < maxAmmoLoaded && ammoCarried > 0) {
			ammoLoaded++;
			ammoCarried--;
			if (!wasReloading) {
				reloadDelay = startReloadDelayTime;
			} else {
				reloadDelay = reloadDelayTime;
			}
		}
	}
}