package com.mojang.herpfortress.entity.pickup;

import com.mojang.herpfortress.unit.Mob;

public class AmmoPickup extends Pickup {
	public AmmoPickup(int size) {
		this.size = size;
		icon = size + 1 * 8;
	}

	public boolean take(Mob u) {
		if (!u.weapon.canPickupAmmo()) return false;
		if (size == 0) u.weapon.takeAmmo((int) (u.weapon.getAmmoCapacity() * 0.205));
		if (size == 1) u.weapon.takeAmmo((int) (u.weapon.getAmmoCapacity() * 0.50));
		if (size == 2) u.weapon.takeAmmo((int) (u.weapon.getAmmoCapacity() * 1));
		return true;
	}
}