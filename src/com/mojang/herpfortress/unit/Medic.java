package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.entity.Entity;
import com.mojang.herpfortress.particle.MedigunBeam;
import com.mojang.herpfortress.weapon.MediGun;

public class Medic extends Mob {
	public Medic(Player player) {
		super(6, player);
		maxHealth = health = 150;
		speed = 106.67;

		weapon = new MediGun(this);
	}

	public boolean isLegalTarget(Mob u) {
		return u.team == team && u.health < u.maxHealth * 100 / 100;
	}

	public void shootAt(Entity target) {
		if (target instanceof Mob) {
			Mob u = (Mob) target;
			if (isLegalTarget(u)) {
				u.health++;
				level.add(new MedigunBeam(this, u));
				aimDir = dir;
				shootTime = 10;
			}
		}
	}
}