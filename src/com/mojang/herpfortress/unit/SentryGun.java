package com.mojang.herpfortress.unit;

import com.mojang.herpfortress.*;

public class SentryGun extends Unit {
	public int time = 0;
	public int size = 0;

	public SentryGun(Player player) {
		super(player);
		visRange = 6;
	}

	public void tick() {
		time++;
	}

	public void renderShadow(Bitmap b, int xp, int yp) {
		if (size == 0)
			b.fill(xp - 3, yp - 1, xp + 1, yp + 1, 1);
		else
			b.fill(xp - 5, yp - 1, xp + 3, yp + 1, 1);
	}

	public void render(Bitmap b, int xp, int yp) {
		if (deadTime > 0) return;

		int frame = 0;

		aimDir = time / 10.0;
		int dirFrame = (int) (-Math.floor(aimDir * 8 / (Math.PI * 2) - 0.5)) & 7;
		int yFrame = 9 + size;
		yp += 2;
		frame = dirFrame;
		if (dirFrame > 4) {
			frame = 3 - (dirFrame - 5);
			b.xFlip = true;
			xp--;
		}

		Bitmap[][] sheet = team == Team.blu ? Art.i.blu : Art.i.red;
		if (hurtTime > 0 && hurtTime / 2 % 2 == 1) {
			b.blendDraw(sheet[frame][yFrame], xp - 8, yp - 15, 0xffffffff);
		} else {
			b.draw(sheet[frame][yFrame], xp - 8, yp - 15);
		}
		b.xFlip = false;
	}
}