package xdean.OpenGLSuperBible.chapter05;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.chapter04.App4_07_SphereWorld;
import xdean.OpenGLSuperBible.share.Math3d;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App5_09_SphereWorld2 extends App4_07_SphereWorld {

	public static void main(String[] args) {
		new App5_09_SphereWorld2().setVisible(true);
	}

	protected static final int NUM_SPHERES = 20;

	protected FloatBuffer fLightPos = FloatBuffer.wrap(new float[] { -100.0f, 100.0f,
			50.0f, 1.0f });
	protected FloatBuffer fNoLight = FloatBuffer.wrap(new float[] { 0.0f, 0.0f, 0.0f,
			0.0f });
	protected FloatBuffer fLowLight = FloatBuffer.wrap(new float[] { 0.25f, 0.25f, 0.25f,
			1.0f });
	protected FloatBuffer fBrightLight = FloatBuffer.wrap(new float[] { 1.0f, 1.0f, 1.0f,
			1.0f });

	protected float[] mShadowMatrix = new float[16];

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		gl.glClearColor(fLowLight.get(0), fLowLight.get(1), fLowLight.get(2),
				fLowLight.get(3));

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glCullFace(GL2.GL_BACK);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_DEPTH_TEST);


		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, fNoLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fNoLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fBrightLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fBrightLight);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		float[] planeEquation = new float[4];
		Math3d.m3dGetPlaneEquation(planeEquation, new float[] { 0, -0.4f, 0 },
				new float[] { 10, -0.4f, 0 }, new float[] { 5, -0.4f, -5 });
		Math3d.m3dMakePlanarShadowMatrix(mShadowMatrix, planeEquation,
				fLightPos.array());

		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		frameCamera.ApplyCameraTransform();

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPos);

		drawGround();

		drawWorld();

		gl.glPopMatrix();

		gl.glFlush();
	}

	@Override
	protected void drawGround() {
		float fExtent = 20.0f;
		float fStep = 1.0f;
		float y = -0.4f;

		gl.glColor3f(0.60f, .40f, .10f);
		for (int iStrip = (int) -fExtent; iStrip <= fExtent; iStrip += fStep) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			gl.glNormal3f(0.0f, 1.0f, 0.0f);
			for (int iRun = (int) fExtent; iRun >= -fExtent; iRun -= fStep) {
				gl.glVertex3f(iStrip, y, iRun);
				gl.glVertex3f(iStrip + fStep, y, iRun);
			}
			gl.glEnd();
		}
	}

	@Override
	protected void drawWorld() {
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glPushMatrix();
		gl.glMultMatrixf(mShadowMatrix, 0);
		drawShadow();
		gl.glPopMatrix();
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		drawSpheres();
	}

	@Override
	protected void drawSpheres() {
		drawInhabitants(false);
	}

	protected void drawShadow() {
		drawInhabitants(true);
	}

	protected void drawInhabitants(boolean nShadow) {

		if (nShadow)
			gl.glColor3f(0.0f, 0.0f, 0.0f);
		else {
			yRot += 0.5f;
			gl.glColor3f(0.0f, 1.0f, 0.0f);
		}

		for (int i = 0; i < NUM_SPHERES; i++) {
			gl.glPushMatrix();
			spheres[i].ApplyActorTransform();
			glut.glutSolidSphere(0.3f, 25, 15);
			gl.glPopMatrix();
		}

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.1f, -2.5f);

		if (nShadow == false)
			gl.glColor3f(0.0f, 0.0f, 1.0f);

		gl.glPushMatrix();
		gl.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(1.0f, 0.0f, 0.0f);
		glut.glutSolidSphere(0.1f, 17, 9);
		gl.glPopMatrix();

		if (nShadow == false) {
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fBrightLight);
		}

		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		glt.gltDrawTorus(0.35f, 0.15f, 61, 37);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fNoLight);
		gl.glPopMatrix();
	}
}
