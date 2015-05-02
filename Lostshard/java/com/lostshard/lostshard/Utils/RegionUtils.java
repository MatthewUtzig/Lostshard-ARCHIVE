package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class RegionUtils {

	public static List<Location> getCylinder(Location pos, double radius,
			int height) {
		List<Location> rs = new ArrayList<Location>();

		pos.getBlock().getLocation(pos);

		radius += 0.5;

		if (height == 0) {
			return rs;
		} else if (height < 0) {
			height = -height;
			pos = pos.subtract(0, height, 0);
		}

		if (pos.getBlockY() < 0) {
			pos.setY(0);
		} else if (pos.getBlockY() + height - 1 > pos.getWorld().getMaxHeight()) {
			height = pos.getWorld().getMaxHeight() - pos.getBlockY() + 1;
		}

		final double invRadius = 1 / radius;

		final int ceilRadius = (int) Math.ceil(radius);

		double nextXn = 0;
		forX: for (int x = 0; x <= ceilRadius; ++x) {
			final double xn = nextXn;
			nextXn = (x + 1) * invRadius;
			double nextZn = 0;
			forZ: for (int z = 0; z <= ceilRadius; ++z) {
				final double zn = nextZn;
				nextZn = (z + 1) * invRadius;

				double distanceSq = lengthSq(xn, zn);
				if (distanceSq > 1) {
					if (z == 0) {
						break forX;
					}
					break forZ;
				}

				for (int y = 0; y < height; ++y) {
					rs.add(pos.clone().add(x, y, z));
					rs.add(pos.clone().add(-x, y, z));
					rs.add(pos.clone().add(x, y, -z));
					rs.add(pos.clone().add(-x, y, -z));
				}
			}
		}
		return rs;
	}

	public static List<Location> getSphere(Location pos, double radius) {
		List<Location> rs = new ArrayList<Location>();

		pos.getBlock().getLocation(pos);

		radius += 0.5;

		final double invRadius = 1 / radius;

		final int ceilRadius = (int) Math.ceil(radius);

		double nextXn = 0;
		forX: for (int x = 0; x <= ceilRadius; ++x) {
			final double xn = nextXn;
			nextXn = (x + 1) * invRadius;
			double nextYn = 0;
			forY: for (int y = 0; y <= ceilRadius; ++y) {
				final double yn = nextYn;
				nextYn = (y + 1) * invRadius;
				double nextZn = 0;
				forZ: for (int z = 0; z <= ceilRadius; ++z) {
					final double zn = nextZn;
					nextZn = (z + 1) * invRadius;

					double distanceSq = lengthSq(xn, yn, zn);
					if (distanceSq > 1) {
						if (z == 0) {
							if (y == 0) {
								break forX;
							}
							break forY;
						}
						break forZ;
					}

					rs.add(pos.clone().add(x, y, z));
					rs.add(pos.clone().add(-x, y, z));
					rs.add(pos.clone().add(x, -y, z));
					rs.add(pos.clone().add(x, y, -z));
					rs.add(pos.clone().add(-x, -y, z));
					rs.add(pos.clone().add(x, -y, -z));
					rs.add(pos.clone().add(-x, y, -z));
					rs.add(pos.clone().add(-x, -y, -z));
				}
			}
		}
		return rs;
	}

	private static double lengthSq(double x, double z) {
		return (x * x) + (z * z);
	}

	private static double lengthSq(double x, double y, double z) {
		return (x * x) + (y * y) + (z * z);
	}

}
