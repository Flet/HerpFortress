package com.mojang.herpfortress.weapon;

import java.util.Random;

import com.mojang.herpfortress.unit.Mob;

public class Weapon {
	public static final Random random = new Random();

	public Mob owner;

	public int maxAmmoLoaded;
	public int maxAmmoCarried;
	public int ammoLoaded;
	public int ammoCarried;

	public double shootDelayTime;
	public double startReloadDelayTime;
	public double reloadDelayTime;

	public double shootDelay;
	public double reloadDelay;

	public boolean wasReloading;
	public boolean aimAtGround;

	public double maxRange = 150;
	public double aimLead = 0.3;

	public double nearDistance = 0;
	public double midDistance = 512;
	public double farDistance = 1024;
	public double highRamp = 175;
	public double lowRamp = 50;

	public Weapon(Mob owner) {
		this.owner = owner;
	}

	public void tick() {
		if (shootDelay > 0) {
			shootDelay -= 1 / 60.0;
		}
		wasReloading = reloadDelay > 0;
		if (reloadDelay > 0) {
			reloadDelay -= 1 / 60.0;
		}
	}

	public void shoot(double xa, double ya, double za) {
		ammoLoaded--;
	}

	public boolean canUse() {
		return shootDelay <= 0 && reloadDelay <= 0;
	}

	public void reload() {
		if (ammoLoaded < maxAmmoLoaded && ammoCarried > 0) {
			ammoLoaded++;
			ammoCarried--;
			if (!wasReloading) {
				reloadDelay = startReloadDelayTime;
			} else {
				reloadDelay = reloadDelayTime;
			}
		}
	}

	public void playerDied() {
	}

	public boolean canPickupAmmo() {
		if (maxAmmoCarried == 0) {
			return ammoLoaded < maxAmmoLoaded;
		} else {
			return ammoCarried < maxAmmoCarried;
		}
	}

	public void takeAmmo(int ammo) {
		if (maxAmmoCarried == 0) {
			ammoLoaded += ammo;
			if (ammoLoaded > maxAmmoLoaded) ammoLoaded = maxAmmoLoaded;
		} else {
			ammoCarried += ammo;
			if (ammoCarried > maxAmmoCarried) ammoCarried = maxAmmoCarried;
		}
	}

	public int getAmmoCapacity() {
		if (maxAmmoCarried == 0) {
			return maxAmmoLoaded;
		} else {
			return maxAmmoCarried;
		}
	}
}