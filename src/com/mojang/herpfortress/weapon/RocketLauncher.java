package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.entity.Rocket;
import com.mojang.herpfortress.unit.Mob;

public class RocketLauncher extends Weapon {
	public RocketLauncher(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 4;
		ammoCarried = maxAmmoCarried = 20;
		shootDelayTime = 0.8;
		startReloadDelayTime = 0.8;
		reloadDelayTime = 0.92;
		aimAtGround = true;
		aimLead = 2;

		highRamp = 125;
		lowRamp = 53;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		owner.level.add(new Rocket(owner, this, xa, ya, za, 90));
		shootDelay = shootDelayTime;
	}
}