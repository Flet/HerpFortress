package com.mojang.herpfortress.weapon;

import java.util.*;

import com.mojang.herpfortress.entity.StickyBomb;
import com.mojang.herpfortress.unit.Mob;

public class StickyBombLauncher extends Weapon {
	private List<StickyBomb> bombs = new ArrayList<StickyBomb>();

	public StickyBombLauncher(Mob owner) {
		super(owner);
		ammoLoaded = maxAmmoLoaded = 8;
		ammoCarried = maxAmmoCarried = 24;

		shootDelayTime = 0.6;
		startReloadDelayTime = 1.09;
		reloadDelayTime = 0.67;
		aimLead = 0;

		highRamp = 125;
		lowRamp = 53;
	}

	public void shoot(double xa, double ya, double za) {
		super.shoot(xa, ya, za);
		for (int i = 0; i < bombs.size(); i++) {
			if (bombs.get(i).removed) bombs.remove(i--);
		}
		if (bombs.size() == 8) {
			bombs.remove(0).detonate();
		}
		StickyBomb bomb = new StickyBomb(owner, this, xa, ya, za, 90);
		bombs.add(bomb);
		owner.level.add(bomb);
		shootDelay = shootDelayTime;
	}

	public void playerDied() {
		for (int i = 0; i < bombs.size(); i++) {
			if (!bombs.get(i).removed) bombs.remove(i--).fizzle();
		}
	}
}