package com.mojang.herpfortress.particle;

import com.mojang.herpfortress.entity.Entity;

public class MeatDebris extends Debris {
	public MeatDebris(double x, double y, double z) {
		super(x, y, z);
	}

	public void tick() {
		super.tick();
		Debris blood = new BloodDebris(x, y, z);
		blood.xa *= 0.05;
		blood.ya *= 0.05;
		blood.za *= 0.05;
		blood.xa += xa * 0.5;
		blood.ya += ya * 0.5;
		blood.za += za * 0.5;
		level.add(blood);
	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (za < -0.5) {
			for (int i = 0; i < 20; i++) {
				Debris blood = new BloodDebris(x, y, 0);
				blood.xa *= 0.4;
				blood.ya *= 0.4;
				blood.za *= 0.2;
				blood.xa += xa * 0.5;
				blood.ya += ya * 0.5;
				blood.za = -za * 0.5;
				level.add(blood);
			}
		}
		super.collide(e, xxa, yya, zza);
	}
}