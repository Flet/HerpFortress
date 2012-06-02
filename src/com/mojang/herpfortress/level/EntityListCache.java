package com.mojang.herpfortress.level;

import java.util.*;

import com.mojang.herpfortress.entity.Entity;

public class EntityListCache {
	private static List<ArrayList<Entity>> cache = new ArrayList<ArrayList<Entity>>();
	private static int cc = 0;

	public static List<Entity> get() {
		if (cc == cache.size()) {
			cache.add(new ArrayList<Entity>());
		}
		List<Entity> el = cache.get(cc++);
		el.clear();
		return el;
	}

	public static void reset() {
		cc = 0;
	}
}
