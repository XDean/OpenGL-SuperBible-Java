package xdean.OpenGLSuperBible.share.GL;

import xdean.OpenGLSuperBible.share.Math3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class GLFrame {

	private GL2 gl;
	@SuppressWarnings("unused")
	private GLU glu;
	private float[] origin;
	private float[] forward;
	private float[] up;

	public GLFrame(GL2 gl, GLU glu) {
		this.gl = gl;
		this.glu = glu;
		this.origin = new float[3];
		this.forward = new float[3];
		this.up = new float[3];

		origin[0] = 0.0f;
		origin[1] = 0.0f;
		origin[2] = 0.0f;

		up[0] = 0.0f;
		up[1] = 1.0f;
		up[2] = 0.0f;

		forward[0] = 0.0f;
		forward[1] = 0.0f;
		forward[2] = -1.0f;
	}

	public void setOrigin(float x, float y, float z) {
		origin[0] = x;
		origin[1] = y;
		origin[2] = z;
	}

	public void MoveForward(float delta) {
		// XXX: The forward vector seems to be left-handed but i don't know
		// what's wrong.
		// origin[0] += forward[0] * delta;
		// origin[1] += forward[1] * delta;
		origin[0] -= forward[0] * delta;
		origin[1] -= forward[1] * delta;
		origin[2] += forward[2] * delta;
	}

	public void RotateLocalX(float angle) {
		float[] rotMat = new float[9];
		float[] localX = new float[3];
		float[] rotVec = new float[3];

		Math3d.m3dCrossProduct3(localX, up, forward);
		
		Math3d.m3dRotationMatrix33(rotMat, angle, localX[0], localX[1],
				localX[2]);

		Math3d.m3dRotateVector(rotVec, up, rotMat);
		Math3d.m3dCopyVector3(up, rotVec);
		
		Math3d.m3dRotateVector(rotVec, forward, rotMat);
		Math3d.m3dCopyVector3(forward, rotVec);
	}

	public void RotateLocalY(float fAngle) {
		float[] rotMat = new float[16];

		Math3d.m3dRotationMatrix44(rotMat, fAngle, up[0], up[1], up[2]);

		float[] newVect = new float[3];

		newVect[0] = rotMat[0] * forward[0] + rotMat[4] * forward[1]
				+ rotMat[8] * forward[2];
		newVect[1] = rotMat[1] * forward[0] + rotMat[5] * forward[1]
				+ rotMat[9] * forward[2];
		newVect[2] = rotMat[2] * forward[0] + rotMat[6] * forward[1]
				+ rotMat[10] * forward[2];
		Math3d.m3dCopyVector3(forward, newVect);
	}

	public void ApplyCameraTransform() {
		ApplyCameraTransform(false);
	}

	public void ApplyCameraTransform(boolean bRotOnly) {
		float[] m = new float[16];
		GetCameraOrientation(m);
		gl.glMultMatrixf(m, 0);
		if (!bRotOnly)
			gl.glTranslatef(-origin[0], -origin[1], -origin[2]);

		// XXX:if use following code, the rotate angle should be inverse
		// glu.gluLookAt(origin[0], origin[1], origin[2], origin[0] +
		// forward[0],
		// origin[1] + forward[1], origin[2] + forward[2], up[0], up[1],
		// up[2]);
	}

	public void ApplyActorTransform() {
		ApplyActorTransform(false);
	}

	public void ApplyActorTransform(boolean rotationOnly) {
		float[] rotMat = new float[16];
		GetMatrix(rotMat, rotationOnly);
		// System.out.println(Arrays.toString(rotMat));
		gl.glMultMatrixf(rotMat, 0);
	}

	private void GetMatrix(float[] matrix, boolean bRotationOnly) {

		// Calculate the right side (x) vector, drop it right into the matrix
		float[] vXAxis = new float[3];
		Math3d.m3dCrossProduct3(vXAxis, up, forward);

		// Set matrix column does not fill in the fourth value...
		Math3d.m3dSetMatrixColumn44(matrix, vXAxis, 0);
		matrix[3] = 0.0f;

		// Y Column
		Math3d.m3dSetMatrixColumn44(matrix, up, 1);
		matrix[7] = 0.0f;

		// Z Column
		Math3d.m3dSetMatrixColumn44(matrix, forward, 2);
		matrix[11] = 0.0f;

		// Translation (already done)
		if (bRotationOnly == true) {

			matrix[12] = 0.0f;
			matrix[13] = 0.0f;
			matrix[14] = 0.0f;

		} else
			Math3d.m3dSetMatrixColumn44(matrix, origin, 3);

		matrix[15] = 1.0f;
	}

	public void GetCameraOrientation(float[] m) {

		float[] x = new float[3], z = new float[3];

		// Make rotation matrix
		// Z vector is reversed
		z[0] = -forward[0];
		z[1] = -forward[1];
		z[2] = -forward[2];

		// X vector = Y cross Z
		Math3d.m3dCrossProduct3(x, up, z);

		// Matrix has no translation information and is
		// transposed.... (rows instead of columns)
		// #define M(row,col) m[col*4+row]
		m[0] = x[0];
		m[1] = x[1];
		m[2] = x[2];
		m[3] = 0.0f;
		m[4] = up[0];
		m[5] = up[1];
		m[6] = up[2];
		m[7] = 0.0f;
		m[8] = z[0];
		m[9] = z[1];
		m[10] = z[2];
		m[11] = 0.0f;
		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 0.0f;
		m[15] = 1.0f;
		// #undef M
	}

	public void MoveUp(float delta) {
		origin[0] += up[0] * delta;
		origin[1] += up[1] * delta;
		origin[2] += up[2] * delta;
	}
}
