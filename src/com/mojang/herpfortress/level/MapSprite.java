package com.mojang.herpfortress.level;

import com.mojang.herpfortress.*;

public class MapSprite extends Sprite {
	public Bitmap bitmap;

	public MapSprite(Bitmap bitmap, double x, double y) {
		this.bitmap = bitmap;
		this.x = x+8;
		this.y = y+8;
	}

	public void render(Bitmap b, int xp, int yp) {
		b.draw(bitmap, xp - 12, yp - 18);
	}
}
