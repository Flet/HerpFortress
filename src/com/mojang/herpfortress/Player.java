package com.mojang.herpfortress;

import com.mojang.herpfortress.level.Level;
import com.mojang.herpfortress.unit.*;

public class Player {
	public int selected = 0;
	public Team team;
	public int[] visMap;
	public Level level;

	public Player(Level level, Team team) {
		this.team = team;
		this.level = level;

		visMap = new int[level.w * level.h];
	}

	public void tick() {
		for (int i = 0; i < visMap.length; i++) {
			visMap[i] &= 1;
		}

		for (Unit u : level.units) {
			if (u.team == team) {
				reveal(u);
			}
		}
	}

	private void reveal(Unit u) {
		int r = u.visRange;
		int xx = (int) ((u.x) / 16);
		int yy = (int) ((u.y) / 16);
		for (int i = 0; i <= r * 2 + 1; i++) {
			revealRay(xx, yy, xx - r, yy - r + i, r);
			revealRay(xx, yy, xx + r, yy - r + i, r);
			revealRay(xx, yy, xx - r + i, yy - r, r);
			revealRay(xx, yy, xx - r + i, yy + r, r);
		}

	}

	private void revealRay(int x0, int y0, int x1, int y1, int r) {
		int steps = (int) r + 1;

		double xd = x1 - x0;
		double yd = y1 - y0;
		for (int i = 0; i < steps; i++) {
			double xa = xd * i / steps;
			double ya = yd * i / steps;
			if (xa * xa + ya * ya > r * r - 3) return;

			int x = (int) (x0 + xa + 0.5);
			int y = (int) (y0 + ya + 0.5);
			if (x >= 0 && y >= 0 && x < level.w && y < level.h) {
				if (level.tiles[x + y * level.w] == 1) return;
				visMap[x + y * level.w] = 3;
			}
			/*for (int y = y0; y <= y1; y++) {
				double yd = y + 0.5 - u.y / 16;

				for (int x = x0; x <= x1; x++) {
					double xd = x + 0.5 - u.x / 16;

					if (xd * xd + yd * yd < r * r) {
						visMap[x + y * game.level.w] = 3;
					}
				}
			}*/
		}
	}

	public boolean canSee(int x, int y) {
		if (x < 1 || y < 1 || x >= level.w - 1 || y >= level.h - 1) return false;
		int pp = x - 1 + (y - 1) * level.w;
		if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2]) > 0) return true;
		pp += level.w;
		if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2]) > 0) return true;
		pp += level.w;
		if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2]) > 0) return true;
		return false;
	}

	public Mob getSelectedUnit() {
		return getUnit(selected);
	}

	public Mob getUnit(int unitClass) {
		for (Unit u : level.units) {
			if (u.team == team && u instanceof Mob && ((Mob) u).unitClass == unitClass) {
				return (Mob) u;
			}
		}
		return null;
	}
}