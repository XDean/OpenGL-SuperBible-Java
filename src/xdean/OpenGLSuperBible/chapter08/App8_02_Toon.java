package xdean.OpenGLSuperBible.chapter08;

import java.nio.ByteBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.Math3d;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App8_02_Toon extends BaseApp {
	public static void main(String[] args) {
		new App8_02_Toon().setVisible(true);
	}

	float[] vLightDir = { -1.0f, 1.0f, 1.0f };
	float yRot = 0.0f;

	void timerFunction(int value) {
		glutPostRedisplay();
		glutTimerFunc(33, this::timerFunction, 1);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glutTimerFunc(33, this::timerFunction, 1);

		ByteBuffer toonTable = ByteBuffer.wrap(new byte[] { 0, 32, 0, 0,
				(byte) 64, 0, 0, (byte) 128, 0, 0, (byte) 192, 0 });

		gl.glClearColor(0.0f, 0.0f, .50f, 1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_CULL_FACE);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
		gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP);
		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glTexImage1D(GL2.GL_TEXTURE_1D, 0, GL2.GL_RGB, 4, 0, GL2.GL_RGB,
				GL2.GL_UNSIGNED_BYTE, toonTable);

		gl.glEnable(GL2.GL_TEXTURE_1D);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -2.5f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		toonDrawTorus(0.35f, 0.15f, 50, 25, vLightDir);
		gl.glPopMatrix();

		gl.glFlush();

		yRot += 0.5f;
	}

	void toonDrawTorus(float majorRadius, float minorRadius, int numMajor,
			int numMinor, float[] vLightDir) {
		float[] mModelViewMatrix = new float[16];
		float[] mInvertedLight = new float[16];
		float[] vNewLight = new float[3];
		float[] vNormal = new float[3];
		double majorStep = 2.0f * PI / numMajor;
		double minorStep = 2.0f * PI / numMinor;
		int i, j;

		gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, mModelViewMatrix, 0);
		
		Math3d.m3dInvertMatrix44(mInvertedLight, mModelViewMatrix);
		Math3d.m3dTransformVector3(vNewLight, vLightDir, mInvertedLight);
		vNewLight[0] -= mInvertedLight[12];
		vNewLight[1] -= mInvertedLight[13];
		vNewLight[2] -= mInvertedLight[14];
		Math3d.m3dNormalizeVector(vNewLight);

		for (i = 0; i < numMajor; ++i) {
			double a0 = i * majorStep;
			double a1 = a0 + majorStep;
			float x0 = (float) Math.cos(a0);
			float y0 = (float) Math.sin(a0);
			float x1 = (float) Math.cos(a1);
			float y1 = (float) Math.sin(a1);

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for (j = 0; j <= numMinor; ++j) {
				double b = j * minorStep;
				float c = (float) Math.cos(b);
				float r = minorRadius * c + majorRadius;
				float z = minorRadius * (float) Math.sin(b);

				vNormal[0] = x0 * c;
				vNormal[1] = y0 * c;
				vNormal[2] = z / minorRadius;
				Math3d.m3dNormalizeVector(vNormal);

				gl.glTexCoord1f(Math3d.m3dDotProduct(vNewLight, vNormal));
				gl.glVertex3f(x0 * r, y0 * r, z);

				vNormal[0] = x1 * c;
				vNormal[1] = y1 * c;
				vNormal[2] = z / minorRadius;
				Math3d.m3dNormalizeVector(vNormal);

				gl.glTexCoord1f(Math3d.m3dDotProduct(vNewLight, vNormal));
				gl.glVertex3f(x1 * r, y1 * r, z);
			}
			gl.glEnd();
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
	}
}
