package com.mojang.herpfortress.unit.order;

import com.mojang.herpfortress.unit.Mob;

public class Order {
	public Mob unit;

	public void init(Mob unit) {
		this.unit = unit;
	}

	public void tick() {
	}

	public boolean finished() {
		return true;
	}
}