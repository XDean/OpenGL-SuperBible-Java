package xdean.OpenGLSuperBible.chapter05;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.DefaultImpl;
import xdean.OpenGLSuperBible.share.Math3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App5_08_Shadow extends BaseApp {

	public static void main(String[] args) {
		new App5_08_Shadow().setVisible(true);
	}

	float xRot;
	float yRot;

	FloatBuffer ambientLight = FloatBuffer.wrap(new float[] { 0.3f, 0.3f, 0.3f,
			1.0f });
	FloatBuffer diffuseLight = FloatBuffer.wrap(new float[] { 0.7f, 0.7f, 0.7f,
			1.0f });
	FloatBuffer specular = FloatBuffer.wrap(new float[] { 1.0f, 1.0f, 1.0f,
			1.0f });
	FloatBuffer lightPos = FloatBuffer.wrap(new float[] { -75.0f, 150.0f,
			-50.0f, 0.0f });
	FloatBuffer specref = FloatBuffer
			.wrap(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
	float[] shadowMat = new float[16];

	@Override
	protected void specialKeys(int key, int x, int y) {
		if (key == GLUT_KEY_UP)
			xRot -= 5.0f;
		if (key == GLUT_KEY_DOWN)
			xRot += 5.0f;
		if (key == GLUT_KEY_LEFT)
			yRot -= 5.0f;
		if (key == GLUT_KEY_RIGHT)
			yRot += 5.0f;
		if (key > 356.0f)
			xRot = 0.0f;
		if (key < -1.0f)
			xRot = 355.0f;
		if (key > 356.0f)
			yRot = 0.0f;
		if (key < -1.0f)
			yRot = 355.0f;
		glutPostRedisplay();
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		float[][] points = { { -30.0f, -149.0f, -20.0f },
				{ -30.0f, -149.0f, 20.0f }, { 40.0f, -149.0f, 20.0f } };

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specref);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);

		gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

		float[] vPlaneEquation = new float[4];
		Math3d.m3dGetPlaneEquation(vPlaneEquation, points[0], points[1],
				points[2]);
		Math3d.m3dMakePlanarShadowMatrix(shadowMat, vPlaneEquation,
				lightPos.array());
		gl.glEnable(GL2.GL_NORMALIZE);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 166, (byte) 156, (byte) 188);
		gl.glVertex3f(400.0f, -150.0f, -200.0f);
		gl.glVertex3f(-400.0f, -150.0f, -200.0f);
		gl.glVertex3f(-400.0f, -150.0f, 200.0f);
		gl.glVertex3f(400.0f, -150.0f, 200.0f);
		gl.glEnd();

		gl.glPushMatrix();

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		drawJet(false);

		gl.glPopMatrix();

		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_LIGHTING);
		
		gl.glPushMatrix();

		gl.glMultMatrixf(shadowMat, 0);

		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		drawJet(true);

		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(lightPos.get(0), lightPos.get(1), lightPos.get(2));
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		glut.glutSolidSphere(5.0f, 10, 10);
		gl.glPopMatrix();

		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glFlush();
	}

	private void drawJet(boolean shadow) {
		float[] vNormal = new float[3];
		if (shadow)
			gl.glColor3ub((byte) 44, (byte) 44, (byte) 44);
		else
			gl.glColor3ub((byte) 111, (byte) 122, (byte) 133);
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 60.0f);
		gl.glVertex3f(-15.0f, 0.0f, 30.0f);
		gl.glVertex3f(15.0f, 0.0f, 30.0f);

		{
			float[][] vPoints = { { 15.0f, 0.0f, 30.0f },
					{ 0.0f, 15.0f, 30.0f }, { 0.0f, 0.0f, 60.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 0.0f, 0.0f, 60.0f },
					{ 0.0f, 15.0f, 30.0f }, { -15.0f, 0.0f, 30.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { -15.0f, 0.0f, 30.0f },
					{ 0.0f, 15.0f, 30.0f }, { 0.0f, 0.0f, -56.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 0.0f, 0.0f, -56.0f },
					{ 0.0f, 15.0f, 30.0f }, { 15.0f, 0.0f, 30.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glVertex3f(15.0f, 0.0f, 30.0f);
		gl.glVertex3f(-15.0f, 0.0f, 30.0f);
		gl.glVertex3f(0.0f, 0.0f, -56.0f);

		{
			float[][] vPoints = { { 0.0f, 2.0f, 27.0f },
					{ -60.0f, 2.0f, -8.0f }, { 60.0f, 2.0f, -8.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 60.0f, 2.0f, -8.0f },
					{ 0.0f, 7.0f, -8.0f }, { 0.0f, 2.0f, 27.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 60.0f, 2.0f, -8.0f },
					{ -60.0f, 2.0f, -8.0f }, { 0.0f, 7.0f, -8.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 0.0f, 2.0f, 27.0f }, { 0.0f, 7.0f, -8.0f },
					{ -60.0f, 2.0f, -8.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glVertex3f(-30.0f, -0.50f, -57.0f);
		gl.glVertex3f(30.0f, -0.50f, -57.0f);
		gl.glVertex3f(0.0f, -0.50f, -40.0f);

		{
			float[][] vPoints = { { 0.0f, -0.5f, -40.0f },
					{ 30.0f, -0.5f, -57.0f }, { 0.0f, 4.0f, -57.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 0.0f, 4.0f, -57.0f },
					{ -30.0f, -0.5f, -57.0f }, { 0.0f, -0.5f, -40.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 30.0f, -0.5f, -57.0f },
					{ -30.0f, -0.5f, -57.0f }, { 0.0f, 4.0f, -57.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 0.0f, 0.5f, -40.0f },
					{ 3.0f, 0.5f, -57.0f }, { 0.0f, 25.0f, -65.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 0.0f, 25.0f, -65.0f },
					{ -3.0f, 0.5f, -57.0f }, { 0.0f, 0.5f, -40.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}

		{
			float[][] vPoints = { { 3.0f, 0.5f, -57.0f },
					{ -3.0f, 0.5f, -57.0f }, { 0.0f, 25.0f, -65.0f } };

			Math3d.m3dFindNormal(vNormal, vPoints[0], vPoints[1], vPoints[2]);
			gl.glNormal3fv(vNormal, 0);
			gl.glVertex3fv(vPoints[0], 0);
			gl.glVertex3fv(vPoints[1], 0);
			gl.glVertex3fv(vPoints[2], 0);
		}
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		DefaultImpl
				.reshapWithPerspective(drawable, width, height, 60, 200, 500);
		gl.glTranslatef(0.0f, 0.0f, -400.0f);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);
	}
}
