package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.Player;
import com.mojang.herpfortress.weapon.Revolver;

public class Spy extends Mob {
	public Spy(Player player) {
		super(8, player);
		maxHealth = health = 125;
		speed = 100;

		weapon = new Revolver(this);
	}
}