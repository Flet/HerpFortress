package com.mojang.herpfortress.level;

import com.mojang.herpfortress.*;

public class Minimap {
	public Level level;
	public Player player;
	public Bitmap img;

	public Minimap(Level level, Player player) {
		this.level = level;
		this.player = player;
		img = new Bitmap(56, 56);
	}

	public void update() {
		for (int y = 0; y < 56; y++) {
			for (int x = 0; x < 56; x++) {
				int visLevel = player.visMap[x + 4 + (y + 4) * 64];
				int col = 0xff000000;
				if (visLevel > 2) {
					col = 0xffffffff;
				} else if (visLevel > 0) {
					col = 0xff808080;
				}
				img.pixels[x + y * 56] = col;
			}
		}
	}
}