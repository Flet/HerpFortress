package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.Minigun;

public class Heavy extends Mob {
	public Heavy(Player player) {
		super(4, player);
		maxHealth = health = 300;
		speed = 77;

		weapon = new Minigun(this);
	}
}