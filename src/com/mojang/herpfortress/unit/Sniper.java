package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.SniperRifle;

public class Sniper extends Mob {
	public Sniper(Player player) {
		super(7, player);
		maxHealth = health = 125;
		speed = 100;
		visRange = 12;

		weapon = new SniperRifle(this);
	}
}