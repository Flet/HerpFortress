package com.mojang.herpfortress.level;

import java.util.*;

import com.mojang.herpfortress.entity.Entity;

public class Blockmap {
	public static long tries;
	public static long oks;

	private class BlockCell {
		public List<Entity> entities = new ArrayList<Entity>();

		public void getEntities(List<Entity> result, double x0, double y0, double z0, double x1, double y1, double z1) {
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				tries++;
				if (e.intersects(x0, y0, z0, x1, y1, z1)) {
					oks++;
					result.add(e);
				}
			}
		}
	}

	private int xs, ys;
	private int divide;
	private BlockCell[] blockCells;

	public Blockmap(int w, int h, int divide) {
		this.xs = w / divide + 1;
		this.ys = h / divide + 1;
		this.divide = divide;

		blockCells = new BlockCell[xs * ys];
		for (int i = 0; i < xs * ys; i++) {
			blockCells[i] = new BlockCell();
		}
	}

	public void add(Entity e) {
		if (!e.isCollideable) return;
		e.xBlockCell = (int) (e.x / divide);
		e.yBlockCell = (int) (e.y / divide);
		blockCells[e.xBlockCell + e.yBlockCell * xs].entities.add(e);
	}

	public void update(Entity e) {
		if (!e.isCollideable) return;
		int xOld = e.xBlockCell;
		int yOld = e.yBlockCell;
		e.xBlockCell = (int) (e.x / divide);
		e.yBlockCell = (int) (e.y / divide);
		if (xOld != e.xBlockCell || yOld != e.yBlockCell) {
			blockCells[xOld + yOld * xs].entities.remove(e);
			blockCells[e.xBlockCell + e.yBlockCell * xs].entities.add(e);
		}
	}

	public void remove(Entity e) {
		if (!e.isCollideable) return;
		blockCells[e.xBlockCell + e.yBlockCell * xs].entities.remove(e);
	}

	public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) {
		double r = 10;
		int xc0 = (int) ((x0 - r) / divide);
		int xc1 = (int) ((x1 + r) / divide);
		int yc0 = (int) ((y0 - r) / divide);
		int yc1 = (int) ((y1 + r) / divide);
		if (xc0 < 0) xc0 = 0;
		if (yc0 < 0) yc0 = 0;
		if (xc1 >= xs) xc1 = xs - 1;
		if (yc1 >= ys) yc1 = ys - 1;

		List<Entity> result = EntityListCache.get();

		for (int y = yc0; y <= yc1; y++) {
			for (int x = xc0; x <= xc1; x++) {
				blockCells[x + y * xs].getEntities(result, x0, y0, z0, x1, y1, z1);
			}
		}

		return result;
	}

}
