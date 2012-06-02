package com.mojang.herpfortress;

import com.mojang.herpfortress.level.Minimap;
import com.mojang.herpfortress.unit.*;
import com.mojang.herpfortress.unit.order.MoveOrder;

public class PlayerView {
	public Game game;
	public Player player;
	public Input input;

	public boolean dragging;
	public int xStartDrag, yStartDrag;

	public double xScroll = 442;
	public double yScroll = 286;
	public double xScrollA = 0;
	public double yScrollA = 0;
	public double xScrollT = 0;
	public double yScrollT = 0;
	public double scrollSpeed = 2;
	public int scrollSteps = 0;
	public int time = 0;

	public Minimap minimap;

	public PlayerView(Game game, Player player, Input input) {
		this.game = game;
		this.player = player;
		this.input = input;
		minimap = new Minimap(game.level, player);
	}

	public void sendAllSelectedTo(int x, int y) {
		double xx = (x + xScroll) / Sprite.SCALE_X / 2;
		double yy = (y + yScroll) / Sprite.SCALE_X;
		double xt = xx + yy;
		double yt = yy - xx;

		Mob unit = player.getSelectedUnit();
		if (unit != null) {
			unit.setOrder(new MoveOrder(xt, yt));
		}
	}

	public void tick() {
		player.tick();
		time++;

		xScrollA *= 0.2;
		yScrollA *= 0.2;
		if (input.up.down | input.down.down | input.left.down | input.right.down) {
			scrollSpeed += (6 - scrollSpeed) * 0.05;
		} else {
			scrollSpeed = 2;
		}
		if (input.up.down) yScrollA -= scrollSpeed;
		if (input.down.down) yScrollA += scrollSpeed;
		if (input.left.down) xScrollA -= scrollSpeed;
		if (input.right.down) xScrollA += scrollSpeed;

		xScroll += xScrollA;
		yScroll += yScrollA;
		if (scrollSteps > 0) {
			xScroll += (xScrollT - xScroll) / scrollSteps;
			yScroll += (yScrollT - yScroll) / scrollSteps;
			scrollSteps--;
		}

		int toSelect = -1;
		if (input.unit1.typed) toSelect = 0;
		if (input.unit2.typed) toSelect = 1;
		if (input.unit3.typed) toSelect = 2;
		if (input.unit4.typed) toSelect = 3;
		if (input.unit5.typed) toSelect = 4;
		if (input.unit6.typed) toSelect = 5;
		if (input.unit7.typed) toSelect = 6;
		if (input.unit8.typed) toSelect = 7;
		if (input.unit9.typed) toSelect = 8;
		if (toSelect >= 0) {
			if (toSelect == player.selected) {
				centerOn(player.getSelectedUnit());
			} else {
				player.selected = toSelect;
			}
		}

		if (input.nextUnit.typed) player.selected++;
		if (input.prevUnit.typed) player.selected--;
		if (player.selected >= 9) player.selected -= 9;
		if (player.selected < 0) player.selected += 9;

		if (input.b0Clicked) {
			Mob nearest = getNearest(input.x, input.y);
			if (nearest != null && nearest != player.getSelectedUnit()) {
				player.selected = nearest.unitClass;
			}
		}

		if (input.b1Clicked) {
			sendAllSelectedTo(input.x, input.y);
		}

		if (input.b2Clicked) {
			dragging = true;
			xStartDrag = input.x;
			yStartDrag = input.y;
		}

		if (input.b2) {
			xScroll -= input.x - xStartDrag;
			yScroll -= input.y - yStartDrag;
			xStartDrag = input.x;
			yStartDrag = input.y;
			xScrollA = yScrollA = 0;
			scrollSteps = 0;
		}

		minimap.update();
	}

	private void centerOn(Mob u) {
		if (u == null) return;
		double xx = (int) (Math.floor((u.x - u.y) * Unit.SCALE_X));
		double yy = (int) (Math.floor((u.y + u.x - 6) * Unit.SCALE_Y));
		scrollSteps = (int) (Math.sqrt(Math.sqrt(u.distanceToScreenSpaceSqr(xScroll + HerpFortress.WIDTH / 2, yScroll + HerpFortress.HEIGHT / 2))) / 2 + 1);
		xScrollT = xx - HerpFortress.WIDTH / 2;
		yScrollT = yy - HerpFortress.HEIGHT / 2;
	}

	public void drawSelectBox(Bitmap screen, int x0, int y0, int x1, int y1) {
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		screen.box(x0, y0, x1, y1, 0xff00ff00);
	}

	public void selectAll(int x0, int y0, int x1, int y1) {
		x0 += xScroll;
		y0 += yScroll;
		x1 += xScroll;
		y1 += yScroll;
		if (x0 > x1) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y0 > y1) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		int r = 8;

		//		player.selected.clear();
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x1 + r, y1 + r)) {
			if (u instanceof Mob) {
				Mob m = (Mob) u;
				if (m.team == player.team) {
					player.selected = m.unitClass;
				}
			}
		}
	}

	public Mob getNearest(int x0, int y0) {
		x0 += xScroll;
		y0 += yScroll;
		int r = 8;

		Mob nearest = null;
		for (Unit u : game.level.getUnitScreenSpace(x0 - r, y0 - r, x0 + r, y0 + r)) {
			if (u.team == player.team && u instanceof Mob) {
				if (nearest == null || u.distanceToScreenSpaceSqr(x0, y0) < nearest.distanceToScreenSpaceSqr(x0, y0)) {
					nearest = (Mob) u;
				}
			}
		}

		return nearest;
	}

	public void render(Bitmap screen) {
		screen.fill(0, 0, screen.w, screen.h, 0xffff00ff);
		game.render(screen, (int) Math.floor(xScroll), (int) Math.floor(yScroll), player.visMap);

		Mob u = player.getSelectedUnit();
		if (u != null) u.renderSelected(screen);

		screen.xOffs = 0;
		screen.yOffs = 0;

		screen.draw(minimap.img, 2, screen.h - 58);
		for (int i = 0; i < 9; i++) {
			if (player.selected == i) {
				screen.draw(Art.i.red[i][14], i * 20 + 64, 240 - 16);
				screen.draw(Art.i.cursors[1][0], i * 20 + 64, 240 - 16 - 10 - (int) (Math.abs(Math.sin(time / 10.0) * 6.5)));
			} else {
				screen.blendDraw(Art.i.red[i][14], i * 20 + 64, 240 - 14, 0);
			}
		}
	}
}