package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.Scattergun;

public class Scout extends Mob {
	public Scout(Player player) {
		super(0, player);
		maxHealth = health = 125;
		speed = 133;
		visRange = 10;

		weapon = new Scattergun(this);
	}
}