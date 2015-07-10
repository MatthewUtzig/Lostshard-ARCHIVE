package com.lostshard.Lostshard.Utils;

import java.util.ArrayList;

public class Bresenham {
	public static ArrayList<IntPoint> bresenham3d(int x, int y, int z, int xx,
			int yy, int zz) {
		final ArrayList<IntPoint> pointList = new ArrayList<IntPoint>();

		int dx = xx - x;
		int dy = yy - y;
		int dz = zz - z;

		// Direction pointer.

		int step_x = 0;
		int step_y = 0;
		int step_z = 0;

		// Moving right step +1 else -1

		if (dx >= 0)
			step_x = 1;
		else {
			step_x = -1;
			dx = -dx;
		}
		if (dy >= 0)
			step_y = 1;
		else {
			step_y = -1;
			dy = -dy;
		}
		if (dz >= 0)
			step_z = 1;
		else {
			step_z = -1;
			dz = -dz;
		}

		// You need this to make the err_term work.
		// Because we are using integers we must multiply with 2

		final int dx2 = dx * 2; // delta X * 2 instead of 0.5
		final int dy2 = dy * 2; // delta Y * 2 ..
		final int dz2 = dz * 2; // delta Z * 2 ..

		int err_termXY = 0; // Zero it
		int err_termXZ = 0; // Zero it

		// If width is greater than height
		// we are going to adjust the height movment after the x steps.

		if (dx >= dy && dx >= dz) {
			// Set err_term to height*2 and decrement by the segment width.
			// example. 2-10 =-8

			err_termXY = dy2 - dx;
			err_termXZ = dz2 - dx;

			// Step x direction by one until the end of width.

			for (int i = 0; i <= dx; i++) {
				// Paint the pixel
				// Example..

				// bm.SetPixel(x,y,color);
				pointList.add(new IntPoint(x, y, z));

				// Adjust error_term
				// and step down or up by one in the y path.
				// This if it's time to do so.

				if (err_termXY >= 0) {
					err_termXY -= dx2; // err minus the width*2;

					y += step_y; // Step down or up by one.

				}
				if (err_termXZ >= 0) {
					err_termXZ -= dx2; // err minus the width*2;

					z += step_z; // Step in or out by one.

				}
				err_termXY += dy2; // Add err_term by the height * 2;

				err_termXZ += dz2; // Add err_term by the depth * 2;

				// This will happen all the time.

				x += step_x; // step right or left

			}
		} else if (dy > dx && dy > dz)// Height is leading the position.

		{
			// Set err_term to width*2 and decrement by the delta y.

			err_termXY = dx2 - dy;
			err_termXZ = dz2 - dy;

			// Step y direction by one until the end of height.

			for (int i = 0; i <= dy; i++) {
				// Paint one pixel

				// bm.SetPixel(x,y,color);
				pointList.add(new IntPoint(x, y, z));

				// Adjust error_term
				// and step left or right by one in the x path.
				// This if it's time to do so.

				if (err_termXY >= 0) {
					err_termXY -= dy2; // err minus the height*2;

					x += step_x; // Step right or left by one.

				}
				if (err_termXZ >= 0) {
					err_termXZ -= dy2; // err minus the height*2;

					z += step_z; // Step depth in or out by one.

				}
				err_termXY += dx2; // Add err_term by the width * 2;

				err_termXZ += dz2; // Add err_term by the depth * 2;

				// This will happen all the time.

				y += step_y; // step up or down.

			}
		} else if (dz > dx && dz > dy)// Depth is leading the position.

		{
			// Set err_term to width*2 and decrement by the delta z.

			err_termXY = dx2 - dz;
			err_termXZ = dy2 - dz;

			// Step z direction by one until the end of depth.

			for (int i = 0; i <= dz; i++) {
				// Paint one pixel

				// bm.SetPixel(x,y,color);
				pointList.add(new IntPoint(x, y, z));

				// Adjust error_term

				// and step up or down by one in the y path.

				// This if it's time to do so.

				if (err_termXY >= 0) {
					err_termXY -= dz2; // err minus the depth*2;

					y += step_y; // Step up or down by one.

				}
				if (err_termXZ >= 0) {
					err_termXZ -= dz2; // err minus the depth*2;

					x += step_x; // Step right or left by one.

				}
				err_termXY += dy2; // Add err_term by the height * 2;

				err_termXZ += dx2; // Add err_term by the width * 2;

				z += step_z; // step in or out.

			}
		}
		return pointList;
	}
}
