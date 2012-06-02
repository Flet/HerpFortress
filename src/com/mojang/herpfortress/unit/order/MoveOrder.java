package com.mojang.herpfortress.unit.order;

import com.mojang.herpfortress.unit.Mob;

public class MoveOrder extends Order {
	public double x, y;

	public MoveOrder(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void init(Mob unit) {
		super.init(unit);
		unit.pathFinder.startFindingPath(getTravelCosts(), (int) (unit.x / 16), (int) (unit.y / 16), (int) (x / 16), (int) (y / 16));
	}

	private int[] getTravelCosts() {
		int[] costs = new int[64 * 64];
		for (int y = 0; y < 64; y++) {
			for (int x = 0; x < 64; x++) {
				if (!unit.player.canSee(x, y)) {
					costs[x + y * 64] = 10;
				} else {
					int t = unit.level.tiles[x + y * unit.level.w];
					if (t == 1) {
						costs[x + y * 64] = 0;
					} else {
						costs[x + y * 64] = 5;
					}
				}
			}
		}

		return costs;
	}

	public void tick() {
		if (unit.pathFinder.isPathing) {
			unit.pathFinder.continueFindingPath(50);
		}

		if (unit.pathFinder.pathP > 1) {
			int target = unit.pathFinder.path[unit.pathFinder.pathP - 2];
			double xt = target % 64 * 16 + 8;
			double yt = target / 64 * 16 + 8;
			if (unit.distanceTo(xt, yt) > 6) {
				if (unit.turnTowards(unit.angleTo(xt, yt))) {
					unit.moveForwards();
				}
			} else {
				if (--unit.pathFinder.pathP > 1) {
					unit.pathFinder.startFindingPath(getTravelCosts(), (int) (unit.x / 16), (int) (unit.y / 16), (int) (x / 16), (int) (y / 16));
				}
			}
		} else {
			if (unit.distanceTo(x, y) > 1) {
				if (unit.turnTowards(unit.angleTo(x, y))) {
					unit.moveForwards();
				}
			}
		}
	}

	public boolean finished() {
		return unit.distanceTo(x, y) < 2;
	}
}