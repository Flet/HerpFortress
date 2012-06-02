package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.RocketLauncher;

public class Soldier extends Mob {
	public Soldier(Player player) {
		super(1, player);
		maxHealth = health = 200;
		speed = 80;

		weapon = new RocketLauncher(this);
	}
}