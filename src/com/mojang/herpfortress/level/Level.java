package com.mojang.herpfortress.level;

import java.util.*;

import com.mojang.herpfortress.*;
import com.mojang.herpfortress.entity.*;
import com.mojang.herpfortress.entity.pickup.*;
import com.mojang.herpfortress.particle.*;
import com.mojang.herpfortress.unit.*;

public class Level {
	public Random random = new Random();

	public List<Entity> entities = new ArrayList<Entity>();
	public List<Particle> particles = new ArrayList<Particle>();
	public List<Sprite> mapSprites = new ArrayList<Sprite>();
	public final int w, h;
	public final int xs, ys;
	public double maxHeight = 128;

	public int[] tiles;
	public List<Unit> units = new ArrayList<Unit>();
	boolean[] visibleTmpMap;
	private ShadowMask sm = new ShadowMask();

	public Blockmap blockmap;
	public Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
		public int compare(Sprite s0, Sprite s1) {
			if (s0.y + s0.x < s1.y + s1.x) return -1;
			if (s0.y + s0.x > s1.y + s1.x) return 1;
			if (s0.z < s1.z) return -1;
			if (s0.z > s1.z) return 1;
			if (s0.x < s1.x) return -1;
			if (s0.x > s1.x) return 1;
			if (s0.y < s1.y) return -1;
			if (s0.y > s1.y) return 1;
			return 0;
		}
	};

	public Player redPlayer;
	public Player bluPlayer;

	public Level() {
		Bitmap bmp = Art.load("/levels/level.png");

		int w = bmp.w + 8;
		int h = bmp.h + 8;

		this.w = w;
		this.h = h;

		visibleTmpMap = new boolean[w * h];

		xs = w;
		ys = h;
		blockmap = new Blockmap(w * 16, h * 16, 32);

		tiles = new int[xs * ys];
		for (int y = 0; y < ys; y++) {
			for (int x = 0; x < xs; x++) {
				int xx = x - 4;
				int yy = y - 4;
				if (xx < 0 || yy < 0 || xx >= bmp.w || yy >= bmp.h) {
					tiles[x + y * xs] = 1;
				} else {
					int col = bmp.pixels[xx + yy * bmp.w];
					Entity item = null;
					if (col == 0xff000000) {
						tiles[x + y * xs] = 1;
					} else {
						tiles[x + y * xs] = 0;

						if (col == 0xff808080) item = new AmmoPickup(0);
						if (col == 0xffa0a0a0) item = new AmmoPickup(1);
						if (col == 0xffc0c0c0) item = new AmmoPickup(2);
						if (col == 0xff8080ff) item = new HealthPickup(0);
						if (col == 0xffa0a0ff) item = new HealthPickup(1);
						if (col == 0xffc0c0ff) item = new HealthPickup(2);
					}

					if (item != null) {
						item.x = x * 16 + 8;
						item.y = y * 16 + 8;
						add(item);
					}
				}
			}
		}

		redPlayer = new Player(this, Team.red);
		bluPlayer = new Player(this, Team.blu);

		SentryGun sg = new SentryGun(redPlayer);
		sg.x = 32 * 16 + 8;
		sg.y = 6 * 16 + 8;
		add(sg);
	}

	public void add(Particle p) {
		particles.add(p);
		p.init(this);
	}

	public void add(Entity e) {
		entities.add(e);
		blockmap.add(e);
		e.init(this);
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.removed) e.tick();
			if (e.removed) {
				blockmap.remove(e);
				e.onRemoved();
				entities.remove(i--);
			} else {
				blockmap.update(e);
			}
		}

		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if (!p.removed) p.tick();
			if (p.removed) particles.remove(i--);
		}

		spawnUnits(bluPlayer, 8 * 16 + 8, 7 * 16 + 8);
		spawnUnits(redPlayer, 33 * 16 + 8, 7 * 16 + 8);

	}

	private void spawnUnits(Player player, double x, double y) {
		for (int i = 0; i < 9; i++) {
			if (player.getUnit(i) == null) {
				double dir = random.nextDouble() * Math.PI * 2;
				double dist = random.nextDouble() * 24;
				Unit u = Mob.create(i, player);
				u.x = x + Math.cos(dir) * dist;
				u.y = y + Math.sin(dir) * dist;
				add(u);
			}
		}
	}

	public List<Unit> getUnitScreenSpace(double x0, double y0, double x1, double y1) {
		List<Unit> result = new ArrayList<Unit>();
		for (Unit u : units) {
			if (u.intersectsScreenSpace(x0, y0, x1, y1)) {
				result.add(u);
			}
		}
		return result;
	}

	public List<Entity> getEntities(double x0, double y0, double z0, double x1, double y1, double z1) {
		return blockmap.getEntities(x0, y0, z0, x1, y1, z1);
	}

	public boolean wallBlocks(double x0, double y0, double z0, double x1, double y1, double z1) {
		int xx0 = (int) Math.floor(x0 / 16);
		int yy0 = (int) Math.floor(y0 / 16);
		int xx1 = (int) Math.floor(x1 / 16);
		int yy1 = (int) Math.floor(y1 / 16);
		if (xx0 < 0) xx0 = 0;
		if (yy0 < 0) yy0 = 0;
		if (xx1 >= xs) xx1 = xs - 1;
		if (yy1 >= ys) yy1 = ys - 1;

		for (int y = yy0; y <= yy1; y++) {
			for (int x = xx0; x <= xx1; x++) {
				if (tiles[x + y * xs] == 1) return true;
			}
		}
		return false;
	}

	public void renderBg(Bitmap bm, int xScroll, int yScroll, int[] visMap) {
		int w = bm.w / 24 + 4;
		int h = bm.h / 6 + 5;

		int x0 = xScroll / 24 - 2;
		int y0 = yScroll / 6 - 3;
		int x1 = x0 + w;
		int y1 = y0 + h;
		mapSprites.clear();
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				int xt = x + (y >> 1) + (y & 1);
				int yt = (y >> 1) - x;

				if (xt > 0 && yt > 0 && xt < xs - 3 && yt < ys - 3) {
					boolean visible = false;
					int pp = xt - 1 + (yt - 1) * xs;
					if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
					pp += xs;
					if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
					pp += xs;
					if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
					pp += xs;
					if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3] | visMap[pp + 4]) > 0) visible = true;
					//					if (visMap[xt - 1 + (yt - 1) * xs] > 0 || visMap[xt - 1 + (yt + 0) * xs] > 0 || visMap[xt - 1 + (yt + 1) * xs] > 0) visible = true;
					//					if (visMap[xt + 0 + (yt + 2) * xs] > 0 || visMap[xt + 0 + (yt + 0) * xs] > 0 || visMap[xt + 0 + (yt + 1) * xs] > 0) visible = true;
					//					if (visMap[xt + 1 + (yt + 2) * xs] > 0 || visMap[xt + 1 + (yt + 0) * xs] > 0 || visMap[xt + 1 + (yt + 1) * xs] > 0) visible = true;
					//					if (visMap[xt + 2 + (yt + 2) * xs] > 0 || visMap[xt + 2 + (yt + 0) * xs] > 0 || visMap[xt + 2 + (yt + 1) * xs] > 0) visible = true;

					if (visible) {
						if (tiles[xt + yt * xs] == 1) {
							mapSprites.add(new MapSprite(Art.i.mapTiles[0][1], xt * 16, yt * 16));
						}
						bm.draw(Art.i.mapTiles[((yt ^ xt) & 1) == 0 ? 0 : 1][0], x * 24 + (y & 1) * 12, y * 6 - 6);
					}
				}
			}
		}
	}

	public void renderShadows(Bitmap bm, int[] visMap) {
		for (int yt = 1; yt < ys - 4; yt++) {
			for (int xt = 1; xt < xs - 4; xt++) {
				boolean visible = false;
				int pp = xt - 1 + (yt - 1) * xs;
				if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
				pp += xs;
				if (!visible && (visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
				pp += xs;
				if (!visible && (visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;

				visibleTmpMap[xt + yt * xs] = visible;
			}
		}
		for (Sprite s : entities) {
			if (visibleTmpMap[(int) (s.x / 16) + (int) (s.y / 16) * xs]) s.renderShadow(bm);
		}
		for (Sprite s : particles) {
			if (visibleTmpMap[(int) (s.x / 16) + (int) (s.y / 16) * xs]) s.renderShadow(bm);
		}
	}

	public void renderSprites(Bitmap bm, int[] visMap) {
		for (int yt = 1; yt < ys - 4; yt++) {
			for (int xt = 1; xt < xs - 4; xt++) {
				boolean visible = false;
				int pp = xt - 1 + (yt - 1) * xs;
				if ((visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
				pp += xs;
				if (!visible && (visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;
				pp += xs;
				if (!visible && (visMap[pp] | visMap[pp + 1] | visMap[pp + 2] | visMap[pp + 3]) > 2) visible = true;

				visibleTmpMap[xt + yt * xs] = visible;
			}
		}

		TreeSet<Sprite> sortedSprites = new TreeSet<Sprite>(spriteComparator);
		for (Sprite s : entities) {
			if (visibleTmpMap[(int) (s.x / 16) + (int) (s.y / 16) * xs]) sortedSprites.add(s);
		}
		for (Sprite s : particles) {
			if (visibleTmpMap[(int) (s.x / 16) + (int) (s.y / 16) * xs]) sortedSprites.add(s);
		}
		sortedSprites.addAll(mapSprites);
		for (Sprite s : sortedSprites) {
			s.render(bm);
		}
	}

	public void explode(Bullet rocket, double x, double y, double z, int dmg, double radius) {
		double r = radius;
		List<Entity> entities = getEntities(x - r, y - r, z - r, x + r, y + r, z + r);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			//			if (!(e instanceof Unit) ) continue;
			//			Unit u = (Unit) e;
			double xd = e.x - x;
			double yd = e.y - y;
			double zd = (e.z + e.zh / 2) - z;
			if (xd * xd + yd * yd + zd * zd < r * r) {
				double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
				xd /= dd;
				yd /= dd;
				zd /= dd;
				dd /= r;
				double falloff = (1 - dd) * 0.5 + 0.5;
				falloff *= 0.5;
				e.handleExplosion(rocket, (int) (dmg * falloff), xd * 5 * (1 - dd), yd * 5 * (1 - dd), zd * 5 * (1 - dd));
				//				e.xa += xd * 2;
				//				e.ya += yd * 2;
				//				e.za += zd * 2;
			}
		}
		add(new Explosion(x, y, z));
	}

	public void renderInvis(Bitmap bm, int xScroll, int yScroll, int[] visMap) {
		int w = bm.w / 24 + 4;
		int h = bm.h / 6 + 5;

		int x0 = xScroll / 24 - 2;
		int y0 = yScroll / 6 - 3;
		int x1 = x0 + w;
		int y1 = y0 + h;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				int xt = x + (y >> 1) + (y & 1) + 1;
				int yt = (y >> 1) - x;
				if (xt < 0) xt = 0;
				if (yt < 0) yt = 0;
				if (xt > this.w - 2) xt = this.w - 2;
				if (yt > this.h - 2) yt = this.h - 2;
				if (xt >= 0 && yt >= 0 && xt < this.w - 1 && yt < this.h - 1) {
					int slot1 = 0;
					if (visMap[(xt) + (yt) * this.w] > 0) slot1 += 1;
					if (visMap[(xt + 1) + (yt) * this.w] > 0) slot1 += 2;
					if (visMap[(xt) + (yt + 1) * this.w] > 0) slot1 += 4;
					if (visMap[(xt + 1) + (yt + 1) * this.w] > 0) slot1 += 8;

					int slot2 = 0;
					if (visMap[(xt) + (yt) * this.w] > 2) slot2 += 1;
					if (visMap[(xt + 1) + (yt) * this.w] > 2) slot2 += 2;
					if (visMap[(xt) + (yt + 1) * this.w] > 2) slot2 += 4;
					if (visMap[(xt + 1) + (yt + 1) * this.w] > 2) slot2 += 8;

					if (slot1 > 0) {
						if (slot2 == 15) continue;
						bm.fogBlend(sm.masks[slot2 + 16], x * 24 + (y & 1) * 12, y * 6 - 6);
					}

					bm.blend(sm.masks[slot1], x * 24 + (y & 1) * 12, y * 6 - 6);
				}
			}
		}
	}

}