package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.StickyBombLauncher;

public class Demoman extends Mob {
	public Demoman(Player player) {
		super(3, player);
		maxHealth = health = 175;
		speed = 93;

		weapon = new StickyBombLauncher(this);
	}
}