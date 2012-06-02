package com.mojang.herpfortress.weapon;

import com.mojang.herpfortress.unit.Mob;

public class MediGun extends Weapon {
	public MediGun(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 0;
		ammoCarried = maxAmmoCarried = 0;
		shootDelayTime = 1.0/25.0f;
		startReloadDelayTime = 0.0;
		reloadDelayTime = 0.0;
		maxRange = 60;
		aimLead = 2;

		highRamp = 100;
		lowRamp = 100;
	}

	public void shoot(double xa, double ya, double za) {
	}
}