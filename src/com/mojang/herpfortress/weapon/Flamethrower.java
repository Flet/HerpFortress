package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.entity.FlameBullet;
import com.mojang.herpfortress.unit.Mob;

public class Flamethrower extends Weapon {
	public Flamethrower(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 200;
		ammoCarried = maxAmmoCarried = 0;
		shootDelayTime = 0.04;
		startReloadDelayTime = 0.0;
		reloadDelayTime = 0.0;
		maxRange = 60;
		aimLead = 2;

		highRamp = 100;
		lowRamp = 60;
		midDistance = 200 / 2;
		farDistance = 384 / 2;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		owner.level.add(new FlameBullet(owner, this, xa, ya, za, 6));
		shootDelay = shootDelayTime;
	}
}