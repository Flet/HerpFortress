package com.mojang.herpfortress.unit;

import java.util.Random;

import com.mojang.herpfortress.*;
import com.mojang.herpfortress.entity.*;
import com.mojang.herpfortress.level.Level;
import com.mojang.herpfortress.particle.SplatDebris;

public class Unit extends Entity {

	public static Random random = new Random();
	public Team team;
	public double dir = 0;
	public int shootTime = 0;
	public int maxHealth = 125;
	public int health = 125;
	public int hurtTime = 0;
	public int burnTime;
	public int burnInterval;
	public double aimDir;
	public int visRange = 8;
	public Player player;
	public int deadTime;

	public Unit(Player player) {
		this.player = player;
		team = player.team;
	}

	public void init(Level level) {
		super.init(level);
		level.units.add(this);
	}

	public void onRemoved() {
		level.units.remove(this);
	}

	public void hitBy(Bullet bullet) {
		if (bullet.owner.team == team) return;

		bullet.applyHitEffect(this);

		hurt(bullet.getDamage(this));
		knockBack(bullet.xa * 0.25, bullet.ya * 0.25, bullet.za * 0.25);
		SplatDebris sd = new SplatDebris(x, y, z + 5);
		sd.xa -= bullet.xa * 0.1;
		sd.ya -= bullet.ya * 0.1;
		sd.za -= bullet.za * 0.1;
		level.add(sd);
	}

	public void knockBack(double xxa, double yya, double zza) {
		xa += (xxa - xa) * 0.4;
		ya += (yya - ya) * 0.4;
		za += (zza - za) * 0.4;
	}

	protected boolean isOnGround() {
		return z <= 1;
	}

	public boolean isLegalTarget(Mob u) {
		return u.team != team;
	}

	public void hurt(int dmg) {
		health -= dmg;
		hurtTime = 20;
	}

	public void handleExplosion(Bullet source, int dmg, double xd, double yd, double zd) {
		if (this == source.owner) {
			dmg /= 2;
		} else if (team == source.owner.team) {
			return;
		}
		hurt(dmg);
		knockBack(xd * 2, yd * 2, zd * 2);
	}

	public void renderSelected(Bitmap screen) {
		if (deadTime > 0) return;
		int xp = (int) (Math.floor((x - y) * SCALE_X));
		int yp = (int) (Math.floor((y + x) * SCALE_Y)) - 6;

		int r = 8;
		screen.box(xp - r, yp - r - 1, xp + r - 1, yp + r, 0xff40ff80);

		int dmg = (maxHealth - health) * 10 / maxHealth;
		screen.fill(xp - 5, yp - 10, xp + 4, yp - 10, 0xffff0000);
		screen.fill(xp - 5, yp - 10, xp + 4 - dmg, yp - 10, 0xff00ff00);
	}

	public double distanceToScreenSpaceSqr(double x0, double y0) {
		double xx = (int) (Math.floor((x - y) * SCALE_X));
		double yy = (int) (Math.floor((y + x - 6) * SCALE_Y));

		double xd = xx - x0;
		double yd = yy - y0;

		return xd * xd + yd * yd;
	}

	public boolean intersectsScreenSpace(double x0, double y0, double x1, double y1) {
		double xx = (int) (Math.floor((x - y) * SCALE_X));
		double yy = (int) (Math.floor((y + x - 6) * SCALE_Y));

		int ww = 4;
		int hh = 6;
		if (x1 <= xx - ww || x0 > xx + ww || y1 <= yy - hh || y0 > yy + hh) return false;
		return true;
	}

	public double distanceTo(double x, double y) {
		double xd = x - this.x;
		double yd = y - this.y;
		return Math.sqrt(xd * xd + yd * yd);
	}

	public double angleTo(double x, double y) {
		return Math.atan2(y - this.y, x - this.x);
	}

	public boolean turnTowards(double angle) {
		while (dir < -Math.PI)
			dir += Math.PI * 2;
		while (dir >= Math.PI)
			dir -= Math.PI * 2;
		while (angle < -Math.PI)
			angle += Math.PI * 2;
		while (angle >= Math.PI)
			angle -= Math.PI * 2;

		double angleDiff = angle - dir;
		while (angleDiff < -Math.PI)
			angleDiff += Math.PI * 2;
		while (angleDiff >= Math.PI)
			angleDiff -= Math.PI * 2;

		double turnSpeed = 0.2;
		double near = 1.0;
		boolean wasAimed = angleDiff * angleDiff < near * near;
		if (angleDiff < -turnSpeed) angleDiff = -turnSpeed;
		if (angleDiff > +turnSpeed) angleDiff = +turnSpeed;
		dir += angleDiff;
		return wasAimed;
	}

	public void heal(int toHeal) {
		int maxHeal = maxHealth - health;
		if (maxHeal <= 0) return;
		if (toHeal > maxHeal) toHeal = maxHeal;
		health += toHeal;
	}

}