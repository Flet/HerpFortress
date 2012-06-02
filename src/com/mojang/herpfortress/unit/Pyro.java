package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.Flamethrower;

public class Pyro extends Mob {
	public Pyro(Player player) {
		super(2, player);
		maxHealth = health = 175;
		speed = 100;

		weapon = new Flamethrower(this);
	}

	public void tick() {
		if (burnTime > 0) burnTime = 0;
		super.tick();
	}
}