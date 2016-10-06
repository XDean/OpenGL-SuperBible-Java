package xdean.OpenGLSuperBible.share;

public class Math3d {

	public static void m3dCrossProduct3(float[] result, float[] u, float[] v) {
		result[0] = u[1] * v[2] - v[1] * u[2];
		result[1] = -u[0] * v[2] + v[0] * u[2];
		result[2] = u[0] * v[1] - v[0] * u[1];
	}

	public static void m3dSetMatrixColumn44(float[] dst, float[] src, int column) {
		// memcpy(dst + (4 * column), src, sizeof(float) * 4);
		System.arraycopy(src, 0, dst, 4 * column, Math.min(src.length, 4));
	}

	public static void m3dRotationMatrix44(float[] m, float angle, float x,
			float y, float z) {
		float mag, s, c;
		float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;

		s = (float) (Math.sin(angle));
		c = (float) (Math.cos(angle));

		mag = (float) (Math.sqrt(x * x + y * y + z * z));

		// Identity matrix
		if (mag == 0.0f) {
			m3dLoadIdentity44(m);
			return;
		}

		// Rotation matrix is normalized
		x /= mag;
		y /= mag;
		z /= mag;

		xx = x * x;
		yy = y * y;
		zz = z * z;
		xy = x * y;
		yz = y * z;
		zx = z * x;
		xs = x * s;
		ys = y * s;
		zs = z * s;
		one_c = 1.0f - c;

		m[0] = (one_c * xx) + c;
		m[1] = (one_c * xy) - zs;
		m[2] = (one_c * zx) + ys;
		m[3] = 0.0f;

		m[4] = (one_c * xy) + zs;
		m[5] = (one_c * yy) + c;
		m[6] = (one_c * yz) - xs;
		m[7] = 0.0f;

		m[8] = (one_c * zx) - ys;
		m[9] = (one_c * yz) + xs;
		m[10] = (one_c * zz) + c;
		m[11] = 0.0f;

		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 0.0f;
		m[15] = 1.0f;
	}

	private static final float[] IDENTITY44 = { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };

	public static void m3dLoadIdentity44(float[] m) {
		System.arraycopy(IDENTITY44, 0, m, 0, 16);
	}

	public static void m3dCopyVector3(float[] dst, float[] src) {
		System.arraycopy(src, 0, dst, 0, 3);
	}

	public static void m3dFindNormal(float[] result, float[] point1,
			float[] point2, float[] point3) {

		float[] v1 = new float[3], v2 = new float[3];
		v1[0] = point1[0] - point2[0];
		v1[1] = point1[1] - point2[1];
		v1[2] = point1[2] - point2[2];

		v2[0] = point2[0] - point3[0];
		v2[1] = point2[1] - point3[1];
		v2[2] = point2[2] - point3[2];
		m3dCrossProduct3(result, v1, v2);
	}

	public static void m3dMakePlanarShadowMatrix(float[] result,
			float[] planeEq, float[] vLightPos) {
		float a = planeEq[0];
		float b = planeEq[1];
		float c = planeEq[2];
		float d = planeEq[3];

		float dx = -vLightPos[0];
		float dy = -vLightPos[1];
		float dz = -vLightPos[2];

		result[0] = b * dy + c * dz;
		result[1] = -a * dy;
		result[2] = -a * dz;
		result[3] = 0.0f;

		result[4] = -b * dx;
		result[5] = a * dx + c * dz;
		result[6] = -b * dz;
		result[7] = 0.0f;

		result[8] = -c * dx;
		result[9] = -c * dy;
		result[10] = a * dx + b * dy;
		result[11] = 0.0f;

		result[12] = -d * dx;
		result[13] = -d * dy;
		result[14] = -d * dz;
		result[15] = a * dx + b * dy + c * dz;
	}

	public static void m3dGetPlaneEquation(float[] planeEq, float[] p1,
			float[] p2, float[] p3) {
		float[] v1 = new float[3], v2 = new float[3];

		v1[0] = p3[0] - p1[0];
		v1[1] = p3[1] - p1[1];
		v1[2] = p3[2] - p1[2];

		v2[0] = p2[0] - p1[0];
		v2[1] = p2[1] - p1[1];
		v2[2] = p2[2] - p1[2];

		m3dCrossProduct3(planeEq, v1, v2);
		m3dNormalizeVector(planeEq);

		planeEq[3] = -(planeEq[0] * p3[0] + planeEq[1] * p3[1] + planeEq[2]
				* p3[2]);
	}

	public static void m3dNormalizeVector(float[] u) {
		m3dScaleVector3(u, 1.0f / m3dGetVectorLength3(u));
	}

	public static void m3dScaleVector3(float[] v, float scale) {
		v[0] *= scale;
		v[1] *= scale;
		v[2] *= scale;
	}

	public static float m3dGetVectorLength3(float[] u) {
		return (float) Math.sqrt(m3dGetVectorLengthSquared3(u));
	}

	public static float m3dGetVectorLengthSquared3(float[] u) {
		return (u[0] * u[0]) + (u[1] * u[1]) + (u[2] * u[2]);
	}

	public static float m3dDotProduct(float[] u, float[] v) {
		return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
	}

	public static void m3dInvertMatrix44(float[] mInverse, float[] m) {
		float det, detij;
		det = 0.0f;
		for (int i = 0; i < 4; i++) {
			det += (i & 0x1) != 0 ? (-m[i] * DetIJ(m, 0, i)) : (m[i] * DetIJ(m,
					0, i));
		}
		det = 1.0f / det;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				detij = DetIJ(m, j, i);
				mInverse[(i * 4) + j] = (((i + j) & 0x1) != 0) ? (-detij * det)
						: (detij * det);
			}
		}

	}

	private static float DetIJ(float[] m, int i, int j) {
		int x, y, ii, jj;
		float ret, mat[][] = new float[3][3];

		x = 0;
		for (ii = 0; ii < 4; ii++) {
			if (ii == i)
				continue;
			y = 0;
			for (jj = 0; jj < 4; jj++) {
				if (jj == j)
					continue;
				mat[x][y] = m[(ii * 4) + jj];
				y++;
			}
			x++;
		}

		ret = mat[0][0] * (mat[1][1] * mat[2][2] - mat[2][1] * mat[1][2]);
		ret -= mat[0][1] * (mat[1][0] * mat[2][2] - mat[2][0] * mat[1][2]);
		ret += mat[0][2] * (mat[1][0] * mat[2][1] - mat[2][0] * mat[1][1]);

		return ret;
	}

	public static void m3dTransformVector3(float[] vOut, float[] v, float[] m) {
		vOut[0] = m[0] * v[0] + m[4] * v[1] + m[8] * v[2] + m[12];
		vOut[1] = m[1] * v[0] + m[5] * v[1] + m[9] * v[2] + m[13];
		vOut[2] = m[2] * v[0] + m[6] * v[1] + m[10] * v[2] + m[14];
	}
}
